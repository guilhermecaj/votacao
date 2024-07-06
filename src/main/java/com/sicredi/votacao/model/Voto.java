package com.sicredi.votacao.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sicredi.votacao.model.enums.VotoEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;    
    
    @Enumerated(EnumType.STRING)
    private VotoEnum voto; 

    @ManyToOne
    @JoinColumn(name = "sessao_votacao_id")
    private SessaoVotacao sessaoVotacao;        

    private String associadoId;   

    private LocalDateTime data;    

    private String cpf;    

    public Voto() {
        this.data = LocalDateTime.now();
    }  

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }   

    public VotoEnum getVoto() {
        return voto;
    }

    public void setVoto(VotoEnum voto) {
        this.voto = voto;
    }

    public SessaoVotacao getSessaoVotacao() {
        return sessaoVotacao;
    }

    public void setSessaoVotacao(SessaoVotacao sessaoVotacao) {
        this.sessaoVotacao = sessaoVotacao;
    }

    public String getAssociadoId() {
        return associadoId;
    }

    public void setAssociadoId(String associadoId) {
        this.associadoId = associadoId;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Voto{" +
                "id='" + id   + '\'' + 
                ", voto='" + voto + '\'' +  
                ", associadoId='" + associadoId + '\'' + 
                ", data=" + data +
                ", cpf='" + cpf + '\'' + 
                '}';
    }    
    
}