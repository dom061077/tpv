/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.FormaPago;
import com.tpv.principal.DataModelTicket;
import com.tpv.principal.LineaTicketData;
import com.tpv.service.FacturacionService;
import com.tpv.service.PagoService;
import com.tpv.util.ui.MaskTextField;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;

/**
 *|
 * @author daniel
 */
@FXMLController(value="PagoTicket.fxml", title = "pago ticket")
public class PagoTicketController {
    Logger log = Logger.getLogger(PagoTicketController.class);
    
    private MaskTextField textFieldTipoPago;
    private MaskTextField textFieldMonto;
    private MaskTextField textFieldCantidadCuotas;
    private boolean tieneCuotas=false;
    private boolean tieneCuponPago = false;
    private DataModelTicket modelTicket;
    
    PagoService pagoService = new PagoService();
    
    @FXML
    private Label labelFormaPagoDescripcion;
    
    
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
    
    
    @FXMLViewFlowContext
    private ViewFlowContext context;    
    
    
    @FXML
    private GridPane gridPanePagos;
    
    @FXML
    private Label labelCantidadCuotas;
    
    @FXML
    @ActionTrigger("volverFacturacion")
    private Button volverButton;
    
    @FXML
    @ActionTrigger("confirmarTicket")
    private Button confirmarButton;
    
    
    
    @PostConstruct
    public void init(){
        iniciarIngresosVisibles();
        modelTicket = context.getRegisteredObject(DataModelTicket.class);
        modelTicket.getPagos();
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
        
        cantidadCuotaColumn.setCellValueFactory(new PropertyValueFactory("cantidadCuotas"));
        cantidadCuotaColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        codigoCuponColumn.setCellValueFactory(new PropertyValueFactory("codigoCupon"));
        codigoCuponColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        
        textFieldMonto.setText(modelTicket.getTotalTicket().toString());
        
        Platform.runLater(() -> {
            tableViewPagos.setItems(modelTicket.getPagos());
            textFieldTipoPago.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.SUBTRACT){
                    eliminarLineaPago();
                    refrescarTextFieldSaldo();
                    keyEvent.consume();;
                    return;
                }
                    
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(modelTicket.getsaldo().compareTo(BigDecimal.valueOf(Double.parseDouble("0")))==0){
                        confirmarButton.fire();
                        keyEvent.consume();
                        return;
                    }
                    try{
                        buscarDescTipoPago(Integer.parseInt(textFieldTipoPago.getText()));
                        if(labelFormaPagoDescripcion.getText().length()!=0){
                            textFieldMonto.setDisable(false);
                            textFieldMonto.requestFocus();
                            
                        }
                    }catch(Exception e){
                        
                    }
                    keyEvent.consume();
                    return;
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    volverButton.fire();
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
                if(keyEvent.getCode() == KeyCode.ENTER){
                    agregarLineaPago();
                    refrescarTextFieldSaldo();
                    scrollDown();
                    keyEvent.consume();
                    return;
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    textFieldMonto.setDisable(true);
                    textFieldTipoPago.requestFocus();
                    keyEvent.consume();
                    return;
                }
            });
                
        });            
    }
    
    private void iniciarIngresosVisibles(){
        textFieldTipoPago = new MaskTextField();
        textFieldTipoPago.setMask("N!");
        textFieldMonto = new MaskTextField();
        textFieldMonto.setMask("N!.N!");
        textFieldCantidadCuotas = new MaskTextField();
        textFieldCantidadCuotas.setMask("N!");
        
        gridPanePagos.add(textFieldTipoPago,2,1);
        gridPanePagos.add(textFieldMonto,2,2);
        gridPanePagos.add(textFieldCantidadCuotas,2,3);
        
        labelCantidadCuotas.setVisible(false);
        textFieldCantidadCuotas.setVisible(false);
    }
    
    private void buscarDescTipoPago(int codigoPago){
        FormaPago formaPago = pagoService.getFormaPago(codigoPago);
        if(formaPago!= null){
            labelFormaPagoDescripcion.setText(formaPago.getDetalle());
            if  (formaPago.getMaxiCuotas()>0)
                tieneCuotas = true;
        }else
            labelFormaPagoDescripcion.setText("");
        
    }
    
    private void agregarLineaPago(){
        BigDecimal pagoParcial = new BigDecimal(textFieldMonto.getText());
        pagoParcial = pagoParcial.add(modelTicket.getTotalPagos());
        if(pagoParcial.compareTo(modelTicket.getTotalTicket())==1)
            return;
            
        int codigoPago = 0;int cantidadCuotas=0;int codigoCupon=0;
        BigDecimal monto = new BigDecimal(0);
//(int codigoPago,String descripcion,BigDecimal monto
            //,int cantidadCuotas, int codigoCupon)        
        codigoPago = Integer.parseInt(textFieldTipoPago.getText());
        monto = new BigDecimal(textFieldMonto.getText());
        
        modelTicket.getPagos().add(new LineaPagoData(
            codigoPago,labelFormaPagoDescripcion.getText(),monto
            ,cantidadCuotas,codigoCupon));
        //BigDecimal saldoParcial = modelTicket.getTotalTicket().subtract(modelTicket.getTotalPagos());
        textFieldMonto.setText(modelTicket.getsaldo().toString());
        textFieldTipoPago.setText("");
        labelFormaPagoDescripcion.setText("");
        textFieldTipoPago.requestFocus();
        textFieldMonto.setDisable(true);
        
    }
 
    private void eliminarLineaPago(){
       int index = tableViewPagos.getSelectionModel().getSelectedIndex();
       LineaPagoData lineaPagoData = (LineaPagoData)tableViewPagos.getItems().get(index);
       modelTicket.getPagos().remove(lineaPagoData);
    }

    private void scrollDown(){
            if(tableViewPagos.getItems().size()>0){
                tableViewPagos.getSelectionModel().select(tableViewPagos.getItems().size()-1);
                tableViewPagos.scrollTo(tableViewPagos.getItems().size()-1);
            }
    }    
    
    private void refrescarTextFieldSaldo(){
        textFieldMonto.setText(modelTicket.getsaldo().toString());
    }
    
}