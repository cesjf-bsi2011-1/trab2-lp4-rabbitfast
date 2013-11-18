/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aparecida
 */
@Entity
@Table(name = "ta_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Status.findAll", query = "SELECT s FROM Status s"),
    @NamedQuery(name = "Status.findByIdRota", query = "SELECT s FROM Status s WHERE s.statusPK.idRota = :idRota"),
    @NamedQuery(name = "Status.findByIdRotaPercurso", query = "SELECT s FROM Status s WHERE s.statusPK.idRotaPercurso = :idRotaPercurso"),
    @NamedQuery(name = "Status.findByIdEntrega", query = "SELECT s FROM Status s WHERE s.statusPK.idEntrega = :idEntrega"),
    @NamedQuery(name = "Status.findByDataHoraPassagemPonto", query = "SELECT s FROM Status s WHERE s.dataHoraPassagemPonto = :dataHoraPassagemPonto")})
public class Status implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StatusPK statusPK;
    @Column(name = "data_hora_passagem_ponto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraPassagemPonto;
    @JoinColumn(name = "id_entrega", referencedColumnName = "id_entrega", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Entrega entrega;
    @JoinColumns({
        @JoinColumn(name = "id_rota", referencedColumnName = "id_rota", insertable = false, updatable = false),
        @JoinColumn(name = "id_rota_percurso", referencedColumnName = "id_ponto_referencia", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private RotaPercurso rotaPercurso;

    public Status() {
    }

    public Status(StatusPK statusPK) {
        this.statusPK = statusPK;
    }

    public Status(int idRota, int idRotaPercurso, int idEntrega) {
        this.statusPK = new StatusPK(idRota, idRotaPercurso, idEntrega);
    }

    public StatusPK getStatusPK() {
        return statusPK;
    }

    public void setStatusPK(StatusPK statusPK) {
        this.statusPK = statusPK;
    }

    public Date getDataHoraPassagemPonto() {
        return dataHoraPassagemPonto;
    }

    public void setDataHoraPassagemPonto(Date dataHoraPassagemPonto) {
        this.dataHoraPassagemPonto = dataHoraPassagemPonto;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    public RotaPercurso getRotaPercurso() {
        return rotaPercurso;
    }

    public void setRotaPercurso(RotaPercurso rotaPercurso) {
        this.rotaPercurso = rotaPercurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statusPK != null ? statusPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Status)) {
            return false;
        }
        Status other = (Status) object;
        if ((this.statusPK == null && other.statusPK != null) || (this.statusPK != null && !this.statusPK.equals(other.statusPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.auadottonizaidem.dao.Status[ statusPK=" + statusPK + " ]";
    }
    
}
