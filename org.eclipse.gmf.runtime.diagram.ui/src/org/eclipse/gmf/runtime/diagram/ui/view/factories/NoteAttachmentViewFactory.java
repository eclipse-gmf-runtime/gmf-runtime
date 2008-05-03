/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.view.factories;

import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.view.factories.ConnectionViewFactory;
import org.eclipse.gmf.runtime.notation.ConnectorStyle;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * View factory for the note attachment (line).
 * 
 * @author Anthony Hunter
 * @since 2.1
 */
public class NoteAttachmentViewFactory extends ConnectionViewFactory {

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.view.factories.ConnectionViewFactory#createStyles(org.eclipse.gmf.runtime.notation.View)
	 */
	protected List createStyles(View view) {
		List styles = super.createStyles(view);
		
		ConnectorStyle style = (ConnectorStyle) styles.get(0);
		if (style != null) {
			style.setLineWidth(1);
		}
		
		styles.add(NotationFactory.eINSTANCE.createArrowStyle());
		styles.add(NotationFactory.eINSTANCE.createLineTypeStyle());
		
		return styles;
	}

}
