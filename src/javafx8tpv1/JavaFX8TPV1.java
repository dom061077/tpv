/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx8tpv1;

import com.tpv.cliente.ClienteSceneController;
import com.tpv.login.ErrorController;
import com.tpv.login.LoginController;
import com.tpv.pagoticket.PagoTicketController;
import com.tpv.principal.FXMLMainController;
import com.tpv.principal.MenuPrincipalController;
import com.tpv.producto.BuscarPorDescProductoController;
import com.tpv.util.Connection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.datafx.controller.flow.Flow;
//import org.datafx.samples.app.MasterViewController;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


/**
 *
 * @author daniel
 */
public class JavaFX8TPV1 extends Application {
    Logger log = Logger.getLogger(JavaFX8TPV1.class);
    @Override
    public void start(Stage stage) throws Exception {
        
       stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
               Platform.exit();
               System.exit(0);
            }
       });
        
        DOMConfigurator.configure(getClass().getResource("log4j.xml"));
        try{
            log.debug("INICIANDO LAS CONEXIONES");
            Connection.initConnections();
            
        }   catch(Exception e){
            
            log.info("Error general de conexiòn");
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
        
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));
      
        /*Flow flow = new Flow(FXMLMainController.class).withLink(FXMLMainController.class
                , "buscarCliente", ClienteSceneController.class).withLink(
                        ClienteSceneController.class, "seleccionarCliente", FXMLMainController.class);
        */
        log.debug("Inicializando Flow de infaces gráficas");
        Flow flow = new Flow(LoginController.class).withLink(LoginController.class, "goToError", ErrorController.class)
                    //--flow ventana buscar cliente
                   .withLink(LoginController.class,"iniciarSesion",MenuPrincipalController.class)
                   .withLink(MenuPrincipalController.class, "facturacion", FXMLMainController.class)
                   .withLink(FXMLMainController.class,"buscarCliente", ClienteSceneController.class)
                   .withLink(FXMLMainController.class, "volverMenuPrincipal", MenuPrincipalController.class)
                   .withLink(ClienteSceneController.class, "seleccionarCliente", FXMLMainController.class)
                    //  flow ventana buscar por descripcion de producto
                   .withLink(FXMLMainController.class,"buscarProducto",BuscarPorDescProductoController.class)
                   .withLink(BuscarPorDescProductoController.class, "seleccionarProducto", FXMLMainController.class)
                   .withLink(FXMLMainController.class, "pagoTicket", PagoTicketController.class)
                   .withLink(PagoTicketController.class,"volverpantallaprincipal",FXMLMainController.class);
        StackPane root = flow.start();   
        
        Scene scene = new Scene(root);
        
        //scene.setCursor(Cursor.NONE);
        String css = this.getClass().getResource("caspian.css").toExternalForm(); 
        scene.getStylesheets().add(css);        
        //stage.setFullScreen(true); //full screen without borders (no program menu bars)
        stage.setFullScreenExitHint(""); //Don't show "Press ESC to exit full screen"
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
