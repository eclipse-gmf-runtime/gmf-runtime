/******************************************************************************
 * Copyright (c) 2006, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.services.dialogs;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractElementSelectionInput;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionComposite;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog to select an existing element.
 * 
 * @author Anthony Hunter
 */
public class TestSelectElementDialog
    extends Dialog {

    /**
     * The element selection composite with filter and list.
     */
    private ElementSelectionComposite selectElementComposite;

    /**
     * ElementSelectionScope.VISIBLE - provide all visible elements based on the
     * provided context.
     */
    private ElementSelectionScope scope = ElementSelectionScope.VISIBLE;

    /**
     * The context for the input.
     */
    private IAdaptable context = new TestElementSelectionProviderContext();

    /**
     * Constructor for TestSelectElementDialog
     * @param parentShell the parent shell.
     */
    public TestSelectElementDialog(Shell parentShell) {
        super(parentShell);
    }

    /**
     * {@inheritDoc}
     */
    protected Control createDialogArea(Composite parent) {
        getShell().setText("Select Element"); //$NON-NLS-1$

        IFilter filter = new IFilter() {

            public boolean select(Object toTest) {
                return true;
            }

        };

        /*
         * The input for the element selection service.
         */
        AbstractElementSelectionInput input = new AbstractElementSelectionInput(filter,
            context, scope, StringStatics.BLANK);

        selectElementComposite = new ElementSelectionComposite(
            "Select an element (? = any character, * = any string):", //$NON-NLS-1$
            input) {

            protected void handleWidgetDefaultSelected() {
                okPressed();
            }

            protected boolean isValidSelection(List currentSelectedElements) {
                return true;
            }

            public void handleSelection(boolean isValid) {
                if (getButton(IDialogConstants.OK_ID) != null) {
                    getButton(IDialogConstants.OK_ID).setEnabled(isValid);
                }
            }

        };
        Composite result = new Composite(parent, SWT.NONE);
        result.setLayout(new GridLayout());
        result.setLayoutData(new GridData(GridData.FILL_BOTH));

        selectElementComposite.createComposite(result);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        /**
         * Initially disable OK button.
         */
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }

    /**
     * Retrieve the user selected elements.
     * 
     * @return the user selected elements
     */
    public List getSelectedElements() {
        return selectElementComposite.getSelectedElements();
    }

    /**
     * {@inheritDoc}
     */
    protected void cancelPressed() {
        selectElementComposite.cancel();
        super.cancelPressed();
    }

}
