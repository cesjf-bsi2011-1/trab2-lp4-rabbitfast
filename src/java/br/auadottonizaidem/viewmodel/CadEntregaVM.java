/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auadottonizaidem.viewmodel;

import br.auadottonizaidem.dao.ClienteJpaController;
import br.auadottonizaidem.dao.LocalidadeJpaController;
import br.auadottonizaidem.dao.RotaJpaController;
import br.auadottonizaidem.dao.VeiculoJpaController;
import br.auadottonizaidem.dao.EmpresaJpaController;
import br.auadottonizaidem.dao.EntregaJpaController;
import br.auadottonizaidem.dao.exceptions.IllegalOrphanException;
import br.auadottonizaidem.dao.exceptions.NonexistentEntityException;
import br.auadottonizaidem.entity.Cliente;
import br.auadottonizaidem.entity.Empresa;
import br.auadottonizaidem.entity.Entrega;
import br.auadottonizaidem.entity.Localidade;
import br.auadottonizaidem.entity.Rota;
import br.auadottonizaidem.entity.RotaPercurso;
import br.auadottonizaidem.entity.Status;
import br.auadottonizaidem.entity.Veiculo;
import br.auadottonizaidem.viewmodelutil.StatusCrud;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author Willian
 */
public class CadEntregaVM {

    private List<Rota> listaRotas;
    private List<Localidade> listaLocalidades;
    private Session sessao;
    private Rota selected;
    private Cliente cliente;
    private Entrega entrega;
    private Veiculo veiculo;
    private double valor;
    private Localidade origem;
    private Localidade destino;
    private Query query;
    private EntityManager entity;
    private Empresa empresa;

    @Wire
    private Window fmrCadEntregas;
    private StatusCrud status;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);//sempre colocar pra pegar uma window interna
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        listaRotas = new RotaJpaController(emf).findRotaEntities();
        listaLocalidades = new LocalidadeJpaController(emf).findLocalidadeEntities();
        empresa = new EmpresaJpaController(emf).findEmpresa(1);
        entrega = new Entrega();

        //cliente = (Cliente) sessao.getAttribute("user");
        cliente = new Cliente();
        cliente.setIdCliente(1);
        selected = new Rota();
        origem = new Localidade();
        destino = new Localidade();
        status = StatusCrud.insert;

    }

    @NotifyChange({"selected", "origem", "destino", "status"})
    @Command
    public void open() {
        status = StatusCrud.view;
        fmrCadEntregas.doModal();

    }

    @NotifyChange({"selected", "origem", "destino", "status"})
    @Command
    public void novo() {
        selected = new Rota();
        origem = new Localidade();
        destino = new Localidade();
        status = StatusCrud.insert;
        fmrCadEntregas.doModal();

    }

    @NotifyChange({"status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void alteraEntrega() {
        status = StatusCrud.edit;

    }

    @NotifyChange({"selected", "status"})//para atualizar assim que gravar no banco de dados.
    @Command
    public void gravaEntrega() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");

        cliente.setIdCliente(1);
        if (status == StatusCrud.insert) {
            try {
                selected.setLocOrigem(origem);
                selected.setLocDestino(destino);
                new RotaJpaController(emf).create(selected);
                entrega.setIdRota(selected);
                entrega.setIdCliente(cliente);
                if (verificaVeiculo()) {
                    entrega.setValor(valor);
                    entrega.setPlacaVeiculo(veiculo);
                    veiculo.setEstado("E");
                    new VeiculoJpaController(emf).edit(veiculo);
                    new EntregaJpaController(emf).create(entrega);
                    Messagebox.show("Entrega Registrada com Sucesso, Acompanhe o Status de Entrega.");
                } else {
                    Messagebox.show("Nenhum Veículo disponivel no momento");
                }

            } catch (Exception ex) {
                Logger.getLogger(CadRotaVM.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (status == StatusCrud.edit) {
            try {
                new EntregaJpaController(emf).edit(entrega);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(CadRotaVM.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CadRotaVM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fmrCadEntregas.setVisible(false);

    }

    private boolean verificaVeiculo() {
        if (calculaValorEntrega() != 0) {
            return true;
        } else {
            return false;
        }
    }

    private void registrarStatus(Rota rota, RotaPercurso rotaPercurso, Entrega entrega) {
        Status st = new Status();
    }

    @Command
    public double calculaValorEntrega() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
        entity = emf.createEntityManager();
        double peso = entrega.getPeso();
        valor = 0;

        query = entity.createNamedQuery("Veiculo.findEstadoParado");
        List<Veiculo> listParado = query.getResultList(); //lista de veiculos parados caso P(parado) e E(entregando)

        if (peso <= 50) { //Verifica se existe tipo de veiculo moto parado
            int tipo;

            for (int i = 0; i < listParado.size(); i++) {
                tipo = listParado.get(i).getTipoVeiculo();
                if (tipo == 1) {
                    veiculo = listParado.get(i);
                    valor = empresa.getPrecoMoto();
                } else {
                    valor = 0;
                }
            }

            return valor;

        } else if (peso > 50 && peso <= 450) { //Verifica se existe tipo de veiculo carro parado
            int tipo;

            for (int i = 0; i < listParado.size(); i++) {
                tipo = listParado.get(i).getTipoVeiculo();
                if (tipo == 2) {
                    veiculo = listParado.get(i);
                    valor = empresa.getPrecoCarro();
                } else {
                    valor = 0;
                }
            }

            return valor;
        } else { //Verifica se existe tipo de veiculo caminhão parado
            int tipo;

            for (int i = 0; i < listParado.size(); i++) {
                tipo = listParado.get(i).getTipoVeiculo();
                if (tipo == 3) {
                    veiculo = listParado.get(i);
                    valor = empresa.getPrecoCaminhao();
                } else {
                    valor = 0;
                }
            }

            return valor;
        }
    }

    @Command
    public void apagaEntregas() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("trab2-lp4-rabbitfastPU");
            try {
                new RotaJpaController(emf).destroy(selected.getIdRota());
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(CadRotaVM.class.getName()).log(Level.SEVERE, null, ex);
            }
            fmrCadEntregas.setVisible(false);
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

    public void setOrigem(Localidade origem) {
        this.origem = origem;
    }

    public Localidade getDestino() {
        return destino;
    }

    public void setDestino(Localidade destino) {
        this.destino = destino;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Window getFmrCadEntregas() {
        return fmrCadEntregas;
    }

    public void setFmrCadEntregas(Window fmrCadEntregas) {
        this.fmrCadEntregas = fmrCadEntregas;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Session getSessao() {
        return sessao;
    }

    public void setSessao(Session sessao) {
        this.sessao = sessao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

}
