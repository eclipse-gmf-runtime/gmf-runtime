/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.action.global;

import org.eclipse.ui.IWorkbenchPart;

/**
 * Interface for accessing the attributes used to determine the 
 * <code>IGlobalActionHandler</code>.
 * 
 * @author Vishy Ramaswamy
 */
public interface IGlobalActionHandlerContext {
    /**
     * Returns the <code>GlobalActionId</code>
     * 
     * @return Returns an <code>String</code>
     */
    public String getActionId();

    /**
     * Returns an element type within the selection that
     * occured in the active <code>IWorkbenchPart</code>
     * 
     * @return Returns a <code>Class</code>
     */
    public Class getElementType();

    /**
     * Returns the active <code>IWorkbenchPart</code>
     * 
     * @return Returns the active <code>IWorkbenchPart</code>
     */
    public IWorkbenchPart getActivePart();

    /**
     * Returns a boolean to indicate whether a direct match is
     * required for the element type or not. For a compatible type
     * the element type supported by the provider should be assignable from
     * this context's element type.
     * 
     * @return true if compatible is requested, false otherwise (for a direct match) 
     */
    public boolean isCompatible();
}
