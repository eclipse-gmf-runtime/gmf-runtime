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

package org.eclipse.gmf.runtime.diagram.ui.render.internal.providers;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.actions.IPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.actions.EnhancedPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.internal.DiagramUIPrintingRenderDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.internal.DiagramUIPrintingRenderPlugin;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.internal.JPSDiagramPrinter;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.internal.JPSDiagramPrinterHelper;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * This class is strictly temporary while we introduce the printing via the
 * Print Service API.
 * 
 * @author James Bruck (jbruck)
 */
public class PrintExperimentalAction extends Action implements
		IWorkbenchWindowActionDelegate {

	/**
	 * ID for this page setup action
	 */
	public static final String ID = "printExperimentalAction";//$NON-NLS-1$

	/**
	 * Constructor sets the id and label that is displayed in the menu bar.
	 */
	public PrintExperimentalAction() {
		setId(ID);
		setText("Print (Experimental)..."); //$NON-NLS-1$
	}

	/**
	 * We override the print to always use the newer JPS printing options.
	 */
	public void run() {

		IPrintActionHelper helper = new EnhancedPrintActionHelper() {
			@Override
			public void doPrint(IWorkbenchPart workbenchPart) {

				DiagramEditor diagramEditor = null;

				if (workbenchPart instanceof DiagramEditor) {
					diagramEditor = (DiagramEditor) workbenchPart;
				} else {
					Log.error(DiagramUIPrintingRenderPlugin.getInstance(),
							IStatus.ERROR, "Invalid IWorkbenchPart"); //$NON-NLS-1$
					IllegalArgumentException e = new IllegalArgumentException(
							"Invalid IWorkbenchPart."); //$NON-NLS-1$
					Trace
							.throwing(
									DiagramUIPrintingRenderPlugin.getInstance(),
									DiagramUIPrintingRenderDebugOptions.EXCEPTIONS_THROWING,
									EnhancedPrintActionHelper.class,
									"doPrint()", e); //$NON-NLS-1$
					throw e;
				}

				IDiagramGraphicalViewer viewer = diagramEditor
						.getDiagramGraphicalViewer();
				RootEditPart rootEP = (viewer == null) ? null : viewer
						.getRootEditPart();
				PreferencesHint preferencesHint = (rootEP instanceof IDiagramPreferenceSupport) ? ((IDiagramPreferenceSupport) rootEP)
						.getPreferencesHint()
						: PreferencesHint.USE_DEFAULTS;

				IMapMode mapMode = (rootEP instanceof DiagramRootEditPart) ? ((DiagramRootEditPart) rootEP)
						.getMapMode()
						: MapModeUtil.getMapMode();

				JPSDiagramPrinterHelper
						.getDiagramPrinterHelper()
						.printWithSettings(diagramEditor, createDiagramMap(),
								new JPSDiagramPrinter(preferencesHint, mapMode));
			}
		};

		helper.doPrint(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActivePart());
	}

	/**
	 * The run method does the real run action. From IActionDelegate
	 */
	public void run(IAction action) {
		run();
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
		// do nothing
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		// do nothing
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		//do nothing
	}
}
