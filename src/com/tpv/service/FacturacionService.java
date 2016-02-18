/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.principal.LineaTicketData;
import com.tpv.util.Connection;
import java.math.BigDecimal;
import javafx.beans.property.ListProperty;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author daniel
 */
public class FacturacionService  {
    Logger log = Logger.getLogger(FacturacionService.class);
    
    
    public void registrarFactura(Factura factura, ListProperty<LineaTicketData> detalle)throws TpvException{
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        try{
            if(!tx.isActive())
               tx.begin();     
                em.persist(factura);
                
                
//            detalle.forEach(item->{
                FacturaDetalle facturaDetalle = new FacturaDetalle();
                facturaDetalle.setFactura(factura);
                facturaDetalle.setCantidad(10);//(item.getCantidad());
                facturaDetalle.setSubTotal(new  BigDecimal(12));//(item.getSubTotal());
                factura.getDetalle().add(facturaDetalle);
                em.persist(facturaDetalle);
//            });
//                
            tx.commit();
            em.clear();        
        }catch(Exception e){
            String fullTraceStr=e.getMessage()+"\n";
            for(int i=0;i<=e.getStackTrace().length-1;i++){
                fullTraceStr+="Clase: "+e.getStackTrace()[i].getClassName()+"; "
                        +"Archivo: "+e.getStackTrace()[i].getFileName()+"; "
                        +"Mètodo: "+e.getStackTrace()[i].getMethodName()+"; "
                        +"Nro. Línea: "+e.getStackTrace()[i].getLineNumber()+"; "
                        +"\n";
            }
            log.error("Error grave: "+fullTraceStr);
            throw new TpvException(fullTraceStr);
        }
        
    }
}
