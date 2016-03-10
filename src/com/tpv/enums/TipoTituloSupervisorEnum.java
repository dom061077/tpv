/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.enums;

/**
 *
 * @author daniel
 */
public enum TipoTituloSupervisorEnum {
    HABILITAR_NEGATIVO("Habilitar Negativos",1),
    CANCELAR_TICKET("Cancelar Ticket",2);
    
    
    private String titulo;
    private int tituloNum;
    
    private TipoTituloSupervisorEnum(String titulo, int tituloNum){
        this.titulo = titulo;
        this.tituloNum = tituloNum;
    }
    
    public String getTitulo(){
        return this.titulo;
    }
    
    public int getTituloNum(){
        return tituloNum;
    }
            
    
}
