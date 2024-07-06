package com.sicredi.votacao.controller.v1;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sicredi.votacao.model.SessaoVotacao;
import com.sicredi.votacao.model.Voto;
import com.sicredi.votacao.model.enums.VotoEnum;
import com.sicredi.votacao.repository.SessaoVotacaoRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SessaoVotacaoController.class)
public class SessaoVotacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Test
    public void testGetAllSessaoVotacao() throws Exception {
        SessaoVotacao sessao1 = new SessaoVotacao();
        sessao1.setId(UUID.randomUUID());
        sessao1.setDataAbertura(LocalDateTime.now());
        sessao1.setDuracao(60);

        SessaoVotacao sessao2 = new SessaoVotacao();
        sessao2.setId(UUID.randomUUID());
        sessao2.setDataAbertura(LocalDateTime.now());
        sessao2.setDuracao(30);

        List<SessaoVotacao> sessoes = Arrays.asList(sessao1, sessao2);

        when(sessaoVotacaoRepository.findAll()).thenReturn(sessoes);

        mockMvc.perform(get("/api/v1/sessoesvotacao"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].duracao", is(sessao1.getDuracao())))
            .andExpect(jsonPath("$[1].duracao", is(sessao2.getDuracao())));

        verify(sessaoVotacaoRepository, times(1)).findAll();
    }

    @Test
    public void testGetVotosBySessaoVotacaoId() throws Exception {
        UUID sessaoVotacaoId = UUID.randomUUID();
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setId(sessaoVotacaoId);
        sessaoVotacao.setDataAbertura(LocalDateTime.now());
        sessaoVotacao.setDuracao(60);        

        Voto voto1 = new Voto();
        voto1.setId(UUID.randomUUID());
        voto1.setSessaoVotacao(sessaoVotacao);
        voto1.setAssociadoId("associado1");
        voto1.setVoto(VotoEnum.SIM);

        Voto voto2 = new Voto();
        voto2.setId(UUID.randomUUID());
        voto2.setSessaoVotacao(sessaoVotacao);
        voto2.setAssociadoId("associado2");
        voto2.setVoto(VotoEnum.NAO);

        sessaoVotacao.setVotos(Arrays.asList(voto1, voto2));

        when(sessaoVotacaoRepository.findById(sessaoVotacaoId)).thenReturn(java.util.Optional.of(sessaoVotacao));

        mockMvc.perform(get("/api/v1/sessoesvotacao/{id}/votos", sessaoVotacaoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].associadoId").value(voto1.getAssociadoId().toString()))
                .andExpect(jsonPath("$[1].associadoId").value(voto2.getAssociadoId().toString()))
                .andExpect(jsonPath("$[0].voto").value(voto1.getVoto().toString())) // Verifica o enum como string
                .andExpect(jsonPath("$[1].voto").value(voto2.getVoto().toString())); // Verifica o enum como string

        verify(sessaoVotacaoRepository).findById(sessaoVotacaoId);
    }

    @Test  
    public void testGetSessaoVotacaoById() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setId(sessaoId);
        sessao.setDataAbertura(LocalDateTime.now());
        sessao.setDuracao(60);

        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

        mockMvc.perform(get("/api/v1/sessoesvotacao/{id}", sessaoId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.duracao", is(sessao.getDuracao())));

        verify(sessaoVotacaoRepository, times(1)).findById(sessaoId);
    }

    @Test
    public void testCreateSessaoVotacao() throws Exception {
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setDataAbertura(LocalDateTime.now());
        sessao.setDuracao(60);

        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessao);

        mockMvc.perform(post("/api/v1/sessoesvotacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataAbertura\": \"" + LocalDateTime.now().toString() + "\", \"duracao\": 60}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.duracao", is(sessao.getDuracao())));

        verify(sessaoVotacaoRepository, times(1)).save(any(SessaoVotacao.class));
    }

    @Test
    public void testDeleteSessaoVotacao() throws Exception {
        UUID sessaoId = UUID.randomUUID();
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setId(sessaoId);
        sessao.setDataAbertura(LocalDateTime.now());
        sessao.setDuracao(60);

        when(sessaoVotacaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

        mockMvc.perform(delete("/api/v1/sessoesvotacao/{id}", sessaoId))
            .andExpect(status().isOk());

        verify(sessaoVotacaoRepository, times(1)).findById(sessaoId);
        verify(sessaoVotacaoRepository, times(1)).delete(sessao);
    }
}
