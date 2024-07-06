package com.sicredi.votacao.controller.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sicredi.votacao.exception.ResourceNotFoundException;

import com.sicredi.votacao.model.SessaoVotacao;
import com.sicredi.votacao.model.Voto;
import com.sicredi.votacao.repository.SessaoVotacaoRepository;

@RestController
@RequestMapping("/api/v1/sessoesvotacao")
public class SessaoVotacaoController {

     @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @GetMapping   
    public List<SessaoVotacao> getAllSessaoVotacao() {
        return sessaoVotacaoRepository.findAll();
    }   
    
    @GetMapping("/{id}/votos")    
    public ResponseEntity<List<Voto>> getSessoesByPautaId(@PathVariable UUID id) {
        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sessao de votação não encontrada com o id " + id));
        return ResponseEntity.ok(sessaoVotacao.getVotos());
    }

    @GetMapping("/{id}")    
    public ResponseEntity<SessaoVotacao> getSessaoVotacaoById(@PathVariable UUID  id) {
        return sessaoVotacaoRepository.findById(id)
                .map(sessaoVotacao -> ResponseEntity.ok().body(sessaoVotacao))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping    
    public SessaoVotacao createSessaoVotacao(@RequestBody SessaoVotacao sessaoVotacao) {
        return sessaoVotacaoRepository.save(sessaoVotacao);
    }    

    @DeleteMapping("/{id}")   
    public void deleteSessaoVotacao(@PathVariable UUID id) {
        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sessão de Votação não encontrada com o id " + id));

        sessaoVotacaoRepository.delete(sessaoVotacao);
    }

}
