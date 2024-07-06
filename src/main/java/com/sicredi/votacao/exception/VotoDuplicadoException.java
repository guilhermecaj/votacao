package com.sicredi.votacao.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT )
public class VotoDuplicadoException extends RuntimeException {
    public VotoDuplicadoException(String message) {
        super(message);
    }
}
