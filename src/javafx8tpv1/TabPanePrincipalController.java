/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx8tpv1;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.errorui.ErrorController;
import com.tpv.login.LoginController;
import com.tpv.modelo.Checkout;
import com.tpv.principal.Context;
import com.tpv.principal.FXMLMainController;
import com.tpv.principal.MenuPrincipalController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.log4j.Logger;
import com.tpv.exceptions.TpvException;
import com.tpv.service.UsuarioService;
import com.tpv.supervisor.SupervisorController;
import com.tpv.util.Connection;

/**
 *
 * @author daniel
 */

//@FXMLController(value="TabPanePrincipal.fxml", title = "Ingreso al Sistema")
public class TabPanePrincipalController implements Initializable {
    Logger log = Logger.getLogger(TabPanePrincipalController.class);
    UsuarioService usuarioService = new UsuarioService();        
    
    @FXML private LoginController loginController;
    @FXML private MenuPrincipalController menuPrincipalController;
    @FXML private FXMLMainController facturacionController;
    @FXML private ErrorController errorController;
    @FXML private SupervisorController supervisorController;
    
    @FXML private Button buttonMenuPrincipal;
    
    @FXML private Tab tabMenuPrincipal;
    @FXML private Tab tabLogin;
    @FXML private Tab tabFacturacion;  
    @FXML private Tab tabError;
    @FXML private Tab tabSupervisor;
    @FXML private TabPane tabPanePrincipal;
    
    @FXML private Button buttonGoToError;
    
    
    
    public Button getButtonMenuPrincipal(){
        return buttonMenuPrincipal;
    }
            
    
    //@Override
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.loginController.setTabController(this);
        this.loginController.getPassword().requestFocus();
        this.loginController.setTabController(this);
        this.menuPrincipalController.setTabController(this);
        this.facturacionController.setTabController(this);
        this.errorController.setTabController(this);
        buttonMenuPrincipal.setOnAction((ActionEvent event)->{
            log.debug("Botón de ingreso de menu pulsado");
        });
        checkMac();
        tabPanePrincipal.getSelectionModel().selectedItemProperty()
                .addListener((observable,oldTab,newTab)->{
                   if(newTab.getId().compareTo("tabMenuPrincipal")==0){
                       //menuPrincipalController.setMenuFocus();
                   }
                });
        //initImpresora();
        gotoLogin();
    }      
    
    @FXML
    private void handleButtonMenuPrincipal(ActionEvent event){
        System.out.println("IR AL MENU PRINCIPAL");
    }   
    
    private void initImpresora(){
        log.info("Ingresando al menú principal");
        try{
            Connection.initFiscalPrinter();
        }catch(TpvException e){
            log.error(e.getMessage());
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_MENUPRINCIPAL);
            this.gotoError();
        }
        
    }
    
    private void checkMac(){
            Checkout checkout = null;
            try{
                checkout = usuarioService.checkMac();
            }catch(TpvException e){
                log.error("Error: "+e.getMessage(),e);
                Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_LOGIN);
                Context.getInstance().currentDMTicket().setException(e);
                buttonGoToError.fire();
            }
            if(checkout == null){
                log.fatal("La MAC de la PC no coincide con el registro del Checkout");
                /*labelError.setText("La PC no está habilitada para este Checkout");
                FadeTransition fadeInOut;
                fadeInOut = new FadeTransition(Duration.seconds(1),labelError);
		fadeInOut.setFromValue(1.0);
		fadeInOut.setToValue(.20);
		fadeInOut.setCycleCount(FadeTransition.INDEFINITE);
		fadeInOut.setAutoReverse(true);
		fadeInOut.play();
                
                
                stackPaneError.setVisible(true);
                labelError.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.F11){
                        System.exit(0);
                    }
                });*/
                
            }else
                Context.getInstance().currentDMTicket().setCheckout(checkout);
        
    }
    
    public void gotoLogin(){
        this.loginController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabLogin);
       
    }
    
    public void gotoMenuPrincipal(){
        this.tabPanePrincipal.getSelectionModel().select(tabMenuPrincipal);
        
        this.menuPrincipalController.configurarInicio();
    }
    
    public void gotoFacturacion(){
        this.facturacionController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabFacturacion);
    }
    
    public void gotoError(){
        this.errorController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabError);
    }
    
    public void gotoSupervisor(){
        this.tabPanePrincipal.getSelectionModel().select(tabSupervisor);
    }
}
