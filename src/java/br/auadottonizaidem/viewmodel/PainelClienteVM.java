/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.entity.Cliente;
import br.auadottonizaidem.entity.Entrega;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 *
 * @author Willian
 */
public class PainelClienteVM {

    private Cliente cliente;
    private List<Entrega> listEntregasCliente;
    private Entrega entrega;
    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        Query query;
        EntityManager entityManager;


        Autenticacao a = new Autenticacao();
        cliente = a.getUserSession();
        entityManager = emf.createEntityManager();
        query = entityManager.createNamedQuery("Entrega.findAllToCliente");
        query.setParameter("idCliente", cliente);
        
        listEntregasCliente = query.getResultList();


    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Entrega> getListEntregasCliente() {
        return listEntregasCliente;
    }

    public void setListEntregasCliente(List<Entrega> listEntregasCliente) {
        this.listEntregasCliente = listEntregasCliente;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }
    
    
    
    
}
