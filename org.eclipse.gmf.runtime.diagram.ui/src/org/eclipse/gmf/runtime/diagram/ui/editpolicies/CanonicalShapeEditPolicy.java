/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This edit policy will register itself with the model server to receive
 * semantic events fired to its host editpart.  It will create, if necessary,
 * notation elements for all semantic elements inserted into the host
 * element or delete the notation element for the semantic element removed
 * from the host element.
 * <P>
 * @see #refreshSemanticChildren().
 * @see #shouldDeleteView(View)
 * @author mhanner
 */
public abstract class CanonicalShapeEditPolicy extends CanonicalEditPolicy {
	
	/**
	 * Forwards the supplied request to the editpart's <code>host</code>.
	 * @param request to create views
	 * @return Command to create the views in the request
	 */
	protected Command getCreateViewCommand(CreateRequest request) {
		return host().getCommand(request);
	}

	
	/**
	 * Return <tt>true</tt> if this editpolicy should try and delete the supplied view;
	 * otherwise <tt>false<tt>.  
	 * The default behaviour is to return <tt>true</tt> if the view's semantic element is <tt>null</tt>.
	 * <P>
	 * Subclasses should override this method to ensure the correct behaviour.
	 * @return  <code>view.resolveSemanticElement() == null</code>
	 */
	protected boolean shouldDeleteView(View view ) {
		return  ViewUtil.resolveSemanticElement(view) == null;
	}
}