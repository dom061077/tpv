/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.util.Connection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;

/**
 *
 * @author daniel
 */
@FXMLController(value="MenuPrincipal.fxml", title = "Menu Principal")
public class MenuPrincipalController {
    Logger log = Logger.getLogger(MenuPrincipalController.class);
    @FXML
    @ActionTrigger("facturacion")
    private Button buttonFacturacion;
         
    @FXML
    @ActionTrigger("controlador")
    private Button buttonControlador;
    
    @FXML
    @ActionTrigger("retirarDinero")
    private Button buttonRetirarDinero;
    
    @FXML
    @ActionTrigger("mostrarError")
    private Button buttonError;
    
    @FXML
    private VBox vboxMenuPrincipal;
    
    @FXML
    private ImageView imageViewLogoRight;
    @FXML
    private ImageView imageViewLogoLeft;
    
    
    @Inject
    private DataModelTicket modelTicket;
    
        
    @PostConstruct
    public void init(){
        log.info("Ingresando al menú principal");
        loadImage();
        Platform.runLater(()->{
            try{
                Connection.initFiscalPrinter();
            }catch(TpvException e){
                log.error(e.getMessage());
                modelTicket.setException(e);
                modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_MENUPRINCIPAL);
                buttonError.fire();
            }
            
            vboxMenuPrincipal.setOnKeyPressed(keyEvent->{
                log.debug("Tecla pulsada: "+keyEvent.getCode());
                if(keyEvent.getCode()==KeyCode.NUMPAD1)
                    modelTicket.setCliente(null);
                    modelTicket.setClienteSeleccionado(false);
                    modelTicket.setReinicioVerificado(false);
                    modelTicket.getDetalle().clear();
                    buttonFacturacion.fire();
                if(keyEvent.getCode()==KeyCode.NUMPAD4)
                    System.exit(0);
                keyEvent.consume();
            });
        });
    }
    
    private void loadImage(){
        String f = this.getClass().getResource("/com/tpv/resources/logologin.jpg").toExternalForm();
        imageViewLogoRight.setImage(new Image(f));
        imageViewLogoLeft.setImage(new Image(f));
    }    
}
