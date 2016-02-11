/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.tpv.principal.DataModelTicket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
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
@FXMLController(value="PagoTicket.fxml", title = "pago ticket")
public class PagoTicketController {
    Logger log = Logger.getLogger(PagoTicketController.class);
    
    @FXMLViewFlowContext
    private ViewFlowContext context;    
    
    @FXML
    @ActionTrigger("volverpantallaprincipal")
    private Button buttonAceptar;    
    @PostConstruct
    public void init(){
        
        //DataModelTicket model = context.getRegisteredObject(DataModelTicket.class);
        //log.debug("TICKETS: "+model.getTickets().size());
        
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
