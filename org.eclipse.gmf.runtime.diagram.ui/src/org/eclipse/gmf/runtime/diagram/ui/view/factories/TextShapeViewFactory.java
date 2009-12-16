/******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.view.factories;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.InternalDiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal.ShapeViewFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The Factory class responsible for creating the Text shape view 
 * @author mmostafa
 */
public class TextShapeViewFactory
	extends ShapeViewFactory {

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.view.AbstractNodeView#decorateView(org.eclipse.gmf.runtime.diagram.ui.internal.view.IContainerView,
	 *      org.eclipse.core.runtime.IAdaptable, java.lang.String, int, boolean)
	 */
	protected void decorateView(View containerView, View view,
			IAdaptable semanticAdapter, String semanticHint, int index,
			boolean persisted) {
		super.decorateView(containerView, view, semanticAdapter, semanticHint,
			index, persisted);

		getViewService().createNode(semanticAdapter, view,
			ViewType.DIAGRAM_NAME, ViewUtil.APPEND, persisted, getPreferencesHint());

		getViewService().createNode(semanticAdapter, view,
			CommonParserHint.DESCRIPTION, ViewUtil.APPEND, persisted, getPreferencesHint());
		
		// Set initial description to "Text"
		ShapeStyle shapeStyle = (ShapeStyle)view.getStyle(NotationPackage.Literals.SHAPE_STYLE);
		if (shapeStyle != null) {
			shapeStyle.setDescription(InternalDiagramUIMessages.Text_InitialValue);
		}		
	}
}