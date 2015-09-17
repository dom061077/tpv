/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx8tpv1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 *
 * @author daniel
 */
public class JavaFX8TPV1 extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));
        
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.NONE);
        String css = this.getClass().getResource("caspian.css").toExternalForm(); 
        scene.getStylesheets().add(css);        
        stage.setFullScreen(true); //full screen without borders (no program menu bars)
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
