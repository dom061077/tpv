SELECT * FROM combos c
INNER JOIN combosdetalle cd ON c.idCOMBOS=cd.iCOMBOS


SELECT * FROM productos WHERE 148,149,150


SELECT * FROM grupoproductos WHERE idgrupoproductos IN (148,149,150)



SELECT * FROM combosgrupo

SELECT * FROM combosgrupodetalle

SELECT c.`idCOMBOS`, c.`DESCRIPCION`,cg.idCOMBOSGRUPO,cg.cantidadproducto,cg.porcentaje,cg.monto,cg.bonificacion_en_menor_precio
,'--' AS separagrupo
,cgd.idproductos,cgd.idgrupoproductos,cgd.idproveedor FROM combos c
INNER JOIN combosgrupo cg ON c.`idCOMBOS` = cg.`idCOMBOS`
INNER JOIN combosgrupodetalle cgd ON cg.`idCOMBOSGRUPO`=cgd.`idCOMBOSGRUPO`
#WHERE c.`idCOMBOS`=6
ORDER BY c.`idCOMBOS`


SHOW VARIABLES

SELECT p.idproductos,p.codigoproducto,idgrupoproductos,detalle,detallesucursales,lp.preciopublico
,lp.preciolista FROM productos p
INNER JOIN listaprecios_productos lp ON p.idproductos = lp.idproductos
WHERE idgrupoproductos IN (148,149,150)
AND p.codigoproducto IN (9024,9018)



SELECT idproductos,codigoproducto,DETALLESUCURSALES,`detalle`
	,impuestointerno
 FROM productos WHERE idproductos IN (77364,77327,7979)




SELECT MAX(idfacturas) FROM facturas


                SELECT DISTINCT c.* FROM facturasdetalle fd
                 INNER JOIN productos p ON fd.idPRODUCTOS=p.idPRODUCTOS AND p.DISCONTINUADO = 0
                 LEFT JOIN (
                       SELECT gp.idGRUPOPRODUCTOS AS grupohijo
                                ,glevel1.idGRUPOPRODUCTOS AS grupopadre FROM grupoproductos gp
                        INNER JOIN grupoproductos glevel1 ON glevel1.idgrupoproductos = gp.padreid
                 ) grupoprod ON (p.idgrupoproductos = grupoprod.grupohijo OR p.idgrupoproductos = grupoprod.grupopadre)
                 LEFT JOIN proveedores_productos pp ON fd.idPRODUCTOS = pp.idPRODUCTOS        
                 LEFT JOIN combosgrupodetalle cgd ON fd.idPRODUCTOS = cgd.idproductos OR grupoprod.grupohijo = cgd.idGRUPOPRODUCTOS
				OR grupoprod.grupopadre = cgd.idGRUPOPRODUCTOS OR pp.idProveedor=cgd.idProveedor
                 LEFT JOIN combosgrupo cg ON cgd.idCOMBOSGRUPO = cg.idCOMBOSGRUPO
                 LEFT JOIN combos c ON cg.idCOMBOS = c.idCOMBOS
                 WHERE c.idcombos IS NOT NULL AND fd.idFACTURAS = 686 AND CONVERT(NOW(),DATE) BETWEEN c.FECHADESDE AND c.FECHAHASTA
                 ORDER BY c.PRIORIDAD


