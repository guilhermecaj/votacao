package com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST )
public class CpfNaoAutorizadoException extends RuntimeException {
    public CpfNaoAutorizadoException(String message) {
        super(message);
    }
}
