/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.printing.render.internal;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.PageInfoHelper.PageMargins;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.FontData;

 
 /**
 * PageData is used during the JPS printing process to cache
 * information that will be used later when printing is invoked
 * through a call-back mechanism.
 *  
 * @author Christian Damus (cdamus)
 * @author James Bruck (jbruck)
 *
 */
 
public class PageData {
	
	final int index;
	final int row;
	final int column;

	final DiagramEditPart diagram;
	final Rectangle bounds;
	final PageMargins margins;
	final FontData font;
	final IPreferenceStore preferences;

	PageData(int index, int row, int column, DiagramEditPart diagram,
			Rectangle bounds, PageMargins margins, FontData font,
			IPreferenceStore preferences) {
		
		this.index = index;
		this.row = row;
		this.column = column;
		this.diagram = diagram;
		this.bounds = bounds;
		this.margins = margins;
		this.font = font;
		this.preferences = preferences;
	}
}
