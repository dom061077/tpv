/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.print.fiscal;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.principal.Context;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx8tpv1.TabPanePrincipalController;
import com.tpv.service.ImpresoraService;
import com.tpv.util.Connection;
import com.tpv.util.ui.TabPaneModalCommand;
import javafx.scene.control.Alert;


/**
 *
 * @author daniel
 */

public class ConfiguracionImpresoraController implements Initializable,TabPaneModalCommand {
    private ImpresoraService impresoraService = new ImpresoraService();
    Logger log = Logger.getLogger(ConfiguracionImpresoraController.class);
    private TabPanePrincipalController tabController;

    
    @FXML
    private VBox vboxMenuImpresora;
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        log.debug("Init del controlador");
        Platform.runLater(()->{
            vboxMenuImpresora.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode()==KeyCode.F11){
                    log.debug("Button volver de la pantalla de configuracion");
                    this.tabController.gotoMenuPrincipal();
                }
                if(keyEvent.getCode()==KeyCode.NUMPAD1){
                    try{
                        impresoraService.cancelarTicket();
                        /*Alert alert = new Alert(Alert.AlertType.INFORMATION,"El ticket fue cancelado");
                        alert.getDialogPane().getStylesheets().add(Connection.getCss());                        
                        alert.showAndWait();*/
                        tabController.getLabelCancelarModal().setVisible(false);
                        tabController.getLabelMensaje().setText("El ticket fue cancelado con éxito");
                        tabController.mostrarMensajeModal();
                    }catch(TpvException e){
                        log.error("Error al tratar de cancelar el ticket",e);
                        Context.getInstance().currentDMTicket().setException(e);
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONTROLADOR);
                        tabController.gotoError();
                                
                    }
                }
                if(keyEvent.getCode()==KeyCode.NUMPAD2){
                    try{
                        impresoraService.cierreZ();
                        /*Alert alert=new Alert(Alert.AlertType.INFORMATION,"El cierre diario fue correcto");
                        alert.showAndWait();
                        */
                        tabController.getLabelCancelarModal().setVisible(false);
                        tabController.getLabelMensaje().setText("El cierre diario Z fue correcto");
                        tabController.mostrarMensajeModal();
                        
                    }catch(TpvException e){
                        log.error("Error al tratar de hacer el cierre diario Z",e);
                        Context.getInstance().currentDMTicket().setException(e);
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONTROLADOR);
                        tabController.gotoError();
                                
                    }
                }
                
                if(keyEvent.getCode()==KeyCode.NUMPAD3){
                    try{
                      impresoraService.cierreX();
                    tabController.getLabelCancelarModal().setVisible(false);
                    tabController.getLabelMensaje().setText("El cierre diario X fue correcto");
                    tabController.mostrarMensajeModal();
                      
                    }catch(TpvException e){
                        log.error("Error al tratar de hacer el cierre diario X");
                    }
                }
            });

        });        
    }    
    

    
    public void setTabController(TabPanePrincipalController tabPane){
        this.tabController=tabPane;
    }


    public void configurarInicio(){
        this.tabController.repeatFocus(vboxMenuImpresora);
    }        
    
    @Override
    public void aceptarMensajeModal(){
        this.tabController.ocultarMensajeModal();
        this.tabController.getLabelCancelarModal().setVisible(true);
        this.tabController.repeatFocus(vboxMenuImpresora);
    }
    
    @Override
    public void cancelarMensajeModal(){
    }    
    

}
