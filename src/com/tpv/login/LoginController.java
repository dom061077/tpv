/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.login;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.errorui.ErrorController;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Usuario;
import com.tpv.principal.Context;
import com.tpv.principal.MenuPrincipalController;
import com.tpv.service.UsuarioService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;
import org.datafx.controller.flow.action.LinkAction;

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
    @LinkAction(MenuPrincipalController.class)
    private Button buttonLogin;
    
    @FXML
    @LinkAction(ErrorController.class)
    private Button goToErrorButton;
    
    @FXML
    private StackPane stackPaneError;
    
    @FXML
    private TextField userName;
    
    @FXML
    private TextField password;
    
    @FXML
    private ImageView imageViewLogoRight;
    
    @FXML
    private ImageView imageViewLogoLeft;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private Label labelError;
                      
    
    
    public void configurarInicio(){
        userName.requestFocus();            
        repeatFocus(userName);
    }
    
    
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        //loadImage();
        Platform.runLater(() -> {
            userName.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    password.requestFocus();
                    
                }
                if(keyEvent.getCode() == KeyCode.F11){
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
                    try{
                        usuario = usuarioService.authenticar(userName.getText(), password.getText());
                    }catch(TpvException e){
                        log.error("Error: "+e.getMessage());
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_LOGIN);
                        Context.getInstance().currentDMTicket().setException(e);
                        goToErrorButton.fire();
                    }                        
                    if(usuario!=null){
                        Context.getInstance().currentDMTicket().setUsuario(usuario);
                        //buttonLogin.fire();
                       tabController.gotoMenuPrincipal();
                    }else{
                        labelError.setText("Usuario o contraseña incorrectos");
                        
                        stackPaneError.setVisible(true);
                        labelError.requestFocus();
                        
                    }
                    
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    userName.requestFocus();
                }
                if(keyEvent.getCode() == KeyCode.F11){
                    System.exit(0);
                }
                keyEvent.consume();
            });
            
                    
        });
        
    }
    
    private void loadImage(){
        String f = this.getClass().getResource("/com/tpv/resources/logologin.jpg").toExternalForm();
        imageViewLogoRight.setImage(new Image(f));
        imageViewLogoLeft.setImage(new Image(f));
    }
    
    public void setTabController(TabPanePrincipalController tabPane){
        this.tabController=tabPane;
    }

    public TextField getPassword(){
        return password;
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
