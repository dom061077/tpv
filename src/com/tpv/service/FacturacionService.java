/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.service;

import com.tpv.exceptions.TpvException;
import com.tpv.modelo.AlicuotaIngresosBrutos;
import com.tpv.modelo.Combo;
import com.tpv.modelo.ComboGrupo;
import com.tpv.modelo.ComboGrupoDetalle;
import com.tpv.modelo.Concurso;
import com.tpv.modelo.CondicionIva;
import com.tpv.modelo.Factura;
import com.tpv.modelo.FacturaDetalle;
import com.tpv.modelo.FacturaDetalleCombo;
import com.tpv.modelo.FacturaDetalleComboAbierto;
import com.tpv.modelo.FacturaDetalleConcurso;
import com.tpv.modelo.FacturaFormaPagoDetalle;
import com.tpv.modelo.ProductoAgrupadoEnFactura;
import com.tpv.modelo.Usuario;
import com.tpv.modelo.enums.FacturaEstadoEnum;
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
import java.util.ArrayList;

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
            factura.setCondicionIva(em.find(CondicionIva.class, 1));
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

    public Factura getFacturaAbiertaPorCheckout(int idCheckout, int usuarioId) throws TpvException{
        Factura factura = null;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            Query q = em.createQuery("FROM Factura f WHERE f.estado = :estado and f.checkout.id = :idCheckout "
                        +" and f.usuario.id = :usuarioId")
                        .setParameter("usuarioId",usuarioId)
                        .setParameter("estado", FacturaEstadoEnum.ABIERTA)
                        .setParameter("idCheckout", idCheckout);
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
            factura.setFechaHoraCierre(factura.getFechaHoy());
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
    
    public void anularFacturaPorReinicio(Long id) throws TpvException{
        anularFactura(id,null,FacturaEstadoEnum.ANULADA);
    }
    
    private void anularFactura(Long id,Usuario usuarioSupervisor, FacturaEstadoEnum estadoCancelacion) throws TpvException{
        log.info("Capa de servicios, cancelar factura");
        Factura factura;
        EntityManager em = Connection.getEm();
        EntityTransaction tx = null;
        try{
            tx = em.getTransaction();
            tx.begin();
            factura = em.find(Factura.class, id);
            factura.setUsuarioModificacion(usuarioSupervisor);
            factura.setEstado(estadoCancelacion);
            tx.commit();
            log.info("Factura con id: "+factura.getId()+", Nro. factura: "
                      +factura.getNumeroComprobante());   
        }catch(RuntimeException e){
            tx.rollback();
            log.error("Error en la capa de servicios al cancelar la factura",e);
            throw new TpvException("Error en la capa de servicios al cancelar la factura. "+e.getMessage());
        }finally{
            em.clear();
        }
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
                +" LEFT JOIN combos c ON cg.idCOMBOS = c.idCOMBOS"
                +" WHERE c.idcombos IS NOT NULL AND fd.idFACTURAS = ?1 AND CONVERT(NOW(),DATE) BETWEEN c.FECHADESDE AND c.FECHAHASTA"
                +" ORDER BY c.PRIORIDAD"
                , Combo.class).setParameter(1, id);
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
//                        fd.setBonificacion(combo.getBonificacion().setScale(2,BigDecimal.ROUND_HALF_EVEN));
//                    else    
//                        fd.setBonificacion(combo.getBonificacionSinCombinacion().setScale(2,BigDecimal.ROUND_HALF_EVEN));
//                    factura.getDetalleCombosAux().add(fd);
//                }
                int cantidadArmada = combo.getCantidadArmada();
                if(cantidadArmada>0){
                    FacturaDetalleCombo fd = new FacturaDetalleCombo();
                    fd.setCombo(combo);
                    fd.setCantidad(cantidadArmada);
                    fd.setBonificacion(combo.getBonificacionFinal().setScale(2,BigDecimal.ROUND_HALF_EVEN));
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
                facturaDetalle.setDescuento(BigDecimal.ZERO);
                facturaDetalle.setExento(fdc.getExentoBonif());
                facturaDetalle.setImpuestoInterno(fdc.getImpuestoInterno().multiply(BigDecimal.valueOf(-1)));
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
                fd.getProducto().decStock(fd.getCantidad());
                total=total.add(fd.getSubTotal());
                costo = costo.add(fd.getPrecioUnitario());
                neto = neto.add(fd.getNeto());
                netoReducido = netoReducido.add(fd.getNetoReducido());
                descuento = descuento.add(fd.getDescuento());
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
        factura.setIvaTarjeta(totalInteresPago);
        BigDecimal totalFactura = factura.getTotal();
        totalFactura = totalFactura.subtract(totalBonificacionPago).add(totalInteresPago);
        factura.setTotal(totalFactura);
        //---------CARGANDO CONCURSOS -----
        EntityManager em = Connection.getEm();
        try{
                Query q = em.createNativeQuery(
                            si la factura tiene anulaciones, no sumar las mismas
                                    para los concursos
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
                            +" GROUP BY c.idCONCURSOS,c.TEXTOCORTO,c.CANTIDADPRODUCTOS,c.CANTIDADCUPONES"
                            +" HAVING (SUM(fd.CANTIDAD) DIV c.CANTIDADPRODUCTOS)*c.CANTIDADCUPONES>0")
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
                            ).setParameter("idUsuario", idUsuario)
                            .setParameter("idCheckout",idCheckout)
                                ;
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
