/******************************************************************************
 * Copyright (c) 2008, 2014, 2022 IBM Corporation and others.
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
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A section of the print dialog that handles selection of diagrams to print.
 * 
 * @author James Bruck (jbruck)
 */
public class DiagramPrintRangeBlock extends DialogBlock {

	private final DataBindingContext bindings;

	private final PrintOptions options;

	private ListViewer diagramList;

	private List<String> availableDiagrams;

	DiagramPrintRangeBlock(IDialogUnitConverter dluConverter,
			DataBindingContext bindings, PrintOptions options,
			List<String> availableDiagrams) {

		super(dluConverter);

		this.bindings = bindings;
		this.options = options;
		this.availableDiagrams = availableDiagrams;
	}

	/**
	 * Create the contents of the diagram selection block. It contains 2 radio
	 * buttons for current or multi-selection and a list box of diagram options.
	 */
	@Override
	public Control createContents(Composite parent) {

		final Realm realm = bindings.getValidationRealm();

		Composite result = group(parent,
				DiagramUIPrintingMessages.JPSOptionsDialog_DiagramPrintRange);
		layout(result, 3);

		Button currentDiagramRadio = radio(result,
				DiagramUIPrintingMessages.JPSOptionsDialog_DiagramPrintCurrent);
		layoutSpanHorizontal(currentDiagramRadio, 4);

		Button selectedDiagramsRadio = radio(result,
				DiagramUIPrintingMessages.JPSOptionsDialog_DiagramPrintSelected);
		layoutSpanHorizontal(selectedDiagramsRadio, 4);

		diagramList = list(result);
				
		layoutFillHorizontal(layoutHeight(diagramList.getControl(),48));
		GridData data = getLayoutData(diagramList.getControl());
		data.widthHint = 300;
		
		diagramList.getControl().setEnabled(options.isDiagramSelection());

		currentDiagramRadio.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//do nothing
			}
			public void widgetSelected(SelectionEvent event) {
				diagramList.getControl().setEnabled(
						options.isDiagramSelection());
			}
		});

		selectedDiagramsRadio.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
				//do nothing
			}
			public void widgetSelected(SelectionEvent event) {
				diagramList.getControl()
						.setEnabled(!options.isDiagramCurrent());
			}
		});

		diagramList
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@SuppressWarnings("unchecked")
					public void selectionChanged(SelectionChangedEvent event) {
						if (event != null) {

							StructuredSelection selection = (StructuredSelection) event
									.getSelection();

							options.setDiagramsToPrint(selection.toList());

						}

					}
				});

		diagramList.setContentProvider(new DiagramContentProvider());
		diagramList.setLabelProvider(new DiagramLabelProvider());

		diagramList.setInput(availableDiagrams);

		bindings.bindValue(
		        WidgetProperties.buttonSelection().observe(currentDiagramRadio),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_DIAGRAM_CURRENT).observe(realm, options), null, null);

		bindings.bindValue(
		        WidgetProperties.buttonSelection().observe(selectedDiagramsRadio),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_DIAGRAM_SELECTION).observe(realm, options), null, null);

		return result;
	}

	@Override
	public void dispose() {
		// nothing special to dispose currently
	}

	/**
	 * A helper class used to retrieve label text
	 * 
	 * @author James Bruck (jbruck)
	 */
	private class DiagramContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			return availableDiagrams.toArray();
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// input never changes
		}

		public void dispose() {
			// nothing to dispose
		}
	}

	private class DiagramLabelProvider extends LabelProvider {
		public String getText(Object element) {
			return (String) element;
		}
	}

}
