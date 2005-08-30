/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.util;

import org.eclipse.gmf.runtime.diagram.ui.util.INotationType;
import org.eclipse.gmf.runtime.emf.core.internal.util.ElementType;

/**
 * Abstract class for notation types. The name is used as the semantic hint.
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public abstract class AbstractNotationType
	extends ElementType
	implements INotationType {

	/**
	 * Constructor for PresentationNotationType.
	 * 
	 * @param displayName
	 *            the localized name of the type
	 * @param semanticHint
	 *            the semantic hint used when creating a view for this type
	 */
	protected AbstractNotationType(String displayName, String semanticHint) {
		super(displayName, semanticHint, null, getNextOrdinal());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.util.INotationType#getSemanticHint()
	 */
	public String getSemanticHint() {
		return getName();
	}

}