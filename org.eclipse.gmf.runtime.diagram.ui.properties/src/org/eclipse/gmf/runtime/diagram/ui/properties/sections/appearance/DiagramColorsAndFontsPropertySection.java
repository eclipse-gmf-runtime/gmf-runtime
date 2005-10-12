/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;


public class DiagramColorsAndFontsPropertySection
	extends ShapeColorsAndFontsPropertySection {
	/**
	 * @return - an itertor object to iterate over the selected/input edit parts
	 */
	protected Iterator getInputIterator() {
		DiagramEditPart diagram = (DiagramEditPart) super.getSingleInput();
		return diagram != null ? diagram.getPrimaryEditParts().iterator()
			: Collections.EMPTY_LIST.iterator();

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractNotationPropertiesSection#getSingleInput()
	 */
	public IGraphicalEditPart getSingleInput() {

		DiagramEditPart diagram = (DiagramEditPart) super.getSingleInput();
		if (diagram != null)
			return (IGraphicalEditPart) diagram.getPrimaryChildEditPart();
		return null;
	}

	/**
	 * Change fill color property value
	 */
	protected void changeFillColor() {
	
		// Update model in response to user
	
		if (fillColor != null) {
	
			List commands = new ArrayList();
			Iterator it = getInputIterator();
	
			while (it.hasNext()) {
				final IGraphicalEditPart ep = (IGraphicalEditPart) it.next();
				if (!(ep instanceof ConnectionNodeEditPart))
					commands.add(createCommand(FILL_COLOR_COMMAND_NAME,
						((View) ep.getModel()).eResource(), new Runnable() {
	
							public void run() {
								ep.setStructuralFeatureValue(NotationPackage.eINSTANCE.getFillStyle_FillColor(),
									FigureUtilities.RGBToInteger(fillColor));
							}
						}));
			}
	
			executeAsCompositeCommand(FILL_COLOR_COMMAND_NAME, commands);
			Image overlyedImage = new ColorOverlayImageDescriptor(
				ResourceManager.getInstance().getImage(FILL_COLOR_IMAGE_NAME)
					.getImageData(), fillColor).createImage();
			fillColorButton.setImage(overlyedImage);
		}
	}

	/**
	 * Adapt the object to an EObject - if possible
	 * 
	 * @param object
	 *            object from a diagram or ME
	 * @return EObject
	 */
	protected EObject adapt(Object object) {
		if (object instanceof IAdaptable) {
			if (object instanceof IGraphicalEditPart)// digram case
				return (EObject) ((IAdaptable) object).getAdapter(View.class);
			// ME case
			return (EObject) ((IAdaptable) object).getAdapter(EObject.class);
		}
	
		return null;
	}	
}
