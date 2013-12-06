/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Aparecida
 */
@Entity
@Table(name = "tb_veiculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Veiculo.findAll", query = "SELECT v FROM Veiculo v"),
    @NamedQuery(name = "Veiculo.findByPlacaVeiculo", query = "SELECT v FROM Veiculo v WHERE v.placaVeiculo = :placaVeiculo"),
    @NamedQuery(name = "Veiculo.findByNomeMotorista", query = "SELECT v FROM Veiculo v WHERE v.nomeMotorista = :nomeMotorista"),
    @NamedQuery(name = "Veiculo.findByTipoVeiculo", query = "SELECT v FROM Veiculo v WHERE v.tipoVeiculo = :tipoVeiculo"),})

public class Veiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "placa_veiculo")
    private String placaVeiculo;
    @Column(name = "nome_motorista")
    private String nomeMotorista;
    @Column(name = "tipo_veiculo")
    private String tipoVeiculo;
    @Column(name = "senha")
    private String senha;
    @OneToMany(mappedBy = "placaVeiculo")
    private List<Entrega> entregaList;

    public Veiculo() {
    }

    public Veiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }    

    @XmlTransient
    public List<Entrega> getEntregaList() {
        return entregaList;
    }

    public void setEntregaList(List<Entrega> entregaList) {
        this.entregaList = entregaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (placaVeiculo != null ? placaVeiculo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Veiculo)) {
            return false;
        }
        Veiculo other = (Veiculo) object;
        if ((this.placaVeiculo == null && other.placaVeiculo != null) || (this.placaVeiculo != null && !this.placaVeiculo.equals(other.placaVeiculo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.auadottonizaidem.dao.Veiculo[ placaVeiculo=" + placaVeiculo + " ]";
    }

}