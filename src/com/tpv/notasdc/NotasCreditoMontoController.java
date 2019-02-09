/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.notasdc;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.MotivoNotaDC;
import com.tpv.modelo.enums.FacturaEstadoEnum;
import com.tpv.modelo.enums.TipoComprobanteEnum;
import com.tpv.pagoticket.ConfirmaPagoTicketController;
import com.tpv.principal.Context;
import com.tpv.print.event.FiscalPrinterEvent;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.util.Util;
import com.tpv.util.ui.MaskTextField;
import com.tpv.util.ui.MensajeModal;
import com.tpv.util.ui.MensajeModalAceptar;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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
public class NotasCreditoMontoController implements Initializable {
    Logger log = Logger.getLogger(ConfirmaPagoTicketController.class);
    
    private MaskTextField textFieldPrefijo; 
    private MaskTextField textFieldNumero;
    private MaskTextField textFieldMontoCredito;
    @FXML private Label labelNumeroFactura;
    @FXML private Label labelCliente;
    @FXML private Label labelNumeroFacturaDato;
    @FXML private Label labelCuitClienteDato;
    @FXML private Label labelNombreClienteDato;
    @FXML private Label labelTotalFacturaDato;
    @FXML private Label labelSaldoDCDato;
    @FXML private Label labelMotivoData;
    @FXML private StackPane stackPaneMotivos;
    @FXML private TableColumn motivoDescripcionColumn;
    @FXML private TableColumn motivoCodigoColumn;
    
    @FXML
    private TabPanePrincipalController tabPaneController;
    
    @FXML
    private GridPane gridPaneIngreso;
    @FXML private GridPane gridPaneDatos;
    @FXML private TableView tableViewMotivos;
    
    private boolean esIngresoProducto;
    private FacturacionService factService = new FacturacionService();
    private ImpresoraService impresoraService = new ImpresoraService();
    private Factura facturaOrigenCredito;
    private FiscalPrinterEvent fiscalPrinterEvent;
    private int idMotivo;
    
    //@FXML private Label labelIngreso;
    //@FXML private Label labelCantidad;
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        textFieldPrefijo =  new MaskTextField();
        textFieldPrefijo.setMask("N!");
        //textFieldIngreso.setVisible(false);
        textFieldPrefijo.getStyleClass().add("textfield_sin_border");        
        gridPaneIngreso.add(textFieldPrefijo,2,1);
        
        textFieldNumero = new MaskTextField();
        textFieldNumero.setMask("N!");
        textFieldNumero.getStyleClass().add("textfield_sin_border");
        gridPaneIngreso.add(textFieldNumero, 2, 2);
        
        textFieldMontoCredito = new MaskTextField();
        textFieldMontoCredito.setMask("N!.N!");
        textFieldMontoCredito.getStyleClass().add("textfield_sin_border");
        textFieldMontoCredito.setPrefWidth(100);
        textFieldMontoCredito.setMaxWidth(250);
        gridPaneDatos.add(textFieldMontoCredito,1,5);
        Platform.runLater(()->{
            tableViewMotivos.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    stackPaneMotivos.setVisible(false);
                    labelMotivoData
                       .setText(((LineaMotivoData)tableViewMotivos.getSelectionModel().getSelectedItem()).getDescripcion());
                    idMotivo = ((LineaMotivoData)tableViewMotivos.getSelectionModel().getSelectedItem()).getCodigoMotivo();
                    tabPaneController.repeatFocus(textFieldMontoCredito);
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    stackPaneMotivos.setVisible(false);
                    tabPaneController.repeatFocus(textFieldNumero);
                    limpiarLabelsDato();
                }
                    
                if(keyEvent.getCode() == KeyCode.TAB)
                    keyEvent.consume();
                
            });
            textFieldPrefijo.setOnKeyPressed(keyEvent ->{
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
            
            textFieldMontoCredito.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.TAB)
                    keyEvent.consume();                
                if(keyEvent.getCode() == KeyCode.ENTER){
                        keyEvent.consume();

                        if (textFieldMontoCredito.getText().trim().equals("")){
                            return;
                        }else{
                            try{
                                BigDecimal monto = new BigDecimal(textFieldMontoCredito.getText());
                                if(monto.compareTo(BigDecimal.ZERO)<=0)
                                    return;
                            }catch (Exception e){
                                
                            }
                        }
                        
                        
                        if (facturaOrigenCredito.getSaldoDispNotasDC()
                                .compareTo(new BigDecimal(textFieldMontoCredito.getText()))<0){
                            tabPaneController
                                  .showMsgModal(new MensajeModalAceptar("Error"
                                            ,"El monto del crédito supera el saldo disponible para crédito"
                                              ,"",textFieldMontoCredito));            
                            return;
                        }                        
                    
                        tabPaneController.showMsgModal(new MensajeModal("Confirmación"
                                , "¿Confirma el cierre del ticket?", "", null){
                                    @Override
                                    public void aceptarMensaje(){
                                        confirmarCredito();
                                    }
                                    
                                    @Override
                                    public void cancelarMensaje(){
                                        tabPaneController.repeatFocus(textFieldMontoCredito);
                                    }
                               });                    
                        
                }
                if(keyEvent.getCode() == KeyCode.F11){
                    this.tabPaneController.gotoNotasDCMenu();
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    this.tabPaneController.repeatFocus(textFieldNumero);
                    limpiarLabelsDato();
                    keyEvent.consume();
                }
                
                
            });
            
            
            
        });
    }
    
    private void limpiarLabelsDato(){
            idMotivo=0;
            labelCuitClienteDato.setText("");
            labelNombreClienteDato.setText("");
            labelNumeroFacturaDato.setText("");
            labelSaldoDCDato.setText("");
            labelTotalFacturaDato.setText("");
            labelMotivoData.setText("");
    }
    
    private void initTableViewMotivos(){
        motivoCodigoColumn.setCellValueFactory(new PropertyValueFactory<LineaMotivoData,Integer>("codigo"));
        motivoDescripcionColumn.setCellValueFactory(new PropertyValueFactory<LineaMotivoData,String>("descripcion"));
    }
    
    private void confirmarCredito(){
        String nombreComprador=" ";
        String cuitComprador=" ";
        String domicilioComprador=" ";
        String tipoCreditoImpresion=ImpresoraService.NOTACREDITO_BC;
        String condicionIVA=ImpresoraService.IVA_CONSUMIDOR_FINAL;
        BigDecimal montoCredito = new BigDecimal(textFieldMontoCredito.getText());
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
            this.impresoraService.abrirNotaCredito(
                    tipoCreditoImpresion
                    ,facturaOrigenCredito.getPrefijoFiscal()+"-"+ facturaOrigenCredito.getNumeroComprobante()
                    , nombreComprador, cuitComprador, condicionIVA, domicilioComprador,tipoDocImpresion);
            //imprimirLineaTicket(String descripcion,BigDecimal cantidad
            //,BigDecimal precio, BigDecimal iva,boolean imprimeNegativo,BigDecimal impuestoInterno)            
            
            this.impresoraService.imprimirLineaTicket("CREDITO POR", BigDecimal.ONE
                     ,montoCredito 
                     , BigDecimal.valueOf(21), false, BigDecimal.ZERO);
            this.impresoraService.cerrarNotaCredito();
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOMONTO);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }
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
            
    private void traerTicket(){
        try{
            this.facturaOrigenCredito = factService.getFactura(
                 Long.parseLong(textFieldPrefijo.getText().trim())
                , Long.parseLong(textFieldNumero.getText().trim()));
            if(this.facturaOrigenCredito!=null 
                    && this.facturaOrigenCredito.getEstado()==FacturaEstadoEnum.CERRADA){
                labelNumeroFacturaDato.setText(facturaOrigenCredito.getPrefijoFiscal().toString()
                    +'-'+facturaOrigenCredito.getNumeroComprobante());
                labelTotalFacturaDato.setText(Util.formatearDecimal(facturaOrigenCredito.getTotal()));
                labelSaldoDCDato.setText(Util.formatearDecimal(facturaOrigenCredito.getSaldoDispNotasDC()));
                if(facturaOrigenCredito.getCliente()!=null){
                    labelCuitClienteDato.setText(facturaOrigenCredito.getCliente().getCuit());
                    labelNombreClienteDato.setText(facturaOrigenCredito.getCliente().getRazonSocial());
                    this.ocultarRowCliente(false);
                }else
                    this.ocultarRowCliente(true);
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
    }
    
    private void asignarEvento(){
        this.fiscalPrinterEvent = new FiscalPrinterEvent(){
            @Override
            public void commandExecuted(FiscalPrinter source, FiscalPacket command
                    ,FiscalPacket response){
                if(command.getCommandCode()==HasarCommands.CMD_OPEN_DNFH){
                }
                if(command.getCommandCode()==HasarCommands.CMD_CLOSE_DNFH){
                    try{
/*confirmarNotaDCMonto(TipoComprobanteEnum tipo
            ,Factura facturaOrigen,BigDecimal monto
            ,Long prefijoFiscal,String numeroComprobante
            ,AperturaCierreCajeroDetalle apCiereCajDet
            ,Checkout checkout                        */
                        
                        factService.confirmarNotaDCMonto(TipoComprobanteEnum.C, facturaOrigenCredito
                               , new BigDecimal(textFieldMontoCredito.getText())
                               , Context.getInstance().currentDMTicket().getPuntoVenta()
                               , response.getString(3)
                               , Context.getInstance().currentDMTicket().getAperturaCierreCajDetalle()
                               , Context.getInstance().currentDMTicket().getCheckout()
                               , Context.getInstance().currentDMTicket().getUsuario()
                               ,idMotivo
                               , Context.getInstance().currentDMTicket().getCaja()
                             );
                        tabPaneController.gotoNotasDCMenu();
                    }catch(TpvException e){
                        Context.getInstance().currentDMTicket().setException(e);
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_NOTACREDITOMONTO);
                        tabPaneController.gotoError();
                    }
                }
            }
            
            @Override
            public void printEnded(FiscalPrinter source, FiscalMessages msgs){
                
            }
        };
        impresoraService.getHfp().setEventListener(this.fiscalPrinterEvent);
    }
    
    public void setTabController(TabPanePrincipalController tabPaneController){
        this.tabPaneController=tabPaneController;
    }

    public void configurarInicio() throws TpvException{
        this.tabPaneController.repeatFocus(textFieldPrefijo);
        this.stackPaneMotivos.setVisible(false);
        this.labelCuitClienteDato.setText("");
        this.labelNombreClienteDato.setText("");
        this.labelNumeroFacturaDato.setText("");
        this.labelTotalFacturaDato.setText("");
        this.labelSaldoDCDato.setText("");
        this.ocultarRowCliente(true);
        if(impresoraService.getHfp().getEventListener()==null)
           asignarEvento(); 
        initTableViewMotivos();
        cargarTableViewMotivos();
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
    
}
