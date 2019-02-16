/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.notasdc;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.MotivoNotaDC;
import com.tpv.modelo.enums.FacturaEstadoEnum;
import com.tpv.modelo.enums.TipoComprobanteEnum;
import com.tpv.principal.Context;
import com.tpv.principal.LineaTicketData;
import com.tpv.print.event.FiscalPrinterEvent;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
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
import org.tpv.print.fiscal.msg.FiscalMessage;
import org.tpv.print.fiscal.msg.FiscalMessages;

/**
 *
 * @author COMPUTOS
 */
public class NotasCreditoFacturaController implements Initializable {
    Logger log = Logger.getLogger(NotasCreditoFacturaController.class);
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
    
    
    
    
    MaskTextField textFieldPrefijo;
    MaskTextField textFieldNumero;
    
    
    private Factura facturaOrigenCredito;
    private FacturacionService factService = new FacturacionService();
    private int idMotivo;
    private ImpresoraService impresoraService = new ImpresoraService();
    private FiscalPrinterEvent fiscalPrinterEvent;
    private Long idNotaDCGenerada;
    
    
    
    public void setTabController(TabPanePrincipalController tabPaneController){
        this.tabPaneController=tabPaneController;
    }
    
    public void configurarInicio() throws TpvException{
        stackPaneMotivos.setVisible(false);
        tabPaneController.repeatFocus(textFieldPrefijo);
        limpiarLabelsDatoYTableViewTickets();
        if(impresoraService.getHfp().getEventListener()==null)
            asignarEvento();
        initTableViewMotivos();
        cargarTableViewMotivos();
        initTableViewTickets();
        
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
        
        Platform.runLater(()->{
            tableViewTickets.setOnKeyPressed(keyEvent ->{
                
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
                                , "¿Confirma el cierre de la nota de crédito?", "", null){
                                    @Override
                                    public void aceptarMensaje(){
                                        confirmarCredito();
                                    }
                                    
                                    @Override
                                    public void cancelarMensaje(){
                                        tabPaneController.repeatFocus(tableViewTickets);
                                    }
                               });                    
                    
                    
                }
            });
            
            tableViewMotivos.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    stackPaneMotivos.setVisible(false);
                    labelMotivoData
                       .setText(((LineaMotivoData)tableViewMotivos.getSelectionModel().getSelectedItem()).getDescripcion());
                    idMotivo = ((LineaMotivoData)tableViewMotivos.getSelectionModel().getSelectedItem()).getCodigoMotivo();
                    tabPaneController.repeatFocus(tableViewTickets);
                    if(tableViewTickets.getItems().size()>0)
                        tableViewTickets.getSelectionModel().select(0);
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
                        this.traerTicket();
                        
                    }
                    keyEvent.consume();
                }
            });

        });
        
        
    }
    
    private void cargarTableViewTickets(){
        tableViewTickets.getItems().clear();
        for(Iterator iterator = facturaOrigenCredito.getDetalleOrdenadoPorId().iterator();iterator.hasNext();){
                FacturaDetalle fd = (FacturaDetalle)iterator.next();


                LineaTicketData lineaTicketData = new LineaTicketData(
                                 fd.getProducto().getIdProducto()
                                ,fd.getProducto().getCodigoProducto()
                                ,fd.getProducto().getCodBarra()
                                ,fd.getProducto().getDescripcion(),fd.getCantidad()
                                ,fd.getPrecioUnitario()
                                ,fd.getPrecioUnitarioBase()
                                ,fd.getNeto(),fd.getNetoReducido(),fd.getExento()
                                ,fd.getDescuentoCliente()
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
    }
    
    private void traerTicket(){
        try{
            this.facturaOrigenCredito = factService.getFactura(
                 Long.parseLong(textFieldPrefijo.getText().trim())
                , Long.parseLong(textFieldNumero.getText().trim()));
            if(this.facturaOrigenCredito==null  
                    && this.facturaOrigenCredito.getEstado()==FacturaEstadoEnum.CERRADA){
                tabPaneController.showMsgModal(new MensajeModalAceptar(
                        "Mensaje","La factura con el prefijo y Nº ingresado no existe."
                        ,"",textFieldPrefijo
                ));
                return;
            }
            if(facturaOrigenCredito.getTotalNotasDC().compareTo(BigDecimal.ZERO)<0){
                    
                tabPaneController.showMsgModal(new MensajeModalAceptar("Mensaje"
                        , "La factura con el prefijo y Nº ingresado tiene al menos una nota de Crédito."
                            +" No se puede acreditar toda la factura."
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
                cargarTableViewTickets();
                stackPaneMotivos.setVisible(true);
                this.tabPaneController.repeatFocus( tableViewMotivos);
                
                
                
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
    
    private void confirmarCredito(){
        String nombreComprador=" ";
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
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURA);
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
                BigDecimal coeficienteK = ImpresoraService.getCoeficienteK(
                     factDet.getImpuestoInterno().divide(factDet.getCantidad(), 4,RoundingMode.HALF_EVEN)
                     ,factDet.getPrecioUnitarioBase()
                   );
                this.impresoraService.imprimirLineaTicket(factDet.getProducto().getDescripcionConCodigo()
                        ,factDet.getCantidad(),factDet.getPrecioUnitario()
                        ,factDet.getPorcentajeIva(),false,coeficienteK);
            }
            this.impresoraService.cerrarNotaCredito(facturaOrigenCredito);
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURA);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }
    }    
    

    
    private void asignarEvento(){
        this.fiscalPrinterEvent = new FiscalPrinterEvent(){
            @Override
            public void commandExecuted(FiscalPrinter source, FiscalPacket command
                ,FiscalPacket response){
                
                if(command.getCommandCode()==HasarCommands.CMD_CLOSE_DNFH){
                    try{
                        factService.modificarNroCreditoYCerrar(
                                    Context.getInstance().currentDMTicket().getPuntoVenta()
                                    ,Long.parseLong(response.getString(3))
                                    ,idNotaDCGenerada
                                );
                        tabPaneController.gotoNotasDCMenu();
                    }catch(TpvException e){
                        Context.getInstance().currentDMTicket().setException(e);
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOFACTURA);
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
    
}
