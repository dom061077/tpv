/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpv.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 * @author COMPUTOS
 */
public class Util {
    public static String formatearDecimal(BigDecimal numero){
        DecimalFormat df = new DecimalFormat("##,###,###,##0.00");
        return df.format(numero);
    }
}
