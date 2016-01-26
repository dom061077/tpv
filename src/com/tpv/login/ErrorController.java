/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.login;

import com.tpv.principal.FXMLMainController;
import com.tpv.util.ConnectionState;
import com.tpv.util.TpvLogger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javax.annotation.PostConstruct;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;

/**
 *
 * @author daniel
 */

@FXMLController(value="Error.fxml", title = "Error de Sistema")
public class ErrorController {
    TpvLogger logger = TpvLogger.getTpvLogger(ErrorController.class);
    @FXML
    @ActionTrigger("salir")
    private Button salirButton;
    
    @FXML
    @ActionTrigger("reintentar")
    private Button reintentarButton;
    
    @FXML
    private TextArea errorMsg;
    
    @PostConstruct
    public void init(){
            Platform.runLater(()->{
                logger = TpvLogger.getTpvLogger(FXMLMainController.class);
                try{
                    ConnectionState.initEmf();
                }catch(Exception e){
                    errorMsg.setText(e.getMessage());
                    logger.error("Error en la conexión con la base de datos");
                }
            });
                    
    }
}
