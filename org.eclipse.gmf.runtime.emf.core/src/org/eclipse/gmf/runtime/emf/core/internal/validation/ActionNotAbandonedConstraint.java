/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.core.internal.validation;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;

import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

/**
 * A constraint that ensures that a write action was not abandoned by some
 * listener adding an abandon-action command to the write action.
 *
 * @author Christian W. Damus (cdamus)
 */
public class ActionNotAbandonedConstraint
	extends AbstractModelConstraint {
	
	/**
	 * Initializes me.
	 */
	public ActionNotAbandonedConstraint() {
		super();
	}
	
	public IStatus validate(IValidationContext ctx) {
    	if (ctx.getCurrentConstraintData() == null) {
    		// put myself as a token to prevent calling me again
    		ctx.putCurrentConstraintData(this);
    		
    		List events = ctx.getAllEvents();
    		
    		for (Iterator iter = events.iterator(); iter.hasNext();) {
    			Object next = iter.next();
    			
    			if (next instanceof ActionAbandonedNotification) {
    				return ctx.createFailureStatus(new Object[] {
    					((ActionAbandonedNotification) next).getStatus().getMessage()
    				});
    			}
    		}
    	}
    	
    	return ctx.createSuccessStatus();
	}
}
