/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import com.tpv.combos.FacturaDetalleComboData;
import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.enums.TipoTituloSupervisorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Cliente;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.FacturaDetalleCombo;
import com.tpv.modelo.ListaPrecioProducto;
import com.tpv.modelo.Producto;
import com.tpv.modelo.enums.FacturaEstadoEnum;
import com.tpv.print.event.FiscalPrinterEvent;
import com.tpv.service.ClienteService;
import com.tpv.service.FacturacionService;
import com.tpv.service.ImpresoraService;
import com.tpv.service.ProductoService;
import com.tpv.util.ui.MaskTextField;
import com.tpv.util.ui.MensajeModalAceptar;
import com.tpv.util.ui.TabPaneModalCommand;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.Callback;
import javafx.util.Duration;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;
import org.tpv.print.fiscal.FiscalPacket;
import org.tpv.print.fiscal.FiscalPrinter;
import org.tpv.print.fiscal.hasar.HasarCommands;
import org.tpv.print.fiscal.msg.FiscalMessages;

/**
 *
 * 
 * @author daniel
 */
//@FXMLController(value="FXMLMain.fxml", title = "Edit user")
public class FXMLMainController implements Initializable {
    TabPanePrincipalController tabPaneController;
    private final static String LABEL_CANTIDAD="Cantidad:";
    private final static String LABEL_CANTIDAD_INGRESADA="(Cantidad->";
    private final static String TITULO_INGRESO_CLIENTE="Ingreso de Cliente";
    private final static String TITULO_INGRESO_CANTIDAD="Ingrese Cantidad";
    
    Logger log = Logger.getLogger(FXMLMainController.class);

    
    ProductoService productoService = new ProductoService();
    ClienteService clienteService = new ClienteService();
    final ImpresoraService impresoraService = new ImpresoraService();
    
    private FiscalPrinterEvent fiscalPrinterEvent;
    
    
    private FacturacionService factService = new FacturacionService();
    
    
    
    private MaskTextField textFieldProducto;
    private MaskTextField textFieldCantidad;
    private MaskTextField textFieldCodCliente;
    
    private FadeTransition fadeInOut;
    private FadeTransition fadeRetiroDinero;
    private LineaTicketData lineaTicketData;
    
    @FXML
    private GridPane gridPaneCodigoProducto;

    @FXML
    private Label nroticket;
    
    @FXML
    private Label retiroDineroLabel;
    
    //@FXML
    //private Pane retiroDineroPane;
    
    @FXML
    private Label labelProducto;
    
    @FXML
    private Label labelCantidad;
    
    @FXML
    private Label labelCliente;
    
    @FXML
    private Label nombreCliente;
    
    @FXML
    private Label checkout;
    
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
    private ImageView imageViewDer;
    
//    @FXML
//    private ImageView imageViewIzq;
//    
    @FXML
    private ImageView imageViewLoading;
    
    
    
    
    
   
    @FXML
    private Label totalGeneral;
    
    @FXML
    private Label subtotal;
    
    @FXML
    private Label bonificaciones;
    
    @FXML
    private Label labelTotalGral;
    
    private boolean imprimiendo;
    
    public void configurarInicio() throws TpvException{
        log.info("Ingresando a pantalla de facturación");
        //impresoraService.getPrinterVersion();        
        verificarRetiroDinero();        
        this.tabPaneController.repeatFocus(textFieldCodCliente);
        
        if(impresoraService.getHfp().getEventListener()==null)
            asignarEvento();
        

        traerInfoImpresora();        
        
        initTableViewTickets();
        verificarDetalleTableView();
        
        textFieldCodCliente.setText("");
        
        chequearInterfazNegativo();    
        
        initLoadingIcon();

        
        tableViewTickets.setItems(Context.getInstance().currentDMTicket().getDetalle());
        calcularTotalGeneral();
        scrollDown();
        if(Context.getInstance().currentDMTicket().isClienteSeleccionado()){
            labelProducto.setVisible(true);
            textFieldProducto.setVisible(true);
            labelCliente.setVisible(false);
            textFieldCodCliente.setVisible(false);
            stackPaneIngresos.setVisible(false);
            if(Context.getInstance().currentDMTicket().getCliente()!=null){
                nombreCliente.setText(Context.getInstance().currentDMTicket().getCliente().getRazonSocial());
            }
            tabPaneController.repeatFocus(textFieldProducto);
        }else{
            nombreCliente.setVisible(false);
            labelSubTituloIngresos.setText(TITULO_INGRESO_CLIENTE);
            stackPaneIngresos.setVisible(true);
            textFieldCodCliente.requestFocus();
            textFieldProducto.setVisible(false);
            labelProducto.setVisible(false);
            textFieldCodCliente.setVisible(true);
            tabPaneController.repeatFocus(textFieldCodCliente);
            labelCliente.setVisible(true);
        }
        
        if(Context.getInstance().currentDMTicket().getCodigoProdSelecEnBuscarPorDesc()>0){
            textFieldProducto.setText(Context.getInstance().currentDMTicket().getCodigoProdSelecEnBuscarPorDesc()+"");
            Context.getInstance().currentDMTicket().setCodigoProdSelecEnBuscarPorDesc(0);
        }
        if(Context.getInstance().currentDMTicket().getCodigoClienteSelecEnBuscarPorDesc()>0){
            textFieldCodCliente.setText(""+Context.getInstance().currentDMTicket().getCodigoClienteSelecEnBuscarPorDesc());
            Context.getInstance().currentDMTicket().setCodigoClienteSelecEnBuscarPorDesc(0);
        }
        labelCantidad.setVisible(false);
        
    }
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        setBanner();
        configurarAnimacionIngresoNegativo();
        retiroDineroLabel.setText("Pedir retiro de dinero");
        activarAnimacionRetiroDinero();
        asignarEvento();
        
        labelCantidad.setText(LABEL_CANTIDAD);
        
       
                
        
        
        Platform.runLater(() -> {
            
            iniciaIngresosVisibles();

            textFieldCodCliente.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.F2){
                    tabPaneController.gotoCliente();
                    keyEvent.consume();
                } 
                
                if(keyEvent.getCode()== KeyCode.ENTER){
                    
                    if(textFieldCodCliente.getText().trim().equals("")){
                        labelCliente.setVisible(false);
                        textFieldCodCliente.setVisible(false);
                        stackPaneIngresos.setVisible(false);                        
                        labelProducto.setVisible(true);
                        textFieldProducto.setVisible(true);
                        Context.getInstance().currentDMTicket().setCliente(null);
                        nombreCliente.setVisible(false);
                        Context.getInstance().currentDMTicket().setClienteSeleccionado(true);
                    }else{
                        traerCliente();
                    } 
                    tabPaneController.repeatFocus(textFieldProducto);
                }
                if(keyEvent.getCode()==KeyCode.F11 ){
                    if(Context.getInstance().currentDMTicket().getDetalle().size()==0)
                        this.tabPaneController.gotoMenuPrincipal();
                    /*else{
                        Context.getInstance().currentDMTicket().setTipoTituloSupervisor(TipoTituloSupervisorEnum.HABILITAR_MENU);
                        tabPaneController.gotoSupervisor();
                        
                    }*/
                }
                
                if(keyEvent.getCode() == KeyCode.BACK_SPACE
                        || keyEvent.getCode() == KeyCode.DELETE)
                    return;
                keyEvent.consume();
            });
            
            textFieldCantidad.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.ENTER ||
                        keyEvent.getCode() == KeyCode.ESCAPE){
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        try{
                            int cantidad = Integer.parseInt(textFieldCantidad.getText());
                            if(cantidad <=0)
                                return;
                        }catch(Exception e){
                            return;
                        }
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
                        tabPaneController.repeatFocus(textFieldProducto);
                    }
                    
                }
                if(keyEvent.getCode() == KeyCode.BACK_SPACE
                        || keyEvent.getCode() == KeyCode.DELETE)
                    return;
                keyEvent.consume();
            });
            
            
            textFieldProducto.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    if(Context.getInstance().currentDMTicket().getDetalle().size()==0){
                        Context.getInstance().currentDMTicket().setClienteSeleccionado(false);
                        labelProducto.setVisible(false);
                        textFieldProducto.setVisible(false);
                        stackPaneIngresos.setVisible(true);
                        textFieldCantidad.setVisible(false);
                        labelCantidad.setVisible(false);
                        textFieldCodCliente.setVisible(true);
                        labelCliente.setVisible(true);
                        labelSubTituloIngresos.setText("Ingreso de Cliente");
                        tabPaneController.repeatFocus(textFieldCodCliente);
                    }
                }
                if(keyEvent.getCode() == KeyCode.F3){
                    tabPaneController.gotoProducto();
                }
                if(keyEvent.getCode() == KeyCode.ENTER){
                    labelCantidadIngresada.setVisible(false);
                    if(textFieldCantidad.isVisible()){
                        textFieldCantidad.setVisible(false);
                    }
                    if(textFieldProducto.getText().trim().length()>0){
                        
                        enviarComandoLineaTicket();
                        
                        //efectoImpresion();
                        
                        
                        scrollDown();
                    }else{
                        if(Context.getInstance().currentDMTicket().getDetalle().size()>0)
                            tabPaneController.gotoPago();
                        else{
                            //tabPaneController.getLabelCancelarModal().setVisible(false);
                            //tabPaneController.getLabelMensaje().setText("No es posible ir al pago con un ticket sin Productos");
                            //tabPaneController.mostrarMensajeModal();
                            tabPaneController.showMsgModal(new  MensajeModalAceptar(
                                    "Error","No es posible ir al pago con un ticket sin Productos"
                                    ,"",textFieldProducto
                            ));
                        }
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
                    if(Context.getInstance().currentDMTicket().getDetalle().isEmpty())
                        
                        this.tabPaneController.gotoMenuPrincipal();
                    else{
                        
                        //Context.getInstance().currentDMTicket().setTipoTituloSupervisor(TipoTituloSupervisorEnum.HABILITAR_MENU);
                        //tabPaneController.gotoSupervisor();
                        
                    }
                }
                if(keyEvent.getCode() == KeyCode.F4){
                        textFieldCantidad.setVisible(true);
                        textFieldCantidad.setText("");
                        textFieldProducto.setVisible(false);
                        labelCantidad.setText(LABEL_CANTIDAD);
                        labelCantidad.setVisible(true);
                        labelProducto.setVisible(false);
                        stackPaneIngresos.setVisible(true);
                        labelSubTituloIngresos.setText(TITULO_INGRESO_CANTIDAD);
                        tabPaneController.repeatFocus(textFieldCantidad);
                        stackPaneIngresos.toFront();
                        
                }
                
                
                if(keyEvent.getCode() ==  KeyCode.F5){
                    if(Context.getInstance().currentDMTicket().getDetalle().size()>0){
                        Context.getInstance().currentDMTicket().setTipoTituloSupervisor(TipoTituloSupervisorEnum.HABILITAR_NEGATIVO);
                        tabPaneController.gotoSupervisor();
                    }
                }
                
                if(keyEvent.getCode() == KeyCode.F6){
                    Context.getInstance().currentDMTicket().setImprimeComoNegativo(false);
                    chequearInterfazNegativo();        
                    keyEvent.consume();
                }
                
                if(keyEvent.getCode() == KeyCode.F7){
                    if(Context.getInstance().currentDMTicket().getIdFactura()!=null){
                        Context.getInstance().currentDMTicket().setTipoTituloSupervisor(TipoTituloSupervisorEnum.CANCELAR_TICKET);
                        //habilitarSupervisorButton.fire();
                        tabPaneController.gotoSupervisor();
                    }else{
                        log.error("No se puede cancelar un ticket que no está abierto");
                        Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
                        Context.getInstance().currentDMTicket().setException(new TpvException("No se puede cancelar un ticket que no está abierto"));
                        tabPaneController.gotoError();
                    }
                }
                
                if(keyEvent.getCode() == KeyCode.F8){
                    if(Context.getInstance().currentDMTicket().getDetalle().getSize()>0)
                        tabPaneController.gotoCombos();
                }
                
                if(keyEvent.getCode() == KeyCode.F2)
                    tabPaneController.gotoCliente();
                
                if(keyEvent.getCode() == KeyCode.F3)
                    tabPaneController.gotoProducto();
                    
                if(keyEvent.getCode() == KeyCode.BACK_SPACE
                        || keyEvent.getCode() == KeyCode.DELETE)
                    return;
                keyEvent.consume();

            });

        });

    }    
    

    private void calcularTotalGeneral(){
        DecimalFormat df = new DecimalFormat("##,##0.00");
        subtotal.setText(df.format(Context.getInstance().currentDMTicket().getTotalTicket()));
        totalGeneral.setText(df.format(Context.getInstance().currentDMTicket().getTotalTicket()));
        
    }
    
    private void scrollDown(){
            if(tableViewTickets.getItems().size()>0){
                tableViewTickets.getSelectionModel().select(tableViewTickets.getItems().size()-1);
                tableViewTickets.scrollTo(tableViewTickets.getItems().size()-1);
            }
    }
    
    private void enviarComandoLineaTicket(){
        int codigoIngresado=0;
        String codBarra="";
        BigDecimal cantidad = new BigDecimal(1);
        String descripcion="";
        Producto producto = null;
        BigDecimal precioOPeso = BigDecimal.ZERO;
        BigDecimal precio = BigDecimal.valueOf(0);
        BigDecimal descuentoCliente = BigDecimal.valueOf(0);
        ListaPrecioProducto lpp;
        try{
            cantidad = new BigDecimal(textFieldCantidad.getText());
        }catch(Exception e){
            
        }
        try{
            codigoIngresado = Integer.parseInt(textFieldProducto.getText());
            log.debug("Codigo a consultar en productoService");
        }catch(Exception e){
            codBarra = textFieldProducto.getText();
        }

        try{
        
                if(codigoIngresado>0){
                    log.debug("Antes de buscar en productoService");
                    producto = productoService.getProductoPorCodigo(codigoIngresado); //productoService.getProductoPorCodigo(codigoIngresado);
                    //producto = productoService.getProductoPorCodBarra(textFieldProducto.getText());
                }else{
                    if(codBarra.substring(0, 2).equals("20")){
                        codigoIngresado = Integer.parseInt(codBarra.substring(2,7));
                        precioOPeso = BigDecimal
                                .valueOf(Double.parseDouble(codBarra.substring(7,9)+"."+codBarra.substring(9, 13))) ;
                        precioOPeso = precioOPeso.setScale(3,BigDecimal.ROUND_HALF_EVEN);
                        producto = productoService.getProductoPorCodigo(codigoIngresado);
                    }else{
                        producto = productoService.getProductoPorCodBarra(codBarra);
                    }
                }
                        
                        
                if(producto!=null){
                    log.debug("Producto encontrado: "+producto.getDescripcion());
                    lpp = productoService.getListaPrecioProducto(producto.getCodigoProducto(),Context.getInstance().currentDMTicket().getCliente());
                    if(lpp == null){
                        throw new TpvException("El producto "+producto.getDescripcion()+", con código "+producto.getCodigoProducto()+" no tiene precio");
                         
                    }
                    precio = lpp.getPrecioFinal();
                   
                    if(producto.isProductoVilleco()){
                        if(precioOPeso.compareTo(BigDecimal.ZERO)>0){
                           cantidad = precioOPeso.divide(precio).setScale(3, RoundingMode.DOWN);
                        }
                    }else{
                        if(precioOPeso.compareTo(BigDecimal.ZERO)>0){
                            cantidad = precioOPeso.setScale(3, RoundingMode.DOWN);
                        }
                    }
                    if(precio.compareTo(BigDecimal.valueOf(0))>0){
                        if(Context.getInstance().currentDMTicket().getDetalle().size()==0){
                                imageViewLoading.setVisible(true);
                                efectoAbrirTicket();
                        }
                        /*descripcion = producto.getCodigoProducto()+" "+ producto.getDescripcion();
                        if(producto.isProductoVilleco()){
                            descripcion="#"+descripcion;
                        }*/

                        if(Context.getInstance().currentDMTicket().isImprimeComoNegativo())
                            if(!anulaItemIngresado(producto.getCodigoProducto(), cantidad)){
                                textFieldCantidad.setText("");
                                return;
                            }
                        
                    //LineaTicketData(int codigoProducto,String descripcion,BigDecimal cantidad,BigDecimal precioUnitario
                    //,BigDecimal precioUnitarioBase,BigDecimal neto,BigDecimal netoReducido,BigDecimal exento
                    //,BigDecimal descuentoCliente,BigDecimal iva ,BigDecimal ivaReducido
                    //,BigDecimal impuestoInterno,BigDecimal retencion ,boolean devuelto)
                        
                        
                        lineaTicketData = new LineaTicketData(
                                  producto.getCodigoProducto()
                                , producto.getDescripcion(),cantidad,precio
                                , lpp.getPrecioUnitario()
                                , lpp.getNeto().multiply(cantidad)
                                , lpp.getNetoReducido().multiply(cantidad)
                                , lpp.getExento().multiply(cantidad)
                                , lpp.getDescuentoCliente().multiply(cantidad)
                                , lpp.getIvaCompleto().multiply(cantidad)
                                , lpp.getIvaReducido().multiply(cantidad)
                                , lpp.getMontoImpuestoInterno().multiply(cantidad)
                                , new BigDecimal(0)
                                , producto.getValorImpositivo().getValor()
                                , Context.getInstance().currentDMTicket().isImprimeComoNegativo());

                        if(Context.getInstance().currentDMTicket().isImprimeComoNegativo()){
                            precio = precio.multiply(BigDecimal.valueOf(-1));
                            cantidad = cantidad.multiply(new BigDecimal(-1));
                        }    
                        precio = precio.setScale(2,BigDecimal.ROUND_HALF_EVEN);
                        

                        
                        //impresoraService.imprimirLineaTicket(producto.getDescripcionConCodigo(), cantidad
                        //        ,precio ,producto.getValorImpositivo().getValor() ,Context.getInstance().currentDMTicket().isImprimeComoNegativo(), producto.getImpuestoInterno());
                        imageViewLoading.setVisible(true);
                        efectoImprimirLinea(producto, cantidad, precio);


        //                    Context.getInstance().currentDMTicket().getDetalle().add(lineaTicketData);                    
        //                    if(Context.getInstance().currentDMTicket().getDetalle().size()>1){
        //                            agregarDetalleFactura(lineaTicketData);
        //
        //                    }
                    }
                }
        }catch(TpvException e){
                Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
                Context.getInstance().currentDMTicket().setException(e);
                tabPaneController.gotoError();
        }
        
        textFieldProducto.setText("");
        //(String descripcion,BigDecimal cantidad
        //,BigDecimal precio, BigDecimal iva,BigDecimal impuestoInterno) 
        calcularTotalGeneral();
        textFieldCantidad.setText("");
    }
    
    public void iniciaIngresosVisibles(){
        
        textFieldProducto =  new MaskTextField();
        textFieldProducto.setMask("N!");
        textFieldProducto.setVisible(false);
        textFieldProducto.getStyleClass().add("textfield_sin_border");
        
        textFieldCantidad = new MaskTextField();
        textFieldCantidad.setMask("N!");//textFieldCantidad.setRawMask("^[0-9]+(\\.([0-9]{1,2})?)?$");
        textFieldCantidad.setVisible(false);
        textFieldCantidad.setMaxDigitos(4);
        textFieldCantidad.setPrefWidth(150);
        textFieldCantidad.setMaxWidth(150);
        textFieldCantidad.getStyleClass().add("textfield_sin_border");
        
        textFieldCodCliente = new MaskTextField();
        textFieldCodCliente.setMask("N!");
        textFieldCodCliente.setMaxDigitos(11);
        textFieldCodCliente.setPrefWidth(190);
        textFieldCodCliente.setMaxWidth(190);
        textFieldCodCliente.getStyleClass().add("textfield_sin_border");
        textFieldCodCliente.setFocusTraversable(true);
        
        labelCantidadIngresada.setVisible(false);
        
        gridPaneCodigoProducto.add(textFieldCodCliente,1,1);
        gridPaneCodigoProducto.add(textFieldProducto,1,1);
        gridPaneCodigoProducto.add(textFieldCantidad,1,3);
        gridPaneIngresos.add(textFieldCodCliente,1,1);
        gridPaneIngresos.add(textFieldCantidad,1,1);
    }

    public void traerCliente(){
        
        try{
            Cliente cliente = clienteService.getClientePorCodODniOCuit(textFieldCodCliente.getText());
            if(cliente!=null){
                nombreCliente.setText("Cliente: "+cliente.getCuit()+" "+cliente.getRazonSocial());
                nombreCliente.setVisible(true);
                labelCliente.setVisible(false);
                textFieldCodCliente.setVisible(false);
                stackPaneIngresos.setVisible(false);                                    
                labelProducto.setVisible(true);
                textFieldProducto.setVisible(true);
                Context.getInstance().currentDMTicket().setClienteSeleccionado(true);
                Context.getInstance().currentDMTicket().setCliente(cliente);
            }else
                textFieldCodCliente.setText("");
        }catch(TpvException e){
                Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
                Context.getInstance().currentDMTicket().setException(e);
                tabPaneController.gotoError();
        }
            

    }
    
    public void traerInfoImpresora() throws TpvException{
        if(Context.getInstance().currentDMTicket().getNroTicket()==0){
//            try{

                String retorno[] = impresoraService.getPtoVtaNrosTicket();
                Context.getInstance().currentDMTicket().setNroTicket(Integer.parseInt(retorno[1])+1);
                Context.getInstance().currentDMTicket().setNroFacturaA(Integer.parseInt(retorno[2])+1);
                Context.getInstance().currentDMTicket().setPuntoVenta(Long.parseLong(retorno[0]));
                Context.getInstance().currentDMTicket().setTicketAbierto(Boolean.parseBoolean(retorno[3]));

//            }catch(TpvException e){
//                log.error(e.getMessage());
//                Context.getInstance().currentDMTicket().setException(e);
//                Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
//                tabPaneController.gotoError();
                
//            }
        }
        nroticket.setText("Pto.Venta: "+Context.getInstance().currentDMTicket().getPuntoVenta()+" ║ Nº Ticket: "
                            +Context.getInstance().currentDMTicket().getNroTicket()
                            +" ║ Nº Fact.A: "+Context.getInstance().currentDMTicket().getNroFacturaA()
                //+" Nro. Ticket (A): "+retorno[2]
        );
        checkout.setText("Checkout: "+Context.getInstance().currentDMTicket().getCheckout().getId()
                    +" ║ Caja: "+Context.getInstance().currentDMTicket().getCaja()
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
//                        Context.getInstance().currentDMTicket().setException(new TpvException("La impresora no está conectada"));
//                        throw Context.getInstance().currentDMTicket().getTpvException();
//                    }else{
//                            retorno = impresoraService.getPtoVtaNrosTicket();
//                    }
//                    
//                    updateMessage("Pto.Venta: "+retorno[0]+" Nro. Ticket (B/C): "
//                            +retorno[1]+" Nro. Ticket (A): "+retorno[2]);
//                    Context.getInstance().currentDMTicket().setNroTicket(Integer.parseInt(retorno[1]));
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
        ingresoNegativoHabilitado.setVisible(Context.getInstance().currentDMTicket().isImprimeComoNegativo());
        ingresoNegativoPane.setVisible(Context.getInstance().currentDMTicket().isImprimeComoNegativo());
        if(Context.getInstance().currentDMTicket().isImprimeComoNegativo()){
            labelProducto.getStyleClass().clear();
            labelProducto.getStyleClass().add("label_textfield_negativo");
            labelCliente.getStyleClass().clear();
            labelCliente.getStyleClass().add("label_textfield_negativo");
            labelCantidad.getStyleClass().clear();
            labelCantidad.getStyleClass().add("label_textfield_negativo");
            totalGeneral.getStyleClass().clear();
            totalGeneral.getStyleClass().add("label_textfield_negativo");
            labelTotalGral.getStyleClass().clear();
            labelTotalGral.getStyleClass().add("label_textfield_negativo");
        }else{
            labelProducto.getStyleClass().clear();
            labelProducto.getStyleClass().add("label_textfield");
            labelCliente.getStyleClass().clear();
            labelCliente.getStyleClass().add("label_textfield");
            labelCantidad.getStyleClass().clear();
            labelCantidad.getStyleClass().add("label_textfield");
            totalGeneral.getStyleClass().clear();
            totalGeneral.getStyleClass().add("label_textfield");
            labelTotalGral.getStyleClass().clear();
            labelTotalGral.getStyleClass().add("label_textfield");
        }
    }
    
    private void configurarAnimacionIngresoNegativo(){
                fadeInOut = new FadeTransition(Duration.seconds(1),ingresoNegativoPane);
		fadeInOut.setFromValue(1.0);
		fadeInOut.setToValue(.20);
		fadeInOut.setCycleCount(FadeTransition.INDEFINITE);
		fadeInOut.setAutoReverse(true);
		fadeInOut.play();
        
    }
    
    private void activarAnimacionRetiroDinero(){
        fadeRetiroDinero = new FadeTransition(Duration.millis(400),retiroDineroLabel);
        fadeRetiroDinero.setFromValue(1.0);
        fadeRetiroDinero.setToValue(.20);
        fadeRetiroDinero.setCycleCount(FadeTransition.INDEFINITE);
        fadeRetiroDinero.setAutoReverse(true);
        fadeRetiroDinero.play();
    }
    
    private boolean anulaItemIngresado(int codigo,BigDecimal cantidad ){
        Iterator iterator = tableViewTickets.getItems().iterator();
        BigDecimal totalCantidad= new BigDecimal(0);
        boolean itemEncontrado=false;
        while(iterator.hasNext()){
            LineaTicketData lineaTicket = (LineaTicketData)iterator.next();
            if(lineaTicket.getCodigoProducto()==codigo &&
                    lineaTicket.getDevuelto()==false){
                  totalCantidad = totalCantidad.add(lineaTicket.getCantidad());
                
            }
            if(lineaTicket.getCodigoProducto()==codigo && 
                lineaTicket.getCantidad().compareTo(cantidad) >= 0 && 
                lineaTicket.getDevuelto()==false &&
                itemEncontrado == false
                    ){  
                lineaTicket.setDevuelto(true);
                itemEncontrado=true;
                break;
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
        factura.setTotal(Context.getInstance().currentDMTicket().getTotalTicket());
        factura.setEstado(FacturaEstadoEnum.ABIERTA);
        factura.setCliente(Context.getInstance().currentDMTicket().getCliente());
        if(Context.getInstance().currentDMTicket().getCliente()!=null)
            factura.setCondicionIva(Context.getInstance().currentDMTicket().getCliente().getCondicionIva());
        factura.setCheckout(Context.getInstance().currentDMTicket().getCheckout());
        factura.setPrefijoFiscal(Context.getInstance().currentDMTicket().getPuntoVenta());
        /*ListProperty<LineaTicketData> detalle =  Context.getInstance().currentDMTicket().getDetalle();
        
        detalle.forEach(item->{
            FacturaDetalle facturaDetalle = new FacturaDetalle();
            Producto producto = null;
            try{
                producto=productoService.getProductoPorCodigo(item.getCodigoProducto());
            }catch(TpvException e){
                log.error("Error: "+e.getMessage());
                Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
                Context.getInstance().currentDMTicket().setException(e);
                tabPaneController.gotoError();
            }

            facturaDetalle.setFactura(factura);
            facturaDetalle.setProducto(producto);
            facturaDetalle.setCantidad(item.getCantidad());
            facturaDetalle.setSubTotal(item.getSubTotal());
            facturaDetalle.setDescuento(BigDecimal.ZERO);
            facturaDetalle.setPrecioUnitario(item.getPrecioUnitario());
            facturaDetalle.setPorcentajeIva(item.getPorcentajeIva());
            factura.getDetalle().add(facturaDetalle);
        });*/
        Factura facturaGuardada=null;
        try{
            factura.setNumeroComprobante(impresoraService.getNroUltimoTicketBC());
            factura.setUsuario(Context.getInstance().currentDMTicket().getUsuario());
            factura.setCheckout(Context.getInstance().currentDMTicket().getCheckout());
            facturaGuardada=factService.registrarFactura(factura);
        }catch(TpvException e){
            log.error("Error: "+e.getMessage());
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }
        Context.getInstance().currentDMTicket().setIdFactura(facturaGuardada.getId());
        log.debug("ID de factura: "+facturaGuardada.getId());
    }  
    
    private void agregarDetalleFactura(){
        Context.getInstance().currentDMTicket().getDetalle().add(lineaTicketData);                    
//        if(Context.getInstance().currentDMTicket().getDetalle()d.size()>1){
//            agregarDetalleFactura(lineaTicketData);
//
//        }
        
        FacturaDetalle facturaDetalle = new FacturaDetalle();
        facturaDetalle.setCantidad(lineaTicketData.getCantidad());
        facturaDetalle.setDescuento(lineaTicketData.getDescuentoCliente());
        facturaDetalle.setExento(lineaTicketData.getExento());
        facturaDetalle.setImpuestoInterno(lineaTicketData.getImpuestoInterno());
        facturaDetalle.setIva(lineaTicketData.getIva());
        facturaDetalle.setIvaReducido(lineaTicketData.getIvaReducido());
        facturaDetalle.setNeto(lineaTicketData.getNeto());
        facturaDetalle.setNetoReducido(lineaTicketData.getNetoReducido());
        facturaDetalle.setPrecioUnitario(lineaTicketData.getPrecioUnitario());
        facturaDetalle.setPrecioUnitarioBase(lineaTicketData.getPrecioUnitarioBase());
        facturaDetalle.setCosto(BigDecimal.ZERO);
        facturaDetalle.setSubTotal(lineaTicketData.getSubTotal());
        facturaDetalle.setPorcentajeIva(lineaTicketData.getPorcentajeIva());
        Producto producto ;
        

        try{
            producto = productoService.getProductoPorCodigo(lineaTicketData.getCodigoProducto());
            /*facturaDetalle.setImpuestoInterno(
                        producto.getImpuestoInterno().multiply(facturaDetalle.getSubTotal())
                                .divide(new BigDecimal(100))
            );*/
            if(Context.getInstance().currentDMTicket().isImprimeComoNegativo())
                facturaDetalle.setUsuarioSupervisor(Context.getInstance().currentDMTicket().getUsuarioSupervisor());
            facturaDetalle.setProducto(producto);
            factService.agregarDetalleFactura(Context.getInstance().currentDMTicket().getIdFactura(), facturaDetalle);
        }catch(TpvException e){
            log.error("Error: "+e.getMessage());
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }
    }
    
    
    private void initTableViewTickets(){
        tableViewTickets.setRowFactory(new Callback<TableView<LineaTicketData>, TableRow<LineaTicketData>>(){
            @Override
            public TableRow<LineaTicketData> call(TableView<LineaTicketData> paramP) {
                return new TableRow<LineaTicketData>() {
                    
                    @Override
                    protected void updateItem(LineaTicketData item, boolean paramBoolean) {
                        super.updateItem(item, paramBoolean);
                        if (item!=null){
                            
                            if(item.getSubTotal().compareTo(BigDecimal.valueOf(0))<0){
                                setStyle(
                                     "-fx-background-color: red;"   
                                    +"-fx-background-insets: 0, 0 0 1 0;"
                                    +"-fx-padding: 0.0em;"
                                    +"-fx-text-fill: black;"
                                );
                                
                            }else{
                                setStyle(
                                    "-fx-background-insets: 0, 0 0 1 0;"
                                    +"-fx-padding: 0.0em;"
                                    +"-fx-text-fill: -fx-text-inner-color;"
                                );

                            }
                        }else{
                            getStyleClass().remove("table-row-cell-negativo");
                        }
                        setItem(item);
                        
                    }
                };
            }
        });
        
        
        
        codigoColumn.setCellValueFactory(new PropertyValueFactory<LineaTicketData,Integer>("codigoProducto"));
        codigoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        descripcionColumn.setCellValueFactory(new PropertyValueFactory("descripcion"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory("cantidad"));
        cantidadColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        cantidadColumn.setCellFactory(col ->{
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
//                            if(item.compareTo(BigDecimal.ZERO)<0)
//                                this.setStyle("-fx-text-fill: red");
//                            else
//                                this.setStyle("-fx-text-fill: black");
                            this.setText(df.format(item));
                    }
                }
            };
            return cell;
        });
        
        subTotalColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
//        subTotalColumn.setCellFactory(col -> {
//            TableCell<LineaTicketData,BigDecimal>cell = new TableCell<LineaTicketData,BigDecimal>(){
//                @Override
//                public void updateItem(BigDecimal item,boolean empty){
//                    super.updateItem(item, empty);
//                    this.setText(null);
//                    this.setGraphic(null);
//                    if (!empty) {
//                            //String formattedDob = De
//                            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
//                                    
//                            this.setText(df.format(item));
//                    }
//                }
//            };
//            return cell;
//        });
        
    }
   

    
    private void asignarEvento(){
        this.fiscalPrinterEvent = new FiscalPrinterEvent(){
            @Override
            public void commandExecuted(FiscalPrinter source, FiscalPacket command, FiscalPacket response){
                log.info("Se ejecutó correctamente el siguiente comando:");
                if(command.getCommandCode()==HasarCommands.CMD_OPEN_FISCAL_RECEIPT){
                    log.debug("     CMD_OPEN_FISCAL_RECEIPT: ");
                    imprimiendo=false;
                    guardarFacturaPrimeraVez();
                }
                
                if(command.getCommandCode()==HasarCommands.CMD_STATUS_REQUEST){
                    log.debug("     CMD_STATUS_REQUEST");
                    
                }
                
                if(command.getCommandCode()==HasarCommands.CMD_PRINT_LINE_ITEM){

                    agregarDetalleFactura();
                    calcularTotalGeneral();
                    verificarRetiroDinero();
                }
//                log.debug("Mensajes de error: ");
//                source.getMessages().getErrorMsgs().forEach(item->{
//                    log.debug("     Código de Error: "+item.getCode());
//                    log.debug("     Titulo: "+item.getTitle());
//                    log.debug("     Descripción: "+item.getDescription());
//                });
//                log.debug("Mensajes: ");
//                source.getMessages().getMsgs().forEach(item->{
//                    log.debug("     Código de Msg: "+item.getCode());
//                    log.debug("     Titulo: "+item.getTitle());
//                    log.debug("     Descripción: "+item.getDescription());
//                    
//                });
                
            }
            
            @Override
            public void printEnded(FiscalPrinter source, FiscalMessages msgs){
                tabPaneController.ocultarMensajeModal();
            }
        };
        impresoraService.getHfp().setEventListener(this.fiscalPrinterEvent);
        
    }
    
    private void initLoadingIcon(){
        String f = this.getClass().getResource("/com/tpv/resources/loading31.gif").toExternalForm();
                
        imageViewLoading.setImage(new Image(f));
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



        String f = this.getClass().getResource("/com/tpv/resources/gif-emilio-luque.gif").toExternalForm();
        imageViewDer.setImage(new Image(f));
        
        
//        imageViewIzq.setImage(new Image(f));
        //mp.play();
    }
            
    /**
     * Verifica si hay una factura abierta. Si la hay debe ser anulada
     * en base de datos y cancelada en controlador fiscal
     * @throws TpvException 
     */
    private void verificarDetalleTableView() throws TpvException{
        log.info("Verificando detalle de TableView");
        if(Context.getInstance().currentDMTicket().isReinicioVerificado()){
            return;
        }
        if(Context.getInstance().currentDMTicket().getDetalle().size()==0 && 
                tableViewTickets.getItems().size()>0)
            tableViewTickets.getItems().clear();
        Factura factura = null;
        //try{
            factura = factService.getFacturaAbiertaPorCheckout(Context.getInstance().currentDMTicket().getCheckout().getId()
                    ,Context.getInstance().currentDMTicket().getUsuario().getIdUsuario());
            if(factura!=null){
                factService.anularFacturaPorReinicio(factura.getId());
                if(Context.getInstance().currentDMTicket().isTicketAbierto()){
                    log.warn("Factura abierta en base de datos y cerrada en impresora. Se procede a cancelar en BD");
                   impresoraService.cancelarTicket();
                }
                //}else{
                if(factura.getCliente()!=null){
                    Context.getInstance().currentDMTicket().setCliente(factura.getCliente());
                    
                }
                Context.getInstance().currentDMTicket().setClienteSeleccionado(true);
                
                impresoraService.abrirTicket();
                    
                        for(Iterator iterator = factura.getDetalle().iterator();iterator.hasNext();){
                            FacturaDetalle fd = (FacturaDetalle)iterator.next();


                            lineaTicketData = new LineaTicketData(
                                             fd.getProducto().getCodigoProducto()
                                            ,fd.getProducto().getDescripcion(),fd.getCantidad()
                                            ,fd.getPrecioUnitario()
                                            ,fd.getPrecioUnitarioBase()
                                            ,fd.getNeto(),fd.getNetoReducido(),fd.getExento()
                                            ,fd.getDescuento()
                                            ,fd.getIva()
                                            ,fd.getIvaReducido()
                                            ,fd.getImpuestoInterno()
                                            ,new BigDecimal(0)
                                            ,fd.getPorcentajeIva()
                                            ,(fd.getSubTotal().compareTo(BigDecimal.ZERO)<0?true:false)
                            );   
                            //Context.getInstance().currentDMTicket().getDetalle().add(lineaTicketData);
                            impresoraService.imprimirLineaTicket(
                                    fd.getProducto().getDescripcionConCodigo()
                                    ,fd.getCantidad()
                                    ,fd.getPrecioUnitario()
                                    ,fd.getProducto().getValorImpositivo().getValor() 
                                    ,(fd.getSubTotal().compareTo(BigDecimal.ZERO)<0?true:false)
                                    ,fd.getProducto().getImpuestoInterno());

                        }
                        //Context.getInstance().currentDMTicket().setIdFactura(factura.getId());
                //}
            }
            Context.getInstance().currentDMTicket().setReinicioVerificado(true);                    
        /*}catch(TpvException e){
            log.error("Error en capa controller: "+e.getMessage());
              
            Context.getInstance().currentDMTicket().setCliente(null);
            Context.getInstance().currentDMTicket().setClienteSeleccionado(false);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }*/
    }
    
    private void verificarRetiroDinero(){
        BigDecimal saldoRetiro = BigDecimal.ZERO;
        try{
            saldoRetiro = factService.saldoRetiroDinero(Context.getInstance()
                        .currentDMTicket().getUsuario().getIdUsuario(), Context.getInstance().currentDMTicket().getCheckout().getId());
        }catch(TpvException e){
            log.error("Error en capa controller: "+e.getMessage());
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }   
        if(saldoRetiro.compareTo(Context.getInstance().currentDMParametroGral().getMontoRetiroDinero())>=0)
            retiroDineroLabel.setVisible(true);
        else
            retiroDineroLabel.setVisible(false);        
    }
    
    private void verCombos(){
        ObservableList<FacturaDetalleComboData> combosItems = FXCollections.observableArrayList();
        ListProperty<FacturaDetalleComboData> listCombos = new SimpleListProperty<>(combosItems);
        
        try{
            Factura factura = factService.calcularCombos(Context.getInstance().currentDMTicket().getIdFactura());
            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
                FacturaDetalleCombo fdc = it.next();
                FacturaDetalleComboData fdcd = new FacturaDetalleComboData(
                            fdc.getCombo().getCodigoCombo(),
                            fdc.getCombo().getDescripcion(),
                            fdc.getCantidad(),
                            fdc.getBonificacion(),
                            ""
                        );
                listCombos.add(fdcd);
                
            }
        }catch(TpvException e){
            log.error("Error en capa controller: "+e.getMessage());
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            Context.getInstance().currentDMTicket().setException(e);
            tabPaneController.gotoError();
        }
    }
    
    public void setTabController(TabPanePrincipalController tabPane){
        this.tabPaneController=tabPane;
    }
    
    /*
    public void aceptarMensajeModal(){
        this.tabPaneController.ocultarMensajeModal();
        this.tabPaneController.getLabelCancelarModal().setVisible(true);
        this.tabPaneController.repeatFocus(textFieldProducto);
    }
    
    public void cancelarMensajeModal(){

    }
    */
    
    public void efectoAbrirTicket(){
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                try{
                    impresoraService.abrirTicket();                
                }catch(TpvException e){
                    Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
                    Context.getInstance().currentDMTicket().setException(e);
                    tabPaneController.gotoError();
                }
                finally { 
                    imageViewLoading.setVisible(false);
                }     
            }
        });        
    }
    
    
    public void efectoImprimirLinea(Producto producto,BigDecimal cantidad
            ,BigDecimal precio){
        
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                try{
                        
                
                        impresoraService.imprimirLineaTicket(producto.getDescripcionConCodigo(), cantidad
                                ,precio ,producto.getValorImpositivo().getValor() ,Context.getInstance().currentDMTicket().isImprimeComoNegativo(), producto.getImpuestoInterno());

                        
                }catch(TpvException e){
                    Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
                    Context.getInstance().currentDMTicket().setException(e);
                    tabPaneController.gotoError();
                }
                finally { 
                    imageViewLoading.setVisible(false);
                } 
                
            }
        });
    }
    
    
}


