package com.sicredi.votacao.controller.v1;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sicredi.votacao.model.Pauta;
import com.sicredi.votacao.model.SessaoVotacao;
import com.sicredi.votacao.model.Voto;
import com.sicredi.votacao.model.enums.VotoEnum;
import com.sicredi.votacao.repository.SessaoVotacaoRepository;
import com.sicredi.votacao.repository.VotoRepository;
import com.sicredi.votacao.service.CpfService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(VotoController.class)
public class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotoRepository votoRepository;

    @MockBean
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @MockBean
    private CpfService cpfService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test  
    public void testGetAllVotos() throws Exception {
        Voto voto1 = new Voto();
        voto1.setId(UUID.randomUUID());
        voto1.setVoto(VotoEnum.SIM);
        voto1.setAssociadoId("associado1");
        voto1.setCpf("12345678901");
        voto1.setData(LocalDateTime.now());

        Voto voto2 = new Voto();
        voto2.setId(UUID.randomUUID());
        voto2.setVoto(VotoEnum.NAO);
        voto2.setAssociadoId("associado2");
        voto2.setCpf("09876543210");
        voto2.setData(LocalDateTime.now());

        List<Voto> votos = Arrays.asList(voto1, voto2);

        when(votoRepository.findAll()).thenReturn(votos);

        mockMvc.perform(get("/api/v1/votos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].voto", is(voto1.getVoto().toString())))
            .andExpect(jsonPath("$[1].voto", is(voto2.getVoto().toString())));

        verify(votoRepository, times(1)).findAll();
    }

    @Test
    public void testGetVotoById() throws Exception {
        UUID votoId = UUID.randomUUID();
        Voto voto = new Voto();
        voto.setId(votoId);
        voto.setVoto(VotoEnum.SIM);
        voto.setAssociadoId("associado1");
        voto.setData(LocalDateTime.now());

        when(votoRepository.findById(votoId)).thenReturn(Optional.of(voto));

        mockMvc.perform(get("/api/v1/votos/{id}", votoId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.voto", is(voto.getVoto().toString())));

        verify(votoRepository, times(1)).findById(votoId);
    }

    @Test
    public void testCreateVotoCpfAutorizado() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setId(sessaoId);
        sessao.setDataAbertura(LocalDateTime.now().minusMinutes(30));
        sessao.setDuracao(60);

        Pauta pauta = new Pauta();
        pauta.setId(UUID.randomUUID());
        pauta.setDescricao("Pauta Teste");

        // Associar a Pauta à SessaoVotacao
        sessao.setPauta(pauta);

        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));
        when(sessaoVotacaoRepository.existsById(sessaoId)).thenReturn(true);

        Voto voto = new Voto();
        voto.setId(UUID.randomUUID());
        voto.setVoto(VotoEnum.SIM);
        voto.setAssociadoId("associado1");
        voto.setCpf("09876543210");
        voto.setData(LocalDateTime.now());
        voto.setSessaoVotacao(sessao);

        when(votoRepository.save(any(Voto.class))).thenReturn(voto);
        when(votoRepository.existsByAssociadoIdAndPautaId(any(String.class), any(UUID.class))).thenReturn(false);
        when(cpfService.isAbleToVote(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/v1/votos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.voto").value(voto.getVoto().toString()));

        verify(votoRepository, times(1)).save(any(Voto.class));
    }

    @Test
    public void testCreateVotoCpfNaoAutorizado() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setId(sessaoId);
        sessao.setDataAbertura(LocalDateTime.now().minusMinutes(30));
        sessao.setDuracao(60);

        Pauta pauta = new Pauta();
        pauta.setId(UUID.randomUUID());
        pauta.setDescricao("Pauta Teste");

        // Associar a Pauta à SessaoVotacao
        sessao.setPauta(pauta);

        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));
        when(sessaoVotacaoRepository.existsById(sessaoId)).thenReturn(true);

        Voto voto = new Voto();
        voto.setId(UUID.randomUUID());
        voto.setVoto(VotoEnum.SIM);
        voto.setAssociadoId("associado1");
        voto.setCpf("09876543210");
        voto.setData(LocalDateTime.now());
        voto.setSessaoVotacao(sessao);

        when(cpfService.isAbleToVote(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/v1/votos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voto)))
                .andExpect(status().isBadRequest());                

        verify(votoRepository, never()).save(any(Voto.class));
    }   
    
}

