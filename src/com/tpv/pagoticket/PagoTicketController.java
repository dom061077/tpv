/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FormaPago;
import com.tpv.modelo.InteresTarjeta;
import com.tpv.service.FacturacionService;
import com.tpv.service.PagoService;
import com.tpv.util.ui.MaskTextField;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;
import com.tpv.principal.Context;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;

/**
 *|
 * @author daniel
 */
public class PagoTicketController implements Initializable {
    Logger log = Logger.getLogger(PagoTicketController.class);
    
    private MaskTextField textFieldTipoPago;
    private MaskTextField textFieldMonto;
    private MaskTextField textFieldCantidadCuotas;
    private MaskTextField textFieldNroCupon;
    private MaskTextField textFieldNroTarjeta;
    private boolean tieneCantidadCuotas;
    private boolean tieneCuponPago;
    private FormaPago formaPago;
    private TabPanePrincipalController tabPaneController;
    private ListProperty<LineaInteresTarjetaData> intBonifData;
    private ListProperty<LineaFormaPagoData> formaPagoData;
    
    PagoService pagoService = new PagoService();
    FacturacionService factService = new FacturacionService();
    
    @FXML
    private Label labelFormaPagoDescripcion;
    
    @FXML
    private Label labelNroTarjeta;
    
    @FXML
    private Label labelNroCuponTarjeta;
    
    @FXML
    private Label labelSaldoAPagar;
    
    
    @FXML
    private Label totalGral;
    
    @FXML
    private Label totalVenta;
    
    @FXML
    private Label saldoPagar;
    
    @FXML
    private Label bonificaciones;
    
    @FXML
    private Label bonificacionPorPagoTotal;
    
    @FXML
    private Label interesPorPagoTotal;
            
    @FXML
    private TableView tableViewPagos;
    
    @FXML
    private TableView tableViewIntTarjeta;
    
    @FXML
    private TableView tableViewFormaPago;        
                      
    
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
    private TableColumn cuotasColumn;
    
    @FXML
    private TableColumn porcentajeColumn;
    
    @FXML
    private TableColumn descripcionColumn;
    
    @FXML
    private TableColumn totalPagoColumn;
    
    
    @FXML
    private GridPane gridPanePagos;
    
    @FXML
    private Label labelCantidadCuotas;
    
    @FXML
    private Label tituloFormaPagoLabel;
    
    @FXML
    private StackPane stackPaneIntereses;
    
    @FXML
    private StackPane stackPaneFormaPago;
    
    @FXML
    private TableColumn formaPagoCodigoColumn;
    
    @FXML
    private TableColumn formaPagoDescColumn;
    
    
    
    
    public void configurarInicio() throws TpvException{
        
            //Factura factura = factService.calcularCombos(Context.getInstance().currentDMTicket().getIdFactura());
            Factura factura = factService.getFacturaConTotalesSinPagos(Context.getInstance().currentDMTicket().getIdFactura());
            Context.getInstance().currentDMTicket().setBonificaciones(factura.getBonificacionCombosAux());
            Context.getInstance().currentDMTicket().setTotalIva(factura.getIva());
            Context.getInstance().currentDMTicket().setTotalNeto(factura.getNeto().add(
                    factura.getNetoReducido()
            ));
            Context.getInstance().currentDMTicket()
                    .setTotalImpuestoInterno(factura.getImpuestoInterno());
            
        
        iniciarIngresosVisibles();
        tableViewPagos.getItems().clear();
        tableViewPagos.setItems(Context.getInstance().currentDMTicket().getPagos());
        if(tableViewPagos.getItems().size()>0){
            tableViewPagos.getSelectionModel().selectLast();
            scrollDown();

        }        
        Context.getInstance().currentDMTicket().getPagos();
        //textFieldCantidadCuotas.setDisable(true);
        DecimalFormat df = new DecimalFormat("##,###,##0.00");
        totalVenta.setText(df.format(Context.getInstance().currentDMTicket().getTotalTicket()));
        //saldoPagar.setText(df.format(Context.getInstance().currentDMTicket().getTotalTicket().subtract(Context.getInstance().currentDMTicket().getTotalPagos())));
        textFieldMonto.setText(Context.getInstance().currentDMTicket().getSaldo().toString());
        textFieldCantidadCuotas.setText("");
        textFieldNroCupon.setText("");
        textFieldNroTarjeta.setText("");
        textFieldTipoPago.setText("");
        labelFormaPagoDescripcion.setText("");
        
        
        bonificaciones.setText(df.format(Context.getInstance().currentDMTicket().getBonificaciones()));
        saldoPagar.setText(Context.getInstance().currentDMTicket().getFormatSaldo());        
        bonificacionPorPagoTotal.setText(Context.getInstance().currentDMTicket().getFormatBonificacionPorPagoTotal());
        interesPorPagoTotal.setText(Context.getInstance().currentDMTicket().getFormatInteresPorPagoTotal());
        totalGral.setText(df.format(Context.getInstance().currentDMTicket().getTotalGral()));
    }

    
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        log.info("Ingresando al mètodo init");
        stackPaneIntereses.setVisible(false);
        stackPaneFormaPago.setVisible(false);
        initTableViewInteresesBonif();
        initTableViewFormasPago();
        
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
                    if (!empty) {
                            //String formattedDob = De
                            DecimalFormat df = new DecimalFormat("##,##0.00");
                                    
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
        });
        interesTarjetaColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        bonificacionTarjetaColumn.setCellValueFactory(new PropertyValueFactory("bonificacion"));
        bonificacionTarjetaColumn.setCellFactory(col -> {
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
        bonificacionTarjetaColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        
        nroTarjetaColumn.setCellValueFactory(new PropertyValueFactory("nroTarjeta"));
        codigoCuponColumn.setCellValueFactory(new PropertyValueFactory("codigoCupon"));
                
        
        
        
        
        textFieldTipoPago = new MaskTextField();
        textFieldTipoPago.getStyleClass().add("textfield_sin_border");
        textFieldTipoPago.setMask("N!");
        textFieldMonto = new MaskTextField();
        textFieldMonto.getStyleClass().add("textfield_sin_border");
        textFieldMonto.setMask("N!.N!");
        textFieldMonto.setDisable(true);
        textFieldCantidadCuotas = new MaskTextField();
        textFieldCantidadCuotas.getStyleClass().add("textfield_sin_border");
        textFieldCantidadCuotas.setMask("N!");
        textFieldCantidadCuotas.setMaxDigitos(2);
        textFieldNroCupon = new MaskTextField();
        textFieldNroCupon.getStyleClass().add("textfield_sin_border");
        textFieldNroCupon.setMask("N!");
        textFieldNroCupon.setMaxDigitos(15);
        textFieldNroTarjeta = new MaskTextField();
        textFieldNroTarjeta.setMask("N!");
        textFieldNroTarjeta.setMaxDigitos(15);
        textFieldNroTarjeta.getStyleClass().add("textfield_sin_border");
        
        gridPanePagos.add(textFieldTipoPago,1,1);
        gridPanePagos.add(textFieldMonto,1,2);
        gridPanePagos.add(textFieldCantidadCuotas,1,3);
        gridPanePagos.add(textFieldNroTarjeta,1,4);
        gridPanePagos.add(textFieldNroCupon,1,5);
        
        cantidadCuotaColumn.setCellValueFactory(new PropertyValueFactory("cantidadCuotas"));
        cantidadCuotaColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        codigoCuponColumn.setCellValueFactory(new PropertyValueFactory("codigoCupon"));
        codigoCuponColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        textFieldMonto.setStyle("-fx-alignment: CENTER-RIGHT;");
        textFieldTipoPago.setStyle("-fx-alignment: CENTER-RIGHT;");
        textFieldNroTarjeta.setStyle("-fx-alignment: CENTER-RIGHT;");
        textFieldNroCupon.setStyle("-fx-alignment: CENTER-RIGHT;");
        textFieldCantidadCuotas.setStyle("-fx-alignment: CENTER-RIGHT;");
        textFieldCantidadCuotas.setDisable(true);
        
        if (Context.getInstance().currentDMTicket().getSaldo().compareTo(BigDecimal.valueOf(0))>0)
            textFieldMonto.setText(Context.getInstance().currentDMTicket().getSaldo().toString());
        else
            textFieldMonto.setText("0");
        
        Platform.runLater(() -> {
            
            tableViewFormaPago.setOnKeyPressed(keyEvent->{
                int index;
                if (keyEvent.getCode() == KeyCode.UP){
                    tableViewFormaPago.getSelectionModel().selectPrevious();
                    index = tableViewFormaPago.getSelectionModel().getSelectedIndex();
                    tableViewFormaPago.scrollTo(index);
                }
                if (keyEvent.getCode() == KeyCode.DOWN){
                    tableViewPagos.getSelectionModel().selectNext();
                    index = tableViewFormaPago.getSelectionModel().getSelectedIndex();
                    tableViewFormaPago.scrollTo(index);
                }
                
                if (keyEvent.getCode() == KeyCode.ESCAPE){
                    stackPaneFormaPago.setVisible(false);
                    tabPaneController.repeatFocus(textFieldTipoPago);
                }
                
                if (keyEvent.getCode() == KeyCode.ENTER){
                    stackPaneFormaPago.setVisible(false);
                    textFieldTipoPago.setText(""+((LineaFormaPagoData)tableViewFormaPago.getSelectionModel().getSelectedItem()).getCodigoForma());
                    tabPaneController.repeatFocus(textFieldTipoPago);
                }
                if(keyEvent.getCode() == KeyCode.TAB){
                    keyEvent.consume();
                    return;
                }
                        
            });

            tableViewIntTarjeta.setOnKeyPressed(keyEvent->{
                if ( keyEvent.getCode() == KeyCode.UP ){
                    int index;
                    tableViewIntTarjeta.getSelectionModel().selectPrevious();
                    index = tableViewIntTarjeta.getSelectionModel().getSelectedIndex();
                    tableViewIntTarjeta.scrollTo(index);
                }
                if ( keyEvent.getCode() == KeyCode.DOWN ){
                    int index;
                    tableViewIntTarjeta.getSelectionModel().selectNext();
                    index = tableViewIntTarjeta.getSelectionModel().getSelectedIndex();
                    tableViewIntTarjeta.scrollTo(index);
                }
                if ( keyEvent.getCode() == KeyCode.ESCAPE){
                    stackPaneIntereses.setVisible(false);
                    tabPaneController.repeatFocus(textFieldMonto);
                }
                if (keyEvent.getCode() == KeyCode.ENTER){
                    stackPaneIntereses.setVisible(false);
                    textFieldCantidadCuotas.setText( ""+((LineaInteresTarjetaData)tableViewIntTarjeta.getSelectionModel().getSelectedItem()).getCuotas() );
                    textFieldNroTarjeta.setDisable(false);
                    tabPaneController.repeatFocus(textFieldNroTarjeta);
                    
                }
                
                keyEvent.consume();
                return;                
                
            });
            
            textFieldTipoPago.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.SUBTRACT){
                    eliminarLineaPago();
                    refrescarTextFieldSaldo();
                }
                
                if(keyEvent.getCode() == KeyCode.F3){
                    try{
                        cargarDatosTableViewFormaPago();
                    }catch(TpvException e){
                        log.error("Error: "+e.getMessage());
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_PAGOTICKET);
                        Context.getInstance().currentDMTicket().setException(e);
                        tabPaneController.gotoError();//goToError.fire();
                    }
                    stackPaneFormaPago.setVisible(true);
                    tableViewFormaPago.getSelectionModel().select(0);
                    tabPaneController.repeatFocus(tableViewFormaPago);
                }
                    
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(Context.getInstance().currentDMTicket().getSaldo().compareTo(BigDecimal.valueOf(Double.parseDouble("0")))<=0){
                        tabPaneController.gotoConfirmarPago();//confirmarButton.fire();
                        keyEvent.consume();
                        return;
                    }
                    int codigoPago=0;
                    try{
                        codigoPago=Integer.parseInt(textFieldTipoPago.getText());
                    }catch(NumberFormatException e){
                       keyEvent.consume();
                       return;
                    }
                    buscarDescTipoPago(codigoPago);
                    if(labelFormaPagoDescripcion.getText().length()!=0){
                        textFieldMonto.setDisable(false);
                        textFieldMonto.requestFocus();

                    }
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    tabPaneController.gotoFacturacion();//volverButton.fire();
                }
                
                
                int index;
                if(keyEvent.getCode() == KeyCode.DOWN){
                    tableViewPagos.getSelectionModel().selectNext();
                    index = tableViewPagos.getSelectionModel().getSelectedIndex();
                    tableViewPagos.scrollTo(index);
                }
                
                if(keyEvent.getCode() == KeyCode.UP){
                    tableViewPagos.getSelectionModel().selectPrevious();
                    index = tableViewPagos.getSelectionModel().getSelectedIndex();
                    tableViewPagos.scrollTo(index);
                }
                    
            });
            textFieldMonto.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldMonto.setDisable(true);
                    if(textFieldCantidadCuotas.isVisible())
                        textFieldCantidadCuotas.setDisable(true);
                    textFieldTipoPago.requestFocus();
                }
                
                
                if(textFieldMonto.getText().trim().equals("") || textFieldMonto.getText().equals("0")){
                    keyEvent.consume();
                    return;
                }
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(Context.getInstance().currentDMTicket().getSaldo().compareTo(new BigDecimal(textFieldMonto.getText()))<0
                         && !formaPago.isTieneVuelto()){
                        keyEvent.consume();
                        return;
                    }
                    if (formaPago.getInteresesTarjeta().size()>0){
                        cargarDatosTableViewInteresesTarjeta();
                        stackPaneIntereses.setVisible(true);
                        tableViewIntTarjeta.getSelectionModel().select(0);
                        tabPaneController.repeatFocus(tableViewIntTarjeta);
                    }else{
                        if(textFieldCantidadCuotas.isVisible()){
                            textFieldCantidadCuotas.setDisable(false);
                            textFieldCantidadCuotas.requestFocus();
                        }else{
                            agregarLineaPago();
                            refrescarTextFieldSaldo();
                            scrollDown();
                        }
                    }
                }
                keyEvent.consume();                
            });
            /*textFieldCantidadCuotas.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldCantidadCuotas.setText("0");
                    textFieldCantidadCuotas.setDisable(true);
                    textFieldMonto.requestFocus();
                }
                
                if(textFieldCantidadCuotas.getText().trim().equals("") || 
                        textFieldCantidadCuotas.getText().equals("0")){
                    keyEvent.consume();
                    return;
                }
                
                if(keyEvent.getCode() == KeyCode.ENTER){
                    textFieldNroTarjeta.setDisable(false);
                    textFieldNroTarjeta.requestFocus();
                    keyEvent.consume();
                    return;
                }
            });*/
            
            textFieldNroTarjeta.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldNroTarjeta.setDisable(true);
                    textFieldMonto.requestFocus();
                }
                
                if(textFieldNroTarjeta.getText().trim().equals("") || textFieldNroTarjeta.getText().equals("0")){
                }
                
                if(keyEvent.getCode() == KeyCode.ENTER){
                    textFieldNroCupon.setDisable(false);
                    textFieldNroCupon.requestFocus();
                }
                keyEvent.consume();
            });
            
            textFieldNroCupon.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldNroCupon.setDisable(true);
                    textFieldNroTarjeta.requestFocus();
                }
                if(textFieldNroCupon.getText().trim().equals("") || textFieldNroCupon.getText().equals("0")){
                    keyEvent.consume();
                    return;
                }
                

                if(keyEvent.getCode() == KeyCode.ENTER){
                        agregarLineaPago();
                        refrescarTextFieldSaldo();
                        scrollDown();
                }
                keyEvent.consume();
                
            });
                
        });            
    }
    
    private void iniciarIngresosVisibles(){
        labelCantidadCuotas.setVisible(false);
        textFieldCantidadCuotas.setVisible(false);
        textFieldNroCupon.setVisible(false);
        textFieldNroTarjeta.setVisible(false);
        labelNroCuponTarjeta.setVisible(false);
        labelNroTarjeta.setVisible(false);
    }
    
    private void buscarDescTipoPago(int codigoPago){
        try{
            formaPago = pagoService.getFormaPago(codigoPago);
        }catch(TpvException e){
            log.error("Error: "+e.getMessage());
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_PAGOTICKET);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();//goToError.fire();
        }            
        if(formaPago!= null){
            labelFormaPagoDescripcion.setText(formaPago.getDetalle());
            if  (formaPago.isTieneCupon()){
                labelNroCuponTarjeta.setVisible(true);
                textFieldNroCupon.setVisible(true);
                
                labelNroTarjeta.setVisible(true);
                textFieldNroTarjeta.setVisible(true);
                
                textFieldNroCupon.setDisable(true);
                textFieldNroTarjeta.setDisable(true);
            }else{
                textFieldNroCupon.setVisible(false);
                labelNroCuponTarjeta.setVisible(false);
                textFieldNroTarjeta.setVisible(false);
                labelNroTarjeta.setVisible(false);
            }
            if(formaPago.getInteresesTarjeta().size()>0){
                labelCantidadCuotas.setVisible(true);
                textFieldCantidadCuotas.setVisible(true);
            }else{
                labelCantidadCuotas.setVisible(false);
                textFieldCantidadCuotas.setVisible(false);
            }
                
            
        }else
            labelFormaPagoDescripcion.setText("");
        
    }
    
   
    private void agregarLineaPago(){
        //BigDecimal pagoParcial = new BigDecimal(textFieldMonto.getText());
        //pagoParcial = pagoParcial.add(Context.getInstance().currentDMTicket().getTotalPagos());
        //if(pagoParcial.compareTo(Context.getInstance().currentDMTicket().getTotalTicket())==1)
        //    return;
            
//(int codigoPago,String descripcion,BigDecimal monto
            //,int cantidadCuotas, int codigoCupon)        
        int codigoPago = 0;int cantidadCuotas=0;long codigoCupon=0;
        long nroTarjeta = 0;
        BigDecimal monto = new BigDecimal(0);
        codigoPago = Integer.parseInt(textFieldTipoPago.getText());
        monto = new BigDecimal(textFieldMonto.getText());
        BigDecimal netoBonifTarjeta = BigDecimal.ZERO;
        BigDecimal ivaBonifTarjeta = BigDecimal.ZERO;
        BigDecimal netoInteresTarjeta = BigDecimal.ZERO;
        BigDecimal ivaInteresTarjeta = BigDecimal.ZERO;
        
        
        try{
            cantidadCuotas = Integer.parseInt(textFieldCantidadCuotas.getText());
        }catch(Exception e){
            cantidadCuotas = 0;
        }
        try{
            codigoCupon = Long.parseLong(textFieldNroCupon.getText());
        }catch(Exception e){
            codigoCupon = 0;
        }
        try{
            nroTarjeta = Long.parseLong(textFieldNroTarjeta.getText());
        }catch(Exception e){
            nroTarjeta = 0;
        }
        
        BigDecimal auxTarjeta = BigDecimal
                    .valueOf(1 + Context.getInstance().getPorcentajeIvaBonifTarjeta().doubleValue()/100);
        netoBonifTarjeta = formaPago
                    .getBonificacionEnFormaPago(cantidadCuotas, monto)
                    .divide(auxTarjeta,RoundingMode.HALF_EVEN);
        ivaBonifTarjeta = formaPago
                    .getBonificacionEnFormaPago(cantidadCuotas, monto)
                    .subtract(netoBonifTarjeta);
        auxTarjeta = BigDecimal
                    .valueOf(1 + Context.getInstance().getPorcentajeIvaIntTarjeta().doubleValue()/100);
        netoInteresTarjeta = formaPago
                    .getInteresEnFormaPago(cantidadCuotas, monto)
                    .divide(auxTarjeta,RoundingMode.HALF_EVEN);
        ivaInteresTarjeta = formaPago
                    .getInteresEnFormaPago(cantidadCuotas, monto)
                    .subtract(auxTarjeta);
        
                
        Context.getInstance().currentDMTicket().getPagos().add(new LineaPagoData(
            codigoPago,labelFormaPagoDescripcion.getText(),monto
            ,cantidadCuotas,nroTarjeta,codigoCupon
            ,formaPago.getInteresEnFormaPago(cantidadCuotas,monto)
            ,formaPago.getBonificacionEnFormaPago(cantidadCuotas,monto)
            ,ivaInteresTarjeta
            ,ivaBonifTarjeta
        ));
        //BigDecimal saldoParcial = Context.getInstance().currentDMTicket().getTotalTicket().subtract(Context.getInstance().currentDMTicket().getTotalPagos());
        if (Context.getInstance().currentDMTicket().getSaldo().compareTo(BigDecimal.valueOf(0))>0)
            textFieldMonto.setText(Context.getInstance().currentDMTicket().getSaldo().toString());
        else
            textFieldMonto.setText("0");        
        
        textFieldTipoPago.setText("");
        labelFormaPagoDescripcion.setText("");
        textFieldTipoPago.requestFocus();
        textFieldMonto.setDisable(true);
        textFieldCantidadCuotas.setVisible(false);
        textFieldNroCupon.setVisible(false);
        textFieldNroTarjeta.setVisible(false);
        tableViewPagos.getSelectionModel().selectLast();
        saldoPagar.setText(null);
        
        labelCantidadCuotas.setVisible(false);
        labelNroCuponTarjeta.setVisible(false);
        labelNroTarjeta.setVisible(false);
    }
 
    private void eliminarLineaPago(){
       int index = tableViewPagos.getSelectionModel().getSelectedIndex();
       if(index>=0){
            LineaPagoData lineaPagoData = (LineaPagoData)tableViewPagos.getItems().get(index);
            Context.getInstance().currentDMTicket().getPagos().remove(lineaPagoData);
       }
    }
    
    private void initTableViewInteresesBonif(){
        cuotasColumn.setCellValueFactory(new PropertyValueFactory<LineaPagoData,Integer>("cuotas"));
        cuotasColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<LineaPagoData,Integer>("descripcion"));
        descripcionColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        porcentajeColumn.setCellValueFactory(new PropertyValueFactory<LineaPagoData,Integer>("porcentaje"));
        porcentajeColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        totalPagoColumn.setCellValueFactory(new PropertyValueFactory<LineaPagoData,Integer>("totalPago"));
        totalPagoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        //inicializo el data del tableview de intereses y bonificaciones
        ObservableList<LineaInteresTarjetaData> innerList = FXCollections.observableArrayList();
        intBonifData = new SimpleListProperty<>(innerList);
        tableViewIntTarjeta.setItems(intBonifData);
    }
    
    private void initTableViewFormasPago(){
        formaPagoCodigoColumn.setCellValueFactory(new PropertyValueFactory<LineaFormaPagoData,Integer>("codigoForma"));
        formaPagoCodigoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        formaPagoDescColumn.setCellValueFactory(new PropertyValueFactory<LineaFormaPagoData,Integer>("descripcionForma"));
        formaPagoDescColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        ObservableList<LineaFormaPagoData> innerList = FXCollections.observableArrayList();
        formaPagoData = new SimpleListProperty<>(innerList);
        tableViewIntTarjeta.setItems(intBonifData);        
                
    }
            
    
    private void cargarDatosTableViewInteresesTarjeta(){

        BigDecimal totalPago = new BigDecimal(textFieldMonto.getText());
        tableViewIntTarjeta.getItems().clear();
        for(Iterator<InteresTarjeta> it=formaPago.getInteresesTarjeta().iterator();it.hasNext();){
            InteresTarjeta intTarj = it.next();
            tableViewIntTarjeta.getItems().add(new LineaInteresTarjetaData(
                    intTarj.getCuota(),intTarj.getTipo().toString()
                    ,intTarj.getPorcentaje(),totalPago.add(intTarj.getMonto(totalPago)) )
            );
        }
    }
            
    private void cargarDatosTableViewFormaPago() throws TpvException{

        tableViewFormaPago.getItems().clear();
        List<FormaPago> list = pagoService.getFormasPago();
        for(Iterator<FormaPago> it=list.iterator();it.hasNext();){
            FormaPago formaPago = it.next();
            tableViewFormaPago.getItems().add(new LineaFormaPagoData(
                        formaPago.getId(),formaPago.getDetalle()
                    )
            );
        }
    }
    
    
    private void scrollDown(){
            if(tableViewPagos.getItems().size()>0){
                tableViewPagos.getSelectionModel().select(tableViewPagos.getItems().size()-1);
                tableViewPagos.scrollTo(tableViewPagos.getItems().size()-1);
            }
    }    
    
    private void refrescarTextFieldSaldo(){
        if(Context.getInstance().currentDMTicket().getSaldo().compareTo(BigDecimal.ZERO)<0){
            labelSaldoAPagar.setText("Cambio del Cliente:");
        }else{
            textFieldMonto.setText(Context.getInstance().currentDMTicket().getSaldo().toString());
            labelSaldoAPagar.setText("Saldo a Pagar:");
        }
        saldoPagar.setText(Context.getInstance().currentDMTicket().getFormatSaldo());
        
        
        
        bonificacionPorPagoTotal.setText(Context.getInstance().currentDMTicket().getFormatBonificacionPorPagoTotal());
        interesPorPagoTotal.setText(Context.getInstance().currentDMTicket().getFormatInteresPorPagoTotal());
        totalGral.setText(Context.getInstance().currentDMTicket().getFormatTotalGral());
        
    }
    
    public void setTabController(TabPanePrincipalController tabPaneController){
        this.tabPaneController=tabPaneController;
    }
    
}