/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.layout.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.examples.runtime.diagram.layout.LayoutPlugin;
import org.eclipse.gmf.examples.runtime.diagram.layout.provider.SquareLayoutProvider;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUtil;
import org.eclipse.gmf.runtime.emf.core.ResourceSetModifyOperation;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author sshaw
 *
 * Sample action for demonstrating invokation of a custom layout provider.  In this
 * case the square layout provider is invoked through a separate menu action.
 */
public class SquareLayoutAction	implements IWorkbenchWindowActionDelegate {

	/**
	 * Title of diagram creation operation
	 */
	private static final String KEY_SQUARE_LAYOUT = "squareLayoutTitle"; //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public SquareLayoutAction() {
		//No-op
	}

	/**
	 * Walk the selected objects and creates a new diagram for each visited
	 * packages
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {

		/* Get selection */
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		// Get selection from the window
		final ISelection selection = window.getSelectionService().getSelection();
		
		/* Perform remaining work within a ResourceSetWriteOperation */
		try {
			MSLEditingDomain.INSTANCE.run(
				new ResourceSetModifyOperation(LayoutPlugin
					.getResourceString(KEY_SQUARE_LAYOUT)) {

					protected void execute(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {

						if (selection instanceof IStructuredSelection) {

							IStructuredSelection structuredSelection = (IStructuredSelection) selection;

							// Walk selection
							for (Iterator i = structuredSelection.iterator(); i.hasNext();) {

								// Try to adapt the selection to a modeling element or a view
								Object selectedObject = i.next();
								if (selectedObject instanceof IAdaptable) {

									// Try to get a View (new notation)
									Object object = ((IAdaptable) selectedObject)
										.getAdapter(View.class);
									
									Diagram diag = ((View)object).getDiagram();
									if (diag != null) {
										DiagramUtil.layout(diag, SquareLayoutProvider.SQUARE_LAYOUT);
			 						}
								}
							}
						}
					}
				}, new NullProgressMonitor());

		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
	}

	/**
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		//No-op
	}

	/**
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
		//No-op
	}

	/**
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow windowIn) {
		//No-op
	}
}
