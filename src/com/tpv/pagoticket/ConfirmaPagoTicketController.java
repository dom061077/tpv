/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.enums.TipoTituloSupervisorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.FacturaDetalleCombo;
import com.tpv.modelo.FacturaDetalleConcurso;
import com.tpv.principal.Context;
import com.tpv.print.event.FiscalPrinterEvent;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.PagoService;
import com.tpv.service.ProductoService;
import com.tpv.util.ui.MensajeModal;
import com.tpv.util.ui.TabPaneModalCommand;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class ConfirmaPagoTicketController implements Initializable{
    Logger log = Logger.getLogger(ConfirmaPagoTicketController.class);
    
    private FacturacionService factService = new FacturacionService();
    private ImpresoraService impresoraService = new ImpresoraService();
    private ProductoService productoService = new ProductoService();
    private PagoService pagoService = new PagoService();
    private FiscalPrinterEvent fiscalPrinterEvent;
    private String fechaHoraFiscal;
    private ListProperty<LineaConcursoData> concursosList;
    private boolean flagEstadoImpresora ;
    private boolean flagCanceladoPorUsuario;

    
    
    
    
    
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
    private Label totalPagarLabel;
    
    
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
    private Label bonifTarjetaLabel;
    
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
    private Label intTarjetaLabel;
    

    
    @FXML
    private StackPane stackPaneConcursos;
    
    @FXML
    private TableView tableViewConcursos;
    
    @FXML
    private TableColumn concursoColumn;
    
    @FXML
    private TableColumn cantidadCuponesColumn;
    
    
    
    
    @FXML
    private TabPanePrincipalController tabPaneController;
    
    private boolean ticketConfirmado;        
            
    public void configurarInicio()throws TpvException{
            log.info("Ingresando a la confirmación de pago");
            ticketConfirmado=false;
            /*List concursos = factService.getConcursos(Context.getInstance().currentDMTicket().getIdFactura());
            for(Iterator<Object[]> it = concursos.iterator();it.hasNext();){
               Object[] o = it.next();
                //LineaConcursoData(Long codigoConcurso,String textoCorto,boolean imprimeTexto
                //,java.sql.Date vigenciaDesde, java.sql.Date vigenciaHasta
                //,int cantidadProductos, int cantidadConcursos)               
               LineaConcursoData lineaData = new LineaConcursoData(
                       Long.parseLong(o[1].toString()),o[2],
                );
            } */           
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
            bonifTarjetaLabel.setText(df.format(Context.getInstance().currentDMTicket().getBonificaciones()));
            totalTicketLabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalTicket()));
            totalPagarLabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalGral()));
            cambioLabel.setText(df.format(Context.getInstance().currentDMTicket().getCambioCliente()));
            
            intTarjetaLabel.setText(df.format(Context.getInstance().currentDMTicket().getInteresPorPagoTotal()));
            bonifTarjetaLabel.setText(df.format(Context.getInstance().currentDMTicket().getBonificacionPorPagoTotal()));
            
            
            totalNetoLabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalNeto()));
            totalIVALabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalIva()));
            totalInternoLabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalInterno()));
            totalExentoIVALabel.setText(df.format(Context.getInstance().currentDMTicket().getTotalExento()));
            ingBrutosLabel.setText(df.format(Context.getInstance().currentDMTicket().getRetencion()));
                    
            stackPaneConcursos.setVisible(false);
            
            tabPaneController.repeatFocus(tableViewPagos);
                    
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
                        log.debug("Bonificación en updateItem: "+item);
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
            
            ObservableList<LineaConcursoData> innerList = FXCollections.observableArrayList();
            concursosList = new SimpleListProperty<>(innerList);
            
            concursoColumn.setCellValueFactory(new PropertyValueFactory("textoCorto"));
            cantidadCuponesColumn.setCellValueFactory(new PropertyValueFactory("cantidadConcursos"));
            cantidadCuponesColumn.setStyle("-fx-alignment: CENTER-RIGHT");
            
            
            tableViewConcursos.setItems(concursosList);
            Platform.runLater(()->{
                tableViewPagos.setItems(Context.getInstance().currentDMTicket().getPagos());
                
                tableViewPagos.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        Context.getInstance().currentDMTicket().setTipoTituloSupervisor(TipoTituloSupervisorEnum.CANCELAR_CONFIRMACION_PAGO);
                        tabPaneController.gotoSupervisor();                        
                    }
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        //this.tabPaneController.getLabelMensaje().setText("¿Confirma el cierre del ticket?");
                        //this.ticketConfirmado = true;
                        //this.tabPaneController.mostrarMensajeModal();
                        tabPaneController.showMsgModal(new MensajeModal("Confirmación"
                                , "¿Confirma el cierre del ticket?", "", null){
                                    @Override
                                    public void aceptarMensaje(){
                                        tabPaneController.setDisableTabConfirmarPago(true);
                                        confirmarFactura();
                                    }
                                    
                                    @Override
                                    public void cancelarMensaje(){
                                        tabPaneController.repeatFocus(tableViewPagos);
                                    }
                               });
                        
                        
                    }
                    if(keyEvent.getCode() == KeyCode.TAB)
                        keyEvent.consume();
                });
                
                tableViewConcursos.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ENTER){
                        tabPaneController.gotoFacturacion();
                    }
                    keyEvent.consume();
                });
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
                facturaDetalle.setDescuentoCliente(BigDecimal.ZERO);
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
                //fd.getProducto().decStock(fd.getCantidad());
                total=total.add(fd.getSubTotal());
                costo = costo.add(fd.getCosto());
                neto = neto.add(fd.getNeto());
                netoReducido = netoReducido.add(fd.getNetoReducido());
                descuento = descuento.add(fd.getDescuentoCliente());
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
        Factura factura = null;
        flagCanceladoPorUsuario = false;
        try{
            log.info("Cerrando y confirmando factura ");
            
            factura = factService.getFacturaConTotalesConPagos(Context.getInstance().currentDMTicket().getIdDocumento()
                                    ,Context.getInstance().currentDMTicket().getPagos().iterator());
            if (factura.getClaseComprobante().compareTo("A")==0)
                factura
                  .setNumeroComprobante(
                          new Long(Context.getInstance().currentDMTicket().getNroFacturaA()));
            if (factura.getClaseComprobante().compareTo("B")==0)
                factura
                  .setNumeroComprobante(
                          new Long(Context.getInstance().currentDMTicket().getNroTicket()));
                
            factura = factService.confirmarFactura(factura);
        }catch(TpvException e){
            log.error("Error en controlador llamando al método cerrarTicket de ImpresoraService",e);
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
            tabPaneController.gotoError();
        }            
        Context.clearCurrentDMTicket();
           
        Context.getInstance().currentDMTicket().setIdDocumento(factura.getId());
        log.info("Factura cerrada y confirmada: "+factura.getId());
        final Factura facturaFinal = factura;
        /*try{
            impresoraService.cerrarTicket(factura);
        }catch(TpvException e){
            log.error("Error en controlador llamando al método cerrarTicket de ImpresoraService",e);
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            tabPaneController.gotoError();
        } */
            
        Thread confirmarTicketThread = new Thread(new Runnable(){
            @Override
                public void run(){
                    flagEstadoImpresora = false;
                    while(!flagEstadoImpresora){
                        try{
                            Thread.sleep(50);
                        }catch(InterruptedException ex){
                           log.warn("Error en sleep the hilo",ex);
                        }
                        try{
                            impresoraService.enviarConsultaEstado();
                            flagEstadoImpresora = true;
                        }catch(TpvException e){
                            tabPaneController.setMsgImpresoraVisible(true);
                            //tabPaneController.repeatFocus(borderPane);
                        }

                    }
                    
                    if (!flagCanceladoPorUsuario){
                        try{
                            impresoraService.cerrarTicket(facturaFinal);
                        }catch(Exception e){

                        }
                    }
                    
                    
                    Platform.runLater(new Runnable(){
                        public void run(){
                            tabPaneController.setMsgImpresoraVisible(false);
                            tabPaneController.setDisableTabConfirmarPago(false);                    
                            if(!flagCanceladoPorUsuario)
                                tabPaneController.gotoFacturacion();
                            else
                                tabPaneController.gotoMenuPrincipal();

                        }
                    });
                    
                }
            
            
        }) ;
        
        // don't let thread prevent JVM shutdown
        confirmarTicketThread.setDaemon(true);
        confirmarTicketThread.start();
        
        

    }
    
    /*public LineaConcursoData(Long codigoConcurso,String textoCorto,boolean imprimeTexto
            ,java.sql.Date vigenciaDesde, java.sql.Date vigenciaHasta
            ,int cantidadProductos, int cantidadConcursos){*/
    private void cargarGrillaConcursos(Factura factura){
        concursosList.clear();
        factura.getDetalleConcursos().forEach(item->{
            FacturaDetalleConcurso factDetConcurso =  (FacturaDetalleConcurso)item;
            LineaConcursoData detConcurso = 
                    new LineaConcursoData(item.getId(),item.getConcurso().getTextoCorto()
                            ,item.getConcurso().isImprimeTexto()
                            ,item.getConcurso().getVigenciaDesde()
                            ,item.getConcurso().getVigenciaHasta()
                            ,item.getConcurso().getCantidadProductos()
                            ,item.getCantidadCupones());
            concursosList.add(detConcurso);
        });
        
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
//registrarNroComprobanteYHoraFiscal(Long idFactura
//            ,Long numeroComprobante,String fechaHoraFiscal)                    
                    try{
                        factService.registrarNroComprobanteYHoraFiscal(
                            Context.getInstance().currentDMTicket().getIdDocumento()
                            , Long.parseLong(nroTicketEmitido), fechaHoraFiscal);
                        tabPaneController.gotoFacturacion();
                    }catch(TpvException e){
                        Context.getInstance().currentDMTicket().setException(e);
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
                        tabPaneController.gotoError();
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }
                    /*
                    try{
                            log.info("El cierre del ticket para el id de Factura : "+Context.getInstance().currentDMTicket().getIdDocumento()
                                +" en la impresora fiscal fue correcto. A continuación se cierra el ticket en la base de datos");
                            
                            Factura factura = factService
                                       .getFacturaConTotalesConPagos(Context.getInstance().currentDMTicket().getIdDocumento()
                                                ,Context.getInstance().currentDMTicket().getPagos().iterator());
                            
                            factura.setNumeroComprobante(Long.parseLong(nroTicketEmitido));
                            factura.setFechaHoraFiscal(fechaHoraFiscal);
                            factura = factService.confirmarFactura(factura);
                            Context.getInstance().currentDMTicket()
                                    .setNroTicket(Context.getInstance().currentDMTicket().getNroTicket()+1);
                            Context.clearCurrentDMTicket();
                            
                            log.info("Factura cerrada y confirmada: "+factura.getId());
                            if(factura.getDetalleConcursos().size()>0){
                                cargarGrillaConcursos(factura);
                                stackPaneConcursos.setVisible(true);
                                
                                tabPaneController.repeatFocus(tableViewConcursos);
                                tableViewConcursos.getSelectionModel().select(0);
                            }else
                                tabPaneController.gotoFacturacion();
                    }catch(TpvException e){
                        Context.getInstance().currentDMTicket().setException(e);
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CONFIRMARTICKET);
                        tabPaneController.gotoError();
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }
                    */  
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
    
    public void killEstadoImpresora(){
        flagCanceladoPorUsuario = true;
        flagEstadoImpresora = true;
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
    
    /*public void aceptarMensajeModal(){
        this.tabPaneController.getLabelCancelarModal().setVisible(true);
        this.tabPaneController.ocultarMensajeModal();
        confirmarFactura();            
    }
    
    public void cancelarMensajeModal(){
        this.ticketConfirmado = false;
        this.tabPaneController.ocultarMensajeModal();
        this.tabPaneController.repeatFocus(tableViewPagos);
    }*/


    
    
}
