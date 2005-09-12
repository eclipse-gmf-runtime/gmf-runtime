/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.util.INotationType;
import org.eclipse.gmf.runtime.emf.core.internal.util.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.Node;

/**
 * This request encapsulates a list of <code>CreateViewRequest</code> or
 * <code>CreateViewAndElementRequest</code> for each type that this tool
 * supports. Each method in <code>CreateRequest</code> that is called to
 * configure the request in <code>CreationTool</code> is propagated to each
 * individual request.
 * 
 * @author cmahoney
 */
public class CreateUnspecifiedTypeRequest
	extends CreateRequest {

	/**
	 * List of element types of which one will be created (of type
	 * <code>IElementType</code>).
	 */
	protected List elementTypes;

	/**
	 * A map containing the <code>CreateRequest</code> for each element type.
	 */
	protected Map requests = new HashMap();

	/** The result to be returned from which the new views can be retrieved. */
	private List newObjectList = new ArrayList();

	/**
	 * The hint used to find the appropriate preference store from which general
	 * diagramming preference values for properties of shapes, connectors, and
	 * diagrams can be retrieved. This hint is mapped to a preference store in
	 * the {@link DiagramPreferencesRegistry}.
	 */
	private PreferencesHint preferencesHint;
	
	/**
	 * Creates a new <code>CreateUnspecifiedTypeRequest</code>.
	 * 
	 * @param elementTypes
	 *            List of element types of which one will be created (of type
	 *            <code>IElementType</code>).
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateUnspecifiedTypeRequest(List elementTypes, PreferencesHint preferencesHint) {
		super();
		this.elementTypes = elementTypes;
		this.preferencesHint = preferencesHint;
		createRequests();
	}

	/**
	 * Creates a <code>CreateViewRequest</code> or
	 * <code>CreateViewAndElementRequest</code> for each creation hint and
	 * adds it to the map of requests.
	 */
	protected void createRequests() {
		for (Iterator iter = elementTypes.iterator(); iter.hasNext();) {
			IElementType elementType = (IElementType) iter.next();

			Request request = null;
			if (elementType instanceof INotationType) {
				ViewDescriptor viewDescriptor = new ViewDescriptor(null,
					Node.class, ((INotationType) elementType)
						.getSemanticHint(), getPreferencesHint());
				request = new CreateViewRequest(viewDescriptor);
			} else if (elementType instanceof IHintedType) {
				ViewAndElementDescriptor viewDescriptor = new ViewAndElementDescriptor(
					new CreateElementRequestAdapter(new CreateElementRequest(
						elementType)), Node.class,
					((IHintedType) elementType).getSemanticHint(), getPreferencesHint());
				request = new CreateViewAndElementRequest(viewDescriptor);
			} else {
				ViewAndElementDescriptor viewDescriptor = new ViewAndElementDescriptor(
					new CreateElementRequestAdapter(new CreateElementRequest(
						elementType)), Node.class, getPreferencesHint());
				request = new CreateViewAndElementRequest(viewDescriptor);
			}

			request.setType(getType());
			requests.put(elementType, request);
		}
	}

	/**
	 * Returns the <code>CreateRequest</code> for the element type passed in.
	 * 
	 * @param creationHint
	 * @return the <code>CreateRequest</code>
	 */
	public CreateRequest getRequestForType(IElementType creationHint) {
		if (requests != null) {
			return (CreateRequest) requests.get(creationHint);
		}
		return null;
	}

	/**
	 * Returns the list of element types.
	 * 
	 * @return Returns the list of element types.
	 */
	public List getElementTypes() {
		return elementTypes;
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
		return preferencesHint;
	}

	/**
	 * @see org.eclipse.gef.requests.CreateRequest#setLocation(org.eclipse.draw2d.geometry.Point)
	 */
	public void setLocation(Point location) {
		if (requests != null) {
			for (Iterator iter = requests.values().iterator(); iter.hasNext();) {
				CreateRequest request = (CreateRequest) iter.next();
				request.setLocation(location);
			}
		}
		super.setLocation(location);
	}

	/**
	 * @see org.eclipse.gef.Request#setType(java.lang.Object)
	 */
	public void setType(Object type) {
		if (requests != null) {
			for (Iterator iter = requests.values().iterator(); iter.hasNext();) {
				CreateRequest request = (CreateRequest) iter.next();
				request.setType(type);
			}
		}
		super.setType(type);
	}

	/**
	 * Sets the new object to be returned. Must be of the type expected in
	 * getNewObjectType().
	 * @param theNewObjects
	 */
	public void setNewObject(Collection theNewObjects) {
		newObjectList.addAll(theNewObjects);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest#getNewObject()
	 */
	public Object getNewObject() {
		return newObjectList;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest#getNewObjectType()
	 */
	public Object getNewObjectType() {
		return List.class;
	}
}