/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.AlicuotaIngresosBrutos;
import com.tpv.modelo.AperturaCierreCajeroDetalle;
import com.tpv.modelo.BonificacionCliente;
import com.tpv.modelo.Checkout;
import com.tpv.modelo.Combo;
import com.tpv.modelo.ComboGrupo;
import com.tpv.modelo.ComboGrupoDetalle;
import com.tpv.modelo.Concurso;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.FacturaDetalleCombo;
import com.tpv.modelo.FacturaDetalleComboAbierto;
import com.tpv.modelo.FacturaDetalleConcurso;
import com.tpv.modelo.FacturaFormaPagoDetalle;
import com.tpv.modelo.MotivoNotaDC;
import com.tpv.modelo.ProductoAgrupadoEnFactura;
import com.tpv.modelo.Usuario;
import com.tpv.modelo.enums.FacturaEstadoEnum;
import com.tpv.modelo.enums.RetiroDineroEnum;
import com.tpv.modelo.enums.TipoComprobanteEnum;
import com.tpv.pagoticket.LineaPagoData;
import com.tpv.principal.Context;
import com.tpv.util.Connection;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author daniel
 */
public class FacturacionService  {
    Logger log = Logger.getLogger(FacturacionService.class);
    
    
    public Factura registrarFactura(Factura factura)throws TpvException{
        log.info("Capa de servicios FacturacionService, registrando por primera vez factura");
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin(); 
            factura.setTotal(BigDecimal.ZERO);
            factura.setBonificaTarjeta(BigDecimal.ZERO);
            factura.setIvaBonificaTarjeta(BigDecimal.ZERO);
            factura.setImpuestoInterno(BigDecimal.ZERO);
            //factura.setCondicionIva(em.find(CondicionIva.class, 1));
            factura.setFechaAlta(factura.getUsuario().getFechaHoraHoy());
            em.persist(factura);
            tx.commit();
        }catch(RuntimeException e){
            tx.rollback();
            log.error("Error en la capa de servicios al registrar la factura por primera vez.",e);
            throw new TpvException("Error en la capa de servicios al registrar la factura por primera vez.");
        }finally{
            em.clear();
        }
        return factura;
    }
    
    public Factura getFactura(Long prefijo, Long numero) throws TpvException{
        Factura factura=null;
        EntityManager em = Connection.getEm();
        try{
            Query q = em.createQuery("FROM Factura f WHERE f.tipoComprobante = :tipoComprobante "
                    +" AND f.prefijoFiscal = :prefijoFiscal"
                    +" AND f.numeroComprobante = :numeroComprobante"
                    )
                    .setParameter("tipoComprobante", TipoComprobanteEnum.F)
                    .setParameter("prefijoFiscal", prefijo)
                    .setParameter("numeroComprobante", numero);
            factura = (Factura)q.getSingleResult();
        }catch(NonUniqueResultException e){    
            log.error("Hay más de una factura para el prefijo: "+prefijo
                    +" y numero: "+numero,e);
            throw new TpvException("Hay más de una factura para numero ingresado "
                                    +" debe solucionar el problema");
        }catch(NoResultException e){
            log.info("no se encontró factura prefijo: "+prefijo+", número: "
                    +numero+", no se toma como error NoResultException");
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al devolver la factura.",e);
            throw new TpvException("Error en la capa de servicios al devolver la factura.");
        }finally{
            em.clear();
        }
        return factura;
    }
    
    public Factura getFactura(Long id) throws TpvException{
        Factura factura = null;
        EntityManager em = Connection.getEm();
        try{
            factura = em.find(Factura.class, id);
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al devolver la factura.",e);
            throw new TpvException("Error en la capa de servicios al devolver la factura.");
        }finally{
            em.clear();
        }
        return factura;
    }

    public Factura getFacturaAbiertaPorCheckout(int idCheckout, int usuarioId
            ,TipoComprobanteEnum tipoComprobante) throws TpvException{
        Factura factura = null;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            Query q = em.createQuery("FROM Factura f WHERE f.estado = :estado and f.checkout.id = :idCheckout "
                        +" and f.usuario.id = :usuarioId and f.tipoComprobante = :tipoComprobante")
                        .setParameter("usuarioId",usuarioId)
                        .setParameter("estado", FacturaEstadoEnum.ABIERTA)
                        .setParameter("idCheckout", idCheckout)
                        .setParameter("tipoComprobante",tipoComprobante);
            factura = (Factura)q.getSingleResult();
        }catch(NonUniqueResultException e){    
            log.error("Hay más de una factura abierta",e);
            throw new TpvException("Hay más de una factura abierta para el checkout y usuario. Administración "
                                    +" debe solucionar el problema");
        }catch(NoResultException e){
            log.info("no se encontró factura abierta, no se toma como error NoResultException");
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al devolver la factura.",e);
            throw new TpvException("Error en la capa de servicios al devolver la factura.");
        }finally{
            em.clear();
        }
        
        return factura;
    }
    
    public void agregarDetalleFactura(Long id,FacturaDetalle facturaDetalle) throws TpvException{
        log.info("Capa de servicios FacturacionService, agregando detalle de factura");
        Factura factura;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            
            tx = em.getTransaction();
            tx.begin();
            factura = em.find(Factura.class,id);
            facturaDetalle.setFactura(factura);
            
            factura.getDetalle().add(facturaDetalle);
            
            tx.commit();
        }catch(RuntimeException e){
            if(tx.isActive())
                tx.rollback();
            log.error("Error en la capa de servicios al agregar detalle de la factura.",e);
            throw new TpvException("Error en la capa de servicios al agregar detalle de la factura.");
        }finally{
            em.clear();
        }
    }
    
    public void registrarNroComprobanteYHoraFiscal(Long idFactura
            ,Long numeroComprobante,String fechaHoraFiscal)throws TpvException{
        Factura factura;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            factura = em.find(Factura.class, idFactura);
            factura.setNumeroComprobante(numeroComprobante);
            factura.setFechaHoraFiscal(fechaHoraFiscal);
            tx.commit();
        em.clear();
        }catch(RuntimeException e){
            if(tx.isActive())
                tx.rollback();
            log.error("Error en la capa de servicios al agregar detalle de la factura.",e);
            throw new TpvException("Error en la capa de servicios al agregar detalle de la factura.");
        }finally{
            em.clear();
        }
    }
    
    public Factura confirmarFactura(Factura factura)throws TpvException{
        //Factura factura;
        log.info("Capa de servicios, Confirmar Factura con id: "+factura.getId());
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            /*
            Query q = em.createNativeQuery(
                     "SELECT c.idCONCURSOS,c.TEXTOCORTO,c.CANTIDADPRODUCTOS,c.CANTIDADCUPONES"
                    +" ,(SUM(fd.CANTIDAD) DIV c.CANTIDADPRODUCTOS)*c.CANTIDADCUPONES AS CUPONESENTREGADOS"
                    +" FROM facturasdetalle fd"
                    +" INNER JOIN productos p ON fd.idPRODUCTOS = p.idPRODUCTOS"
                    +" INNER JOIN proveedores_productos pp ON fd.idPRODUCTOS = pp.idPRODUCTOS"
                    +" LEFT JOIN concursos cgp ON cgp.idGRUPOPRODUCTOS=p.idGRUPOPRODUCTOS"
                    +" LEFT JOIN  concursos cgph ON cgph.idSUBGRUPO = p.idGRUPOPRODUCTOS"
                    +" LEFT JOIN  concursos cp ON fd.idPRODUCTOS = cp.idPRODUCTOS"
                    +" LEFT JOIN proveedores prov ON pp.idProveedor = prov.idProveedor "
                    +" LEFT JOIN concursos c ON c.idCONCURSOS = cgp.idCONCURSOS OR"
                    +" c.idCONCURSOS = cgph.idCONCURSOS OR c.idCONCURSOS = cp.idCONCURSOS"
                    +" OR c.idProveedor = prov.idProveedor "            
                    +" WHERE fd.idFACTURAS = :idFacturas AND c.idCONCURSOS IS NOT NULL"
                    +" GROUP BY c.idCONCURSOS,c.TEXTOCORTO,c.CANTIDADPRODUCTOS,c.CANTIDADCUPONES")
                    .setParameter("idFacturas", factura.getId());
            List list = q.getResultList();            
            for(Iterator<Object[]> it = list.iterator();it.hasNext();){
                Object[] o = it.next();
                FacturaDetalleConcurso detConcurso = new FacturaDetalleConcurso();
                detConcurso.setCantidadCupones(Integer.parseInt(o[4].toString()));
                Concurso concurso = em.find(Concurso.class, Long.parseLong(o[0].toString()));
                detConcurso.setConcurso(concurso);
                
                
                        
                detConcurso.setFactura(factura);
                factura.getDetalleConcursos().add(detConcurso);
            }
            */
            tx = em.getTransaction();
            tx.begin();
            factura.setDescuento(BigDecimal.ZERO);
            factura.getBonificacionCombos();
            factura.setEstado(FacturaEstadoEnum.CERRADA);
            factura.setFechaHoraCierre(factura.getUsuario().getFechaHoy());
            BigDecimal descuentoAcumCliente = BigDecimal.ZERO;//CAMBIAR
            for(Iterator<FacturaDetalle> it = factura.getDetalle().iterator();it.hasNext();){
                FacturaDetalle factDet = (FacturaDetalle)it.next();
                factDet.getProducto().decStock(factDet.getCantidad());
                if(factDet.getDescuentoCliente().compareTo(BigDecimal.ZERO)>0){
                    descuentoAcumCliente = descuentoAcumCliente
                            .add(factDet.getPrecioUnitarioBase()
                                  .multiply(factDet.getCantidad()));
                }
            }
            
            if(factura.getCliente()!=null){
                try{
                    Query q = em.createQuery("FROM BonificacionCliente b WHERE b.cliente.id = :idCliente"
                        +" AND b.mesAnio = :mesAnio")
                        .setParameter("idCliente", factura.getCliente().getId())
                        .setParameter("mesAnio", factura.getUsuario().getMesAnioCalc());
                    BonificacionCliente bc = (BonificacionCliente)q.getSingleResult();
                    bc.addMontoAcumulado(descuentoAcumCliente);
                    bc.addMontoAcumuladoTotal(descuentoAcumCliente);
                    em.merge(bc);
                }catch(NoResultException e){
                    log.info("No se encontró el registro de bonificación para el cliente: "
                        +factura.getCliente().getId()+". Se procede a insertar el registro por primera vez.");
                    BonificacionCliente bc = new BonificacionCliente();
                    bc.setCliente(factura.getCliente());
                    bc.setMesAnio(factura.getUsuario().getMesAnioCalc());
                    bc.setMontoAcumulado(descuentoAcumCliente);
                    bc.setMontoAcumuladoTotal(descuentoAcumCliente);
                    em.persist(bc);

                }catch(NonUniqueResultException e){
                    log.error("Se encontró más de un registro de bonificación para el cliente: "
                            +factura.getCliente().getId()+". Se interrumpe confirmación de factura",e);
                    throw new TpvException("Se encontró más de un registro de bonificación para el cliente: "
                            +factura.getCliente().getId()+". Se interrumpe confirmación de factura");
                }
            }
            
            factura=em.merge(factura);
            tx.commit();
            log.info("Factura guardada, id: "+factura.getId());
            log.info("                      Nro. factura: "+factura.getNumeroComprobante());
        }catch(RuntimeException e){
            if(tx!=null)
                tx.rollback();
            log.error("Error en la capa de servicios al confirmar la factura.",e);
            throw new TpvException("Error en la capa de servicios al confirmar la factura.");
        }finally{
            em.clear();
        }
        
        return factura;
    }
    
    public void cancelarFacturaSupervisor(Long id) throws TpvException{
        log.info("Capa de servicios, cancelar factura por supervisor");
        Factura factura;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            factura = em.find(Factura.class, id);
            factura.setEstado(FacturaEstadoEnum.ANULADA_SUPERVISOR);
            tx.commit();
            log.info("Factura con id: "+factura.getId()+", Nro. factura: "
                      +factura.getNumeroComprobante());   
        }catch(RuntimeException e){
            tx.rollback();
            log.error("Error en la capa de servicios al cancelar la factura",e);
            throw new TpvException("Error en la capa de servicios al cancelar la factura.");
        }finally{
            em.clear();
        }
        
    }
    
    public void anularFacturaPorSupervisor(Long id,Usuario usuarioSupervisor) throws TpvException{
        anularFactura(id,usuarioSupervisor,FacturaEstadoEnum.ANULADA_SUPERVISOR);
    }
    
    public Long anularFacturaPorReinicio(Long id) throws TpvException{
        return anularFactura(id,null,FacturaEstadoEnum.ANULADA);
    }
    
    public void anularFacturasAbiertas(int idCheckout,int idUsuario
            ,Usuario usuarioSupervisor)throws TpvException{
        log.info("Capa de servicios, anular facturas abiertas para checkout : "
            +idCheckout+", Usuario cajero: "+idUsuario
            +", Usuario supervisor: "+usuarioSupervisor.getIdUsuario());
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            Query q = em.createQuery("FROM Factura f WHERE f.checkout.id = :idCheckout "
                +" AND f.usuario.id = :idUsuario AND f.estado = :estado")
                   .setParameter("idCheckout", idCheckout)
                   .setParameter("idUsuario", idUsuario)
                   .setParameter("estado", FacturaEstadoEnum.ABIERTA);
            List<Factura> facturas = q.getResultList();
            facturas.forEach(item->{
                item.setEstado(FacturaEstadoEnum.ANULADA_SUPERVISOR);
                item.setUsuarioModificacion(usuarioSupervisor);
            });
            tx.commit();
        }catch (RuntimeException e){
            log.error("Error en la capa de servicios al anular facturas abiertas",e);
            throw new TpvException("Error en la capa de servicios al anular facturas abiertas. "
                    +e.getMessage());
        }finally{
            em.clear();
        }
    }
    
    private Long anularFactura(Long id,Usuario usuarioSupervisor, FacturaEstadoEnum estadoCancelacion) throws TpvException{
        log.info("Capa de servicios, cancelar factura");
        Factura factura;
        Factura facturaReinicioAbierta;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        Long idFactura = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            factura = em.find(Factura.class, id);
            factura.setUsuarioModificacion(usuarioSupervisor);
            factura.setEstado(estadoCancelacion);
            if(estadoCancelacion == FacturaEstadoEnum.ANULADA){
                facturaReinicioAbierta = new Factura();
                facturaReinicioAbierta.setAperturaCierreCajeroDetalle(factura.getAperturaCierreCajeroDetalle());
                facturaReinicioAbierta.setCaja(factura.getCaja());
                facturaReinicioAbierta.setCheckout(factura.getCheckout());
                facturaReinicioAbierta.setCliente(factura.getCliente());
                facturaReinicioAbierta.setCondicionIva(factura.getCondicionIva());
                facturaReinicioAbierta.setEstado(FacturaEstadoEnum.ABIERTA);
                facturaReinicioAbierta.setFechaAlta(factura.getUsuario().getFechaHoraHoy());
                facturaReinicioAbierta.setTipoComprobante(factura.getTipoComprobante());
                facturaReinicioAbierta.setClaseComprobante(factura.getClaseComprobante());
                facturaReinicioAbierta.setPrefijoFiscal(factura.getPrefijoFiscal());
                facturaReinicioAbierta.setUsuario(factura.getUsuario());
                
                facturaReinicioAbierta.setTotal(BigDecimal.ZERO);
                facturaReinicioAbierta.setBonificaTarjeta(BigDecimal.ZERO);
                facturaReinicioAbierta.setIvaBonificaTarjeta(BigDecimal.ZERO);
                facturaReinicioAbierta.setImpuestoInterno(BigDecimal.ZERO);                
                em.persist(facturaReinicioAbierta);
                idFactura = facturaReinicioAbierta.getId();
            }
            tx.commit();
            log.info("Factura con id: "+factura.getId()+", Nro. factura: "
                      +factura.getNumeroComprobante());   
        }catch(RuntimeException e){
            //tx.rollback();
            log.error("Error en la capa de servicios al cancelar la factura",e);
            throw new TpvException("Error en la capa de servicios al cancelar la factura. "+e.getMessage());
        }finally{
            em.clear();
        }
        return idFactura;
    }
            
//    public void registrarCombos(Long id){
//        Factura factura;
//        EntityManager em = Connection.getEm();
//        EntityTransaction tx = em.getTransaction();
//        if(!tx.isActive())
//            tx.begin();
//        factura = em.find(Factura.class,id );
//        factura.setEstado(FacturaEstadoEnum.CERRADA);
//        tx.commit();
//        em.clear();
//    }
    

    
    
    public Factura calcularCombos(Long id) throws TpvException{
        log.info("Calculando combos para id factura: "+id);
        if(id==null)
            return null;
        List<Combo> listadoCombos = null;
        Factura factura = null;
        EntityManager em = Connection.getEm();
        factura = em.find(Factura.class, id);
        factura.agruparProductosEnFactura();
//        for(Iterator<ProductoAgrupadoEnFactura> it = factura.getProductosAgrupados().iterator();it.hasNext();){
//            ProductoAgrupadoEnFactura paf = it.next();
//            System.out.println("Producto: "+paf.getProducto().getDescripcion());
//            System.out.println("Cantidad Acumulada; "+paf.getCantidad());
//            System.out.println("Precio del producto: "+paf.getPrecioUnitario());
//        }
//        if(factura!=null)
//            return;
        
        factura.getDetalleCombosAux().clear();
        Query q = em.createNativeQuery(
                "SELECT DISTINCT c.* FROM facturasdetalle fd"
                +" INNER JOIN productos p ON fd.idPRODUCTOS=p.idPRODUCTOS AND p.DISCONTINUADO = 0"
                +" LEFT JOIN ("
                +"       SELECT gp.idGRUPOPRODUCTOS AS grupohijo"
                +"                ,glevel1.idGRUPOPRODUCTOS AS grupopadre FROM grupoproductos gp"
                +"        INNER JOIN grupoproductos glevel1 ON glevel1.idgrupoproductos = gp.padreid"
                +" ) grupoprod ON (p.idgrupoproductos = grupoprod.grupohijo OR p.idgrupoproductos = grupoprod.grupopadre)"
                +" LEFT JOIN proveedores_productos pp ON fd.idPRODUCTOS = pp.idPRODUCTOS"        
                +" LEFT JOIN combosgrupodetalle cgd ON fd.idPRODUCTOS = cgd.idproductos OR grupoprod.grupohijo = cgd.idGRUPOPRODUCTOS"
		+"		OR grupoprod.grupopadre = cgd.idGRUPOPRODUCTOS OR pp.idProveedor=cgd.idProveedor"
                +" LEFT JOIN combosgrupo cg ON cgd.idCOMBOSGRUPO = cg.idCOMBOSGRUPO"
                +" LEFT JOIN combos c ON cg.idCOMBOS = c.idCOMBOS AND c.ANULADO = ?2"
                +" WHERE c.idcombos IS NOT NULL AND fd.idFACTURAS = ?1 AND CONVERT(NOW(),DATE) BETWEEN c.FECHADESDE AND c.FECHAHASTA"
                +" ORDER BY c.PRIORIDAD"
                , Combo.class)
                .setParameter(1, id)
                .setParameter(2, false);
        try{
            listadoCombos = q.getResultList();
//            if(listadoCombos.size()>0){
//                for(Iterator<Combo> it = listadoCombos.iterator();it.hasNext(); ){
//                    Combo c = it.next();
//                    System.out.println("Combo: "+c.getDescripcion()+" Prioridad: "
//                            +c.getPrioridad());
//                }
//                return;
//            }
            boolean hayDetalleGrupo;
            for(Iterator itCombo = listadoCombos.iterator();itCombo.hasNext();){
                Combo combo = (Combo)itCombo.next();
                for(Iterator itGrupo = combo.getCombosGrupo().iterator();itGrupo.hasNext();){
                    ComboGrupo grupo = (ComboGrupo)itGrupo.next();
                    for(Iterator<ProductoAgrupadoEnFactura> itAg = factura.getProductosAgrupados().iterator();itAg.hasNext();){
                        ProductoAgrupadoEnFactura paf = itAg.next();
                        hayDetalleGrupo=false;
                        for(Iterator itDetalle = grupo.getGruposDetalle().iterator();itDetalle.hasNext();){
                            ComboGrupoDetalle gDetalle = (ComboGrupoDetalle)itDetalle.next();
                            if(paf.getCantidad().compareTo(BigDecimal.ZERO)<=0)//if(paf.getCantidad()<=0)
                                continue;
                            if(gDetalle.getProducto()!=null){
                                if(gDetalle.getProducto().equals(paf.getProducto())){
                                    if(gDetalle.getProveedor()!=null){
                                        if(paf.getProducto().tieneEsteProveedor(gDetalle.getProveedor())){
                                            hayDetalleGrupo = true;
                                        }
                                    }else{
                                            hayDetalleGrupo= true;
                                    }
                                }
                            }else{
                                if(gDetalle.getGrupoProducto()!=null){
                                    if(paf.getProducto().tieneEsteGrupo(gDetalle.getGrupoProducto())){
                                        if(gDetalle.getProveedor()!=null){
                                            if(paf.getProducto().tieneEsteProveedor(gDetalle.getProveedor())){
                                                hayDetalleGrupo = true;
                                            }
                                        }else{
                                            hayDetalleGrupo = true;
                                        }
                                    }
                                }else{
                                    if(gDetalle.getProveedor()!=null){
                                        if(paf.getProducto().tieneEsteProveedor(gDetalle.getProveedor())){
                                            hayDetalleGrupo = true;
                                        }
                                    }
                                }
                            }
                            if(hayDetalleGrupo){
                                if(combo.isCombinarProductos()){
                                    grupo.addDetallePrecioProducto(
                                                paf.getPrecioUnitario(), paf
                                                );
                                }else{
                                    grupo.addDetallePrecioProducto(
                                             paf.getPrecioUnitario(),paf);
                                }
                            }
                        }
                    }
                }
//                int cantidad = combo.getCantidadArmada();
//                System.out.println("Cantidad armada: "+cantidad);
//                System.out.println("Bonificación: "+combo.getBonificacionFinal());
                
//                if(combo.cumpleCondicion()){
//                    FacturaDetalleCombo fd = new FacturaDetalleCombo();
//                    fd.setCombo(combo);
//                    fd.setCantidad(combo.getCantidadCombosArmados());
//                    if(combo.isCombinarProductos())
//                        fd.setBonificacion(combo.getBonificacion().setScale(2,BigDecimal.ROUND_HALF_UP));
//                    else    
//                        fd.setBonificacion(combo.getBonificacionSinCombinacion().setScale(2,BigDecimal.ROUND_HALF_UP));
//                    factura.getDetalleCombosAux().add(fd);
//                }
                int cantidadArmada = combo.getCantidadArmada();
                if(cantidadArmada>0){
                    FacturaDetalleCombo fd = new FacturaDetalleCombo();
                    fd.setCombo(combo);
                    fd.setCantidad(cantidadArmada);
                    fd.setBonificacion(combo.getBonificacionFinal().setScale(2,BigDecimal.ROUND_HALF_UP));
                    for(Iterator<FacturaDetalleComboAbierto> it = combo.getComboAbierto().iterator();it.hasNext();){
                        FacturaDetalleComboAbierto fca = it.next();
                        fca.setFdCombo(fd);
                        fd.getDetalleAbierto().add(fca);
                    }
                    factura.getDetalleCombosAux().add(fd);
                }
                
                
            }
            
//            factura.getDetalleCombosAux().forEach(item ->{
//                System.out.println("Combo armado: "+item.getCombo().getDescripcion());
//                System.out.println("Bonificación: "+item.getBonificacion());
//                System.out.println("Cantidad Combos: "+item.getCantidad());
//                System.out.println("");
//                item.getCombo().getCombosGrupo().forEach(itemg->{
//                    System.out.println("            Grupo condicion de cantidad: "+itemg.getCantidad());
//                });
//            });
//            System.out.println("---------------------------------------------------------");
//            for(Iterator<ProductoAgrupadoEnFactura> it = factura.getProductosAgrupados().iterator();it.hasNext();){
//                ProductoAgrupadoEnFactura paf = it.next();
//                System.out.println("Producto: "+paf.getProducto().getIdProducto()+" "+paf.getProducto().getCodigoProducto()+"-"+paf.getProducto().getDescripcion());
//                System.out.println("Cantidad Sobrante: "+paf.getCantidad());
//            }
            
            
        }catch(RuntimeException e){    
            e.printStackTrace();
            //log.error("Error al calcular el combo sin combinaciones para id factura: ",e);
            throw new TpvException("Error al calcular combo para id factura: "
                    +". "+e.getMessage());
        }finally{
            em.clear();
        }
        return factura;
    }
    
    
    public BigDecimal getRetencionIngBrutoCliente(String cuit) throws TpvException{
        log.info("Calculando porcentaje de retención a CUIT: "+cuit);
        AlicuotaIngresosBrutos alicuota=null;
        EntityManager em = Connection.getEm();
        BigDecimal porcentaje = BigDecimal.valueOf(7);
        try{
            Query q = em.createQuery("FROM AlicuotaIngresosBrutos a WHERE a.cuit = :cuit");
            q.setParameter("cuit", cuit);
            alicuota = (AlicuotaIngresosBrutos) q.getSingleResult();
            porcentaje = alicuota.getPorcentaje();
            if(alicuota.getConvenio().equals("CM")){
                porcentaje=porcentaje.divide(BigDecimal.valueOf(2));
            }
            
        }catch(NoResultException e){
            log.info("CUIT no encontrado");
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios de cliente al recuperar Alicuota de CUIT: "+cuit,e);
            throw new TpvException("Error en la capa de servicios de cliente al recuperar cliente por cod. o D.N.I.");
        }finally{
            em.clear();
        }
        return porcentaje;
    }
    
     
    
    public Factura getFacturaConTotalesSinPagos(Long id)throws TpvException{
            Factura factura = calcularCombos(id);
            //-----------resumen de calculos en cabecera-----------
            BigDecimal totalBonifCombos = BigDecimal.ZERO;
            BigDecimal totalIvaBonifCombos = BigDecimal.ZERO;

            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombosAux().iterator();it.hasNext();){
                FacturaDetalleCombo fdc = it.next();
                factura.getDetalleCombos().add(fdc);

                fdc.setFactura(factura);
                log.info("          Combo: "+fdc.getCombo().getDescripcion());
            }             

            FacturaDetalle facturaDetalle = new FacturaDetalle();
            
            for(Iterator<FacturaDetalleCombo> it = factura.getDetalleCombos().iterator();it.hasNext();){
                FacturaDetalleCombo fdc = it.next();
                facturaDetalle.setCantidad(BigDecimal.valueOf(fdc.getCantidad()));
                facturaDetalle.setDescuentoCliente(BigDecimal.ZERO);
                facturaDetalle.setExento(fdc.getExentoBonif());
                facturaDetalle.setImpuestoInterno(fdc.getImpuestoInterno().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setCosto(fdc.getCostoPiso().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setIva(fdc.getIvaCompletoBonif().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setIvaReducido(fdc.getIvaReducidoBonif().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setNeto(fdc.getNetoCompletoBonif().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setNetoReducido(fdc.getNetoReducidoBonif().multiply(BigDecimal.valueOf(-1)));
                facturaDetalle.setPrecioUnitario(BigDecimal.ZERO);
                facturaDetalle.setPrecioUnitarioBase(BigDecimal.ZERO);
                facturaDetalle.setPorcentajeIva(BigDecimal.ZERO);
                facturaDetalle.setSubTotal(fdc.getBonificacion().multiply(BigDecimal.valueOf(-1)));            
                totalBonifCombos = totalBonifCombos.add(fdc.getBonificacion());
                totalIvaBonifCombos = totalIvaBonifCombos.add(fdc.getIVABonificacion());
                facturaDetalle.setProducto(fdc.getCombo().getProducto());
                facturaDetalle.setFactura(factura);
                factura.getDetalle().add(facturaDetalle);
                
            }
            
            BigDecimal total= BigDecimal.ZERO;
            BigDecimal costo = BigDecimal.ZERO;
            BigDecimal neto = BigDecimal.ZERO;
            BigDecimal netoReducido = BigDecimal.ZERO;
            BigDecimal impuestoInterno= BigDecimal.ZERO;
            BigDecimal descuento = BigDecimal.ZERO;
            BigDecimal exento = BigDecimal.ZERO;
            BigDecimal ivaReducido = BigDecimal.ZERO;
            BigDecimal iva = BigDecimal.ZERO;

            
            for(Iterator<FacturaDetalle>it = factura.getDetalle().iterator();it.hasNext();){
                FacturaDetalle fd = it.next();
                //fd.getProducto().decStock(fd.getCantidad());
                total=total.add(fd.getSubTotal());
                costo = costo.add(fd.getCosto());
                neto = neto.add(fd.getNeto());
                netoReducido = netoReducido.add(fd.getNetoReducido());
                descuento = descuento.add(fd.getDescuentoCliente());
                exento = exento.add(fd.getExento());
                iva = iva.add(fd.getIva());
                ivaReducido = ivaReducido.add(fd.getIvaReducido());
                impuestoInterno = impuestoInterno.add(fd.getImpuestoInterno());
                
            }
            factura.setNeto(neto);
            factura.setIva(iva);
            factura.setIvaReducido(ivaReducido);
            factura.setNetoReducido(netoReducido);
            factura.setImpuestoInterno(impuestoInterno);
            //TODO asignar el valor correcto
            factura.setCosto(costo);
            factura.setDescuento(descuento);
            factura.setExento(exento);
            factura.setTotal(total);
            factura.setRetencion(BigDecimal.ZERO);
            factura.setBonificacion(totalBonifCombos);
            factura.setIvaBonificacion(totalIvaBonifCombos);
            //---------fin cálculo en cabecera----
            //--------verificacion y aplicacion de ingreso brutos si fuese necesario---------
            //TODO la condicion de iva está siendo usado con hard code en la ret.Ing.Brutos
            if(factura.getCliente()!=null
                    && factura.getCliente().getCondicionIva().getId()==2
                    ){
                BigDecimal porcentajeRet = getRetencionIngBrutoCliente(Context.getInstance()
                                .currentDMTicket().getCliente().getCuit());    
                BigDecimal netoGral = factura.getNeto().add(factura.getNetoReducido());
                BigDecimal montoRet = netoGral.multiply(porcentajeRet).divide(BigDecimal.valueOf(100));
                montoRet = montoRet.setScale(2,BigDecimal.ROUND_HALF_EVEN);
                if(montoRet.compareTo(Context.getInstance().currentDMParametroGral().getMontoMinRetIngBrutos())>0){
                    factura.setRetencion(montoRet);
                }
            }
            
            factura.setTotal(factura.getTotal().add(factura.getRetencion()));
            
            
            return factura;
    }
    
    public Factura getFacturaConTotalesConPagos(Long id,Iterator<LineaPagoData> pagos)throws TpvException{
        Factura factura = getFacturaConTotalesSinPagos(id);
        
        
        PagoService pagoService = new PagoService();
        BigDecimal totalIvaInteresPago = BigDecimal.ZERO;
        BigDecimal totalIvaBonificacionPago = BigDecimal.ZERO;
        BigDecimal totalInteresPago = BigDecimal.ZERO;
        BigDecimal totalBonificacionPago = BigDecimal.ZERO;
        for(Iterator<LineaPagoData> it = pagos;it.hasNext();){
            LineaPagoData item = it.next();
            FacturaFormaPagoDetalle formaPagoDetalle = new FacturaFormaPagoDetalle();
            formaPagoDetalle.setFormaPago(pagoService.getFormaPago(item.getCodigoPago()));
            formaPagoDetalle.setFactura(factura);

            formaPagoDetalle.setMontoPago(item.getMonto());
            formaPagoDetalle.setCuota(item.getCantidadCuotas());
            formaPagoDetalle.setInteres(item.getInteres());
            formaPagoDetalle.setBonificacion(item.getBonificacion());
            formaPagoDetalle.setIvaInteres(item.getIvaInteres());
            formaPagoDetalle.setIvaBonificacion(item.getIvaBonficacion());
            formaPagoDetalle.setNumeroCupon(item.getCodigoCupon());
            formaPagoDetalle.setNumeroTarejta(item.getNroTarjeta());
            formaPagoDetalle.setTerminal(item.getTerminal());
            formaPagoDetalle.setNumeroLote(item.getNroLote());
            formaPagoDetalle.setPorcentaje(item.getPorcentaje());
            formaPagoDetalle.setDniCliente(item.getDniCliente());
            factura.addFormaPago(formaPagoDetalle);
            
            totalIvaInteresPago = totalIvaInteresPago.add(item.getIvaInteres());
            totalIvaBonificacionPago = totalIvaBonificacionPago.add(item.getIvaBonficacion());
            totalInteresPago = totalInteresPago.add(item.getInteres());
            totalBonificacionPago = totalBonificacionPago.add(item.getBonificacion());
        }
        
        factura.setBonificaTarjeta(totalBonificacionPago);
        factura.setInteresTarjeta(totalInteresPago);
        factura.setIvaBonificaTarjeta(totalIvaBonificacionPago);
        factura.setIvaTarjeta(totalIvaInteresPago);
        BigDecimal totalFactura = factura.getTotal();
        totalFactura = totalFactura.subtract(totalBonificacionPago).add(totalInteresPago);
        factura.setTotal(totalFactura);
        //---------CARGANDO CONCURSOS -----
        EntityManager em = Connection.getEm();
        try{
                Query q = em.createNativeQuery(
                             "SELECT c.idCONCURSOS,c.TEXTOCORTO,c.CANTIDADPRODUCTOS,c.CANTIDADCUPONES"
                            +" ,(SUM( IF(fd.total<0,fd.CANTIDAD*(-1),fd.CANTIDAD)) DIV c.CANTIDADPRODUCTOS)*c.CANTIDADCUPONES"
                            +" FROM facturasdetalle fd"
                            +" INNER JOIN productos p ON fd.idPRODUCTOS = p.idPRODUCTOS"
                            +" INNER JOIN proveedores_productos pp ON fd.idPRODUCTOS = pp.idPRODUCTOS"
                            +" LEFT JOIN concursos cgp ON cgp.idGRUPOPRODUCTOS=p.idGRUPOPRODUCTOS"
                            +" LEFT JOIN  concursos cgph ON cgph.idSUBGRUPO = p.idGRUPOPRODUCTOS"
                            +" LEFT JOIN  concursos cp ON fd.idPRODUCTOS = cp.idPRODUCTOS"
                            +" LEFT JOIN proveedores prov ON pp.idProveedor = prov.idProveedor "
                            +" LEFT JOIN concursos c ON (c.idCONCURSOS = cgp.idCONCURSOS OR"
                            +" c.idCONCURSOS = cgph.idCONCURSOS OR c.idCONCURSOS = cp.idCONCURSOS"
                            +" OR c.idProveedor = prov.idProveedor) "            
                            +"  AND c.vigenciaDesde <= CONVERT(NOW() , DATE) AND "         
                            +"  c.vigenciaHasta >= CONVERT(NOW() , DATE)"         
                            +" WHERE fd.idFACTURAS = :idFacturas AND c.idCONCURSOS IS NOT NULL"
                            +" GROUP BY c.idCONCURSOS,c.TEXTOCORTO,c.CANTIDADPRODUCTOS,c.CANTIDADCUPONES"
                            +" HAVING (SUM( IF(fd.total<0,fd.CANTIDAD*(-1),fd.CANTIDAD)) DIV c.CANTIDADPRODUCTOS)*c.CANTIDADCUPONES>0")
                            .setParameter("idFacturas", factura.getId());
                    List list = q.getResultList();            
                    for(Iterator<Object[]> it = list.iterator();it.hasNext();){
                        Object[] o = it.next();
                        FacturaDetalleConcurso detConcurso = new FacturaDetalleConcurso();
                        detConcurso.setCantidadCupones(Integer.parseInt(o[4].toString()));
                        Concurso concurso = em.find(Concurso.class, Long.parseLong(o[0].toString()));
                        detConcurso.setConcurso(concurso);



                        detConcurso.setFactura(factura);
                        factura.getDetalleConcursos().add(detConcurso);
                    }
        }catch(RuntimeException e){
            log.error("Error al consultar los concursos",e);
            throw new TpvException("Error al consultar los concursos");
        }
        
        //---------------------------------------
        
        
        return factura;
    } 
    
    public BigDecimal saldoRetiroDinero(int idUsuario, int idCheckout) throws TpvException{
        EntityManager em = Connection.getEm();
        BigDecimal totalRetiro = BigDecimal.ZERO;
        BigDecimal totalFacturado = BigDecimal.ZERO;
        try{
           Query q = em.createQuery("SELECT SUM(r.monto) FROM RetiroDinero r WHERE"
                                +"  r.usuario.id = :idUsuario AND r.checkout.id = :idCheckout"
                                +"  AND r.fechaAlta >= r.fechaHoy"
                                +"  AND r.estado = :estado"
                            ).setParameter("idUsuario", idUsuario)
                            .setParameter("idCheckout",idCheckout)
                            .setParameter("estado",RetiroDineroEnum.RETIRADO);
           totalRetiro = (BigDecimal)q.getSingleResult();
        }catch(RuntimeException e){
            log.error("No se pudo calcular el total de retiros del día. Id usuario: "
                            +idUsuario+", Id checkout: "+idCheckout,e);
            throw new TpvException("\"No se pudo calcular el total de retiros del día. Id usuario: \"\n" +
"                            +idUsuario+\", Id checkout: \"+idCheckout");
        }
        if(totalRetiro==null)
            totalRetiro = BigDecimal.ZERO;
        try{
           Query q = em.createQuery("SELECT SUM(f.total) FROM Factura f"
                            +" WHERE f.usuario.idUsuario=:idUsuario AND f.checkout.id=:idCheckout"
                            +" AND f.fechaAlta>=f.fechaHoy"
                            +" AND f.estado = :estado"   
                   
                            ).setParameter("idUsuario", idUsuario)
                            .setParameter("idCheckout",idCheckout)
                            .setParameter("estado", FacturaEstadoEnum.CERRADA)
                            ;
           totalFacturado = (BigDecimal)q.getSingleResult();
        }catch(RuntimeException e){
            log.error("No se pudo calcular el total de retiros del día. Id usuario: "
                            +idUsuario+", Id checkout: "+idCheckout,e);
            throw new TpvException("\"No se pudo calcular el total de retiros del día. Id usuario: \"\n" +
"                            +idUsuario+\", Id checkout: \"+idCheckout");
        }
        if(totalFacturado==null)
            totalFacturado = BigDecimal.ZERO;
        
        return totalFacturado.subtract(totalRetiro);
    }
    
    public void invertirSignoNotaDCConFactOrigen(Factura notaDC,Factura factOrigen){
            notaDC.setBonificaTarjeta(factOrigen.getBonificaTarjeta()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setBonificacion(factOrigen.getBonificacion()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setCosto(factOrigen.getCosto()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setDescuento(factOrigen.getDescuento()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setExento(factOrigen.getExento()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setImpuestoInterno(factOrigen.getImpuestoInterno()
                        .multiply(factOrigen.getImpuestoInterno()));
            notaDC.setInteresTarjeta(factOrigen.getInteresTarjeta()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setIva(factOrigen.getIva()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setIvaBonificaTarjeta(factOrigen.getIvaBonificaTarjeta()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setIvaBonificacion(factOrigen.getIvaBonificacion()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setIvaReducido(factOrigen.getIvaReducido()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setIvaTarjeta(factOrigen.getIvaTarjeta()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setNeto(factOrigen.getNeto()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setNetoReducido(factOrigen.getNetoReducido()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setNetoReducido(factOrigen.getNetoReducido()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setRetencion(factOrigen.getRetencion()
                        .multiply(BigDecimal.valueOf(-1)));
            notaDC.setTotal(factOrigen.getTotal()
                    .multiply(BigDecimal.valueOf(-1)));
            
    }
    
    private void copiarDetalleFactADetalleNotaDC(Factura factOrigen
            ,Factura notaDC){
        factOrigen.getDetalle().forEach(det ->{
            FacturaDetalle factDet = new FacturaDetalle();
            factDet.setCantidad(det.getCantidad().multiply(BigDecimal.valueOf(-1)));
            //factDet.setCantidadAuxCombo(0); trasient
            factDet.setCosto(det.getCosto().multiply(BigDecimal.valueOf(-1)));
            factDet.setDescuentoCliente(det.getDescuentoCliente().multiply(BigDecimal.valueOf(-1)));
            factDet.setExento(det.getExento().multiply(BigDecimal.valueOf(-1)));
            factDet.setImpuestoInterno(det.getImpuestoInterno()
                    .multiply(BigDecimal.valueOf(-1)));
            factDet.setIva(det.getIva().multiply(BigDecimal.valueOf(-1)));
            factDet.setIvaReducido(det.getIvaReducido().multiply(BigDecimal.valueOf(-1)));
            factDet.setNeto(det.getNeto().multiply(BigDecimal.valueOf(-1)));
            factDet.setNetoReducido(det.getNetoReducido().multiply(BigDecimal.valueOf(-1)));
            factDet.setPorcentajeIva(det.getPorcentajeIva().multiply(BigDecimal.valueOf(-1)));
            factDet.setPrecioUnitario(det.getPrecioUnitario().multiply(BigDecimal.valueOf(-1)));
            factDet.setPrecioUnitarioBase(det.getPrecioUnitarioBase().multiply(BigDecimal.valueOf(-1)));
            factDet.setSubTotal(det.getSubTotal().multiply(BigDecimal.valueOf(-1)));
            
            factDet.setProducto(det.getProducto());
            
            factDet.getProducto().incStock(det.getCantidad());
            
            factDet.setFactura(notaDC);
            notaDC.getDetalle().add(factDet);
            
        });
    }
    
    public Factura confirmarNotaDCFactura(TipoComprobanteEnum tipo
            ,Factura facturaOrigen
            ,AperturaCierreCajeroDetalle apCiereCajDet
            ,Checkout checkout
            ,Usuario usuario
            ,int idMotivo
            ,int caja
        ) throws TpvException{
        log.info("Capa de servicios FacturaService, registrando nota DC");
        EntityManager em = Connection.getEm();
        MotivoNotaDC motivo = em.find(MotivoNotaDC.class,idMotivo);
        EntityTransaction tx = null;
        Factura notaDC = new Factura();
        try{
            tx = em.getTransaction();
            tx.begin();
            
            notaDC.setTipoComprobante(tipo);
            notaDC.setFacturaOrigen(facturaOrigen);
            notaDC.setCliente(facturaOrigen.getCliente());
            notaDC.setClaseComprobante(notaDC.getFacturaOrigen().getClaseComprobante());
            notaDC.setAperturaCierreCajeroDetalle(apCiereCajDet);
            notaDC.setCheckout(checkout);
            notaDC.setEstado(FacturaEstadoEnum.CERRADA);
            notaDC.setUsuario(usuario);
            notaDC.setMotivo(motivo);
            notaDC.setClaseComprobante("B");
            notaDC.setFechaAlta(facturaOrigen.getUsuario().getFechaHoraHoy());
            
            notaDC.setFechaHoraCierre(facturaOrigen.getUsuario().getFechaHoy());
            
            if(facturaOrigen.getCliente()!=null){
                notaDC.setCondicionIva(facturaOrigen.getCliente().getCondicionIva());
                if(notaDC.getCondicionIva().getId()==2)
                    notaDC.setClaseComprobante("A");
            }
            facturaOrigen.getDetalleNotasDC().add(notaDC);
            notaDC.setCaja(caja);
            
            invertirSignoNotaDCConFactOrigen(notaDC,facturaOrigen);
            copiarDetalleFactADetalleNotaDC(facturaOrigen,notaDC);            
            /*tengo que hacer un merge en un bucle del detalle 
                    para actualizar el stock de productos
                            
            revisar en la facturacion, el stock descuenta dos veces
                    */
            em.persist(notaDC);
            em.merge(notaDC);
            tx.commit();
            
        }catch(RuntimeException e){
            if(tx!=null)
                tx.rollback();
            log.error("Error en la capa de servicios al confirmar la nota DC.",e);
            throw new TpvException("Error en la capa de servicios al confirmar la nota DC.");
            
        }finally{
            em.clear();
        }
        return notaDC;
    }
            
    
    
    public void confirmarNotaDCMonto(TipoComprobanteEnum tipo
            ,Factura facturaOrigen,BigDecimal monto
            ,Long prefijoFiscal,String numeroComprobante
            ,AperturaCierreCajeroDetalle apCiereCajDet
            ,Checkout checkout
            ,Usuario usuario
            ,int idMotivo
            ,int caja
        ) throws TpvException{
        BigDecimal signo = BigDecimal.ONE;
        log.info("Capa de servicios FacturacionService, registrando nota DC");
        if(tipo  == TipoComprobanteEnum.C)
            signo = signo.multiply(BigDecimal.valueOf(-1));
        EntityManager em = Connection.getEm();
        MotivoNotaDC motivo = em.find(MotivoNotaDC.class, idMotivo);
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            Factura notaDC = new Factura();
            notaDC.setPrefijoFiscal(prefijoFiscal);
            notaDC.setNumeroComprobante(Long.parseLong(numeroComprobante));
            notaDC.setTipoComprobante(tipo);
            notaDC.setFacturaOrigen(facturaOrigen);
            notaDC.setCliente(facturaOrigen.getCliente());
            notaDC.setClaseComprobante(notaDC.getFacturaOrigen().getClaseComprobante());
            notaDC.setCondicionIva(facturaOrigen.getCondicionIva());
            
            facturaOrigen.getDetalleNotasDC().add(notaDC);
            notaDC.setTotal(monto.multiply(signo));
            notaDC.setFechaAlta(facturaOrigen.getUsuario().getFechaHoraHoy());
            notaDC.setFechaHoraCierre(facturaOrigen.getUsuario().getFechaHoraHoy());
            
            notaDC.setIva(monto.multiply(BigDecimal.valueOf(0.21)));
            notaDC.setIva(notaDC.getIva().multiply(signo));
            notaDC.setNeto(notaDC.getTotal().subtract(notaDC.getIva()));
            notaDC.setNeto(notaDC.getNeto().multiply(signo));
            
            notaDC.setCosto(BigDecimal.ZERO);
            notaDC.setDescuento(BigDecimal.ZERO);
            notaDC.setExento(BigDecimal.ZERO);
            notaDC.setImpuestoInterno(BigDecimal.ZERO);
            notaDC.setInteresTarjeta(BigDecimal.ZERO);
            notaDC.setIvaBonificaTarjeta(BigDecimal.ZERO);
            notaDC.setIvaBonificacion(BigDecimal.ZERO);
            notaDC.setIvaReducido(BigDecimal.ZERO);
            notaDC.setIvaTarjeta(BigDecimal.ZERO);
            notaDC.setNetoReducido(BigDecimal.ZERO);
            notaDC.setRetencion(BigDecimal.ZERO);
            notaDC.setBonificacion(BigDecimal.ZERO);
            notaDC.setBonificaTarjeta(BigDecimal.ZERO);
            
            notaDC.setAperturaCierreCajeroDetalle(apCiereCajDet);
            notaDC.setCheckout(checkout);
            notaDC.setEstado(FacturaEstadoEnum.CERRADA);
            notaDC.setUsuario(usuario);
            notaDC.setMotivo(motivo);
            if(facturaOrigen.getCliente()!=null){
                notaDC.setCondicionIva(facturaOrigen.getCliente().getCondicionIva());
                if(notaDC.getCondicionIva().getId()==2)
                    notaDC.setClaseComprobante("A");
                else
                    notaDC.setClaseComprobante("B");
            }
                
            em.persist(notaDC);
            tx.commit();
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al registrar la nota D/C ",e);
            throw new TpvException("Error en la capa de servicios al registrar la nota D/C");
        }finally{
            em.clear();
        }
    }
    
    public void modificarNroCreditoTotalizarYCerrar(Long prefijo,Long numeroComprobante,Long idFactura) throws TpvException{
        log.info("Capa de servicios, modificar numero comprobante con id");
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            Factura factura = em.find(Factura.class, idFactura);
            
            BigDecimal bonificaTarjeta = BigDecimal.ZERO;
            BigDecimal bonificacion = BigDecimal.ZERO;
            BigDecimal costo = BigDecimal.ZERO;
            BigDecimal descuento = BigDecimal.ZERO;
            BigDecimal exento = BigDecimal.ZERO;
            BigDecimal impuestoInterno = BigDecimal.ZERO;
            BigDecimal interesTarjeta = BigDecimal.ZERO;
            BigDecimal iva = BigDecimal.ZERO;
            BigDecimal ivaBonificacionTarjeta = BigDecimal.ZERO;
            BigDecimal ivaBonificacion = BigDecimal.ZERO;
            BigDecimal ivaReducido = BigDecimal.ZERO;
            BigDecimal ivaTarjeta = BigDecimal.ZERO;
            BigDecimal neto = BigDecimal.ZERO;
            BigDecimal netoReducido = BigDecimal.ZERO;
            BigDecimal retencion = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;
            
            for(Iterator<FacturaDetalle> it = factura.getDetalle().iterator();it.hasNext();){
                FacturaDetalle det = it.next();
                costo = costo.add(det.getCosto());
                descuento = descuento.add(det.getDescuentoCliente());
                exento = exento.add(det.getExento());
                impuestoInterno = impuestoInterno.add(det.getImpuestoInterno());
                iva = det.getIva();
                ivaReducido = ivaReducido.add(det.getIvaReducido());
                neto = neto.add(det.getNeto());
                netoReducido = netoReducido.add(det.getNetoReducido());
                total = total.add(det.getSubTotal());
                BigDecimal cantidadSumaStock = det.getCantidad().multiply(BigDecimal.valueOf(-1));
                det.getProducto().incStock(cantidadSumaStock);
                
            }
            
            factura.setCosto(costo);
            factura.setDescuento(descuento);
            factura.setExento(exento);
            factura.setImpuestoInterno(impuestoInterno);
            factura.setIva(iva);
            factura.setIvaReducido(ivaReducido);
            factura.setNeto(neto);
            factura.setNetoReducido(netoReducido);
            factura.setTotal(total);
            factura.setFechaHoraCierre(factura.getUsuario().getFechaHoraHoy());
            
            factura.setNumeroComprobante(numeroComprobante);
            factura.setPrefijoFiscal(prefijo);
            factura.setEstado(FacturaEstadoEnum.CERRADA);
            em.merge(factura);
            tx.commit();
            
        }catch(RuntimeException e){
            if(tx!=null)
                tx.rollback();
            log.error("Erro en la capa de servicios al confirmar la factura.",e);
            throw new TpvException("Error en la capa de servicios al confirmar la factura.");
        }finally{
            em.clear();
        }
    
    }
    
    public void modificarNroCreditoYCerrar(Long prefijo,Long numeroComprobante,Long idFactura) throws TpvException{
        log.info("Capa de servicios, modificar numero comprobante con id");
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            Factura factura = em.find(Factura.class, idFactura);
            factura.setNumeroComprobante(numeroComprobante);
            factura.setPrefijoFiscal(prefijo);
            factura.setFechaHoraCierre(factura.getUsuario().getFechaHoraHoy());
            factura.setEstado(FacturaEstadoEnum.CERRADA);
            em.merge(factura);
            tx.commit();
            
        }catch(RuntimeException e){
            if(tx!=null)
                tx.rollback();
            log.error("Error en la capa de servicios al confirmar la factura.",e);
            throw new TpvException("Error en la capa de servicios al confirmar la factura.");
        }finally{
            em.clear();
        }
    }
            
    
    public List<MotivoNotaDC> getMotivos(TipoComprobanteEnum tipo) throws TpvException{
        List<MotivoNotaDC> motivos = null;
        EntityManager em = Connection.getEm();
        try{
            Query q = em.createQuery("FROM MotivoNotaDC WHERE tipo = :tipo")
                    .setParameter("tipo", tipo);
            motivos = q.getResultList();
        }catch(RuntimeException e){
            log.error("Error en capa de servicios al tratar de recuperar motivos de Nota D/C",e);
            throw new TpvException("Error en capa de servicios al tratar de recuperar motivos de Nota D/C");
        }finally{
            em.clear();
        }
        
        return motivos;
    }
    
    
    public boolean validarCierreNotaDC(Long idDocumento)throws TpvException{
        log.info("Capa de servicios, validando cierra de NotaDC. Parametros: idDocumento="+idDocumento);
        boolean valido=false;
        EntityManager em = Connection.getEm();
        try{
            Factura notaDC = em.find(Factura.class,idDocumento);
            if(notaDC.getTotal().compareTo(notaDC.getFacturaOrigen().getSaldoDispNotasDC()) <= 0){
                valido = true;
            }
        }catch(RuntimeException e){
            log.error("Error en la capa de servicios al validar cierre de NotaDC.",e);
            throw new TpvException("Error en la capa de servicios al validar cierre de NotaDC.");
        }finally{
            em.clear();
        }
        return valido;
    }
    
    
    
    /*public List getConcursos(Long idFactura) throws TpvException{
        List list=new ArrayList();
        EntityManager em = Connection.getEm();
        try{
            Query q = em.createNativeQuery(
                    "SELECT DISTINCT fd.idFACTURASDETALLE,c.idCONCURSOS,c.TEXTOCORTO,c.CANTIDADPRODUCTOS,c.CANTIDADCUPONES"
                    +"                ,((fd.CANTIDAD/c.CANTIDADPRODUCTOS)*c.CANTIDADCUPONES) AS CUPONESENTREGADOS"
                    +" FROM facturasdetalle fd"
                    +" INNER JOIN productos p ON fd.idPRODUCTOS = p.idPRODUCTOS"
                    +" INNER JOIN proveedores_productos pp ON fd.idPRODUCTOS = pp.idPRODUCTOS"
                    +" LEFT JOIN concursos cgp ON cgp.idGRUPOPRODUCTOS=p.idGRUPOPRODUCTOS"
                    +" LEFT JOIN  concursos cgph ON cgph.idSUBGRUPO = p.idGRUPOPRODUCTOS"
                    +" LEFT JOIN  concursos cp ON fd.idPRODUCTOS = cp.idPRODUCTOS"
                    +" LEFT JOIN concursos c ON c.idCONCURSOS = cgp.idCONCURSOS OR"
                    +" c.idCONCURSOS = cgph.idCONCURSOS OR c.idCONCURSOS = cp.idCONCURSOS"
                    +" WHERE fd.idFACTURAS = :idFacturas AND c.idCONCURSOS IS NOT NULL")
                    .setParameter("idFacturas", idFactura);
            list = q.getResultList();
                
        }catch(RuntimeException e){
            log.error("Error al consultar los concursos",e);
            throw new TpvException("Error al consultar los concursos");
        }
        
        return list;
    }*/
    
    
    /*
 SELECT DISTINCT fd.`idFACTURASDETALLE`,c.`idCONCURSOS`,c.`CANTIDADPRODUCTOS`,c.`CANTIDADCUPONES`
		 ,((fd.`CANTIDAD`/c.`CANTIDADPRODUCTOS`)*c.`CANTIDADCUPONES`) AS CUPONESENTREGADOS
  FROM `facturasdetalle` fd
 INNER JOIN productos p ON fd.`idPRODUCTOS` = p.`idPRODUCTOS`
 INNER JOIN proveedores_productos pp ON fd.`idPRODUCTOS` = pp.idPRODUCTOS
 LEFT JOIN `concursos` cgp ON cgp.`idGRUPOPRODUCTOS`=p.`idGRUPOPRODUCTOS`
 LEFT JOIN  concursos cgph ON cgph.`idSUBGRUPO` = p.idGRUPOPRODUCTOS
 LEFT JOIN  concursos cp ON fd.idPRODUCTOS = cp.`idPRODUCTOS`
 LEFT JOIN concursos c ON c.`idCONCURSOS` = cgp.`idCONCURSOS` OR
 c.`idCONCURSOS` = cgph.`idCONCURSOS` OR c.`idCONCURSOS` = cp.`idCONCURSOS`
 WHERE fd.`idFACTURAS` = 1    
    */
    
    
    
}
