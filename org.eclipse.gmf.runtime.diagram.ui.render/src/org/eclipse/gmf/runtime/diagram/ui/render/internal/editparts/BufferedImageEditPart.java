/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.render.internal.editparts;

import java.beans.PropertyChangeEvent;

import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import com.ibm.xtools.notation.View;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 * 
 * Concrete subclass of AbstractImageEditPart for images that are based on a
 * stored buffer instead of a file.
 */
public class BufferedImageEditPart
	extends AbstractImageEditPart {

	/**
	 * Default constructor
	 * 
	 * @param view
	 *            IShapeView that the EditPart is controlling
	 */
	public BufferedImageEditPart(View view) {
		super(view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.AbstractImageEditPart#regenerateImageFromSource()
	 */
	public RenderedImage regenerateImageFromSource() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent evt) {
		super.handlePropertyChangeEvent(evt);
	}
}