/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javax.annotation.PostConstruct;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;

/**
 *
 * @author daniel
 */
@FXMLController(value="PagoTicket.fxml", title = "pago ticket")
public class PagoTicketController {
    
    @FXML
    @ActionTrigger("volverpantallaprincipal")
    private Button buttonAceptar;    
    @PostConstruct
    public void init(){
        Platform.runLater(() -> {
            //cargarTableView();
                //----------------- eventos de navegacion, para aceptar la seleccion del producto
                // or para cancelar la seleccion
                buttonAceptar.getScene().setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        buttonAceptar.fire();
                    }
                });
        });            
    }
    
}
