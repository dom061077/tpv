/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.modelo.ListaPrecioProducto;
import com.tpv.modelo.Producto;
import com.tpv.util.Connection;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 * Clase de servicio de Producto, desde aqui se hacen todas las operaciones 
 * con la base de datos hacia productos
 * <p>
 * 
 * 
 * 
 */
public class ProductoService {
    Logger log = Logger.getLogger(ProductoService.class);
    
    
    
    /**
     * Este método se usa para traer todos los productos segun el parametro 
     * de filtro.
     * @param filtro es el filtro que se va aplicar para el listado de productos.
     * Se va a usar para filtrar el codigo o la descripción del producto.
     * 
     * 
    */
    public List getProductos(String filtro){
        log.info("Filtro de búsqueda: "+filtro);
        List<Producto> productos;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        if(!tx.isActive())
           tx.begin();
        int codigoProducto=0;
        
        try{
            codigoProducto = Integer.parseInt(filtro);
        }catch(Exception e){
            
        }
        if(filtro==null)
            filtro="";
        if(codigoProducto>0){
            log.info("Busqueda de productos por código");
            Query q = em.createQuery("FROM Producto p");
            q.setFirstResult(0);
            q.setMaxResults(100);
            productos = em.createQuery("FROM Producto p WHERE p.codigoProducto = :codigoProducto").setParameter("codigoProducto", codigoProducto).getResultList();
            productos = q.getResultList();
        }else{
            log.info("Busqueda de productos por descripción o código de barra");
            Query q = em.createQuery("FROM Producto p WHERE p.descripcion like  :detalleSuc");
            q.setParameter("detalleSuc", "%"+filtro+"%");
            q.setFirstResult(0);
            q.setMaxResults(100);
            productos = q.getResultList();
            
        }
        
        
        //List usuarios = em.createQuery("FROM Producto p WHERE p. = :nombre").setParameter("nombre", nombre).getResultList();
        
        

/*        if (usuarios.size()>0) {
                    Usuario usuario = (Usuario)usuarios.get(0);
            if(usuario.getPassword().equals(password))
                flagReturn=true;
        }else
            flagReturn=false;*/
        
        tx.commit();
        
        
        em.clear();
        //em.close();
        return productos;
    }
    
    public Producto getProductoPorCodigo(int filtroCodigo){
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        if(!tx.isActive())
            tx.begin();
        Query q = em.createQuery("FROM Producto p WHERE p.discontinuado = 0 and p.codigoProducto = :codigoProducto").setParameter("codigoProducto", filtroCodigo);
        
        Producto producto = null;
        try{
            producto = (Producto)q.getSingleResult();
        }catch(NoResultException e){
            
        }catch(NonUniqueResultException e){
            
        }catch(Exception e){
            
        }finally{
            
        }
        tx.commit();
        em.clear();
        //em.close();
        
        return producto;
    }
    
    public Producto getProductoPorCodBarra(String codigoBarra){
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query q = em.createQuery("FROM Producto p WHERE p.discontinuado = 0 and p.codBarra = :codBarra").setParameter("codBarra", codigoBarra);
        Producto producto = (Producto)q.getSingleResult();
        tx.commit();
        em.clear();
        em.close();
        
        return producto;
    }
    
    
    public BigDecimal getPrecioProducto(int filtroCodigo){
        ListaPrecioProducto lstPrecioProducto=null;
        BigDecimal precio = new BigDecimal(0);
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        if(!tx.isActive())
            tx.begin();
        //traigo la lista por fecha de precio de oferta
        Query q = em.createQuery("SELECT lpp,current_date() FROM ListaPrecioProducto lpp where lpp.producto.discontinuado = 0"
                +" and lpp.producto.codigoProducto = :codigoProducto").setParameter("codigoProducto",filtroCodigo);
        ListaPrecioProducto listaPrecio;
        java.sql.Date fechaHoy;
        
        try{
            Object[] resultado = (Object[])q.getSingleResult();
            if(resultado.length>0){
               listaPrecio = ((ListaPrecioProducto)resultado[0]);
               fechaHoy = (java.sql.Date)resultado[1];
               if(listaPrecio.getFechaInicioEspecial().compareTo(fechaHoy)<=0 &&
                       listaPrecio.getFechaFinEspecial().compareTo(fechaHoy)>=0){
                   precio = listaPrecio.getPrecioEspecial();
               }else{
                   if(listaPrecio.getFechaInicioOferta().compareTo(fechaHoy)<=0 &&
                       listaPrecio.getFechaFinOferta().compareTo(fechaHoy)>=0)
                       precio = listaPrecio.getPrecioOferta();
                   else
                       precio = listaPrecio.getPrecioPublico();
               }
            }
        }catch(NoResultException e){
            
        }catch(NonUniqueResultException e){    
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            
        }
        
        tx.commit();
        
        return precio;
    }

    
    
}
