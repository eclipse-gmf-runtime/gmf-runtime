/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.services.actions;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionScope;
import org.eclipse.gmf.tests.runtime.common.ui.services.dialogs.TestElementSelectionProviderContext;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Action to test UI capabilities of the ElementSelectionService.
 * 
 * @author Anthony Hunter
 */
public abstract class AbstractTestElementSelectionServiceActionDelegate
    implements IWorkbenchWindowActionDelegate {

    protected IWorkbenchWindow window;

    /**
     * The string input filter for the input which matches everything.
     */
    protected String inputString = "*"; //$NON-NLS-1$

    /**
     * The filter for the input which matches everything.
     */
    protected IFilter filter = new IFilter() {

        public boolean select(Object toTest) {
            return true;
        }

    };

    /**
     * ElementSelectionScope.VISIBLE - provide all visible elements based on the
     * provided context.
     */
    protected ElementSelectionScope scope = ElementSelectionScope.VISIBLE;

    /**
     * The context for the input.
     */
    protected IAdaptable context = new TestElementSelectionProviderContext();

    /**
     * {@inheritDoc}
     */
    public abstract void run(IAction action);

    /**
     * {@inheritDoc}
     */
    public void selectionChanged(IAction action, ISelection selection) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    public void dispose() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    public void init(IWorkbenchWindow aWindow) {
        this.window = aWindow;
    }
}