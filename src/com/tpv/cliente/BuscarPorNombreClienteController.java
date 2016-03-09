package com.tpv.cliente;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;

/**
 * FXML Controller class
 *
 * @author daniel
 */
@FXMLController(value="BuscarPorNombreCliente.fxml", title = "Cliente a buscar")
public class BuscarPorNombreClienteController  {
    Logger log = Logger.getLogger(BuscarPorNombreClienteController.class);

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TextField textFieldFiltroNombreCliente;
    
    @FXML
    private TableView tableView;
    
    @FXML
    private TableColumn codigoColumn;
    
    @FXML
    private TableColumn nombreColumn;
    
    @FXML
    private TableColumn dniColumn;
    
    @FXML
    private TableColumn cuitColumn;
    
    
    @FXML
    @ActionTrigger("seleccionarCliente")
    private Button seleccionarCliente;
    
    
    
    @PostConstruct
    public void init() {
        // TODO
        Platform.runLater(() -> {
                seleccionarCliente.getScene().setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.F10)
                        if(seleccionarCliente.getScene()!=null){
                            seleccionarCliente.fire();
                        }
                });
                seleccionarCliente.getScene().setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        if(seleccionarCliente.getScene()!=null){
                            seleccionarCliente.fire();
                        }
                    }
                });
            
        });
    }    
    
}
