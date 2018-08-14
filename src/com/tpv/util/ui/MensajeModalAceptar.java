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
public class MensajeModalAceptar extends MensajeModalAbstract {
    
    public MensajeModalAceptar(String titulo, String mensaje, String mensajeSuperior,Node node){
        super(titulo,mensaje,mensajeSuperior,node);
    }

    @Override
    public void aceptarMensaje(){
        
    }    
}
