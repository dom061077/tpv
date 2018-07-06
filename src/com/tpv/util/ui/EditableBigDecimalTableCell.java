/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.converter.NumberStringConverter;

/**
 *
 * @author COMPUTOS
 */
public class EditableBigDecimalTableCell<T> extends TableCell<T, Integer> {
private TextField textField;
private int minDecimals, maxDecimals;
/**
 * This is the default - we will use this as 2 decimal places
 */
public EditableBigDecimalTableCell () {
    minDecimals = 0;
    maxDecimals = 0;
}

/**
 * Used when the cell needs to have a different behavior than 2 decimals
 */
public EditableBigDecimalTableCell (int min, int max) {
    minDecimals = min;
    maxDecimals = max;
}

@Override
public void startEdit() {
    if(editableProperty().get()){
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setGraphic(textField);
            textField.setText(getItem().toString());
            textField.requestFocus();
        }
    }
}

@Override
public void cancelEdit() {
    super.cancelEdit();
    setText(getItem() != null ? getItem().toString() : null);
    setGraphic(null);
}

@Override
public void updateItem(Integer item, boolean empty) {
    super.updateItem(item, empty);
    if (empty) {
        setText(null);
        setGraphic(null);
    } else {
        if (isEditing()) {
            if (textField != null) {
                textField.setText(getString());
                textField.selectAll();
            }
            setText(null);
            setGraphic(textField);
        } else {
            //DecimalFormat df = new DecimalFormat("##,##0");
            //setText(df.format(Integer.valueOf(getString().replace(".", ""))));
            setText(getString());
            setGraphic(null);
        }
    }
}

private void createTextField() {
    //textField = new TextField();
    textField = new MaskTextField();
    ((MaskTextField)textField).setMask("N!");
    ((MaskTextField)textField).setMaxDigitos(5);
    //textField.setTextFormatter(new DecimalTextFormatter(minDecimals, maxDecimals));
    
    
    
    

    
    textField.setOnAction(evt -> {
        if(textField.getText() != null && !textField.getText().isEmpty()){
            NumberStringConverter nsc = new NumberStringConverter();
            Number n = nsc.fromString(textField.getText());
            commitEdit(Integer.valueOf(n.intValue()));
        }
    });
    textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

    textField.setOnKeyPressed((ke) -> {
        if (ke.getCode().equals(KeyCode.ESCAPE)) {
            cancelEdit();
        }
        
    });

    textField.setAlignment(Pos.CENTER_RIGHT);
    this.setAlignment(Pos.CENTER_RIGHT);
}

private String getString() {
    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMinimumFractionDigits(minDecimals);
    nf.setMaximumFractionDigits(maxDecimals);
    return getItem() == null ? "" : nf.format(getItem());
}

@Override
public void commitEdit(Integer item) {

    if (!isEditing()) {
        super.commitEdit(item);        
    } else {
        final TableView<T> table = getTableView();
        if (table != null) {
            TablePosition<T, Integer> position = new TablePosition<T, Integer>(getTableView(),
                    getTableRow().getIndex(), getTableColumn());
            CellEditEvent<T, Integer> editEvent = new CellEditEvent<T, Integer>(table, position,
                    TableColumn.editCommitEvent(), item);
            Event.fireEvent(getTableColumn(), editEvent);
        }
        updateItem(item, false);
        if (table != null) {
            table.edit(-1, null);
            
        }

    }
}

}