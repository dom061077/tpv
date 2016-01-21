/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx8tpv1;

import com.tpv.cliente.ClienteSceneController;
import com.tpv.login.LoginController;
import com.tpv.pagoticket.PagoTicketController;
import com.tpv.principal.FXMLMainController;
import com.tpv.producto.BuscarPorDescProductoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;
//import org.datafx.samples.app.MasterViewController;

/**
 *
 * @author daniel
 */
public class JavaFX8TPV1 extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));
      
        /*Flow flow = new Flow(FXMLMainController.class).withLink(FXMLMainController.class
                , "buscarCliente", ClienteSceneController.class).withLink(
                        ClienteSceneController.class, "seleccionarCliente", FXMLMainController.class);
        */
        Flow flow = new Flow(LoginController.class).withLink(LoginController.class, "iniciarSesion", FXMLMainController.class)
                    //--flow ventana buscar cliente
                   .withLink(FXMLMainController.class,"buscarCliente", ClienteSceneController.class)
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
