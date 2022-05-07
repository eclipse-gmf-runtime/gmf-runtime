/******************************************************************************
 * Copyright (c) 2008, 2022 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/



package org.eclipse.gmf.runtime.diagram.ui.printing.render.dialogs;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.databinding.swt.DisplayRealm;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


/**
 * A dialog that supports platform independent printing based on the 
 * Java Printing Service API.
 * 
 * @author Christian Damus (cdamus)
 * @author James Bruck (jbruck)
 */
public class JPSPrintDialog
    extends TrayDialog {

    private DataBindingContext bindings;
    private final PrintOptions options;
    
    protected PrinterBlock printerBlock;
    protected DiagramPrintRangeBlock diagramPrintRangeBlock;
    protected ScalingBlock scalingBlock;
    private RangeBlock rangeBlock;
    private CopiesBlock copiesBlock;
    private ActionsBlock actionsBlock;
    
    private List<String> allDiagrams;
    
    private final DialogBlock.IDialogUnitConverter dluConverter = new DialogBlock.IDialogUnitConverter() {

		public int convertHorizontalDLUsToPixels(int dlus) {
			return JPSPrintDialog.this.convertHorizontalDLUsToPixels(dlus);
		}

		public Shell getShell() {
			return JPSPrintDialog.this.getShell();
		}
	};
    
    public JPSPrintDialog(IShellProvider parentShell, PrintOptions options, List<String> allDiagrams) {
        super(parentShell);
        this.options = options;
        this.allDiagrams = allDiagrams;
    }

    public JPSPrintDialog(Shell shell, PrintOptions options, List<String> allDiagrams) {
        super(shell);
        this.options = options;
        this.allDiagrams = allDiagrams;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        
        newShell.setText(DiagramUIPrintingMessages.JPSPrintDialog_Title);
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
		bindings = new DataBindingContext(DisplayRealm.getRealm(parent.getDisplay()));

		Composite result = new Composite(parent, SWT.NONE);
		DialogBlock.layout(result, 2);

		createPrinterBlockArea(result);
		createDiagramPrintRangeBlockArea(result);
		createScalingBlockArea(result);
		createRangeBlockArea(result);
		createCopiesBlockArea(result);
		createActionsBlockArea(result);
		createExtensibleBlockArea(result);

		return result;
	}
    
    protected void createPrinterBlockArea(Composite result) {
		printerBlock = new PrinterBlock(dluConverter, bindings, options);
		printerBlock.layoutSpanHorizontal(printerBlock.createContents(result),
				2);
	}
    
    protected void createScalingBlockArea(Composite result) {
    	 scalingBlock = new ScalingBlock(dluConverter, bindings, options);
         scalingBlock.layoutSpanHorizontal(scalingBlock.createContents(result), 2);
	}
    
    protected void createRangeBlockArea(Composite result) {
    	 rangeBlock = new RangeBlock(dluConverter, bindings, options);
         rangeBlock.createContents(result);
	}
    
    protected void createCopiesBlockArea(Composite result) {
    	  copiesBlock = new CopiesBlock(dluConverter, bindings, options);
          copiesBlock.createContents(result);
	}
    
    protected void createActionsBlockArea(Composite result) {
    	 actionsBlock = new ActionsBlock(dluConverter, options);
         actionsBlock.layoutSpanHorizontal(actionsBlock.createContents(result), 2);
	}
    
    protected void createDiagramPrintRangeBlockArea(Composite result){
    	diagramPrintRangeBlock = new DiagramPrintRangeBlock(dluConverter,bindings,options, allDiagrams);
    	diagramPrintRangeBlock.layoutSpanHorizontal(diagramPrintRangeBlock.createContents(result),
				2);
    }
    
    protected void createExtensibleBlockArea(Composite result) {
    	  // meant to be overridden by subclasses to add additional blocks.
  	}
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }
    
    @Override
    protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case -1:
			break;
		default:
			super.buttonPressed(buttonId);
		}
	}

    @Override
	public boolean close() {
		bindings.dispose();
		copiesBlock.dispose();
		printerBlock.dispose();
		diagramPrintRangeBlock.dispose();
		scalingBlock.dispose();
		rangeBlock.dispose();
		actionsBlock.dispose();
		return super.close();
	}
    
    /**
	 * Obtains the user's selected printing options, or <code>null</code> if
	 * the user canceled the print operation.
	 * 
	 * @return the printing options, or <code>null</code> if canceled
	 */
    public PrintOptions getPrintOptions() {
        return options;
    }
}
