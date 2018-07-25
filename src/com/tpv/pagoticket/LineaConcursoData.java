/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.pagoticket;

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
    private LongProperty IdFacDetConcurso;
    private StringProperty TextoCorto;
    private BooleanProperty ImprimeTexto;
    private ObjectProperty VigenciaDesde;
    private ObjectProperty VigenciaHasta;
    private IntegerProperty CantidadProductos;
    private IntegerProperty CantidadConcursos;
    
    public LineaConcursoData(){
        
    }
    
    
    public LineaConcursoData(Long IdFacDetConcurso,String textoCorto,boolean imprimeTexto
            ,java.sql.Date vigenciaDesde, java.sql.Date vigenciaHasta
            ,int cantidadProductos, int cantidadConcursos){
        this.IdFacDetConcurso = new SimpleLongProperty(IdFacDetConcurso);
        this.TextoCorto = new SimpleStringProperty(textoCorto);
        this.ImprimeTexto = new SimpleBooleanProperty(imprimeTexto);
        this.VigenciaDesde = new SimpleObjectProperty(vigenciaDesde);
        this.VigenciaHasta = new SimpleObjectProperty(vigenciaHasta);
        this.CantidadConcursos = new SimpleIntegerProperty(cantidadConcursos);
        this.CantidadProductos = new SimpleIntegerProperty(cantidadProductos);
    }
    
    public LongProperty idFacDetConcursoProperty(){
        return IdFacDetConcurso;
    }
    
    public StringProperty textoCortoProperty(){
        return TextoCorto;
    }
    
    public BooleanProperty imprimeTextoProperty(){
        return ImprimeTexto;
    }
    
    public ObjectProperty vigenciaDesdeProperty(){
        return VigenciaDesde;
    }
    
    public ObjectProperty vigenciaHastaProperty(){
        return VigenciaHasta;
    }
    
    public IntegerProperty cantidadProductosProperty(){
        return CantidadProductos;
    }
    
    public IntegerProperty cantidadConcursosProperty(){
        return CantidadConcursos;
    }

    /**
     * @return the CodigoConcurso
     */
    public Long getIdFactDetConcurso() {
        return IdFacDetConcurso.get();
    }

    /**
     * @return the TextoCorto
     */
    public String getTextoCorto() {
        return TextoCorto.get();
    }

    /**
     * @return the ImprimeTexto
     */
    public boolean getImprimeTexto() {
        return ImprimeTexto.get();
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
