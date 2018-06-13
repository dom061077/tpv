/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.retirodinero;

import com.tpv.util.ui.MaskTextField;
import com.tpv.util.ui.TabPaneModalCommand;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx8tpv1.TabPanePrincipalController;
import org.apache.log4j.Logger;

/**
     *
 * @author COMPUTOS
 */
public class RetiroDineroController implements Initializable,TabPaneModalCommand {
    Logger log = Logger.getLogger(RetiroDineroController.class);
    private TabPanePrincipalController tabController;
    private MaskTextField textField1000 ;
    private MaskTextField textField500;
    private MaskTextField textField200;
    private MaskTextField textField100;
    private MaskTextField textField50;
    
    @FXML BorderPane borderPane;
    @FXML GridPane gridPane;
    
    @FXML
    public  void initialize(URL url, ResourceBundle rb) {    
        log.info("Ingreso la metodo initialize ");
        
        initIngresos();
        
        textField1000.setOnKeyPressed(keyEvent->{
            if(keyEvent.getCode()==KeyCode.F11){
                this.tabController.gotoMenuPrincipal();
            }
            if(keyEvent.getCode() == KeyCode.ENTER){
                this.tabController.repeatFocus(textField500);
            }
            if(keyEvent.getCode() == KeyCode.TAB)
                keyEvent.consume();
            
        });
        textField500.setOnKeyPressed(keyEvent->{
            if(keyEvent.getCode()==KeyCode.F11){
                this.tabController.gotoMenuPrincipal();
            }
            if(keyEvent.getCode() == KeyCode.ENTER){
                this.tabController.repeatFocus(textField200);
            }
            
            if(keyEvent.getCode() == KeyCode.TAB)
                keyEvent.consume();
            
            if(keyEvent.getCode() == KeyCode.ESCAPE)
                this.tabController.repeatFocus(textField1000);
            
        });
        
        textField200.setOnKeyPressed(keyEvent->{
            if(keyEvent.getCode()==KeyCode.F11){
                this.tabController.gotoMenuPrincipal();
            }
            if(keyEvent.getCode() == KeyCode.ENTER){
                this.tabController.repeatFocus(textField100);
            }
            if(keyEvent.getCode() == KeyCode.TAB)
                keyEvent.consume();
            
            if(keyEvent.getCode() == KeyCode.ESCAPE)
                this.tabController.repeatFocus(textField500);
            
            
        });
        
        textField100.setOnKeyPressed(keyEvent->{
            if(keyEvent.getCode()==KeyCode.F11){
                this.tabController.gotoMenuPrincipal();
            }
            if(keyEvent.getCode() == KeyCode.ENTER){
                this.tabController.repeatFocus(textField50);
            }
            if(keyEvent.getCode() == KeyCode.TAB)
                keyEvent.consume();
            
            if(keyEvent.getCode() == KeyCode.ESCAPE)
                this.tabController.repeatFocus(textField200);
            
        });
        
        textField50.setOnKeyPressed(keyEvent->{
            if(keyEvent.getCode()==KeyCode.F11){
                this.tabController.gotoMenuPrincipal();
            }
            if(keyEvent.getCode() == KeyCode.TAB)
                keyEvent.consume();
            if(keyEvent.getCode() == KeyCode.ESCAPE)
                this.tabController.repeatFocus(textField100);
            if(keyEvent.getCode() == KeyCode.ENTER){
                this.tabController.mostrarMensajeModal();
                this.tabController.repeatFocus(this.tabController.getStackPane());
            }
            
        });
        
    }

    public void configurarInicio(){
        this.tabController.repeatFocus(textField1000);
        this.tabController.setTabPaneModalCommand(this);
    }
    
    public void setTabController(TabPanePrincipalController tabController){
        this.tabController=tabController;
    }
    
    
    private void initIngresos(){
        textField1000 = new MaskTextField();
        textField1000.setMask("N!");
        textField1000.getStyleClass().add("textfield_sin_border");
        textField1000.setPrefWidth(150);
        textField1000.setMaxWidth(150);
        textField1000.setStyle("-fx-alignment: CENTER-RIGHT;");
        gridPane.add(textField1000,1,0);
        
        
        textField500 = new MaskTextField();
        textField500.setMask("N!");
        textField500.getStyleClass().add("textfield_sin_border");
        textField500.setPrefWidth(150);
        textField500.setMaxWidth(150);
        textField500.setStyle("-fx-alignment: CENTER-RIGHT;");
        gridPane.add(textField500,1,1);
        
        textField200 = new MaskTextField();
        textField200.setMask("N!");
        textField200.getStyleClass().add("textfield_sin_border");
        textField200.setPrefWidth(150);
        textField200.setMaxWidth(150);
        textField200.setStyle("-fx-alignment: CENTER-RIGHT;");
        gridPane.add(textField200,1, 2);
        
        textField100 = new MaskTextField();
        textField100.setMask("N!");
        textField100.getStyleClass().add("textfield_sin_border");
        textField100.setPrefWidth(150);
        textField100.setMaxWidth(150);
        textField100.setStyle("-fx-alignment: CENTER-RIGHT;");
        gridPane.add(textField100,1, 3);
        
        textField50 = new MaskTextField();
        textField50.setMask("N!");
        textField50.getStyleClass().add("textfield_sin_border");
        textField50.setPrefWidth(150);
        textField50.setMaxWidth(150);
        textField50.setStyle("-fx-alignment: CENTER-RIGHT;");
        gridPane.add(textField50,1, 4);
    }
    
    public void aceptarMensajeModal(){
        this.tabController.ocultarMensajeModal();
        this.tabController.repeatFocus(this.textField1000);
    }
    
    public void cancelarMensajeModal(){
        this.tabController.ocultarMensajeModal();
        this.tabController.repeatFocus(this.textField1000);
    }
    
    
    
    
}
