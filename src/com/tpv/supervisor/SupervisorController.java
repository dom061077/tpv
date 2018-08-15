/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.supervisor;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Usuario;
import com.tpv.principal.Context;
import com.tpv.print.event.FiscalPrinterEvent;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.UsuarioService;
import com.tpv.util.ui.MaskTextField;
import com.tpv.util.ui.MensajeModalAceptar;
import com.tpv.util.ui.TabPaneModalCommand;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;
import org.tpv.print.fiscal.FiscalPacket;
import org.tpv.print.fiscal.FiscalPrinter;
import org.tpv.print.fiscal.hasar.HasarCommands;

/**
 *
 * @author daniel
 */

        

public class SupervisorController implements Initializable{
    Logger log = Logger.getLogger(SupervisorController.class);

    private ImpresoraService impresoraService = new ImpresoraService();
    private FacturacionService facturaService = new FacturacionService();
    private UsuarioService usuarioService = new UsuarioService();    
    private TabPanePrincipalController tabController;
    private FiscalPrinterEvent fiscalPrinterEvent;

    
    //@FXML
    //private Label labelTitulo;
    
    @FXML
    private TextField textFieldCodigoSupervisor;
    
    @FXML
    private TextField textFieldPassword;
    
    
    private MaskTextField textFieldCodigoBarra;
    
    @FXML
    private StackPane stackPaneError;
    
    @FXML
    private Label labelError;
    
    @FXML
    private BorderPane borderPaneIngreso;
    
    @FXML
    private GridPane gridPane;
    


    public void configurarInicio(){
        //stackPaneError.setVisible(false);
        //labelTitulo.setText(Context.getInstance().currentDMTicket().getTipoTituloSupervisor().getTitulo());
        
        textFieldCodigoSupervisor.setText("");
        textFieldPassword.setText("");
        textFieldCodigoBarra.setText("");
        tabController.repeatFocus(textFieldCodigoSupervisor);
        if(impresoraService.getHfp().getEventListener()==null)
            asignarEvento();        
    }
    
    

    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
            log.info("Ingresando al método init");
            
            /*labelError.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    stackPaneError.setVisible(false);
                    borderPaneIngreso.setDisable(false);
                    textFieldCodigoSupervisor.requestFocus();
                    keyEvent.consume();
                    
                }
                if(keyEvent.getCode() == KeyCode.TAB){
                    keyEvent.consume();
                }
            });
            */
            textFieldCodigoSupervisor.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    switch(Context.getInstance().currentDMTicket().getTipoTituloSupervisor()){
                        case HABILITAR_MENU:
                            tabController.gotoFacturacion();
                            break;
                        case HABILITAR_NEGATIVO:
                            tabController.gotoFacturacion();
                            break;
                        case CANCELAR_TICKET:
                            tabController.gotoFacturacion();
                            break;
                        case HABILITAR_CONTROLADOR:
                            tabController.gotoMenuPrincipal();
                            break;
                        case HABILITAR_CONFIRMARETIRODINERO:
                            tabController.gotoMenuPrincipal();
                            break;
                        case CANCELAR_PAGO:
                            tabController.gotoPago();
                            break;
                        case CANCELAR_CONFIRMACION_PAGO:
                            tabController.gotoConfirmarPago();
                            break;
                    }                    
                    
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.ENTER){
                    textFieldPassword.requestFocus();
                    tabController.repeatFocus(textFieldPassword);
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.TAB){
                    keyEvent.consume();
                    return;
                }
                    
            });
            
            textFieldPassword.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    tabController.repeatFocus(textFieldCodigoSupervisor);
                    keyEvent.consume();
                    return;
                }
                if(keyEvent.getCode()==KeyCode.ENTER){
                    textFieldCodigoBarra.setDisable(false);
                    tabController.repeatFocus(textFieldCodigoBarra);
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.TAB){
                    keyEvent.consume();
                    return;
                }
                
            });
            
            textFieldCodigoBarra = new MaskTextField();
            textFieldCodigoBarra.setMask("N!");
            textFieldCodigoBarra.getStyleClass().add("textfield_sin_border");
            
            gridPane.add(textFieldCodigoBarra, 1, 2);
            
            textFieldCodigoBarra.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    Usuario usuario = null;
                    try{        
                            usuario = usuarioService.authenticarSupervisor(textFieldCodigoSupervisor.getText()
                            ,textFieldPassword.getText(), textFieldCodigoBarra.getText());
                            if(usuario==null){
                                //this.tabController.getLabelMensaje().setText("Credenciales de Supervisor Incorrectas");
                                //this.tabController.getLabelCancelarModal().setVisible(false);
                                //this.tabController.mostrarMensajeModal();
                                tabController.showMsgModal(
                                        new MensajeModalAceptar("Error"
                                                ,"Credenciales de Supervisor Incorrectas"
                                                ,"", textFieldCodigoSupervisor  )
                                );
                                
                            }else{
                                /*if(Context.getInstance().currentDMTicket().getTipoTituloSupervisor()==TipoTituloSupervisorEnum.HABILITAR_MENU){
                                    tabController.gotoMenuPrincipal();
                                }else{
                                    if(Context.getInstance().currentDMTicket().getTipoTituloSupervisor()==TipoTituloSupervisorEnum.HABILITAR_NEGATIVO)
                                        habilitarNegativos(true);
                                    if(Context.getInstance().currentDMTicket().getTipoTituloSupervisor()==TipoTituloSupervisorEnum.CANCELAR_TICKET)
                                        cancelarTicketCompleto();
                                    keyEvent.consume();
                                    tabController.gotoFacturacion();
                                }*/
                                Context.getInstance().getDataModelTicket().setUsuarioSupervisor(usuario);
                                switch(Context.getInstance().currentDMTicket().getTipoTituloSupervisor()){
                                    case HABILITAR_MENU:
                                        tabController.gotoMenuPrincipal();
                                        break;
                                    case HABILITAR_NEGATIVO:
                                        habilitarNegativos(true);
                                        tabController.gotoFacturacion();
                                        break;
                                    case CANCELAR_TICKET:
                                        cancelarTicketCompleto();
                                        tabController.gotoFacturacion();
                                        break;
                                    case HABILITAR_CONTROLADOR:
                                        tabController.gotoControlador();
                                        break;
                                    case HABILITAR_CONFIRMARETIRODINERO:
                                        tabController.gotoRetiroDineroConfirmacion(false);
                                        break;
                                    case CANCELAR_PAGO:
                                        tabController.gotoFacturacion();
                                        break;
                                    case CANCELAR_CONFIRMACION_PAGO:
                                        tabController.gotoPago();
                                        break;
                                }
                                keyEvent.consume();
                            }
                            
                    }catch(TpvException e){
                        log.error("Error: "+e.getMessage());
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_SUPERVISOR);
                        Context.getInstance().currentDMTicket().setException(e);
                        tabController.gotoError();
                        
                    }
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldCodigoBarra.setDisable(true);
                    tabController.repeatFocus(textFieldPassword);
                    keyEvent.consume();
                    
                }
                
                if(keyEvent.getCode() == KeyCode.TAB){
                    keyEvent.consume();
                    return;
                }
            });

            
            
    }
    
    private void habilitarNegativos(boolean habilita){
        Context.getInstance().currentDMTicket().setImprimeComoNegativo(habilita);
    }
    
    
    private void cancelarTicketCompleto() throws TpvException{
            impresoraService.cancelarTicket();
    }
    
    public void setTabController(TabPanePrincipalController tabController){
        this.tabController=tabController;
    }
    
    private void asignarEvento(){
        
        this.fiscalPrinterEvent = new FiscalPrinterEvent(){
            
            @Override
             public void commandExecuted(FiscalPrinter source, FiscalPacket command, FiscalPacket response){
                log.debug("Evento despues de cerrar cancelar ticket");
                if(command.getCommandCode()==HasarCommands.CMD_CANCEL_DOCUMENT){
                    try{
                        facturaService.anularFacturaPorSupervisor(Context.getInstance().currentDMTicket().getIdFactura()
                                ,Context.getInstance().currentDMTicket().getUsuarioSupervisor()
                        );
                        Context.getInstance().currentDMTicket().setCliente(null);
                        Context.getInstance().currentDMTicket().setClienteSeleccionado(false);
                        Context.getInstance().currentDMTicket().getDetalle().clear();
                        Context.getInstance().currentDMTicket().getPagos().clear();
                    }catch(TpvException e){
                        Context.getInstance().currentDMTicket().setException(e);
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
                        tabController.gotoError();
                    }
                }
            }
        };
        
        impresoraService.getHfp().setEventListener(this.fiscalPrinterEvent);
        
    }
    
    public void aceptarMensajeModal(){
        this.tabController.getLabelCancelarModal().setVisible(true);
        this.tabController.ocultarMensajeModal();
        this.tabController.repeatFocus(textFieldCodigoSupervisor);
    }
    
    public void cancelarMensajeModal(){
    }
        
    
}
