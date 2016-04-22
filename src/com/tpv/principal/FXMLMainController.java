/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.enums.TipoTituloSupervisorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Cliente;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.Producto;
import com.tpv.modelo.enums.FacturaEstadoEnum;
import com.tpv.print.event.FiscalPrinterEvent;
import com.tpv.service.ClienteService;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.ProductoService;
import com.tpv.util.Connection;
import com.tpv.util.ui.MaskTextField;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;
import org.tpv.print.fiscal.FiscalPacket;
import org.tpv.print.fiscal.FiscalPrinter;
import org.tpv.print.fiscal.hasar.HasarCommands;
import static org.tpv.print.fiscal.hasar.HasarCommands.CMD_STATUS_REQUEST;
import org.tpv.print.fiscal.msg.FiscalMessages;

/**
 *
 * 
 * @author daniel
 */
@FXMLController(value="FXMLMain.fxml", title = "Edit user")
public class FXMLMainController implements Initializable {
    private final static String LABEL_CANTIDAD="Cantidad:";
    private final static String LABEL_CANTIDAD_INGRESADA="(Cantidad->";
    private final static String TITULO_INGRESO_CLIENTE="Ingreso de Cliente";
    private final static String TITULO_INGRESO_CANTIDAD="Ingrese Cantidad";
    
    Logger log = Logger.getLogger(FXMLMainController.class);

    
    ProductoService productoService = new ProductoService();
    ClienteService clienteService = new ClienteService();
    ImpresoraService impresoraService = new ImpresoraService();
    
    private FiscalPrinterEvent fiscalPrinterEvent;
    
    
    private FacturacionService factService = new FacturacionService();
    
    
    
    private MaskTextField textFieldProducto;
    private MaskTextField textFieldCantidad;
    private MaskTextField textFieldCodCliente;
    
    private FadeTransition fadeInOut;
    private LineaTicketData lineaTicketData;
    
    @FXML
    private GridPane gridPaneCodigoProducto;

    @FXML
    private Label nroticket;
    
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
    private Label ingresoNegativoHabilitado;
    
    @FXML
    private Label labelCantidadIngresada;
    
    @FXML
    private Label labelSubTituloIngresos;
    
    @FXML
    private Pane ingresoNegativoPane;
    
    @FXML
    private GridPane gridPaneIngresos;
    
    @FXML
    private StackPane stackPaneMediaView;
    
    @FXML
    private StackPane stackPaneIngresos;
    
    @FXML
    //private MediaView mediaView;
    private ImageView imageView;
    
    
    @FXML
    @ActionTrigger("mostrarError")
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
    @ActionTrigger("habilitarSupervisor")
    private Button habilitarSupervisorButton;
   
    @FXML
    private Label totalGeneral;
    
    @FXML
    private Label labelTotalGral;
    
    @Inject
    private DataModelTicket modelTicket;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @PostConstruct
    public void init(){
        setBanner();
        asignarEvento();
        configurarAnimacionIngresoNegativo();
        tableViewTickets.setRowFactory(new Callback<TableView<LineaTicketData>, TableRow<LineaTicketData>>(){
            @Override
            public TableRow<LineaTicketData> call(TableView<LineaTicketData> paramP) {
                return new TableRow<LineaTicketData>() {
                    @Override
                    protected void updateItem(LineaTicketData item, boolean paramBoolean) {
                        super.updateItem(item, paramBoolean);
                        if (item!=null){
                            if(item.getSubTotal().compareTo(BigDecimal.valueOf(0))<0){
                                setStyle("-fx-background-color: red");
                            }else{
                                setStyle("-fx-background-color: white");
                            }
                        }
                        setItem(item);
                        
                    }
                };
            }
            
          

        });
        
        labelCantidad.setText(LABEL_CANTIDAD);
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
                            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                                    
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
        });
        subTotalColumn.setCellValueFactory(new PropertyValueFactory("subTotal"));
        subTotalColumn.setCellFactory(col->{
            TableCell<LineaTicketData,BigDecimal> cell = new TableCell<LineaTicketData,BigDecimal>(){
                @Override
                public void updateItem(BigDecimal item, boolean empty){
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
                            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                                    
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
        });
        if(modelTicket.getCodigoProdSelecEnBuscarPorDesc()>0){
            textFieldProducto.setText(modelTicket.getCodigoProdSelecEnBuscarPorDesc()+"");
            modelTicket.setCodigoProdSelecEnBuscarPorDesc(0);
        }
        if(modelTicket.getCodigoClienteSelecEnBuscarPorDesc()>0){
            textFieldCodCliente.setText(""+modelTicket.getCodigoClienteSelecEnBuscarPorDesc());
            modelTicket.setCodigoClienteSelecEnBuscarPorDesc(0);
        }
        
        
        
        Platform.runLater(() -> {
            chequearInterfazNegativo();            
            //traerInfoImpresora();
            tableViewTickets.setItems(modelTicket.getDetalle());
            calcularTotalGeneral();
            scrollDown();
            
            
            textFieldCodCliente.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.F2){
                    clienteButton.fire();
                    keyEvent.consume();
                } 
                
                if(keyEvent.getCode()== KeyCode.ENTER){
                    if(textFieldCodCliente.getText().trim().equals("")){
                        labelCliente.setVisible(false);
                        textFieldCodCliente.setVisible(false);
                        
                        stackPaneIngresos.setVisible(false);                        
                        labelProducto.setVisible(true);
                        textFieldProducto.setVisible(true);


                        modelTicket.setClienteSeleccionado(true);
                    }else{
                        traerCliente();
                    } 
                    
                }
                if(keyEvent.getCode()==KeyCode.F11){
                    volverMenuPrincipalButton.fire();
                }
            });
            
            textFieldCantidad.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.ENTER ||
                        keyEvent.getCode() == KeyCode.ESCAPE){
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        labelCantidadIngresada.setText(LABEL_CANTIDAD_INGRESADA+textFieldCantidad.getText()+")");
                        labelCantidadIngresada.setVisible(true);
                    }else{
                        labelCantidadIngresada.setVisible(false);                                                
                    }
                    if(textFieldCantidad.isVisible()){
                        textFieldProducto.setVisible(true);
                        labelProducto.setVisible(true);
                        textFieldCantidad.setVisible(false);
                        stackPaneIngresos.setVisible(false);
                    }
                    
                }
            });
            
            textFieldProducto.requestFocus();
            textFieldProducto.setOnKeyPressed(keyEvent -> {
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

                        enviarComandoLineaTicket();
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
                        stackPaneIngresos.setVisible(true);
                        labelSubTituloIngresos.setText(TITULO_INGRESO_CANTIDAD);
                        stackPaneIngresos.toFront();
                }
                
                if(keyEvent.getCode() ==  KeyCode.F5){
                    modelTicket.setTipoTituloSupervisor(TipoTituloSupervisorEnum.HABILITAR_NEGATIVO);
                    habilitarSupervisorButton.fire();
                    keyEvent.consume();
                }
                
                if(keyEvent.getCode() == KeyCode.F6){
                    modelTicket.setImprimeComoNegativo(false);
                    chequearInterfazNegativo();        
                    keyEvent.consume();
                }
                
                if(keyEvent.getCode() == KeyCode.F7){
                    modelTicket.setTipoTituloSupervisor(TipoTituloSupervisorEnum.CANCELAR_TICKET);
                    habilitarSupervisorButton.fire();
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
    
    private void enviarComandoLineaTicket(){
        int codigoIngresado=0;
        BigDecimal cantidad = new BigDecimal(1);
        String descripcion="";
        Producto producto = null;
        BigDecimal precio = BigDecimal.valueOf(0);
        try{
            cantidad = new BigDecimal(textFieldCantidad.getText());
        }catch(Exception e){
            
        }
        try{
            codigoIngresado = Integer.parseInt(textFieldProducto.getText());
        }catch(Exception e){
            
        }

        if(codigoIngresado>0){
            producto = productoService.getProductoPorCodigo(codigoIngresado); //productoService.getProductoPorCodigo(codigoIngresado);
        }else{
            //producto = productoService.getProductoPorCodBarra(textFieldProducto.getText());
        }
        if(producto!=null){
            precio= productoService.getPrecioProducto(codigoIngresado);
            if(precio.compareTo(BigDecimal.valueOf(0))>0){
                if(modelTicket.getDetalle().size()==0){
                    try{
                        impresoraService.abrirTicket();
                        //guardarFacturaPrimeraVez();
                    }catch(TpvException e){
                        modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
                        modelTicket.setException(e);
                        goToErrorButton.fire();
                    }
                }
                descripcion = producto.getCodigoProducto()+" "+ producto.getDescripcion();

//                if(modelTicket.isImprimeComoNegativo())
//                    if(!anulaItemIngresado(producto.getCodigoProducto(), cantidad)){
//                        textFieldCantidad.setText("");
//                        return;
//                    }
                lineaTicketData = new LineaTicketData(producto.getCodigoProducto()
                        ,producto.getDescripcion(),cantidad,precio,modelTicket.isImprimeComoNegativo());
                
                try{
                    impresoraService.imprimirLineaTicket(descripcion, cantidad
                            ,precio ,producto.getValorImpositivo().getValor() ,modelTicket.isImprimeComoNegativo(), producto.getImpuestoInterno());
                    if(modelTicket.isImprimeComoNegativo()){
                        precio = precio.multiply(BigDecimal.valueOf(-1));
                        cantidad = cantidad.multiply(new BigDecimal(-1));
                    }                    

                    
//                    modelTicket.getDetalle().add(lineaTicketData);                    
//                    if(modelTicket.getDetalle().size()>1){
//                            agregarDetalleFactura(lineaTicketData);
//
//                    }
                }catch(TpvException e){
                    modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
                    modelTicket.setException(e);
                    goToErrorButton.fire();
                }
            }
            
        }
        textFieldProducto.setText("");
        //(String descripcion,BigDecimal cantidad
        //,BigDecimal precio, BigDecimal iva,BigDecimal impuestoInterno) 
        calcularTotalGeneral();
        textFieldCantidad.setText("");
    }
    
    public void iniciaIngresosVisibles(){
        textFieldProducto = new MaskTextField();
        textFieldProducto.setMask("N!");
        textFieldProducto.setVisible(false);
        textFieldProducto.getStyleClass().add("textfield_sin_border");
        
        textFieldCantidad = new MaskTextField();
        textFieldCantidad.setMask("N!.N!");
        textFieldCantidad.setVisible(false);
        textFieldCantidad.setPrefWidth(150);
        textFieldCantidad.setMaxWidth(150);
        textFieldCantidad.getStyleClass().add("textfield_sin_border");
        
        textFieldCodCliente = new MaskTextField();
        textFieldCodCliente.setMask("N!");
        textFieldCodCliente.setPrefWidth(150);
        textFieldCodCliente.setMaxWidth(150);
        textFieldCodCliente.getStyleClass().add("textfield_sin_border");
        
        labelCantidadIngresada.setVisible(false);
        
        
//        gridPaneCodigoProducto.add(textFieldCodCliente,1,1);
        gridPaneCodigoProducto.add(textFieldProducto,1,1);
//        gridPaneCodigoProducto.add(textFieldCantidad,1,3);
        gridPaneIngresos.add(textFieldCodCliente,1,1);
        gridPaneIngresos.add(textFieldCantidad,1,1);
        
        if(modelTicket.isClienteSeleccionado()){
            labelProducto.setVisible(true);
            textFieldProducto.setVisible(true);
            labelCliente.setVisible(false);
            textFieldCodCliente.setVisible(false);
            stackPaneIngresos.setVisible(false);
            if(modelTicket.getCliente()!=null){
                nombreCliente.setText(modelTicket.getCliente().getRazonSocial());
            }
        }else{
            nombreCliente.setVisible(false);
            labelSubTituloIngresos.setText(TITULO_INGRESO_CLIENTE);
            
        }
                
    }

    public void traerCliente(){
        
        
        Cliente cliente = clienteService.getClientePorCodYDni(Integer.parseInt(textFieldCodCliente.getText()));
        if(cliente!=null){
            nombreCliente.setText(cliente.getRazonSocial());
            nombreCliente.setVisible(true);
            labelCliente.setVisible(false);
            textFieldCodCliente.setVisible(false);
            stackPaneIngresos.setVisible(false);                                    
            labelProducto.setVisible(true);
            textFieldProducto.setVisible(true);
            modelTicket.setClienteSeleccionado(true);
            modelTicket.setCliente(cliente);
        }

    }
    
    public void traerInfoImpresora(){
        if(modelTicket.getNroTicket()==0){
            try{

                String retorno[] = impresoraService.getPtoVtaNrosTicket();
                modelTicket.setNroTicket(Integer.parseInt(retorno[1])+1);
                modelTicket.setPuntoVenta(Integer.parseInt(retorno[0]));
            }catch(TpvException e){
                log.error(e.getMessage());
                modelTicket.setException(e);
                modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
                goToErrorButton.fire();
                
            }
        }
        nroticket.setText("Pto.Venta: "+modelTicket.getPuntoVenta()+" Nro. Ticket (B/C): "
                            +modelTicket.getNroTicket()
                //+" Nro. Ticket (A): "+retorno[2]
        );
        
//            Worker<String> worker = new Task<String>(){
//                @Override
//                protected String call() throws Exception{
//                    String retorno[] = new String[3];
//                    try {
//                        Thread.sleep(20);
//                    } catch (InterruptedException e) {
//                        if (isCancelled()) {
//                            //updateValue("Canceled at " + System.currentTimeMillis());
//                            return null; // ignored
//                        }
//                    }
//                    
//                    if(!Connection.getStcp().isConnected()){
//                        modelTicket.setException(new TpvException("La impresora no está conectada"));
//                        throw modelTicket.getTpvException();
//                    }else{
//                            retorno = impresoraService.getPtoVtaNrosTicket();
//                    }
//                    
//                    updateMessage("Pto.Venta: "+retorno[0]+" Nro. Ticket (B/C): "
//                            +retorno[1]+" Nro. Ticket (A): "+retorno[2]);
//                    modelTicket.setNroTicket(Integer.parseInt(retorno[1]));
//                    return "Tarea finalizada";
//                }
//            };
//            ((Task<String>) worker).setOnFailed(event -> {
//               goToErrorButton.fire();
//            });
//            nroticket.textProperty().bind(worker.messageProperty());
//            
//            new Thread((Runnable) worker).start();
    }

    
    private void chequearInterfazNegativo(){
        //ingresoNegativoHabilitado.setVisible(modelTicket.isImprimeComoNegativo());
        ingresoNegativoPane.setVisible(modelTicket.isImprimeComoNegativo());
//        if(modelTicket.isImprimeComoNegativo()){
//            labelProducto.getStyleClass().clear();
//            labelProducto.getStyleClass().add("label_textfield_negativo");
//            labelCliente.getStyleClass().clear();
//            labelCliente.getStyleClass().add("label_textfield_negativo");
//            labelCantidad.getStyleClass().clear();
//            labelCantidad.getStyleClass().add("label_textfield_negativo");
//            totalGeneral.getStyleClass().clear();
//            totalGeneral.getStyleClass().add("label_textfield_negativo");
//            labelTotalGral.getStyleClass().clear();
//            labelTotalGral.getStyleClass().add("label_textfield_negativo");
//        }else{
//            labelProducto.getStyleClass().clear();
//            labelProducto.getStyleClass().add("label_textfield");
//            labelCliente.getStyleClass().clear();
//            labelCliente.getStyleClass().add("label_textfield");
//            labelCantidad.getStyleClass().clear();
//            labelCantidad.getStyleClass().add("label_textfield");
//            totalGeneral.getStyleClass().clear();
//            totalGeneral.getStyleClass().add("label_textfield");
//            labelTotalGral.getStyleClass().clear();
//            labelTotalGral.getStyleClass().add("label_textfield");
//        }
    }
    
    private void configurarAnimacionIngresoNegativo(){
                fadeInOut = new FadeTransition(Duration.seconds(1),ingresoNegativoPane);
		fadeInOut.setFromValue(1.0);
		fadeInOut.setToValue(.20);
		fadeInOut.setCycleCount(FadeTransition.INDEFINITE);
		fadeInOut.setAutoReverse(true);
		fadeInOut.play();
        
    }
    
    private boolean anulaItemIngresado(int codigo,BigDecimal cantidad ){
        Iterator iterator = tableViewTickets.getItems().iterator();
        BigDecimal totalCantidad= new BigDecimal(0);
        boolean itemEncontrado=false;
        while(iterator.hasNext()){
            LineaTicketData lineaTicket = (LineaTicketData)iterator.next();
            if(lineaTicket.getCodigoProducto()==codigo){
                  //totalCantidad+=lineaTicket.getCantidad();
                
            }
            if(lineaTicket.getCodigoProducto()==codigo && 
                lineaTicket.getCantidad().compareTo(cantidad) == 0 && 
                lineaTicket.getDevuelto()==false &&
                itemEncontrado == false
                    ){
                lineaTicket.setDevuelto(true);
                itemEncontrado=true;
            }
        }
        if(totalCantidad.compareTo(cantidad) >= 0 && itemEncontrado==false){
            Iterator segundoIterator = tableViewTickets.getItems().iterator();
            while(segundoIterator.hasNext()){
                LineaTicketData lineaTicket = (LineaTicketData)segundoIterator.next();
                if(lineaTicket.getCodigoProducto()==codigo && 
                    lineaTicket.getCantidad().compareTo(cantidad)>=0 && 
                    lineaTicket.getDevuelto()==false &&
                    itemEncontrado == false
                        ){
                    lineaTicket.setDevuelto(true);
                    itemEncontrado=true;
                    continue;
                }
            }
        }
        if(totalCantidad.compareTo(cantidad)>=0 && itemEncontrado == true){
            
            return true;
        }else{
            return false;
        }
    }
    
    private void guardarFacturaPrimeraVez(){
        log.debug("Guardar Factura Primera Vez:");
        Factura factura = new Factura();
        factura.setTotal(modelTicket.getTotalTicket());
        factura.setEstado(FacturaEstadoEnum.ABIERTA);
        factura.setCliente(modelTicket.getCliente());
        ListProperty<LineaTicketData> detalle =  modelTicket.getDetalle();
        
        detalle.forEach(item->{
            FacturaDetalle facturaDetalle = new FacturaDetalle();
            Producto producto = productoService.getProductoPorCodigo(item.getCodigoProducto());
            facturaDetalle.setFactura(factura);
            facturaDetalle.setProducto(producto);
            facturaDetalle.setCantidad(item.getCantidad());
            facturaDetalle.setSubTotal(item.getSubTotal());
            factura.getDetalle().add(facturaDetalle);
        });
        Factura facturaGuardada=null;
        try{
            factura.setNumeroComprobante(impresoraService.getNroUltimoTicketBC());
            factura.setUsuario(modelTicket.getUsuario());
            facturaGuardada=factService.registrarFactura(factura);
        }catch(TpvException e){
            log.error("Error: "+e.getMessage());
            modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            modelTicket.setException(e);
        }
        modelTicket.setIdFactura(facturaGuardada.getId());
        log.debug("ID de factura: "+facturaGuardada.getId());
    }  
    
    private void agregarDetalleFactura(){
        modelTicket.getDetalle().add(lineaTicketData);                    
//        if(modelTicket.getDetalle().size()>1){
//            agregarDetalleFactura(lineaTicketData);
//
//        }
        
        FacturaDetalle facturaDetalle = new FacturaDetalle();
        facturaDetalle.setCantidad(lineaTicketData.getCantidad());
        facturaDetalle.setSubTotal(lineaTicketData.getSubTotal());

        try{
            facturaDetalle.setProducto(productoService.getProductoPorCodigo(lineaTicketData.getCodigoProducto()));
            factService.agregarDetalleFactura(modelTicket.getIdFactura(), facturaDetalle);
        }catch(TpvException e){
            log.error("Error: "+e.getMessage());
            modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            modelTicket.setException(e);
        }
    }
    
    private void asignarEvento(){
        this.fiscalPrinterEvent = new FiscalPrinterEvent(){
            @Override
            public void commandExecuted(FiscalPrinter source, FiscalPacket command, FiscalPacket response){
                log.debug("Se ejecutó correctamente el siguiente comando:");
                if(command.getCommandCode()==HasarCommands.CMD_OPEN_FISCAL_RECEIPT){
                    log.debug("     CMD_OPEN_FISCAL_RECEIPT: ");
                    guardarFacturaPrimeraVez();
                }
                
                if(command.getCommandCode()==HasarCommands.CMD_STATUS_REQUEST){
                    log.debug("     CMD_STATUS_REQUEST");
                    
                }
                
                if(command.getCommandCode()==HasarCommands.CMD_PRINT_LINE_ITEM){
                    agregarDetalleFactura();
                }
                log.debug("Mensajes de error: ");
                source.getMessages().getErrorMsgs().forEach(item->{
                    log.debug("     Código de Error: "+item.getCode());
                    log.debug("     Titulo: "+item.getTitle());
                    log.debug("     Descripción: "+item.getDescription());
                });
                log.debug("Mensajes: ");
                source.getMessages().getMsgs().forEach(item->{
                    log.debug("     Código de Msg: "+item.getCode());
                    log.debug("     Titulo: "+item.getTitle());
                    log.debug("     Descripción: "+item.getDescription());
                    
                });
                
            }
        };
        impresoraService.getHfp().setEventListener(this.fiscalPrinterEvent);
                
    }
    
    private void setBanner(){
//        File f = new File("E:\\JAVA TPV\\luque\\sucursales.mp4");//(this.getClass().getResource("Banner.flv").toExternalForm());
//        Media m = new Media(f.toURI().toString());
//        MediaPlayer mp = new MediaPlayer(m);
//        mediaView.setMediaPlayer(mp);
//        //stackPaneMediaView.getChildren().add(mv);
//        
//       
//        mp.setAutoPlay(true);
//        mp.setCycleCount(MediaPlayer.INDEFINITE);
//        
//        mp.setRate(0.5);
        String f = this.getClass().getResource("/com/tpv/resources/sucursales.gif").toExternalForm();
        imageView.setImage(new Image(f));
        
        //mp.play();
    }
           
    
}
