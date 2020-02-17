/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.editparts;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.figures.ScalableImageFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.WrapperNodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

/**
 * @author sshaw
 * 
 * Abstract EditPart for handling display of a image of the diagram surface.
 * There are no assumptions about a semantic element being owned by the view. It
 * is up to subclasses to determine where the image information is being
 * retrieved from.
 */
public abstract class AbstractImageEditPart
	extends ShapeNodeEditPart {

	private RenderedImage renderedImage = null;
	
	/* Keep figure in order to apply anti-aliasing */
	ScalableImageFigure sif = null;

	   /**
	* Listener for the PreferenceStore.
	* Listen and respond for changes to the 
	* drop shadow preference store value.
	* 
	*/
	protected class PreferencePropertyChangeListener 
		implements IPropertyChangeListener {
	
		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {			           
			if (event.getProperty().equals(IPreferenceConstants.PREF_ENABLE_ANTIALIAS)){
				refreshEnableAntiAlias();
			}			
		}
	}

	
	
	/**
	 * Constructor
	 * 
	 * @param view
	 *            IShapeView element that this is a controller for.
	 */
	public AbstractImageEditPart(View view) {
		super(view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#createNodeFigure()
	 */
	protected NodeFigure createNodeFigure() {

		// Check anti-aliasing preference
		IPreferenceStore preferenceStore =
			(IPreferenceStore) getDiagramPreferencesHint().getPreferenceStore();
		boolean antiAlias = preferenceStore.getBoolean(
			IPreferenceConstants.PREF_ENABLE_ANTIALIAS);
		sif = new ScalableImageFigure(getRenderedImage(),
			true, true, antiAlias);
		sif.setMaintainAspectRatio(false);

		return new WrapperNodeFigure(sif);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshImage();
	}

	/**
	 * Refresh the display image if accessible. Essentially toggles between the
	 * comment mode and image mode depending on whether there is an image that
	 * can be displayed.
	 */
	protected void refreshImage() {
		renderedImage = null;
		getFigure().invalidate();
	}

	/**
	 * Refreshes the figures and enables anti-aliasing on the
	 * non-text portions
	 */
	protected void refreshEnableAntiAlias() {
		// Check anti-aliasing preference
		IPreferenceStore preferenceStore =
			(IPreferenceStore) getDiagramPreferencesHint().getPreferenceStore();
		boolean antiAlias = preferenceStore.getBoolean(
			IPreferenceConstants.PREF_ENABLE_ANTIALIAS);
		sif.setAntiAlias(antiAlias);
	}	
	
	
	/**
	 * regenerateImageFromSource This method will recreate the RenderedImage
	 * object from a specific source (file, bits etc.).
	 * 
	 * @return RenderedImage object that will be rendered to the screen using
	 *         the ScalableImageFigure class. Concrete clients of
	 *         AbstractImageEditPart need to override this method to return an
	 *         instance that can be used for rendering.
	 */
	abstract protected RenderedImage regenerateImageFromSource();

	/**
	 * getRenderedImage Accessor method to return the cached rendered image used
	 * for display.
	 * 
	 * @return RenderedImage object that was generated using the
	 *         regenerateImageFromSource api.
	 */
	final public RenderedImage getRenderedImage() {
		if (renderedImage == null) {
			RenderedImage img = regenerateImageFromSource();
			if (img != null && img.getSWTImage() != null)
				renderedImage = img;
		}

		return renderedImage;
	}
}
