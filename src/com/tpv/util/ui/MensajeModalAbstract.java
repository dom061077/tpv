/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util.ui;

/**
 *
 * @author COMPUTOS
 */
public abstract class MensajeModalAbstract implements MensajeModalInterface {

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje the mensaje to set
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    private String titulo;
    private String mensaje;
    @Override
    public void aceptarMensaje(){
        
    }
    
    @Override
    public void cancelarMensaje(){
        
    }
}
