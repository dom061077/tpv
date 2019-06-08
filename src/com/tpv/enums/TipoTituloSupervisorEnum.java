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
    CANCELAR_TICKET("Cancelar Ticket",2),
    CANCELAR_NOTADC("Cancelar Doc. No Fiscal Homologado",3),
    HABILITAR_MENU("Ir a Menu",4),
    HABILITAR_CONTROLADOR("Habilitar Controlador",5),
    HABILITAR_CONFIRMARETIRODINERO("Confirmar Retiro de Dinero",6),
    CANCELAR_PAGO("Cancelar Pago",7),
    CANCELAR_CONFIRMACION_PAGO("Cancelar Confirmación Pago",8),
    HABILITAR_MENU_NOTASDC("Menú Notas D/C",9);
    ;

    
    
    
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
