/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.errorui;

import com.tpv.principal.DataModelTicket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;

/**
 *
 * @author daniel
 */

@FXMLController(value="Error.fxml", title = "Error de Sistema")
public class ErrorController implements Initializable {
    Logger log = Logger.getLogger(ErrorController.class);
//    @FXML
//    @ActionTrigger("salir")
//    private Button salirButton;
//    
//    @FXML
//    @ActionTrigger("reintentar")
//    private Button reintentarButton;
    
    
    @FXMLViewFlowContext
    private ViewFlowContext context;    
    
    
    @FXML
    @ActionTrigger("facturacion")
    private Button volverButton;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private Label labelError;
    //@FXML
    //private TextArea errorMsg;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    @PostConstruct
    public void init(){
            DataModelTicket model = context.getRegisteredObject(DataModelTicket.class);
            log.info("Ingresando a pantalla de error: "+model.getTpvException().getMessage());
            labelError.setText(model.getTpvException().getMessage());
            Platform.runLater(()->{
                borderPane.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        volverButton.fire();
                    }
                });
            });
                    
    }
}
