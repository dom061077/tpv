/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.login;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.AperturaCierreCajeroDetalle;
import com.tpv.modelo.Checkout;
import com.tpv.modelo.Usuario;
import com.tpv.principal.Context;
import com.tpv.service.UsuarioService;
import com.tpv.util.ui.MensajeModalAceptar;
import com.tpv.util.ui.TabPaneModalCommand;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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

//@FXMLController(value="Login.fxml", title = "Ingreso al Sistema")

public class LoginController implements Initializable{
    private TabPanePrincipalController tabController;
    Logger log = Logger.getLogger(LoginController.class);
    UsuarioService usuarioService = new UsuarioService();    
    
    
    
    @FXML
    private StackPane stackPaneError;
    
    @FXML
    private TextField userName;
    
    @FXML
    private TextField password;
    
    
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private Label labelError;
                      
    
    
    public void configurarInicio() throws TpvException{
            checkMac();
            this.tabController.repeatFocus(userName);
    }
    
    private void checkMac() throws TpvException{
            Checkout checkout = null;
            checkout = usuarioService.checkMac();
            //if(checkout == null){
            //    log.fatal("La MAC de la PC no coincide con el registro del Checkout");
            //    throw new TpvException("Error en la capa de servicios al recuperar checkout a través de la MAC.");                
            //}else
                Context.getInstance().currentDMTicket().setCheckout(checkout);
                
        
    }    
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        log.info("Ingresando al mètodo init");
        //loadImage();
        
        Platform.runLater(() -> {
            userName.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    password.requestFocus();
                    
                }
                if(keyEvent.getCode() == KeyCode.F12){
                    try{
                        usuarioService.logout();
                    }catch(TpvException e){
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_MENUPRINCIPAL);
                        Context.getInstance().currentDMTicket().setException(e);
                        tabController.gotoError();
                    }
                    System.exit(0);
                }
                
            });
            
            labelError.setOnKeyPressed(keyEventLabelError -> {
                if(keyEventLabelError.getCode() == KeyCode.ESCAPE){
                    stackPaneError.setVisible(false);
                    userName.requestFocus();
                }
            });
            
            password.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    Usuario usuario = null;
                    AperturaCierreCajeroDetalle aperturaCierreCajDet;
                    try{
                        usuario = usuarioService.authenticar(userName.getText(), password.getText());
                    }catch(TpvException e){
                        log.error("Error: "+e.getMessage());
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_LOGIN);
                        Context.getInstance().currentDMTicket().setException(e);
                        tabController.gotoError();
                    }                        
                    if(usuario!=null){
                        Context.getInstance().currentDMTicket().setUsuario(usuario);
                        if(usuario.isSupervisor()){
                            tabController.getUsuarioLogueadoLabel().setText("Usuario: "+usuario.getNombreCompleto());
                            tabController.gotoMenuPrincipal();
                        }else{
                        
                            try{

                                aperturaCierreCajDet = usuarioService.verificarAperturaCaja(usuario.getIdUsuario()
                                        , Context.getInstance().currentDMTicket().getCheckout().getId());

                                if(aperturaCierreCajDet!=null){
                                    Context.getInstance().currentDMTicket().setCaja(aperturaCierreCajDet.getCaja());
                                    Context.getInstance().currentDMTicket().setAperturaCierreCajDetalle(aperturaCierreCajDet);
                                    tabController.getUsuarioLogueadoLabel().setText("Usuario: "+usuario.getNombreCompleto());
                                    tabController.gotoMenuPrincipal();
                                }else{
                                    //this.tabController.getLabelCancelarModal().setVisible(false);
                                    //this.tabController.getLabelMensaje().setText("La caja no está abierta. Consulte a su administrador");
                                    //this.tabController.mostrarMensajeModal();
                                    tabController.showMsgModal(new MensajeModalAceptar     
                                        ("Error", "La caja no está abierta. Consulte con su administrador"
                                                ,"", userName));
                                }
                            }catch(TpvException e){
                                log.error("Error: "+e.getMessage());
                                Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_LOGIN);
                                Context.getInstance().currentDMTicket().setException(e);
                                tabController.gotoError();
                            }    
                        }
                    }else{
                        /*this.tabController.getLabelMensaje().setText("Nombre de Usuario o Contraseña incorrectos");
                        this.tabController.getLabelCancelarModal().setVisible(false);
                        this.tabController.mostrarMensajeModal();*/
                        tabController.showMsgModal(new MensajeModalAceptar("Error"
                                , "Nombre de Usuario o Contraseña incorrectos", "", userName));
                    }
                    
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    userName.requestFocus();
                }
                if(keyEvent.getCode() == KeyCode.F11){
                    System.exit(0);
                }
                if(keyEvent.getCode() == KeyCode.BACK_SPACE
                        || keyEvent.getCode() == KeyCode.DELETE)
                    return;
                keyEvent.consume();
            });
            
                    
        });
        
    }
    
    private void loadImage(){
        String f = this.getClass().getResource("/com/tpv/resources/logologin.jpg").toExternalForm();
    }
    
    public void setTabController(TabPanePrincipalController tabPane){
        this.tabController=tabPane;
    }

    public TextField getPassword(){
        return password;
    }
    
    /*private void repeatFocus(Node node){
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
                repeatFocus(node);
            }
        });        
    }*/

/*    public void aceptarMensajeModal(){
        this.tabController.ocultarMensajeModal();
        this.tabController.getLabelCancelarModal().setVisible(true);
        this.tabController.repeatFocus(userName);
    }
    
    public void cancelarMensajeModal(){
        
    }
*/
}
