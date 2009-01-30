/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.descriptors;

import java.util.StringTokenizer;

import org.eclipse.gmf.runtime.common.ui.dialogs.GradientSelectionDialog;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.GradientStyle;
import org.eclipse.gmf.runtime.notation.datatype.GradientData;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

/**
 * A cell editor that manages the gradient field.
 * 
 * @author lgrahek
 * @since 1.2
 */
public class GradientCellEditor extends DialogCellEditor {

    /**
     * The composite widget containing the color and RGB label widgets
     */
    private Composite composite;

    /**
     * The label widget showing the current gradient. Format:
     * RGB {x, x, x},RGB {x, x, x},style
     * Format has to be the same as in NotationPropertyDescriptor.getPropertyValue()
     */
    private Label gradientLabel;
    
    /**
     * Internal class for laying out this cell editor.
     */
    private class GradientCellLayout extends Layout {
        public Point computeSize(Composite editor, int wHint, int hHint,
                boolean force) {
            if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
            Point labelSize = gradientLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT,
                    force);
            return new Point(labelSize.x, labelSize.y);
        }

        public void layout(Composite editor, boolean force) {
            Point gradientSize = gradientLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT,
                    force);
            gradientLabel.setBounds(0, 0, gradientSize.x, gradientSize.y);
        }
    }
    
    /**
     * Creates a new GradientCellEditor with the given control as the parent.
     *
     * @param parent the parent control
     */
    public GradientCellEditor(Composite parent) {
        this(parent, SWT.NONE);
    }

    /**
     * Creates a new GradientCellEditor with the given control as the parent. 
     * 
     * @param parent the parent control
     * @param style the style bits
     */
    public GradientCellEditor(Composite parent, int style) {
        super(parent, style);
        doSetValue(null);
    }    
    
	/** 
	 * Opens GradientSelectionDialog without transparency setting available, receives the
	 * return values and uses them to create the resulting GradientData object. 
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		String value = (String)getValue();
		RGB color1 = null, color2 = null;
		int gradientStyle = -1;
		// value is in format: RGB {x, x, x},RGB {x, x, x},style
		// parse it
        if (value != null) {
			StringTokenizer st = new StringTokenizer(value, ",{}"); //$NON-NLS-1$
			try {
				int red, green, blue;
				st.nextToken().trim(); // RGB string				
				// color1
				red = Integer.parseInt(st.nextToken().trim());
				green = Integer.parseInt(st.nextToken().trim());
				blue = Integer.parseInt(st.nextToken().trim());
				if (red > -1 && green > -1 && blue > -1) {
					color1 = new RGB(red, green, blue);
				}
				st.nextToken().trim(); // RGB string				
				// color2
				red = Integer.parseInt(st.nextToken().trim());
				green = Integer.parseInt(st.nextToken().trim());
				blue = Integer.parseInt(st.nextToken().trim());
				if (red > -1 && green > -1 && blue > -1) {
					color2 = new RGB(red, green, blue);
				}				
				// style
				GradientStyle gradientStyleObj = GradientStyle.get(value.substring(value.lastIndexOf(',')+1).trim());
				if (gradientStyleObj != null) {
					gradientStyle = gradientStyleObj.getValue();
				}
			} finally {
				if (color1 == null || color2 == null || gradientStyle == -1) {
					value = null;
				}				
			}
        }
		if (value == null) {
			// use default
			GradientData def = GradientData.getDefaultGradientData();
			color1 = FigureUtilities.integerToRGB(def.getGradientColor1());
			color2 = FigureUtilities.integerToRGB(def.getGradientColor2());
			gradientStyle = def.getGradientStyle();
		}       
		GradientSelectionDialog dialog = new GradientSelectionDialog(
				cellEditorWindow.getShell(), SWT.APPLICATION_MODAL,
				color1, color2, gradientStyle, 0);
		int result = dialog.open();	
		if (result == SWT.OK) { 
			return new GradientData(
					FigureUtilities.RGBToInteger(dialog.getGradientColor1()), 
					FigureUtilities.RGBToInteger(dialog.getGradientColor2()), 
					dialog.getGradientStyle());
		} else {
			// make the calls here, since parent ignores null, and we need to clear gradient
			markDirty();
            doSetValue(null);
            fireApplyEditorValue();
			return null;
		}
	}
	

	/**
	 * Creates gradientLabel
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite cell) {
		Color bg = cell.getBackground();
		composite = new Composite(cell, getStyle());
		composite.setBackground(bg);
		composite.setLayout(new GradientCellLayout());
		gradientLabel = new Label(composite, SWT.LEFT);
		gradientLabel.setBackground(bg);
		gradientLabel.setFont(cell.getFont());
		return composite;
	}	

	/**
	 * Updates the contents of this cell editor, i.e. the value of gradientLabel with the given value. 
	 * Parameter value is a string if this method is called when user selects the editor.
	 * Parameter value is GradientData if this method is called after user changed gradient 
	 * (after opening GradientSelectionDialog from this cell editor)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#updateContents(java.lang.Object)
	 */
	protected void updateContents(Object value) {
		if (value == null) {
			gradientLabel.setText("");
		} else if (value instanceof String) {
			gradientLabel.setText((String)value);
		} else {
			// format GradientData to string:
			//  RGB {x,x,x),RGB {x,x,x},style
			// (the same as in NotationPropertyDescriptor.getPropertyValue())
			GradientData gradient = (GradientData)value;
			StringBuffer sf = new StringBuffer();
			sf.append(FigureUtilities.integerToRGB(gradient.getGradientColor1()));
			sf.append(',');
			sf.append(FigureUtilities.integerToRGB(gradient.getGradientColor2()));
			sf.append(',');
			sf.append(GradientStyle.get(gradient.getGradientStyle()));
			gradientLabel.setText(sf.toString());
		}
	}	

}
