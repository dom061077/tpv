/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.Producto;
import com.tpv.principal.DataModelTicket;
import com.tpv.principal.LineaTicketData;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.ProductoService;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;

/**
 *
 * @author daniel
 */
public class ConfirmaPagoTicketController {
    Logger log = Logger.getLogger(ConfirmaPagoTicketController.class);
    
    private FacturacionService factService = new FacturacionService();
    private ImpresoraService impresoraService = new ImpresoraService();
    private ProductoService productoService = new ProductoService();
    
    @FXMLViewFlowContext
    private ViewFlowContext context;    
    
    
    @FXML
    @ActionTrigger("volverPagoTicket")
    private Button volverButton;
    
    @FXML
    @ActionTrigger("facturacion")
    private Button confirmarButton;
    
    @FXML
    @ActionTrigger("mostrarError")
    private Button mostrarErrorButton;
    
    @FXML
    BorderPane borderPane;
    
    
    @PostConstruct
    public void init(){
            DataModelTicket model = context.getRegisteredObject(DataModelTicket.class);
            log.info("Ingresando a la confirmación de pago");
            //labelError.setText(model.getTpvException().getMessage());
            Platform.runLater(()->{
                borderPane.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        volverButton.fire();
                        
                    }
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        guardarTicket();
                        confirmarButton.fire();
                    }
                    keyEvent.consume();
                });
            });
                    
    }    
    
    public void guardarTicket(){
        DataModelTicket modelTicket = context.getRegisteredObject(DataModelTicket.class);
        Factura factura = new Factura();
        factura.setTotal(modelTicket.getTotalTicket());
        factura.setCliente(modelTicket.getCliente());
        //factura.setNumeroComprobante(LABEL_CANTIDAD);
        ListProperty<LineaTicketData> detalle =  modelTicket.getDetalle();
        
        detalle.forEach(item->{
            FacturaDetalle facturaDetalle = new FacturaDetalle();
            Producto producto = productoService.getProductoPorCodigo(item.getCodigoProducto());
            facturaDetalle.setFactura(factura);
            facturaDetalle.setProducto(producto);
            facturaDetalle.setCantidad(item.getCantidad());
            facturaDetalle.setSubTotal(item.getSubTotal());
            factura.getDetalle().add(facturaDetalle);
        });
        try{
            impresoraService.cerrarTicket();
            factService.registrarFactura(factura);
            modelTicket.setCliente(null);
            modelTicket.getDetalle().clear();
            modelTicket.getPagos().clear();;

        }catch(TpvException e){
            log.error("Error: "+e.getMessage());
            modelTicket.setException(e);
            mostrarErrorButton.fire();
            
        }
        
    }    
    
}
