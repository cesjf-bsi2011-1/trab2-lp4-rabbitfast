/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "tb_localidade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Localidade.findAll", query = "SELECT l FROM Localidade l"),
    @NamedQuery(name = "Localidade.findByIdLocalizacao", query = "SELECT l FROM Localidade l WHERE l.idLocalizacao = :idLocalizacao"),
    @NamedQuery(name = "Localidade.findByRegiao", query = "SELECT l FROM Localidade l WHERE l.regiao = :regiao")})
public class Localidade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_localizacao")
    private Integer idLocalizacao;
    @Basic(optional = false)
    @Column(name = "regiao")
    private String regiao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locOrigem")
    private List<Rota> rotaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locDestino")
    private List<Rota> rotaList1;

    public Localidade() {
    }

    public Localidade(Integer idLocalizacao) {
        this.idLocalizacao = idLocalizacao;
    }

    public Localidade(Integer idLocalizacao, String regiao) {
        this.idLocalizacao = idLocalizacao;
        this.regiao = regiao;
    }

    public Integer getIdLocalizacao() {
        return idLocalizacao;
    }

    public void setIdLocalizacao(Integer idLocalizacao) {
        this.idLocalizacao = idLocalizacao;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    @XmlTransient
    public List<Rota> getRotaList() {
        return rotaList;
    }

    public void setRotaList(List<Rota> rotaList) {
        this.rotaList = rotaList;
    }

    @XmlTransient
    public List<Rota> getRotaList1() {
        return rotaList1;
    }

    public void setRotaList1(List<Rota> rotaList1) {
        this.rotaList1 = rotaList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLocalizacao != null ? idLocalizacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Localidade)) {
            return false;
        }
        Localidade other = (Localidade) object;
        if ((this.idLocalizacao == null && other.idLocalizacao != null) || (this.idLocalizacao != null && !this.idLocalizacao.equals(other.idLocalizacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.auadottonizaidem.dao.Localidade[ idLocalizacao=" + idLocalizacao + " ]";
    }
    
}
