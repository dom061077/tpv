package com.tpv.cliente;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Cliente;
import com.tpv.service.ClienteService;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx8tpv1.TabPanePrincipalController;
import com.tpv.principal.Context;
import org.apache.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author daniel
 */
//@FXMLController(value="BuscarPorNombreCliente.fxml", title = "Cliente a buscar")
public class BuscarPorNombreClienteController implements Initializable  {
    Logger log = Logger.getLogger(BuscarPorNombreClienteController.class);
    
    private ObservableList<ClienteData> data;
    private ClienteService clienteService = new ClienteService();
    private TabPanePrincipalController tabPaneController;

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
    
    public void configurarInicio(){
        
    }
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
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
                            if (clienteData!=null){    
                                Context.getInstance().currentDMTicket().setCodigoClienteSelecEnBuscarPorDesc(clienteData.getCodigoCliente());
                                tabPaneController.gotoFacturacion();
                            }
                            keyEvent.consume();
                        
                    }else{
                        cargarTableView(textFieldFiltroNombreCliente.getText());
                        textFieldFiltroNombreCliente.setText("");
                    }
                }
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    tabPaneController.gotoFacturacion();
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
        List<Cliente> clientes = null;
        try{
                clientes=clienteService.getClientes(filtro);
        }catch(TpvException e){
                log.error("Error: "+e.getMessage());
                Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_BUSCARPORNOMBRECLIENTE);
                Context.getInstance().currentDMTicket().setException(e);
        }
                
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
    
    public void setTabController(TabPanePrincipalController tabPaneController){
        this.tabPaneController = tabPaneController;
    }

   
    
}
