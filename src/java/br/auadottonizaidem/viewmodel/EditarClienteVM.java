/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.dao.ClienteJpaController;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.entity.Cliente;
import br.auadottonizaidem.viewmodelutil.StatusCrud;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author Aparecida
 */
public class EditarClienteVM {

    HttpSession session;
    private List<Cliente> listaCliente;
    private Cliente selected;
    @Wire
    private Window frmEditaCliente;
    private StatusCrud status;
    private Cliente cli, cliente;
    private Cliente id;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        listaCliente = new ClienteJpaController(emf).findClienteEntities();
        Autenticacao autenticacao = new Autenticacao();
        selected = autenticacao.getUserSession();
        frmEditaCliente.doModal();
    }

    @NotifyChange({"selected", "status"})
    @Command
    public void open() {
        status = StatusCrud.view;
    }

    @Command
    public void Valida() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        cli = new ClienteJpaController(emf).findUserById(id);

        if (cli.getIdCliente().equals(cliente.getIdCliente())) {
            frmEditaCliente.setVisible(true);

        } else {
            Messagebox.show("Cliente incorreto");
        }

    }

    @NotifyChange({"selected", "status"})
    @Command
    public void novo() {
        selected = new Cliente();
        status = StatusCrud.insert;
        frmEditaCliente.doModal();

    }
    
    @Command
    public void alteraCliente() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        try {
            new ClienteJpaController(emf).edit(selected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EditarClienteVM.class.getName()).log(Level.SEVERE, null, ex);
        }
        Messagebox.show("Cliente Editado com sucesso!");
        frmEditaCliente.setVisible(false);
    }

    @NotifyChange({"listaCliente", "selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void apagaCliente() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
            new ClienteJpaController(emf).destroy(selected.getIdCliente());
            frmEditaCliente.setVisible(false);
            status = StatusCrud.view;
            selected = new Cliente();
            listaCliente = new ClienteJpaController(emf).findClienteEntities();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EditarClienteVM.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Command
    public void painelCliente() {
        Executions.sendRedirect("painelCliente.zul");
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

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getId() {
        return id;
    }

    public void setId(Cliente id) {
        this.id = id;
    }

}
