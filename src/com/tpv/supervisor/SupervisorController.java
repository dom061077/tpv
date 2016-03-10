/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.supervisor;

import com.tpv.principal.DataModelTicket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.datafx.controller.FXMLController;

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
    
    
    @Inject
    private DataModelTicket modelTicket;

    
    @PostConstruct
    public void init(){
        labelTitulo.setText(modelTicket.getTipoTituloSupervisor().getTitulo());
        Platform.runLater(() -> {
            textFieldCodigoSupervisor.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    
                }
            });
            
        });
    }
    
}
