/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.ITerminalOwnerEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Element;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import com.ibm.xtools.notation.View;

/**
 * @author qili
 *
 * Sync notation views with semantic children
 */
public class TerminalCanonicalEditPolicy extends CanonicalEditPolicy {
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#getSemanticChildrenList()
	 */
	protected List getSemanticChildrenList() {
		Element logicElement = getLogicElement();
		
//		making sure that semantic element is not null before proceeding.
		if (logicElement == null)
			return Collections.EMPTY_LIST;
		
		List theElements = new ArrayList();
		
		if (logicElement instanceof Circuit) {
			theElements = logicElement.getInputTerminals();
		} else {
			List theInput = logicElement.getInputTerminals();
			List theOutput = logicElement.getOutputTerminals();
		
			int icnt = theInput.size();
			int ocnt = theOutput.size();
		
			if(icnt < 1 || ocnt < 1)
			{
				return Collections.EMPTY_LIST;
			}
		
			theElements.addAll(theInput);
			theElements.addAll(theOutput);
		}
		return theElements;
	}
	
	/** 
	 * Returns <tt>false</tt> not to delete LogicShapeCompartmentview.
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#shouldDeleteView(com.ibm.xtools.notation.View)
	 */
	protected boolean shouldDeleteView(View view) {
		return view.getType().equals(LogicConstants.LOGIC_SHAPE_COMPARTMENT) == false; //$NON-NLS-1$
	}
	
	
	/**
	 * @return semantic element for the logic shape
	 */
	private final Element getLogicElement() {
		return (Element)ViewUtil.resolveSemanticElement((View)(this.host().getModel()));
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#getCreateViewCommand(org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor)
	 */
	protected ICommand getCreateViewCommand(ViewDescriptor descriptor) {
		CompositeCommand cc = new CompositeCommand(PresentationResourceManager.getI18NString("AddCommand.Label")); //$NON-NLS-1$
		
		ICommand viewCommand = super.getCreateViewCommand(descriptor);
		
		IAdaptable adapter = descriptor.getElementAdapter();
		if( adapter == null ) {
			return null;
		}
		Terminal element = (Terminal)adapter.getAdapter(Terminal.class);
	
		ICommand boundsCommand = 
			new SetBoundsCommand(
				PresentationResourceManager.getInstance().getString("SetLocationCommand.Label.Resize"),//$NON-NLS-1$
				descriptor,
				(Point)((ITerminalOwnerEditPart)getHost()).createBoundsMap().get(element.getId()));
		
		cc.compose(viewCommand);
		cc.compose(boundsCommand);
		
		return cc;
	}
}
