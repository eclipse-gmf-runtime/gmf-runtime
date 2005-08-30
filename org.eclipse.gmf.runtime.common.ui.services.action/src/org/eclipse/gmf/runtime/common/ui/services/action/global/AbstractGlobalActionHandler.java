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



/**
 * The abstract parent of all concrete global action handlers. A concrete handler
 * needs to override the <code>getCommand()</code> and <code>canHandle()</code> methods.
 * This class implements the <code>IGlobalActionHandler</code> interface. It provides
 * access to the <code>IGlobalActionContext</code> to its decendents.
 * 
 * @author Vishy Ramaswamy
 */
public abstract class AbstractGlobalActionHandler
    implements IGlobalActionHandler {
    /**
     * Creates an AbstractGlobalActionHandler.
     */
    public AbstractGlobalActionHandler() {
        super();
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.global.IGlobalActionHandler#getLabel(org.eclipse.gmf.runtime.common.ui.action.global.IGlobalActionContext)
	 */
	public String getLabel(IGlobalActionContext cntxt) {
		return null;
	}
}
