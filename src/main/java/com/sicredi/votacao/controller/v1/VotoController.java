package com.sicredi.votacao.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sicredi.votacao.exception.BadRequestException;
import com.sicredi.votacao.exception.CpfNaoAutorizadoException;
import com.sicredi.votacao.exception.ResourceNotFoundException;
import com.sicredi.votacao.exception.SessaoVotacaoFechadaException;
import com.sicredi.votacao.exception.VotoDuplicadoException;

import com.sicredi.votacao.model.SessaoVotacao;
import com.sicredi.votacao.model.Voto;
import com.sicredi.votacao.repository.SessaoVotacaoRepository;
import com.sicredi.votacao.repository.VotoRepository;
import com.sicredi.votacao.service.CpfService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/votos")
public class VotoController {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;   
    
    @Autowired
    private CpfService cpfService;

    @GetMapping   
    public List<Voto> getAllVotos() {
        return votoRepository.findAll();
    }

    @GetMapping("/{id}")    
    public ResponseEntity<Voto> getVotoById(@PathVariable UUID id) {
        return votoRepository.findById(id)
                .map(voto -> ResponseEntity.ok().body(voto))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping    
    public Voto createVoto(@RequestBody Voto voto) {

        if(voto.getAssociadoId() == null){
            throw new BadRequestException("ID do associado não informado.");
        }

        if (voto.getCpf() == null) {
            throw new BadRequestException("CPF do associado não informado.");
        }

        if (voto.getVoto() == null) {
            throw new BadRequestException("Voto do associado não informado.");
        }

        SessaoVotacao sessaoVotacao = voto.getSessaoVotacao();

        // Verifica se a sessao de votação foi informada
        if (sessaoVotacao == null) {
            throw new BadRequestException("Sessão de votação não informada.");
        }

        sessaoVotacao = sessaoVotacaoRepository.findById(sessaoVotacao.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sessão de Votação não encotrada com o id " + voto.getSessaoVotacao().getId()));

        // Verifica se a sessão está aberta
        if (!sessaoVotacao.isSessaoAberta()) {
            throw new SessaoVotacaoFechadaException("A sessão de votação não está aberta.");
        }

        // Verifica pelo CPF do associado se ele pode votar para votar
        if (!cpfService.isAbleToVote(voto.getCpf())) {
            throw new CpfNaoAutorizadoException("O associado não está autorizado a votar.");
        }

        // Verifica se o associado já votou em qualquer sessão de votação da pauta
        boolean jaVotou = votoRepository.existsByAssociadoIdAndPautaId(voto.getAssociadoId(),
                sessaoVotacao.getPauta().getId());

        if (jaVotou) {
            throw new VotoDuplicadoException("O associado já votou nesta pauta.");
        }               

        return votoRepository.save(voto);

    }

}
