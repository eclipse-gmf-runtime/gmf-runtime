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

package org.eclipse.gmf.runtime.common.ui.services.action.global;

import org.eclipse.ui.IWorkbenchPart;

/**
 * The class used to create a <code>IGlobalActionHandlerContext</code> object.
 * This class implements <code>IGlobalActionHandlerContext</code> interface.
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalActionHandlerContext
    implements IGlobalActionHandlerContext {

    /**
     * Attribute for the <code>IWorkbenchPart</code>
     */
    final private IWorkbenchPart activePart;

    /**
     * Attribute for the <code>GlobalActionId</code>
     */
    final private String actionId;

    /**
     * Attribute for the element type (<code>Class</code>)
     */
    final private Class elementType;

    /**
     * Attribute for compatability flag
     */
    final private boolean isCompatible;

    /**
     * Constructor.
     * 
     * @param activePart attribute for active part
     * @param actionId attribute for action ID
     * @param elementType attribute for elementType
     * @param isCompatible attribute for isCompatible
     */
    public GlobalActionHandlerContext(
        IWorkbenchPart activePart,
        String actionId,
        Class elementType,
        boolean isCompatible) {
        super();

        assert null != activePart : "activePart cannot be null"; //$NON-NLS-1$
        assert null != elementType : "elementType cannot be null"; //$NON-NLS-1$

        this.activePart = activePart;
        this.actionId = actionId;
        this.elementType = elementType;
        this.isCompatible = isCompatible;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerContext#getActionId()
     */
    public String getActionId() {
        return actionId;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerContext#getElementType()
     */
    public Class getElementType() {
        return elementType;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerContext#getActivePart()
     */
    public IWorkbenchPart getActivePart() {
        return activePart;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerContext#isCompatible()
     */
    public boolean isCompatible() {
        return isCompatible;
    }
}
