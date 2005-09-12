/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * A property descriptor for a property that is edited via a property page.
 * <P>
 * Implements IAdaptable. When the cell editor for this descriptor is asked
 * to show the property page, the dialog action is initialized with
 * an instance of this object as its selection. The property page dialog and
 * property pages expect to be able to talk to this object as all of the
 * following types:
 * <P>
 * <UL>
 * <LI>IPropertySource</LI>
 * </UL>
 * 
 * 
 * @author ldamus
 */
abstract public class PropertyPagePropertyDescriptor
	extends ExtendedPropertyDescriptor
	implements IAdaptable {

	/**
	 * My cell editor which is null until it is created
	 */
	private CellEditor propertyEditor;

	/**
	 * Creates a property page property descriptor with the given id,
	 * and display name.
	 * 
	 * @param id the id of the property
	 * @param displayName the name to display for the property
	 */
	public PropertyPagePropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	/**
	 * Creates and returns property pages for the property descriptor
	 * @return List the list of property pages
	 */
	abstract public List createPropertyPages();

	/**
	 * The <code>PropertyPagePropertyDescriptor</code> implementation of this 
	 * <code>IPropertyDescriptor</code> method creates and returns a new
	 * <code>PropertyPageCellEditor</code>.
	 * <p>
	 * The editor is configured with the current validator if there is one.
	 * </p>
	 */
	public CellEditor createPropertyEditor(Composite parent) {
		if (isReadOnly())
			return null;

		CellEditor editor = createCellEditor(parent);

		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		setPropertyEditor(editor);
		return editor;
	}

	/**
	 * Creates a cell editor for the property descriptor
	 * @param parent the parent composite
	 * @return CellEditor the cell editor
	 */
	protected CellEditor createCellEditor(Composite parent) {
		return new PropertyPageCellEditor(parent, this);
	}

	/**
	 * Gets my property editor, or null if it hasn't been created
	 * @return my property editor
	 */
	private CellEditor getPropertyEditor() {
		return propertyEditor;
	}

	/**
	 * Sets my property editor
	 * @param editor The property editor
	 */
	private void setPropertyEditor(CellEditor editor) {
		propertyEditor = editor;
	}

	/**
	 * Adapts this propertyDescriptor to its IPropertySource or
	 * IActionFilter, IPropertyDescriptor or
	 * CellEditor source.
	 * 
	 * @param adapter The adapter class
	 * @return the adapted object, or null if I don't adapt to
	 *          <code>adapter</code>
	 */
	public Object getAdapter(Class adapter) {

		if (IPropertySource.class.equals(adapter)) {
			return getPropertySource();

		} else if (IPropertyDescriptor.class.equals(adapter)) {
			return this;

		} else if (CellEditor.class.equals(adapter)) {
			return getPropertyEditor();
		}

		return null;
	}

	/**
	 * Determines if <code>value</code> is the same value as is already on
	 * the property source.
	 * 
	 * @param value to be verified
	 * @return <code>true</code> if the value is the same value as on the property source, <code>false</code> otherwise
	 */
	protected boolean isSameValue(Object value) {
		return getPropertySource().getPropertyValue(getId()).equals(value);
	}
}
