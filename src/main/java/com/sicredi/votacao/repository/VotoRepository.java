package com.sicredi.votacao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sicredi.votacao.model.Voto;

public interface VotoRepository extends JpaRepository<Voto, UUID> {

    @Query("SELECT COUNT(v) > 0 FROM Voto v WHERE v.associadoId = :associadoId AND v.sessaoVotacao.pauta.id = :pautaId")
    boolean existsByAssociadoIdAndPautaId(@Param("associadoId") String associadoId, @Param("pautaId") UUID pautaId);

}

