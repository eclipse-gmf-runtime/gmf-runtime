/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.providers;

import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.printing.actions.DefaultPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.SWTDiagramPrinter;
import org.eclipse.gmf.runtime.diagram.ui.providers.DiagramGlobalActionHandler;
import org.eclipse.ui.IEditorPart;

/**
 * Concrete class that implements the <code>IGlobalActionHandler</code>
 * interface and provides a command for <code>GlobalActionId.PRINT</code>.
 * 
 * @author tmacdoug
 */
public class DiagramWithPrintGlobalActionHandler
	extends DiagramGlobalActionHandler {

	/**
	 * Constructor for DiagramWithPrintGlobalActionHandler.
	 */
	public DiagramWithPrintGlobalActionHandler() {
		super();
	}

	public ICommand getCommand(IGlobalActionContext cntxt) {
		ICommand command = null;

		/* Check the action id and create the command */
		String actionId = cntxt.getActionId();
		if (actionId.equals(GlobalActionId.PRINT)) {
			doPrint(cntxt);
		} else {
			command = super.getCommand(cntxt);
		}

		return command;
	}

	/**
	 * @param cntxt
	 */
	protected void doPrint(IGlobalActionContext cntxt) {
	
		DefaultPrintActionHelper.doRun((IEditorPart) cntxt.getActivePart(),
			new SWTDiagramPrinter(getPreferencesHint((IEditorPart) cntxt
				.getActivePart()), getMapMode(cntxt)));
		}

	/**
	 * Checks if we will allow a print
	 * 
	 * @return boolean true
	 */
	private boolean canPrint() {
		return true;
	}

	public boolean canHandle(IGlobalActionContext cntxt) {
		boolean result = false;

		/* Check the action id */
		String actionId = cntxt.getActionId();
		if (actionId.equals(GlobalActionId.PRINT)) {
			result = canPrint();
		} else {
			result = super.canHandle(cntxt);
		}
		return result;
	}

	/**
	 * Gets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @return the preferences hint
	 */
	protected PreferencesHint getPreferencesHint(IEditorPart editorPart) {
		if (editorPart instanceof IDiagramWorkbenchPart) {
			RootEditPart rootEP = ((IDiagramWorkbenchPart) editorPart)
				.getDiagramGraphicalViewer().getRootEditPart();
			if (rootEP instanceof IDiagramPreferenceSupport) {
				return ((IDiagramPreferenceSupport) rootEP)
					.getPreferencesHint();
			}
		}
		return PreferencesHint.USE_DEFAULTS;
	}
}
