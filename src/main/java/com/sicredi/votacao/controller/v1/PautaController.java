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

@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

    @Autowired
    private PautaRepository pautaRepository;    
  
    @GetMapping    
    public List<Pauta> getAllPautas() {
        return pautaRepository.findAll();
    }

    @GetMapping("/{id}/sessoesvotacao")    
    public ResponseEntity<List<SessaoVotacao>> getSessoesByPautaId(@PathVariable UUID id) {
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada com o id " + id));
        return ResponseEntity.ok(pauta.getSessoesVotacao());
    }

    @GetMapping("/{id}")    
    public ResponseEntity<Pauta> getPautaById(@PathVariable UUID  id) {
        return pautaRepository.findById(id)
                .map(pauta -> ResponseEntity.ok().body(pauta))        
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada com o id  " + id));
    }

  
    @PostMapping    
    public Pauta createPauta(@RequestBody Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    @PutMapping("/{id}")    
    public Pauta updatePauta(@PathVariable UUID id, @RequestBody Pauta novosValoresPauta) {
        Pauta pauta = pautaRepository.findById(id)
                                    .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada com o id  " + id));

        pauta.setDescricao(novosValoresPauta.getDescricao());
        
        return pautaRepository.save(pauta);
    }

    @DeleteMapping("/{id}")   
    public void deletePauta(@PathVariable UUID id) {
        Pauta pauta = pautaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada com o id " + id));

        pautaRepository.delete(pauta);
    }

}
