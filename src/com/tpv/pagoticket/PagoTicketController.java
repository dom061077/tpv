/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.tpv.principal.DataModelTicket;
import com.tpv.util.ui.MaskTextField;
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
 *|
 * @author daniel
 */
@FXMLController(value="PagoTicket.fxml", title = "pago ticket")
public class PagoTicketController {
    Logger log = Logger.getLogger(PagoTicketController.class);
    
    private MaskTextField textFieldTipoPago;
    
    @FXMLViewFlowContext
    private ViewFlowContext context;    
    
    
    @FXML
    @ActionTrigger("volverFacturacoin")
    private Button volverButton;
    
    
    @PostConstruct
    public void init(){
        
        //DataModelTicket model = context.getRegisteredObject(DataModelTicket.class);
        //log.debug("TICKETS: "+model.getTickets().size());
        
        Platform.runLater(() -> {
                
        });            
    }
    
}
