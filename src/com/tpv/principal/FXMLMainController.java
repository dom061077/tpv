/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import com.tpv.modelo.Cliente;
import com.tpv.modelo.Producto;
import com.tpv.service.ClienteService;
import com.tpv.service.ProductoService;
import com.tpv.util.Connection;
import com.tpv.util.ui.MaskTextField;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;

/**
 *
 * 
 * @author daniel
 */
@FXMLController(value="FXMLMain.fxml", title = "Edit user")
public class FXMLMainController implements Initializable {
    private final static String LABEL_CANTIDAD="Cantidad:";
    private final static String LABEL_CANTIDAD_INGRESADA="(Cantidad->";
    
    ProductoService productoService = new ProductoService();
    ClienteService clienteService = new ClienteService();
    
    
    private MaskTextField textFieldProducto;
    private MaskTextField textFieldCantidad;
    private MaskTextField textFieldCodCliente;
    
    @FXML
    private GridPane gridPaneCodigoProducto;
    
    @FXML
    private Label labelProducto;
    
    @FXML
    private Label labelCantidad;
    
    @FXML
    private Label labelCliente;
    
    @FXML
    private Label nombreCliente;
    
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
    @ActionTrigger("gotoError")
    private Button goToErrorButton;
    
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
    @ActionTrigger("volverMenuPrincipal")
    private Button volverMenuPrincipalButton;
    
   
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
        labelCantidad.setText(LABEL_CANTIDAD);
        textFieldProducto = new MaskTextField();
        textFieldProducto.setMask("N!");
        textFieldProducto.setStyle(
                "-fx-background-color: none, none, none;//-fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background;"
                +"-fx-background-insets: 0, 1, 2;"
                +"-fx-background-radius: 3, 2, 2;"
                +"-fx-padding: 0.25em 0.416667em  0.333333em 0.416667em; "
                +"-fx-text-fill: -fx-text-inner-color;"
                +"-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);"
                +"-fx-cursor: text;"
        );
        iniciaIngresosVisibles();
        
        
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
            
            Connection.setButtonFlowFire(goToErrorButton);
            
            
            tableViewTickets.setItems(modelTicket.getTickets());
            calcularTotalGeneral();
            scrollDown();
            
            textFieldCodCliente.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode()== KeyCode.ENTER){
                    if(textFieldCodCliente.getText().trim().equals("")){
                        labelCliente.setVisible(false);
                        textFieldCodCliente.setVisible(false);
                        labelProducto.setVisible(true);
                        textFieldProducto.setVisible(true);
                        modelTicket.setClienteSeleccionado(true);
                    }else{
                        traerCliente();
                    } 
                }
            });
            
            textFieldCantidad.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.ENTER ||
                        keyEvent.getCode() == KeyCode.ESCAPE){
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        labelCantidad.setText(LABEL_CANTIDAD_INGRESADA+textFieldCantidad.getText()+")");
                    }else{
                        labelCantidad.setVisible(false);                                                
                    }
                    if(textFieldCantidad.isVisible()){
                        textFieldProducto.setVisible(true);
                        labelProducto.setVisible(true);
                        textFieldCantidad.setVisible(false);
                        
                    }
                    
                }
            });
            
            textFieldProducto.requestFocus();
            textFieldProducto.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.F2){
                    clienteButton.fire();
                    keyEvent.consume();
                }
                if(keyEvent.getCode() == KeyCode.F3){
                    buscarProductoButton.fire();
                    keyEvent.consume();
                }
                /*if(keyEvent.getCode() == KeyCode.F4){
                    pagoTicketButton.fire();
                    keyEvent.consume();
                }*/
                if(keyEvent.getCode() == KeyCode.ENTER){
                    if(labelCantidad.isVisible()){
                        labelCantidad.setVisible(false);
                    }
                    if(textFieldProducto.getText().trim().length()>0){
                        agregarLineaTicket();
                        scrollDown();
                    }else{
                        pagoTicketButton.fire();
                    }
                }
                int index=0;
                if(keyEvent.getCode() == KeyCode.PAGE_DOWN){
                    
                    index = tableViewTickets.getSelectionModel().getSelectedIndex();
                    tableViewTickets.getSelectionModel().select(index+20);
                    tableViewTickets.scrollTo(index+21);
                }
                if(keyEvent.getCode() == KeyCode.PAGE_UP){
                    index = tableViewTickets.getSelectionModel().getSelectedIndex();
                    tableViewTickets.getSelectionModel().select(index-20);
                    tableViewTickets.scrollTo(index-21);
                    
                    
                }
                if(keyEvent.getCode() == KeyCode.DOWN){
                    tableViewTickets.getSelectionModel().selectNext();
                    index =tableViewTickets.getSelectionModel().getSelectedIndex();
                    tableViewTickets.scrollTo(index);
                            
                }
                
                if(keyEvent.getCode() == KeyCode.UP){
                    tableViewTickets.getSelectionModel().selectPrevious();
                    index =tableViewTickets.getSelectionModel().getSelectedIndex();
                    tableViewTickets.scrollTo(index);
                    
                }
                
                if(keyEvent.getCode() == KeyCode.F11){
                    volverMenuPrincipalButton.fire();
                }
                if(keyEvent.getCode() == KeyCode.F4){
                        textFieldCantidad.setVisible(true);
                        textFieldProducto.setVisible(false);
                        labelCantidad.setText(LABEL_CANTIDAD);
                        labelCantidad.setVisible(true);
                        labelProducto.setVisible(false);
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
        int cantidad = 1;
        Producto producto;
        try{
            cantidad = Integer.parseInt(textFieldCantidad.getText());
        }catch(Exception e){
            
        }
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
                    ,producto.getDescripcion(),cantidad,new BigDecimal(10)));
        }
        textFieldProducto.setText("");
        calcularTotalGeneral();
        textFieldCantidad.setText("");
    }
    
    public void iniciaIngresosVisibles(){
        textFieldProducto.setVisible(false);
        
        textFieldCantidad = new MaskTextField();
        textFieldCantidad.setMask("N!.N!");
        textFieldCantidad.setVisible(false);
        textFieldCantidad.setPrefWidth(150);
        textFieldCantidad.setMaxWidth(150);
        
        textFieldCodCliente = new MaskTextField();
        textFieldCodCliente.setMask("N!");
        textFieldCodCliente.setPrefWidth(150);
        textFieldCodCliente.setMaxWidth(150);
        
        
        
        
        gridPaneCodigoProducto.add(textFieldCodCliente,1,1);
        gridPaneCodigoProducto.add(textFieldProducto,1,2);
        gridPaneCodigoProducto.add(textFieldCantidad,1,3);
        
        if(modelTicket.isClienteSeleccionado()){
            labelProducto.setVisible(true);
            textFieldProducto.setVisible(true);
            labelCliente.setVisible(false);
            textFieldCodCliente.setVisible(false);
            if(modelTicket.getCliente()!=null){
                nombreCliente.setText(modelTicket.getCliente().getRazonSocial());
            }
        }else{
            nombreCliente.setVisible(false);
        }
                
    }

    public void traerCliente(){
        Cliente cliente = clienteService.getClientePorCodYDni(Integer.parseInt(textFieldCodCliente.getText()));
        if(cliente!=null){
            nombreCliente.setText(cliente.getRazonSocial());
            nombreCliente.setVisible(true);
            labelCliente.setVisible(false);
            textFieldCodCliente.setVisible(false);
            labelProducto.setVisible(true);
            textFieldProducto.setVisible(true);
            modelTicket.setClienteSeleccionado(true);
            modelTicket.setCliente(cliente);
            
            
        }
    }
}
