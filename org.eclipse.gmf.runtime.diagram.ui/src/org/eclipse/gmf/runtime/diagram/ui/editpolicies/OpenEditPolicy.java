/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/** Open Shape Edit Policy
 * <p>
 * This edit policy handles double click (open) gestures on shapes. When the user
 * double clicks on an edit part, GEF creates a REQ_OPEN request and subclasses of this
 * edit policy should handle that request. This policy should be installed with the
 * policy role OPEN_ROLE. Normally, the corresponding edit policy role
 * would be defined in {@link org.eclipse.gef.EditPolicy}, but no such role currently exists.
 * <p>
 * This edit policy is different from "DirectEditPolicy" even though both are a result of
 * a double click on a representation in a diagram. With the DIRECT_EDIT_ROLE, some type 
 * of in-diagram editing is performed (e.g., modify a text label, move line segment nodes, 
 * etc.). The OPEN_ROLE policy is typically installed on edit part whose underlying data
 * can only be examined/modified by opening another editor window. For example, if an edit
 * part represented another diagram, then an "open" request on that edit part should result 
 * in the corresponding diagram being opened in another editor window.
 * <p>
 * Although edit policies for both DIRECT_EDIT_ROLE and OPEN_ROLE could be installed on 
 * an edit part, typically only one of these roles is appropriate.
 * <p>
 * @author Kevin Cornell
 */
public abstract class OpenEditPolicy extends AbstractEditPolicy {

    /**
     * Returns the <code>Command</code> to perform the open request.
     * @param request the Request
     * @return the command to perform the open
     */
    protected abstract Command getOpenCommand(Request request);

    /** 
     * Intercept a command request for REQ_OPEN.
     * <p>
     * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
     */
    public Command getCommand(Request request) {
        if (RequestConstants.REQ_OPEN.equals(request.getType()))
            return getOpenCommand(request);
        return null;
    }

    /** 
     * Determine if the request type is supported by this edit policy.
     * <p>
     * @see org.eclipse.gef.EditPolicy#understandsRequest(org.eclipse.gef.Request)
     */
    public boolean understandsRequest(Request request) {
        if (RequestConstants.REQ_OPEN.equals(request.getType()))
            return true;
        return false;
    }

    /**
     * By default, the target edit part is the host edit part.
     * <p>
     * @see org.eclipse.gef.EditPolicy#getTargetEditPart(Request)
     */
    public EditPart getTargetEditPart(Request request) {

        if (understandsRequest(request))
            return getHost();

        return null;
    }
}
