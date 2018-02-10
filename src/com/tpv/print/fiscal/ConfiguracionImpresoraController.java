/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.print.fiscal;

import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;

import javafx.scene.control.Button;
import javafx.fxml.FXML;
import org.datafx.controller.flow.action.ActionTrigger;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

/**
 *
 * @author daniel
 */

@FXMLController(value="ConfiguracionImpresora.fxml", title="Configuración de Impresora")
public class ConfiguracionImpresoraController {
    Logger log = Logger.getLogger(ConfiguracionImpresoraController.class);
    
    
    @ActionTrigger("volverMenuPrincipal")
    @FXML
    private Button buttonVolver;
    
    @FXML
    private VBox vboxMenuImpresora;
    
    @PostConstruct
    public void init(){
        log.debug("Init del controlador");
        Platform.runLater(()->{
            vboxMenuImpresora.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode()==KeyCode.F11){
                    log.debug("Button volver de la pantalla de configuracion");
                    buttonVolver.fire();
                }
                if(keyEvent.getCode()==KeyCode.NUMPAD1){
                    
                }
            });

        });        
    }    
    

}
