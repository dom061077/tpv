/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx8tpv1;

import com.tpv.cliente.BuscarPorNombreClienteController;
import com.tpv.combos.CombosController;
import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.errorui.ErrorController;
import com.tpv.login.LoginController;
import com.tpv.principal.Context;
import com.tpv.principal.FXMLMainController;
import com.tpv.principal.MenuPrincipalController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.log4j.Logger;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.ParametroGeneral;
import com.tpv.pagoticket.ConfirmaPagoTicketController;
import com.tpv.pagoticket.PagoTicketController;
import com.tpv.print.fiscal.ConfiguracionImpresoraController;
import com.tpv.producto.BuscarPorDescProductoController;
import com.tpv.retirodinero.RetiroDineroConfirmacionController;
import com.tpv.retirodinero.RetiroDineroController;
import com.tpv.retirodinero.RetiroDineroMenuController;
import com.tpv.service.ImpresoraService;
import com.tpv.service.UsuarioService;
import com.tpv.service.UtilidadesService;
import com.tpv.supervisor.SupervisorController;
import com.tpv.util.Connection;
import com.tpv.util.ui.MensajeModal;
import com.tpv.util.ui.MensajeModalAbstract;
import com.tpv.util.ui.MensajeModalAceptar;
import com.tpv.util.ui.MensajeModalCancelar;
import com.tpv.util.ui.TabPaneModalCommand;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author daniel
 */

public class TabPanePrincipalController implements Initializable {

    /**
     * @return the mensajeModal
     */
    public MensajeModal getMensajeModal() {
        return mensajeModal;
    }

    /**
     * @param mensajeModal the mensajeModal to set
     */
    public void setMensajeModal(MensajeModal mensajeModal) {
        this.mensajeModal = mensajeModal;
    }

    /**
     * @return the usuarioLogueadoLabel
     */
    public Label getUsuarioLogueadoLabel() {
        return usuarioLogueadoLabel;
    }



    /**
     * @return the labelMenssajeModalSuperior
     */
    public Label getLabelMenssajeModalSuperior() {
        return labelMensajeModalSuperior;
    }
    Logger log = Logger.getLogger(TabPanePrincipalController.class);
    UsuarioService usuarioService = new UsuarioService();        
    ImpresoraService impresoraService = new ImpresoraService();
    private TabPaneModalCommand tabPaneModalCommand;
    private MensajeModal mensajeModal;
    
    @FXML private LoginController loginController;
    @FXML private MenuPrincipalController menuPrincipalController;
    @FXML private FXMLMainController facturacionController;
    @FXML private ErrorController errorController;
    
    @FXML private SupervisorController supervisorController;
    @FXML private BuscarPorNombreClienteController buscarPorNombreClienteController;
    @FXML private BuscarPorDescProductoController productoController;
    @FXML private ConfirmaPagoTicketController confirmaPagoController;
    @FXML private PagoTicketController pagoTicketController;
    @FXML private ConfiguracionImpresoraController configImpresoraController;
    @FXML private RetiroDineroController retiroDineroController;
    @FXML private RetiroDineroConfirmacionController retiroDineroConfirmacionController;
    @FXML private CombosController combosController; 
    @FXML private RetiroDineroMenuController retiroDineroMenuController;
    
    @FXML private Button buttonMenuPrincipal;
    
    @FXML private Tab tabMenuPrincipal;
    @FXML private Tab tabLogin;
    @FXML private Tab tabFacturacion;  
    @FXML private Tab tabError;
    @FXML private Tab tabSupervisor;
    @FXML private Tab tabCliente;
    @FXML private Tab tabProducto;
    @FXML private Tab tabConfirmarPago;
    @FXML private Tab tabPago;
    @FXML private Tab tabCombos;
    @FXML private Tab tabControlador;
    @FXML private Tab tabRetiroDinero;
    @FXML private Tab tabRetiroDineroConfirmacion;
    @FXML private Tab tabRetiroDineroMenu;
    
    @FXML private TabPane tabPanePrincipal;
    @FXML private StackPane stackPaneModal;
    @FXML private Label labelMensaje;
    @FXML private Label labelAceptarModal;
    @FXML private Label labelCancelarModal;
    @FXML private Label labelTituloVentana;
    @FXML private Label labelShortCut;
    @FXML private Label labelMensajeModalSuperior;
    @FXML private GridPane gridPaneMensajeSuperior;
    @FXML private ImageView imageSuperiorDerecha;
    @FXML private ImageView imageSuperiorIzquierda;
    @FXML private ImageView imageIzquierda;
    @FXML private ImageView imageDerecha;
    @FXML private Label usuarioLogueadoLabel;
    
    
    public Button getButtonMenuPrincipal(){
        return buttonMenuPrincipal;
    }
            
    
    //@Override
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            
            log.error("Error:",throwable);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            Context.getInstance().currentDMTicket().setException(new TpvException("Error no controlado: "+throwable.getMessage()));
            gotoError();
            
        });           

        labelMensaje.wrapTextProperty().set(true);
        labelMensajeModalSuperior.wrapTextProperty().set(true);
        //loadImage();
        this.loginController.setTabController(this);
        
        try{
            initParametrosGenerales();
            initImpresora();
            
            gotoLogin();            
        }catch(TpvException e){
            log.error("Error iniciando sistema",e);
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_LOGIN);
            this.gotoError();
        }
        
        
        this.loginController.getPassword().requestFocus();
        this.stackPaneModal.setVisible(false);
        
        this.menuPrincipalController.setTabController(this);
        this.facturacionController.setTabController(this);
        this.errorController.setTabController(this);
        this.supervisorController.setTabController(this);
        
        this.pagoTicketController.setTabController(this);
        this.confirmaPagoController.setTabController(this);
        this.productoController.setTabController(this);
        this.buscarPorNombreClienteController.setTabController(this);
        this.configImpresoraController.setTabController(this);
        this.combosController.setTabController(this);
        this.retiroDineroController.setTabController(this);
        this.retiroDineroConfirmacionController.setTabController(this);
        this.retiroDineroMenuController.setTabController(this);
        

        getTabPanePrincipal().getSelectionModel().selectedItemProperty()
                .addListener((observable,oldTab,newTab)->{
                   if(newTab.getId().compareTo("tabMenuPrincipal")==0){
                       //menuPrincipalController.setMenuFocus();
                   }
                });
        stackPaneModal.setOnKeyPressed(keyEvent->{
            if(keyEvent.getCode() == KeyCode.ENTER){
                getTabPaneModalCommand().aceptarMensajeModal();
                if(getMensajeModal() != null){
                    getMensajeModal().aceptarMensaje();
                    stackPaneModal.setVisible(false);
                    repeatFocus(getMensajeModal().getNode());
                }
            }
            if(keyEvent.getCode() == KeyCode.ESCAPE){
                getTabPaneModalCommand().cancelarMensajeModal();
                if(getMensajeModal() != null){
                    getMensajeModal().cancelarMensaje();
                    stackPaneModal.setVisible(false);
                    repeatFocus(getMensajeModal().getNode());
                }
            }
        });
        
    }      
    
    @FXML
    private void handleButtonMenuPrincipal(ActionEvent event){
        System.out.println("IR AL MENU PRINCIPAL");
    }   
    
    private void initImpresora() throws TpvException{
            Connection.initFiscalPrinter();
            impresoraService.getPrinterVersion();        
        
    }
    
    private void initParametrosGenerales() throws TpvException{

        ParametroGeneral param = UtilidadesService.getParametroGral("RETENCION_ING_BRUTO_LEYENDA");
        Context.getInstance().currentDMParametroGral().setLeyendaRetIngBrutosCliente(param.getParametroCadena());
        param = UtilidadesService.getParametroGral("RETENCION_ING_BRUTO_MONTO_MINIMO");
        Context.getInstance().currentDMParametroGral().setMontoMinRetIngBrutos(param.getParametroNumerico());
        
        param = UtilidadesService.getParametroGral("INTERES_IVA_TARJETA");
        Context.getInstance().currentDMParametroGral().setPorcentajeIvaIntTarjeta(param.getParametroNumerico());
        Context.getInstance().currentDMParametroGral().setLeyendaIntTarjeta(param.getParametroCadena());
        
        param = UtilidadesService.getParametroGral("BONIFICACION_IVA_TARJETA");
        Context.getInstance().currentDMParametroGral().setPorcentajeIvaBonifTarjeta(param.getParametroNumerico());
        Context.getInstance().currentDMParametroGral().setLeyendaBonifTarjeta(param.getParametroCadena());
        
         param = UtilidadesService.getParametroGral("FORMAT_NUMERO_DINERO");
        Context.getInstance().currentDMParametroGral().setFormatNumeroDinero(param.getParametroCadena());
        
        param = UtilidadesService.getParametroGral("PERFIL_SUPERVISOR");
        Context.getInstance().currentDMParametroGral().setPerfilSupervisor(param.getParametroCadena());
        
        param = UtilidadesService.getParametroGral("SETHEADERTRAILER_LINEA12");
        Context.getInstance().currentDMParametroGral().setSetHeaderTrailerLinea12(param.getParametroCadena());

        param = UtilidadesService.getParametroGral("MONTO_RETIRODINERO");
        Context.getInstance().currentDMParametroGral().setMontoRetiroDinero(param.getParametroNumerico());

    }
    

    
    public void gotoLogin(){
        try{
            this.getLabelTituloVentana().setText("INGRESO");
            this.getLabelShortCut().setText("F12 - Sale del Sistema");
            this.loginController.configurarInicio();
            this.getTabPanePrincipal().getSelectionModel().select(tabLogin);
        }catch(TpvException e){
            log.error(e.getMessage());
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_LOGIN);
            gotoError();
        }
            
       
    }
    
    public void gotoMenuPrincipal(){
        this.getLabelTituloVentana().setText("MENU PRINCIPAL");
        this.getLabelShortCut().setText("1-Facturacion   |   2-Controlador   |   3-Carga Retiro de Dinero  |   4-Retiro de Dinero  |   5-Salir del Sistema");
        this.getTabPanePrincipal().getSelectionModel().select(tabMenuPrincipal);
        
        this.menuPrincipalController.configurarInicio();
    }
    
    public void gotoFacturacion(){
        try{
            this.getLabelTituloVentana().setText("FACTURACIÓN");
            this.getLabelShortCut().setText("F2-Cliente     |   F3-Producto |   F4-Ing.Cantidad     |   F5-Negativo     |   F6-Deshabilita Negativo     |   F7-Cancela Ticket   |   F8-Ofertas   |  F11-Menú Principal");            
            this.facturacionController.configurarInicio();
            this.getTabPanePrincipal().getSelectionModel().select(tabFacturacion);
        }catch(TpvException e){
            log.error("Error en inicio de facturación",e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            Context.getInstance().currentDMTicket().setException(e);
            gotoError();
        }    
        
    }
    
    public void gotoError(){
        this.getLabelTituloVentana().setText("ERROR");
        this.getLabelShortCut().setText("Esc-Recuperar Sistema | F12-Salir de Sistema");
        this.errorController.configurarInicio();
        this.getTabPanePrincipal().getSelectionModel().select(tabError);
    }
    
    public void gotoSupervisor(){
        this.getLabelTituloVentana().setText("AUTORIZACIÓN DE SUPERVISOR");
        this.getLabelShortCut().setText("Esc-Cancela Operación");
        this.supervisorController.configurarInicio();
        this.getTabPanePrincipal().getSelectionModel().select(tabSupervisor);
        
    }
    
    public void gotoCliente(){
        this.getLabelTituloVentana().setText("BÚSQUEDA DE CLIENTE");
        this.getLabelShortCut().setText("Esc-Volver");
        this.buscarPorNombreClienteController.configurarInicio();
        this.getTabPanePrincipal().getSelectionModel().select(tabCliente);
    }
    
    public void gotoProducto(){
        this.getLabelTituloVentana().setText("BÚSQUEDA DE PRODUCTO");
        this.getLabelShortCut().setText("Esc-Volver");
        this.productoController.configurarInicio();
        this.getTabPanePrincipal().getSelectionModel().select(tabProducto);
    }
    
    public void gotoConfirmarPago(){
        this.getLabelTituloVentana().setText("CONFIRMACIÓN DE PAGO Y CIERRE DE TICKET");
        this.getLabelShortCut().setText("Esc-Volver");
        try{
            this.confirmaPagoController.configurarInicio();
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_PAGOTICKET);
            Context.getInstance().currentDMTicket().setException(e);
            gotoError();
        }
        this.getTabPanePrincipal().getSelectionModel().select(tabConfirmarPago);
    }
    
    public void gotoPago(){
        try{
            this.getLabelTituloVentana().setText("INGRESO DE PAGOS");
            this.getLabelShortCut().setText("Esc-Volver  |   F3-Formas de Pago  |   Singo Menos (-) Elimina Pago");
            this.pagoTicketController.configurarInicio();
            this.getTabPanePrincipal().getSelectionModel().select(tabPago);
        }catch(TpvException e)    {
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_PAGOTICKET);
            Context.getInstance().currentDMTicket().setException(e);
            gotoError();
        }
        
    }
    
    public void gotoCombos(){
        this.getLabelTituloVentana().setText("BONIFICACIONES DE LA COMPRA");
        this.getLabelShortCut().setText("Esc-Volver");        
        this.combosController.configurarInicio();
        this.getTabPanePrincipal().getSelectionModel().select(tabCombos);
    }
    
    public void gotoMenuRetiroDinero(){
        this.getLabelTituloVentana().setText("MENU RETIRO DE DINERO");
        this.getLabelShortCut().setText("1-Carga de Retiro de Dinero	|	2-Listado de Retiro de Dinero	|	F11-Retornar a Menú Principal");
        this.getTabPanePrincipal().getSelectionModel().select(tabRetiroDineroMenu);
        this.retiroDineroMenuController.configurarInicio();
    }
    
    public void gotoControlador(){
        this.getLabelTituloVentana().setText("OPERACIONES DE CONTROLADOR");
        this.getLabelShortCut().setText("1-Cancelar Ticket   |   2-Cierre Z  |   3-Cierre X  |   F11-Retornar a Menú Principal");        
        this.configImpresoraController.configurarInicio();
        this.getTabPanePrincipal().getSelectionModel().select(getTabControlador());
    }
    
    public void gotoRetiroDinero(){
        try{
            this.getLabelTituloVentana().setText("RETIRO DE DINERO");
            this.getLabelShortCut().setText("F11 - Retornar a Menú Principal");        
            this.retiroDineroController.configurarInicio();
            this.getTabPanePrincipal().getSelectionModel().select(tabRetiroDinero);
        }catch(TpvException e)    {
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_CARGARETIRODINERO);
            Context.getInstance().currentDMTicket().setException(e);
            gotoError();
        }        
    }
    
    public void gotoRetiroDineroConfirmacion(boolean readOnly){
        try{
            this.retiroDineroConfirmacionController.configurarInicio(readOnly);
            this.getTabPanePrincipal().getSelectionModel().select(tabRetiroDineroConfirmacion);
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_LOGIN);
            Context.getInstance().currentDMTicket().setException(e);
            gotoError();
        }
    }
            

    public void repeatFocus(Node node){
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
                repeatFocus(node);
            }
        });        
    }    
    
    public void setTabPaneModalCommand(TabPaneModalCommand tabPaneModalCommand){
        this.tabPaneModalCommand = tabPaneModalCommand;
    }
    
    public TabPaneModalCommand getTabPaneModalCommand(){
        return this.tabPaneModalCommand;
    }

    public void mostrarMensajeModal(){
        if(getLabelMenssajeModalSuperior().getText().trim().equals("")){
            getLabelMenssajeModalSuperior().setVisible(false);
            gridPaneMensajeSuperior.setPrefHeight(0);
        }else{
            getLabelMenssajeModalSuperior().setVisible(true);
            gridPaneMensajeSuperior.setPrefHeight(170);
        }
            
        this.stackPaneModal.setVisible(true);
        this.repeatFocus(this.getStackPaneModal());
    }
    
    
    public void ocultarMensajeModal(){
        this.stackPaneModal.setVisible(false);
    }
    
    public StackPane getStackPaneModal(){
        return stackPaneModal;
    }
    
    //    @FXML private Label labelMensaje;
    //@FXML private Label labelAceptarModal;
    //@FXML private Label labelCancelarModal;
    public Label getLabelMensaje(){
        return labelMensaje;
    }
    
    public Label getLabelAceptarModal(){
        return labelAceptarModal;
    }
    
    public Label getLabelCancelarModal(){
        return labelCancelarModal;
    }
    
    private void loadImage(){
        //imageDerecha.setImage(new Image(this.getClass().getResource("/com/tpv/resources/logosplash.jpg").toExternalForm()));
        //imageIzquierda.setImage(new Image(this.getClass().getResource("/com/tpv/resources/logosplash.jpg").toExternalForm()));
        imageSuperiorDerecha.setImage(
                new Image(this.getClass().getResource("/com/tpv/resources/LogoLuque.jpg").toExternalForm())
        );
        imageSuperiorIzquierda.setImage(
                new Image(this.getClass().getResource("/com/tpv/resources/LogoLuque.jpg").toExternalForm())
        );
        
        //imageSuperior.fitWidthProperty().bind(this.widthProperty());
        
    } 

    /**
     * @return the labelTituloVentana
     */
    public Label getLabelTituloVentana() {
        return labelTituloVentana;
    }

    /**
     * @return the labelShortCut
     */
    public Label getLabelShortCut() {
        return labelShortCut;
    }

    /**
     * @return the tabPanePrincipal
     */
    public TabPane getTabPanePrincipal() {
        return tabPanePrincipal;
    }

    /**
     * @return the tabControlador
     */
    public Tab getTabControlador() {
        return tabControlador;
    }
    
    
    public void showMsgModal(MensajeModalAbstract mensajeModal){
        getLabelMensaje().setText(mensajeModal.getMensaje());
        getLabelMenssajeModalSuperior().setText(mensajeModal.getMensajeSuperior());
        getLabelTituloVentana().setText(mensajeModal.getTitulo());
        getLabelAceptarModal().setVisible(true);
        getLabelCancelarModal().setVisible(true);
        if(mensajeModal instanceof MensajeModalAceptar){
            getLabelCancelarModal().setVisible(false);
        }
        if(mensajeModal instanceof MensajeModalCancelar){
            getLabelAceptarModal().setVisible(false);
        }
        this.setMensajeModal((MensajeModal)mensajeModal);
        
        this.stackPaneModal.setVisible(true);
        this.repeatFocus(stackPaneModal);
    }
    
    
    
}
