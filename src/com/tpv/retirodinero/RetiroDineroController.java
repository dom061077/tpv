/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.retirodinero;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Billete;
import com.tpv.modelo.RetiroDinero;
import com.tpv.modelo.RetiroDineroDetalle;
import com.tpv.modelo.enums.RetiroDineroEnum;
import com.tpv.principal.Context;
import com.tpv.service.RetiroDineroService;
import com.tpv.util.ui.EditableBigDecimalTableCell;
import com.tpv.util.ui.TabPaneModalCommand;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;

/**
     *
 * @author COMPUTOS
 */
public class RetiroDineroController implements Initializable,TabPaneModalCommand {
    Logger log = Logger.getLogger(RetiroDineroController.class);
    private TabPanePrincipalController tabController;
    private RetiroDineroService retiroDineroService = new RetiroDineroService();
    private ObservableList<RetiroDineroData> retiroDineroDataList;
    
    @FXML BorderPane borderPane;
    @FXML GridPane gridPane;
    
    @FXML TableColumn billeteColumn;
    @FXML TableColumn cantidadRetiradaColumn;
    @FXML TableView tableViewRetiro;
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {    
        log.info("Ingreso la metodo initialize ");
        
        
        tableViewRetiro.setEditable(true);
        tableViewRetiro.getSelectionModel().setCellSelectionEnabled(true);
        
        retiroDineroDataList = FXCollections.observableArrayList();
        tableViewRetiro.setItems(new SimpleListProperty<>(retiroDineroDataList));
        billeteColumn.setCellValueFactory(new PropertyValueFactory("descripcionBillete"));
        billeteColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        cantidadRetiradaColumn.setCellValueFactory(new PropertyValueFactory("CantidadBilletes"));
        cantidadRetiradaColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        cantidadRetiradaColumn.setCellFactory(col -> new EditableBigDecimalTableCell<RetiroDineroData>());
        
        Platform.runLater(()->{
            tableViewRetiro.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.TAB){
                    keyEvent.consume();
                    return;
                }
                if(keyEvent.getCode() == KeyCode.F11)
                    tabController.gotoMenuPrincipal();
                TablePosition tp;
                tp = tableViewRetiro.getFocusModel().getFocusedCell();
                tableViewRetiro.edit(tp.getRow(), tp.getTableColumn());
            });
            cantidadRetiradaColumn.setOnEditCommit(
                new EventHandler<CellEditEvent<RetiroDineroData, Integer>>() {
                    public void handle(CellEditEvent<RetiroDineroData, Integer> t) {
                        ((RetiroDineroData) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setCantidadBilletes(t.getNewValue());
                        
                        TablePosition tp = t.getTableView().getFocusModel().getFocusedCell();
                        int nextRow = tp.getRow();
                        if(tp.getRow()==(retiroDineroDataList.size()-1)){
                            tabController.getLabelMensaje().setText("¿Confirma la carga de retiro de dinero?");
                            tabController.mostrarMensajeModal();
                        }else    
                            t.getTableView().getSelectionModel().select(nextRow+1, cantidadRetiradaColumn);
                    }
                }                  
            );
        });
        
    }

    public void configurarInicio() throws TpvException{
        tableViewRetiro.getItems().clear();
        this.tabController.setTabPaneModalCommand(this);
        tabController.repeatFocus(tableViewRetiro);
        /*cantidadRetiradaColumn.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<RetiroDineroData,String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<RetiroDineroData, String> t) {
                    tabController.repeatFocus(tableViewRetiro);
                }
            }                
        );*/
        
        cargarBilletes();
        tableViewRetiro.getSelectionModel().select(0, cantidadRetiradaColumn);
        
    }
    
    public void setTabController(TabPanePrincipalController tabController){
        this.tabController=tabController;
    }
    
    
    private void cargarBilletes() throws TpvException{
        List<Billete> billetes = retiroDineroService.getBilletes();
        retiroDineroDataList.clear();
        billetes.forEach(billete->{
            retiroDineroDataList.add(new RetiroDineroData(billete.getId()
                    ,billete.getDetalleFormulario()
                    ,billete.getValor(),0)
            );
        });
        
    }
    
    
    
    public void aceptarMensajeModal(){
        RetiroDinero retiroDinero = null;
        this.guardarRetiro();
    }
    
    public void cancelarMensajeModal(){
        this.tabController.ocultarMensajeModal();
        tabController.repeatFocus(tableViewRetiro);
    }
    
    
    public void guardarRetiro(){
        log.info("Guardando retiro de dinero");
        RetiroDinero retiroDinero = new RetiroDinero();
        try{
            retiroDinero.setEstado(RetiroDineroEnum.PENDIENTE);
            retiroDinero.setCheckout(Context.getInstance().currentDMTicket().getCheckout());
            retiroDinero.setUsuario(Context.getInstance().currentDMTicket().getUsuario());
            for(Iterator<RetiroDineroData> it = retiroDineroDataList.iterator();it.hasNext();){
                RetiroDineroData item = it.next();
                RetiroDineroDetalle retDetalle = new RetiroDineroDetalle();
                retDetalle.setBillete(retiroDineroService.getBillete(item.getIdBillete()));
                retDetalle.setCantidadBilletes(item.getCantidadBilletes());
                retDetalle.setMonto(BigDecimal.ONE);
                retiroDinero.getDetalle().add(retDetalle);
            }
            retiroDineroService.registrarRetiro(retiroDinero);
            this.tabController.gotoMenuPrincipal();
        }catch(TpvException e){
            log.error("Error en controlador llamando al método registrarRetiro de RetiroDineroService",e);
            tabController.ocultarMensajeModal();
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CARGARETIRODINERO);
            Context.getInstance().currentDMTicket().setException(e);
            tabController.gotoError();
        }
    }
    
    
}
