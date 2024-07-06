package com.sicredi.votacao.controller.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sicredi.votacao.exception.ResourceNotFoundException;
import com.sicredi.votacao.model.Pauta;
import com.sicredi.votacao.model.SessaoVotacao;
import com.sicredi.votacao.repository.PautaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pautas")
@Tag(name = "Pauta", description = "API para gerenciamento de pautas de assembléias")
public class PautaController {

    @Autowired
    private PautaRepository pautaRepository;    
  
    @GetMapping
    @Operation(summary = "Listar todas as pautas", description = "Retorna uma lista de todas as pautas da assembléia")   
    public List<Pauta> getAllPautas() {
        return pautaRepository.findAll();
    }

    @GetMapping("/{id}/sessoesvotacao")
    @Operation(summary = "Consultar sessões de votação de uma pauta", description = "Retorna todas as sessões de votação associadas a uma pauta específica com base no ID fornecido")
    public ResponseEntity<List<SessaoVotacao>> getSessoesByPautaId(@PathVariable UUID id) {
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada com o id " + id));
        return ResponseEntity.ok(pauta.getSessoesVotacao());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma pauta pelo ID", description = "Retorna uma pauta específica com base no ID fornecido")  
    public ResponseEntity<Pauta> getPautaById(@PathVariable UUID  id) {
        return pautaRepository.findById(id)
                .map(pauta -> ResponseEntity.ok().body(pauta))        
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada com o id  " + id));
    }

  
    @PostMapping
    @Operation(summary = "Criar uma nova pauta", description = "Cria uma nova pauta com a descrição fornecida") 
    public Pauta createPauta(@RequestBody Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar a descrição de uma pauta", description = "Atualiza a descrição de uma pauta específica com base no ID fornecido")
    public Pauta updatePauta(@PathVariable UUID id, @RequestBody Pauta novosValoresPauta) {
        Pauta pauta = pautaRepository.findById(id)
                                    .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada com o id  " + id));

        pauta.setDescricao(novosValoresPauta.getDescricao());
        
        return pautaRepository.save(pauta);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma pauta", description = "Exclui uma pauta específica com base no ID fornecido") 
    public void deletePauta(@PathVariable UUID id) {
        Pauta pauta = pautaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada com o id " + id));

        pautaRepository.delete(pauta);
    }

}
