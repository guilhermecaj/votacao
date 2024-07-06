package com.sicredi.votacao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sicredi.votacao.model.SessaoVotacao;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, UUID> {

}
