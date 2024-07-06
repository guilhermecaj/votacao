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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/sessoesvotacao")
@Tag(name = "Sessões de votação", description = "API para gerenciamento de sessões de votação em pautas de assembléias")
public class SessaoVotacaoController {

     @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @GetMapping
    @Operation(summary = "Listar todas as sessões de votação", description = "Retorna uma lista de todas as sessões de votação em pautas de assembléias")
    public List<SessaoVotacao> getAllSessaoVotacao() {
        return sessaoVotacaoRepository.findAll();
    }   
    
    @GetMapping("/{id}/votos")
    @Operation(summary = "Consultar votos de uma sessões de votação", description = "Retorna todos os votos associados a uma sessao de votação específica com base no ID fornecido") 
    public ResponseEntity<List<Voto>> getSessoesByPautaId(@PathVariable UUID id) {
        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sessao de votação não encontrada com o id " + id));
        return ResponseEntity.ok(sessaoVotacao.getVotos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma sessão de votação pelo ID", description = "Retorna uma sessão de votação específica com base no ID fornecido")
    public ResponseEntity<SessaoVotacao> getSessaoVotacaoById(@PathVariable UUID  id) {
        return sessaoVotacaoRepository.findById(id)
                .map(sessaoVotacao -> ResponseEntity.ok().body(sessaoVotacao))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar uma nova sessão de votação", description = "Cria uma nova sessão de votação com as informações fornecidas") 
    public SessaoVotacao createSessaoVotacao(@RequestBody SessaoVotacao sessaoVotacao) {
        return sessaoVotacaoRepository.save(sessaoVotacao);
    }    

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma sessão de votação", description = "Exclui uma sessão de votação específica com base no ID fornecido")  
    public void deleteSessaoVotacao(@PathVariable UUID id) {
        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sessão de Votação não encontrada com o id " + id));

        sessaoVotacaoRepository.delete(sessaoVotacao);
    }

}
