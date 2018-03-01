/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx8tpv1;

import com.tpv.login.LoginController;
import com.tpv.principal.FXMLMainController;
import com.tpv.principal.MenuPrincipalController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;

/**
 *
 * @author daniel
 */

//@FXMLController(value="TabPanePrincipal.fxml", title = "Ingreso al Sistema")
public class TabPanePrincipalController implements Initializable {
    Logger log = Logger.getLogger(TabPanePrincipalController.class);
    @FXML private LoginController loginController;
    @FXML private MenuPrincipalController menuController;
    @FXML private FXMLMainController facturacionController;
    
    @FXML private Button buttonMenuPrincipal;
    
    @FXML private TabPane tabPane;
    
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
        this.menuController.setTabController(this);
        this.facturacionController.setTabController(this);
        buttonMenuPrincipal.setOnAction((ActionEvent event)->{
            log.debug("Botón de ingreso de menu pulsado");
        });
    }      
    
    @FXML
    private void handleButtonMenuPrincipal(ActionEvent event){
        System.out.println("IR AL MENU PRINCIPAL");
    }   
}
