/***************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004.  All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
***************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.ruler;

import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.rulers.RulerProvider;

import org.eclipse.gmf.runtime.diagram.core.listener.PresentationListener;
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
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		PresentationListener.getInstance().addPropertyChangeListener(getGuideStyle(),listener);

	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		PresentationListener.getInstance().removePropertyChangeListener(getGuideStyle(),listener);

	}
}
