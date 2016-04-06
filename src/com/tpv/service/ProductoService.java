/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.modelo.GrupoProducto;
import com.tpv.modelo.ListaPrecioProducto;
import com.tpv.modelo.Producto;
import com.tpv.util.Connection;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
        tx.commit();
        em.clear();
        //em.close();
        return productos;
    }
    
    public List getProductosPrecio(String filtro){
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        List<ListaPrecioProducto> productosPrecios=null;
        if(!tx.isActive())
            tx.begin();
        Query q = em.createQuery("FROM ListaPrecioProducto lpp WHERE "
                +" lpp.producto.descripcion like :filtro"
            ).setParameter("filtro", "%"+filtro+"%");
        q.setFirstResult(0);
        q.setMaxResults(100);
        productosPrecios = q.getResultList();
        
        tx.commit();
        em.clear();
        return productosPrecios;
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
        Query q = em.createQuery("FROM ListaPrecioProducto lpp WHERE"
                +" lpp.producto.discontinuado = 0"
                +" and lpp.producto.codigoProducto = :codigoProducto").setParameter("codigoProducto", filtroCodigo);
        try{
            lstPrecioProducto = (ListaPrecioProducto)q.getSingleResult();
            precio = lstPrecioProducto.getPrecioFinal();
        }catch(NoResultException e){
            
        }catch(NonUniqueResultException e){
            
        }catch(Exception e){
            
        }
        
        
        tx.commit();
        precio = precio.setScale(2,BigDecimal.ROUND_HALF_EVEN);
        return precio;
    }
    
    public BigDecimal getPrecioProducto(ListaPrecioProducto lstPrecioProducto){
        BigDecimal precio=new BigDecimal(0);
//        if(lstPrecioProducto.getFechaInicioEspecial().compareTo(fechaHoy)<=0 &&
//                lstPrecioProducto.getFechaFinEspecial().compareTo(fechaHoy)>=0){
//            precio = lstPrecioProducto.getPrecioEspecial();
//        }else{
//            if(lstPrecioProducto.getFechaInicioOferta().compareTo(fechaHoy)<=0 &&
//                lstPrecioProducto.getFechaFinOferta().compareTo(fechaHoy)>=0)
//                precio = lstPrecioProducto.getPrecioOferta();
//            else
//                precio = lstPrecioProducto.getPrecioPublico();
//        }
//        
        return precio;
    }

    /*public void ConsultaGrupo(){
        EntityManager em = Connection.getEm();
        EntityTransaction tx = em.getTransaction();
        if(!tx.isActive())
            tx.begin();
        Query q = em.createQuery("FROM GrupoProducto gp");
        List<GrupoProducto> lista = null;
        try{
            lista = q.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        lista.forEach(item ->{
            System.out.println("Grupo: "+item.getDescripcion()
                    +"      Padre: "+item.getGrupoPadre().getDescripcion()
            );
        });
        tx.commit();
        em.clear();
        
    }*/
    
    
}
