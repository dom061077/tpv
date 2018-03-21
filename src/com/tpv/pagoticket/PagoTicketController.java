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
import com.tpv.principal.DataModelTicket;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;
import com.tpv.principal.Context;

/**
 *|
 * @author daniel
 */
@FXMLController(value="PagoTicket.fxml", title = "pago ticket")
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
    
    PagoService pagoService = new PagoService();
    FacturacionService factService = new FacturacionService();
    
    @FXML
    private Label labelFormaPagoDescripcion;
    
    @FXML
    private Label labelNroTarjeta;
    
    @FXML
    private Label labelNroCuponTarjeta;
    
    @FXML
    private Label totalGral;
    
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
    private GridPane gridPanePagos;
    
    @FXML
    private Label labelCantidadCuotas;
    
    
    public void configurarInicio(){
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
        totalGral.setText(df.format(Context.getInstance().currentDMTicket().getTotalTicket()));
        //saldoPagar.setText(df.format(Context.getInstance().currentDMTicket().getTotalTicket().subtract(Context.getInstance().currentDMTicket().getTotalPagos())));
        textFieldMonto.setText(Context.getInstance().currentDMTicket().getSaldo().toString());
        try{
            Factura factura = factService.calcularCombos(Context.getInstance().currentDMTicket().getIdFactura());
            Context.getInstance().currentDMTicket().setBonificaciones(factura.getBonificacionCombosAux());
        }catch(TpvException e)    {
            log.error("Error en capa controller "+e.getMessage());
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_PAGOTICKET);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }
        bonificaciones.setText(df.format(Context.getInstance().currentDMTicket().getBonificaciones()));
        saldoPagar.setText(Context.getInstance().currentDMTicket().getFormatSaldo());        
        bonificacionPorPagoTotal.setText(Context.getInstance().currentDMTicket().getFormatBonificacionPorPagoTotal());
        interesPorPagoTotal.setText(Context.getInstance().currentDMTicket().getFormatInteresPorPagoTotal());
        
    }

    
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        log.info("Ingresando al mètodo init");
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
        
        if (Context.getInstance().currentDMTicket().getSaldo().compareTo(BigDecimal.valueOf(0))>0)
            textFieldMonto.setText(Context.getInstance().currentDMTicket().getSaldo().toString());
        else
            textFieldMonto.setText("0");
        
        Platform.runLater(() -> {

            
            textFieldTipoPago.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.SUBTRACT){
                    eliminarLineaPago();
                    refrescarTextFieldSaldo();
                    keyEvent.consume();;
                    return;
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
                    keyEvent.consume();
                    return;
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    tabPaneController.gotoFacturacion();//volverButton.fire();
                    keyEvent.consume();
                    return;
                }
                
                
                int index;
                if(keyEvent.getCode() == KeyCode.DOWN){
                    tableViewPagos.getSelectionModel().selectNext();
                    index = tableViewPagos.getSelectionModel().getSelectedIndex();
                    tableViewPagos.scrollTo(index);
                    keyEvent.consume();
                    return;
                }
                
                if(keyEvent.getCode() == KeyCode.UP){
                    tableViewPagos.getSelectionModel().selectPrevious();
                    index = tableViewPagos.getSelectionModel().getSelectedIndex();
                    tableViewPagos.scrollTo(index);
                    keyEvent.consume();
                    return;
                }
                    
                    
                    
            });
            textFieldMonto.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldMonto.setDisable(true);
                    if(textFieldCantidadCuotas.isVisible())
                        textFieldCantidadCuotas.setDisable(true);
                    textFieldTipoPago.requestFocus();
                    keyEvent.consume();
                    return;
                }
                
                if(textFieldMonto.getText().trim().equals("") || textFieldMonto.getText().equals("0")){
                    keyEvent.consume();
                    return;
                }
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(textFieldCantidadCuotas.isVisible()){
                        textFieldCantidadCuotas.setDisable(false);
                        textFieldCantidadCuotas.requestFocus();
                    }else{
                        agregarLineaPago();
                        refrescarTextFieldSaldo();
                        scrollDown();
                    }
                    keyEvent.consume();
                    return;
                }
            });
            textFieldCantidadCuotas.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    
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
            });
            
            textFieldNroTarjeta.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldNroTarjeta.setDisable(true);
                    textFieldCantidadCuotas.requestFocus();
                }
                
                if(textFieldNroTarjeta.getText().trim().equals("") || textFieldNroTarjeta.getText().equals("0")){
                    keyEvent.consume();
                    return;
                }
                
                if(keyEvent.getCode() == KeyCode.ENTER){
                    textFieldNroCupon.setDisable(false);
                    textFieldNroCupon.requestFocus();
                    keyEvent.consume();
                    return;
                }
                
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
            if  (formaPago.getMaxiCuotas()>0){
                textFieldCantidadCuotas.setVisible(true);
                labelCantidadCuotas.setVisible(true);
                
                labelNroCuponTarjeta.setVisible(true);
                textFieldNroCupon.setVisible(true);
                
                labelNroTarjeta.setVisible(true);
                textFieldNroTarjeta.setVisible(true);
                
                
                textFieldNroCupon.setDisable(true);
                textFieldCantidadCuotas.setDisable(true);
                textFieldNroTarjeta.setDisable(true);
                
                
            }else{
                textFieldCantidadCuotas.setVisible(false);
                labelCantidadCuotas.setVisible(false);
                textFieldNroCupon.setVisible(false);
                labelNroCuponTarjeta.setVisible(false);
                textFieldNroTarjeta.setVisible(false);
                labelNroTarjeta.setVisible(false);
            }
        }else
            labelFormaPagoDescripcion.setText("");
        
    }
    
    private void agregarLineaPago(){
        BigDecimal pagoParcial = new BigDecimal(textFieldMonto.getText());
        pagoParcial = pagoParcial.add(Context.getInstance().currentDMTicket().getTotalPagos());
        //if(pagoParcial.compareTo(Context.getInstance().currentDMTicket().getTotalTicket())==1)
        //    return;
            
        int codigoPago = 0;int cantidadCuotas=0;long codigoCupon=0;
        long nroTarjeta = 0;
        BigDecimal monto = new BigDecimal(0);
//(int codigoPago,String descripcion,BigDecimal monto
            //,int cantidadCuotas, int codigoCupon)        
        codigoPago = Integer.parseInt(textFieldTipoPago.getText());
        monto = new BigDecimal(textFieldMonto.getText());
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
            
        
        Context.getInstance().currentDMTicket().getPagos().add(new LineaPagoData(
            codigoPago,labelFormaPagoDescripcion.getText(),monto
            ,cantidadCuotas,nroTarjeta,codigoCupon
            ,formaPago.getInteresEnFormaPago(cantidadCuotas)
                    .multiply(monto).divide(BigDecimal.valueOf(100))
            ,formaPago.getBonificacionEnFormaPago(cantidadCuotas)
                    .multiply(monto).divide(BigDecimal.valueOf(100))
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

    private void scrollDown(){
            if(tableViewPagos.getItems().size()>0){
                tableViewPagos.getSelectionModel().select(tableViewPagos.getItems().size()-1);
                tableViewPagos.scrollTo(tableViewPagos.getItems().size()-1);
            }
    }    
    
    private void refrescarTextFieldSaldo(){
        textFieldMonto.setText(Context.getInstance().currentDMTicket().getSaldo().toString());
        saldoPagar.setText(Context.getInstance().currentDMTicket().getFormatSaldo());
        bonificacionPorPagoTotal.setText(Context.getInstance().currentDMTicket().getFormatBonificacionPorPagoTotal());
        interesPorPagoTotal.setText(Context.getInstance().currentDMTicket().getFormatInteresPorPagoTotal());
    }
    
    public void setTabController(TabPanePrincipalController tabPaneController){
        this.tabPaneController=tabPaneController;
    }
    
}