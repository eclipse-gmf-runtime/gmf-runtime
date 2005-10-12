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

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * Colors section to represent line, font and fill colors properties of a 2D shape
 * 
 * @author nbalaba
 *
 */
public class ShapeColorsAndFontsPropertySection
	extends ColorsAndFontsPropertySection {


	protected ToolBar createFontsGroup(Composite contents) {
		ToolBar toolBar = super.createFontsGroup(contents);

		fillColorButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				changeFillColor(event);
			}
		});

		if (isReadOnly())
			fillColorButton.setEnabled(false);
		else
			fillColorButton.setEnabled(true);
		
		return toolBar;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance.ColorsAndFontsPropertySection#updateColorCache()
	 */
	protected void updateColorCache() {
		super.updateColorCache();
		executeAsReadAction(new MRunnable() {

			public Object run() {

				if (getSingleInput() instanceof GraphicalEditPart) {
					GraphicalEditPart ep = (GraphicalEditPart) getSingleInput();
					fillColor = FigureUtilities.integerToRGB((Integer) ep
						.getStructuralFeatureValue(NotationPackage.eINSTANCE.getFillStyle_FillColor()));
				} else
					fillColor = DEFAULT_PREF_COLOR;

				return fillColor;
			}
		});

	}

	/**
	 * Change fill color property value
	 */
	protected void changeFillColor(SelectionEvent event) {
		fillColor = changeColor(event, fillColorButton,
			IPreferenceConstants.PREF_FILL_COLOR, Properties.ID_FILLCOLOR,
			FILL_COLOR_COMMAND_NAME, FILL_COLOR_IMAGE_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#refresh()
	 */
	public void refresh() {
		super.refresh();
		if(!isDisposed()){
		Image overlyedImage = new ColorOverlayImageDescriptor(
			ResourceManager.getInstance().getImage(
				FILL_COLOR_IMAGE_NAME).getImageData(), fillColor).createImage();
		fillColorButton.setImage(overlyedImage);
		}
	}

}
