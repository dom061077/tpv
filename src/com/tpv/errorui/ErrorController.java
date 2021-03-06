/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.errorui;

import com.tpv.exceptions.TpvException;
import com.tpv.util.Connection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;
import com.tpv.principal.Context;
import javafx.scene.Node;
        

/**
 *
 * @author daniel
 */

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
            log.info("Ingresando al mètodo init");
            Platform.runLater(()->{
                textAreaError.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        recuperarFallo();
                        switch (Context.getInstance().currentDMTicket().getOrigenPantalla()){
                            case PANTALLA_FACTURACION:
                                tabController.gotoFacturacion();
                                break;
                            case PANTALLA_MENUPRINCIPAL:
                                tabController.gotoMenuPrincipal();
                                break;
                            case PANTALLA_CONFIRMARTICKET:
                                tabController.gotoConfirmarPago();
                                break;
                            case PANTALLA_BUSCARPORNOMBRECLIENTE:
                                tabController.gotoCliente();
                                break;
                            case PANTALLA_BUSCARPORDESCPRODUCTO:
                                tabController.gotoProducto();
                                break;
                            case PANTALLA_PAGOTICKET:
                                tabController.gotoPago();
                                break;
                            case PANTALLA_LOGIN:
                                tabController.gotoLogin();
                                break;
                            case PANTALLA_SUPERVISOR:
                                tabController.gotoSupervisor();
                                break;
                            case PANTALLA_CONTROLADOR:
                                tabController.gotoControlador();
                                break;
                        
                            case PANTALLA_CARGARETIRODINERO:
                                tabController.gotoRetiroDinero();
                                break;
                            case PANTALLA_CONFIRMARETIRODINERO:
                                tabController.gotoMenuRetiroDinero();
                                break;
                            case PANTALLA_NOTACREDITOMONTO:
                                tabController.gotoNotasCreditoMonto();
                                break;
                            case PANTALLA_MENUNOTASDC:
                                tabController.gotoNotasDCMenu();
                                break;
                            case PANTALLA_NOTACREDITOFACTURA:
                                tabController.gotoNotasDCFactura();
                                break;
                            case PANTALLA_NOTACREDITOFACTURAPROPRODUCTO:
                                tabController.gotoNotasDCFacturaPorProducto();
                                break;
                            case PANTALLA_NOTADEBITOMONTO:
                                tabController.gotoNotasDCDebitoMonto();
                                break;
                            default:
                                tabController.gotoLogin();
                                break;
                        }
                        /*if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_FACTURACION)
                            tabController.gotoFacturacion();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_MENUPRINCIPAL)
                            tabController.gotoMenuPrincipal();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET)
                            tabController.gotoConfirmarPago();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_BUSCARPORNOMBRECLIENTE)
                            tabController.gotoCliente();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_BUSCARPORDESCPRODUCTO)
                            tabController.gotoProducto();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_PAGOTICKET)
                            tabController.gotoPago();
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_LOGIN)
                            tabController.gotoLogin();
                        
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_SUPERVISOR)
                            tabController.gotoSupervisor();
                        
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_CONTROLADOR)
                            tabController.gotoControlador();
                        
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_CARGARETIRODINERO)
                            tabController.gotoRetiroDinero();
                        
                        if(Context.getInstance().currentDMTicket().getOrigenPantalla()==OrigenPantallaErrorEnum.PANTALLA_CONFIRMARETIRODINERO)
                            tabController.gotoMenuRetiroDinero();
                        */
                    }
                    if(keyEvent.getCode()==KeyCode.F12)
                        tabController.gotoMenuPrincipal();
                    
                    if(keyEvent.getCode()==KeyCode.TAB){
                        keyEvent.consume();
                    }
                    
                });
            });
                    
    }
    
    private void recuperarFallo(){
        //if(Context.getInstance().currentDMTicket().getTpvException().getExceptionOrigen() instanceof ConnectException
        //   || Context.getInstance().currentDMTicket().getTpvException().getExceptionOrigen() instanceof UnknownHostException){
        if(!Connection.isDBConnected()){
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
                repeatFocus(textAreaError);
            //}        
    }
    
    
    private void repeatFocus(Node node){
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
                repeatFocus(node);
            }
        });        
    }
    
    
}
