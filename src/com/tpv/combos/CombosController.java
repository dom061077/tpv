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
import com.tpv.service.FacturacionService;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
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
    
    @Inject
    private DataModelTicket modelTicket;
    
    
    private FacturacionService factService = new FacturacionService();
    
    @PostConstruct
    public void init(){
        Platform.runLater(() -> {
            tableViewCombos.setOnKeyPressed(keyEvent->{
                if(keyEvent.getCode()==KeyCode.ESCAPE){
                    volverButton.fire();
                }
            });
        });      
    }
    
    private void verCombos(){
        ObservableList<FacturaDetalleComboData> combosItems = FXCollections.observableArrayList();
        ListProperty<FacturaDetalleComboData> listCombos = new SimpleListProperty<>(combosItems);
        
        try{
            Factura factura = factService.calcularCombos(modelTicket.getIdFactura());
            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
                FacturaDetalleCombo fdc = it.next();
                FacturaDetalleComboData fdcd = new FacturaDetalleComboData(
                            fdc.getCombo().getCodigoCombo(),
                            fdc.getCombo().getDescripcion(),
                            fdc.getCantidad(),
                            fdc.getBonificacion()
                        );
                listCombos.add(fdcd);
                
            }
        }catch(TpvException e){
            log.error("Error en capa controller: "+e.getMessage());
            modelTicket.setOrigenPantalla(OrigenPantallaErrorEnum.PANTALLA_FACTURACION);
            modelTicket.setException(e);
            goToErrorButton.fire();
        }
    }
    
    
}
