/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.dao.EntregaJpaController;
import br.auadottonizaidem.entity.Cliente;
import br.auadottonizaidem.entity.Entrega;
import br.auadottonizaidem.entity.RotaPercurso;
import br.auadottonizaidem.entity.Status;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 *
 * @author Willian
 */
public class PainelEmpresaVM {

    Query query;
    EntityManager entityManager;
    private Entrega entrega;
    private List<Entrega> listaEntregas;
    @WireVariable
    Session sessao;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        entrega = new Entrega();
        listaEntregas = new EntregaJpaController(emf).findEntregaEntities();

    }

    @Command
    public String formatDate(Date data) {
        SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return spf.format(data);

    }

    @Command
    public void atualizaStatus() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        Status ultimoStatus = (Status) entityManager.createNamedQuery("Status.findUltimoStatus")
                .setParameter("idEntrega", entrega.getIdEntrega()).getSingleResult();
        
        int proximaSequencia = ultimoStatus.getRotaPercurso().getSequencia() + 1;
        
        RotaPercurso proximaRotaPercurso = (RotaPercurso) 
                entityManager.createNamedQuery("RotaPercurso.findByIdRotaESequencia")
                .setParameter("idRota", ultimoStatus.getStatusPK().getIdRota())
                .setParameter("sequencia", proximaSequencia).getSingleResult();
        
        if(proximaRotaPercurso == null) {
            //Concluiu a entrega
        }
        
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    public List<Entrega> getListaEntregas() {
        return listaEntregas;
    }

    public void setListaEntregas(List<Entrega> listaEntregas) {
        this.listaEntregas = listaEntregas;
    }
}
