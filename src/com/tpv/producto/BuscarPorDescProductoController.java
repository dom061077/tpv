/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.producto;

import com.tpv.modelo.Producto;
import com.tpv.principal.DataModelTicket;
import com.tpv.principal.LineaTicketData;
import com.tpv.service.ProductoService;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;



@FXMLController(value="BuscarPorDescProducto.fxml", title = "busqueda")
public class BuscarPorDescProductoController {
    private ObservableList<ProductoData> data;
    private ProductoService productoService= new ProductoService();
    
    @FXML
    @ActionTrigger("seleccionarProducto")
    private Button buttonAceptar;
    
    @FXML
    private Button buttonCancelar;
    
    @FXML
    private TextField filtroProducto;
    
    @FXML
    private TableView tableView; 
    
    @FXML
    private TableColumn codigoColumn;
    
    @FXML
    private TableColumn descripcionColumn;

    @Inject
    private DataModelTicket modelTicket;
    
    @PostConstruct
    public void init(){
        System.out.println("INIT DEL Buscar por descripcion controller");
        
        codigoColumn.setCellValueFactory(new  PropertyValueFactory("CodigoProducto"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory("Descripcion"));
        Platform.runLater(() -> {
            //cargarTableView();
                //----------------- eventos de navegacion, para aceptar la seleccion del producto
                // or para cancelar la seleccion
                buttonAceptar.getScene().setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.F10){
                        if(buttonAceptar.getScene()!=null){
                            ProductoData productoData = (ProductoData)tableView.getSelectionModel().getSelectedItem();
                            modelTicket.getTickets().add(
                                    new LineaTicketData(productoData.CodigoProducto.get(),productoData.Descripcion.get(),1,BigDecimal.valueOf(10000))
                            );
                            buttonAceptar.fire();
                            keyEvent.consume();
                        }
                    }
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        if(buttonAceptar.getScene()!=null)
                            buttonAceptar.fire();
                    }
                    
                });
                
                filtroProducto.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ENTER){
                        cargarTableView();
                        
                    }
                });
                
                tableView.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE)
                        buttonAceptar.fire();
                });
            
                /*buttonCancelar.getScene().setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        if(buttonAceptar.getScene()!=null)
                            buttonAceptar.fire();
                    }
                });*/
            
        });
        
    }
    
    public void cargarTableView(){
        data = FXCollections.observableArrayList();
        List<Producto> productos = productoService.getProductos(filtroProducto.getText());
        for(Iterator iter = productos.iterator();iter.hasNext();){
            Producto producto = (Producto)iter.next();
            ProductoData productoData = new ProductoData(producto.getCodigoProducto(),producto.getDescripcion());
            data.add(productoData);
        }
        
        tableView.getItems().clear();
        tableView.setItems(null);
        tableView.setItems(data);
        if(data.size()>0){
            tableView.getSelectionModel().select(0);
            tableView.requestFocus();
            
        }
        
    }
    
    public static class ProductoData{
        private IntegerProperty CodigoProducto;
        private StringProperty Descripcion;
        
        public ProductoData(int codigoProducto,String descripcion){
            this.CodigoProducto = new SimpleIntegerProperty(codigoProducto);
            this.Descripcion = new SimpleStringProperty(descripcion);
        }

        public IntegerProperty CodigoProductoProperty(){
            return CodigoProducto;
        }
        
        public StringProperty DescripcionProperty(){
            return Descripcion;
        }
        
        
    }
}
