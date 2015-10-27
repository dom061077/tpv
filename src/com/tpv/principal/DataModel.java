/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.principal;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.datafx.controller.flow.injection.FlowScoped;
import org.datafx.samples.app.Person;

/**
 *
 * @author daniel
 */

@FlowScoped
public class DataModel {
    private ListProperty<LineaTicket> tickets;
    
    public ListProperty<LineaTicket> getTickets() {
    if (tickets == null) {
        ObservableList<LineaTicket> innerList = FXCollections.observableArrayList();
        tickets = new SimpleListProperty<>(innerList);
    }
    return tickets;
}
    
}
