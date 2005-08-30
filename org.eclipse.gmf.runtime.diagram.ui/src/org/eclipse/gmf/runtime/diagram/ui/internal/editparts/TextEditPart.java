/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;

import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import com.ibm.xtools.notation.View;

/*
 * @canBeSeenBy %level1
 */
public class TextEditPart extends ShapeEditPart {

	private static final int TEXT_DEFAULT_WIDTH  = 2645;
	private static final int TEXT_DEFAULT_HEIGHT =  344;
	

	/**
	* a static array of appearance property ids applicable to shapes
	*/
	private static final String[] appearanceProperties =
		new String[] {
			Properties.ID_ISVISIBLE,
			Properties.ID_FONTNAME,
			Properties.ID_FONTSIZE,
			Properties.ID_FONTBOLD,
			Properties.ID_FONTITALIC,
			Properties.ID_FONTCOLOR};

	public TextEditPart(View view) {
		super(view);
	}

	protected IFigure createFigure() {
		DefaultSizeNodeFigure nodeFigure = new DefaultSizeNodeFigure();
		nodeFigure.setBorder(null);
		nodeFigure.setOpaque(false);
		nodeFigure.setLayoutManager(new StackLayout());
		nodeFigure.setDefaultSize(new Dimension(TEXT_DEFAULT_WIDTH, TEXT_DEFAULT_HEIGHT));
		return nodeFigure;
	}

	protected void setBackgroundColor(Color color) {
		getFigure().setBackgroundColor(null);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 * The Text view does not have foreground and background colors
	 */
	protected void refreshVisuals() {
		refreshVisibility();
		refreshBounds();
		refreshFont();
	}

	/**
	 * Returns an array of the appearance property ids applicable to the receiver.
	 * Fro this type it is  
	 *			Properties.ID_ISVISIBLE
	 * @return - an array of the appearance property ids applicable to the receiver
	 */
	private String[] getAppearancePropertyIDs() {
		return appearanceProperties;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#fillAppearancePropertiesMap(java.util.Map)
	 */
	public void fillAppearancePropertiesMap(Map properties) {
		if (getAppearancePropertyIDs().length > 0) {
			// only if there are any appearance properties
			final Dictionary local_properties = new Hashtable();
			for (int i = 0; i < getAppearancePropertyIDs().length; i++)
				local_properties.put(
					getAppearancePropertyIDs()[i],
					getPropertyValue(getAppearancePropertyIDs()[i]));
			String semanticType = ""; //$NON-NLS-1$
			View view = getNotationView();
			if (view!=null)
				semanticType = view.getType();
			properties.put(semanticType,local_properties);
		}
	}
	
	/**
	 * this method will return the primary child EditPart  inside this edit part
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart(){
		return getChildBySemanticHint(CommonParserHint.DESCRIPTION);
	}

}
