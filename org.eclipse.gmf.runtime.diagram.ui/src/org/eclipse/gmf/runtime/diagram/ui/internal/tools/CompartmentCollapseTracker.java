/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IResizableCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

public class CompartmentCollapseTracker
	extends AbstractTool
	implements DragTracker {

	private IResizableCompartmentEditPart compartmentEditPart;

	public CompartmentCollapseTracker(IResizableCompartmentEditPart compartmentEditPart) {
		this.compartmentEditPart = compartmentEditPart;
	}

	protected List createOperationSet() {
		List list = new ArrayList(1);
		list.add(compartmentEditPart);
		return list;
	}

	protected Command getCommand(Boolean expand) {
		ChangePropertyValueRequest request = new ChangePropertyValueRequest(
			DiagramUIMessages.PropertyDescriptorFactory_CollapseCompartment,
			Properties.ID_COLLAPSED, expand);
		return compartmentEditPart.getCommand(request);
	}

	protected String getCommandName() {
		return "Collapse Compartment"; //$NON-NLS-1$
	}

	protected String getDebugName() {
		return "Collapse Compartment Tool"; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
	 */
	protected boolean handleButtonDown(int button) {
		View view  = compartmentEditPart.getNotationView();
		if (view!=null){
			DrawerStyle style = (DrawerStyle)view.getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
			if (style != null) {
				Boolean newValue = style.isCollapsed() ? Boolean.FALSE : Boolean.TRUE;
				setCurrentCommand(getCommand(newValue));
				executeCurrentCommand();
				return true;
			} 
		}
		return false;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleKeyDown(org.eclipse.swt.events.KeyEvent)
	 */
	protected boolean handleKeyDown(KeyEvent e) {
		if (e.keyCode == SWT.ARROW_RIGHT || e.keyCode == SWT.ARROW_LEFT) {
			Boolean b =
				e.keyCode == SWT.ARROW_RIGHT ? Boolean.FALSE : Boolean.TRUE;
			setCurrentCommand(getCommand(b));
			executeCurrentCommand();
			return true;
		}
		return false;
	}

}
