/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "ta_rota_percurso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RotaPercurso.findAll", query = "SELECT r FROM RotaPercurso r"),
    @NamedQuery(name = "RotaPercurso.findByIdRota", query = "SELECT r FROM RotaPercurso r WHERE r.rotaPercursoPK.idRota = :idRota"),
    @NamedQuery(name = "RotaPercurso.findByIdPontoReferencia", query = "SELECT r FROM RotaPercurso r WHERE r.rotaPercursoPK.idPontoReferencia = :idPontoReferencia"),
    @NamedQuery(name = "RotaPercurso.findBySequencia", query = "SELECT r FROM RotaPercurso r WHERE r.sequencia = :sequencia"),
    @NamedQuery(name = "RotaPercurso.findByDistancia", query = "SELECT r FROM RotaPercurso r WHERE r.distancia = :distancia"),
    @NamedQuery(name = "RotaPercurso.findByIdRotaESequencia", query = "SELECT r FROM RotaPercurso r WHERE r.rotaPercursoPK.idRota = :idRota AND r.sequencia = :sequencia")})
    
public class RotaPercurso implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RotaPercursoPK rotaPercursoPK;
    @Column(name = "sequencia")
    private Integer sequencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "distancia")
    private Double distancia;
    @JoinColumn(name = "id_ponto_referencia", referencedColumnName = "id_ponto_referencia", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PontoReferencia pontoReferencia;
    @JoinColumn(name = "id_rota", referencedColumnName = "id_rota", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Rota rota;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rotaPercurso")
    private List<Status> statusList;

    public RotaPercurso() {
    }

    public RotaPercurso(RotaPercursoPK rotaPercursoPK) {
        this.rotaPercursoPK = rotaPercursoPK;
    }

    public RotaPercurso(int idRota, int idPontoReferencia) {
        this.rotaPercursoPK = new RotaPercursoPK(idRota, idPontoReferencia);
    }

    public RotaPercursoPK getRotaPercursoPK() {
        return rotaPercursoPK;
    }

    public void setRotaPercursoPK(RotaPercursoPK rotaPercursoPK) {
        this.rotaPercursoPK = rotaPercursoPK;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public PontoReferencia getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(PontoReferencia pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public Rota getRota() {
        return rota;
    }

    public void setRota(Rota rota) {
        this.rota = rota;
    }

    @XmlTransient
    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rotaPercursoPK != null ? rotaPercursoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RotaPercurso)) {
            return false;
        }
        RotaPercurso other = (RotaPercurso) object;
        if ((this.rotaPercursoPK == null && other.rotaPercursoPK != null) || (this.rotaPercursoPK != null && !this.rotaPercursoPK.equals(other.rotaPercursoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.auadottonizaidem.dao.RotaPercurso[ rotaPercursoPK=" + rotaPercursoPK + " ]";
    }
    
}
