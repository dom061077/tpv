/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.retirodinero;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.principal.Context;
import com.tpv.util.ui.TabPaneModalCommand;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;

/**
 *
 * @author COMPUTOS
 */


public class RetiroDineroMenuController implements Initializable,TabPaneModalCommand {
    Logger log = Logger.getLogger(TabPanePrincipalController.class);
    @FXML private Tab tabRetiroDineroMenu; 
    @FXML private GridPane gridPaneMenu;
    
    private TabPanePrincipalController tabController;
    
    
    public RetiroDineroMenuController(){
    }
    
    public void configurarInicio(){
        this.tabController.repeatFocus(gridPaneMenu);
    }
    
    public  void initialize(URL url, ResourceBundle rb) {    
        log.info("Ingreso la metodo initialize ");
        Platform.runLater(()->{
            gridPaneMenu.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode()==KeyCode.F11)
                   tabController.gotoMenuPrincipal(); 
                if(keyEvent.getCode()==KeyCode.NUMPAD1){
                    this.tabController.gotoRetiroDinero();
                }
                
                if(keyEvent.getCode()==KeyCode.NUMPAD2){
                    this.tabController.gotoRetiroDineroConfirmacion(true);
                }
                    
                keyEvent.consume();
            });
        });
    }
    
    public void setTabController(TabPanePrincipalController tabPane){
        this.tabController=tabPane;
    }    
    
    public void aceptarMensajeModal(){
        
    }
    
    public void cancelarMensajeModal(){
        this.tabController.ocultarMensajeModal();
        
    }    
    
}
