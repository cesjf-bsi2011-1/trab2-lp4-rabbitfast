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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ta_rota")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rota.findAll", query = "SELECT r FROM Rota r"),
    @NamedQuery(name = "Rota.findByIdRota", query = "SELECT r FROM Rota r WHERE r.idRota = :idRota")})
public class Rota implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_rota")
    private Integer idRota;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rota")
    private List<RotaPercurso> rotaPercursoList;
    @OneToMany(mappedBy = "idRota")
    private List<Entrega> entregaList;
    @JoinColumn(name = "loc_origem", referencedColumnName = "id_localizacao")
    @ManyToOne(optional = false)
    private Localidade locOrigem;
    @JoinColumn(name = "loc_destino", referencedColumnName = "id_localizacao")
    @ManyToOne(optional = false)
    private Localidade locDestino;

    public Rota() {
    }

    public Rota(Integer idRota) {
        this.idRota = idRota;
    }

    public Integer getIdRota() {
        return idRota;
    }

    public void setIdRota(Integer idRota) {
        this.idRota = idRota;
    }

    @XmlTransient
    public List<RotaPercurso> getRotaPercursoList() {
        return rotaPercursoList;
    }

    public void setRotaPercursoList(List<RotaPercurso> rotaPercursoList) {
        this.rotaPercursoList = rotaPercursoList;
    }

    @XmlTransient
    public List<Entrega> getEntregaList() {
        return entregaList;
    }

    public void setEntregaList(List<Entrega> entregaList) {
        this.entregaList = entregaList;
    }

    public Localidade getLocOrigem() {
        return locOrigem;
    }

    public void setLocOrigem(Localidade locOrigem) {
        this.locOrigem = locOrigem;
    }

    public Localidade getLocDestino() {
        return locDestino;
    }

    public void setLocDestino(Localidade locDestino) {
        this.locDestino = locDestino;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRota != null ? idRota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rota)) {
            return false;
        }
        Rota other = (Rota) object;
        if ((this.idRota == null && other.idRota != null) || (this.idRota != null && !this.idRota.equals(other.idRota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.auadottonizaidem.dao.Rota[ idRota=" + idRota + " ]";
    }
    
}
