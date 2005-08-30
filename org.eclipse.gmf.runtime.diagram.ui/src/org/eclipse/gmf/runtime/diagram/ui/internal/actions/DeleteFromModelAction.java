/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.diagram.ui.actions.AbstractDeleteFromAction;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.XtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.View;

/**
 * This Action is used to send a request that will destroy a semantic element
 * from the model.
 * 
 * @author melaasar
 * @canBeSeenBy %level1
 * @author choang 
 */
public class DeleteFromModelAction
	extends AbstractDeleteFromAction {
	
	
	/**
	 * Creates a <code>DeleteFromModelAction</code> with a default label.
	 *
	 * @param part The part this action will be associated with.
	 */
	public DeleteFromModelAction(IWorkbenchPart part) {
		super(part);
		
		
	}
 
	/**
	 * Constructor
	 * @param workbenchPage
	 */
	public DeleteFromModelAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
		
	}

	/**
	 * Initializes this action's text and images.
	 */
	public void init() {
		super.init();
		setId(ActionIds.ACTION_DELETE_FROM_MODEL);
		setText(PresentationResourceManager.getI18NString("DiagramEditor.Delete_from_Model"));//$NON-NLS-1$
		setToolTipText(PresentationResourceManager.getI18NString("DiagramEditor.Delete_from_ModelToolTip"));//$NON-NLS-1$
		ISharedImages workbenchImages = PlatformUI.getWorkbench().getSharedImages();
		setHoverImageDescriptor(
			workbenchImages.getImageDescriptor(
				ISharedImages.IMG_TOOL_DELETE));
		setImageDescriptor(
			workbenchImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(
			workbenchImages.getImageDescriptor(
				ISharedImages.IMG_TOOL_DELETE_DISABLED));
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#getCommand(org.eclipse.gef.Request)
	 */
	protected Command getCommand(Request request) {	
		List operationSet = getOperationSet();
		Iterator editParts = operationSet.iterator();
		CompositeModelCommand command =
			new CompositeModelCommand(getCommandLabel());
		while (editParts.hasNext()) {
			EditPart editPart = (EditPart) editParts.next();
			// disable on diagram links 
			if (editPart instanceof IGraphicalEditPart){
				IGraphicalEditPart gEditPart = 
					(IGraphicalEditPart) editPart;
				View view = (View)gEditPart.getModel();
				EObject element = ViewUtil.resolveSemanticElement(view);
				if(element instanceof Diagram)
					return null;
			}
			Command curCommand = editPart.getCommand(request);
			if (curCommand != null) {
				command.compose(new XtoolsProxyCommand(curCommand));				
			}
		}
		
		if ((command.isEmpty())
			|| (command.getCommands().size() != operationSet.size())){
			return UnexecutableCommand.INSTANCE;
		}
		return new EtoolsProxyCommand(command);
	}
}
