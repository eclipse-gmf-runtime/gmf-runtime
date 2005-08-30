/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions;


/**
 * @author qili
 *
 * Ids for all action specific to logic diagram
 */
public interface LogicActionIds {
	
	/* Menu contribution ids */
	public final String MENU_INCREMENT = "incrementMenu"; //$NON-NLS-1$
	public final String MENU_DECREMENT = "decrementMenu"; //$NON-NLS-1$
	
	/* Action contribution ids */
    public final String ACTION_INCREMENT_VALUE = "incrementValueAction";//$NON-NLS-1$
	public final String ACTION_DECREMENT_VALUE = "decrementValueAction";//$NON-NLS-1$
	public final String DELETE_SEMANTIC_VALUE = "deleteSemanticAction"; //$NON-NLS-1$
}
