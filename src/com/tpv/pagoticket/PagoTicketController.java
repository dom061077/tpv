/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.principal.DataModelTicket;
import com.tpv.principal.LineaTicketData;
import com.tpv.service.FacturacionService;
import com.tpv.util.ui.MaskTextField;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
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
    private MaskTextField textFieldMonto;
    private MaskTextField textFieldCantidadCuotas;
    private FacturacionService factService = new FacturacionService();
    
    @FXMLViewFlowContext
    private ViewFlowContext context;    
    
    
    @FXML
    private GridPane gridPanePagos;
    
    @FXML
    private Label labelCantidadCuotas;
    
    @FXML
    @ActionTrigger("volverFacturacion")
    private Button volverButton;
    
    @FXML
    @ActionTrigger("mostrarError")
    private Button gotoError;
    
    
    
    @PostConstruct
    public void init(){
        iniciarIngresosVisibles();
        DataModelTicket model = context.getRegisteredObject(DataModelTicket.class);
        textFieldMonto.setText(model.getTotalTicket().toString());
        //log.debug("TICKETS: "+model.getTickets().size());
        Platform.runLater(() -> {
            textFieldTipoPago.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    textFieldMonto.requestFocus();
                    textFieldMonto.setDisable(false);
                    keyEvent.consume();
                    return;
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    volverButton.fire();
                    keyEvent.consume();
                    return;
                }
                    
            });
            textFieldMonto.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    guardarTicket();
                    keyEvent.consume();
                    return;
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldMonto.setDisable(true);
                    textFieldTipoPago.requestFocus();
                    keyEvent.consume();
                    return;
                }
            });
                
        });            
    }
    
    private void iniciarIngresosVisibles(){
        textFieldTipoPago = new MaskTextField();
        textFieldTipoPago.setMask("N!.N!");
        textFieldMonto = new MaskTextField();
        textFieldMonto.setMask("N!.N!");
        textFieldCantidadCuotas = new MaskTextField();
        textFieldCantidadCuotas.setMask("N!.N!");
        
        gridPanePagos.add(textFieldTipoPago,2,1);
        gridPanePagos.add(textFieldMonto,2,2);
        gridPanePagos.add(textFieldCantidadCuotas,2,3);
        
        labelCantidadCuotas.setVisible(false);
        textFieldCantidadCuotas.setVisible(false);
    }
    
    private void agregarLineaPago(){
        int codigoPago = 0;
    }
    
    public void guardarTicket(){
        DataModelTicket modelTicket = context.getRegisteredObject(DataModelTicket.class);
        Factura factura = new Factura();
        factura.setTotal(modelTicket.getTotalTicket());
        //factura.setNumeroComprobante(LABEL_CANTIDAD);
        ListProperty<LineaTicketData> detalle =  modelTicket.getDetalle();
        
        detalle.forEach(item->{
            FacturaDetalle facturaDetalle = new FacturaDetalle();
            facturaDetalle.setCantidad(item.getCantidad());
            facturaDetalle.setSubTotal(item.getSubTotal());
            factura.getDetalle().add(facturaDetalle);
        });
        try{
            factService.registrarFactura(factura);
        }catch(TpvException e){
            log.error("Error: "+e.getMessage());
            modelTicket.setException(e);
            
        }
        
    }
    
            
    
}
