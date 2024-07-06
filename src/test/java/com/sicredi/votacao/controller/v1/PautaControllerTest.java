package com.sicredi.votacao.controller.v1;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sicredi.votacao.model.Pauta;
import com.sicredi.votacao.model.SessaoVotacao;
import com.sicredi.votacao.repository.PautaRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PautaController.class)
public class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaRepository pautaRepository;    

    @Test    
    public void testGetAllPautas() throws Exception {
        Pauta pauta1 = new Pauta();
        pauta1.setId(UUID.randomUUID());
        pauta1.setDescricao("Pauta 1");

        Pauta pauta2 = new Pauta();
        pauta2.setId(UUID.randomUUID());
        pauta2.setDescricao("Pauta 2");

        List<Pauta> pautas = Arrays.asList(pauta1, pauta2);

        when(pautaRepository.findAll()).thenReturn(pautas);

        mockMvc.perform(get("/api/v1/pautas"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].descricao", is(pauta1.getDescricao())))
            .andExpect(jsonPath("$[1].descricao", is(pauta2.getDescricao())));

        verify(pautaRepository, times(1)).findAll();
    }

    @Test
    public void testGetSessoesByPautaId() throws Exception {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta();
        pauta.setId(pautaId);
        pauta.setDescricao("Pauta 1");

        LocalDateTime dataAbertura = LocalDateTime.now();

        SessaoVotacao sessao1 = new SessaoVotacao();
        sessao1.setId(UUID.randomUUID());
        sessao1.setDataAbertura(dataAbertura);
        sessao1.setDuracao(30);        

        SessaoVotacao sessao2 = new SessaoVotacao();
        sessao2.setId(UUID.randomUUID());
        sessao2.setDataAbertura(dataAbertura);
        sessao2.setDuracao(120); 

        pauta.setSessoesVotacao(Arrays.asList(sessao1, sessao2));

        when(pautaRepository.findById(pautaId)).thenReturn(java.util.Optional.of(pauta));

        mockMvc.perform(get("/api/v1/pautas/{id}/sessoesvotacao", pautaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].dataAbertura").value(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(sessao1.getDataAbertura())))
                .andExpect(jsonPath("$[0].duracao").value(sessao1.getDuracao()))
                .andExpect(jsonPath("$[1].dataAbertura").value(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(sessao2.getDataAbertura())))
                .andExpect(jsonPath("$[1].duracao").value(sessao2.getDuracao()));
                

        verify(pautaRepository).findById(pautaId);
    }

    @Test    
    public void testGetPautaById() throws Exception {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta();
        pauta.setId(pautaId);
        pauta.setDescricao("Pauta 1");

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        mockMvc.perform(get("/api/v1/pautas/{id}", pautaId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.descricao", is(pauta.getDescricao())));

        verify(pautaRepository, times(1)).findById(pautaId);
    }

    @Test   
    public void testCreatePauta() throws Exception {
        Pauta pauta = new Pauta();
        pauta.setDescricao("Nova Pauta");

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        mockMvc.perform(post("/api/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descricao\": \"Nova Pauta\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descricao", is(pauta.getDescricao())));

        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    public void testUpdatePauta() throws Exception {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta();
        pauta.setId(pautaId);
        pauta.setDescricao("Pauta Original");

        Pauta novosValoresPauta = new Pauta();
        novosValoresPauta.setDescricao("Pauta Atualizada");

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        mockMvc.perform(put("/api/v1/pautas/{id}", pautaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"descricao\": \"Pauta Atualizada\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descricao", is(novosValoresPauta.getDescricao())));

        verify(pautaRepository, times(1)).findById(pautaId);
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    public void testDeletePauta() throws Exception {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta();
        pauta.setId(pautaId);
        pauta.setDescricao("Pauta a ser deletada");

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        mockMvc.perform(delete("/api/v1/pautas/{id}", pautaId))
            .andExpect(status().isOk());

        verify(pautaRepository, times(1)).findById(pautaId);
        verify(pautaRepository, times(1)).delete(pauta);
    }
}
