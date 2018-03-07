/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.errorui;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.principal.DataModelTicket;
import com.tpv.util.Connection;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx8tpv1.TabPanePrincipalController;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;
import com.tpv.principal.Context;
        

/**
 *
 * @author daniel
 */

@FXMLController(value="Error.fxml", title = "Error de Sistema")
public class ErrorController implements Initializable {
    private TabPanePrincipalController tabController;
    Logger log = Logger.getLogger(ErrorController.class);
//    @FXML
//    @ActionTrigger("salir")
//    private Button salirButton;
//    
//    @FXML
//    @ActionTrigger("reintentar")
//    private Button reintentarButton;
    
    
    @FXMLViewFlowContext
    private ViewFlowContext context;    
    
    
    @FXML
    private Button facturacionButton;
    
    @FXML
    private Button menuButton;
    
    @FXML
    private Button confirmarTicketButton;
    
    @FXML
    private Button buscarClienteButton; 
    
    @FXML
    private Button buscarProdButton; 

    @FXML
    private Button pagoTicketButton; 
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button supervisorButton;
    
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private TextArea textAreaError;
    
    
    
    
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
            Platform.runLater(()->{
                textAreaError.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        log.debug("Tecla Escape pulsada");
                        recuperarFallo();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_FACTURACION)
                            tabController.gotoFacturacion();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_MENUPRINCIPAL)
                            tabController.gotoMenuPrincipal();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET)
                            confirmarTicketButton.fire();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_BUSCARPORNOMBRECLIENTE)
                            buscarClienteButton.fire();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_BUSCARPORDESCPRODUCTO)
                            buscarProdButton.fire();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_PAGOTICKET)
                            pagoTicketButton.fire();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_LOGIN)
                            tabController.gotoLogin();
                        
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_SUPERVISOR)
                            supervisorButton.fire();
                        
                        
                    }
                    if(keyEvent.getCode()==KeyCode.F12)
                        System.exit(0);
                    
                });
            });
                    
    }
    
    private void recuperarFallo(){
        if(Context.getInstance().currentDMTicket().getTpvException().getExceptionOrigen() instanceof ConnectException
           || Context.getInstance().currentDMTicket().getTpvException().getExceptionOrigen() instanceof UnknownHostException){
            reconectarImpresora();
        }
    }
    
    private void reconectarImpresora(){
        try{
            Connection.initFiscalPrinter();
        }catch(TpvException e){
            log.error("Error al reconectar la impresora fiscal");
        }
    }
    
    public void setTabController(TabPanePrincipalController tabController){
        this.tabController=tabController;
    }
    
    public void configurarInicio(){
            //if(Context.getInstance().currentDMTicket().getTpvException()!=null){
                log.info("Ingresando a pantalla de error: "+Context.getInstance().currentDMTicket().getTpvException().getMessage());
                textAreaError.setText(Context.getInstance().currentDMTicket().getTpvException().getMessage()+'\n'
                    +Context.getInstance().currentDMTicket().getTpvException().getFiscalErrorMsg());
            //}        
        
    }
}
