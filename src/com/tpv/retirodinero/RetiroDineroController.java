/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.retirodinero;

import com.tpv.util.ui.EditableBigDecimalTableCell;
import com.tpv.util.ui.TabPaneModalCommand;
import java.math.BigDecimal;
import java.net.URL;
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
import javafx8tpv1.Main.Person;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;

/**
     *
 * @author COMPUTOS
 */
public class RetiroDineroController implements Initializable,TabPaneModalCommand {
    Logger log = Logger.getLogger(RetiroDineroController.class);
    private TabPanePrincipalController tabController;
    
    @FXML BorderPane borderPane;
    @FXML GridPane gridPane;
    
    @FXML TableColumn billeteColumn;
    @FXML TableColumn cantidadRetiradaColumn;
    @FXML TableView tableViewRetiro;
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {    
        log.info("Ingreso la metodo initialize ");
        
        initIngresos();
        tableViewRetiro.setEditable(true);
        tableViewRetiro.getSelectionModel().setCellSelectionEnabled(true);
        
        ObservableList<RetiroDineroData> list = FXCollections.observableArrayList();
        tableViewRetiro.setItems(new SimpleListProperty<>(list));
        billeteColumn.setCellValueFactory(new PropertyValueFactory("descripcionForma"));
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
                new EventHandler<CellEditEvent<RetiroDineroData, BigDecimal>>() {
                    public void handle(CellEditEvent<RetiroDineroData, BigDecimal> t) {
                        ((RetiroDineroData) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setCantidadBilletes(t.getNewValue());
                        TablePosition tp = t.getTableView().getFocusModel().getFocusedCell();
                        int nextRow = tp.getRow();
                        t.getTableView().getSelectionModel().select(nextRow+1, cantidadRetiradaColumn);
                    }
                }                  
            );
        });
        
    }

    public void configurarInicio(){
        tableViewRetiro.getItems().clear();
        this.tabController.setTabPaneModalCommand(this);
        tabController.repeatFocus(tableViewRetiro);
        tableViewRetiro.getItems().add(new RetiroDineroData(5,"$ 1.000"
        ,new BigDecimal(1000), new BigDecimal(1000)));
        tableViewRetiro.getItems().add(new RetiroDineroData(5,"$ 500"
        ,new BigDecimal(500), new BigDecimal(500)));
        tableViewRetiro.getItems().add(new RetiroDineroData(5,"$ 100"
        ,new BigDecimal(100), new BigDecimal(100)));
        tableViewRetiro.getItems().add(new RetiroDineroData(5,"$ 50"
        ,new BigDecimal(50), new BigDecimal(50)));
        tableViewRetiro.getSelectionModel().select(0, cantidadRetiradaColumn);
        /*cantidadRetiradaColumn.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<RetiroDineroData,String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<RetiroDineroData, String> t) {
                    tabController.repeatFocus(tableViewRetiro);
                }
            }                
        );*/
        
    }
    
    public void setTabController(TabPanePrincipalController tabController){
        this.tabController=tabController;
    }
    
    
    private void initIngresos(){
        
        
    }
    
    public void aceptarMensajeModal(){
        this.tabController.ocultarMensajeModal();
    }
    
    public void cancelarMensajeModal(){
        this.tabController.ocultarMensajeModal();
    }
    
    
    
    
}
