/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.print.fiscal;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.principal.Context;
import com.tpv.service.FacturacionService;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx8tpv1.TabPanePrincipalController;
import com.tpv.service.ImpresoraService;
import com.tpv.util.ui.MensajeModalAceptar;
import com.tpv.util.ui.TabPaneModalCommand;
import javafx.scene.layout.BorderPane;


/**
 *
 * @author daniel
 */

public class ConfiguracionImpresoraController implements Initializable,TabPaneModalCommand {
    private ImpresoraService impresoraService = new ImpresoraService();
    private FacturacionService factService = new FacturacionService();
    Logger log = Logger.getLogger(ConfiguracionImpresoraController.class);
    private TabPanePrincipalController tabController;

    
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        log.debug("Init del controlador");
        Platform.runLater(()->{
            borderPane.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode()==KeyCode.F11){
                    log.debug("Button volver de la pantalla de configuracion");
                    this.tabController.gotoMenuPrincipal();
                }
                if(keyEvent.getCode()==KeyCode.NUMPAD1){
                    try{
                        factService.anularFacturasAbiertas(
                                Context.getInstance().currentDMTicket().getCheckout().getId()
                                , Context.getInstance().currentDMTicket().getUsuario().getIdUsuario()
                                , Context.getInstance().currentDMTicket().getUsuarioSupervisor());
                        impresoraService.cancelarTicket();
                        //tabController.getLabelCancelarModal().setVisible(false);
                        //tabController.getLabelMensaje().setText("El ticket fue cancelado con éxito");
                        //tabController.mostrarMensajeModal();
                        
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
                        
                        //tabController.getLabelCancelarModal().setVisible(false);
                        //tabController.getLabelMensaje().setText("El cierre diario Z fue correcto");
                        //tabController.mostrarMensajeModal();
                        tabController.showMsgModal(new MensajeModalAceptar("Mensaje"
                                ,"El cierre diario Z fue correcto","",borderPane));
                        
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
                        //tabController.getLabelCancelarModal().setVisible(false);
                        //tabController.getLabelMensaje().setText("El cierre diario X fue correcto");
                        //tabController.mostrarMensajeModal();
                        tabController.showMsgModal(new MensajeModalAceptar("Mensaje"
                                ,"El cierre diario X fue correcto","",borderPane));
                      
                    }catch(TpvException e){
                        log.error("Error al tratar de hacer el cierre diario X");
                    }
                }
                keyEvent.consume();
            });

        });        
    }    
    

    
    public void setTabController(TabPanePrincipalController tabPane){
        this.tabController=tabPane;
        
    }


    public void configurarInicio(){
        this.tabController.repeatFocus(borderPane);
        this.tabController.setTabPaneModalCommand(this);
    }        
    
    @Override
    public void aceptarMensajeModal(){
        this.tabController.ocultarMensajeModal();
        this.tabController.getLabelCancelarModal().setVisible(true);
        this.tabController.repeatFocus(borderPane);
        
    }
    
    @Override
    public void cancelarMensajeModal(){
    }    
    

}
