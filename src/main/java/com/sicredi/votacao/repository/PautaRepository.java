package com.sicredi.votacao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sicredi.votacao.model.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, UUID> {

}
