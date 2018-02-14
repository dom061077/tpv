/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.errorui.ErrorController;
import com.tpv.exceptions.TpvException;
import com.tpv.print.fiscal.ConfiguracionImpresoraController;
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
import org.datafx.controller.flow.action.LinkAction;

/**
 *
 * @author daniel
 */
@FXMLController(value="MenuPrincipal.fxml", title = "Menu Principal")
public class MenuPrincipalController {
    Logger log = Logger.getLogger(MenuPrincipalController.class);
    @FXML
    @LinkAction(FXMLMainController.class)
    private Button buttonFacturacion;
         
    @FXML
    @LinkAction(ConfiguracionImpresoraController.class)
    private Button buttonControlador;
    
    @FXML
    @ActionTrigger("retirarDinero")
    private Button buttonRetirarDinero;
    
    @FXML
    @LinkAction(ErrorController.class)
    private Button buttonError;
    
    @FXML
    private VBox vboxMenuPrincipal;
    
    @FXML
    private ImageView imageViewLogoRight1;
    @FXML
    private ImageView imageViewLogoRight2;
    @FXML
    private ImageView imageViewLogoRight3;
    
    
    
    @FXML
    private ImageView imageViewLogoLeft1;
    @FXML
    private ImageView imageViewLogoLeft2;
    @FXML
    private ImageView imageViewLogoLeft3;
    
    
    
    
    @FXML
    private ImageView imageViewLogoBottom1;
    @FXML
    private ImageView imageViewLogoBottom2;
    @FXML
    private ImageView imageViewLogoBottom3;
    
    
    @FXML
    private ImageView imageViewLogoTop1;
    
    @FXML
    private ImageView imageViewLogoTop2;

    @FXML
    private ImageView imageViewLogoTop3;
    
    
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
                if(keyEvent.getCode()==KeyCode.NUMPAD1){
                    modelTicket.setCliente(null);
                    modelTicket.setClienteSeleccionado(false);
                    modelTicket.setReinicioVerificado(false);
                    modelTicket.getDetalle().clear();
                    buttonFacturacion.fire();
                }
                if(keyEvent.getCode()==KeyCode.NUMPAD4)
                    System.exit(0);
                if(keyEvent.getCode()==KeyCode.NUMPAD2){
                    log.debug("Antes de buttoncontrolador: ");
                    buttonControlador.fire();
                }
                keyEvent.consume();
            });
        });
    }
    
    private void loadImage(){
        String f = this.getClass().getResource("/com/tpv/resources/logologin.jpg").toExternalForm();
        imageViewLogoRight1.setImage(new Image(f));
        imageViewLogoRight2.setImage(new Image(f));
        imageViewLogoRight3.setImage(new Image(f));
        imageViewLogoLeft1.setImage(new Image(f));
        imageViewLogoLeft2.setImage(new Image(f));
        imageViewLogoLeft3.setImage(new Image(f));
        imageViewLogoTop1.setImage(new Image(f));
        imageViewLogoTop2.setImage(new Image(f));
        imageViewLogoTop3.setImage(new Image(f));
        imageViewLogoBottom1.setImage(new Image(f));
        imageViewLogoBottom2.setImage(new Image(f));
        imageViewLogoBottom3.setImage(new Image(f));
                
    }    
}
