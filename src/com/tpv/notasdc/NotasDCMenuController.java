/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.notasdc;

import com.tpv.principal.Context;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx8tpv1.TabPanePrincipalController;

/**
 *
 * @author COMPUTOS
 */
public class NotasDCMenuController implements Initializable {
    private TabPanePrincipalController tabController;
    
    @FXML private GridPane gridPaneMenu;
    
    
    public void configurarInicio(){
        
        this.tabController.repeatFocus(gridPaneMenu);
    }
    
    public void setTabController(TabPanePrincipalController tabPane){
        this.tabController=tabPane;
    }     
    
    @FXML
    public void initialize(URL url, ResourceBundle rb){
        Platform.runLater(()->{
            gridPaneMenu.setOnKeyPressed(keyEvent->{
                keyEvent.consume();
                if(keyEvent.getCode()==KeyCode.F11)
                   tabController.gotoMenuPrincipal(); 
                if(keyEvent.getCode()==KeyCode.NUMPAD1){
                    this.tabController.gotoNotasCreditoMonto();
                }
                
                if(keyEvent.getCode()==KeyCode.NUMPAD2){
                    this.tabController.gotoNotasDCFactura();
                }
                
                if(keyEvent.getCode()==KeyCode.NUMPAD3){
                    Context.getInstance().currentDMTicket().setCliente(null);
                    Context.getInstance().currentDMTicket().setClienteSeleccionado(false);
                    Context.getInstance().currentDMTicket().setReinicioVerificado(false);
                    this.tabController.gotoNotasDCFacturaPorProducto();
                }
                    
                if(keyEvent.getCode()==KeyCode.NUMPAD4){
                    this.tabController.gotoNotasDCDebitoMonto();
                }
                
            });
        });        
    }
}
