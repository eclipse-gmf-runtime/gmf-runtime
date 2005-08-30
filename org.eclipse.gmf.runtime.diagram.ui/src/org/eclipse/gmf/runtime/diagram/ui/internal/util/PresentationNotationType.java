/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.util;

import org.eclipse.gmf.runtime.diagram.ui.util.INotationType;
import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;

/**
 * Element types for notation elements defined in the presentation plugin.
 * 
 * @author cmahoney, ldamus
 * @canBeSeenBy %level1
 */
public class PresentationNotationType
	extends AbstractElementTypeEnumerator {

	public static final INotationType NOTE = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.presentation.note"); //$NON-NLS-1$

	public static final INotationType NOTE_ATTACHMENT = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.presentation.noteAttachment"); //$NON-NLS-1$

	public static final INotationType TEXT = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.presentation.text"); //$NON-NLS-1$

}