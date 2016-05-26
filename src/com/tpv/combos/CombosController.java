/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.combos;

import com.tpv.enums.OrigenPantallaErrorEnum;
import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalleCombo;
import com.tpv.principal.DataModelTicket;
import com.tpv.principal.LineaTicketData;
import com.tpv.service.FacturacionService;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.datafx.controller.flow.action.ActionTrigger;

/**
 *
 * @author daniel
 */
public class CombosController{

    Logger log = Logger.getLogger(CombosController.class);
    
    @FXML
    @ActionTrigger("volverFacturacion")
    private Button volverButton;
    
    @FXML
    private TableView tableViewCombos;
    
    @FXML
    private TableColumn codigoColumn;
    
    @FXML
    private TableColumn descripcionColumn;
    
    @FXML
    private TableColumn cantidadColumn;
    
    @FXML
    private TableColumn subTotalColumn;
    
    @FXML
    private Button goToErrorButton;
    
    @FXML
    private Label totalTicket;
    
    @FXML
    private Label totalBonificaciones;
    
    @FXML
    private Label terminaPagando;
    
    private BigDecimal totalBonificado;
    
    
           
    
    @Inject
    private DataModelTicket modelTicket;
    private FacturacionService factService = new FacturacionService();
    
    ObservableList<FacturaDetalleComboData> combosItems = FXCollections.observableArrayList();
    ListProperty<FacturaDetalleComboData> listCombos = new SimpleListProperty<>(combosItems);
    
    
    @PostConstruct
    public void init(){
        Platform.runLater(() -> {
            initTableViewCombos();
            tableViewCombos.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode()==KeyCode.ESCAPE){
                    volverButton.fire();
                }
            });
            traerCombos();
            tableViewCombos.setItems(listCombos);
            
        });      
    }
    
    
    private void initTableViewCombos(){
        codigoColumn.setCellValueFactory(new PropertyValueFactory<FacturaDetalleComboData,Integer>("codigo"));
        codigoColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        descripcionColumn.setCellValueFactory(new PropertyValueFactory("descripcion"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory("cantidad"));
        cantidadColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        subTotalColumn.setCellValueFactory(new PropertyValueFactory("subTotal"));
        subTotalColumn.setCellFactory(col->{
            TableCell<FacturaDetalleComboData,BigDecimal> cell = new TableCell<FacturaDetalleComboData,BigDecimal>(){
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
            TableCell<FacturaDetalleComboData,BigDecimal>cell = new TableCell<FacturaDetalleComboData,BigDecimal>(){
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
        
    }
    
    
    private void traerCombos(){
        
        try{
            Factura factura = factService.calcularCombos(modelTicket.getIdFactura());
            totalBonificado = BigDecimal.ZERO;
            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
                FacturaDetalleCombo fdc = it.next();
                FacturaDetalleComboData fdcd = new FacturaDetalleComboData(
                            fdc.getCombo().getCodigoCombo(),
                            fdc.getCombo().getDescripcion(),
                            fdc.getCantidad(),
                            fdc.getBonificacion()
                        );
                totalBonificado = totalBonificado.add(fdc.getBonificacion());
                listCombos.add(fdcd);
                DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                totalBonificaciones.setText(df.format(totalBonificado));
                totalTicket.setText(df.format(modelTicket.getTotalTicket()));
                terminaPagando.setText(df.format(modelTicket.getTotalTicket().subtract(totalBonificado)));
                
            }
        }catch(TpvException e){
            log.error("Error en capa controller: "+e.getMessage());
            modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            modelTicket.setException(e);
            goToErrorButton.fire();
        }
    }
    
    
}
