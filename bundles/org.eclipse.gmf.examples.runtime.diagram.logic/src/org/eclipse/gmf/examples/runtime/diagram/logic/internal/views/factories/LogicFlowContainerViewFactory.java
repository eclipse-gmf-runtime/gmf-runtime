/******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal.ShapeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The LogicFlowContainerView Factory class 
 * @author mmostafa
 */
public class LogicFlowContainerViewFactory
	extends ShapeViewFactory {

	/**
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param persisted
	 */
	public View createView(IAdaptable semanticAdapter, View containerView,
			String semanticHint, int index, boolean persisted, final PreferencesHint preferencesHint) {
		View view =  super.createView(semanticAdapter, containerView, semanticHint,
			index, persisted, preferencesHint);
		return view;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.view.AbstractNodeView#decorateView(org.eclipse.gmf.runtime.diagram.ui.internal.view.IContainerView,
	 *      org.eclipse.core.runtime.IAdaptable, java.lang.String, int, boolean)
	 */
	protected void decorateView(View containerView, View view,
			IAdaptable semanticAdapter, String semanticHint, int index,
			boolean persisted) {
		super.decorateView(containerView, view, semanticAdapter, semanticHint,
			index, persisted);
        ShapeStyle style = (ShapeStyle)view.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
        style.setFillColor((FigureUtilities.colorToInteger(LogicColorConstants.logicGreen)).intValue());
        style.setLineColor((FigureUtilities.colorToInteger(LogicColorConstants.logicBlack)).intValue());
		getViewService().createNode(semanticAdapter, view,
			LogicConstants.LOGIC_FLOW_COMPARTMENT, ViewUtil.APPEND, getPreferencesHint());	
	}
}