/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;


/**
 * Customizes the look of GEF's ScrollBar
 * 
 * @author sshaw
 * @author lgrahek
 *
 */
public class ListScrollBar extends ScrollBar {

	protected ArrowButton downButtonFigure;
	protected ArrowButton upButtonFigure;
	ThumbFigure thumb;
	
	protected static Color fillLightGrey = new Color(null, new RGB(240, 240, 240));
	protected static Color outlineLightGrey = new Color(null, new RGB(185, 185, 185));
	protected static Color fillDarkGrey = new Color(null, new RGB(84, 84, 84));
	protected static Color outlineDarkGrey = new Color(null, new RGB(109, 109, 109));
	protected static Color arrowFill = new Color(null, new RGB(187, 187, 187));
	
	protected int mm_1;
	protected int mm_2;

	/**
	 * Constructor
	 * 
	 * @param orientation int that is a define from <code>Orientable</code>
	 * @param insets the <code>Insets> that represents the white space buffer around the scroll bar in 
	 * logical coordinates.
	 * @param size the <code>Dimension</code> that is the size of the scroll bar end boxes in 
	 * logical coordinates
	 * @param stepInc the <code>int</code> space to jump when incrementing the scroll bar one step in
	 * logical coordinates
	 * @param pageInc the <code>int</code> space to jump when paging the scroll bar up or down in
	 * logical coordinates.
	 */
	public ListScrollBar(int orientation, Insets insets, Dimension size, int stepInc, int pageInc) {
		setOrientation(orientation);
		
		Border margin = new MarginBorder(insets.top, insets.left, insets.bottom, insets.right);		
		setBorder(margin);
		setPreferredSize(size.width, size.height);
		setStepIncrement(stepInc);
		setPageIncrement(pageInc);
		if (isHorizontal()) {
			downButtonFigure.setDirection(EAST);
			upButtonFigure.setDirection(WEST);
		} else {
			downButtonFigure.setDirection(SOUTH);
			upButtonFigure.setDirection(NORTH);
		}
		
        IMapMode mm= MapModeUtil.getMapMode(this);
        mm_1 = mm.DPtoLP(1);
        mm_2 = mm.DPtoLP(2);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ScrollBar#createDefaultDownButton()
	 */
	protected Clickable createDefaultDownButton() {
		downButtonFigure = new ArrowButton();
		downButtonFigure.setBorder(new MarginBorder(new Insets(mm_1)));
		Clickable button = new Clickable(downButtonFigure) {
			@Override
			public boolean hasFocus() {
				return false;
			}		
		};
		button.getModel().addChangeListener(new ChangeListener() {
			public void handleStateChanged(ChangeEvent event) {
				updateDownColors();
			}
		});

		button.setFiringMethod(Clickable.REPEAT_FIRING);
		button.setRolloverEnabled(true);
		return button;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ScrollBar#createDefaultUpButton()
	 */
	protected Clickable createDefaultUpButton() {
		upButtonFigure = new ArrowButton();
		upButtonFigure.setBorder(new MarginBorder(new Insets(mm_1)));
		Clickable button = new Clickable(upButtonFigure) {
			@Override
			public boolean hasFocus() {
				return false;
			}		
		};
		button.getModel().addChangeListener(new ChangeListener() {
			public void handleStateChanged(ChangeEvent event) {
				updateUpColors();
			}
		});

		button.setFiringMethod(Clickable.REPEAT_FIRING);
		button.setRolloverEnabled(true);
		return button;		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ScrollBar#initialize()
	 */
	protected void initialize() {
		super.initialize();
		setPageUp(null);
		setPageDown(null);
		setOpaque(true);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ScrollBar#createDefaultThumb()
	 */
	protected IFigure createDefaultThumb() {
		thumbDragger = new ThumbDraggerColors();
		
		thumb = new ThumbFigure();
		thumb.setMinimumSize(new Dimension(6, 6));
		return thumb;
	}

	/**
	 * Updates colors of down button depending on its state (highlighted,
	 * pressed, or neither)
	 */
	protected void updateDownColors() {
		boolean pressed = false;
		boolean highlighted = false;
		if (getValue() < (getMaximum() - getExtent())) {
			// Arrow is scrollable
			if (((Clickable) getButtonDown()).getModel().isPressed()) {
				pressed = true;
			} else if (((Clickable) getButtonDown()).getModel().isMouseOver()) {
				highlighted = true;
			}
		}
		boolean doRepaint = false;
		if (downButtonFigure.isHighlighted() != highlighted) {
			downButtonFigure.setHighlighted(highlighted);
			doRepaint = true;
		}
		if (downButtonFigure.isPressed() != pressed) {
			doRepaint = true;
			downButtonFigure.setPressed(pressed);
		}
		if (doRepaint) {
			// Just changing color, no recalculation of arrow position needed
			downButtonFigure.setRecalculate(false);			
			downButtonFigure.repaint();
		}
	}

	/**
	 * Updates colors of up button depending on its state (highlighted,
	 * pressed, or neither)
	 */	
	protected void updateUpColors() {
		boolean pressed = false;
		boolean highlighted = false;
		if (getValue() > getMinimum()) {
			// Arrow is scrollable			
			if (((Clickable) getButtonUp()).getModel().isPressed()) {
				pressed = true;
			} else if (((Clickable) getButtonUp()).getModel().isMouseOver()) {
				highlighted = true;
			}
		}
		boolean doRepaint = false;
		if (upButtonFigure.isHighlighted() != highlighted) {
			upButtonFigure.setHighlighted(highlighted);
			doRepaint = true;
		}
		if (upButtonFigure.isPressed() != pressed) {
			doRepaint = true;
			upButtonFigure.setPressed(pressed);
		}
		if (doRepaint) {
			// Just changing color, no recalculation of arrow position needed
			upButtonFigure.setRecalculate(false);
			upButtonFigure.repaint();
		}
	}

	/**
	 * Figure that defines the content for up and down buttons
	 * 
	 * @author lgrahek
	 * 
	 * @since 1.2
	 *
	 */
	protected class ArrowButton extends Figure 
	{
		private PointList arrow = new PointList();
		private boolean recalculate;
		private int direction;
		private boolean highlighted;
		private boolean pressed;		

		@Override
		protected void paintFigure(Graphics g) {
			Rectangle r = getBounds();
			// If not pressed, fill with white, so later there will be 1px of
			// white left around outline.
			g.setAlpha(130);
			if (!pressed) {
				g.setBackgroundColor(ColorConstants.white);
				g.fillRectangle(r);
			}						
			// Draw the outline (none when button is pressed)
			if (!pressed) {
				g.setLineWidth(mm_1);
				if (highlighted) {
					g.setForegroundColor(outlineDarkGrey);
				} else {
					g.setForegroundColor(outlineLightGrey);
				}
				g.drawRectangle(r);
			}
						
			// Fill the interior. If not pressed, leave 1px white between outline and filled area.
			if (pressed) {
				g.setBackgroundColor(fillDarkGrey);
			} else {
				g.setBackgroundColor(fillLightGrey);
				r = r.getCopy().shrink(mm_2, mm_2);
			}
			g.fillRectangle(r);		
			
			// Draw the arrow
			if (pressed) {
				r = r.getCopy().shrink(mm_2, mm_2);
			}
			if (recalculate || arrow.size() == 0) {
				int size;
				switch (direction) {
					case EAST:
					case WEST:
						size = Math.min(r.height / 2, r.width);
						r.x += (r.width - size) / 2;
						break;
					default: //North or south
						size = Math.min(r.height, r.width / 2);
						r.y += (r.height - size) / 2;
						break;
				}
				size = Math.max(size, 1); //Size cannot be negative		
				Point head, p2, p3;
				switch (direction) {
					case NORTH:
						head = new Point(r.x + r.width / 2, r.y);
						p2   = new Point (head.x - size, head.y + size);
						p3   = new Point (head.x + size, head.y + size);
						break;
					case SOUTH:
						head = new Point (r.x + r.width / 2, r.y + size);
						p2   = new Point (head.x - size, head.y - size);
						p3   = new Point (head.x + size, head.y - size);
						break;
					case WEST:
						head = new Point (r.x, r.y + r.height / 2);
						p2   = new Point (head.x + size, head.y - size);
						p3   = new Point (head.x + size, head.y + size);
						break;
					default:
						head = new Point(r.x + size, r.y + r.height / 2);
						p2   = new Point(head.x - size, head.y - size);
						p3   = new Point(head.x - size, head.y + size);
		
				}
				arrow.removeAllPoints();
				arrow.addPoint(p2);
				arrow.addPoint(head);
				arrow.addPoint(p3);
			} else {
				// Recalculating with every paint unless explicitly told not too
				recalculate = true;
			}
			if (highlighted) {
				g.setForegroundColor(fillDarkGrey);
			} else if (pressed) {
				g.setAlpha(255);
				g.setForegroundColor(ColorConstants.white);
			} else {
				g.setForegroundColor(arrowFill);
			}
			g.setLineWidth(mm_2);
			g.drawPolyline(arrow);			
		}

		/**
		 * @param value
		 *            one of PositionConstants: EAST, WEST, NORTH, SOUTH that
		 *            determine the arrow direction
		 */
		public void setDirection(int value) {
			direction = value;
		}

		/**
		 * @param value
		 *            false if arrow points do no have to be recalculated (e.g.
		 *            when just changing color)
		 */
		public void setRecalculate(boolean value) {
			recalculate = value;
		}
		
		/**
		 * @param value true if highlighted, false if not
		 */
		public void setHighlighted(boolean value) {
			highlighted = value;
		}
		
		/**
		 * @return true if highlighted, false if not
		 */
		public boolean isHighlighted() {
			return highlighted;
		}		
		
		/**
		 * @param value true if pressed, false if not
		 */
		public void setPressed(boolean value) {
			pressed = value;
		}

		/**
		 * @return true if pressed
		 */
		public boolean isPressed() {
			return pressed;
		}	
	}
	
	/**
	 * Figure that defines scroll bar thumb.
	 * 
	 * @author lgrahek
	 * 
	 * @since 1.2
	 *
	 */
	protected class ThumbFigure extends Panel {		
		boolean highlighted;
		boolean pressed;

		@Override
		protected void paintFigure(Graphics g) {
			g.setAlpha(130);
			
			Rectangle r = getBounds().getCopy();
			// Fill with white, so later there will be 1px of
			// white left around outline.
			g.setBackgroundColor(ColorConstants.white);
			g.fillRectangle(getBounds());
			// Draw the outline (none when button is pressed)
			g.setLineWidth(mm_1);
			if (highlighted || pressed) {
				g.setForegroundColor(outlineDarkGrey);
			} else {
				g.setForegroundColor(outlineLightGrey);
			}
			g.drawRectangle(r);
									
			// Fill the interior, while leaving 1px white between outline and filled area.
			r = r.getCopy().shrink(mm_2, mm_2);
			if (pressed) {
				g.setBackgroundColor(fillDarkGrey);
			} else {
				g.setBackgroundColor(fillLightGrey);				
			}
			g.fillRectangle(r);		
		}	

		/**
		 * @param value true if highlighted, false if not
		 */
		public void setHighlighted(boolean value) {
			highlighted = value;
		}
		
		/**
		 * @return true if highlighted, false if not
		 */		
		public boolean isHighlighted() {
			return highlighted;
		}		
		
		/**
		 * @param value true if pressed, false if not
		 */		
		public void setPressed(boolean value) {
			pressed = value;
		}

		/**
		 * @return true if pressed
		 */		
		public boolean isPressed() {
			return pressed;
		}			
	}
	
	/**
	 * Customizes ThumbDragger to set different colors to thumb depending on its
	 * state.
	 * 
	 * @author lgrahek
	 * 
	 * @since 1.2
	 * 
	 */
	protected class ThumbDraggerColors extends ThumbDragger {
		
		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.MouseMotionListener.Stub#mouseEntered(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent me) {
			// Set up highlight colors
			thumb.setHighlighted(true);
			thumb.setPressed(false);
			thumb.repaint();
			super.mouseEntered(me);
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.MouseMotionListener.Stub#mouseExited(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent me) {
			// Set up colors: back to no-hover, no-drag colors
			thumb.setHighlighted(false);
			thumb.setPressed(false);
			thumb.repaint();
			super.mouseExited(me);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.ScrollBar.ThumbDragger#mousePressed(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent me) {
			// Set up pressed colors
			thumb.setHighlighted(false);
			thumb.setPressed(true);
			thumb.repaint();
			super.mousePressed(me);
		}
	
		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.ScrollBar.ThumbDragger#mouseReleased(org.eclipse.draw2d.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent me) {
			// Set up colors: back to hover
			thumb.setHighlighted(true);
			thumb.setPressed(false);
			thumb.repaint();
			super.mouseReleased(me);
		}		
	}	
}
