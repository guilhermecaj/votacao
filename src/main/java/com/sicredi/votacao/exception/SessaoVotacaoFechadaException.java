package com.sicredi.votacao.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class SessaoVotacaoFechadaException extends RuntimeException {
    public SessaoVotacaoFechadaException(String message) {
        super(message);
    }
}
