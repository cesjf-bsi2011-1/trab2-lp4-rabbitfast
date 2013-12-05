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
public class TemplateEmpresaVM {

    private Empresa emp;
    //   para pegar uma div interna, filha, está pegando
    @Wire
    private Center center;
    private Window winLogin;
    private String login, senha;
    @WireVariable
    Session sessao;

    // assim que a pagina carregar ela executa esse metodo
    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        Autenticacao a = new Autenticacao();
        emp = a.getEmpresaSession();
    }

    @Command
    public void Logar() {
        //pegando o window la na template
        winLogin = (Window) center.getFirstChild().getChildren().get(1);
        winLogin.setVisible(true);
        //       Messagebox.show("olá"+ center.getId());

    }

    @Command
    public void Logout() {

        Autenticacao a = new Autenticacao("E");
        Executions.sendRedirect("index.zul");

    }

    @Command
    public void cadRota() {
        Executions.sendRedirect("cadRota.zul");
    }

    @Command
    public void cadLocalidade() {
        Executions.sendRedirect("cadLocalidade.zul");
    }

    @Command
    public void cadPontoReferencia() {
        Executions.sendRedirect("cadPontoReferencia.zul");
    }

    @Command
    public void cadRotaPercurso() {
        Executions.sendRedirect("cadRotaPercurso.zul");
    }

    @Command
    public void cadStatus() {
        Executions.sendRedirect("cadStatus.zul");
    }

    @Command
    public void cadVeiculo() {
        Executions.sendRedirect("cadVeiculo.zul");
    }

    @Command
    public void painelEmpresa() {
        Executions.sendRedirect("painelEmpresa.zul");
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

    public Empresa getEmp() {
        return emp;
    }

    public void setEmp(Empresa emp) {
        this.emp = emp;
    }
}
