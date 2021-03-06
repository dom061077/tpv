/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.enums.TipoTituloSupervisorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.service.UsuarioService;
import com.tpv.util.ui.MensajeModalAceptar;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;


/**
 *
 * @author daniel
 */
//@FXMLController(value="MenuPrincipal.fxml", title = "Menu Principal")
public class MenuPrincipalController implements Initializable {
    private TabPanePrincipalController tabController;
    Logger log = Logger.getLogger(MenuPrincipalController.class);
    UsuarioService usuarioService = new UsuarioService();
    @FXML
    private Button buttonFacturacion;
         
    @FXML
    private Button buttonControlador;
    
    @FXML
    private Button buttonRetirarDinero;
    
    @FXML
    private Button buttonError;
    
    @FXML
    private VBox vboxMenuPrincipal;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private ImageView imageViewLogoRight1;
    @FXML
    private ImageView imageViewLogoRight2;
    @FXML
    private ImageView imageViewLogoRight3;
    
    
    
    @FXML
    private ImageView imageViewLogoLeft1;
    @FXML
    private ImageView imageViewLogoLeft2;
    @FXML
    private ImageView imageViewLogoLeft3;
    
    
    
    
    @FXML
    private ImageView imageViewLogoBottom1;
    @FXML
    private ImageView imageViewLogoBottom2;
    @FXML
    private ImageView imageViewLogoBottom3;
    
    
    @FXML
    private ImageView imageViewLogoTop1;
    
    @FXML
    private ImageView imageViewLogoTop2;

    @FXML
    private ImageView imageViewLogoTop3;
    
    @FXML
    private ImageView imageFacturacion;
    
    @FXML
    private ImageView imageControlador;
    
    @FXML
    private ImageView imageRetiroDinero;
    
    @FXML
    private ImageView imageCargaRetiroDinero;
    
    @FXML
    private ImageView imageNotasCredito;
    
    @FXML
    private ImageView imageLogin;
    
    @FXML
    private ImageView imageFin;
    
    
    public void configurarInicio(){
        repeatFocus(borderPane);
    }    
        
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        log.info("Ingresando al mètodo init");
        loadImage();
        
        repeatFocus(borderPane);
        Platform.runLater(()->{
            
            borderPane.setOnKeyPressed(keyEvent->{
                log.debug("Tecla pulsada: "+keyEvent.getCode());
                if(keyEvent.getCode()==KeyCode.NUMPAD1){
                    if(Context.getInstance().currentDMTicket().getUsuario().isSupervisor()){
                        //this.tabController.getLabelCancelarModal().setVisible(false);
                        //this.tabController.getLabelMensaje().setText("Esta opción es solo para cajeros habilitados");
                        //this.tabController.mostrarMensajeModal();
                        tabController.showMsgModal(new MensajeModalAceptar("Error",
                                 "Esta opción es solo para cajeros habilitados",
                                 "",borderPane
                        ));
                    }else{
                        Context.getInstance().currentDMTicket().setCliente(null);
                        Context.getInstance().currentDMTicket().setClienteSeleccionado(false);
                        Context.getInstance().currentDMTicket().setReinicioVerificado(false);
                        Context.getInstance().currentDMTicket().getDetalle().clear();
                        Context.getInstance().currentDMTicket().setIdDocumento(null);
                        tabController.gotoFacturacion();
                    }
                }
                
                if(keyEvent.getCode()==KeyCode.NUMPAD5){
                    Context.getInstance().currentDMTicket().setCliente(null);
                    Context.getInstance().currentDMTicket().setClienteSeleccionado(false);
                    Context.getInstance().currentDMTicket().setReinicioVerificado(false);
                    Context.getInstance().currentDMTicket().setIdDocumento(null);
                    Context.getInstance().currentDMTicket().getDetalle().clear();
                    Context.getInstance().currentDMTicket()
                         .setTipoTituloSupervisor(TipoTituloSupervisorEnum.HABILITAR_MENU_NOTASDC);
                    tabController.gotoSupervisor();
                    
                    //tabController.gotoNotasDCMenu();
                }
                if (keyEvent.getCode() == KeyCode.NUMPAD6){
                    try{
                        usuarioService.logout();
                    }catch(TpvException e){
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_MENUPRINCIPAL);
                        Context.getInstance().currentDMTicket().setException(e);
                        tabController.gotoError();
                    }    
                    tabController.gotoLogin();
                }
                
                if(keyEvent.getCode()==KeyCode.NUMPAD7){
                    try{
                        usuarioService.logout();
                    }catch(TpvException e){
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_MENUPRINCIPAL);
                        Context.getInstance().currentDMTicket().setException(e);
                        tabController.gotoError();
                    }
                    
                    powerOffPc();
                    System.exit(0);
                    
                }
                if(keyEvent.getCode()==KeyCode.NUMPAD2){
                    Context.getInstance().currentDMTicket().setTipoTituloSupervisor(TipoTituloSupervisorEnum.HABILITAR_CONTROLADOR);
                    tabController.gotoSupervisor();
                }
                if(keyEvent.getCode()==KeyCode.NUMPAD3){
                    if(Context.getInstance().currentDMTicket().getUsuario().isSupervisor()){
                        //this.tabController.getLabelCancelarModal().setVisible(false);
                        //this.tabController.getLabelMensaje().setText("Esta opción es solo para cajeros habilitados");
                        //this.tabController.mostrarMensajeModal();
                        tabController.showMsgModal(new MensajeModalAceptar("Error","Esta opción es solo para cajeros habilitados"
                            ,"",borderPane));
                    }else{

                        tabController.gotoMenuRetiroDinero();
                    }
                }
                
                if(keyEvent.getCode() == KeyCode.NUMPAD4){
                    Context.getInstance().currentDMTicket().setTipoTituloSupervisor(TipoTituloSupervisorEnum.HABILITAR_CONFIRMARETIRODINERO);
                    tabController.gotoSupervisor();
                }
                
                keyEvent.consume();
            });
        });
    }
    
    private void loadImage(){
        //String f = this.getClass().getResource("/com/tpv/resources/logologin.jpg").toExternalForm();
        imageFacturacion.setImage(new Image(this.getClass().getResource("/com/tpv/resources/facturacion3.png").toExternalForm()));
        imageControlador.setImage(new Image(this.getClass().getResource("/com/tpv/resources/controlador.png").toExternalForm()));
        imageRetiroDinero.setImage(new Image(this.getClass().getResource("/com/tpv/resources/retiro_dinero.png").toExternalForm()));
        
        imageCargaRetiroDinero.setImage(new Image(this.getClass().getResource("/com/tpv/resources/carga_retirodinero.png").toExternalForm()));
        imageNotasCredito.setImage(new Image(this.getClass().getResource("/com/tpv/resources/notadecredito.png").toExternalForm()));
        imageFin.setImage(new Image(this.getClass().getResource("/com/tpv/resources/fin.png").toExternalForm()));
        imageLogin.setImage(new Image(this.getClass().getResource("/com/tpv/resources/login.png").toExternalForm()));
                
    }    
    
    public void setTabController(TabPanePrincipalController tabController){
        this.tabController=tabController;
    }
    private void repeatFocus(Node node){
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
                repeatFocus(node);
            }
        });        
    }
    
    private void powerOffPc(){
         try {
            log.info("Antes de ejecutar spooler");
            Process process = Runtime
                    .getRuntime()
                    .exec("/home/tpv1/TPV/./shutdown.sh");
            InputStream inputstream = process.getInputStream();
            log.info("Despues de ejecutar spooler");           
        } catch (IOException e) {
            log.error(e);
        }          
    }
    
    
    /*public void aceptarMensajeModal(){
        this.tabController.ocultarMensajeModal();
        this.tabController.getLabelCancelarModal().setVisible(true);
        this.tabController.repeatFocus(borderPane);
                
    }
    
    public void cancelarMensajeModal(){
        
    }*/
    
}
