/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.render.model;

import org.eclipse.swt.printing.PrinterData;


/**
 * This class is used as part of the infrastructure required for data-bindings
 * used with the JPS dialog.
 *
 * @author Christian Damus (cdamus)
 * @author James Bruck (jbruck)
 */
public class PrintDestination extends PrintModelElement {
    public static String PROPERTY_NAME = "name"; //$NON-NLS-1$
    public static String PROPERTY_PRINT_TO_FILE = "printToFile"; //$NON-NLS-1$
    public static String PROPERTY_FILE_PATH = "filePath"; //$NON-NLS-1$
    
   
    protected PrinterData printerData;
           
    public PrinterData getPrinterData(){
    	if(printerData == null){
    		printerData = new PrinterData();
    	}
    	return printerData;
    }
    
    public PrintDestination(String printerName) {
    	getPrinterData().name = printerName;
    }
    
    public String getName() {
        return getPrinterData().name;
    }
    
    public void setName(String name) {
        String oldName = this.getPrinterData().name;
        this.getPrinterData().name = name;
        firePropertyChange(PROPERTY_NAME, oldName, name);
    }
    
    public boolean isPrintToFile() {
        return getPrinterData().printToFile;
    }
    
    public void setPrintToFile(boolean printToFile) {
        boolean oldToFile = getPrinterData().printToFile;
        getPrinterData().printToFile = printToFile;
        firePropertyChange(PROPERTY_PRINT_TO_FILE, oldToFile, printToFile);
    }
    
    public String getFilePath() {
        return getPrinterData().fileName;
    }
    
    public void setFilePath(String filePath) {
        String oldPath = getPrinterData().fileName;
        getPrinterData().fileName = filePath;
        firePropertyChange(PROPERTY_FILE_PATH, oldPath, filePath);
    }
      
}
