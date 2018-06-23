/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author COMPUTOS
 */

@Entity
@Table(name="usuarios_perfiles")
public class UsuarioPerfil {
    
    @Embeddable
    public static class Id implements Serializable{
        @Column(name = "idUSUARIO")
        private int usuarioId;
        
        @Column(name="idPERFIL")
        private String perfilId;
        public Id(){
            
        }
        
        public Id(int usuarioId,String perfilId){
            this.usuarioId = usuarioId;
            this.perfilId = perfilId;
        }
        
        public boolean equals(Object o){
            if (o != null && o instanceof Id){
                Id that = (Id)o;
                return this.usuarioId == that.usuarioId &&
                        this.perfilId == that.perfilId;
            }else{
                return false;
            }
        }
        
        public int hashCode(){
            return Long.hashCode(usuarioId)+perfilId.hashCode();
        }
        
        
    }
    @EmbeddedId
    private Id id = new Id();    
    
    
}
