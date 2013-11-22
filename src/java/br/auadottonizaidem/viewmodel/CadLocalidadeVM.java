/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.dao.LocalidadeJpaController;
import br.auadottonizaidem.dao.exceptions.IllegalOrphanException;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.entity.Localidade;
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
 * @author Willian
 */
public class CadLocalidadeVM {

    private List<Localidade> listaLocalidade;
    private Localidade selected;
    @Wire
    private Window fmrCadLocalidade;
    private StatusCrud status;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        listaLocalidade = new LocalidadeJpaController(emf).findLocalidadeEntities();
        selected = new Localidade();
        status = StatusCrud.insert;
    }

    @NotifyChange({"selected", "status"})
    @Command
    public void open() {
        status = StatusCrud.view;
        fmrCadLocalidade.doModal();

    }

    @NotifyChange({"selected", "status"})
    @Command
    public void novo() {
        selected = new Localidade();
        status = StatusCrud.insert;
        fmrCadLocalidade.doModal();

    }

    @NotifyChange({"listaLocalidade", "selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void alteraLocalizacao() {
        status = StatusCrud.edit;

    }

    @NotifyChange({"listaLocalidade", "selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void gravaLocalizacao() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        if (status == StatusCrud.insert) {
            new LocalidadeJpaController(emf).create(selected);
        } else if (status == StatusCrud.edit) {
            try {
                new LocalidadeJpaController(emf).edit(selected);
                
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(CadLocalidadeVM.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CadLocalidadeVM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fmrCadLocalidade.setVisible(false);
        Messagebox.show("Localidade cadastrada com sucesso!");
        status = StatusCrud.view;
        selected = new Localidade();
        listaLocalidade = new LocalidadeJpaController(emf).findLocalidadeEntities();

    }

    @NotifyChange({"listaLocalidade", "selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void apagaLocalizacao() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

            new LocalidadeJpaController(emf).destroy(selected.getIdLocalizacao());

            fmrCadLocalidade.setVisible(false);
            status = StatusCrud.view;
            selected = new Localidade();
            listaLocalidade = new LocalidadeJpaController(emf).findLocalidadeEntities();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(CadLocalidadeVM.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Localidade getSelected() {
        return selected;
    }

    public void setSelected(Localidade selected) {
        this.selected = selected;
    }

    public StatusCrud getStatus() {
        return status;
    }

    public void setStatus(StatusCrud status) {
        this.status = status;
    }

    public List<Localidade> getListaLocalidade() {
        return listaLocalidade;
    }

    public void setListaLocalidade(List<Localidade> listaLocalidade) {
        this.listaLocalidade = listaLocalidade;
    }

}
