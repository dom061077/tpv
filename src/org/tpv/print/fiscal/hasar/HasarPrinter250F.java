/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tpv.print.fiscal.hasar;

import java.math.BigDecimal;
import java.util.Map;
import org.tpv.print.fiscal.FiscalPacket;
import org.tpv.print.fiscal.comm.FiscalComm;
import org.tpv.print.fiscal.document.Customer;
import org.tpv.print.fiscal.exception.FiscalPrinterIOException;
import org.tpv.print.fiscal.exception.FiscalPrinterStatusError;
import static org.tpv.print.fiscal.hasar.HasarCommands.CMD_CLOSE_DNFH;
import static org.tpv.print.fiscal.hasar.HasarCommands.CMD_CLOSE_FISCAL_RECEIPT;
import static org.tpv.print.fiscal.hasar.HasarCommands.CMD_CLOSE_NON_FISCAL_RECEIPT;
import static org.tpv.print.fiscal.hasar.HasarCommands.CMD_OPEN_DRAWER;
import static org.tpv.print.fiscal.hasar.HasarCommands.CMD_PRINT_FISCAL_TEXT;
import static org.tpv.print.fiscal.hasar.HasarConstants.PST_MONEY_DRAWER_CLOSED;
import static org.tpv.print.fiscal.hasar.HasarFiscalPrinter.SIN_CALIFICADOR;
import org.tpv.print.fiscal.msg.FiscalMessage;
import org.tpv.print.fiscal.msg.MsgRepository;

/**
 *
 * @author daniel
 */
public class HasarPrinter250F extends HasarFiscalPrinter {
    
        
        private static final int FACTURA_B = 6;
        private static final int TIQUE_FACTURA_B = 82;
    
    
	/**
	 * 
	 */
	public HasarPrinter250F() {
		super();
	}

	/**
	 * @param fiscalComm
	 */
	public HasarPrinter250F(FiscalComm fiscalComm) {
		super(fiscalComm);
	}    
    
	public String formatAmount(BigDecimal amount) {
		amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		return amount.toString();
	}

	public String formatQuantity(BigDecimal quantity) {
		return quantity.toString();
	}
    
        @Override
	public FiscalPacket cmdOpenFiscalReceipt(String docType) {
                int codigoDoc=0;
                if(docType.compareTo("B")==0)
                    codigoDoc = FACTURA_B;
		FiscalPacket cmd = createFiscalPacket(CMD_OPEN_FISCAL_RECEIPT);
		int i = 1;
		cmd.setText(i++, "6", false);
		//cmd.setText(i++, "T", true);
		return cmd;
	}
        
    
	@Override
	protected FiscalPacket loadCustomerData(Customer customer) throws FiscalPrinterStatusError, FiscalPrinterIOException {
		// Este modelo no soporta el tipo de identificacion SIN_CALIFICADOR
		// ni el nº de identificacion nulo.
		
		// Se asume DNI 0
		if (traduceIdentificationType(customer.getIdentificationType()).equals(SIN_CALIFICADOR)) {
			customer.setIdentificationType(Customer.DNI);
		}
		String idNumber = customer.getIdentificationNumber();
		if (idNumber == null || idNumber.length() <1) {
			customer.setIdentificationNumber("0");
		}
		
		return super.loadCustomerData(customer);
	}

	@Override
	public FiscalPacket cmdPrintLineItem(String description, BigDecimal quantity, BigDecimal price, BigDecimal ivaPercent, boolean substract, BigDecimal internalTaxes, boolean basePrice, Integer display, int descMaxLength) {
		// Force display 0 and maxlength 20 to comply with model docs.
		//return cmdPrintLineItem(description, quantity, price, ivaPercent,substract, internalTaxes, basePrice, 0);
            
                
		FiscalPacket cmd = createFiscalPacket(CMD_PRINT_LINE_ITEM);
		int i = 1;
		cmd.setText(i++, description, descMaxLength, false);
		cmd.setNumber(i++, quantity,10,2, false,"");
		cmd.setNumber(i++,price,6,2,false,"");//cmd.setAmount(i++, price, false, true);
                
                
                cmd.setNumber(i++, 7, false);//Campo 4 Condición de IVA (opcional)
		if(ivaPercent == null)
			cmd.setText(i++, "**.**", false);
		else
			cmd.setNumber(i++, ivaPercent, 2, 2, false,"");		
		cmd.setBoolean(i++, substract, "m", "M", false);
                //TODO signo porcentaje en impuesto interno
                cmd.setText(i++, "0", false);//7. TipoImpuestoInterno
		cmd.setNumber(i++, internalTaxes, 10, 2, false,"");//cmd.setNumber(i++, internalTaxes, 10, 8, false,"");
		cmd.setNumber(i++, display, true);
		cmd.setBoolean(i++, basePrice, "x", "T", false);
                
                cmd.setNumber(i++,1,false);//Campo 11 Unidad de referencia (opcional)
                        
                cmd.setText(i++, "7790001001054", false);//campo 12 Código Producto/Servicio (opcional)
                cmd.setText(i++, "123", false);//campo 13 Código interno (opcional)
                cmd.setNumber(i++, 7, false);//campo 14 Código unidad de medida (opcional)
                
                
		return cmd;                
		
	}
	
	protected Map<Integer, FiscalMessage> getPrinterStatusMsgs() {
		// This model doesn't have money drawer, the status pointing its absence is not an error.		
		Map<Integer,FiscalMessage> st = super.getPrinterStatusMsgs();
		st.remove(PST_MONEY_DRAWER_CLOSED);
		st.put(PST_MONEY_DRAWER_CLOSED, MsgRepository.getFiscalMsg(PST_MONEY_DRAWER_CLOSED, "PstMoneyDrawerClosedTitle", "PstMoneyDrawerClosedDesc", false));
		
		
		return st;
	}	
	
	@Override
	public FiscalPacket cmdPrintFiscalText(String text, Integer display) {
		FiscalPacket cmd = createFiscalPacket(CMD_PRINT_FISCAL_TEXT);
		int i = 1;
                cmd.setText(i++, "04", false);
		cmd.setText(i++, text, 30, false);
		cmd.setNumber(i++, display == null?0:display, true);
		return cmd;
	}

	@Override
	public FiscalPacket cmdLastItemDiscount(String description, BigDecimal amount, boolean substract, boolean baseAmount, Integer display) {
		// El parámetro display no tiene utilidad en este modelo.
		// Siempre se asigna a 0.
		return super.cmdLastItemDiscount(description, amount, substract, baseAmount, 0);
	}

	@Override
	public FiscalPacket cmdSubtotal(boolean print, Integer display) {
		// El parámetro display no tiene utilidad en este modelo.
		// Siempre se asigna a 0.
		return super.cmdSubtotal(print, 0);
	}

	@Override
	public FiscalPacket cmdTotalTender(String description, BigDecimal amount, boolean cancel, Integer display) {
		// El parámetro display no tiene utilidad en este modelo.
		// Siempre se asigna a 0.
		FiscalPacket cmd = super.cmdTotalTender(description, amount, cancel, 0);
		cmd.setText(1, description, 30, false);
		return cmd;
	}
	
	@Override
	public FiscalPacket cmdCloseFiscalReceipt(Integer copies) {
		// Este modelo no acepta el parámetro de copias.
		FiscalPacket cmd = createFiscalPacket(CMD_CLOSE_FISCAL_RECEIPT);
		return cmd; 
	}
	
	@Override
	public FiscalPacket cmdCloseDNFH(Integer copies) {
		// Este modelo no acepta el parámetro de copias.
		FiscalPacket cmd = createFiscalPacket(CMD_CLOSE_DNFH);
		return cmd; 
	}
	
	public FiscalPacket cmdSetCustomerData(String name, String customerDocNumber, String ivaResponsibility, String docType, String location) {
		FiscalPacket cmd = super.cmdSetCustomerData(name, customerDocNumber, ivaResponsibility, docType, location);
		cmd.setText(1, name, 40, true);
		cmd.setText(5, location, 40, true);
		return cmd;
	}
	
	@Override
	protected FiscalPacket cmdReturnRecharge(String description,
			BigDecimal amount, BigDecimal ivaPercent, boolean subtract,
			BigDecimal internalTaxes, boolean baseAmount, Integer display,
			String operation, int descMaxLength) {
		// El parámetro display no tiene utilidad en este modelo.
		// Siempre se asigna a 0.
		descMaxLength = 20;
		return super.cmdReturnRecharge(description, amount, ivaPercent, subtract,
				internalTaxes, baseAmount, 0, operation, descMaxLength);
	}

	@Override
	public FiscalPacket cmdGeneralDiscount(String description,
			BigDecimal amount, boolean substract, boolean baseAmount,
			Integer display) {
		// El parámetro display no tiene utilidad en este modelo.
		// Siempre se asigna a 0.
		return super.cmdGeneralDiscount(description, amount, substract, baseAmount,
				0);
	}

	@Override
	public FiscalPacket cmdPrintNonFiscalText(String text, Integer display) {
		FiscalPacket cmd = super.cmdPrintNonFiscalText(text, display);
		cmd.setText(1, text, 40, false);
		return cmd;
	}

	@Override
	public FiscalPacket cmdCloseNonFiscalReceipt(Integer copies) {
		FiscalPacket cmd = createFiscalPacket(CMD_CLOSE_NON_FISCAL_RECEIPT);
		return cmd;
	}
	
	@Override
	public FiscalPacket cmdOpenDrawer(){
		FiscalPacket cmd = createFiscalPacket(CMD_OPEN_DRAWER);
		return cmd;
	}
    
    
}
