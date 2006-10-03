/******************************************************************************
 * Copyright (c) 2000, 2003  IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Orientable;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ListScrollBar;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;


/**
 * An implementation of an animatable scroll pane
 * 
 * @author melaasar
 *
 * <p>
 * Code taken from Eclipse reference bugzilla #98820
 *
 */
public class AnimatableScrollPane extends ScrollPane {

	private AnimationModel animationModel = null;
	private boolean expanded = true;
	private static final long delay = 150;

	private void animate() {
		animationModel = new AnimationModel(delay, expanded);
		animationModel.animationStarted();
		while (!animationModel.isFinished())
			this.step();
		animationModel = null;
	}

	/**
	 * Should be called, after which the compoenents can be removed.
	 */
	public void collapse() {
		if (expanded == false)
			return;
		expanded = false;
		animate();
	}

	/** 
	 * Should get called after adding all the new components.
	 */
	public void expand() {
		if (expanded == true)
			return;
		expanded = true;
		animate();
	}

	/**
	 * Sets the scroll pane state as being expanded or not.
	 * 
	 * @param value the <code>boolean</code> to set the expanded value of.  
	 * If <code>true</code> then the state will be set as expanded, <code>false</code>
	 * if not.
	 */
	public void setExpanded(boolean value) {
		if (expanded == value)
			return;
		expanded = value;
		revalidate();
	}

	/**
	 * Accessor for the expanded property
	 * @return boolean expanded
	 */
	public final boolean isExpanded() {
		return expanded;
	}

	private void step() {
		revalidate();
		getUpdateManager().performUpdate();
	}

	/** @see org.eclipse.draw2d.IFigure#isOpaque() */
	public boolean isOpaque() {
		return false;
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		if (animationModel == null) {
			if (expanded)
				return super.getPreferredSize(wHint, hHint);
			return getMinimumSize(wHint, hHint);
		}
		Dimension pref = super.getPreferredSize(wHint, hHint);
		Dimension min = getMinimumSize(wHint, hHint);
		float scale = animationModel.getProgress();
		return pref.equals(min)
			? pref
			: pref.getScaled(scale).expand(min.getScaled(1.0f - scale));
	}
	
	/**
	 * @see IFigure#validate()
	 */
	public void validate() {
		if (isValid())
			return;
		setValid(true);
		layout();
		
		for (int i = 0; i < getChildren().size(); i++)
			((IFigure)getChildren().get(i)).validate();
		if ((hBar != null) && getHorizontalScrollBarVisibility()!= NEVER)
			getHorizontalScrollBar().validate();
		if ((vBar != null) && getVerticalScrollBarVisibility()!= NEVER)
			getVerticalScrollBar().validate();
	}
    
    
    protected void createVerticalScrollBar() {
        IMapMode mm= MapModeUtil.getMapMode(this);
        int mm_1 = mm.DPtoLP(1);
        Insets insets = new Insets(mm_1, mm.DPtoLP(2),
            mm_1, mm.DPtoLP(0));
        int mm_15 = mm.DPtoLP(15);
        Dimension size = new Dimension(mm_15, mm_15);
        setVerticalScrollBar( new ListScrollBar(Orientable.VERTICAL, insets, size, 
            mm.DPtoLP(10), mm.DPtoLP(50)));
    }
    
    
    protected void createHorizontalScrollBar() {
        IMapMode mm= MapModeUtil.getMapMode(this);
        int mm_1 = mm.DPtoLP(1);
        Insets insets = new Insets(mm_1, mm.DPtoLP(2),
            mm_1, mm.DPtoLP(0));
        int mm_15 = mm.DPtoLP(15);
        Dimension size = new Dimension(mm_15, mm_15);
        setHorizontalScrollBar( new ListScrollBar(Orientable.HORIZONTAL, insets, size, 
            mm.DPtoLP(10), mm.DPtoLP(50)));
    }
    
    public ScrollBar basicGetVerticalScrollBar() {
        return vBar;
    }
    
    
    public ScrollBar basicGetHorizontalScrollBar() {        
        return hBar;
    }



}
