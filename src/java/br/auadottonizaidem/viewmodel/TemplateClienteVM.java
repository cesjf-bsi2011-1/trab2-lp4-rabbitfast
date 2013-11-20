/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.dao.ClienteJpaController;
import br.auadottonizaidem.dao.EmpresaJpaController;
import br.auadottonizaidem.entity.Cliente;
import br.auadottonizaidem.entity.Empresa;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author Aparecida
 */
public class TemplateClienteVM {

    //   para pegar uma div interna, filha, está pegando
    @Wire
    private Center center;
    private Window winLogin;
    private String login, senha;

    @WireVariable
    Session sessao;
    private Cliente user;
    private Empresa empresa;

    public Cliente getUser() {
        return user;
    }

    public void setUser(Cliente user) {
        this.user = user;
    }

    // assim que a pagina carregar ela executa esse metodo
    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    public void Logar() {
        //pegando o window la na template
        winLogin = (Window) center.getFirstChild().getChildren().get(1);
        winLogin.setVisible(true);
        //       Messagebox.show("olá"+ center.getId());

    }

    @Command
    public void Valida() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        user = new ClienteJpaController(emf).findUserByLoginAndSenha(login, senha);
        empresa = new EmpresaJpaController(emf).findEmpresaByLoginAndSenha(login, senha);

        if (user == null && empresa == null) {
            Messagebox.show("Login ou senha incorreta");
        }else if (empresa != null) {
            winLogin.setVisible(false);
            sessao.setAttribute("empresa", empresa);
            painelEmpresa();
        } else {
            winLogin.setVisible(false);
            sessao.setAttribute("user", user);
            painelCliente();
        }

    }

    @Command
    public void cadCliente() {
        Executions.sendRedirect("cadCliente.zul");
    }
    
    @Command
    public void painelCliente() {
        Executions.sendRedirect("painelCliente.zul");
    }
    
    @Command
    public void painelEmpresa() {
        Executions.sendRedirect("painelEmpresa.zul");
    }

    @Command
    public void cadEmpresa() {
        Executions.sendRedirect("cadEmpresa.zul");
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
