/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.render.dialogs;

import java.util.List;
import java.util.Locale;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterLocation;
import javax.print.attribute.standard.PrinterMakeAndModel;
import javax.print.attribute.standard.PrinterMessageFromOperator;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.PrinterState;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintDestination;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

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

	private Label resultStatusLabel;
	private Label resultTypeLabel;
	private Label resultWhereLabel;
	private Label resultCommentLabel;

	PrinterBlock(IDialogUnitConverter dluConverter,
			DataBindingContext bindings, PrintOptions options) {

		super(dluConverter);

		this.bindings = bindings;
		this.options = options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.printing.internal.dialogs.DialogBlock#createContents(org.eclipse.swt.widgets.Composite)
	 */
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
		
		// combo.addSelectionChangedListener(new ISelectionChangedListener() {
		//
		// public void selectionChanged(SelectionChangedEvent event) {
		// if (event != null) {
		// handlePrinterSelectionChanged(event);
		// }
		// }
		//		});

		layoutFillHorizontal(combo.getControl());

		Button propertiesButton = button(result,
				DiagramUIPrintingMessages.JPSPrintDialog_Properties);
		propertiesButton.setEnabled(false);

		propertiesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO: Introduce new dialog in phase 2
			}
		});

		IObservableValue destination = BeansObservables.observeValue(realm,
				options, PrintOptions.PROPERTY_DESTINATION);
		bindings.bindValue(ViewersObservables.observeSingleSelection(combo),
				destination, null, null);
		
		// Label statusLabel = label(result, "Status:");
		// layoutAlignLeft(statusLabel);
		// resultStatusLabel = label(result, "");
		// layoutFillHorizontal(layoutSpanHorizontal(resultStatusLabel, 2));
		//				
		// Label typeLabel = label(result, "Type:");
		// layoutAlignLeft(typeLabel);
		// resultTypeLabel = label(result, "");
		// layoutFillHorizontal(layoutSpanHorizontal(resultTypeLabel, 2));
		// 
		// Label whereLabel = label(result, "Where:");
		// layoutAlignLeft(whereLabel);
		// resultWhereLabel = label(result, "");
		// layoutFillHorizontal(layoutSpanHorizontal(resultWhereLabel, 2));
		// 
		// Label commentLabel = label(result, "Comment:");
		// layoutAlignLeft(commentLabel);
		// resultCommentLabel = label(result, "");
		// layoutFillHorizontal(resultCommentLabel);

		Button printToFile = check(result,
				DiagramUIPrintingMessages.JPSPrintDialog_PrintToFile);
		layoutSpanHorizontal(printToFile, 3);

		// TODO: implement in phase 2
		printToFile.setEnabled(false);

		init();

		return result;
	}

	/**
	 * When the printer selection has changed, we update the status information.
	 * The print service API does not currently update the status correctly in Windows
	 * 
	 * This will be re-enabled when a workaround for obtaining printer attributes will 
	 * be discovered.
	 * 
	 * @param selectionChangedEvent
	 */
	private void handlePrinterSelectionChanged(
			SelectionChangedEvent selectionChangedEvent) {

		StructuredSelection selection = (StructuredSelection) selectionChangedEvent
				.getSelection();

		if (selection != null) {
			PrintDestination destination = (PrintDestination) selection
					.getFirstElement();
			if (destination != null) {

				String printerName = destination.getName();

				AttributeSet attributes = new HashPrintServiceAttributeSet(
						new PrinterName(printerName, Locale.getDefault()));

				PrintService[] services = PrintServiceLookup
						.lookupPrintServices(
								DocFlavor.SERVICE_FORMATTED.PRINTABLE,
								attributes);

				PrintService printService = services[0];

				PrintServiceAttributeSet printServiceAttributes = printService
						.getAttributes();

				PrinterState printerState = (PrinterState) printServiceAttributes
						.get(PrinterState.class);

				PrinterLocation printerLocation = (PrinterLocation) printServiceAttributes
						.get(PrinterLocation.class);

				PrinterMakeAndModel printerMakeAndModel = (PrinterMakeAndModel) printServiceAttributes
						.get(PrinterMakeAndModel.class);

				PrinterMessageFromOperator printerComment = (PrinterMessageFromOperator) printServiceAttributes
						.get(PrinterMessageFromOperator.class);

				if (printerState != null) {
					resultStatusLabel.setText(printerState.getName());
				}
				if (printerLocation != null) {
					resultWhereLabel.setText(printerLocation.getName());
				}
				if (printerComment != null) {
					resultCommentLabel.setText(printerComment.getName());
				}
				if (printerMakeAndModel != null) {
					resultTypeLabel.setText(printerMakeAndModel.getName());
				}
			}
		}
	}

	/**
	 * At initialization time, we lookup all the print services and select the default one.
	 */
	private void init() {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(
				null, null);
		PrintService defaultPrintService = PrintServiceLookup
				.lookupDefaultPrintService();

		for (PrintService printService : printServices) {
			destinations.add(new PrintDestination(printService.getName()));
		}
		
		PrintDestination defaultPrintDestination = getPrinterByName(defaultPrintService.getName());

		combo.setContentProvider(new PrinterContentProvider());
		combo.setLabelProvider(new PrinterLabelProvider());

		initializePrinterCombo(defaultPrintDestination);
	}

	/**
	 * Initialize the list of printers.
     *
	 * @param selection
	 */
	private void initializePrinterCombo(PrintDestination selection) {
		combo.setInput(destinations);
		combo.setSelection(new StructuredSelection(selection));
	}

	/**
	 * 
	 * @param name
	 * @return
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

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// input never changes
		}

		public void dispose() {
			// nothing to dispose
		}
	}

	private class PrinterLabelProvider extends LabelProvider {
		public String getText(Object element) {
			return ((PrintDestination) element).getName();
		}
	}
}
