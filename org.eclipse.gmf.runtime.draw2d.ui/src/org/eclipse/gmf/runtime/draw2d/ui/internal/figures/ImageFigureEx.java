/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
/*
 * Created on Sep 17, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.swt.graphics.Image;


/**
 * A copy of GEF's ImageFigure that takes care of himetric conversion
 * 
 * @author melaasar
 */
public class ImageFigureEx
	extends Figure {

	private Image img;

	private Dimension imgSize = new Dimension();

	private int alignment;

	/**
	 * Constructor <br>
	 * The default alignment is <code>PositionConstants.CENTER</code>.
	 */
	public ImageFigureEx() {
		this(null, PositionConstants.CENTER);
	}

	/**
	 * Constructor <br>
	 * The default alignment is <code>PositionConstants.CENTER</code>.
	 * 
	 * @param image
	 *            The Image to be displayed
	 */
	public ImageFigureEx(Image image) {
		this(image, PositionConstants.CENTER);
	}

	/**
	 * Constructor
	 * 
	 * @param image
	 *            The Image to be displayed
	 * @param alignment
	 *            A PositionConstant indicating the alignment
	 * 
	 * @see ImageFigure#setImage(Image)
	 * @see ImageFigure#setAlignment(int)
	 */
	public ImageFigureEx(Image image, int alignment) {
		setImage(image);
		setAlignment(alignment);
	}

	/**
	 * @return The Image that this Figure displays
	 */
	public Image getImage() {
		return img;
	}

    /**
     * @return <code>Dimension</code> that is the size of the image in logical coordinates
     */
    private Dimension getImageSize() {
        if (imgSize.isEmpty() && getImage() != null) {
            org.eclipse.swt.graphics.Rectangle r = getImage().getBounds();
            IMapMode mm = MapModeUtil.getMapMode(this);
            imgSize = new Dimension(mm.DPtoLP(r.width), 
                mm.DPtoLP(r.height));
        }
        
        if (!imgSize.isEmpty())
            return imgSize;
        
        return getBounds().getSize();
    }
    
	/**
	 * Returns the size of the Image that this Figure displays; or (0,0) if no
	 * Image has been set.
	 * 
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return getImageSize();
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		if (getImage() == null)
			return;

		int x, y;
		Rectangle area = getClientArea();
        Dimension size = getImageSize();
        
		switch (alignment & PositionConstants.NORTH_SOUTH) {
			case PositionConstants.NORTH:
				y = area.y;
				break;
			case PositionConstants.SOUTH:
				y = area.y + area.height - size.height;
				break;
			default:
				y = (area.height - size.height) / 2 + area.y;
				break;
		}
		switch (alignment & PositionConstants.EAST_WEST) {
			case PositionConstants.EAST:
				x = area.x + area.width - size.width;
				break;
			case PositionConstants.WEST:
				x = area.x;
				break;
			default:
				x = (area.width - size.width) / 2 + area.x;
				break;
		}
		graphics.drawImage(getImage(), x, y);
	}

	/**
	 * Sets the alignment of the Image within this Figure. The alignment comes
	 * into play when the ImageFigure is larger than the Image. The alignment
	 * could be any valid combination of the following:
	 * 
	 * <UL>
	 * <LI>PositionConstants.NORTH</LI>
	 * <LI>PositionConstants.SOUTH</LI>
	 * <LI>PositionConstants.EAST</LI>
	 * <LI>PositionConstants.WEST</LI>
	 * <LI>PositionConstants.CENTER or PositionConstants.NONE</LI>
	 * </UL>
	 * 
	 * @param flag
	 *            A constant indicating the alignment
	 */
	public void setAlignment(int flag) {
		alignment = flag;
	}

	/**
	 * Sets the Image that this ImageFigure displays.
	 * <p>
	 * IMPORTANT: Note that it is the client's responsibility to dispose the
	 * given image.
	 * 
	 * @param image
	 *            The Image to be displayed. It can be <code>null</code>.
	 */
	public void setImage(Image image) {
		if (img == image)
			return;
		img = image;
        
        // reset image size
        imgSize = new Dimension();
        
		revalidate();
		repaint();
	}
    
    
}

