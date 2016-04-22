/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.login;

import com.tpv.modelo.Usuario;
import com.tpv.principal.DataModelTicket;
import com.tpv.service.UsuarioService;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;

/**
 *
 * @author daniel
 */

@FXMLController(value="Login.fxml", title = "Ingreso al Sistema")
public class LoginController {
    
    UsuarioService usuarioService = new UsuarioService();    
    @FXML
    @ActionTrigger("iniciarSesion")
    private Button buttonLogin;
    
    @FXML
    @ActionTrigger("goToError")
    private Button buttonError;
    
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
    
                      
    
    @Inject
    private DataModelTicket modelTicket;
    
    
    
    
    @PostConstruct
    public void init(){
        loadImage();

        Platform.runLater(() -> {
            userName.requestFocus();            
            userName.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    password.requestFocus();
                    
                }
                if(keyEvent.getCode() == KeyCode.F11){
                    System.exit(0);
                }
                
            });
            
            password.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    Usuario usuario = usuarioService.authenticar(userName.getText(), password.getText());
                    if(usuario!=null){
                      modelTicket.setUsuario(usuario);
                      buttonLogin.fire();
                    }else
                       ; 
                    
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    userName.requestFocus();
                }
                if(keyEvent.getCode() == KeyCode.F11){
                    System.exit(0);
                }
                
            });
            
                    
        });
        
    }
    
    private void loadImage(){
        String f = this.getClass().getResource("/com/tpv/resources/logologin.jpg").toExternalForm();
        imageViewLogoRight.setImage(new Image(f));
        imageViewLogoLeft.setImage(new Image(f));
    }
            
    
}
