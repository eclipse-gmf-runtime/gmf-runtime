/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions.IncrementDecrementAction;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions.LogicActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;

/**
 * An example status bar contribution for the status bar. This added the
 * increment / decrement actions to the status bar. These actions are in the
 * toolbar for GEF, but we are demonstrating them in the status bar for GMF.
 * 
 * @author Anthony Hunter
 */
public class IncrementDecrementContributionItem extends ActionContributionItem
		implements LogicActionIds {

	/**
	 * Constructor for a IncrementDecrementContributionItem
	 * 
	 * @param workbenchPage
	 *            The workbench page
	 * @param id
	 *            the id for the action.
	 */
	public IncrementDecrementContributionItem(IWorkbenchPage workbenchPage,
			String id) {
		super(new IncrementDecrementAction(workbenchPage, id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.ActionContributionItem#isDynamic()
	 */
	public boolean isDynamic() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.action.ActionContributionItem#fill(org.eclipse.swt.
	 * widgets.Composite)
	 */
	public void fill(Composite parent) {
		((DiagramAction) getAction()).init();
		Button button = new Button(parent, SWT.PUSH);
		button.setImage(getAction().getImageDescriptor().createImage());
		button.setToolTipText(getAction().getToolTipText());
		button.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				getAction().run();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// Not implemented

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.ActionContributionItem#isEnabled()
	 */
	public boolean isEnabled() {
		return getAction().isEnabled();
	}

}
