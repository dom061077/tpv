/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.notasdc;

import com.tpv.pagoticket.ConfirmaPagoTicketController;
import com.tpv.service.FacturacionService;
import com.tpv.util.ui.MaskTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;

/**
 *
 * @author COMPUTOS
 */
public class NotasCreditoMontoController implements Initializable {
    Logger log = Logger.getLogger(ConfirmaPagoTicketController.class);
    
    private MaskTextField textFieldIngreso; 
    private MaskTextField textFieldCantidad;
    
    @FXML
    private TabPanePrincipalController tabPaneController;
    
    @FXML
    private GridPane gridPaneIngreso;
    
    private boolean esIngresoProducto;
    private FacturacionService factService = new FacturacionService();
    
    //@FXML private Label labelIngreso;
    //@FXML private Label labelCantidad;
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        textFieldIngreso =  new MaskTextField();
        textFieldIngreso.setMask("N!");
        //textFieldIngreso.setVisible(false);
        textFieldIngreso.getStyleClass().add("textfield_sin_border");        
        gridPaneIngreso.add(textFieldIngreso,2,1);
        
        textFieldCantidad = new MaskTextField();
        textFieldCantidad.setMask("N!");
        textFieldCantidad.getStyleClass().add("textfield_sin_border");
        gridPaneIngreso.add(textFieldCantidad, 2, 2);
        Platform.runLater(()->{
            textFieldIngreso.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    
                    keyEvent.consume();
                }
            });
        });
    }
    
    private void traerTicket(){
        //Factura factura = factService.get
    }
    
    public void setTabController(TabPanePrincipalController tabPaneController){
        this.tabPaneController=tabPaneController;
    }
    
    
}
