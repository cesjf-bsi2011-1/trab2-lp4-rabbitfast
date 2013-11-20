/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.dao.ClienteJpaController;
import br.auadottonizaidem.dao.exceptions.IllegalOrphanException;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.entity.Cliente;
import br.auadottonizaidem.viewmodelutil.StatusCrud;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 *
 * @author Aparecida
 */
public class CadClienteVM {
     private List<Cliente> listaCliente;
   private Cliente selected;
    @Wire
    private Window fmrCadCliente;
    private StatusCrud status;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        listaCliente = new ClienteJpaController(emf).findClienteEntities();
        selected = new Cliente();
        status = StatusCrud.insert;
    }

    @NotifyChange({"selected", "status"})
    @Command
    public void open() {
        status = StatusCrud.view;
        fmrCadCliente.doModal();

    }

    @NotifyChange({"selected", "status"})
    @Command
    public void novo() {
        selected = new Cliente();
        status = StatusCrud.insert;
        fmrCadCliente.doModal();

    }

    @NotifyChange({"listaCliente", "selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void alteraCliente() {
        status = StatusCrud.edit;

    }

    @NotifyChange({"listaCliente", "selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void gravaCliente() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        if (status == StatusCrud.insert) {
            new ClienteJpaController(emf).create(selected);
        } else if (status == StatusCrud.edit) {
            try {
                new ClienteJpaController(emf).edit(selected);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fmrCadCliente.setVisible(false);
        status = StatusCrud.view;
        selected = new Cliente();
        listaCliente = new ClienteJpaController(emf).findClienteEntities();

    }

    @NotifyChange({"listaCliente", "selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void apagaCliente() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
            new ClienteJpaController(emf).destroy(selected.getIdCliente());
            fmrCadCliente.setVisible(false);
            status = StatusCrud.view;
            selected = new Cliente();
            listaCliente  = new ClienteJpaController(emf).findClienteEntities();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Cliente getSelected() {
        return selected;
    }

    public void setSelected(Cliente selected) {
        this.selected = selected;
    }

    public StatusCrud getStatus() {
        return status;
    }

    public void setStatus(StatusCrud status) {
        this.status = status;
    }
    
    
     public List<Cliente> getListaCliente() {
        return listaCliente;
    }

    public void setListaCliente(List<Cliente> listaCliente) {
        this.listaCliente = listaCliente;
    }
    
    
    
}
