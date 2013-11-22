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
<<<<<<< HEAD
=======
import org.zkoss.zul.Messagebox;
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
import org.zkoss.zul.Window;

/**
 *
 * @author Willian
 */
public class CadRotaVM {

    private List<Rota> listaRotas;
    private List<Localidade> listaLocalidades;
<<<<<<< HEAD
=======
    private List<Localidade> listaLocalidades1;
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
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
<<<<<<< HEAD
=======
        
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
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
<<<<<<< HEAD
=======
        destino = new Localidade();
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
        status = StatusCrud.insert;
        fmrCadRotas.doModal();

    }

<<<<<<< HEAD
    @NotifyChange({"listaRotas","listaLocalidades","origem", "destino","selected", "status"})//para atualizar assim que gravar no banco de dados.
=======
    @NotifyChange({ "status"})//para atualizar assim que gravar no banco de dados.
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
    @Command
    public void alteraRotas() {
        status = StatusCrud.edit;

    }

<<<<<<< HEAD
    @NotifyChange({"listaRotas","listaLocalidades", "origem","destino","selected", "status"})//para atualizar assim que gravar no banco de dados.
=======
    @NotifyChange({"selected", "status"})//para atualizar assim que gravar no banco de dados.
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
    @Command
    public void gravaRotas() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        if (status == StatusCrud.insert) {
            try {
                selected.setLocOrigem(origem);
                selected.setLocDestino(destino);
                new RotaJpaController(emf).create(selected);
<<<<<<< HEAD
=======
                Messagebox.show("Cadastro realizado com sucesso!");
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
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
<<<<<<< HEAD
        status = StatusCrud.view;
        selected = new Rota();
        origem = new Localidade();
=======
        selected = new Rota();
>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
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
<<<<<<< HEAD
    
    public Localidade getOrigem() {
        return origem;
    }

    public void setDestino(Localidade origem) {
        this.origem = origem;
    }
=======

>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
    
    
    public Window getFmrCadRotas() {
        return fmrCadRotas;
    }

    public void setFmrCadRotas(Window fmrCadRotas) {
        this.fmrCadRotas = fmrCadRotas;
    }
<<<<<<< HEAD
        
=======

    public List<Localidade> getListaLocalidades1() {
        return listaLocalidades1;
    }

    public void setListaLocalidades1(List<Localidade> listaLocalidades1) {
        this.listaLocalidades1 = listaLocalidades1;
    }

    public Localidade getOrigem() {
        return origem;
    }

>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
    public void setOrigem(Localidade origem) {
        this.origem = origem;
    }

    public Localidade getDestino() {
        return destino;
    }

<<<<<<< HEAD
=======
    public void setDestino(Localidade destino) {
        this.destino = destino;
    }

>>>>>>> f8cc7ae1dd4adea3776203702c7f4459822d81c4
    

}
