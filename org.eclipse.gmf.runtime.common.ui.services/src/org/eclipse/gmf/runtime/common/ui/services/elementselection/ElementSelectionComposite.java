/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.elementselection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gmf.runtime.common.ui.services.internal.elementselection.ElementSelectionCompositeContentProvider;
import org.eclipse.gmf.runtime.common.ui.services.internal.elementselection.ElementSelectionCompositeLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * The element selection composite. The composite functional similar to the JDT
 * select type dialog. There is a filter field and a table containing a list of
 * elements to select from.
 * <p>
 * The element selection composite requires an IElementSelectionInput as input
 * for the element selection service.
 * <p>
 * Subclasses must override the {@link #isValidSelection}and
 * {@link #handleSelection(boolean)} to provide custom validation.
 * 
 * @author Anthony Hunter
 */
public abstract class ElementSelectionComposite {

    /**
     * The title to display at the top of the element selection composite.
     */
    private final String title;

    /**
     * The elements that have been selected by the user.
     */
    private final List selectedElements = new ArrayList();

    /**
     * Text control to display the filter text.
     */
    private Text filterText = null;

    /**
     * The table viewer to display list of matching objects.
     */
    private TableViewer tableViewer = null;

    /**
     * The input for the element selection service.
     */
    private AbstractElementSelectionInput input;

    /**
     * Constructs a new instance that will create the new composite.
     * 
     * @param title
     *            the dialog title
     * @param input
     *            the element selection input.
     */
    public ElementSelectionComposite(String title,
            AbstractElementSelectionInput input) {
        super();
        this.title = title;
        this.input = input;
    }

    /**
     * Determines if the selected elements are a valid selection.
     * 
     * @param currentSelectedElements
     *            the selected list of Elements
     * @return <code>true</code> if the selected elements are a valid
     *         selection
     */
    abstract protected boolean isValidSelection(List currentSelectedElements);

    /**
     * Handle a selection change, where the validity of the new selection is
     * encoded in <code>isValid</code>.
     * 
     * @param isValid
     *            <code>true</code> if the new selection is valid,
     *            <code>false</code> otherwise.
     */
    protected abstract void handleSelection(boolean isValid);

    /**
     * Creates the composite.
     * 
     * @param parent
     *            the parent composite
     * @return the new composite
     */
    public Composite createComposite(Composite parent) {

        Composite result = new Composite(parent, SWT.NONE);
        result.setLayout(new GridLayout());
        result.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Add the selection title label
        Label label = new Label(result, SWT.NONE);
        label.setText(title);

        // Add the element selection text widget
        filterText = new Text(result, SWT.SINGLE | SWT.BORDER);
        filterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        filterText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                handleFilterChange();
            }

        });

        // Add the table viewer
        int selectStyle = SWT.SINGLE;
        tableViewer = new TableViewer(result, selectStyle | SWT.H_SCROLL
            | SWT.V_SCROLL | SWT.BORDER);
        tableViewer.setUseHashlookup(true);

        Table table = tableViewer.getTable();
        GridData gridData = new GridData(GridData.FILL_BOTH);
        GC gc = new GC(result);
        gc.setFont(JFaceResources.getDefaultFont());
        FontMetrics fontMetrics = gc.getFontMetrics();
        gc.dispose();
        gridData.widthHint = Dialog
            .convertWidthInCharsToPixels(fontMetrics, 80);
        gridData.heightHint = table.getItemHeight() * 15;
        table.setLayoutData(gridData);

        table.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                handleSelectionChange();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // method not implemented
            }
        });

        tableViewer
            .setLabelProvider(new ElementSelectionCompositeLabelProvider());
        tableViewer
            .setContentProvider(new ElementSelectionCompositeContentProvider());
        tableViewer.setSorter(new ViewerSorter());

        createCompositeAdditions(result);

        return result;
    }

    /**
     * The method is provided so that clients can add additional fields to the
     * bottom of the selection composite. For example, clients may want to a
     * checkbox button to the bottom of the composite.
     * 
     * @param parent
     *            the parent composite
     */
    protected void createCompositeAdditions(Composite parent) {
        /* clients are expected to override this method */
    }

    /**
     * Handles a filter change.
     */
    private void handleFilterChange() {
        input.setInput(filterText.getText());
        tableViewer.setInput(input);
        Object element = tableViewer.getElementAt(0);
        if (element != null) {
            tableViewer.setSelection(new StructuredSelection(element), true);
        }
        handleSelectionChange();
    }

    /**
     * Handles a selection change and validates the new selection.
     */
    private void handleSelectionChange() {
        StructuredSelection selection = (StructuredSelection) tableViewer
            .getSelection();
        if (selection.size() == 0) {
            // nothing selected
            selectedElements.clear();
            handleSelection(false);
            return;
        }

        List selectionList = selection.toList();

        // get the current selected elements
        List currentSelectedElements = new ArrayList();
        for (Iterator iter = selectionList.iterator(); iter.hasNext();) {
            AbstractMatchingObject matchingObject = (AbstractMatchingObject) iter
                .next();
            currentSelectedElements.add(matchingObject);
        }

        // validate selection
        boolean isValidSelection = isValidSelection(currentSelectedElements);

        // store the selection
        selectedElements.clear();
        if (isValidSelection) {
            selectedElements.addAll(currentSelectedElements);
        }

        // update UI based on selection
        handleSelection(isValidSelection);

    }

    /**
     * Gets the user selected elements.
     * 
     * @return the user selected elements
     */
    public List getSelectedElements() {
        List result = new ArrayList();
        for (Iterator iter = selectedElements.iterator(); iter.hasNext();) {
            IMatchingObject matchingObject = (IMatchingObject) iter.next();
            IElementSelectionProvider provider = matchingObject.getProvider();
            Object object = provider.resolve(matchingObject);
            result.add(object);
        }
        return result;
    }
}
