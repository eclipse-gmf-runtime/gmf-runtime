/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.ruler;

import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.notation.Guide;
import org.eclipse.gmf.runtime.notation.GuideStyle;


/**
 * This Object contains information regarding the ruler and associated guides
 * 
 * @author jschofie
 */
public class DiagramRuler {

	private int unit;
	private boolean horizontal;
	private GuideStyle guideStyle;

	public DiagramRuler(boolean isHorizontal, GuideStyle style) {
		this(isHorizontal, RulerProvider.UNIT_INCHES, style);
	}

	public DiagramRuler(boolean isHorizontal, int unit, GuideStyle style) {
		horizontal = isHorizontal;
		setUnit(unit);
		guideStyle = style;
	}

	private GuideStyle getGuideStyle() {
		return guideStyle;
	}

	/**
	 * Returns a list of Guide(s)
	 */
	public List getGuides() {
		if( !isHorizontal() )
			return getGuideStyle().getHorizontalGuides();
		
		return getGuideStyle().getVerticalGuides();
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int newUnit) {
		if (unit != newUnit) {
			unit = newUnit;
		}
	}
	
	public void addGuide(Guide toAdd) {
		
		if( !isHorizontal() ) {
			guideStyle.getHorizontalGuides().add(toAdd);
		} else {
			guideStyle.getVerticalGuides().add(toAdd);
		}
	}
	
	public void addNotificationListener(
			TransactionalEditingDomain editingDomain,
			NotificationListener listener) {
		
		DiagramEventBroker.getInstance(editingDomain).addNotificationListener(
			getGuideStyle(), listener);
	}

	public void removeNotificationListener(
			TransactionalEditingDomain editingDomain,
			NotificationListener listener) {
		
		DiagramEventBroker.getInstance(editingDomain)
			.removeNotificationListener(getGuideStyle(), listener);
	}

	
	public void setGuideStyle(GuideStyle guideStyle) {
		this.guideStyle = guideStyle;
}
}
