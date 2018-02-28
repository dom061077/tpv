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
import org.datafx.controller.FXMLController;

/**
 *
 * @author daniel
 */

//@FXMLController(value="TabPanePrincipal.fxml", title = "Ingreso al Sistema")
public class TabPanePrincipalController implements Initializable {
    
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
        this.loginController.password.requestFocus();
        this.menuController.setTabController(this);
        this.facturacionController.setTabController(this);
        this.tabPane.setOnKeyPressed(keyEvent->{
            System.out.println("Tecla pulsada en tabpane");
        });
    }      
    
    @FXML
    private void handleButtonMenuPrincipal(ActionEvent event){
        System.out.println("IR AL MENU PRINCIPAL");
    }   
}
