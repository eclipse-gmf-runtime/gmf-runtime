/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import com.ibm.xtools.notation.NotationPackage;
import com.ibm.xtools.notation.ShapeStyle;
import com.ibm.xtools.notation.View;

/**
 * The OrGateView Factory class
 * 
 * @author mmostafa
 */
public class OrGateViewFactory
	extends LogicGateViewFactory {

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
		ShapeStyle style = (ShapeStyle)view.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
		style.setFillColor((FigureUtilities.colorToInteger(LogicColorConstants.orGate)).intValue());
		return view;
	}
}