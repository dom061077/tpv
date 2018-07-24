/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.FacturaDetalleCombo;
import com.tpv.principal.Context;
import com.tpv.print.event.FiscalPrinterEvent;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.PagoService;
import com.tpv.service.ProductoService;
import com.tpv.util.ui.TabPaneModalCommand;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
public class ConfirmaPagoTicketController implements Initializable, TabPaneModalCommand{
    Logger log = Logger.getLogger(ConfirmaPagoTicketController.class);
    
    private FacturacionService factService = new FacturacionService();
    private ImpresoraService impresoraService = new ImpresoraService();
    private ProductoService productoService = new ProductoService();
    private PagoService pagoService = new PagoService();
    private FiscalPrinterEvent fiscalPrinterEvent;
    private String fechaHoraFiscal;

    
    
    
    
    
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
    private Label totalNetoLabel;
    
    @FXML
    private Label totalIVALabel;

    @FXML
    private Label totalInternoLabel;
    
    @FXML
    private Label totalExentoIVALabel;    
    
    @FXML
    private Label totalBonificacionesLabel;
    
    @FXML
    private Label ingBrutosLabel;

    @FXML
    private Label labelBaseImponible;
    
    @FXML
    private Label labelIVA;
    
    @FXML
    private Label labelImpuestoInterno;
    
    @FXML
    private Label labelExentoIVA;
    
    @FXML
    private Label labelRetIngBrutos;
    
    @FXML
    private StackPane stackPaneMensajeCancelar;
    
    
    @FXML
    private TabPanePrincipalController tabPaneController;
    
    private boolean ticketConfirmado;        
            
    public void configurarInicio(){
            log.info("Ingresando a la confirmación de pago");
            ticketConfirmado=false;
            if(impresoraService.getHfp().getEventListener()==null)
                asignarEvento();            
            
            DecimalFormat df = new DecimalFormat("##,##0.00");
            if(Context.getInstance().currentDMTicket().getCliente()==null
                    || Context.getInstance().currentDMTicket().getCliente().getCondicionIva().getId()==1){
                labelBaseImponible.setVisible(false);
                labelExentoIVA.setVisible(false);
                labelIVA.setVisible(false);
                labelImpuestoInterno.setVisible(false);
                labelRetIngBrutos.setVisible(false);
                totalNetoLabel.setVisible(false);
                totalExentoIVALabel.setVisible(false);
                totalIVALabel.setVisible(false);
                totalInternoLabel.setVisible(false);
                ingBrutosLabel.setVisible(false);
            }else{
                labelBaseImponible.setVisible(true);
                labelExentoIVA.setVisible(true);
                labelIVA.setVisible(true);
                labelImpuestoInterno.setVisible(true);
                labelRetIngBrutos.setVisible(true);
                totalNetoLabel.setVisible(true);
                totalExentoIVALabel.setVisible(true);
                totalIVALabel.setVisible(true);
                totalInternoLabel.setVisible(true);
                ingBrutosLabel.setVisible(true);
            }
                
                
            totalPagosLabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalPagos()));
            totalBonificacionesLabel.setText(df.format(Context.getInstance().currentDMTicket().getBonificaciones()));
            totalTicketLabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalGral()));
            cambioLabel.setText(df.format(Context.getInstance().currentDMTicket().getSaldo().abs()));
            
            totalNetoLabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalNeto()));
            totalIVALabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalIva()));
            totalInternoLabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalInterno()));
            totalExentoIVALabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalExento()));
            ingBrutosLabel.setText(df.format(Context.getInstance().currentDMTicket().getRetencion()));
                    
            stackPaneMensajeCancelar.setVisible(false);
            
            tabPaneController.repeatFocus(borderPane);
            tabPaneController.setTabPaneModalCommand(this);
                    
    }
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
            log.info("Ingresando al mètodo init");
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
                        //stackPaneMensajeCancelar.setVisible(true);
                        //tabPaneController.repeatFocus(stackPaneMensajeCancelar);
                        this.tabPaneController.getLabelMensaje().setText("¿Desea abandonar la confirmación del ticket?");
                        this.tabPaneController.mostrarMensajeModal();
                    }
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        this.tabPaneController.getLabelMensaje().setText("¿Confirma el cierre del ticket?");
                        this.ticketConfirmado = true;
                        this.tabPaneController.mostrarMensajeModal();
                    }
                    keyEvent.consume();
                });
                
                /*stackPaneMensajeCancelar.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.S){
                        tabPaneController.gotoPago();
                    }
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        stackPaneMensajeCancelar.setVisible(false);
                        tabPaneController.repeatFocus(borderPane);
                    }
                    keyEvent.consume();
                });*/
            });
            
            
                    
    }    
    
    private void setTotales(Factura factura) throws TpvException{
            //-----------resumen de calculos en cabecera-----------
            BigDecimal totalBonifCombos = BigDecimal.ZERO;
            BigDecimal totalIvaBonifCombos = BigDecimal.ZERO;

            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
                FacturaDetalleCombo fdc = it.next();
                factura.getDetalleCombos().add(fdc);

                fdc.setFactura(factura);
                log.info("          Combo: "+fdc.getCombo().getDescripcion());
            }             

            FacturaDetalle facturaDetalle = new FacturaDetalle();
            
            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombos().iterator();it.hasNext();){
                FacturaDetalleCombo fdc = it.next();
                facturaDetalle.setCantidad(BigDecimal.valueOf(fdc.getCantidad()));
                facturaDetalle.setDescuento(BigDecimal.ZERO);
                facturaDetalle.setExento(fdc.getExentoBonif());
                facturaDetalle.setImpuestoInterno(fdc.getImpuestoInterno().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setIva(fdc.getIvaCompletoBonif().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setIvaReducido(fdc.getIvaReducidoBonif().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setNeto(fdc.getNetoCompletoBonif().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setNetoReducido(fdc.getNetoReducidoBonif().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setPrecioUnitario(BigDecimal.ZERO);
                facturaDetalle.setPrecioUnitarioBase(BigDecimal.ZERO);
                facturaDetalle.setPorcentajeIva(BigDecimal.ZERO);
                facturaDetalle.setSubTotal(fdc.getBonificacion().multiply(BigDecimal.valueOf(-1)));            
                totalBonifCombos = totalBonifCombos.add(fdc.getBonificacion());
                totalIvaBonifCombos = totalIvaBonifCombos.add(fdc.getIVABonificacion());
                facturaDetalle.setProducto(fdc.getCombo().getProducto());
                facturaDetalle.setFactura(factura);
                factura.getDetalle().add(facturaDetalle);
                
            }
            
            BigDecimal total= BigDecimal.ZERO;
            BigDecimal costo = BigDecimal.ZERO;
            BigDecimal neto = BigDecimal.ZERO;
            BigDecimal netoReducido = BigDecimal.ZERO;
            BigDecimal impuestoInterno= BigDecimal.ZERO;
            BigDecimal descuento = BigDecimal.ZERO;
            BigDecimal exento = BigDecimal.ZERO;
            BigDecimal ivaReducido = BigDecimal.ZERO;
            BigDecimal iva = BigDecimal.ZERO;

            
            for(Iterator<FacturaDetalle>it = factura.getDetalle().iterator();it.hasNext();){
                FacturaDetalle fd = it.next();
                fd.getProducto().decStock(fd.getCantidad());
                total=total.add(fd.getSubTotal());
                costo = costo.add(fd.getPrecioUnitario());
                neto = neto.add(fd.getNeto());
                netoReducido = netoReducido.add(fd.getNetoReducido());
                descuento = descuento.add(fd.getDescuento());
                exento = exento.add(fd.getExento());
                iva = iva.add(fd.getIva());
                ivaReducido = ivaReducido.add(fd.getIvaReducido());
                impuestoInterno = impuestoInterno.add(fd.getImpuestoInterno());
                
            }
            factura.setNeto(neto);
            factura.setIva(iva);
            factura.setIvaReducido(ivaReducido);
            factura.setNetoReducido(netoReducido);
            factura.setImpuestoInterno(impuestoInterno);
            //TODO asignar el valor correcto
            factura.setCosto(costo);
            factura.setDescuento(descuento);
            factura.setExento(exento);
            factura.setTotal(total);
            factura.setRetencion(BigDecimal.ZERO);
            factura.setBonificacion(totalBonifCombos);
            factura.setIvaBonificacion(totalIvaBonifCombos);
            //---------fin cálculo en cabecera----
            //--------verificacion y aplicacion de ingreso brutos si fuese necesario---------
            //TODO la condicion de iva está siendo usado con hard code en la ret.Ing.Brutos
            if(factura.getCliente()!=null
                    && factura.getCliente().getCondicionIva().getId()==2
                    ){
                BigDecimal porcentajeRet = factService.getRetencionIngBrutoCliente(Context.getInstance()
                                .currentDMTicket().getCliente().getCuit());    
                BigDecimal netoGral = factura.getNeto().add(factura.getNetoReducido());
                BigDecimal montoRet = netoGral.multiply(porcentajeRet).divide(BigDecimal.valueOf(100));
                montoRet = montoRet.setScale(2,BigDecimal.ROUND_HALF_EVEN);
                if(montoRet.compareTo(Context.getInstance().currentDMParametroGral().getMontoMinRetIngBrutos())>0){
                    factura.setRetencion(montoRet);
                }
            }
            factura.setTotal(factura.getTotal().add(factura.getRetencion()));
            
            
            //---------------------------------------------------------------------------------
            
        
    }
            
    private void confirmarFactura(){
        try{
            log.info("Cerrando y confirmando factura ");
            
            //Factura factura = factService.calcularCombos(Context.getInstance().currentDMTicket().getIdFactura());
            Factura factura = factService.getFacturaConTotalesConPagos(Context.getInstance().currentDMTicket().getIdFactura()
                                    ,Context.getInstance().currentDMTicket().getPagos().iterator());
            
            //log.info("Cantidad de combos a guardar en la base de datos: "+factura.getDetalleCombosAux().size());
           
            
            //setTotales(factura); //tengo que llamar de dos veces el cálculo de totales
                                 //sino el comando de cierre de ticket de la impresora
                                 //no funciona
            
            /*for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
                FacturaDetalleCombo fdc = it.next();
                //TODO en las bonificaciones de los combos
                //impresoraService.imprimirLineaTicket(fdc.getCombo().getDescripcion(), fdc.getCantidad()
                //            ,fdc.getBonificacion() ,producto.getValorImpositivo().getValor() ,Context.getInstance().currentDMTicket().isImprimeComoNegativo(), producto.getImpuestoInterno());
                
            }*/
            
            impresoraService.cerrarTicket(factura);
            

        }catch(TpvException e){
            log.error("Error en controlador llamando al método cerrarTicket de ImpresoraService",e);
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
            tabPaneController.gotoError();
        }
    }
    
    
    private void asignarEvento(){
        this.fiscalPrinterEvent = new FiscalPrinterEvent(){
            @Override
            public void commandExecuted(FiscalPrinter source, FiscalPacket command, FiscalPacket response){
                log.debug("Se ejecutó correctamente el siguiente comando:");
                
                if(command.getCommandCode()==HasarCommands.CMD_GET_DATE_TIME){
                    fechaHoraFiscal = response.getString(3)+" "+response.getString(4);
                }
                
                if(command.getCommandCode()==HasarCommands.CMD_CLOSE_FISCAL_RECEIPT){
                    String nroTicketEmitido = response.getString(3);
                    try{
                            log.info("El cierre del ticket para el id de Factura : "+Context.getInstance().currentDMTicket().getIdFactura()
                                +" en la impresora fiscal fue correcto. A continuación se cierra el ticket en la base de datos");
                            //List<FacturaFormaPagoDetalle> pagos = new ArrayList<FacturaFormaPagoDetalle>();
                            //ListProperty<LineaPagoData> detallePagosData = Context.getInstance().currentDMTicket().getPagos();
                            //Factura factura = factService.calcularCombos(Context.getInstance().currentDMTicket().getIdFactura());
                            
                            Factura factura = factService
                                       .getFacturaConTotalesConPagos(Context.getInstance().currentDMTicket().getIdFactura()
                                                ,Context.getInstance().currentDMTicket().getPagos().iterator());
                            
                            factura.setNumeroComprobante(nroTicketEmitido);
                            factura.setFechaHoraFiscal(fechaHoraFiscal);


                            
                            
                            

                            
                            //Factura factura = factService.getFactura(Context.getInstance().currentDMTicket().getIdFactura());
                            /*log.info("Cantidad de formas de pago: "+detallePagosData.size());
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
                                formaPagoDetalle.setIvaInteres(item.getIvaInteres());
                                formaPagoDetalle.setIvaBonificacion(item.getIvaBonficacion());
//                                factura.getDetallePagos().add(formaPagoDetalle);
                                factura.addFormaPago(formaPagoDetalle);
                                log.info("                  Código forma: "+item.getCodigoPago());
                            }*/
                            
                            //setTotales(factura);
                            
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
    
    /*private void repeatFocus(Node node){
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
                repeatFocus(node);
            }
        });        
    }*/    
    
    public void aceptarMensajeModal(){
        this.tabPaneController.ocultarMensajeModal();
        if (this.ticketConfirmado)
            confirmarFactura();            
        else    
            this.tabPaneController.gotoPago();
    }
    
    public void cancelarMensajeModal(){
        this.ticketConfirmado = false;
        this.tabPaneController.ocultarMensajeModal();
        this.tabPaneController.repeatFocus(borderPane);
    }


    
    
}
