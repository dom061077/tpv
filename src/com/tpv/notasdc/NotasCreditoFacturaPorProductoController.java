/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.notasdc;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.enums.TipoTituloSupervisorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.MotivoNotaDC;
import com.tpv.modelo.Producto;
import com.tpv.modelo.enums.FacturaEstadoEnum;
import com.tpv.modelo.enums.TipoComprobanteEnum;
import com.tpv.principal.Context;
import com.tpv.principal.LineaTicketData;
import com.tpv.print.event.FiscalPrinterEvent;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.ProductoService;
import com.tpv.util.Util;
import com.tpv.util.ui.MaskTextField;
import com.tpv.util.ui.MensajeModal;
import com.tpv.util.ui.MensajeModalAceptar;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;
import org.tpv.print.fiscal.FiscalPacket;
import org.tpv.print.fiscal.FiscalPrinter;
import org.tpv.print.fiscal.hasar.HasarCommands;
import org.tpv.print.fiscal.msg.FiscalMessages;


/**
 *
 * @author COMPUTOS
 */
public class NotasCreditoFacturaPorProductoController implements Initializable {
    Logger log = Logger.getLogger(NotasCreditoFacturaPorProductoController.class);
    TabPanePrincipalController tabPaneController;
    
    @FXML StackPane stackPaneMotivos;
    @FXML GridPane gridPaneIngreso;
    
    @FXML private Label labelNumeroFactura;
    @FXML private Label labelCliente;
    @FXML private Label labelNumeroFacturaDato;
    @FXML private Label labelCuitClienteDato;
    @FXML private Label labelNombreClienteDato;
    @FXML private Label labelTotalFacturaDato;
    @FXML private Label labelMotivoData;   
    @FXML private TableView tableViewMotivos;
    @FXML private GridPane gridPaneDatos;
    @FXML private TableColumn  motivoCodigoColumn;
    @FXML private TableColumn motivoDescripcionColumn;
    @FXML private TableView tableViewTickets;
    
    @FXML private TableColumn codigoColumn;
    @FXML private TableColumn descripcionColumn;
    @FXML private TableColumn cantidadColumn;
    @FXML private TableColumn precioUnitarioColumn;
    @FXML private TableColumn subTotalColumn;
    @FXML private Label labelCantidad;
    @FXML private Label labelTotal; 
    @FXML private GridPane gridPaneCantidad;
    @FXML private StackPane stackPaneCantidad;
    
    
    MaskTextField textFieldPrefijo;
    MaskTextField textFieldNumero;
    MaskTextField textFieldProducto;
    MaskTextField textFieldCantidad;
    private ObjectProperty<BigDecimal> cantidad = new SimpleObjectProperty(null);
    BigDecimal total = BigDecimal.ZERO;    
    
    private Factura facturaOrigenCredito;
    
    private FacturacionService factService = new FacturacionService();
    private ProductoService productoService = new ProductoService();
    private int idMotivo;
    private Long numeroComprobante;
    private ImpresoraService impresoraService = new ImpresoraService();
    private FiscalPrinterEvent fiscalPrinterEvent;
    private LineaTicketData lineaTicketData;
    private String fechaHoraFiscal;
    
    public void setTabController(TabPanePrincipalController tabPaneController){
        this.tabPaneController=tabPaneController;
    }
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {    
        textFieldPrefijo = new MaskTextField();
        textFieldPrefijo.setMask("N!");
        textFieldPrefijo.getStyleClass().add("textfield_sin_border");
        gridPaneIngreso.add(textFieldPrefijo, 2,1 );
        
        textFieldNumero = new MaskTextField();
        textFieldNumero.setMask("N!");
        textFieldNumero.getStyleClass().add("textfield_sin_border");
        gridPaneIngreso.add(textFieldNumero, 2, 2);
        
        textFieldProducto = new MaskTextField();
        textFieldProducto.setMask("N!");
        textFieldProducto.getStyleClass().add("textfield_sin_border");
        gridPaneDatos.add(textFieldProducto, 1, 4);
        
        textFieldCantidad = new MaskTextField();
        textFieldCantidad.setMask("N!");
        textFieldCantidad.setMaxDigitos(3);
        textFieldCantidad.getStyleClass().add("textfield_sin_border");
        textFieldCantidad.setPrefWidth(100);
        textFieldCantidad.setMaxWidth(100);
        textFieldCantidad.setMinWidth(100);
        
        
        labelCantidad.textProperty().bind(this.cantidad.asString());
        
        gridPaneCantidad.add(textFieldCantidad, 1, 1);
              
        
        
        
        Platform.runLater(()->{
            /*tableViewTickets.setOnKeyPressed(keyEvent ->{
                
                if(keyEvent.getCode() == KeyCode.F11){
                    tabPaneController.gotoNotasDCMenu();
                            
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.TAB)
                    keyEvent.consume();
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    limpiarLabelsDatoYTableViewTickets();
                    tabPaneController.repeatFocus(textFieldPrefijo);
                    keyEvent.consume();
                }
                
                
                if(keyEvent.getCode() == KeyCode.ENTER){
                        if (facturaOrigenCredito.getSaldoDispNotasDC()
                                .compareTo(facturaOrigenCredito.getTotal())<0){
                            tabPaneController
                                  .showMsgModal(new MensajeModalAceptar("Error"
                                            ,"El monto del crédito supera el saldo disponible para crédito"
                                              ,"",tableViewTickets));            
                            return;
                        }                        
                    
                        tabPaneController.showMsgModal(new MensajeModal("Confirmación"
                                , "¿Confirma el cierre del ticket?", "", null){
                                    @Override
                                    public void aceptarMensaje(){
                                        enviarComandoLineaTicket();
                                    }
                                    
                                    @Override
                                    public void cancelarMensaje(){
                                        tabPaneController.repeatFocus(tableViewTickets);
                                    }
                               });                    
                    
                    
                }
            });*/
            
            tableViewMotivos.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    stackPaneMotivos.setVisible(false);
                    labelMotivoData
                       .setText(((LineaMotivoData)tableViewMotivos.getSelectionModel().getSelectedItem()).getDescripcion());
                    idMotivo = ((LineaMotivoData)tableViewMotivos.getSelectionModel().getSelectedItem()).getCodigoMotivo();
                    this.tabPaneController.repeatFocus(this.textFieldProducto);
                    //tabPaneController.repeatFocus(tableViewTickets);
                    //if(tableViewTickets.getItems().size()>0)
                        //tableViewTickets.getSelectionModel().select(0);
                        
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    stackPaneMotivos.setVisible(false);
                    tabPaneController.repeatFocus(textFieldPrefijo);
                    limpiarLabelsDatoYTableViewTickets();
                }
                    
                if(keyEvent.getCode() == KeyCode.TAB)
                    keyEvent.consume();
                
            });
            
            
            textFieldPrefijo.setOnKeyPressed(keyEvent ->{
                
                if(keyEvent.getCode() == KeyCode.F11){
                    tabPaneController.gotoNotasDCMenu();
                            
                    keyEvent.consume();
                }                
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(!textFieldPrefijo.getText().trim().equals("")){
                        this.tabPaneController.repeatFocus(textFieldNumero);
                    }
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.TAB)
                    keyEvent.consume();                
                if(keyEvent.getCode() == KeyCode.F11)
                    this.tabPaneController.gotoNotasDCMenu();
            });
            textFieldNumero.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.F11){
                    tabPaneController.gotoNotasDCMenu();
                            
                    keyEvent.consume();
                }                
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    this.tabPaneController.repeatFocus(textFieldPrefijo);
                    
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.TAB)
                    keyEvent.consume();                
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(!this.textFieldNumero.getText().trim().equals("")){
                        this.traerTicket(
                                Long.parseLong(textFieldPrefijo.getText())
                                ,Long.parseLong(textFieldNumero.getText())
                        );
                        if(this.facturaOrigenCredito!=null){
                            stackPaneMotivos.setVisible(true);
                            this.tabPaneController.repeatFocus( tableViewMotivos);                            
                        }
                    }
                    keyEvent.consume();
                }
            });
            
            textFieldProducto.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.TAB)
                    keyEvent.consume();
                if(keyEvent.getCode() == KeyCode.F11){
                    if(tableViewTickets.getItems().size()==0)
                        tabPaneController.gotoNotasDCMenu();
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    if(tableViewTickets.getItems().size()==0){
                        limpiarLabelsDatoYTableViewTickets();
                        tabPaneController.repeatFocus(textFieldPrefijo);
                        this.cantidad.setValue(BigDecimal.ONE);
                    }
                    keyEvent.consume();
                }
                
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(textFieldProducto.getText().compareTo("")!=0)
                        enviarComandoLineaTicket();
                    else{
                        tabPaneController.showMsgModal(new MensajeModal("Confirmación"
                                , "¿Confirma el cierre de la nota de crédito?", "", null){
                                    @Override
                                    public void aceptarMensaje(){
                                        confirmarCredito();
                                    }
                                    
                                    @Override
                                    public void cancelarMensaje(){
                                        tabPaneController.repeatFocus(textFieldProducto);
                                    }
                               });                          
                    }
                    textFieldProducto.setText("");
                    keyEvent.consume();
                }
                
                if(keyEvent.getCode() == KeyCode.F7){
                    if(Context.getInstance().currentDMTicket().getIdDocumento()
                            !=null){
                        Context.getInstance().currentDMTicket().setTipoTituloSupervisor(TipoTituloSupervisorEnum.CANCELAR_NOTADC);
                        //habilitarSupervisorButton.fire();
                        tabPaneController.gotoSupervisor();
                    }else{
                        log.error("No se puede cancelar una nota de crédito que no está abierta.");
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURAPROPRODUCTO);
                        Context.getInstance().currentDMTicket().setException(new TpvException("No se puede cancelar una nota de crédito que no está abierta"));
                        tabPaneController.gotoError();
                    }
                    keyEvent.consume();
                }

                if(keyEvent.getCode() == KeyCode.F4){
                    stackPaneCantidad.setVisible(true);
                    tabPaneController.repeatFocus(textFieldCantidad);
                    keyEvent.consume();
                }
                
                
            });
            textFieldCantidad.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    stackPaneCantidad.setVisible(false);
                    tabPaneController.repeatFocus(textFieldProducto);
                    keyEvent.consume();
                }
                
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(textFieldCantidad.getText().trim().compareTo("")!=0){
                        BigDecimal cantidad = new BigDecimal(textFieldCantidad.getText());
                        if(cantidad.compareTo(BigDecimal.ZERO)>0){
                            this.cantidad.setValue(cantidad);
                            stackPaneCantidad.setVisible(false);
                            tabPaneController.repeatFocus(textFieldProducto);
                            keyEvent.consume();
                        }
                    }
                }
                
            });

        });
        
        
    }
    
    private void verificarDetalleTableView() throws TpvException{
        log.info("Verificando detalle de TableView de créditos por producto");
        String nombreComprador=" ";
        String cuitComprador=" ";
        String domicilioComprador=" ";
        String tipoCreditoImpresion=ImpresoraService.NOTACREDITO_BC;
        String condicionIVA=ImpresoraService.IVA_CONSUMIDOR_FINAL;
        String tipoDocImpresion = " ";        
        if(Context.getInstance().currentDMTicket().isReinicioVerificado()){
            return;
        }
        Factura notaDC = factService.getFacturaAbiertaPorCheckout(
                Context.getInstance().currentDMTicket().getCheckout().getId()
                ,Context.getInstance().currentDMTicket().getUsuario().getIdUsuario()
                , TipoComprobanteEnum.C);
        if(notaDC!=null){
            log.info("Se econtró notaDC abierta con id: "+notaDC.getId()+", se procede a anulación en BD");
            this.facturaOrigenCredito = notaDC.getFacturaOrigen();
            Context.getInstance().currentDMTicket().setIdDocumento(notaDC.getId());
            traerTicket(this.facturaOrigenCredito.getPrefijoFiscal()
                ,this.facturaOrigenCredito.getNumeroComprobante());
            factService.anularFacturaPorReinicio(notaDC.getId());
            if(Context.getInstance().currentDMTicket().isTicketAbierto()){
                log.warn("Factura abierta en impresora. Se procede a cancelar documento en Impresora");
                impresoraService.cancelarTicket();
            }
            if(notaDC.getCliente()!=null){
                Context.getInstance().currentDMTicket().setCliente(notaDC.getCliente());
                if(facturaOrigenCredito.getCliente()!=null){
                    tipoCreditoImpresion = ImpresoraService.NOTACREDITO_A;
                    nombreComprador = facturaOrigenCredito.getCliente().getRazonSocial();
                    cuitComprador = facturaOrigenCredito.getCliente().getCuit();
                    domicilioComprador = facturaOrigenCredito.getCliente().getDireccion();
                    tipoDocImpresion = "C";
                    condicionIVA = ImpresoraService
                                    .getTraduccionCondicionIVA(
                                            facturaOrigenCredito.getCliente().getCondicionIva().getId());
                }                  
            }
            Context.getInstance().currentDMTicket().setClienteSeleccionado(true);
            this.impresoraService.abrirNotaCredito(tipoCreditoImpresion
                , facturaOrigenCredito.getPrefijoFiscal()+"-"+ facturaOrigenCredito.getNumeroComprobante()
                , nombreComprador, cuitComprador, condicionIVA, domicilioComprador, tipoDocImpresion);
            for(Iterator<FacturaDetalle> it = notaDC.getDetalle().iterator();it.hasNext();){
                FacturaDetalle fd = it.next();
                this.lineaTicketData = new LineaTicketData(
                         fd.getProducto().getIdProducto()
                        ,fd.getProducto().getCodigoProducto()
                        ,fd.getProducto().getCodBarra()
                        ,fd.getProducto().getDescripcionConCodigo()
                        ,fd.getCantidad().multiply(BigDecimal.valueOf(-1))
                        ,fd.getPrecioUnitario().multiply(BigDecimal.valueOf(-1))
                        ,fd.getPrecioUnitarioBase().multiply(BigDecimal.valueOf(-1))
                        ,fd.getNeto().multiply(BigDecimal.valueOf(-1))
                        ,fd.getNetoReducido().multiply(BigDecimal.valueOf(-1))
                        ,fd.getExento().multiply(BigDecimal.valueOf(-1))
                        ,fd.getDescuentoCliente().multiply(BigDecimal.valueOf(-1))
                        ,fd.getIva().multiply(BigDecimal.valueOf(-1))
                        ,fd.getIvaReducido().multiply(BigDecimal.valueOf(-1))
                        ,fd.getImpuestoInterno().multiply(BigDecimal.valueOf(-1))
                        ,BigDecimal.ZERO
                        ,fd.getPorcentajeIva()
                        ,fd.getCosto().multiply(BigDecimal.valueOf(-1))
                        ,false
                );
                BigDecimal coeficienteK = ImpresoraService.getCoeficienteK(
                        fd.getImpuestoInterno().multiply(BigDecimal.valueOf(-1))
                             .divide(fd.getCantidad().multiply(BigDecimal.valueOf(-1))
                                     ,4,RoundingMode.HALF_EVEN)
                        , fd.getPrecioUnitarioBase().multiply(BigDecimal.valueOf(-1)));
                this.impresoraService.imprimirLineaTicket(
                        fd.getProducto().getDescripcion()
                        ,fd.getCantidad().multiply(BigDecimal.valueOf(-1)),fd.getPrecioUnitario().multiply(BigDecimal.valueOf(-1))
                        ,fd.getPorcentajeIva(),false,coeficienteK
                );
                
            }
            tabPaneController.repeatFocus(textFieldProducto);
            
        }
        Context.getInstance().currentDMTicket().setReinicioVerificado(true);
    }
    
    public void configurarInicio() throws TpvException{
        log.info("ConfigurarInicio en notas NotasCreditoFacturaProductoController");
        stackPaneMotivos.setVisible(false);
        tabPaneController.actualizarInfoImpresoraEnContexto();
        
        if(impresoraService.getHfp().getEventListener()==null)
            asignarEvento();
        if(Context.getInstance().currentDMTicket().getIdDocumento()==null){
            initTableViewMotivos();
            cargarTableViewMotivos();
            initTableViewTickets();
            limpiarLabelsDatoYTableViewTickets();
            verificarDetalleTableView();
        }
        if(Context.getInstance().currentDMTicket().getIdDocumento()==null)
            tabPaneController.repeatFocus(textFieldPrefijo);
            
        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURAPROPRODUCTO);
        
        this.cantidad.setValue(BigDecimal.ONE);
    }

    /*private void cargarTableViewTickets(){
        tableViewTickets.getItems().clear();
        for(Iterator iterator = facturaOrigenCredito.getDetalleOrdenadoPorId().iterator();iterator.hasNext();){
                FacturaDetalle fd = (FacturaDetalle)iterator.next();


                LineaTicketData lineaTicketData = new LineaTicketData(
                                 fd.getId()
                                ,fd.getProducto().getCodigoProducto()
                                ,fd.getProducto().getCodBarra()
                                ,fd.getProducto().getDescripcion(),fd.getCantidad()
                                ,fd.getPrecioUnitario()
                                ,fd.getPrecioUnitarioBase()
                                ,fd.getNeto(),fd.getNetoReducido(),fd.getExento()
                                ,fd.getDescuento()
                                ,fd.getIva()
                                ,fd.getIvaReducido()
                                ,fd.getImpuestoInterno()
                                ,new BigDecimal(0)
                                ,fd.getPorcentajeIva()
                                ,fd.getCosto()
                                ,(fd.getSubTotal().compareTo(BigDecimal.ZERO)<0?true:false)
                ); 
                tableViewTickets.getItems().add(lineaTicketData);
                
        }
    }*/
    
    private void traerTicket(Long prefijo,Long numero){
        try{
            this.facturaOrigenCredito = factService.getFactura(prefijo, numero);
            if(this.facturaOrigenCredito==null){
                tabPaneController.showMsgModal(new MensajeModalAceptar(
                        "Mensaje","La factura con el prefijo y Nº ingresado no existe."
                        ,"",textFieldPrefijo
                ));
                return;
            }
            
            if(facturaOrigenCredito.getSaldoDispNotasDC().compareTo(BigDecimal.ZERO)<=0){
                this.facturaOrigenCredito = null;
                tabPaneController.showMsgModal(new MensajeModalAceptar("Mensaje"
                        , "La factura con el prefijo y Nº ingresado no tiene saldo para acreditar."
                            +" No se puede registrar la nota de crédito."
                        , "", textFieldPrefijo));
                return;
            }
            
            
            if(this.facturaOrigenCredito!=null){
                labelNumeroFacturaDato.setText(facturaOrigenCredito.getPrefijoFiscal().toString()
                    +'-'+facturaOrigenCredito.getNumeroComprobante());
                labelTotalFacturaDato.setText(Util.formatearDecimal(facturaOrigenCredito.getTotal()));
                if(facturaOrigenCredito.getCliente()!=null){
                    labelCuitClienteDato.setText(facturaOrigenCredito.getCliente().getCuit());
                    labelNombreClienteDato.setText(facturaOrigenCredito.getCliente().getRazonSocial());
                    this.ocultarRowCliente(false);
                }else
                    this.ocultarRowCliente(true);
                //cargarTableViewTickets();
                
                DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                labelTotal.setText(df.format(BigDecimal.ZERO));
                
                
                
                
            }else{
                tabPaneController
                      .showMsgModal(new MensajeModalAceptar("Mensaje"
                                ,"No existe ninguna factura con el prefijo y Nº de factura ingresados"
                                  ,"",textFieldPrefijo));                 
            }
        }catch(TpvException e){
                Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOMONTO);
                Context.getInstance().currentDMTicket().setException(e);
                tabPaneController.gotoError();
        }
        textFieldPrefijo.setText("");
        textFieldNumero.setText("");
        
    }    
    
    private void ocultarRowCliente(boolean ocultar){
        if (ocultar){
            this.gridPaneDatos.getRowConstraints().get(1).setPrefHeight(0);
            this.gridPaneDatos.getRowConstraints().get(1).setMaxHeight(0);
            this.gridPaneDatos.getRowConstraints().get(1).setMinHeight(0);
            labelCliente.setVisible(false);
            labelCuitClienteDato.setVisible(false);
            labelNombreClienteDato.setVisible(false);
        }else{
            this.gridPaneDatos.getRowConstraints().get(1).setPrefHeight(60);
            this.gridPaneDatos.getRowConstraints().get(1).setMaxHeight(60);
            this.gridPaneDatos.getRowConstraints().get(1).setMinHeight(60);
            labelCliente.setVisible(true);
            labelCuitClienteDato.setVisible(true);
            labelNombreClienteDato.setVisible(true);
        }
    }  
    
    private void cargarTableViewMotivos() throws TpvException{
        tableViewMotivos.getItems().clear();
        List<MotivoNotaDC> list = factService.getMotivos(TipoComprobanteEnum.C);
        for(Iterator<MotivoNotaDC> it = list.iterator();it.hasNext(); ){
            MotivoNotaDC motivo = it.next();
            tableViewMotivos.getItems().add(new LineaMotivoData(
                    motivo.getId(),motivo.getDetalle()
                ));
        }
        if(tableViewMotivos.getItems().size()>0)
            tableViewMotivos.getSelectionModel().select(0);
    }    
    
    private void initTableViewMotivos(){
        motivoCodigoColumn.setCellValueFactory(new PropertyValueFactory<LineaMotivoData,Integer>("codigo"));
        motivoDescripcionColumn.setCellValueFactory(new PropertyValueFactory<LineaMotivoData,String>("descripcion"));
    }       
    
    private void limpiarLabelsDatoYTableViewTickets(){
            idMotivo=0;
            labelCuitClienteDato.setText("");
            labelNombreClienteDato.setText("");
            labelNumeroFacturaDato.setText("");
            labelTotalFacturaDato.setText("");
            labelMotivoData.setText("");
            tableViewTickets.getItems().clear();
            textFieldProducto.setText("");
            textFieldNumero.setText("");
            textFieldPrefijo.setText("");
    }    
    
    private void initTableViewTickets(){
        tableViewTickets.setRowFactory(new Callback<TableView<LineaTicketData>, TableRow<LineaTicketData>>(){
            @Override
            public TableRow<LineaTicketData> call(TableView<LineaTicketData> paramP) {
                return new TableRow<LineaTicketData>() {
                    
                    @Override
                    protected void updateItem(LineaTicketData item, boolean paramBoolean) {
                        super.updateItem(item, paramBoolean);
                        if (item!=null){
                            
                            if(item.getSubTotal().compareTo(BigDecimal.valueOf(0))<0){
                                setStyle(
                                     "-fx-background-color: red;"   
                                    +"-fx-background-insets: 0, 0 0 1 0;"
                                    +"-fx-padding: 0.0em;"
                                    +"-fx-text-fill: black;"
                                );
                                
                            }else{
                                setStyle(
                                    "-fx-background-insets: 0, 0 0 1 0;"
                                    +"-fx-padding: 0.0em;"
                                    +"-fx-text-fill: -fx-text-inner-color;"
                                );

                            }
                        }else{
                            getStyleClass().remove("table-row-cell-negativo");
                        }
                        setItem(item);
                        
                    }
                };
            }
        });
        
        
        
        codigoColumn.setCellValueFactory(new PropertyValueFactory<LineaTicketData,Integer>("codigoProducto"));
        codigoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        descripcionColumn.setCellValueFactory(new PropertyValueFactory("descripcion"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory("cantidad"));
        cantidadColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        cantidadColumn.setCellFactory(col ->{
            TableCell<LineaTicketData,BigDecimal> cell = new TableCell<LineaTicketData,BigDecimal>(){
                @Override
                public void updateItem(BigDecimal item,boolean empty){
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                            //String formattedDob = De
                            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
            
        });
        
        precioUnitarioColumn.setCellValueFactory(new PropertyValueFactory("precioUnitario"));
        precioUnitarioColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        precioUnitarioColumn.setCellFactory(col -> {
            TableCell<LineaTicketData,BigDecimal> cell = new TableCell<LineaTicketData,BigDecimal>(){
                @Override
                public void updateItem(BigDecimal item,boolean empty){
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                            //String formattedDob = De
                            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
        });
        subTotalColumn.setCellValueFactory(new PropertyValueFactory("subTotal"));
        subTotalColumn.setCellFactory(col->{
            TableCell<LineaTicketData,BigDecimal> cell = new TableCell<LineaTicketData,BigDecimal>(){
                @Override
                public void updateItem(BigDecimal item, boolean empty){
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                            //String formattedDob = De
                            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
//                            if(item.compareTo(BigDecimal.ZERO)<0)
//                                this.setStyle("-fx-text-fill: red");
//                            else
//                                this.setStyle("-fx-text-fill: black");
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
        });
        
        subTotalColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
    }    
    
    private void calcularTotalGeneral(){
        
        DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
        
        tableViewTickets.getItems().forEach((item)->{
            this.total = total.add(((LineaTicketData)item).getSubTotal());
        });
        labelTotal.setText(df.format(this.total));
        
    }
    
    private void enviarComandoLineaTicket(){
        String nombreComprador=" ";
        String cuitComprador=" ";
        String domicilioComprador=" ";
        String tipoCreditoImpresion=ImpresoraService.NOTACREDITO_BC;
        String condicionIVA=ImpresoraService.IVA_CONSUMIDOR_FINAL;
        String tipoDocImpresion = " ";
        if(facturaOrigenCredito.getCliente()!=null){
            tipoCreditoImpresion = ImpresoraService.NOTACREDITO_A;
            nombreComprador = facturaOrigenCredito.getCliente().getRazonSocial();
            cuitComprador = facturaOrigenCredito.getCliente().getCuit();
            domicilioComprador = facturaOrigenCredito.getCliente().getDireccion();
            tipoDocImpresion = "C";
            condicionIVA = ImpresoraService
                            .getTraduccionCondicionIVA(
                                    facturaOrigenCredito.getCliente().getCondicionIva().getId());
        }    
        try{
            FacturaDetalle det = validarDetalleProducto(textFieldProducto.getText()
                    ,this.cantidad.getValue());
            if(det==null)
                return;
            if(tableViewTickets.getItems().size()==0){
                    this.impresoraService.abrirNotaCredito(tipoCreditoImpresion
                        , facturaOrigenCredito.getPrefijoFiscal()+"-"+ facturaOrigenCredito.getNumeroComprobante()
                        , nombreComprador, cuitComprador, condicionIVA, domicilioComprador, tipoDocImpresion);
            }
            BigDecimal coeficienteK = BigDecimal.ZERO;
            coeficienteK = ImpresoraService.getCoeficienteK(
                    det.getImpuestoInterno().divide(this.cantidad.getValue(),4,RoundingMode.HALF_EVEN)
                    ,det.getPrecioUnitarioBase());        
            this.lineaTicketData = new LineaTicketData(
                     det.getProducto().getIdProducto()
                    ,det.getProducto().getCodigoProducto()
                    ,det.getProducto().getCodBarra()
                    ,det.getProducto().getDescripcionConCodigo(),this.cantidad.getValue()
                    ,det.getPrecioUnitario()
                    ,det.getPrecioUnitarioBase()
                    ,det.getNeto().divide(det.getCantidad(),RoundingMode.HALF_EVEN)
                                .multiply(this.cantidad.getValue())
                    ,det.getNetoReducido().divide(det.getCantidad(),RoundingMode.HALF_EVEN)
                                .multiply(this.cantidad.getValue())
                    ,det.getExento().divide(det.getCantidad(),RoundingMode.HALF_EVEN)
                                .multiply(this.cantidad.getValue())
                    ,det.getDescuentoCliente().divide(det.getCantidad(),RoundingMode.HALF_EVEN)
                                .multiply(this.cantidad.getValue())
                    ,det.getIva().divide(det.getCantidad(),RoundingMode.HALF_EVEN)
                                .multiply(this.cantidad.getValue())
                    ,det.getIvaReducido().divide(det.getCantidad(),RoundingMode.HALF_EVEN)
                                .multiply(this.cantidad.getValue())
                    ,det.getImpuestoInterno().divide(det.getCantidad(),RoundingMode.HALF_EVEN)
                                .multiply(this.cantidad.getValue())
                    ,BigDecimal.ZERO
                    ,det.getPorcentajeIva()
                    ,det.getCosto()
                    ,false
            );
            
            this.impresoraService.imprimirLineaTicket(det.getProducto().getDescripcion()
                    , this.cantidad.getValue(), det.getPrecioUnitario()
                    , det.getPorcentajeIva(), false, coeficienteK);
            textFieldProducto.setText("");
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURAPROPRODUCTO);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }           
            

    }
    
    private void confirmarCredito(){
        
        try{
           boolean cierreValido = this.factService.validarCierreNotaDC(Context.getInstance().currentDMTicket().getIdDocumento());
           if(!cierreValido){
                tabPaneController.showMsgModal(new MensajeModalAceptar(
                        "Error","Total de la Nota de Crédito supera el saldo a acreditar disponible de la factura origen."
                        ,"",textFieldPrefijo
                ));
                return;
           }
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURAPROPRODUCTO);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
            return;
        }
        try{
            this.impresoraService.cerrarNotaCredito(facturaOrigenCredito);
            limpiarLabelsDatoYTableViewTickets();
            tabPaneController.repeatFocus(textFieldPrefijo);
            
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURAPROPRODUCTO);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }
        /*String nombreComprador=" ";
        String cuitComprador=" ";
        String domicilioComprador=" ";
        String tipoCreditoImpresion=ImpresoraService.NOTACREDITO_BC;
        String condicionIVA=ImpresoraService.IVA_CONSUMIDOR_FINAL;
        String tipoDocImpresion = " ";
        
        Factura notaDC = null;

        if(facturaOrigenCredito.getCliente()!=null){
            tipoCreditoImpresion = ImpresoraService.NOTACREDITO_A;
            nombreComprador = facturaOrigenCredito.getCliente().getRazonSocial();
            cuitComprador = facturaOrigenCredito.getCliente().getCuit();
            domicilioComprador = facturaOrigenCredito.getCliente().getDireccion();
            tipoDocImpresion = "C";
            condicionIVA = ImpresoraService
                            .getTraduccionCondicionIVA(
                                    facturaOrigenCredito.getCliente().getCondicionIva().getId());
        }    
        try{
            notaDC = factService.confirmarNotaDCFactura(TipoComprobanteEnum.C, facturaOrigenCredito
                    ,Context.getInstance().currentDMTicket().getAperturaCierreCajDetalle()
                    ,Context.getInstance().currentDMTicket().getCheckout()
                    ,Context.getInstance().currentDMTicket().getUsuario()
                    ,idMotivo
                    ,Context.getInstance().currentDMTicket().getCaja());
            idNotaDCGenerada = notaDC.getId();
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURAPROPRODUCTO);
            tabPaneController.gotoError();
            return;
        }
        try{
            this.impresoraService.abrirNotaCredito(tipoCreditoImpresion
                , facturaOrigenCredito.getPrefijoFiscal()+"-"+ facturaOrigenCredito.getNumeroComprobante()
                , nombreComprador, cuitComprador, condicionIVA, condicionIVA, tipoDocImpresion);
            for(Iterator<FacturaDetalle> it = facturaOrigenCredito.getDetalle().iterator()
                    ;it.hasNext();){
                FacturaDetalle factDet = it.next();
                BigDecimal coeficienteK = ImpresoraService.getCoeficienteK(factDet.getImpuestoInterno());
                this.impresoraService.imprimirLineaTicket(factDet.getProducto().getDescripcionConCodigo()
                        ,factDet.getCantidad(),factDet.getPrecioUnitario()
                        ,factDet.getPorcentajeIva(),false,coeficienteK);
            }
            this.impresoraService.cerrarNotaCredito(facturaOrigenCredito);
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURAPROPRODUCTO);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }*/
    }    
    
    private void guardarNotaDCPrimeraVez(){
        log.info("Guardando NotaDC por primera vez");
        try{
            Factura factura = new Factura();
            factura.setEstado(FacturaEstadoEnum.ABIERTA);
            factura.setFacturaOrigen(facturaOrigenCredito);
            factura.setClaseComprobante(facturaOrigenCredito.getClaseComprobante());
            factura.setTipoComprobante(TipoComprobanteEnum.C);
            factura.setNumeroComprobante(this.numeroComprobante);
            
            factura.setCliente(facturaOrigenCredito.getCliente());
            factura.setAperturaCierreCajeroDetalle(Context.getInstance()
                    .currentDMTicket().getAperturaCierreCajDetalle());
            factura.setCheckout(Context.getInstance().currentDMTicket().getCheckout());
            factura.setCaja(Context.getInstance().currentDMTicket()
                    .getCaja());
            factura.setCondicionIva(facturaOrigenCredito.getCondicionIva());
            
            if(facturaOrigenCredito.getCliente()!=null){
                factura.setCondicionIva(facturaOrigenCredito.getCliente().getCondicionIva());
                if(factura.getCondicionIva().getId()==2)
                    factura.setClaseComprobante("A");
                else
                    factura.setClaseComprobante("B");
            }            
            
            factura.setUsuario(Context.getInstance().currentDMTicket().getUsuario());
            
            factura.setPrefijoFiscal(Context.getInstance().currentDMTicket().getPuntoVenta());
            
            
            Factura notaDC = factService.registrarFactura(factura);
            Context.getInstance().currentDMTicket().setIdDocumento(notaDC.getId());
            
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURAPROPRODUCTO);
            tabPaneController.gotoError();
        }
        
    }    
    
    private void agregarDetalleFactura(){
        log.info("Agregando detalle factura");
        FacturaDetalle facturaDetalle = new FacturaDetalle();
        facturaDetalle.setCantidad(lineaTicketData.getCantidad().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setDescuentoCliente(lineaTicketData.getDescuentoCliente().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setExento(lineaTicketData.getExento().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setImpuestoInterno(lineaTicketData.getImpuestoInterno().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setIva(lineaTicketData.getIva().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setIvaReducido(lineaTicketData.getIvaReducido().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setNeto(lineaTicketData.getNeto().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setNetoReducido(lineaTicketData.getNetoReducido().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setPrecioUnitario(lineaTicketData.getPrecioUnitario().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setPrecioUnitarioBase(lineaTicketData.getPrecioUnitarioBase().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setCosto(lineaTicketData.getCosto().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setSubTotal(lineaTicketData.getSubTotal().multiply(BigDecimal.valueOf(-1)));
        facturaDetalle.setPorcentajeIva(lineaTicketData.getPorcentajeIva());
        Producto producto;
        producto = productoService.getProductoPorId(lineaTicketData.getId());
        facturaDetalle.setUsuarioSupervisor(Context.getInstance().currentDMTicket().getUsuarioSupervisor());
        facturaDetalle.setProducto(producto);
        try{
            factService.agregarDetalleFactura(
                    Context.getInstance().currentDMTicket().getIdDocumento()
                    ,facturaDetalle);
            tableViewTickets.getItems().add(this.lineaTicketData);
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURAPROPRODUCTO);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }
        
    }
    
    private void asignarEvento(){
        this.fiscalPrinterEvent = new FiscalPrinterEvent(){
            @Override
            public void commandExecuted(FiscalPrinter source, FiscalPacket command
                ,FiscalPacket response){
                
                if(command.getCommandCode()==HasarCommands.CMD_OPEN_DNFH){
                    numeroComprobante = new Long(response.getString(3));
                    guardarNotaDCPrimeraVez();
                }
                if(command.getCommandCode()==HasarCommands.CMD_GET_DATE_TIME){
                    fechaHoraFiscal = response.getString(3)+" "+response.getString(4);
                }                 
                
                if(command.getCommandCode()==HasarCommands.CMD_PRINT_LINE_ITEM){
                    agregarDetalleFactura();
                    calcularTotalGeneral();
                }
                
                
                if(command.getCommandCode()==HasarCommands.CMD_CLOSE_DNFH){
                    try{
                        factService.modificarNroCreditoTotalizarYCerrar(
                                    Context.getInstance().currentDMTicket().getPuntoVenta()
                                    ,Long.parseLong(response.getString(3))
                                    ,Context.getInstance().currentDMTicket().getIdDocumento()
                                    ,fechaHoraFiscal
                                );
                        Context.getInstance().currentDMTicket().setIdDocumento(null);
                        tabPaneController.gotoNotasDCMenu();
                    }catch(TpvException e){
                        Context.getInstance().currentDMTicket().setException(e);
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURAPROPRODUCTO);
                        tabPaneController.gotoError();
                    }
                }
            }
            
            @Override
            public void printEnded(FiscalPrinter source, FiscalMessages msgs){
                
            }
        };
        impresoraService.getHfp().setEventListener(fiscalPrinterEvent);
    }

    private FacturaDetalle validarDetalleProducto(String codProdCodBarra,BigDecimal cantidad){
        BigDecimal cantAcreditada = BigDecimal.ZERO;
        BigDecimal cantDisponible = BigDecimal.ZERO;
        FacturaDetalle detResult = null;
        for (Iterator<LineaTicketData> it = tableViewTickets.getItems().iterator();it.hasNext();){
            LineaTicketData det = it.next();
            if(det.getCodigoProducto()==Integer.parseInt(codProdCodBarra)
                    || det.getCodBarra().compareTo(codProdCodBarra)==0)
                cantAcreditada = cantAcreditada.add(det.getCantidad());
        }
        
        //sumo lo que tengo disponible en la factura origen
        List<FacturaDetalle> detalle = this.facturaOrigenCredito.getDetalle();
        for(Iterator<FacturaDetalle> it = detalle.iterator();it.hasNext();){
            FacturaDetalle det = it.next();
            if(det.getProducto().getCodigoProducto()==Integer.parseInt(codProdCodBarra)
                    || det.getProducto().getCodBarra().compareTo(codProdCodBarra)==0){
                cantDisponible = cantDisponible.add(det.getCantidad());
                if(det.getCantidad().compareTo(BigDecimal.ZERO)>0)
                    detResult = det;
            }
        }
        
        //resto lo que ya se acreditó en alguna nota de crédito anterior
        for(Iterator<Factura> it = facturaOrigenCredito.getDetalleNotasDC().iterator();it.hasNext();){
            Factura fact = it.next();
            if(fact.getEstado()==FacturaEstadoEnum.CERRADA)
                for(Iterator<FacturaDetalle> itDet = fact.getDetalle().iterator();itDet.hasNext();){
                    FacturaDetalle det = itDet.next();
                    if(det.getProducto().getCodigoProducto()==Integer.parseInt(codProdCodBarra)
                        || det.getProducto().getCodBarra().compareTo(codProdCodBarra)==0){
                        cantDisponible = cantDisponible.add(det.getCantidad()); //SE SUMA PORQUE LAS NOTASDC SON NEGATIGAS
                    }
                }
            
        }
            
        
        cantAcreditada = cantAcreditada.add(cantidad);
        if(cantAcreditada.compareTo(cantDisponible)>0)
            return null;
        return detResult;
    }
    
    
}
