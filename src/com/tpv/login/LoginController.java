/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.login;

import com.tpv.service.UsuarioService;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.annotation.PostConstruct;
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
    
    @PostConstruct
    public void init(){
        /*Platform.runLater(() -> {
                buttonLogin.getScene().getAccelerators().put(
                        new KeyCodeCombination(KeyCode.NUMPAD1, KeyCombination.CONTROL_DOWN), 
                        new Runnable() {
                          @Override public void run() {
                            buttonLogin.fire();
                          }
                        }
                      );
            
        });*/
        //Direction.NEXT;
        
        Platform.runLater(() -> {
            buttonLogin.getScene().setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(buttonLogin.getScene()!=null){
                        
                        
                        if(keyEvent.getCode() == KeyCode.ENTER && !keyEvent.isControlDown()){
                            if(userName.isFocused()){
                                 password.requestFocus();
                                 keyEvent.consume();
                                 return;
                            }else
                                 if(password.isFocused()){
                                     if(usuarioService.authenticar(userName.getText(), password.getText()))
                                       buttonLogin.fire();
                                     else
                                        ; 

                                 }


                        }
                        
                    }
                }
            });
            
            
            buttonLogin.getScene().addEventFilter(KeyEvent.KEY_PRESSED, 
                    new EventHandler<KeyEvent>(){
                           public void handle(KeyEvent event) {
                               UsuarioService usuarioService = new UsuarioService();
                               if(event.getCode() == KeyCode.ENTER && !event.isControlDown()){
                                   if(userName.isFocused()){
                                        password.requestFocus();
                                        event.consume();
                                        return;
                                   }else
                                        if(password.isFocused()){
                                            if(usuarioService.authenticar(userName.getText(), password.getText()))
                                              buttonLogin.fire();
                                            else
                                               ; 
                                            
                                        }
                                   
                                                    
                               }
                           }
                    }
            );

            
        });
        
    }
    
}
