/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IResizableCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import com.ibm.xtools.notation.DrawerStyle;
import com.ibm.xtools.notation.NotationPackage;
import com.ibm.xtools.notation.View;

/*
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
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
		ChangePropertyValueRequest request = new ChangePropertyValueRequest(PresentationResourceManager.getI18NString("PropertyDescriptorFactory.CollapseCompartment"), Properties.ID_COLLAPSED, expand); //$NON-NLS-1$
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
