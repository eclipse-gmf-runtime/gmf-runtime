/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimationFigureHelper;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;



/**
 * Wrapper that contains the real figure and the gates.
 * This is required to allow the main figure's layout manager to ignore the gates.
 * The GatePane applies a delegating layout manager to allow gate children to lay themselves out.
 * 
 * @author jbruck
 */
public class GatedPaneFigure extends GateFigure {
	
	private GatedFigure gatedFigure;
	private IFigure elementFigure;
	
	/**
	 * Creates a new ClassiferNodeFigire figure.
	 * @param elementFig the figure to use with this figure 
	 */
	public GatedPaneFigure( IFigure elementFig ) {
		super(DrawConstant.INVALID);
		if (elementFig instanceof GateFigure) {
			setPreferredSide(((GateFigure) elementFig).getPreferredSide());
		}

		setOpaque(false); // set transparent by default
		setBorder(null);
		setLayoutManager(null);
		this.elementFigure = elementFig;

		add(getElementPane());
		add(getGatePane());

		setBounds(elementFig.getBounds().getCopy());
		getGatePane().setBounds(new Rectangle(0, 0, 1, 1));
	}
	
	/**
	 * For animated layout.
	 * @author jbruck
	 */
	public class AnimatableDelegatingLayout
		extends DelegatingLayout {

		public void layout(IFigure container) {
			if (!AnimationFigureHelper.getInstance().layoutManagerHook(container)) {
				super.layout(container);
			} 
		}
	}

	/**
	 * 
	 * @return The gate pane
	 */
	public GatedFigure getGatePane() {     
		if (gatedFigure == null) {
			gatedFigure = new GatedFigure();
			gatedFigure.setLayoutManager(new AnimatableDelegatingLayout());
			gatedFigure.setVisible(true);
		}
		return gatedFigure;
	}
	
	/**
	 * 
	 * @return The "main" figure
	 */
	public IFigure getElementPane() {
		return elementFigure;
	}
		
	/**
	 * @see org.eclipse.draw2d.IFigure#getClientArea(Rectangle)
	 */
	public Rectangle getClientArea(Rectangle rect) {
		if( elementFigure != null ){
			return elementFigure.getClientArea(rect);
		}
		return super.getClientArea(rect);
	}
	/**
	 * gets the handle bounds of the main figure
	 * @return  the hnalde bounds
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		if( elementFigure instanceof NodeFigure ) {
			return ((NodeFigure)elementFigure).getHandleBounds().getCopy();
		} else {
			return elementFigure.getBounds().getCopy();
		}
	}
	 
	
	protected void paintChildren(Graphics graphics) {
		super.paintChildren(graphics);
		if( gatedFigure != null )
			gatedFigure.paintClientArea(graphics);
	}
		
	/**
	 * Give the main figure the entire bounds of the wrapper.
	 */
	protected void layout() {
		if (!this.getBounds().equals(elementFigure.getBounds())) {
			elementFigure.setBounds(this.getBounds().getCopy());
		}
		// When parent resizes, cause the gates to be relocated.
		getGatePane().invalidateTree();
		erase();
	}
		

	/**
	 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
	 * We need to override this for smooth painting of gate items.
	 */
	public boolean containsPoint(int x, int y) {
		if( gatedFigure.containsPoint(x,y))	{
			return true;
		}
		return super.containsPoint(x, y);
	}
	
	
	
	protected void primTranslate(int dx, int dy) {
		super.primTranslate(dx,dy);
		erase();
	}
	
	
	/**
	 * @see org.eclipse.draw2d.IFigure#erase()
	 */
	public void erase() {
		super.erase();
		if( gatedFigure != null )
			gatedFigure.erase();
	}
	/**
	 * Refresh adornments
	 */
	public void repaint() {
		super.repaint();
		if( gatedFigure != null )
			gatedFigure.repaint();
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#findFigureAt(int, int, org.eclipse.draw2d.TreeSearch)
	 */
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		IFigure result = gatedFigure.findFigureAt(x,y,search);
		if( result != null ) {
			return result;
		}
		return elementFigure.findFigureAt(x,y,search);
	}
	
	/**
	 * @see org.eclipse.draw2d.IFigure#findFigureAt(int, int,
	 *      org.eclipse.draw2d.TreeSearch)
	 */
	public IFigure findMouseEventTargetAt(int x, int y) {
		IFigure gateFigure = gatedFigure.findMouseEventTargetAt(x,y);
		if( gateFigure != null )
			return gateFigure;
		return super.findMouseEventTargetAt(x, y);
	}
	
	/**
	 * @see org.eclipse.draw2d.IFigure#intersects(Rectangle)
	 */
	public boolean intersects(Rectangle rect) {
		if( gatedFigure.intersects(rect) ) {
			return true;
		}
		return super.intersects(rect);
	}
	
	/**
	 * @see IFigure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize(int wHint, int hHint) {
		return elementFigure.getMinimumSize(wHint, hHint);
	}
	
	/**
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return elementFigure.getPreferredSize(wHint, hHint);
	}
	

	/**
	 * @see org.eclipse.draw2d.IFigure#getToolTip()
	 */
	public IFigure getToolTip() {
		return elementFigure.getToolTip();
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#setToolTip(IFigure)
	 */
	public void setToolTip(IFigure f) {
		elementFigure.setToolTip(f);
	}
	
	/**
	 * Returns a new anchor for this node figure.
	 * @param p Point on the figure that gives a hint which anchor to return.
	 * @return ConnectionAnchor reference to an anchor associated with the given point on the figure.
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		if( elementFigure instanceof NodeFigure )
			return ((NodeFigure)elementFigure).getSourceConnectionAnchorAt(p);
		return super.getSourceConnectionAnchorAt(p);
	}
	
	/*
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getTargetConnectionAnchorAt(org.eclipse.draw2d.geometry.Point)
	 */
	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		if( elementFigure instanceof NodeFigure )
			return ((NodeFigure)elementFigure).getTargetConnectionAnchorAt(p);
		return super.getTargetConnectionAnchorAt(p);
	}
	
	/*
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getConnectionAnchor(java.lang.String)
	 */
	public ConnectionAnchor getConnectionAnchor(String terminal) {
		if( elementFigure instanceof NodeFigure )
			return ((NodeFigure)elementFigure).getConnectionAnchor(terminal);
		return super.getConnectionAnchor(terminal);
	}
	
	/*
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getConnectionAnchorTerminal(org.eclipse.draw2d.ConnectionAnchor)
	 */
	public String getConnectionAnchorTerminal(ConnectionAnchor c) {
		if( elementFigure instanceof NodeFigure )
			return ((NodeFigure)elementFigure).getConnectionAnchorTerminal(c);
		return super.getConnectionAnchorTerminal(c);		
	}

}
