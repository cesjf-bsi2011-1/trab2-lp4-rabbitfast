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
public class RotaPercursoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_rota")
    private int idRota;
    @Basic(optional = false)
    @Column(name = "id_ponto_referencia")
    private int idPontoReferencia;

    public RotaPercursoPK() {
    }

    public RotaPercursoPK(int idRota, int idPontoReferencia) {
        this.idRota = idRota;
        this.idPontoReferencia = idPontoReferencia;
    }

    public int getIdRota() {
        return idRota;
    }

    public void setIdRota(int idRota) {
        this.idRota = idRota;
    }

    public int getIdPontoReferencia() {
        return idPontoReferencia;
    }

    public void setIdPontoReferencia(int idPontoReferencia) {
        this.idPontoReferencia = idPontoReferencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idRota;
        hash += (int) idPontoReferencia;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RotaPercursoPK)) {
            return false;
        }
        RotaPercursoPK other = (RotaPercursoPK) object;
        if (this.idRota != other.idRota) {
            return false;
        }
        if (this.idPontoReferencia != other.idPontoReferencia) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.auadottonizaidem.dao.RotaPercursoPK[ idRota=" + idRota + ", idPontoReferencia=" + idPontoReferencia + " ]";
    }
    
}
