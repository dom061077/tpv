/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.supervisor;

import com.tpv.principal.DataModelTicket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;

/**
 *
 * @author daniel
 */

        

@FXMLController(value="Supervisor.fxml", title = "Habilitacion de Supervisor")
public class SupervisorController {
    
    @FXML
    private Label labelTitulo;
    
    @FXML
    private TextField textFieldCodigoSupervisor;
    
    @FXML
    private TextField textFieldPassword;
    
    @FXML
    @ActionTrigger("volverFacturacion")
    private Button volverButton;
    
    
    @Inject
    private DataModelTicket modelTicket;

    
    @PostConstruct
    public void init(){
        labelTitulo.setText(modelTicket.getTipoTituloSupervisor().getTitulo());
        textFieldPassword.setDisable(true);
        Platform.runLater(() -> {
            textFieldCodigoSupervisor.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    volverButton.fire();
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
                    habilitarNegativos(true);
                    keyEvent.consume();
                    volverButton.fire();                    
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldPassword.setDisable(true);
                    textFieldCodigoSupervisor.requestFocus();
                    keyEvent.consume();
                    volverButton.fire();                    
                }
            });
            
            
        });
    }
    
    private void habilitarNegativos(boolean habilita){
        modelTicket.setImprimeComoNegativo(habilita);
    }
    
}
