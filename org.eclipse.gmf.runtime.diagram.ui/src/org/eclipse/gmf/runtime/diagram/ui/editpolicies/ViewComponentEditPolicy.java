/***************************************************************************
 Licensed Materials - Property of IBM                                  
 (C) Copyright IBM Corp. 2004.  All Rights Reserved.                   
 
 US Government Users Restricted Rights - Use, duplication or disclosure
 restricted by GSA ADP Schedule Contract with IBM Corp.                
***************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;


/**
 * Concrete class that extends the <code>ComponentEditPolicy</code>.
 * This edit policy will return a command in response to delete requests.
 * 
 * @author Jody Schofield
 */
public class ViewComponentEditPolicy extends ComponentEditPolicy {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy#shouldDeleteSemantic()
	 */
	protected boolean shouldDeleteSemantic() {

		return false;
	}
	
}
