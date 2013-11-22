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
<<<<<<< HEAD
=======
import org.zkoss.zul.Messagebox;
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
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
<<<<<<< HEAD
    private StatusCrud estCivil;
=======
    private StatusCrud status;
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        listaCliente = new ClienteJpaController(emf).findClienteEntities();
        selected = new Cliente();
<<<<<<< HEAD
        estCivil = StatusCrud.insert;
    }

    @NotifyChange({"selected", "estCivil"})
    @Command
    public void open() {
        estCivil = StatusCrud.view;
=======
        status = StatusCrud.insert;
    }

    @NotifyChange({"selected", "status"})
    @Command
    public void open() {
        status = StatusCrud.view;
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
        fmrCadCliente.doModal();

    }

<<<<<<< HEAD
    @NotifyChange({"selected", "estCivil"})
    @Command
    public void novo() {
        selected = new Cliente();
        estCivil = StatusCrud.insert;
=======
    @NotifyChange({"selected", "status"})
    @Command
    public void novo() {
        selected = new Cliente();
        status = StatusCrud.insert;
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
        fmrCadCliente.doModal();

    }

<<<<<<< HEAD
    @NotifyChange({"listaCliente", "selected", "estCivil"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void alteraCliente() {
        estCivil = StatusCrud.edit;

    }

    @NotifyChange({"listaCliente", "selected", "estCivil"})//para atualizar assim que gravar no banco de dados.
=======
    @NotifyChange({"listaCliente", "selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void alteraCliente() {
        status = StatusCrud.edit;

    }

    @NotifyChange({"listaCliente", "selected", "status"})//para atualizar assim que gravar no banco de dados.
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
    @Command
    public void gravaCliente() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

<<<<<<< HEAD
        if (estCivil == StatusCrud.insert) {
            new ClienteJpaController(emf).create(selected);
        } else if (estCivil == StatusCrud.edit) {
=======
        if (status == StatusCrud.insert) {
            new ClienteJpaController(emf).create(selected);
        } else if (status == StatusCrud.edit) {
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
            try {
                new ClienteJpaController(emf).edit(selected);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fmrCadCliente.setVisible(false);
<<<<<<< HEAD
        estCivil = StatusCrud.view;
=======
        Messagebox.show("Cadastro realizado com sucesso!");
        status = StatusCrud.view;
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
        selected = new Cliente();
        listaCliente = new ClienteJpaController(emf).findClienteEntities();

    }

<<<<<<< HEAD
    @NotifyChange({"listaCliente", "selected", "estCivil"})//para atualizar assim que gravar no banco de dados.
=======
    @NotifyChange({"listaCliente", "selected", "status"})//para atualizar assim que gravar no banco de dados.
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
    @Command
    public void apagaCliente() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
            new ClienteJpaController(emf).destroy(selected.getIdCliente());
            fmrCadCliente.setVisible(false);
<<<<<<< HEAD
            estCivil = StatusCrud.view;
=======
            status = StatusCrud.view;
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
            selected = new Cliente();
            listaCliente = new ClienteJpaController(emf).findClienteEntities();
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
<<<<<<< HEAD
        return estCivil;
    }

    public void setStatus(StatusCrud status) {
        this.estCivil = status;
=======
        return status;
    }

    public void setStatus(StatusCrud status) {
        this.status = status;
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
    }

    public List<Cliente> getListaCliente() {
        return listaCliente;
    }

    public void setListaCliente(List<Cliente> listaCliente) {
        this.listaCliente = listaCliente;
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
