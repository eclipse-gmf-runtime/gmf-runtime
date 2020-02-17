/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;


/**
 * This request encapsulates a list of
 * <code>CreateConnectionViewAndElementRequest</code> for each relationship
 * type that this tool supports. Each method in
 * <code>CreateConnectionRequest</code> that is called to configure the
 * request in <code>ConnectionCreationTool</code> is propagated to each
 * individual request.
 * 
 * @author cmahoney
 */
public class CreateUnspecifiedTypeConnectionRequest
	extends CreateConnectionRequest {

	/**
	 * List of relationship types of which one will be created (of type
	 * <code>IElementType</code>).
	 */
	private List relationshipTypes;

	/**
	 * A map containing the <code>CreateConnectionRequest</code> for each
	 * element type.
	 */
	private Map requests = new HashMap();

	/**
	 * A flag to indicate if the Modeling Assistant Service should be used to
	 * find the types when the other end of the connection is known.
	 */
	private boolean useModelingAssistantService;

	/**
	 * A flag to indicate if this request is to create a connection from target
	 * to source.
	 */
	private boolean directionReversed = false;
	
	/**
	 * The hint used to find the appropriate preference store from which general
	 * diagramming preference values for properties of shapes, connections, and
	 * diagrams can be retrieved. This hint is mapped to a preference store in
	 * the {@link DiagramPreferencesRegistry}.
	 */
	private PreferencesHint preferencesHint;

	/**
	 * Creates a new <code>CreateUnspecifiedTypeConnectionRequest</code>.
	 * 
	 * @param relationshipTypes
	 *            List of relationship types of which one will be created (of
	 *            type <code>IElementType</code>).
	 * @param useModelingAssistantService
	 *            True if the Modeling Assistant Service should be used to find
	 *            the types when the other end of the connection is known.
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateUnspecifiedTypeConnectionRequest(List relationshipTypes,
			boolean useModelingAssistantService, PreferencesHint preferencesHint) {
		super();
		this.useModelingAssistantService = useModelingAssistantService;
		this.relationshipTypes = relationshipTypes;
		this.preferencesHint = preferencesHint;
		createRequests();
	}

	/**
	 * Creates a <code>CreateConnectionRequest</code> for each relationship
	 * type and adds it to the map of requests.
	 */
	private void createRequests() {
		for (Iterator iter = relationshipTypes.iterator(); iter.hasNext();) {
			IElementType elementType = (IElementType) iter.next();

			Request request = CreateViewRequestFactory
				.getCreateConnectionRequest(elementType, getPreferencesHint());
			request.setType(getType());
			requests.put(elementType, request);
		}
	}

	/**
	 * Returns the <code>CreateRequest</code> for the relationship type passed in.
	 * 
	 * @param relationshipType
	 * @return the <code>CreateRequest</code>
	 */
	public CreateRequest getRequestForType(IElementType relationshipType) {
		if (requests != null) {
			return (CreateConnectionRequest) requests.get(relationshipType);
		}
		return null;
	}

    
    public void addRequest(IElementType relationshipType, Request request) {
        if (requests != null) {
            requests.put(relationshipType, request);
        }        
    }
    
	/**
	 * Returns a list of all the requests.
	 * 
	 * @return the requests
	 */
	public List getAllRequests() {
		if (requests != null) {
			return new ArrayList(requests.values());
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns the list of element types.
	 * 
	 * @return Returns the list of element types.
	 */
	public List getElementTypes() {
		return relationshipTypes;
	}

	/**
	 * @see org.eclipse.gef.requests.CreateConnectionRequest#setSourceEditPart(org.eclipse.gef.EditPart)
	 */
	public void setSourceEditPart(EditPart part) {
		if (requests != null) {
			for (Iterator iter = requests.values().iterator(); iter.hasNext();) {
				CreateConnectionRequest request = (CreateConnectionRequest) iter
					.next();
				request.setSourceEditPart(part);
			}
		}
		super.setSourceEditPart(part);
	}

	/**
	 * @see org.eclipse.gef.requests.TargetRequest#setTargetEditPart(org.eclipse.gef.EditPart)
	 */
	public void setTargetEditPart(EditPart part) {
		if (requests != null) {
			for (Iterator iter = requests.values().iterator(); iter.hasNext();) {
				CreateConnectionRequest request = (CreateConnectionRequest) iter
					.next();
				request.setTargetEditPart(part);
			}
		}
		super.setTargetEditPart(part);
	}

	/**
	 * @see org.eclipse.gef.requests.CreateRequest#setLocation(org.eclipse.draw2d.geometry.Point)
	 */
	public void setLocation(Point location) {
		if (requests != null) {
			for (Iterator iter = requests.values().iterator(); iter.hasNext();) {
				CreateConnectionRequest request = (CreateConnectionRequest) iter
					.next();
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
				CreateConnectionRequest request = (CreateConnectionRequest) iter
					.next();
				request.setType(type);
			}
		}
		super.setType(type);
	}

	/**
	 * Returns true if this request is to create a connection from target to
	 * source.
	 * 
	 * @return Returns the directionReversed.
	 */
	public boolean isDirectionReversed() {
		return directionReversed;
	}

	/**
	 * Sets the directionReversed flag.
	 * 
	 * @param directionReversed
	 *            The directionReversed to set.
	 */
	public void setDirectionReversed(boolean directionReversed) {
		this.directionReversed = directionReversed;
	}

	/**
	 * Should the Modeling Assistant Service be used?
	 * 
	 * @return Returns true if the Modeling Assistant Service should be used to
	 *         find the types when the other end of the connection is known.
	 */
	public boolean useModelingAssistantService() {
		return useModelingAssistantService;
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

}