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
 * @author daniel
 */

@Entity
@Table(name="proveedores_productos")
public class ProveedorProducto {
    @Embeddable
    public static class Id implements Serializable{
        @Column(name = "idProveedor")
        private Long idProveedor;
        
        @Column(name = "idPRODUCTOS")
        private Long idProducto;
        
        public Id(){
        }
        
        public Id(Long idProveedor,Long idProducto){
            this.idProducto = idProducto;
            this.idProveedor = idProveedor;
        }
        
        public int hashCode(){
            return Long.hashCode(idProducto)+Long.hashCode(idProveedor);
        }
    }

    @EmbeddedId
    private Id id = new Id();
    
}
