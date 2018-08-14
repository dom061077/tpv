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
public abstract class MensajeModalAbstract implements MensajeModalInterface {

    /**
     * @return the mensajeSuperior
     */
    public String getMensajeSuperior() {
        return mensajeSuperior;
    }

    /**
     * @param mensajeSuperior the mensajeSuperior to set
     */
    public void setMensajeSuperior(String mensajeSuperior) {
        this.mensajeSuperior = mensajeSuperior;
    }

    /**
     * @return the node
     */
    public Node getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(Node node) {
        this.node = node;
    }

    public MensajeModalAbstract(String titulo,String mensaje, String mensajeSuperior, Node node){
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.mensajeSuperior = mensajeSuperior;
        this.node = node;
    }
    
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
    private String mensajeSuperior;
    private Node node;
    @Override
    public void aceptarMensaje(){
        
    }
    
    @Override
    public void cancelarMensaje(){
        
    }
}
