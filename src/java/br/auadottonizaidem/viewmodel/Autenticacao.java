/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.entity.Cliente;
import br.auadottonizaidem.entity.Empresa;
import br.auadottonizaidem.entity.Veiculo;
import javax.servlet.http.HttpSession;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.SessionCleanup;

/**
 *
 * @author Aparecida
 */
public class Autenticacao {

    HttpSession sessao;

    public Autenticacao() {
        this.sessao = (HttpSession) Sessions.getCurrent().getNativeSession();
    }

    public Autenticacao(String desliga) {
        this.sessao = (HttpSession) Sessions.getCurrent().getNativeSession();
        this.sessao.invalidate();
    }

    public Autenticacao(Cliente c) {
        this.sessao = (HttpSession) Sessions.getCurrent().getNativeSession();
        Sessions.getCurrent().setAttribute("cliente", c);
    }
    public Autenticacao(Veiculo v) {
        this.sessao = (HttpSession) Sessions.getCurrent().getNativeSession();
        Sessions.getCurrent().setAttribute("veiculo", v);
    }

    public Autenticacao(Empresa e) {
        this.sessao = (HttpSession) Sessions.getCurrent().getNativeSession();
        Sessions.getCurrent().setAttribute("empresa", e);
    }

    public Cliente getUserSession() {
        Cliente cliente = (Cliente) sessao.getAttribute("cliente");
        return cliente;
    }

    public Empresa getEmpresaSession() {
        Empresa empresa = (Empresa) sessao.getAttribute("empresa");
        return empresa;
    }
    
    public Veiculo getVeiculoSession() {
        Veiculo veiculo = (Veiculo) sessao.getAttribute("veiculo");
        return veiculo;
    }
}
