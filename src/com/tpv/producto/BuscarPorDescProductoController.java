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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    private TextField textFieldFiltroProducto;
    
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
                textFieldFiltroProducto.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ENTER){
                        if(textFieldFiltroProducto.getText().trim().equals("")){
                            ProductoData productoData = (ProductoData)tableView.getSelectionModel().getSelectedItem();
                            modelTicket.setCodigoProdSelecEnBuscarPorDesc(productoData.CodigoProductoProperty().get());
                            buttonAceptar.fire();
                            keyEvent.consume();
                        }
                        else{
                            cargarTableView();
                            textFieldFiltroProducto.setText("");
                        }
                        
                    }
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        modelTicket.setCodigoProdSelecEnBuscarPorDesc(0);
                        buttonAceptar.fire();
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
                    
                    
                    keyEvent.consume();
                });
        });
        
    }
    
    public void cargarTableView(){
        data = FXCollections.observableArrayList();
        List<Producto> productos = productoService.getProductos(textFieldFiltroProducto.getText());
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
            
        }
        
    }
    
    public static class ProductoData{
        private IntegerProperty CodigoProducto;
        private StringProperty Descripcion;
        private ObjectProperty<BigDecimal> Precio;
        
        public ProductoData(int codigoProducto,String descripcion, BigDecimal precio){
            this.CodigoProducto = new SimpleIntegerProperty(codigoProducto);
            this.Descripcion = new SimpleStringProperty(descripcion);
            this.Precio = new SimpleObjectProperty(precio);
        }

        public IntegerProperty CodigoProductoProperty(){
            return CodigoProducto;
        }
        
        public StringProperty DescripcionProperty(){
            return Descripcion;
        }
        
        public ObjectProperty<BigDecimal> PrecioProperty(){
            return Precio;
        }
        
        public int getCodigoProducto(){
            return CodigoProductoProperty().get();
        }
        
        public String getDescripcion(){
            return DescripcionProperty().get();
        }
        
        public BigDecimal getPrecio(){
            return PrecioProperty().get();
        }
        
    }
}
