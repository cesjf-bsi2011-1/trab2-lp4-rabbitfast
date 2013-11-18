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
@Table(name = "tb_ponto_referencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PontoReferencia.findAll", query = "SELECT p FROM PontoReferencia p"),
    @NamedQuery(name = "PontoReferencia.findByIdPontoReferencia", query = "SELECT p FROM PontoReferencia p WHERE p.idPontoReferencia = :idPontoReferencia"),
    @NamedQuery(name = "PontoReferencia.findByDescrPonto", query = "SELECT p FROM PontoReferencia p WHERE p.descrPonto = :descrPonto")})
public class PontoReferencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ponto_referencia")
    private Integer idPontoReferencia;
    @Column(name = "descr_ponto")
    private String descrPonto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pontoReferencia")
    private List<RotaPercurso> rotaPercursoList;

    public PontoReferencia() {
    }

    public PontoReferencia(Integer idPontoReferencia) {
        this.idPontoReferencia = idPontoReferencia;
    }

    public Integer getIdPontoReferencia() {
        return idPontoReferencia;
    }

    public void setIdPontoReferencia(Integer idPontoReferencia) {
        this.idPontoReferencia = idPontoReferencia;
    }

    public String getDescrPonto() {
        return descrPonto;
    }

    public void setDescrPonto(String descrPonto) {
        this.descrPonto = descrPonto;
    }

    @XmlTransient
    public List<RotaPercurso> getRotaPercursoList() {
        return rotaPercursoList;
    }

    public void setRotaPercursoList(List<RotaPercurso> rotaPercursoList) {
        this.rotaPercursoList = rotaPercursoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPontoReferencia != null ? idPontoReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PontoReferencia)) {
            return false;
        }
        PontoReferencia other = (PontoReferencia) object;
        if ((this.idPontoReferencia == null && other.idPontoReferencia != null) || (this.idPontoReferencia != null && !this.idPontoReferencia.equals(other.idPontoReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.auadottonizaidem.dao.PontoReferencia[ idPontoReferencia=" + idPontoReferencia + " ]";
    }
    
}
