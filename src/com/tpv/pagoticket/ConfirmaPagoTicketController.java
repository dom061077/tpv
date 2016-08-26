/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalleCombo;
import com.tpv.modelo.FacturaFormaPagoDetalle;
import com.tpv.principal.DataModelTicket;
import com.tpv.print.event.FiscalPrinterEvent;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.PagoService;
import com.tpv.service.ProductoService;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
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
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.tpv.print.fiscal.FiscalPacket;
import org.tpv.print.fiscal.FiscalPrinter;
import org.tpv.print.fiscal.hasar.HasarCommands;
import org.tpv.print.fiscal.msg.FiscalMessages;

/**
 *
 * @author daniel
 */

//@FXMLController(value="ConfirmarPagoTicket.fxml", title = "Confirmar Ticket")
public class ConfirmaPagoTicketController {
    Logger log = Logger.getLogger(ConfirmaPagoTicketController.class);
    
    private FacturacionService factService = new FacturacionService();
    private ImpresoraService impresoraService = new ImpresoraService();
    private ProductoService productoService = new ProductoService();
    private PagoService pagoService = new PagoService();
    private FiscalPrinterEvent fiscalPrinterEvent;

    
    
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
    private Button goToErrorButton;
    
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
    private TableColumn nroTarjetaColumn;
    
    @FXML
    private TableColumn interesTarjetaColumn;
    
    @FXML
    private TableColumn bonificacionTarjetaColumn;
    
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
    
    @FXML
    private Label totalBonificacionesLabel;

    @Inject
    private DataModelTicket modelTicket;
    
    
    @PostConstruct
    public void init(){
            log.info("Ingresando a la confirmación de pago");
            asignarEvento();
            //labelError.setText(model.getTpvException().getMessage());
            modelTicket = context.getRegisteredObject(DataModelTicket.class);
            codigoPagoColumn.setCellValueFactory(new PropertyValueFactory<LineaPagoData,Integer>("codigoPago"));
            cantidadCuotaColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
            cantidadCuotaColumn.setCellValueFactory(new PropertyValueFactory<LineaPagoData,Integer>("cantidadCuotas"));
            codigoPagoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
            descripcionPagoColumn.setCellValueFactory(new PropertyValueFactory("descripcion"));
            nroTarjetaColumn.setCellValueFactory(new PropertyValueFactory("nroTarjeta"));
            codigoCuponColumn.setCellValueFactory(new PropertyValueFactory("codigoCupon"));
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
                                DecimalFormat df = new DecimalFormat("##,##0.00");

                                this.setText(df.format(item));
                        }
                    }
                };
                return cell;
            });
            montoPagoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
            interesTarjetaColumn.setCellValueFactory(new PropertyValueFactory("interes"));
            interesTarjetaColumn.setCellFactory(col -> {
                TableCell<LineaPagoData,BigDecimal> cell = new TableCell<LineaPagoData,BigDecimal>(){
                    @Override
                    public void updateItem(BigDecimal item,boolean empty){
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        log.debug("Interes tarjeta en updateItem: "+item);
                        if (!empty) {
                                //String formattedDob = De
                                DecimalFormat df = new DecimalFormat("##,##0.00");

                                this.setText(df.format(item));
                        }
                    }
                };
                return cell;
            });
            interesTarjetaColumn.setStyle("-fx-alignment: CENTER-RIGHT");
            bonificacionTarjetaColumn.setCellValueFactory(new PropertyValueFactory("bonificacion"));
            bonificacionTarjetaColumn.setCellFactory(col -> {
                TableCell<LineaPagoData,BigDecimal> cell = new TableCell<LineaPagoData,BigDecimal>(){
                    @Override
                    public void updateItem(BigDecimal item,boolean empty){
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        log.debug("Bonifiacion en updateItem: "+item);
                        if (!empty) {
                                //String formattedDob = De
                                DecimalFormat df = new DecimalFormat("##,##0.00");

                                this.setText(df.format(item));
                        }
                    }
                };
                return cell;
            });
            bonificacionTarjetaColumn.setStyle("-fx-alignment: CENTER-RIGHT");
            
            DecimalFormat df = new DecimalFormat("##,##0.00");
            
            totalPagosLabel.setText(df.format(modelTicket.getTotalPagos()));
            totalBonificacionesLabel.setText(df.format(modelTicket.getBonificaciones()));
            totalTicketLabel.setText(df.format(modelTicket.getTotalTicket()));
            cambioLabel.setText(df.format(modelTicket.getSaldo().abs()));
            
            Platform.runLater(()->{
                tableViewPagos.setItems(modelTicket.getPagos());
                borderPane.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        volverButton.fire();
                        
                    }
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        confirmarFactura();

                    }
                    keyEvent.consume();
                });
            });
            
            
                    
    }    
    
    public void confirmarFactura(){
        try{
            log.info("Cerrando y confirmando factura ");
            Factura factura = factService.calcularCombos(modelTicket.getIdFactura());
            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
                FacturaDetalleCombo fdc = it.next();
                //TODO en las bonificaciones de los combos
                //impresoraService.imprimirLineaTicket(fdc.getCombo().getDescripcion(), fdc.getCantidad()
                //            ,fdc.getBonificacion() ,producto.getValorImpositivo().getValor() ,modelTicket.isImprimeComoNegativo(), producto.getImpuestoInterno());
                
            }
            
            impresoraService.cerrarTicket(factura);
            

        }catch(TpvException e){
            log.error("Error en controlador llamando al método cerrarTicket de ImpresoraService",e);
            modelTicket.setException(e);
            modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
            goToErrorButton.fire();
        }catch(NullPointerException e){
            e.printStackTrace();
                    
        }
    }
    
    
    private void asignarEvento(){
        this.fiscalPrinterEvent = new FiscalPrinterEvent(){
            @Override
            public void commandExecuted(FiscalPrinter source, FiscalPacket command, FiscalPacket response){
                log.debug("Se ejecutó correctamente el siguiente comando:");
                if(command.getCommandCode()==HasarCommands.CMD_CLOSE_FISCAL_RECEIPT){
                    try{
                            log.info("El cierre del ticket para el id de Factura : "+modelTicket.getIdFactura()
                                +" en la impresora fiscal fue correcto. A continuación se cierra el ticket en la base de datos");
                            List<FacturaFormaPagoDetalle> pagos = new ArrayList<FacturaFormaPagoDetalle>();
                            ListProperty<LineaPagoData> detallePagosData = modelTicket.getPagos();
                            Factura factura = factService.calcularCombos(modelTicket.getIdFactura());
                            log.info("Cantidad de combos a guardar en la base de datos: "+factura.getDetalleCombosAux().size());
                            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
                                FacturaDetalleCombo fdc = it.next();
                                factura.getDetalleCombos().add(fdc);
                                fdc.setFactura(factura);
                                log.info("          Combo: "+fdc.getCombo().getDescripcion());
                            }
                            
                            //Factura factura = factService.getFactura(modelTicket.getIdFactura());
                            log.info("Cantidad de formas de pago: "+detallePagosData.size());
                            for(Iterator<LineaPagoData> it = detallePagosData.iterator();it.hasNext();){
                                LineaPagoData item = it.next();
                                FacturaFormaPagoDetalle formaPagoDetalle = new FacturaFormaPagoDetalle();
                                try{
                                    formaPagoDetalle.setFormaPago(pagoService.getFormaPago(item.getCodigoPago()));
                                }catch(TpvException e){
                                        modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
                                        modelTicket.setException(e);
                                        goToErrorButton.fire();
                                }

                                formaPagoDetalle.setFactura(factura);
                                
                                formaPagoDetalle.setMontoPago(item.getMonto());
                                formaPagoDetalle.setCuota(item.getCantidadCuotas());
                                formaPagoDetalle.setInteres(item.getInteres());
                                formaPagoDetalle.setBonificacion(item.getBonificacion());
//                                factura.getDetallePagos().add(formaPagoDetalle);
                                factura.addFormaPago(formaPagoDetalle);
                                log.info("                  Código forma: "+item.getCodigoPago());
                            }
                            
                            factService.confirmarFactura(factura);
                            modelTicket.setCliente(null);
                            modelTicket.setClienteSeleccionado(false);
                            modelTicket.setNroTicket(modelTicket.getNroTicket()+1);
                            modelTicket.getDetalle().clear();
                            modelTicket.getPagos().clear();
                            modelTicket.setImprimeComoNegativo(false);
                            log.info("Factura cerrada y confirmada");
                            confirmarButton.fire();
                    }catch(TpvException e){
                        modelTicket.setException(e);
                        modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
                        goToErrorButton.fire();
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }  
                    
                    
                }
                log.debug("Mensajes de error: ");
                source.getMessages().getErrorMsgs().forEach(item->{
                    log.debug("     Código de Error: "+item.getCode());
                    log.debug("     Titulo: "+item.getTitle());
                    log.debug("     Descripción: "+item.getDescription());
                });
                log.debug("Mensajes: ");
                source.getMessages().getMsgs().forEach(item->{
                    log.debug("     Código de Msg: "+item.getCode());
                    log.debug("     Titulo: "+item.getTitle());
                    log.debug("     Descripción: "+item.getDescription());
                    
                });
                
            }
            
            @Override
            public void statusChanged(FiscalPrinter source, FiscalPacket command, FiscalPacket response, FiscalMessages msgs) {
                log.debug("Ingresando al evento statusChanged");
                log.debug("Mensajes de error: ");
                source.getMessages().getErrorMsgs().forEach(item->{
                    log.debug("     Código de Error: "+item.getCode());
                    log.debug("     Titulo: "+item.getTitle());
                    log.debug("     Descripción: "+item.getDescription());
                });
                log.debug("Mensajes: ");
                source.getMessages().getMsgs().forEach(item->{
                    log.debug("     Código de Msg: "+item.getCode());
                    log.debug("     Titulo: "+item.getTitle());
                    log.debug("     Descripción: "+item.getDescription());
                    
                });
                                
            }              
        };
        impresoraService.getHfp().setEventListener(this.fiscalPrinterEvent);
                
    }
    
    /*
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
            factura.setUsuario(modelTicket.getUsuario());
            //factService.registrarFactura(factura);
            modelTicket.setCliente(null);
            modelTicket.setClienteSeleccionado(false);
            modelTicket.setNroTicket(modelTicket.getNroTicket()+1);
            modelTicket.getDetalle().clear();
            modelTicket.getPagos().clear();
            modelTicket.setImprimeComoNegativo(false);
           

        }catch(TpvException e){
            log.error("Error: "+e.getMessage());
            modelTicket.setException(e);
            mostrarErrorButton.fire();
            
        }
        
    }    */
    
    
    
}
