/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.TerminalEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.StringConstants;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.PropertiesSetStyle;

/**
 * Implementation of the command for changing ports color
 * 
 * @author aboyko
 * 
 */
public class ModifyPortsColorCommand extends AbstractTransactionalCommand {

	private IGraphicalEditPart ep;
	private Integer color;

	public ModifyPortsColorCommand(TransactionalEditingDomain editingDomain,
			IGraphicalEditPart ep, Integer color) {
		super(editingDomain, "Modify Ports Color", null); //$NON-NLS-1$
		this.ep = ep;
		this.color = color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand#doExecuteWithResult(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.runtime.IAdaptable)
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		List ports = getPorts();
		if (ports != null && color != null && !ports.isEmpty()) {
			PropertiesSetStyle style = (PropertiesSetStyle) ep
					.getNotationView().getNamedStyle(
							NotationPackage.eINSTANCE.getPropertiesSetStyle(),
							StringConstants.PORTS_PROPERTIES_STYLE_NAME);
			if (style != null) {
				if (style
						.hasProperty(StringConstants.PORTS_COLOR_PROPERTY_NAME)) {
					style.setProperty(
							StringConstants.PORTS_COLOR_PROPERTY_NAME, color);
					return CommandResult.newOKCommandResult();
				}
			}
		}
		return CommandResult.newCancelledCommandResult();
	}

	private List getPorts() {
		List ports = new LinkedList();
		for (Iterator itr = ep.getChildren().iterator(); itr.hasNext();) {
			Object obj = itr.next();
			if (obj instanceof TerminalEditPart) {
				ports.add(obj);
			}
		}
		return ports;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.operations.AbstractOperation#canExecute()
	 */
	public boolean canExecute() {
		if (ep.getNotationView() != null) {
			if (ep.getNotationView().getNamedStyle(
					NotationPackage.eINSTANCE.getPropertiesSetStyle(),
					StringConstants.PORTS_PROPERTIES_STYLE_NAME) != null) {
				List ports = getPorts();
				return ports != null && !ports.isEmpty() && super.canExecute();
			}
		}
		return false;
	}

}
