package com.tpv.cliente;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.tpv.modelo.Cliente;
import com.tpv.principal.DataModelTicket;
import com.tpv.service.ClienteService;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
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
    
    private ObservableList<ClienteData> data;
    private ClienteService clienteService = new ClienteService();

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
    private Button volverButton;
    
    @Inject
    private DataModelTicket modelTicket;
    
    
    @PostConstruct
    public void init() {
        log.info("Ingresando al mètodo init");
        codigoColumn.setCellValueFactory(new PropertyValueFactory("CodigoCliente"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory("NombreCliente"));
        dniColumn.setCellValueFactory(new PropertyValueFactory("Dni"));
        cuitColumn.setCellValueFactory(new PropertyValueFactory("Cuit"));
        
        Platform.runLater(() -> {
            textFieldFiltroNombreCliente.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(textFieldFiltroNombreCliente.getText().trim().equals("")){
                            ClienteData clienteData = (ClienteData)tableView.getSelectionModel().getSelectedItem();
                            modelTicket.setCodigoClienteSelecEnBuscarPorDesc(clienteData.getCodigoCliente());
                            volverButton.fire();
                            keyEvent.consume();
                        
                    }else{
                        cargarTableView(textFieldFiltroNombreCliente.getText());
                        textFieldFiltroNombreCliente.setText("");
                    }
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    volverButton.fire();
                }
                
                int index=0;
                if(keyEvent.getCode() == KeyCode.PAGE_DOWN){

                    index = tableView.getSelectionModel().getSelectedIndex();
                    tableView.getSelectionModel().select(index+20);
                    tableView.scrollTo(index+21);
                }
                if(keyEvent.getCode() == KeyCode.PAGE_UP){
                    index = tableView.getSelectionModel().getSelectedIndex();
                    tableView.getSelectionModel().select(index-20);
                    tableView.scrollTo(index-21);


                }
                if(keyEvent.getCode() == KeyCode.DOWN){
                    tableView.getSelectionModel().selectNext();
                    index =tableView.getSelectionModel().getSelectedIndex();
                    tableView.scrollTo(index);

                }

                if(keyEvent.getCode() == KeyCode.UP){
                    tableView.getSelectionModel().selectPrevious();
                    index =tableView.getSelectionModel().getSelectedIndex();
                    tableView.scrollTo(index);

                }
                
                    
            });
        });
    }    
    
    public void cargarTableView(String filtro){
        data = FXCollections.observableArrayList();
        List<Cliente> clientes = clienteService.getClientes(filtro);
        clientes.forEach(cliente->{
            data.add(new ClienteData(
                    cliente.getId(),
                    cliente.getRazonSocial()
                    ,cliente.getDni()
                    ,cliente.getCuit()
            ));
        });
        
        
        tableView.getItems().clear();
        tableView.setItems(null);
        tableView.setItems(data);
        if(data.size()>0){
            tableView.getSelectionModel().select(0);
            
        }
        
    }

   
    
}
