/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.retirodinero;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.RetiroDinero;
import com.tpv.principal.Context;
import com.tpv.service.RetiroDineroService;
import com.tpv.util.ui.TabPaneModalCommand;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;

/**
 *
 * @author COMPUTOS
 */
public class RetiroDineroConfirmacionController implements Initializable, TabPaneModalCommand {
    Logger log = Logger.getLogger(RetiroDineroController.class);
    private TabPanePrincipalController tabController;
    private RetiroDineroService retiroDineroService = new RetiroDineroService();
    private ObservableList<RetiroDineroConfirmacionData> retiroDineroDataList;
    private RetiroDineroConfirmacionData selectedItem;
    private boolean readOnly;
    
    @FXML TableColumn idRetiroDineroColumn;
    @FXML TableColumn fechaHoraCargaColumn;
    @FXML TableColumn montoColumn;
    @FXML TableColumn estadoColumn;
    @FXML TableView tableViewRetiro;
    @FXML Label totalRetiroLabel;    
    
    @FXML
    public void initialize(URL url, ResourceBundle rb){
        log.info("Ingreso al método initialize");
        
        retiroDineroDataList = FXCollections.observableArrayList();
        tableViewRetiro.setItems(new SimpleListProperty<>(retiroDineroDataList));
        idRetiroDineroColumn.setCellValueFactory(new PropertyValueFactory("IdRetiro"));
        idRetiroDineroColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        fechaHoraCargaColumn.setCellValueFactory(new PropertyValueFactory("FechaAlta"));
        fechaHoraCargaColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        fechaHoraCargaColumn.setCellFactory(col ->{
            TableCell<RetiroDineroConfirmacionData,java.util.Date> cell = new TableCell<RetiroDineroConfirmacionData,java.util.Date>(){
                @Override
                public void updateItem(java.util.Date item,boolean empty){
                    super.updateItem(item,empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if(!empty){
                        SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
                        this.setText(df.format(item));
                    }
                }
            };
            return cell;
        });

        estadoColumn.setCellValueFactory(new PropertyValueFactory("Estado"));
        estadoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        
        montoColumn.setCellValueFactory(new PropertyValueFactory("MontoTotal"));
        montoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        montoColumn.setCellFactory(col ->{
            TableCell<RetiroDineroConfirmacionData,BigDecimal> cell = new TableCell<RetiroDineroConfirmacionData,BigDecimal>(){
                @Override
                public void updateItem(BigDecimal item,boolean empty){
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                            //String formattedDob = De
                            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
            
        });
        
        
        
        
        
        Platform.runLater(()->{
            tableViewRetiro.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.F11){
                    if(this.readOnly)
                        this.tabController.gotoMenuRetiroDinero();
                    else
                        this.tabController.gotoMenuPrincipal();
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.TAB){
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.ENTER && !this.readOnly){
                    keyEvent.consume();
                    //int index = tableViewRetiro.getSelectionModel().getSelectedIndex();
                    selectedItem = (RetiroDineroConfirmacionData)tableViewRetiro.getSelectionModel().getSelectedItem();
                    tabController.getLabelMensaje().setText("¿Confirma el retiro de dinero Nº: "+
                            +selectedItem.getIdRetiro()
                            +"?");
                    tabController.mostrarMensajeModal();
                }
            });    
        });
    }
    
    public void configurarInicio(boolean readOnly) throws TpvException{
        this.readOnly = readOnly;
        if(this.readOnly){
            this.tabController.getLabelTituloVentana().setText("LISTADO DE CARGAS DE RETIRO DE DINERO");
            this.tabController.getLabelShortCut().setText("F11 - Retorna a Menú Principal");
        }else{
            this.tabController.getLabelTituloVentana().setText("CONFIRMACION DE RETIRO DE DINERO");
            this.tabController.getLabelShortCut().setText("Enter - Confirma Retiro | F11 - Retorna a Menú Principal");
        }
        tableViewRetiro.getItems().clear();
        cargarRetiro();
        this.tabController.repeatFocus(tableViewRetiro);
        this.tabController.setTabPaneModalCommand(this);
        tableViewRetiro.getSelectionModel().select(0);        
        
    }
    
    private void cargarRetiro() throws TpvException{
        List<RetiroDinero> retiros = retiroDineroService.getRetiros(Context.getInstance().currentDMTicket().getCheckout().getId()
                ,Context.getInstance().currentDMTicket().getUsuario().getIdUsuario());
        for(Iterator<RetiroDinero> it = retiros.iterator();it.hasNext();){
            RetiroDinero item = it.next();
            
            
            RetiroDineroConfirmacionData ret = 
                    new RetiroDineroConfirmacionData(
                            item.getId(),
                            item.getFechaAlta(),
                            item.getEstado(),
                            item.getMonto()
                    );
            
            this.retiroDineroDataList.add(ret);
        }
    }
    
    private void confirmarRetiro(Long idRetiro){
        try{
            retiroDineroService.confirmarRetiro(idRetiro,Context.getInstance()
                    .currentDMTicket().getUsuarioSupervisor());
        }catch(TpvException e){
            log.error("Error en controlador llamando al método confirmarRetiro de RetiroDineroService",e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARETIRODINERO);
            Context.getInstance().currentDMTicket().setException(e);
            tabController.gotoError();            
        }
    }
    
    public void setTabController(TabPanePrincipalController tabController){
        this.tabController=tabController;
    }
    
    public void aceptarMensajeModal(){
        confirmarRetiro(this.selectedItem.getIdRetiro());
        tabController.ocultarMensajeModal();
        tabController.gotoMenuPrincipal();
    }
    
    public void cancelarMensajeModal(){
        tabController.ocultarMensajeModal();
        this.tabController.repeatFocus(tableViewRetiro);
    }
    
    
    
    
}
