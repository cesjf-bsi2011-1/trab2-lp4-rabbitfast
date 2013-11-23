/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Aparecida
 */
@Embeddable
public class StatusPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_rota")
    private int idRota;
    @Basic(optional = false)
    @Column(name = "id_rota_percurso")
    private int idRotaPercurso;
    @Basic(optional = false)
    @Column(name = "id_entrega")
    private int idEntrega;

    public StatusPK() {
    }

    public StatusPK(int idRota, int idRotaPercurso, int idEntrega) {
        this.idRota = idRota;
        this.idRotaPercurso = idRotaPercurso;
        this.idEntrega = idEntrega;
    }

    public int getIdRota() {
        return idRota;
    }

    public void setIdRota(int idRota) {
        this.idRota = idRota;
    }

    public int getIdRotaPercurso() {
        return idRotaPercurso;
    }

    public void setIdRotaPercurso(int idRotaPercurso) {
        this.idRotaPercurso = idRotaPercurso;
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idRota;
        hash += (int) idRotaPercurso;
        hash += (int) idEntrega;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StatusPK)) {
            return false;
        }
        StatusPK other = (StatusPK) object;
        if (this.idRota != other.idRota) {
            return false;
        }
        if (this.idRotaPercurso != other.idRotaPercurso) {
            return false;
        }
        if (this.idEntrega != other.idEntrega) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.auadottonizaidem.dao.StatusPK[ idRota=" + idRota + ", idRotaPercurso=" + idRotaPercurso + ", idEntrega=" + idEntrega + " ]";
    }
    
}
