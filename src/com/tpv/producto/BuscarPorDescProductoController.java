/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.producto;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.ListaPrecioProducto;
import com.tpv.principal.Context;
import com.tpv.principal.LineaTicketData;
import com.tpv.service.ProductoService;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;



//@FXMLController(value="BuscarPorDescProducto.fxml", title = "busqueda")
public class BuscarPorDescProductoController implements Initializable{
    Logger log = Logger.getLogger(BuscarPorDescProductoController.class);
    
    private ObservableList<ProductoData> data;
    private ProductoService productoService= new ProductoService();
    private TabPanePrincipalController tabPaneController;
    
    
    
    @FXML
    private TextField textFieldFiltroProducto;
    
    @FXML
    private TableView tableView; 
    
    @FXML
    private TableColumn codigoColumn;
    
    @FXML
    private TableColumn precioColumn;
    
    
    @FXML
    private TableColumn descripcionColumn;

    public void configurarInicio(){
        
    }
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Ingesando al método init");
        
        codigoColumn.setCellValueFactory(new  PropertyValueFactory("CodigoProducto"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory("Descripcion"));
        precioColumn.setCellValueFactory(new PropertyValueFactory("Precio"));
        precioColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        precioColumn.setCellFactory(col -> {
            TableCell<LineaTicketData,BigDecimal> cell = new TableCell<LineaTicketData,BigDecimal>(){
                @Override
                public void updateItem(BigDecimal item,boolean empty){
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                            //String formattedDob = De
                            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                                    
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
        });
        Platform.runLater(() -> {
                textFieldFiltroProducto.setOnKeyPressed(keyEvent->{
                    if(keyEvent.getCode()==KeyCode.ENTER){
                        if(textFieldFiltroProducto.getText().trim().equals("")){
                            ProductoData productoData = (ProductoData)tableView.getSelectionModel().getSelectedItem();
                            if(productoData!=null){
                                Context.getInstance().currentDMTicket().setCodigoProdSelecEnBuscarPorDesc(productoData.CodigoProductoProperty().get());
                                tabPaneController.gotoFacturacion();
                            }
                            keyEvent.consume();
                        }
                        else{
                            cargarTableView();
                            textFieldFiltroProducto.setText("");
                        }
                        
                    }
                    if(keyEvent.getCode()==KeyCode.ESCAPE){
                        Context.getInstance().currentDMTicket().setCodigoProdSelecEnBuscarPorDesc(0);
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
                    
                    
                    //keyEvent.consume();
                });
        });
        
    }
    
    public void cargarTableView(){
        data = FXCollections.observableArrayList();
        List<ListaPrecioProducto> productosPrecios = null;
        
        try{
              productosPrecios = productoService.getProductosPrecio(textFieldFiltroProducto.getText());
        }catch(TpvException e){
            log.error("Error: "+e.getMessage());
             Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_BUSCARPORDESCPRODUCTO);
             Context.getInstance().currentDMTicket().setException(e);
        }
        if(productosPrecios!=null)    
            productosPrecios.forEach(lstPrecioProducto->{
                if(!lstPrecioProducto.getProducto()
                        .tieneEsteProveedor(Context.getInstance()
                                .currentDMParametroGral()
                                .getIdProveedorPromo()))
                    data.add(new ProductoData(
                            lstPrecioProducto.getProducto().getCodigoProducto(),
                            lstPrecioProducto.getProducto().getDescripcion()
                            ,lstPrecioProducto.getPrecioUnitario()
                    ));
            });
        
        
        tableView.getItems().clear();
        tableView.setItems(null);
        tableView.setItems(data);
        if(data.size()>0){
            tableView.getSelectionModel().select(0);
            
        }
        
        
    }
    public void setTabController(TabPanePrincipalController tabController){
        this.tabPaneController=tabController;
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
