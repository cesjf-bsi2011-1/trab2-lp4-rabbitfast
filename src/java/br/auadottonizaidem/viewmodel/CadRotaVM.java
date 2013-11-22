/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.dao.ClienteJpaController;
import br.auadottonizaidem.dao.LocalidadeJpaController;
import br.auadottonizaidem.dao.RotaJpaController;
import br.auadottonizaidem.dao.exceptions.IllegalOrphanException;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.entity.Localidade;
import br.auadottonizaidem.entity.Rota;
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
 * @author Willian
 */
public class CadRotaVM {

    private List<Rota> listaRotas;
    private List<Localidade> listaLocalidades;
    private Rota selected;
    private Localidade origem;
    private Localidade destino;

    @Wire
    private Window fmrCadRotas;
    private StatusCrud status;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        listaRotas = new RotaJpaController(emf).findRotaEntities();
        listaLocalidades = new LocalidadeJpaController(emf).findLocalidadeEntities();
        selected = new Rota();
        origem = new Localidade();
        destino = new Localidade();
        status = StatusCrud.insert;
    }

    @NotifyChange({"selected","origem","destino", "status"})
    @Command
    public void open() {
        status = StatusCrud.view;
        fmrCadRotas.doModal();

    }

    @NotifyChange({"selected", "origem","destino","status"})
    @Command
    public void novo() {
        selected = new Rota();
        origem = new Localidade();
        status = StatusCrud.insert;
        fmrCadRotas.doModal();

    }

    @NotifyChange({"listaRotas","listaLocalidades","origem", "destino","selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void alteraRotas() {
        status = StatusCrud.edit;

    }

    @NotifyChange({"listaRotas","listaLocalidades", "origem","destino","selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void gravaRotas() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        if (status == StatusCrud.insert) {
            try {
                selected.setLocOrigem(origem);
                selected.setLocDestino(destino);
                new RotaJpaController(emf).create(selected);
            } catch (Exception ex) {
                Logger.getLogger(CadRotaVM.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (status == StatusCrud.edit) {
            try {
                new RotaJpaController(emf).edit(selected);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(CadRotaVM.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CadRotaVM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fmrCadRotas.setVisible(false);
        status = StatusCrud.view;
        selected = new Rota();
        origem = new Localidade();
        listaRotas = new RotaJpaController(emf).findRotaEntities();

    }

    @NotifyChange({"listaRotas", "listaLocalidades", "origem","destino","selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void apagaRotas() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
            try {
                new RotaJpaController(emf).destroy(selected.getIdRota());
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(CadRotaVM.class.getName()).log(Level.SEVERE, null, ex);
            }
            fmrCadRotas.setVisible(false);
            status = StatusCrud.view;
            selected = new Rota();
            origem = new Localidade();
            listaRotas = new RotaJpaController(emf).findRotaEntities();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CadRotaVM.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Rota getSelected() {
        return selected;
    }

    public void setSelected(Rota selected) {
        this.selected = selected;
    }

    public StatusCrud getStatus() {
        return status;
    }

    public void setStatus(StatusCrud status) {
        this.status = status;
    }

    public List<Rota> getListaRotas() {
        return listaRotas;
    }

    public void setListaRotas(List<Rota> listaRotas) {
        this.listaRotas = listaRotas;
    }
    
    public List<Localidade> getListaLocalidades() {
        return listaLocalidades;
    }

    public void setListaLocalidades(List<Localidade> listaLocalidades) {
        this.listaLocalidades = listaLocalidades;
    }
    
    public Localidade getOrigem() {
        return origem;
    }

    public void setDestino(Localidade origem) {
        this.origem = origem;
    }
    
    
    public Window getFmrCadRotas() {
        return fmrCadRotas;
    }

    public void setFmrCadRotas(Window fmrCadRotas) {
        this.fmrCadRotas = fmrCadRotas;
    }
        
    public void setOrigem(Localidade origem) {
        this.origem = origem;
    }

    public Localidade getDestino() {
        return destino;
    }

    

}
