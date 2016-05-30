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
import java.io.IOException;
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
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;

/**
 *
 * @author daniel
 */

@FXMLController(value="Error.fxml", title = "Error de Sistema")
public class ErrorController implements Initializable {
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
    @ActionTrigger("facturacion")
    private Button facturacionButton;
    
    @FXML
    @ActionTrigger("menuprincipal")
    private Button menuButton;
    
    @FXML
    @ActionTrigger("confirmarticket")
    private Button confirmarTicketButton;
    
    @FXML
    @ActionTrigger("buscarcliente")
    private Button buscarClienteButton; 
    
    @FXML
    @ActionTrigger("buscarproducto")
    private Button buscarProdButton; 

    @FXML
    @ActionTrigger("pagoticket")
    private Button pagoTicketButton; 
    
    @FXML
    @ActionTrigger("login")
    private Button loginButton;
    
    @FXML
    @ActionTrigger("error_supervisor")
    private Button supervisorButton;
    
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private TextArea textAreaError;
    
    @Inject
    private DataModelTicket modelTicket;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    @PostConstruct
    public void init(){
            log.info("Ingresando a pantalla de error: "+modelTicket.getTpvException().getMessage());
            //labelError.setText(model.getTpvException().getMessage());
            textAreaError.setText(modelTicket.getTpvException().getMessage()+'\n'
                +modelTicket.getTpvException().getFiscalErrorMsg());
            Platform.runLater(()->{
                textAreaError.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        log.debug("Tecla Escape pulsada");
                        recuperarFallo();
                        if(modelTicket.getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_FACTURACION)
                            facturacionButton.fire();
                        if(modelTicket.getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_MENUPRINCIPAL)
                            menuButton.fire();
                        if(modelTicket.getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET)
                            confirmarTicketButton.fire();
                        if(modelTicket.getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_BUSCARPORNOMBRECLIENTE)
                            buscarClienteButton.fire();
                        if(modelTicket.getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_BUSCARPORDESCPRODUCTO)
                            buscarProdButton.fire();
                        if(modelTicket.getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_PAGOTICKET)
                            pagoTicketButton.fire();
                        if(modelTicket.getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_LOGIN)
                            loginButton.fire();
                        
                        if(modelTicket.getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_SUPERVISOR)
                            supervisorButton.fire();
                        
                        
                    }
                    if(keyEvent.getCode()==KeyCode.F12)
                        System.exit(0);
                    
                });
            });
                    
    }
    
    private void recuperarFallo(){
        if(modelTicket.getTpvException().getExceptionOrigen() instanceof ConnectException
           || modelTicket.getTpvException().getExceptionOrigen() instanceof UnknownHostException){
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
}
