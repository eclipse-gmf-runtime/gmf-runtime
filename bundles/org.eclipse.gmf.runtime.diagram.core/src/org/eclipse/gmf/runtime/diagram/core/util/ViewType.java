/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.core.util;

import org.eclipse.gmf.runtime.diagram.core.services.ViewService;

/**
 * Defines the basic view types.
 * The view type is required at View creation time; It could be an empty string 
 * or it could be a String that indicates what this view respresent.
 * The View type will be set by the {@link ViewService} on the {@link org.eclipse.gmf.runtime.notation.View}, 
 * and it can be retrieved by calling {@link org.eclipse.gmf.runtime.notation.View#getType()}
 * 
 */
public class ViewType {
	/**
	 * the note semantic hint
	 */
	public static String NOTE = "Note"; //$NON-NLS-1$
	/**
	 * the text semanti hint
	 */
	public static String TEXT = "Text"; //$NON-NLS-1$
	/**
	 * the note attachment semantic hint
	 */
	public static String NOTEATTACHMENT = "NoteAttachment"; //$NON-NLS-1$	
	/**
	 * diagram name semantic hint
	 */
	public static String DIAGRAM_NAME = "DiagramName"; //$NON-NLS-1$
	/**
	 * the diagram link semantic hint
	 */
	public static String DIAGRAM_LINK = "DiagramLink"; //$NON-NLS-1$

    /**
     * the group semantic hint
     */
    public static String GROUP = "Group"; //$NON-NLS-1$

}
