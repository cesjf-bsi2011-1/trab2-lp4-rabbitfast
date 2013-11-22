/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.dao.VeiculoJpaController;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import static br.auadottonizaidem.entity.Cliente_.estCivil;
import br.auadottonizaidem.entity.Veiculo;
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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;

/**
 *
 * @author Willian
 */
public class CadVeiculoVM {

    private List<Veiculo> listaVeiculo;
    private Veiculo selected, selectedPesq;
    private StatusCrud tipoVeiculo;
    @Wire
    private Window fmrCadVeiculo, fmrPesquisaVeiculo;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        selected = new Veiculo();
        tipoVeiculo = StatusCrud.insert;
    }

    @NotifyChange({"selected", "tipoVeiculo"})
    @Command
    public void open() {
        tipoVeiculo = StatusCrud.view;
        fmrCadVeiculo.doModal();

    }

    @NotifyChange({"selected", "tipoVeiculo"})
    @Command
    public void novo() {
        selected = new Veiculo();
        tipoVeiculo = StatusCrud.view;
        fmrCadVeiculo.doModal();

    }

    @NotifyChange({"listaVeiculo", "selected", "tipoVeiculo"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void gravaVeiculo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        if (tipoVeiculo == StatusCrud.insert) {
            try {
                new VeiculoJpaController(emf).create(selected);
            } catch (Exception ex) {
                Logger.getLogger(CadVeiculoVM.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (tipoVeiculo == StatusCrud.edit) {
            try {
                new VeiculoJpaController(emf).edit(selected);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fmrCadVeiculo.setVisible(false);
        tipoVeiculo = StatusCrud.view;
        selected = new Veiculo();
        listaVeiculo = new VeiculoJpaController(emf).findVeiculoEntities();

    }

    @NotifyChange({"listaVeiculo", "selected", "tipoVeiculo"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void alteraVeiculo() {
        tipoVeiculo = StatusCrud.edit;

    }

    @NotifyChange({"listaCursos"})
    @Command
    public void pesquisaCurso() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZKCursoPU");
        listaVeiculo = new VeiculoJpaController(emf).findVeiculoByCriterio(selectedPesq);
        fmrPesquisaVeiculo.setVisible(false);

    }

    @NotifyChange({"listaVeiculo", "selected", "tipoUsuario"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void apagaVeiculo() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
            new VeiculoJpaController(emf).destroy(selected.getTipoVeiculo());
            fmrCadVeiculo.setVisible(false);
            tipoVeiculo = StatusCrud.view;
            selected = new Veiculo();
            listaVeiculo = new VeiculoJpaController(emf).findVeiculoEntities();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CadClienteVM.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Veiculo getSelected() {
        return selected;
    }

    public void setSelected(Veiculo selected) {
        this.selected = selected;
    }

    public List<Veiculo> getListaVeiculo() {
        return listaVeiculo;
    }

    public void setListaVeiculo(List<Veiculo> listaVeiculo) {
        this.listaVeiculo = listaVeiculo;
    }

    public StatusCrud getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(StatusCrud tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }
}
