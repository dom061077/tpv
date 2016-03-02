/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx8tpv1;

import com.tpv.modelo.ListaPrecioProducto;
import com.tpv.util.Connection;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author daniel
 */
public class TestHibernate {
    
    public static void main(String[] args){
        try{
            Connection.initConnections();
        }catch(Exception e){
            
        }
        EntityManager em = Connection.getEm();
        Query q = em.createQuery("SELECT lpp,current_date() as FechaHoy FROM ListaPrecioProducto lpp "
                +" WHERE lpp.producto.codigoProducto = :codigoProducto").setParameter("codigoProducto",1003);
        Object o[]=null;
        try{
            o = (Object[])q.getSingleResult();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Descripcion de producto: "+((ListaPrecioProducto)o[0]).getProducto().getDescripcion());
    }
    
}
