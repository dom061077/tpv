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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private DataModelTicket modelTicket;
    
    
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
    
    @FXML
    TableView tableViewPagos;

    @FXML
    private TableColumn codigoPagoColumn;
    
    @FXML
    private TableColumn descripcionPagoColumn;
    
    @FXML
    private TableColumn montoPagoColumn;
    
    @FXML
    private TableColumn cantidadCuotaColumn;
    
    @FXML
    private TableColumn codigoCuponColumn;
    
    @FXML
    private TextField totalTicketTextField;
    
    @FXML
    private TextField totalPagoTextField;
    
    @FXML
    private Label cambioLabel;
    
    @FXML
    private Label totalTicketLabel;
    
    @FXML
    private Label totalPagosLabel;

    
    
    @PostConstruct
    public void init(){
            log.info("Ingresando a la confirmación de pago");
            //labelError.setText(model.getTpvException().getMessage());
            modelTicket = context.getRegisteredObject(DataModelTicket.class);
            codigoPagoColumn.setCellValueFactory(new PropertyValueFactory<LineaPagoData,Integer>("codigoPago"));
            codigoPagoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
            descripcionPagoColumn.setCellValueFactory(new PropertyValueFactory("descripcion"));
            montoPagoColumn.setCellValueFactory(new PropertyValueFactory("monto"));
            montoPagoColumn.setCellFactory(col -> {
                TableCell<LineaPagoData,BigDecimal> cell = new TableCell<LineaPagoData,BigDecimal>(){
                    @Override
                    public void updateItem(BigDecimal item,boolean empty){
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                                //String formattedDob = De
                                DecimalFormat df = new DecimalFormat("##,###.00");

                                this.setText(df.format(item));
                        }
                    }
                };
                return cell;
            });
            montoPagoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
            DecimalFormat df = new DecimalFormat("##,##0.00");
            
            totalPagosLabel.setText(df.format(modelTicket.getTotalPagos()));
            totalTicketLabel.setText(df.format(modelTicket.getTotalTicket()));
            cambioLabel.setText(df.format(modelTicket.getSaldo().abs()));
            
            Platform.runLater(()->{
                tableViewPagos.setItems(modelTicket.getPagos());
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
            factura.setNumeroComprobante(impresoraService.getNroUltimoTicketBC());
            //factura.set
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
