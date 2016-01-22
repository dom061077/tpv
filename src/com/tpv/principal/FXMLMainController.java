/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import com.tpv.modelo.Producto;
import com.tpv.service.ProductoService;
import com.tpv.util.TpvLogger;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;
import com.tpv.util.TpvLogger;

/**
 *
 * @author daniel
 */
@FXMLController(value="FXMLMain.fxml", title = "Edit user")
public class FXMLMainController implements Initializable {
    ProductoService productoService = new ProductoService();
    TpvLogger logger;
    
    @FXML
    private TextField textFieldProducto;
    
    @FXML
    private Label label;
    @FXML
    private Button button;
    
    @FXML
    private TableView tableViewTickets;
    
    @FXML
    private TableColumn codigoColumn;
    
    @FXML
    private TableColumn descripcionColumn;
    
    @FXML
    private TableColumn cantidadColumn;
    
    @FXML
    private TableColumn precioUnitarioColumn;
    
    @FXML
    private TableColumn subTotalColumn;
    
    @FXML
    @ActionTrigger("buscarCliente")
    private Button clienteButton;
    
    @FXML
    @ActionTrigger("buscarProducto")
    private Button buscarProductoButton;
    
    @FXML
    @ActionTrigger("pagoTicket")
    private Button pagoTicketButton;
    
    @FXML
    private Label totalGeneral;
    
    @Inject
    private DataModelTicket modelTicket;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @PostConstruct
    public void init(){
        
        codigoColumn.setCellValueFactory(new PropertyValueFactory<LineaTicketData,Integer>("codigoProducto"));
        codigoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        descripcionColumn.setCellValueFactory(new PropertyValueFactory("descripcion"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory("cantidad"));
        cantidadColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        precioUnitarioColumn.setCellValueFactory(new PropertyValueFactory("precioUnitario"));
        precioUnitarioColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        precioUnitarioColumn.setCellFactory(col -> {
            TableCell<LineaTicketData,BigDecimal> cell = new TableCell<LineaTicketData,BigDecimal>(){
                @Override
                public void updateItem(BigDecimal item,boolean empty){
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                            //String formattedDob = De
                            DecimalFormat df = new DecimalFormat("##,###.00");
                                    
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
        });
        subTotalColumn.setCellValueFactory(new PropertyValueFactory("subTotal"));
        subTotalColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        subTotalColumn.setCellFactory(col -> {
            TableCell<LineaTicketData,BigDecimal>cell = new TableCell<LineaTicketData,BigDecimal>(){
                @Override
                public void updateItem(BigDecimal item,boolean empty){
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                            //String formattedDob = De
                            DecimalFormat df = new DecimalFormat("#,###.00");
                                    
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
        });

        Platform.runLater(() -> {
            logger = TpvLogger.getTpvLogger(FXMLMainController.class);
            tableViewTickets.setItems(modelTicket.getTickets());
            calcularTotalGeneral();
            scrollDown();
            textFieldProducto.requestFocus();
            textFieldProducto.setOnKeyPressed(keyEvent -> {
                logger.info("Tecla pulsada: "+keyEvent.getCode());
                if(keyEvent.getCode() == KeyCode.F2){
                    clienteButton.fire();
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.F3){
                    buscarProductoButton.fire();
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.F4){
                    pagoTicketButton.fire();
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.ENTER){
                    
                    if(textFieldProducto.getText().trim().length()>0){
                        agregarLineaTicket();
                        scrollDown();
                    }else{
                        pagoTicketButton.fire();
                    }
                }
                
                if(keyEvent.getCode() == KeyCode.PAGE_DOWN){
                    
                    tableViewTickets.getSelectionModel().selectNext();
                    logger.info("Se avanzó una linea");
                }
                if(keyEvent.getCode() == KeyCode.PAGE_DOWN){
                    tableViewTickets.getSelectionModel().selectPrevious();
                }
                
            });
            if(clienteButton.getScene()!=null){
                clienteButton.getScene().setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.F2){
                        if(clienteButton.getScene()!=null){
                            clienteButton.fire();
                            keyEvent.consume();
                        }
                    }
                });
                /*buscarProductoButton.getScene().getAccelerators().put(
                        new KeyCodeCombination(KeyCode.NUMPAD2, KeyCombination.ALT_DOWN), 
                        new Runnable() {
                          @Override public void run() {
                            buscarProductoButton.fire();
                          }
                        }
                      );*/
                buscarProductoButton.getScene().setOnKeyPressed(keyEvent ->{
                    if(keyEvent.getCode() == KeyCode.F3){
                        if(buscarProductoButton.getScene()!=null){
                            buscarProductoButton.fire();
                            keyEvent.consume();
                        }
                    }
                });
                //buttonLogin.getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, 
               /* clienteButton.getScene().addEventFilter(KeyEvent.KEY_PRESSED, 
                        new EventHandler<javafx.scene.input.KeyEvent>(){
                               public void handle(javafx.scene.input.KeyEvent event) {
                                   if(event.getCode() == KeyCode.NUMPAD1 && event.isControlDown()){
                                            if (clienteButton.getScene()!=null){
                                                clienteButton.fire();
                                                event.consume();
                                            }
                                   }
                                   if(event.getCode() == KeyCode.NUMPAD2 && event.isControlDown()){
                                            if (buscarProductoButton.getScene()!=null){
                                                buscarProductoButton.fire();
                                                event.consume();
                                            }
                                   }
                               }
                        }
                );*/
            }
        });
        
    }
    private void calcularTotalGeneral(){
        DecimalFormat df = new DecimalFormat("##,##0.00");
        totalGeneral.setText(df.format(modelTicket.getTotalTicket()));
    }
    
    private void scrollDown(){
            if(tableViewTickets.getItems().size()>0){
                tableViewTickets.getSelectionModel().select(tableViewTickets.getItems().size()-1);
                tableViewTickets.scrollTo(tableViewTickets.getItems().size()-1);
            }
    }
    
    private void agregarLineaTicket(){
        
        int codigoIngresado=0;
        Producto producto;
        try{
            codigoIngresado = Integer.parseInt(textFieldProducto.getText());
        }catch(Exception e){
            
        }
        if(codigoIngresado>0){
            producto = productoService.getProductoPorCodigo(codigoIngresado);
        }else{
            producto = productoService.getProductoPorCodBarra(textFieldProducto.getText());
        }
        if(producto!=null){
            modelTicket.getTickets().add(new LineaTicketData(producto.getCodigoProducto()
                    ,producto.getDescripcion(),1,new BigDecimal(10)));
        }
        textFieldProducto.setText("");
        calcularTotalGeneral();
    }
    
}
