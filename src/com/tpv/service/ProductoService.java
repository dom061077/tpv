/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.BonificacionCliente;
import com.tpv.modelo.Cliente;
import com.tpv.modelo.Combo;
import com.tpv.modelo.GrupoProducto;
import com.tpv.modelo.ListaPrecioProducto;
import com.tpv.modelo.Producto;
import com.tpv.modelo.Proveedor;
import com.tpv.util.Connection;
import java.math.BigDecimal;
import java.math.BigInteger;
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
    public List getProductos(String filtro) throws TpvException{
        log.info("Capa de servicios, Filtro de búsqueda: "+filtro);
        List<Producto> productos = null;
        int codigoProducto=0;
        try{
            codigoProducto = Integer.parseInt(filtro);
        }catch(Exception e){
            
        }
        EntityManager em = Connection.getEm();
        try{

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
                filtro=filtro.replace("+", "%");
                q.setParameter("detalleSuc", "%"+filtro+"%");
                q.setFirstResult(0);
                q.setMaxResults(100);
                productos = q.getResultList();

            }
            log.info("Cantidad de productos recuperados: "+productos.size());
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al recuperar listado de productos",e);
            throw new TpvException("Error en la capa de servicios al recuperar listado de productos.");
        }finally{
            em.clear();

        }
        return productos;
    }
    
    public List getProductosPrecio(String filtro) throws TpvException{
        log.info("Capa de servicios, filtro: "+filtro);
        
        int codigoIngresado=0;
        BigInteger codBarra=BigInteger.ZERO;
        try{
            codigoIngresado = Integer.parseInt(filtro);
            log.debug("Codigo a consultar en productoService");
        }catch(Exception e){
            try{
                codBarra = new BigInteger(filtro) ;
            }catch(Exception ex){
                
            }
        }
        
        filtro = filtro.replace("+", "%");
        EntityManager em = Connection.getEm();
        List<ListaPrecioProducto> productosPrecios=null;
        try{
            Query q = em.createQuery("FROM ListaPrecioProducto lpp WHERE "
                    +" lpp.producto.descripcion like :filtro "
                    +" or lpp.producto.codigoProducto = :codigoProducto"
                    +" or lpp.producto.codBarra = :codBarra"
                ).setParameter("filtro", "%"+filtro+"%")
                .setParameter("codigoProducto", codigoIngresado)
                .setParameter("codBarra", codBarra.toString().trim());
            q.setFirstResult(0);
            q.setMaxResults(100);
            productosPrecios = q.getResultList();
            log.info("Productos con precio recuperado: "+productosPrecios.size());
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al recuperar listado de productos",e);
            throw new TpvException("Error en la capa de servicios al recuperar listado de productos.");
        }finally{
            em.clear();
        }

        
        return productosPrecios;
    }
    
    public Producto getProductoPorCodigo(int filtroCodigo) throws TpvException{
        log.info("Capa de servicios búsqueda por código, filtro "+filtroCodigo);
        EntityManager em = Connection.getEm();
        //EntityTransaction tx = null;
        Producto producto = null;
        try{
            //tx = em.getTransaction();
            //tx.begin();
            Query q = em.createQuery("FROM Producto p WHERE p.discontinuado = 0 "
                    +" and p.codigoProducto = :codigoProducto")
                    .setParameter("codigoProducto", filtroCodigo);

            producto = (Producto)q.getSingleResult();
            producto.getProveedores().forEach(item->{
                item.getProveedor().getId();
            });
            //tx.commit();
        }catch(NoResultException e){
            log.warn("No se pudo encontrar el producto con el código: "+filtroCodigo);
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al recuperar un producto con codigo: "+filtroCodigo,e);
            throw new TpvException("Error en la capa de servicios al recuperar un producto con codigo: "+filtroCodigo);
        }finally{
            em.clear();
        }
        
        return producto;
    }
    
    public Producto getProductoPorCodBarra(String codigoBarra) throws TpvException{
        log.info("Capa de servicios búsqueda por código de barra, filtro: "+codigoBarra);
        EntityManager em = Connection.getEm();
        Producto producto = null;
        try{
            Query q = em.createQuery("FROM Producto p WHERE p.discontinuado = 0 and p.codBarra = :codBarra")
                    .setParameter("codBarra", codigoBarra);
            producto = (Producto)q.getSingleResult();
        }catch(NoResultException e){
            log.warn("No se puedo encontrar el producto con el código de barra: "+codigoBarra);
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al recuperar un producto con código de barra: "+codigoBarra,e);
            throw new TpvException("Error en la capa de servicios al recuperar un producto con codigo: "+codigoBarra);
        }finally{
            em.clear();
        }
        return producto;
    }
    
    private boolean isProductoEnCombo(int filtroCodigo) throws TpvException{
        EntityManager em = Connection.getEm();
        List<Combo> combos = null;
        Query q = em.createNativeQuery(
                " SELECT c.* FROM productos p "
                +" LEFT JOIN ("
                +"       SELECT gp.idGRUPOPRODUCTOS AS grupohijo"
                +"                ,glevel1.idGRUPOPRODUCTOS AS grupopadre FROM grupoproductos gp"
                +"        INNER JOIN grupoproductos glevel1 ON glevel1.idgrupoproductos = gp.padreid"
                +" ) grupoprod ON (p.idgrupoproductos = grupoprod.grupohijo OR p.idgrupoproductos = grupoprod.grupopadre)"
                +" LEFT JOIN combosgrupodetalle cgd ON p.idPRODUCTOS = cgd.idproductos OR grupoprod.grupohijo = cgd.idGRUPOPRODUCTOS"
                +"		OR grupoprod.grupopadre = cgd.idGRUPOPRODUCTOS"
                +" LEFT JOIN combosgrupo cg ON cgd.idCOMBOSGRUPO = cg.idCOMBOSGRUPO"
                +" LEFT JOIN combos c ON cg.idCOMBOS = c.idCOMBOS"
                +" LEFT JOIN proveedores_productos pp ON p.idPRODUCTOS = pp.idPRODUCTOS AND pp.idProveedor=cgd.idProveedor"
                +" WHERE c.idcombos IS NOT NULL AND p.codigoProducto = ?1 AND CONVERT(NOW(),DATE) BETWEEN c.FECHADESDE AND c.FECHAHASTA"
                +" AND p.DISCONTINUADO = 0"
                , Combo.class).setParameter(1, filtroCodigo);
        try{
            combos = q.getResultList();
        }catch(NoResultException e){    
            log.warn("No se encontró el código de producto"+filtroCodigo+" en la lista de precios."
                +" La excepción NoResultException no se considera como error. "+e.getMessage());
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al recuperar precio del producto con código: "
                    +filtroCodigo,e);
            throw new TpvException("Error en la capa de servicios al recuperar precio del producto con código: "
                    +filtroCodigo+"");
        }finally{
            em.clear();
        }
        if(combos==null)
            return false;
        if(combos.size()==0)
            return false;
        return true;            
    }
    
    public BigDecimal getPrecioProducto(int filtroCodigo,Cliente cliente) throws TpvException{
        log.info("Capa de servicios, parámetro de filtro producto: "+filtroCodigo
                +", cliente: "+(cliente!=null?cliente.getId():0));
        ListaPrecioProducto lstPrecioProducto=null;
        BigDecimal precio = new BigDecimal(0);
        EntityManager em = Connection.getEm();
        try{
            Query q = em.createQuery("FROM ListaPrecioProducto lpp WHERE"
                    +" lpp.producto.discontinuado = 0"
                    +" and lpp.producto.codigoProducto = :codigoProducto").setParameter("codigoProducto", filtroCodigo);
                lstPrecioProducto = (ListaPrecioProducto)q.getSingleResult();
                precio = lstPrecioProducto.getPrecioFinal();
                
            if(cliente!= null && cliente.getEmpresa().isEstado()){
                    q = em.createQuery("SELECT pp.proveedor FROM ProveedorProducto pp WHERE pp.producto.codigoProducto = :productoId"
                                        +" and pp.proveedor.id = :proveedorId"
                                        ).setParameter("productoId", filtroCodigo)
                                        .setParameter("proveedorId",Long.parseLong("418"));
                    Proveedor proveedor = null;
                    try{
                        proveedor = (Proveedor)q.getSingleResult();
                    }catch(NoResultException e){
                        log.debug("No se encontro relacion entre el producto : "+filtroCodigo
                                    +" y el proveedor 418");
                    }
                    
                    if(proveedor==null && !isProductoEnCombo(filtroCodigo)){
                        log.debug("el proveedor es null ");
                        q = em.createQuery("FROM BonificacionCliente bc WHERE bc.cliente.id = :clienteId "
                                +" AND mesAnio = mesAnioCalc")
                                .setParameter("clienteId", cliente.getId());
                        BonificacionCliente bc = (BonificacionCliente)q.getSingleResult();
                        BigDecimal descuento = precio.multiply(cliente.getEmpresa()
                                .getPorcentajeDescuento()).divide(BigDecimal.valueOf(100));
                        BigDecimal precioConDescuento = precio.subtract(descuento);
                        BigDecimal totalAcumulado = bc.getMontoAcumulado().add(precioConDescuento);
                        if(bc!=null){
                            if(cliente.getEmpresa().getTopeDescuento()
                                .compareTo(totalAcumulado)>0){
                                log.info("El precio del producto tiene descuento de personal");
                                precio = precioConDescuento;
                            }
                        }
                    }
            }
                
                
            log.info("Precio recuperado, codigo de producto: "+filtroCodigo
                    +", precio "+precio);
        }catch(NonUniqueResultException e){
            log.warn("Se econtró mas de un registro en la consulta de único resultado: "+e.getMessage());
        }catch(NoResultException e){    
            log.warn("No se encontró el código de producto"+filtroCodigo+" en la lista de precios."
                +" La excepción NoResultException no se considera como error. "+e.getMessage());
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al recuperar precio del producto con código: "
                    +filtroCodigo,e);
            throw new TpvException("Error en la capa de servicios al recuperar precio del producto con código: "
                    +filtroCodigo+"");
        }finally{
            em.clear();
        }

        
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
