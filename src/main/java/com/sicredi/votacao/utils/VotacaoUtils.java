package com.sicredi.votacao.utils;

import com.sicredi.votacao.model.enums.ResultadoEnum;

public class VotacaoUtils {
    public static ResultadoEnum calcularResultado(int totalVotos, int totalVotosSim, int totalVotosNao) {
        if (totalVotos == 0) {
            return ResultadoEnum.NAO_INICIADO;
        } else if (totalVotosSim == totalVotosNao) {
            return ResultadoEnum.EMPATADO;
        } else if (totalVotosSim > totalVotosNao) {
            return ResultadoEnum.APROVADO;
        } else {
            return ResultadoEnum.REPROVADO;
        }
    }
}
