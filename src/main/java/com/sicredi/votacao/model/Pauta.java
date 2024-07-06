package com.sicredi.votacao.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sicredi.votacao.utils.VotacaoUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Pauta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)   
    private UUID id;
    
    private String descricao;

    @OneToMany(mappedBy = "pauta")  
    @JsonIgnore
    private List<SessaoVotacao> sessoesVotacao;        

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }    

    public List<SessaoVotacao> getSessoesVotacao() {
        return sessoesVotacao;
    }

    public void setSessoesVotacao(List<SessaoVotacao> sessoesVotacao) {
        this.sessoesVotacao = sessoesVotacao;
    }

    public int getTotalVotos() {
        if (sessoesVotacao == null) {
            return 0;
        }
        return sessoesVotacao.stream().mapToInt(SessaoVotacao::getTotalVotos).sum();        
    }   

    public int getTotalVotosNao() {
        if (sessoesVotacao == null) {
            return 0;
        }
        return sessoesVotacao.stream().mapToInt(SessaoVotacao::getTotalVotosNao).sum();
    }   

    public int getTotalVotosSim() {
        if (sessoesVotacao == null) {
            return 0;
        }
        return sessoesVotacao.stream().mapToInt(SessaoVotacao::getTotalVotosSim).sum();
    }     

    public String getResultado() {
        return VotacaoUtils.calcularResultado(getTotalVotos(), getTotalVotosSim(), getTotalVotosNao()).getDescricao();       
    }         

    @Override
    public String toString() {
        return "Pauta{" +
                "id='" + id   + '\'' + 
                ", descricao='" + descricao + '\'' +                
                '}';
    }

}
