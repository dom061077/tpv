/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.retirodinero;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.RetiroDinero;
import com.tpv.service.RetiroDineroService;
import com.tpv.util.ui.EditableBigDecimalTableCell;
import com.tpv.util.ui.TabPaneModalCommand;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    
    
    @FXML TableColumn billeteColumn;
    @FXML TableColumn cantidadRetiradaColumn;
    @FXML TableColumn valorRetiradoColumn;
    @FXML TableView tableViewRetiro;
    @FXML Label totalRetiroLabel;    
    
    @FXML
    public void initialize(URL url, ResourceBundle rb){
        log.info("Ingreso al método initialize");
        
        retiroDineroDataList = FXCollections.observableArrayList();
        tableViewRetiro.setItems(new SimpleListProperty<>(retiroDineroDataList));
        billeteColumn.setCellValueFactory(new PropertyValueFactory("descripcionBillete"));
        billeteColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        cantidadRetiradaColumn.setCellValueFactory(new PropertyValueFactory("CantidadBilletes"));
        cantidadRetiradaColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        valorRetiradoColumn.setCellValueFactory(new PropertyValueFactory("montoTotal"));
        valorRetiradoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        Platform.runLater(()->{
            
        });
    }
    
    public void configurarInicio() throws TpvException{
        tableViewRetiro.getItems().clear();
        
    }
    
    private void cargarRetiro() throws TpvException{
        List<RetiroDinero> retiros = retiroDineroService.getRetiros();
        for(Iterator<RetiroDinero> it = retiros.iterator();it.hasNext();){
            RetiroDinero item = it.next();
            RetiroDineroConfirmacionData ret = new RetiroDineroConfirmacionData();
            ret.set
        }
    }
    
    public void setTabController(TabPanePrincipalController tabController){
        this.tabController=tabController;
    }
    
    public void aceptarMensajeModal(){
    }
    
    public void cancelarMensajeModal(){
    }
    
}
