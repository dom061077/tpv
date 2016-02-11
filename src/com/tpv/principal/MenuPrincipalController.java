/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;

/**
 *
 * @author daniel
 */
@FXMLController(value="MenuPrincipal.fxml", title = "Menu Principal")
public class MenuPrincipalController {
    Logger log = Logger.getLogger(MenuPrincipalController.class);
    @FXML
    @ActionTrigger("facturacion")
    private Button buttonFacturacion;
         
    @FXML
    @ActionTrigger("controlador")
    private Button buttonControlador;
    
    @FXML
    @ActionTrigger("retirarDinero")
    private Button buttonRetirarDinero;
    
    @FXML
    private VBox vboxMenuPrincipal;
        
    @PostConstruct
    public void init(){
        log.info("Ingresando al menú principal");
        Platform.runLater(()->{
            vboxMenuPrincipal.setOnKeyPressed(keyEvent->{
                log.debug("Tecla pulsada: "+keyEvent.getCode());
                if(keyEvent.getCode()==KeyCode.NUMPAD1)
                    buttonFacturacion.fire();
                keyEvent.consume();
            });
        });
    }
}
