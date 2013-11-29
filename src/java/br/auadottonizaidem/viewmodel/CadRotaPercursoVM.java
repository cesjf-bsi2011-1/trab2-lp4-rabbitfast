/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.dao.PontoReferenciaJpaController;
import br.auadottonizaidem.dao.RotaJpaController;
import br.auadottonizaidem.dao.RotaPercursoJpaController;
import br.auadottonizaidem.dao.exceptions.IllegalOrphanException;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.dao.exceptions.PreexistingEntityException;
import br.auadottonizaidem.entity.Localidade;
import br.auadottonizaidem.entity.PontoReferencia;
import br.auadottonizaidem.entity.Rota;
import br.auadottonizaidem.entity.RotaPercurso;
import br.auadottonizaidem.entity.RotaPercursoPK;
import br.auadottonizaidem.viewmodelutil.StatusCrud;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import static sun.net.www.http.HttpClient.New;

/**
 *
 * @author Willian
 */
public class CadRotaPercursoVM {

    private List<PontoReferencia> listaPontoReferencia;
    private RotaPercurso selected;
    private List<Rota> listaRotas;
    private Rota rota;
    private Localidade origem;
    private Localidade destino;
    @Wire
    private Window telaCadRotaPercurso;
    private StatusCrud status;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        listaRotas = new RotaJpaController(emf).findRotaEntities();
        rota = new Rota();
        selected = new RotaPercurso();
        status = StatusCrud.insert;
    }

    @NotifyChange({"selected", "status"})
    @Command
    public void open() {
        status = StatusCrud.view;
        telaCadRotaPercurso.doModal();

    }

    @NotifyChange({"selected", "status"})
    @Command
    public void novo() {
        selected = new RotaPercurso();
        status = StatusCrud.insert;
        telaCadRotaPercurso.doModal();

    }

    @NotifyChange({"listaPontoReferencia", "selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void gravaRotaPercurso(@BindingParam("lb") Component listBox) {
        
        try{
        int i = 1;
        for (Component li : listBox.getChildren()) {
            RotaPercurso rp = new RotaPercurso();
            PontoReferencia pr = ((Listitem)li).getValue();
            rp.setPontoReferencia(pr);
            rp.setSequencia(i++);
            rp.setRotaPercursoPK(new RotaPercursoPK(rota.getIdRota(), pr.getIdPontoReferencia()));
            String distancia = ((String)((Textbox)li.getChildren().get(1).getChildren().get(0)).getValue());
            
            rp.setDistancia(Double.parseDouble(distancia));
            
            rota.getRotaPercursoList().add(rp);
        }
        }catch(Exception e){
            Messagebox.show("Algo deu errado, Confira os campos necessarios");
        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        try {
            new RotaJpaController(emf).edit(rota);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(CadRotaPercursoVM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CadRotaPercursoVM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CadRotaPercursoVM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Messagebox.show("Rota Percurso Cadastrado com sucesso!");
        telaCadRotaPercurso.setVisible(false);
    }

    @NotifyChange({"listaPontoReferencia"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void invoqTelaCadastro() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        listaPontoReferencia = new PontoReferenciaJpaController(emf).findPontoReferenciaEntities();
        telaCadRotaPercurso.doModal();
    }

    @Command
    public void move(@BindingParam("dragged") Component dragged, @BindingParam("droped") Component droped) {
        if (droped instanceof Listitem) {
            ((Listitem) droped).getParent().insertBefore(dragged, droped);
        } else {
            droped.appendChild(dragged);
        }
        boolean aux = droped.getId().equals("right");
        if (droped instanceof Listitem) {
            aux = droped.getParent().getId().equals("right");
        }
        ((Textbox) dragged.getChildren().get(1).getChildren().get(0)).setVisible(aux);
       // PontoReferencia r = (PontoReferencia) droped.getParent().getChildren().get(0);

    }

    public List<PontoReferencia> getListaPontoReferencia() {
        return listaPontoReferencia;
    }

    public void setListaPontoReferencia(List<PontoReferencia> listaPontoReferencia) {
        this.listaPontoReferencia = listaPontoReferencia;
    }

    public RotaPercurso getSelected() {
        return selected;
    }

    public void setSelected(RotaPercurso selected) {
        this.selected = selected;
    }

    public List<Rota> getListaRotas() {
        return listaRotas;
    }

    public void setListaRotas(List<Rota> listaRotas) {
        this.listaRotas = listaRotas;
    }

    public Rota getRota() {
        return rota;
    }

    public void setRota(Rota rota) {
        this.rota = rota;
    }

    public Localidade getOrigem() {
        return origem;
    }

    public void setOrigem(Localidade origem) {
        this.origem = origem;
    }

    public Localidade getDestino() {
        return destino;
    }

    public void setDestino(Localidade destino) {
        this.destino = destino;
    }

    public Window getTelaCadRotaPercurso() {
        return telaCadRotaPercurso;
    }

    public void setTelaCadRotaPercurso(Window telaCadRotaPercurso) {
        this.telaCadRotaPercurso = telaCadRotaPercurso;
    }
    
    

    public StatusCrud getStatus() {
        return status;
    }

    public void setStatus(StatusCrud status) {
        this.status = status;
    }
}
