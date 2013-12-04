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
@Table(name = "tb_entrega")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entrega.findAll", query = "SELECT e FROM Entrega e"),
    @NamedQuery(name = "Entrega.findAllToCliente", query = "SELECT e FROM Entrega e WHERE e.idCliente = :idCliente"),
    @NamedQuery(name = "Entrega.findByIdEntrega", query = "SELECT e FROM Entrega e WHERE e.idEntrega = :idEntrega"),
    @NamedQuery(name = "Entrega.findByDescricao", query = "SELECT e FROM Entrega e WHERE e.descricao = :descricao"),
    @NamedQuery(name = "Entrega.findByValor", query = "SELECT e FROM Entrega e WHERE e.valor = :valor"),
    @NamedQuery(name = "Entrega.findByProximoSequencia", query = "SELECT e.idRota.rotaPercursoList FROM Entrega e "
        + "WHERE e.idRota.rotaPercursoList  NOT IN (e.statusList[1].rotaPercurso) AND e.idEntrega = :idEntrega"),
    @NamedQuery(name = "Entrega.findByPeso", query = "SELECT e FROM Entrega e WHERE e.peso = :peso")})
public class Entrega implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_entrega")
    private Integer idEntrega;
    @Column(name = "descricao")
    private String descricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Column(name = "peso")
    private Double peso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entrega")
    private List<Status> statusList;
    @JoinColumn(name = "placa_veiculo", referencedColumnName = "placa_veiculo")
    @ManyToOne
    private Veiculo placaVeiculo;
    @JoinColumn(name = "id_rota", referencedColumnName = "id_rota")
    @ManyToOne
    private Rota idRota;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    @ManyToOne
    private Cliente idCliente;

    public Entrega() {
    }

    public Entrega(Integer idEntrega) {
        this.idEntrega = idEntrega;
    }

    public Integer getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Integer idEntrega) {
        this.idEntrega = idEntrega;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    @XmlTransient
    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }

    public Veiculo getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(Veiculo placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public Rota getIdRota() {
        return idRota;
    }

    public void setIdRota(Rota idRota) {
        this.idRota = idRota;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEntrega != null ? idEntrega.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entrega)) {
            return false;
        }
        Entrega other = (Entrega) object;
        if ((this.idEntrega == null && other.idEntrega != null) || (this.idEntrega != null && !this.idEntrega.equals(other.idEntrega))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.auadottonizaidem.dao.Entrega[ idEntrega=" + idEntrega + " ]";
    }
    
}
