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

package org.eclipse.gmf.runtime.diagram.ui.figures;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Orientable;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ListScrollBar;
import org.eclipse.gmf.runtime.draw2d.ui.figures.OneLineBorder;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimatableScrollPane;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.OverlayScrollPaneLayout;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;


/**
 * A figure to represent the resizable compartment There are two constructors
 * available The one that takes a String:title consists of two childen: title
 * label + animatable scroll pane. The other one just consists of an animatable
 * scroll pane.
 *
 * <p>
 * Code taken from Eclipse reference bugzilla #98820
 * 
 * @author melaasar
 * @author choang
 */
/**
 * @author Steve
 *
 */
public class ResizableCompartmentFigure extends NodeFigure {

	private boolean _horizontal = false;

	/**
	 * The pane for all text compartment including the title
	 */
	private Figure textPane = null;
	/**
	 * The compartment title label
	 */
	private WrapLabel titleLabel;
	
	/**
	 * the minimum size the client area can occupy in logical coordinates
	 */
	private int minClientSize = 0;
	
	/**
	 * The compartment scroll pane
	 */
	protected ScrollPane scrollPane;
	/**
	 * The selected state
	 */
	private boolean selected;

	/**
	 * Indicates if the scroll pane has been initialized.
	 */
	private boolean isScrollPaneInitialized = false;

	/**
	 * Specifies the default minimum client size of this figure in device coordinates.
	 * Clients should use their editors <code>IMapMode</code> to convert this to logical
	 * coordinates.
	 */
	public static final int MIN_CLIENT_DP = 11;
    
    /**
     * A constructor for a top level resizable compartment figure
     * 
     * @param compartmentTitle <code>String</code> that is the title that is
     * displayed at the top of the compartment figure (optional).
     * @param mm the <code>IMapMode</code> that is used to initialize the
     * default values of of the scrollpane contained inside the figure.  This is
     * necessary since the figure is not attached at construction time and consequently
     * can't get access to the owned IMapMode in the parent containment hierarchy.
     */
    public ResizableCompartmentFigure(String compartmentTitle, IMapMode mm) {
        this.minClientSize = mm.DPtoLP(MIN_CLIENT_DP);
        setTextPane(new Figure() {
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        });
        getTextPane().setLayoutManager(new ConstrainedToolbarLayout());
        add(getTextPane());
        add(scrollPane = createScrollPane(mm));
        setLayoutManager(new ConstrainedToolbarLayout());
        setTitle(compartmentTitle);
        setToolTip(compartmentTitle);
        setBorder(new OneLineBorder());
    }
    
	/**
	 * Creates the animatable scroll pane
	 * 
	 * @return <code>AnimatableScrollPane</code>
     * @deprecated use {@link ResizableCompartmentFigure#createScrollPane(IMapMode)} instead
	 */
	protected AnimatableScrollPane createScrollpane() {
		return createScrollpane(MapModeUtil.getMapMode(this));
	}
    
    /**
     * Creates the animatable scroll pane
     * 
     * @param mm the <code>IMapMode</code> that is used to initialize the
     * default values of of the scrollpane contained inside the figure.  This is
     * necessary since the figure is not attached at construction time and consequently
     * can't get access to the owned IMapMode in the parent containment hierarchy.
     * @return <code>AnimatableScrollPane</code>
     * @deprecated use {@link ResizableCompartmentFigure#createScrollPane(IMapMode)} instead
     */
    protected AnimatableScrollPane createScrollpane(IMapMode mm) {
        scrollPane = new AnimatableScrollPane();
        scrollPane.getViewport().setContentsTracksWidth(true);
        scrollPane.getViewport().setContentsTracksHeight(false);
        scrollPane.setLayoutManager(new OverlayScrollPaneLayout());
        
        Insets insets = new Insets(mm.DPtoLP(1), mm.DPtoLP(2),
            mm.DPtoLP(1), mm.DPtoLP(0));
        Dimension size = new Dimension(mm.DPtoLP(15), mm.DPtoLP(15));
        
        scrollPane.setVerticalScrollBar(new ListScrollBar(Orientable.VERTICAL, insets, size, 
                                    mm.DPtoLP(10), mm.DPtoLP(50)));
        scrollPane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
        scrollPane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
        scrollPane.setContents(new Figure());
        scrollPane.getContents().setBorder(
                new MarginBorder(1, getMinClientSize()/2, 1, getMinClientSize()/2));            
        return (AnimatableScrollPane)scrollPane;
    }
    
    /**
     * Creates the animatable scroll pane
     * 
     * @param mm the <code>IMapMode</code> that is used to initialize the
     * default values of of the scrollpane contained inside the figure.  This is
     * necessary since the figure is not attached at construction time and consequently
     * can't get access to the owned IMapMode in the parent containment hierarchy.
     * @return <code>ScrollPane</code>
     */
    protected ScrollPane createScrollPane(IMapMode mm) {
        return createScrollpane(mm);
    }
	
	/**
	 * @return that is the minimum size the client area can occupy in 
	 * logical coordinates.
	 */
	final protected int getMinClientSize() {
		return minClientSize;
	}
	
	/**
	 * Sets the compartment title visibility
	 * 
	 * @param visibility
	 */
	public void setTitleVisibility(boolean visibility) {
		getTextPane().setVisible(visibility);
	}
	/**
	 * Expands the compartment figure
	 */
	public void expand() {
        if (scrollPane instanceof AnimatableScrollPane) {
            ((AnimatableScrollPane)scrollPane).expand();
        }
		scrollPane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
	}
	/**
	 * Collapses the compartment figure
	 */
	public void collapse() {
		scrollPane.setVerticalScrollBarVisibility(ScrollPane.NEVER);
        if (scrollPane instanceof AnimatableScrollPane) {
            ((AnimatableScrollPane)scrollPane).collapse();
        }
	}
	/**
	 * Expands the compartment figure
	 */
	public void setExpanded() {
        if (scrollPane instanceof AnimatableScrollPane) {
            ((AnimatableScrollPane)scrollPane).setExpanded(true);
        }
		scrollPane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
	}
	/**
	 * Collapses the compartment figure
	 */
	public void setCollapsed() {
		scrollPane.setVerticalScrollBarVisibility(ScrollPane.NEVER);
        if (scrollPane instanceof AnimatableScrollPane) {
            ((AnimatableScrollPane)scrollPane).setExpanded(false);
        }
	}
	
	/**
	 * @return The contents pane of this compartment figure
	 */
	public IFigure getContentPane() {
		return scrollPane.getContents();
	}

	
	public void setFont(Font f) {
		textPane.setFont(f);
	}
	/**
	 * Sets the font color of the compartment title label
	 * 
	 * @param c
	 *            The new color
	 */
	public void setFontColor(Color c) {
		textPane.setForegroundColor(c);
	}
	
	/**
	 * Set the compartment title to the supplied text.
	 * @param title this figure title
	 */
	public void setTitle(String title) {
		if (title == null) {
			if (titleLabel != null)
				getTextPane().remove(titleLabel);
		} else if (titleLabel == null) {
			getTextPane().add(titleLabel = new WrapLabel(title));
		} else
			titleLabel.setText(title);
	}
	
	/**
	 * Set the tooltip to the supplied text.
	 * @param tooltip this figure tooltip
	 */
	public void setToolTip(String tooltip) {
		if (tooltip == null)
			setToolTip((IFigure) null);
		else if (getToolTip() instanceof Label)
			((Label) getToolTip()).setText(tooltip);
		else
			setToolTip(new Label(tooltip));
	}
	
	/**
	 * scrollpane accessor
	 * 
	 * @return the scrollpane figure.
	 */
	public final ScrollPane getScrollPane() {
		return scrollPane;
	}
	
	/**
	 * Accessor for the expanded property
	 * 
	 * @return boolean expanded
	 */
	public final boolean isExpanded() {
        if (scrollPane instanceof AnimatableScrollPane) {
            return ((AnimatableScrollPane)scrollPane).isExpanded();
        }
        
		return true;
	}
	
	/**
	 * Return this figure's compartment title.
	 * @return <code>String</code>
	 */
	public final String getCompartmentTitle() {
		return titleLabel == null ? null : titleLabel.getText();
	}
	
	/**
	 * Gets the adjacent visible sibling before (or after) the figure
	 * @param before flag to identify the before or after, <code>true</code>
	 * means before, <code>false</code> means after
	 * @return <code>IFigure</code>
	 */
	public final IFigure getAdjacentSibling(boolean before) {
		List siblings = getParent().getChildren();
		int index = siblings.indexOf(this);
		if (before) {
			for (int i = index - 1; i >= 0; i--) {
				IFigure sibling = (IFigure) siblings.get(i);
				if (sibling instanceof ResizableCompartmentFigure
						&& sibling.isVisible())
					return sibling;
			}
		} else {
			for (int i = index + 1; i < siblings.size(); i++) {
				IFigure sibling = (IFigure) siblings.get(i);
				if (sibling instanceof ResizableCompartmentFigure
						&& sibling.isVisible())
					return sibling;
			}
		}
		return null;
	}
	
	/**
	 * Sets the selection state of this label
	 * 
	 * @param b
	 *            true will cause the label to appear selected
	 */
	public void setSelected(boolean b) {
		if (this.selected == b)
			return;
		selected = b;
		repaint();
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		if (selected) {
			graphics.setLineWidth(2);
			graphics.drawRectangle(getClientArea().shrink(MapModeUtil.getMapMode(this).DPtoLP(1), MapModeUtil.getMapMode(this).DPtoLP(1)));
		}
	}
	
	/**
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension p = super.getPreferredSize(wHint, hHint);
		return p.getUnioned(getMinimumSize(wHint, hHint));
	}
	/**
	 * @return makes sure that we can fit the collapse handles and the
	 * contents of the scroll pane. 
	 * @see org.eclipse.draw2d.IFigure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize(int w, int h) {
		
		if (minSize != null)
			return minSize;
		
		minSize = new Dimension(); 
		
		if (getLayoutManager() != null) {
			minSize = getLayoutManager().getMinimumSize(this, w, h);
		}
	
		int minHeight = getMinClientDimension().height+getInsets().getHeight();
		minSize.height = Math.max(minHeight, minSize.height);
		if (h >= 0)
			minSize.height = Math.min(minSize.height, h);
		
		int minWidth = getMinClientDimension().width+getInsets().getWidth();
		minSize.width = Math.max(minWidth, minSize.width);
		if (w >= 0)
			minSize.width = Math.min(minSize.width, w);
		
		return minSize;
	}

	/**
	 * getter for the horizontal flag
	 * @return the horizontal flag
	 */
	public final boolean isHorizontal() {
		return _horizontal;
	}
	
	/**
	 * setter for the horizontal flag
	 * @param horizontal the new value of the horizontal flag
	 */
	public final void setHorizontal(boolean horizontal) {
		_horizontal = horizontal;
	}
	
	/**
	 * @see org.eclipse.draw2d.IFigure#getMaximumSize()
	 */
	public Dimension getMaximumSize() {
		Dimension d = super.getMaximumSize().getCopy();
		if (!isExpanded())
			if ( isHorizontal() ) {	
				d.width = getPreferredSize().width;	
			}
			else {	
				d.height = getPreferredSize().height;
			}
		return d;
	}
	/**
	 * @see IFigure#invalidate()
	 */
	public void invalidate() {
		prefSize = null;
		minSize = null;
		super.invalidate();
	}
	/**
	 * @return Returns the textPane.
	 */
	public Figure getTextPane() {
		return textPane;
	}
	/**
	 * @param textPane
	 *            The textPane to set.
	 */
	private void setTextPane(Figure textPane) {
		this.textPane = textPane;
	}

	/**
	 * For this compartment we need it to be a min size so to fit the 
	 * collapse handles and to give the user an area they
	 * can drag and drop into the list compartment
	 * even if there is nothing in the compartment 
	 * @return <code>Dimension</code>
	 */
	public Dimension getMinClientDimension(){
		return new Dimension(getMinClientSize(), getMinClientSize());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#validate()
	 */
	public void validate() {
		super.validate();

		// Need a place to do this after the child figures have all been
		// created.
		initializeScrollPane();
	}
	
	/**
	 * Initializes the scroll to x and y locations on the scroll pane to
	 * accomodate children figures with negative locations. See RATLC00142157.
	 */
	private void initializeScrollPane() {
		if (!isScrollPaneInitialized) {
			if (getScrollPane() != null) {
				Point topLeft = getScrollPane().getContents().getBounds()
					.getTopLeft();
				if (topLeft.x < 0) {
					getScrollPane().getViewport().getHorizontalRangeModel()
						.setValue(topLeft.x);
				}
				if (topLeft.y < 0) {
					getScrollPane().getViewport().getVerticalRangeModel()
						.setValue(topLeft.y);
				}
			}
			isScrollPaneInitialized = true;
		}
	}
}
