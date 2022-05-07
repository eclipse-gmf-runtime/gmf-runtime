/******************************************************************************
 * Copyright (c) 2008, 2010, 2022 IBM Corporation and others.
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

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintDestination;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 * A section of the JPS print dialog that adds printer options.
 * 
 * @author Christian Damus (cdamus)
 * @author James Bruck (jbruck)
 */
class PrinterBlock extends DialogBlock {
	private final DataBindingContext bindings;
	private final PrintOptions options;

	private List<PrintDestination> destinations = new java.util.ArrayList<PrintDestination>();

	private ComboViewer combo;

	PrinterBlock(IDialogUnitConverter dluConverter,
			DataBindingContext bindings, PrintOptions options) {

		super(dluConverter);

		this.bindings = bindings;
		this.options = options;
	}

	@Override
	public Control createContents(Composite parent) {
		final Realm realm = bindings.getValidationRealm();

		Composite result = group(parent,
				DiagramUIPrintingMessages.JPSPrintDialog_Printer);
		layout(result, 3);

		label(result, DiagramUIPrintingMessages.JPSPrintDialog_Name);
		combo = combo(result);
		//
		// The JPS API does not correctly return the printer status
		// information under windows. These options will be temporarily disabled
		// until a workaround can be discovered.
		//
		
		//		 combo.addSelectionChangedListener(new ISelectionChangedListener() {
		//		
		//		 public void selectionChanged(SelectionChangedEvent event) {
		//				if (event != null) {
		//					handlePrinterSelectionChanged(event);
		//				}
		//			}
		//		});

		layoutFillHorizontal(combo.getControl());

		Button propertiesButton = button(result,
				DiagramUIPrintingMessages.JPSPrintDialog_Properties);
		propertiesButton.setEnabled(true);

		propertiesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				 openPrintOptionsDialog();
			}
		});

		IObservableValue<Object> destination = BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_DESTINATION).observe(realm, options);
		bindings.bindValue(ViewerProperties.singleSelection().observe(combo),
				destination, null, null);

		initializePrinters();

		return result;
	}

	/**
	 * At initialization time, we lookup all the print services and select the default one.
	 */
	private void initializePrinters() {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(
				null, null);
		
		for (PrintService printService : printServices) {
			destinations.add(new PrintDestination(printService.getName()));
		}
		combo.setContentProvider(new PrinterContentProvider());
		combo.setLabelProvider(new PrinterLabelProvider());
		
		// Locate a default printer if one is explicitly marked as such.
		PrintDestination defaultPrintDestination = null;
		PrintService defaultPrintService = PrintServiceLookup
			.lookupDefaultPrintService();
		
		if (defaultPrintService != null) {
			defaultPrintDestination = getPrinterByName(defaultPrintService
				.getName());
		} else {
			if( destinations.size() > 0 ){
				defaultPrintDestination = destinations.get(0);
			}
		}
		
		combo.setInput(destinations);
		if(defaultPrintDestination != null){
			combo.setSelection(new StructuredSelection(defaultPrintDestination));
		}
	}

	
	/**
	 * Obtain the proper print destination based on the name of the printer.
	 * 
	 * @param name 
	 * 		The printer Name.
	 * @return
	 * 		 The print destination corresponding to the specified print name.
	 */	
	private PrintDestination getPrinterByName(String name) {

		PrintDestination result = null;
		for (PrintDestination printDestination : destinations) {
			if (printDestination.getName().equals(name)) {
				result = printDestination;
				break;
			}
		}
		return result;
	}

	/**
	 * A helper class used to retrieve label text
	 * 
	 * @author Christian Damus (cdamus)
	 * @author James Bruck (jbruck)
	 */
	private class PrinterContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			return destinations.toArray();
		}
		
	    @Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// input never changes
		}
	    
	    @Override
		public void dispose() {
			// nothing to dispose
		}
	}

	private class PrinterLabelProvider extends LabelProvider {
	    @Override
		public String getText(Object element) {
			return ((PrintDestination) element).getName();
		}
	}
		
	private void openPrintOptionsDialog() {
		JPSOptionsDialog dlg = new JPSOptionsDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), bindings, options);
		dlg.open();
	}

	@Override
	public void dispose() {
		// nothing special to dispose currently	
	}
}
