/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.entity.Cliente;


public class UserForm {

    private Cliente user = new Cliente();
    private String retypedPassword;
 
    public Cliente getUser() {
        return user;
    }
 
    public void setUser(Cliente user) {
        this.user = user;
    }
 
    public String getRetypedPassword() {
        return retypedPassword;
    }
 
    public void setRetypedPassword(String retypedPassword) {
        this.retypedPassword = retypedPassword;
    }
 
}