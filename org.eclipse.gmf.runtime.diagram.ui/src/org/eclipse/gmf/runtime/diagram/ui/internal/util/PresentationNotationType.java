/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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