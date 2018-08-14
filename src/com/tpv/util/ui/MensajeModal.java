/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util.ui;

import javafx.scene.Node;

/**
 *
 * @author COMPUTOS
 */
public class MensajeModal extends MensajeModalAbstract {
    
    
    public MensajeModal(String titulo, String mensaje, String mensajeSuperior,Node node){
        super(titulo, mensaje,mensajeSuperior,node);
    }
    
    @Override
    public void aceptarMensaje(){
        
    }
    
    @Override
    public void cancelarMensaje(){
        
    }    
}
