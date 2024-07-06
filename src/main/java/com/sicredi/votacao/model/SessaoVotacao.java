package com.sicredi.votacao.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sicredi.votacao.model.enums.VotoEnum;
import com.sicredi.votacao.utils.VotacaoUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDateTime dataAbertura;

    private int duracao; // duração em minutos

    @ManyToOne
    @JoinColumn(name = "pauta_id")  
    private Pauta pauta;       

    @OneToMany(mappedBy = "sessaoVotacao")
    @JsonIgnore
    private List<Voto> votos;  
 
    public SessaoVotacao() {
        this.duracao = 1;
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {      
        this.duracao = duracao;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        this.pauta = pauta;
    }

    public List<Voto> getVotos() {
        return votos;
    }

    public void setVotos(List<Voto> votos) {
        this.votos = votos;
    }

    public int getTotalVotos() {
        return votos != null ? votos.size() : 0;     
    }  

    public int getTotalVotosNao() {
        if (votos == null) {
            return 0;
        }
        return (int) votos.stream().filter(voto -> voto.getVoto() == VotoEnum.NAO).count();
    }

    public int getTotalVotosSim() {
        if (votos == null) {
            return 0;
        }
        return (int) votos.stream().filter(voto -> voto.getVoto() == VotoEnum.SIM).count();
    }   
    
    public String getResultado() {
        
        return VotacaoUtils.calcularResultado(getTotalVotos(), getTotalVotosSim(), getTotalVotosNao()).getDescricao(); 
        
    }    
    
    public LocalDateTime getDataFim() {   
        if (dataAbertura == null) {
            return null;
        }           
       return dataAbertura.plusMinutes(duracao);
    }
  
    public boolean isSessaoAberta() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataFim = getDataFim();
        return dataAbertura != null && dataFim != null && agora.isAfter(dataAbertura) && agora.isBefore(dataFim);
    }

    @Override
    public String toString() {
        return "SessaoVotacao{" +
                "id='" + id   + '\'' + 
                ", dataAbertura=" + dataAbertura + 
                ", duracao=" + duracao + 
                '}';
    }
    
}

