/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.supervisor;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.enums.TipoTituloSupervisorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Usuario;
import com.tpv.principal.Context;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.UsuarioService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;

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

    
    @FXML
    private Label labelTitulo;
    
    @FXML
    private TextField textFieldCodigoSupervisor;
    
    @FXML
    private TextField textFieldPassword;
    
    @FXML
    private StackPane stackPaneError;
    
    @FXML
    private Label labelError;
    
    @FXML
    private BorderPane borderPaneIngreso;
    


    public void configurarInicio(){
        stackPaneError.setVisible(false);
        labelTitulo.setText(Context.getInstance().currentDMTicket().getTipoTituloSupervisor().getTitulo());
        textFieldPassword.setDisable(true);
        
        textFieldCodigoSupervisor.setText("");
        textFieldPassword.setText("");
        tabController.repeatFocus(textFieldCodigoSupervisor);
    }
    
    

    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
            log.info("Ingresando al método init");
            labelError.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    stackPaneError.setVisible(false);
                    borderPaneIngreso.setDisable(false);
                    textFieldCodigoSupervisor.requestFocus();
                    keyEvent.consume();
                    
                }
            });
            
            textFieldCodigoSupervisor.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    tabController.gotoFacturacion();
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.ENTER){
                    textFieldPassword.setDisable(false);
                    textFieldPassword.requestFocus();
                    keyEvent.consume();
                }
            });
            textFieldPassword.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    Usuario usuario = null;
                    try{        
                            usuario = usuarioService.authenticarSupervisor(textFieldCodigoSupervisor.getText()
                            ,textFieldPassword.getText() );
                            if(usuario==null){
                                labelError.setText("Credenciales de Supervisor incorrectas");
                                borderPaneIngreso.setDisable(true);
                                stackPaneError.setVisible(true);
                                labelError.requestFocus();
                            }else{
                                if(Context.getInstance().currentDMTicket().getTipoTituloSupervisor()==TipoTituloSupervisorEnum.HABILITAR_MENU){
                                    tabController.gotoMenuPrincipal();
                                }else{
                                    if(Context.getInstance().currentDMTicket().getTipoTituloSupervisor()==TipoTituloSupervisorEnum.HABILITAR_NEGATIVO)
                                        habilitarNegativos(true);
                                    if(Context.getInstance().currentDMTicket().getTipoTituloSupervisor()==TipoTituloSupervisorEnum.CANCELAR_TICKET)
                                        cancelarTicketCompleto();
                                    keyEvent.consume();
                                    tabController.gotoFacturacion();
                                }
                            }
                            
                    }catch(TpvException e){
                        log.error("Error: "+e.getMessage());
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_SUPERVISOR);
                        Context.getInstance().currentDMTicket().setException(e);
                        tabController.gotoError();
                        
                    }
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldPassword.setDisable(true);
                    textFieldCodigoSupervisor.requestFocus();
                    keyEvent.consume();
                    tabController.gotoFacturacion();
                }
            });
            
            
    }
    
    private void habilitarNegativos(boolean habilita){
        Context.getInstance().currentDMTicket().setImprimeComoNegativo(habilita);
    }
    
    
    private void cancelarTicketCompleto() throws TpvException{
            impresoraService.cancelarTicket();
            facturaService.cancelarFactura(Context.getInstance().currentDMTicket().getIdFactura());
            Context.getInstance().currentDMTicket().setCliente(null);
            Context.getInstance().currentDMTicket().setClienteSeleccionado(false);
            Context.getInstance().currentDMTicket().getDetalle().clear();
            Context.getInstance().currentDMTicket().getPagos().clear();
    }
    
    public void setTabController(TabPanePrincipalController tabController){
        this.tabController=tabController;
    }
    
}
