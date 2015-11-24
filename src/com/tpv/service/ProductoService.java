/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.modelo.Producto;
import com.tpv.modelo.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Clase de servicio de Producto, desde aqui se hacen todas las operaciones 
 * con la base de datos hacia productos
 * <p>
 * 
 * 
 * 
 */
public class ProductoService {
    
    /**
     * Este método se usa para traer todos los productos segun el parametro 
     * de filtro.
     * @param filtro es el filtro que se va aplicar para el listado de productos.
     * Se va a usar para filtrar el codigo o la descripción del producto.
     * 
     * 
    */
    public List getProductos(String filtro){
        List<Producto> productos;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tpvpersistence");        
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        int codigoProducto=0;
        
        try{
            codigoProducto = Integer.parseInt(filtro);
        }catch(Exception e){
            
        }
        if(filtro==null)
            filtro="";
        if(codigoProducto>0){
            Query q = em.createQuery("FROM Producto p");
            q.setFirstResult(0);
            q.setMaxResults(100);
            productos = em.createQuery("FROM Producto p WHERE p.codigoProducto = :codigoProducto").setParameter("codigoProducto", codigoProducto).getResultList();
            productos = q.getResultList();
        }else{
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
        em.close();
        emf.close();
        
        return productos;
    }
    
    public Producto getProductoPorCodigo(int filtroCodigo){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tpvpersistence");        
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query q = em.createQuery("FROM Producto p WHERE p.discontinuado = 0 and p.codigoProducto = :codigoProducto").setParameter("codigoProducto", filtroCodigo);
        Producto producto = (Producto)q.getSingleResult();
        tx.commit();
        em.clear();
        em.close();
        emf.close();
        
        return producto;
    }
    
    public Producto getProductoPorCodBarra(String codigoBarra){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tpvpersistence");        
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query q = em.createQuery("FROM Producto p WHERE p.discontinuado = 0 and p.codBarra = :codBarra").setParameter("codBarra", codigoBarra);
        Producto producto = (Producto)q.getSingleResult();
        tx.commit();
        em.clear();
        em.close();
        emf.close();
        
        return producto;
    }
    
}
