/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              	   |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.properties.descriptors;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.ibm.xtools.notation.NotationPackage;

/**
 * A label provider object used by the notation property descriptors.
 * 
 * @author nbalaba
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.properties.*
 */
class NotationItemLabelProvider
	extends LabelProvider {

	/*
	 * IItemPropertyDescriptor label provider. We override some functionality - the rest will be
	 * forwarded back to the original provider.
	 */
	private ILabelProvider itemLabelProvider;

	/*
	 *  The feature that the descriptor is describing
	 */
	private EStructuralFeature feature;

	/**
	 * Create a new label provider given the IItemPropertyDescriptor label provider and
	 * structural feature for descriptor .
	 *  
	 * @param provider - the IItemPropertyDescriptor label provider 
	 * @param feature - descriptors feature
	 */
	public NotationItemLabelProvider(ILabelProvider provider,
			EStructuralFeature feature) {
		this.itemLabelProvider = provider;
		this.feature = feature;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object anObject) {
		if (getFeature().isMany())
			return ""; //$NON-NLS-1$
		if (getFeature() == NotationPackage.eINSTANCE
			.getNode_LayoutConstraint())
			return ""; //$NON-NLS-1$
		if (anObject instanceof RGB) {
			RGB rgb = (RGB) anObject;
			return "(" + rgb.red + "," + rgb.green + "," + rgb.blue + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		return itemLabelProvider.getText(anObject);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object anObject) {
		if (anObject instanceof RGB) {
			ImageData id = createColorImage((RGB) anObject);
			ImageData mask = id.getTransparencyMask();
			return new Image(Display.getCurrent(), id, mask);
		}
		return itemLabelProvider.getImage(anObject);
	}

	/**
	 * Creates and returns the color image data for the given control and RGB
	 * value. The image's size is either the control's item extent or the cell
	 * editor's default extent, which is 16 pixels square.
	 * 
	 * @param w
	 *            the control
	 * @param color
	 *            the color
	 */
	private ImageData createColorImage(RGB color) {

		int size = 10;
		int indent = 6;
		int extent = 16;

		if (size > extent)
			size = extent;
		
		int width = indent + size;
		int height = extent;

		int xoffset = indent;
		int yoffset = (height - size) / 2;

		RGB black = new RGB(0, 0, 0);
		PaletteData dataPalette = new PaletteData(new RGB[] {black, black,
			color});
		ImageData data = new ImageData(width, height, 4, dataPalette);
		data.transparentPixel = 0;

		int end = size - 1;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if (x == 0 || y == 0 || x == end || y == end)
					data.setPixel(x + xoffset, y + yoffset, 1);
				else
					data.setPixel(x + xoffset, y + yoffset, 2);
			}
		}

		return data;
	}

	/**
	 * @return Returns the feature.
	 */
	protected EStructuralFeature getFeature() {
		return feature;
	}
}