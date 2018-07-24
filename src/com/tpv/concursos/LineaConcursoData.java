/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.concursos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author COMPUTOS
 */
public class LineaConcursoData {
    private LongProperty CodigoConcurso;
    private StringProperty TextoCorto;
    private BooleanProperty ImprimeTexto;
    private ObjectProperty VigenciaDesde;
    private ObjectProperty VigenciaHasta;
    private IntegerProperty CantidadProductos;
    private IntegerProperty CantidadConcursos;
    
    public LineaConcursoData(){
        
    }
    
    
    public LineaConcursoData(Long codigoProducto,String textoCorto,boolean imprimeTexto
            ,java.sql.Date vigenciaDesde, java.sql.Date vigenciaHasta
            ,int cantidadProductos, int cantidadConcursos){
        this.CodigoConcurso = new SimpleLongProperty(codigoProducto);
        this.TextoCorto = new SimpleStringProperty(textoCorto);
        this.ImprimeTexto = new SimpleBooleanProperty(imprimeTexto);
        this.VigenciaDesde = new SimpleObjectProperty(vigenciaDesde);
        this.VigenciaHasta = new SimpleObjectProperty(vigenciaHasta);
        this.CantidadConcursos = new SimpleIntegerProperty(cantidadConcursos);
        this.CantidadProductos = new SimpleIntegerProperty(cantidadProductos);
    }
    
    public LongProperty codigoConcursoProperty(){
        return getCodigoConcurso();
    }
    
    public StringProperty textoCortoProperty(){
        return getTextoCorto();
    }
    
    public BooleanProperty imprimeTextoProperty(){
        return getImprimeTexto();
    }
    
    public ObjectProperty vigenciaDesdeProperty(){
        return getVigenciaDesde();
    }
    
    public ObjectProperty vigenciaHastaProperty(){
        return getVigenciaHasta();
    }
    
    public IntegerProperty cantidadProductosProperty(){
        return getCantidadProductos();
    }
    
    public IntegerProperty cantidadConcursosProperty(){
        return getCantidadConcursos();
    }

    /**
     * @return the CodigoConcurso
     */
    public LongProperty getCodigoConcurso() {
        return CodigoConcurso;
    }

    /**
     * @return the TextoCorto
     */
    public StringProperty getTextoCorto() {
        return TextoCorto;
    }

    /**
     * @return the ImprimeTexto
     */
    public BooleanProperty getImprimeTexto() {
        return ImprimeTexto;
    }

    /**
     * @return the VigenciaDesde
     */
    public ObjectProperty getVigenciaDesde() {
        return VigenciaDesde;
    }

    /**
     * @return the VigenciaHasta
     */
    public ObjectProperty getVigenciaHasta() {
        return VigenciaHasta;
    }

    /**
     * @return the CantidadProductos
     */
    public IntegerProperty getCantidadProductos() {
        return CantidadProductos;
    }

    /**
     * @return the CantidadConcursos
     */
    public IntegerProperty getCantidadConcursos() {
        return CantidadConcursos;
    }
}
