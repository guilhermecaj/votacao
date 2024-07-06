package com.sicredi.votacao.service;

import com.sicredi.votacao.dto.CpfResponseDTO;

import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class CpfService {

    private static final String[] CPFS_LIBERADOS = {
        "12345678901", "09876543210", "11223344556", "66778899000"
    };

    private final RestTemplate restTemplate = new RestTemplate();    

    public boolean isAbleToVote(String cpf) {

        /* RETIRAR ESSE IF QUANDO O ENDPOINT ESTIVER FUNCIONAL */
        if(Arrays.asList(CPFS_LIBERADOS).contains(cpf)){
            return true;
        }       
        
        String url = "https://user-info.herokuapp.com/users/" + cpf;

        try {
            CpfResponseDTO response = restTemplate.getForObject(url, CpfResponseDTO.class);
            if(response != null){
                return "ABLE_TO_VOTE".equals(response.getStatus());
            }   
            return false;            
        } catch (HttpClientErrorException.NotFound e) {
            //Erros da chamada externa irão retornar como UNABLE_TO_VOTE
            return false;
            
        }
    }
}