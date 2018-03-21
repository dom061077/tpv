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
import com.tpv.principal.Context;
import com.tpv.print.event.FiscalPrinterEvent;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.PagoService;
import com.tpv.service.ProductoService;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;
import org.tpv.print.fiscal.FiscalPacket;
import org.tpv.print.fiscal.FiscalPrinter;
import org.tpv.print.fiscal.hasar.HasarCommands;
import org.tpv.print.fiscal.msg.FiscalMessages;

/**
 *
 * @author daniel
 */

//@FXMLController(value="ConfirmarPagoTicket.fxml", title = "Confirmar Ticket")
public class ConfirmaPagoTicketController implements Initializable{
    Logger log = Logger.getLogger(ConfirmaPagoTicketController.class);
    
    private FacturacionService factService = new FacturacionService();
    private ImpresoraService impresoraService = new ImpresoraService();
    private ProductoService productoService = new ProductoService();
    private PagoService pagoService = new PagoService();
    private FiscalPrinterEvent fiscalPrinterEvent;

    
    
    
    
    
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

    @FXML
    private TabPanePrincipalController tabPaneController;
    
    public void configurarInicio(){
            log.info("Ingresando a la confirmación de pago");
            DecimalFormat df = new DecimalFormat("##,##0.00");
            
            totalPagosLabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalPagos()));
            totalBonificacionesLabel.setText(df.format(Context.getInstance().currentDMTicket().getBonificaciones()));
            totalTicketLabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalTicket()));
            cambioLabel.setText(df.format(Context.getInstance().currentDMTicket().getSaldo().abs()));
            repeatFocus(borderPane);
                    
    }
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
            log.info("Ingresando al mètodo init");
            asignarEvento();
            //labelError.setText(model.getTpvException().getMessage());
            //modelTicket = context.getRegisteredObject(DataModelTicket.class);
            
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
            
            
            
            Platform.runLater(()->{
                tableViewPagos.setItems(Context.getInstance().currentDMTicket().getPagos());
                borderPane.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        
                        tabPaneController.gotoPago();
                        
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
            Factura factura = factService.calcularCombos(Context.getInstance().currentDMTicket().getIdFactura());
            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
                FacturaDetalleCombo fdc = it.next();
                //TODO en las bonificaciones de los combos
                //impresoraService.imprimirLineaTicket(fdc.getCombo().getDescripcion(), fdc.getCantidad()
                //            ,fdc.getBonificacion() ,producto.getValorImpositivo().getValor() ,Context.getInstance().currentDMTicket().isImprimeComoNegativo(), producto.getImpuestoInterno());
                
            }
            
            impresoraService.cerrarTicket(factura);
            

        }catch(TpvException e){
            log.error("Error en controlador llamando al método cerrarTicket de ImpresoraService",e);
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
            tabPaneController.gotoError();
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
                            log.info("El cierre del ticket para el id de Factura : "+Context.getInstance().currentDMTicket().getIdFactura()
                                +" en la impresora fiscal fue correcto. A continuación se cierra el ticket en la base de datos");
                            List<FacturaFormaPagoDetalle> pagos = new ArrayList<FacturaFormaPagoDetalle>();
                            ListProperty<LineaPagoData> detallePagosData = Context.getInstance().currentDMTicket().getPagos();
                            Factura factura = factService.calcularCombos(Context.getInstance().currentDMTicket().getIdFactura());
                            log.info("Cantidad de combos a guardar en la base de datos: "+factura.getDetalleCombosAux().size());
                            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
                                FacturaDetalleCombo fdc = it.next();
                                factura.getDetalleCombos().add(fdc);
                                fdc.setFactura(factura);
                                log.info("          Combo: "+fdc.getCombo().getDescripcion());
                            }
                            
                            //Factura factura = factService.getFactura(Context.getInstance().currentDMTicket().getIdFactura());
                            log.info("Cantidad de formas de pago: "+detallePagosData.size());
                            for(Iterator<LineaPagoData> it = detallePagosData.iterator();it.hasNext();){
                                LineaPagoData item = it.next();
                                FacturaFormaPagoDetalle formaPagoDetalle = new FacturaFormaPagoDetalle();
                                try{
                                    formaPagoDetalle.setFormaPago(pagoService.getFormaPago(item.getCodigoPago()));
                                }catch(TpvException e){
                                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
                                        Context.getInstance().currentDMTicket().setException(e);
                                        tabPaneController.gotoError();
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
                            Context.getInstance().currentDMTicket().setCliente(null);
                            Context.getInstance().currentDMTicket().setClienteSeleccionado(false);
                            Context.getInstance().currentDMTicket().setNroTicket(Context.getInstance().currentDMTicket().getNroTicket()+1);
                            Context.getInstance().currentDMTicket().getDetalle().clear();
                            Context.getInstance().currentDMTicket().getPagos().clear();
                            Context.getInstance().currentDMTicket().setImprimeComoNegativo(false);
                            log.info("Factura cerrada y confirmada");
                            tabPaneController.gotoFacturacion();
                            //confirmarButton.fire();
                    }catch(TpvException e){
                        Context.getInstance().currentDMTicket().setException(e);
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
                        tabPaneController.gotoError();
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
    
    public void setTabController(TabPanePrincipalController tabPaneController){
        this.tabPaneController=tabPaneController;
    }
    
    
    /*
    public void guardarTicket(){
        DataModelTicket modelTicket = context.getRegisteredObject(DataModelTicket.class);
        Factura factura = new Factura();
        factura.setTotal(Context.getInstance().currentDMTicket().getTotalTicket());
        factura.setCliente(Context.getInstance().currentDMTicket().getCliente());
        //factura.setNumeroComprobante(LABEL_CANTIDAD);
        ListProperty<LineaTicketData> detalle =  Context.getInstance().currentDMTicket().getDetalle();
        
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
            factura.setUsuario(Context.getInstance().currentDMTicket().getUsuario());
            //factService.registrarFactura(factura);
            Context.getInstance().currentDMTicket().setCliente(null);
            Context.getInstance().currentDMTicket().setClienteSeleccionado(false);
            Context.getInstance().currentDMTicket().setNroTicket(Context.getInstance().currentDMTicket().getNroTicket()+1);
            Context.getInstance().currentDMTicket().getDetalle().clear();
            Context.getInstance().currentDMTicket().getPagos().clear();
            Context.getInstance().currentDMTicket().setImprimeComoNegativo(false);
           

        }catch(TpvException e){
            log.error("Error: "+e.getMessage());
            Context.getInstance().currentDMTicket().setException(e);
            mostrarErrorButton.fire();
            
        }
        
    }    */
    
    private void repeatFocus(Node node){
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
                repeatFocus(node);
            }
        });        
    }    
    
}
