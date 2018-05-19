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
import com.tpv.service.UsuarioService;
import com.tpv.service.UtilidadesService;
import com.tpv.supervisor.SupervisorController;
import com.tpv.util.Connection;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;

/**
 *
 * @author daniel
 */

public class TabPanePrincipalController implements Initializable {
    Logger log = Logger.getLogger(TabPanePrincipalController.class);
    UsuarioService usuarioService = new UsuarioService();        
    
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
    
    @FXML private CombosController combosController; 
    
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
    
    @FXML private TabPane tabPanePrincipal;
    
    
    
    
    public Button getButtonMenuPrincipal(){
        return buttonMenuPrincipal;
    }
            
    
    //@Override
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.loginController.getPassword().requestFocus();
        
        
        this.loginController.setTabController(this);
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
        try{
            initParametrosGenerale();
            initImpresora();
            gotoLogin();            
        }catch(TpvException e){
            log.error(e.getMessage());
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_LOGIN);
            this.gotoError();
        }

        tabPanePrincipal.getSelectionModel().selectedItemProperty()
                .addListener((observable,oldTab,newTab)->{
                   if(newTab.getId().compareTo("tabMenuPrincipal")==0){
                       //menuPrincipalController.setMenuFocus();
                   }
                });
        
    }      
    
    @FXML
    private void handleButtonMenuPrincipal(ActionEvent event){
        System.out.println("IR AL MENU PRINCIPAL");
    }   
    
    private void initImpresora() throws TpvException{
            Connection.initFiscalPrinter();
        
    }
    
    private void initParametrosGenerale() throws TpvException{
        List<ParametroGeneral> list = UtilidadesService.getParametroGral();
        list.forEach(param->{
            if(param.getId().compareTo("RETENCION_ING_BRUTO_LEYENDA")==0)
                Context.getInstance().setLeyendaRetIngBrutosCliente(param.getParametroCadena());
            if(param.getId().compareTo("RETENCION_ING_BRUTO_MONTO_MINIMO")==0)
                Context.getInstance().setMontoMinRetIngBrutos(param.getParametroNumerico());
            if(param.getId().compareTo("INTERES_IVA_TARJETA")==0)
                Context.getInstance().setPorcentajeIvaTarjeta(param.getParametroNumerico());
        });
        
        

    }
    

    
    public void gotoLogin(){
        try{
            this.loginController.configurarInicio();
            this.tabPanePrincipal.getSelectionModel().select(tabLogin);
        }catch(TpvException e){
            log.error(e.getMessage());
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_LOGIN);
            gotoError();
        }
            
       
    }
    
    public void gotoMenuPrincipal(){
        this.tabPanePrincipal.getSelectionModel().select(tabMenuPrincipal);
        
        this.menuPrincipalController.configurarInicio();
    }
    
    public void gotoFacturacion(){
        try{
            this.facturacionController.configurarInicio();
            this.tabPanePrincipal.getSelectionModel().select(tabFacturacion);
        }catch(TpvException e){
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            Context.getInstance().currentDMTicket().setException(e);
            gotoError();
        }    
        
    }
    
    public void gotoError(){
        this.errorController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabError);
    }
    
    public void gotoSupervisor(){
        this.supervisorController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabSupervisor);
        
    }
    
    public void gotoCliente(){
        this.buscarPorNombreClienteController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabCliente);
    }
    
    public void gotoProducto(){
        this.productoController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabProducto);
    }
    
    public void gotoConfirmarPago(){
        this.confirmaPagoController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabConfirmarPago);
    }
    
    public void gotoPago(){
        this.pagoTicketController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabPago);
    }
    
    public void gotoCombos(){
        this.combosController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabCombos);
    }
    
    public void gotoControlador(){
        this.configImpresoraController.configurarInicio();
        this.tabPanePrincipal.getSelectionModel().select(tabControlador);
    }
            

    public void repeatFocus(Node node){
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
                repeatFocus(node);
            }
        });        
    }    
    
    
}
