/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart;

import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.IEditPartOperation;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;

/**
 * Abstract operation that simply stores the associated view and provides an
 * accessor method to all subclasses.
 * 
 * @author gsturov
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public abstract class EditPartOperation
	implements IEditPartOperation {

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
	protected EditPartOperation(View view) {
		Assert.isNotNull(view);
		this.view = view;
	}
	
	/** cached caching key */
	private String cachingKey;

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.IEditPartOperation#getCachingKey()
	 */
	public String getCachingKey() {
		if (cachingKey == null)
			cachingKey = determineCachingKey();
		return cachingKey;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.IEditPartOperation#getView()
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

