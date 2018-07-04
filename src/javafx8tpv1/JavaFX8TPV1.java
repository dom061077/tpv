/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx8tpv1;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.principal.Context;
import com.tpv.util.Connection;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader.StateChangeNotification;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
//import org.datafx.samples.app.MasterViewController;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


/**
 *
 * @author daniel
 */
public class JavaFX8TPV1 extends Application {
    Logger log = Logger.getLogger(JavaFX8TPV1.class);
    BooleanProperty ready = new SimpleBooleanProperty(false);
    @Override
    public void start(Stage stage) throws Exception {
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            Alert alert = new Alert(AlertType.ERROR,"Error no controlado: "+throwable.getMessage());
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(stage);
            alert.showAndWait();
            alert.getDialogPane().getStylesheets().add(Connection.getCss());
            log.error("Excepción no controlada",throwable);
        });   
        
        DOMConfigurator.configure(getClass().getResource("log4j.xml"));
        
       //ejecutarSpooler();

       stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
               Platform.exit();
               System.exit(0);
            }
       });
        try{
            Connection.initFiscalPrinter();
        }catch(TpvException e){
            log.error(e.getMessage());
            Context.getInstance().currentDMTicket().setException(e);
            Context.getInstance().currentDMTicket().setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_LOGIN);
        }        
        
        try{
            log.debug("INICIANDO LAS CONEXIONES");
            log.debug("PATH DEL DIRECTORIO DE RECURSOS: "+this.getClass().getResource("/com/tpv/resources/people.png"));
            Connection.initConnections();
            
        }   catch(Exception e){
            
            log.error("Error general de conexiòn a la base de datos",e);
            
        }
        
       
        
        
        
        /*Runnable task = () -> {
            while(true){
                try{
                    Thread.sleep(500);
                    if(!Connection.isDBConnected() && Connection.getButtonFlowFire()!=null){
                        log.debug("Dispara evento de fallo de conexión");
                        Connection.fireButtonEvent();
                    }
                        
                    
                }catch(InterruptedException e){
                    log.info("Error en pausa de monitor de comunicaciones");
                }
                
            }
        };
        
        Thread thread = new Thread(task);
        thread.start();
        */
        
        Parent root = FXMLLoader.load(getClass().getResource("TabPanePrincipal.fxml"));
      
        /*Flow flow = new Flow(FXMLMainController.class).withLink(FXMLMainController.class
                , "buscarCliente", ClienteSceneController.class).withLink(
                        ClienteSceneController.class, "seleccionarCliente", FXMLMainController.class);
        */
        
        
        /*Flow flow = new Flow(LoginController.class)
                    //--flow ventana buscar cliente
                   .withLink(LoginController.class,"iniciarSesion",MenuPrincipalController.class)
                   .withLink(LoginController.class,"mostrarError",ErrorController.class)
                   .withLink(MenuPrincipalController.class, "facturacion", FXMLMainController.class)
                   .withLink(MenuPrincipalController.class,"configimpresora",ConfiguracionImpresoraController.class)
                   .withLink(ConfiguracionImpresoraController.class,"volverMenuPrincipal",MenuPrincipalController.class)
                   .withLink(MenuPrincipalController.class, "mostrarError", ErrorController.class)
                   .withLink(ErrorController.class,"volverMenuPrincipal",MenuPrincipalController.class)
                   .withLink(FXMLMainController.class,"buscarCliente", BuscarPorNombreClienteController.class)
                   .withLink(FXMLMainController.class, "volverMenuPrincipal", MenuPrincipalController.class)
                   .withLink(BuscarPorNombreClienteController.class, "seleccionarCliente", FXMLMainController.class)
                    //  flow ventana buscar por descripcion de producto
                   .withLink(FXMLMainController.class,"buscarProducto",BuscarPorDescProductoController.class)
                   .withLink(BuscarPorDescProductoController.class, "seleccionarProducto", FXMLMainController.class)
                   .withLink(FXMLMainController.class, "pagoTicket", PagoTicketController.class)
                   .withLink(PagoTicketController.class,"volverFacturacion",FXMLMainController.class)
                   .withLink(PagoTicketController.class,"mostrarError",ErrorController.class)
                   .withLink(FXMLMainController.class,"mostrarError",ErrorController.class)
                   .withLink(ErrorController.class,"facturacion",FXMLMainController.class)
                   .withLink(PagoTicketController.class, "confirmarTicket", ConfirmaPagoTicketController.class)
                   .withLink(ConfirmaPagoTicketController.class,"facturacion", FXMLMainController.class)
                   .withLink(FXMLMainController.class, "combos", CombosController.class)
                   .withLink(CombosController.class,"volverFacturacion",FXMLMainController.class)
                   .withLink(ConfirmaPagoTicketController.class,"volverPagoTicket", PagoTicketController.class)
                   .withLink(ConfirmaPagoTicketController.class,"mostrarError",ErrorController.class)
                   .withLink(FXMLMainController.class,"habilitarSupervisor",SupervisorController.class)
                   .withLink(SupervisorController.class,"volverFacturacion", FXMLMainController.class)
                   .withLink(SupervisorController.class, "mostrarError", ErrorController.class )
                   .withLink(ErrorController.class,"menuprincipal", MenuPrincipalController.class)
                   .withLink(ErrorController.class,"confirmarticket",ConfirmaPagoTicketController.class)
                   .withLink(ErrorController.class,"pagoticket",PagoTicketController.class)
                   .withLink(ErrorController.class,"buscarproducto", BuscarPorDescProductoController.class)
                   .withLink(ErrorController.class,"buscarcliente",BuscarPorNombreClienteController.class)
                   .withLink(ErrorController.class,"login",LoginController.class)
                   .withLink(ErrorController.class,"error_supervisor",SupervisorController.class);*/
        
                   
        //StackPane root = flow.start();   
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //scene.setCursor(Cursor.NONE);
        String css = this.getClass().getResource("caspian.css").toExternalForm(); 
        scene.getStylesheets().add(css);        
        
        //stage.setFullScreen(true); //full screen without borders (no program menu bars)
        
        stage.setFullScreenExitHint(""); //Don't show "Press ESC to exit full screen"
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        
        
        
        ready.setValue(Boolean.TRUE);
        notifyPreloader(new StateChangeNotification(
                 StateChangeNotification.Type.BEFORE_START));
        
        
        stage.show();

        
    }

    private void ejecutarSpooler(){
         try {
            log.info("Antes de ejecutar spooler");
            Process process = Runtime
                    .getRuntime()
                    .exec("sudo -S <<< \"exito\" /home/tpv1/TPV/./spooler -pttyS0 -k");
            InputStream inputstream = process.getInputStream();
            log.info("Despues de ejecutar spooler");           
        } catch (IOException e) {
            log.error(e);
        }       
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
