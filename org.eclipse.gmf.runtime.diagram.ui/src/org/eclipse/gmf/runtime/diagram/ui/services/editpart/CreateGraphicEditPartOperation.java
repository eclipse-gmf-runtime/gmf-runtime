/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.editpart;

import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.EditPartOperation;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.IEditPartProvider;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.View;
 
/**
 * Concrete operation to create a <code>IGraphicalEditPart</code> (or subclass) element.
 * 
 * @author gsturov
 */
public class CreateGraphicEditPartOperation
	extends EditPartOperation {
	
	/** cached view. */
	private final View view;
	
	/** a dummy caching key */
	private static final String dummyHiny = "dummy";  //$NON-NLS-1$

	/**
	 * Constructor. Caches the supplied view.
	 * 
	 * @param view
	 *            the view element to be <i>controlled </i> by the created
	 *            editpart.
	 */
	protected CreateGraphicEditPartOperation(View view) {
		Assert.isNotNull(view);
		this.view = view;
	}

	/** 
	 * creates the editpart.
	 * @param provider the provider capable of honoring this operation.
	 * @return the created editpart instance.
	 */
	public Object execute(IProvider provider) {
		return ((IEditPartProvider) provider).createGraphicEditPart(getView());
	}

	/**
	 * gets the cached view.
	 * @return the notation View
	 */
	public final View getView() {
		return view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.EditPartOperation#determineCachingKey()
	 */
	protected String determineCachingKey() {
		String type = getView().getType();
		if (type != null && type.length() > 0)
			return type;
	
		String classId = ViewUtil.getSemanticElementClassId(getView());
		if (classId != null)
			return classId;
	
		return dummyHiny;
	}
}
