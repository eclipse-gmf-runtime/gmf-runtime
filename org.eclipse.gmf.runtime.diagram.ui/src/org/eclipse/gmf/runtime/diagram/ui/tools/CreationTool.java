/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.figures.LayoutHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramUIPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

/**
 * Generic Creation Tool - creates a semantic model element and a view for it
 *
 * The usual usecase is to create "one" element and view. However, in case multiple
 * elements are needed, specialized Semantic and View requests are needed to create
 * new adapters that can adapt to multiple semantic elements and views in the same time
 * 
 * @author melaasar
 */
public class CreationTool extends org.eclipse.gef.tools.CreationTool {

	// temporarily disable the autoexpose helper since it interferes with menu selection.
	// see defect RATLC00525995	
	/**
	 * the anti scroll flag
	 */
	protected boolean antiScroll = false;
	
	/** Constant used to specify no point has been defined for the shape creation
	 * request.  It will be up to the layout edit policy to lay the shape in 
	 * the correct position*/
	private static final Point UNDEFINED_POINT = LayoutHelper.UNDEFINED.getLocation();
	/** the requested element kind */
	private IElementType elementType =null;

	static private Cursor CURSOR_SHAPE = new Cursor(Display.getDefault(),
		DiagramUIPluginImages.DESC_SHAPECURSOR_SOURCE.getImageData(),
		DiagramUIPluginImages.DESC_SHAPECURSOR_MASK.getImageData(), 7, 7);

	static private Cursor CURSOR_SHAPE_NOT = new Cursor(Display.getDefault(),
		DiagramUIPluginImages.DESC_NOSHAPECURSOR_SOURCE.getImageData(),
		DiagramUIPluginImages.DESC_NOSHAPECURSOR_MASK.getImageData(), 7, 7);		

	/**
	 * Method CreationTool.
	 * Creates a new CreationTool with the given elementTypeInfo, and a defailt viewKind equals IShapeView
	 * and a default empty semanticHint
	 * @param elementType
	 */
	public CreationTool(IElementType elementType) {
		setElementType(elementType);
		setDefaultCursor(CURSOR_SHAPE);
		setDisabledCursor(CURSOR_SHAPE_NOT);
	}
	
	/**
	 * Default constructor to allow subclasses to not require an element type.
	 */
	protected CreationTool() {
		// do nothing
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
		EditPartViewer viewer = getCurrentViewer();
		if (viewer != null) {
			RootEditPart rootEP = viewer.getRootEditPart();
			if (rootEP instanceof IDiagramPreferenceSupport) {
				return ((IDiagramPreferenceSupport) rootEP)
					.getPreferencesHint();
			}
		}
		return PreferencesHint.USE_DEFAULTS;
	}

	protected Request createTargetRequest() {
		return CreateViewRequestFactory.getCreateShapeRequest(getElementType(),
			getPreferencesHint());
	}

	/**
	 * Since both the view and semantic requests contain results we need to free
	 * them when the tool is deactivated
	 */
	public void deactivate() {
		super.deactivate();
		setTargetRequest(null);
	}

	protected void performCreation(int button) {
		
		antiScroll = true;
				
		EditPartViewer viewer = getCurrentViewer();
		Command c = getCurrentCommand();
		executeCurrentCommand();
		selectAddedObject(viewer, DiagramCommandStack.getReturnValues(c));
		
		antiScroll = false;
	}

	/**
	 * Select the newly added shape view by default
	 * @param viewer
	 * @param objects
	 */
	protected void selectAddedObject(EditPartViewer viewer, Collection objects) {
		final List editparts = new ArrayList();
		for (Iterator i = objects.iterator(); i.hasNext();) {
			Object object = i.next();
			if (object instanceof IAdaptable) {
				Object editPart =
					viewer.getEditPartRegistry().get(
						((IAdaptable)object).getAdapter(View.class));
				if (editPart != null)
					editparts.add(editPart);
			}
		}

		if (!editparts.isEmpty()) {
			viewer.setSelection(new StructuredSelection(editparts));
		
			// automatically put the first shape into edit-mode
			Display.getCurrent().asyncExec(new Runnable() {
				public void run(){
					EditPart editPart = (EditPart) editparts.get(0);
					//
					// add active test since test scripts are failing on this
					// basically, the editpart has been deleted when this 
					// code is being executed. (see RATLC00527114)
					if ( editPart.isActive() ) {
						editPart.performRequest(new Request(RequestConstants.REQ_DIRECT_EDIT));
						revealEditPart((EditPart)editparts.get(0));
					}
				}
			});
		}
	}

	/**
	*  Handles double click to create the shape in defualt position
	*/
	protected boolean handleDoubleClick(int button) {

		createShapeAt(UNDEFINED_POINT);
		setState(STATE_TERMINAL);
		handleFinished();
		
		return true;

	}
	
	/**
	 * Create the shape corresponding to the current selected tool
	 * on the current diagram at the <code>point</code> specified
	 * @param point to create shape at
	 */
	protected void createShapeAt(Point point) {
		setTargetEditPart(getCurrentViewer().getRootEditPart().getContents());
		getCreateRequest().setLocation(point);
		setCurrentCommand(getCommand());
		performCreation(0);
	}

	/*  Overide to handle use case when the user has selected an tool
	 * and then click on the enter key which translated to SWT.Selection
	 * it will result in the new shape being created
	 * @see org.eclipse.gef.tools.AbstractTool#handleKeyUp(org.eclipse.swt.events.KeyEvent)
	 */
	protected boolean handleKeyUp(KeyEvent e) {
		if (e.keyCode==SWT.Selection){
			setEditDomain(getCurrentViewer().getEditDomain());
			createShapeAt(UNDEFINED_POINT);
			setState(STATE_TERMINAL);
			handleFinished();
			return true;
		}
		return false;
	}

	protected void setTargetEditPart(EditPart editpart) {
		// Set the target request to null if the target editpart has changed.
		// This needs to be done so that the context element (if applicable) in
		// the request can be reset.
		if (editpart != getTargetEditPart()) {
			setTargetRequest(null);
		}
		super.setTargetEditPart(editpart);
	}
	/**
	 * @return Returns the elementType.
	 */
	public IElementType getElementType() {
		return elementType;
	}
	/**
	 * @param elementType The elementType to set.
	 */
	private void setElementType(IElementType elementType) {
		this.elementType = elementType;
	}

	/**
	 * Overridden so that the current tool will remain active (locked) if the
	 * user is pressing the ctrl key.
	 */
	protected void handleFinished() {
		if (!getCurrentInput().isModKeyDown(SWT.MOD1)) {
			super.handleFinished();
		} else {
			reactivate();
		}
	}
	
		
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.CreationTool#handleMove()
	 */
	protected boolean handleMove() {
		if (!antiScroll)
			return super.handleMove();
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.TargetingTool#doAutoexpose()
	 */
	protected void doAutoexpose() {
		if (!antiScroll)
			super.doAutoexpose();
		return;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.TargetingTool#getCommand()
	 */
	protected Command getCommand() {	
		if (!antiScroll)
			return super.getCommand();
		return null;
	}
	
	/**
	 * Reveals the newly created editpart
	 * @param editPart
	 */
	protected void revealEditPart(EditPart editPart){
		if ((editPart != null)&&
				(editPart.getViewer() != null))
				editPart.getViewer().reveal(editPart);
	}

	/**
	 * Creates the request.
	 * 
	 * @see #createTargetRequest()
	 */
	public final Request createCreateRequest() {
		return createTargetRequest();
}
	/**
	 * Selects the new shapes and puts them in direct edit mode if desired.
	 * 
	 * @param viewer
	 *            the editpart viewer
	 * @param objects
	 *            the collection of new shapes
	 * @see #selectAddedObject(EditPartViewer, Collection)
	 */
	public final void selectNewShapes(EditPartViewer viewer, Collection objects) {
		selectAddedObject(viewer, objects);
	}

}
