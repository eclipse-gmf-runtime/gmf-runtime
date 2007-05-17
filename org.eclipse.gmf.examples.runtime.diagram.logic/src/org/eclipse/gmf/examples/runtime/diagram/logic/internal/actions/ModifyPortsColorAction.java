/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n.ExampleDiagramLogicMessages;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.StringConstants;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Implementation of an action that changes ports color on LED and/or Circuit
 * 
 * @author aboyko
 * 
 */
public class ModifyPortsColorAction extends DiagramAction {

	public ModifyPortsColorAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	public void init() {
		super.init();
		setId("PortsColor"); //$NON-NLS-1$
		setText(ExampleDiagramLogicMessages.LogicPortsColor_Action_Label);
		setToolTipText(ExampleDiagramLogicMessages.LogicPortsColor_Action_Tooltip);
	}

	protected Request createTargetRequest() {
		return new Request(StringConstants.PORTSCOLOR_REQUEST);
	}

	protected boolean isSelectionListener() {
		return true;
	}

	protected void doRun(IProgressMonitor progressMonitor) {
		Shell shell = getDiagramGraphicalViewer().getControl().getShell();
		ColorDialog dialog = new ColorDialog(shell);
		RGB newRGB = dialog.open();
		if (newRGB != null) {
			getTargetRequest().getExtendedData().put(
					StringConstants.PORTS_COLOR_PROPERTY_NAME,
					FigureUtilities.RGBToInteger(newRGB));
			ModifyPortsColorAction.super.doRun(progressMonitor);
		}
	}

	protected Command getCommand() {
		Command cmd = super.getCommand();		
		// In the case of composite command the label is blank, hence lets just
		// force the label to be there regardless of the command
		cmd.setLabel(ExampleDiagramLogicMessages.LogicPortsColor_Action_Label);
		return cmd;
	}
	
}
