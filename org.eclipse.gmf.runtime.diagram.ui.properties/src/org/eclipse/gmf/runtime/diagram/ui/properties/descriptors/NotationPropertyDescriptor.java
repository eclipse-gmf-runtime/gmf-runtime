/******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.descriptors;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.diagram.ui.internal.util.FontHelper;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesMessages;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.emf.ui.properties.descriptors.EMFCompositeSourcePropertyDescriptor;
import org.eclipse.gmf.runtime.emf.ui.properties.descriptors.EObjectContainmentListPropertyValue;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * A property descriptor for notation elements (graphical edit parts, views and
 * styles), wrapper aroubd an EMF property descriptor.
 * 
 * @author nbalaba
 */
public class NotationPropertyDescriptor extends
        EMFCompositeSourcePropertyDescriptor {

    /**
     * Create and instance of the <code>NotationPropertyDescriptor</code>.
     * 
     * @param object -
     *            notation element (graphical edit part, view or style)
     * @param itemPropertyDescriptor -
     *            EMF property descriptor
     * @param category -
     *            property category
     */
    public NotationPropertyDescriptor(Object object,
            IItemPropertyDescriptor itemPropertyDescriptor, String category) {
        super(object, itemPropertyDescriptor, category);

    }

    /**
     * Create a cell editor for a data value.
     * 
     * @param composite - parent composite @return - cell editor to edit
     * property value
     * 
     * @see <code> createPropertyEditor </code>
     */  
    protected CellEditor createDataTypeCellEditor(Composite composite) {

        if (isColor())
            return new ColorCellEditor(composite);
        
        CellEditor cellEditor = super.createDataTypeCellEditor(composite);
        
        if (isFontHeight()) {
        	cellEditor.setValidator(getPositiveIntegerValidator());
        }
        	
        return cellEditor;
    }

    /**
     * Create a cell validator that ensures positive integers
     * @return positive integer cell editor validator
     */
    private ICellEditorValidator getPositiveIntegerValidator() {
		ICellEditorValidator cellValidator = new ICellEditorValidator() {
			public String isValid(Object value) {
				String error = null;
				if (value instanceof String) {
					String strValue = (String) value;
					try {
						if (Integer.parseInt(strValue) <= 0) {
								throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
							error = DiagramUIPropertiesMessages.Positive_Number_Error;
					}
				}
				return error;
			}
		};
		return cellValidator;
    }
    
    private boolean isFontHeight() {
        return getFeature() == NotationPackage.eINSTANCE.getFontStyle_FontHeight();
	}

	/**
     * Create combo box cell editor
     * 
     * @param composite - parent composite @return - cell editor to edit
     * property value
     * 
     * @see <code> createPropertyEditor </code>
     */
    protected CellEditor createComboBoxCellEditor(Composite composite) {

        propertyEditor = new ExtendedComboBoxCellEditor(composite,
                getChoiceOfValues(), getLabelProvider(), false, SWT.NONE);

        propertyEditor.setValidator(getValidator());

        return propertyEditor;

    }

    /**
     * Gets this property value
     * @return - value of the property stored in the EMF descriptor
     */
    public Object getPropertyValue() {

        if (getFeature() == NotationPackage.eINSTANCE.getView_Styles()) {
        	if (getEditableValue() instanceof EObjectContainmentEList) {
                EObjectContainmentEList list = (EObjectContainmentEList) getEditableValue();
                EObjectContainmentListPropertyValue value = new FlattenedContainmentListPropertyValue(
                        list);
                //value.setLabelProvider(getLabelProvider());
                return value;        		
        	} else {
        		return getEditableValue();
        	}
        }

        if (isColor()) {
            Integer color = (Integer) getEditableValue();
            return FigureUtilities.integerToRGB(color);
        }

        return getEditableValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertyDescriptor#getLabelProvider()
     */
    public ILabelProvider getLabelProvider() {

        return new NotationItemLabelProvider(super.getLabelProvider(),
                (EStructuralFeature) getFeature());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.common.ui.internal.properties.emf.EMFCompositeSourcePropertyDescriptor#setPropertyValue(java.lang.Object)
     */
    public void setPropertyValue(Object value) {
        if (value instanceof EObjectContainmentListPropertyValue) {
            super
                    .setPropertyValue(((EObjectContainmentListPropertyValue) value)
                            .getTarget());
            return;
        }

        if (isColor()) {
            RGB rgb = (RGB) value;
            super.setPropertyValue(FigureUtilities.colorToInteger(new Color(
                    Display.getCurrent(), rgb)));
            return;
        }

        super.setPropertyValue(value);
    }

    /*
     * Test if the descriptor describes a color @return - true if the descriptor
     * describes a color, false otherwise
     */
    private boolean isColor() {

        return (getFeature() == NotationPackage.eINSTANCE
                .getFillStyle_FillColor()
                || getFeature() == NotationPackage.eINSTANCE
                        .getLineStyle_LineColor() || getFeature() == NotationPackage.eINSTANCE
                .getFontStyle_FontColor());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.emf.ui.properties.descriptors.EMFCompositeSourcePropertyDescriptor#getChoiceOfValues()
     */
    public List getChoiceOfValues() {
        if (getFeature() == NotationPackage.eINSTANCE.getFontStyle_FontName()) {
            return Arrays.asList(FontHelper.getFontNames());

        }
        return super.getChoiceOfValues();
    }
}