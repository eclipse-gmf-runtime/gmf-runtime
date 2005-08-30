/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.internal.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.TargetingTool;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Abstract class for action bar tools.
 * This is the tool used for the Add commands associated with the action bars.
 * The creation tools for action bars also need to impl DragTracker since the SelectionTool
 * calls Handle.getDragTracker during mouseDown.
 * 
 * @author affrantz
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 * 
 */

public abstract class AbstractAddActionBarTool extends TargetingTool implements DragTracker {

	protected EditPart myHostEditPart 			= null;

	/** the requested element kind */
	protected IElementType myElementType 			= null;
	
	/** the create request to be used (optional) */
	protected CreateRequest myRequest	= null;

	/**
	 * @param epHost the host editpart
	 * @param elementType the element type to be created
	 */
	public AbstractAddActionBarTool(EditPart epHost, IElementType elementType) {
		myElementType 	= elementType;
		myHostEditPart 	= epHost;
		lockTargetEditPart(epHost);
	}
	
	/**
	 * @param epHost the host editpart
	 * @param theRequest the create request to be used
	 */
	public AbstractAddActionBarTool(EditPart epHost, CreateRequest theRequest) {
		myHostEditPart 			= epHost;
		myRequest	= theRequest;
		lockTargetEditPart(epHost);
	}
		
	/**
	 * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
	 */
	abstract protected Request createTargetRequest();	
		
	
	/**
	 * @see org.eclipse.gef.tools.TargetingTool#getCommand()
	*/
	abstract protected Command getCommand();
	
	
	/**
	 * Test to see if the tool's associated cmd
	 * is available to be run 
	 */
	public boolean isCommandEnabled()
	{
		Command cmd = this.getCommand();
		if(cmd==null)
		{
			return false;
		}
		
		return cmd.canExecute();
	}
	/**
	* @see org.eclipse.gef.DragTracker#commitDrag()
	*/
	public void commitDrag() {
		// NULL implementation
	}

	protected boolean handleButtonDown(int button) {
		// push the cmd that will be used by performCreation
		setCurrentCommand(getCommand());
		return super.handleButtonDown(button);

	}
	/**
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
	 */
	protected boolean handleButtonUp(int button) {
		performCreation(button);

		return true;
	}
	/**
	 * @see org.eclipse.gef.tools.AbstractTool#performCreation(int)
	 * @param button
	 */
	protected void performCreation(int button) {
		EditPartViewer viewer = getCurrentViewer();
		Command c = getCurrentCommand();
		executeCurrentCommand();
		
		List editparts = new ArrayList(1);
		Collection newObjects = DiagramCommandStack.getReturnValues(c);
		for(Iterator i = newObjects.iterator(); i.hasNext();) {
			Object o = i.next();
			EditPart editpart = null;
			if (o instanceof IAdaptable ) {
				IAdaptable adapter = (IAdaptable) o;
				View view = (View) adapter.getAdapter(View.class);
				if (view != null)
					editpart = (EditPart) viewer.getEditPartRegistry().get(view);
			} else if (o instanceof EObject) {
				IGraphicalEditPart host = (IGraphicalEditPart) myHostEditPart;
				editpart = host.findEditPart(host, (EObject)o);
			}
			if (editpart != null)
				editparts.add(editpart);
		}
		selectAddedObject(viewer, editparts);
	}
	
	/**
	 * Finds the newly created editpart associated with the element that has
	 * been added and then selects the editpart and requests direct editing
	 * 
	*/
	protected void selectAddedObject(final EditPartViewer viewer, final List editparts) {
		if (editparts.isEmpty())
			return;
		// automatically put the first shape into edit-mode
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				viewer.setSelection(new StructuredSelection(editparts));
				EditPart editpart = (EditPart) editparts.get(0);
				editpart.performRequest(new Request(RequestConstants.REQ_DIRECT_EDIT));
			}
		});
	}

	/**
	 * HACK in order to lock the editpart for the duration that this tool is active
	 * and avoid updateTargetUnderMouse, getting called we need to 
	 * override this method to perform a NOP
	 * @see org.eclipse.gef.tools.TargetingTool#unlockTargetEditPart()
	 */
	protected void unlockTargetEditPart() {
		// NULL implementation
	}

	protected IElementType getElementType() {
		return myElementType;
	}
	
	protected void setElementKind( IElementType elementType ) {
		myElementType = elementType;
	}
	
	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
	 */
	protected String getCommandName() {
		return REQ_CREATE;

	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
	 */
	protected String getDebugName() {
		return "Action Bar Creation Tool"; //$NON-NLS-1$

	}
	
	protected EditPart getHost() {
		return myHostEditPart;
	}
	
	/**
	 * Gets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @return the preferences hint
	 */
	protected PreferencesHint getPreferencesHint() {
		PreferencesHint preferencesHint = null;
		if (myHostEditPart != null) {
			preferencesHint = ((IGraphicalEditPart) myHostEditPart)
				.getDiagramPreferencesHint();
		}
		return preferencesHint;
	}
}
