package com.sicredi.votacao.model.enums;

public enum ResultadoEnum {
    NAO_INICIADO("Não iniciado"),
    EMPATADO("Empatado"),
    APROVADO("Aprovado"),
    REPROVADO("Reprovado");

    private final String descricao;

    ResultadoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}