/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.dao.PontoReferenciaJpaController;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.entity.PontoReferencia;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author FelipeZaidem
 */
public class CadPontoReferenciaVM {

    private List<PontoReferencia> listaPontoReferencia;
    private PontoReferencia selected;
    @Wire
    private Window fmrCadPonRef;
    private StatusCrud status;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        listaPontoReferencia = new PontoReferenciaJpaController(emf).findPontoReferenciaEntities();
        selected = new PontoReferencia();
        status = StatusCrud.insert;
    }

    @NotifyChange({"selected", "status"})
    @Command
    public void open() {
        status = StatusCrud.view;
        fmrCadPonRef.doModal();

    }

    @NotifyChange({"selected", "status"})
    @Command
    public void novo() {
        selected = new PontoReferencia();
        status = StatusCrud.insert;
        fmrCadPonRef.doModal();

    }

    @NotifyChange({"listaPontoReferencia", "selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void gravaPontoReferencia() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        if (status == StatusCrud.insert) {
            new PontoReferenciaJpaController(emf).create(selected);
        } else if (status == StatusCrud.edit) {
            try {
                new PontoReferenciaJpaController(emf).edit(selected);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fmrCadPonRef.setVisible(false);
        Messagebox.show("Cadastro realizado com sucesso!");
        status = StatusCrud.view;
        selected = new PontoReferencia();
        listaPontoReferencia = new PontoReferenciaJpaController(emf).findPontoReferenciaEntities();

    }

    public List<PontoReferencia> getListaPontoReferencia() {
        return listaPontoReferencia;
    }

    public void setListaPontoReferencia(List<PontoReferencia> listaPontoReferencia) {
        this.listaPontoReferencia = listaPontoReferencia;
    }

    public PontoReferencia getSelected() {
        return selected;
    }

    public void setSelected(PontoReferencia selected) {
        this.selected = selected;
    }

    public Window getFmrCadPonRef() {
        return fmrCadPonRef;
    }

    public void setFmrCadPonRef(Window fmrCadPonRef) {
        this.fmrCadPonRef = fmrCadPonRef;
    }

    public StatusCrud getStatus() {
        return status;
    }

    public void setStatus(StatusCrud status) {
        this.status = status;
    }

}
