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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.dialogs.PropertiesDialog;
import org.eclipse.gmf.runtime.common.ui.services.properties.PropertiesService;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * Cell editor for properties that can be modified via a property page.
 * This cell editor is composed of an elipsis button for editing via
 * a property page.
 * 
 * @author ldamus
 */
public class PropertyPageCellEditor extends MultiButtonCellEditor {

    /**
	 * My property descriptor
	 */
	private final PropertyPagePropertyDescriptor propertyDescriptor;

	/**
	 * Creates a new property page cell editor parented under the given control.
	 * The cell editor value is <code>null</code> initially, and has no 
	 * validator.
	 *
	 * @param parent The parent control
	 * @param descriptor The property descriptor for this cell
	 */
	public PropertyPageCellEditor(
		Composite parent,
		PropertyPagePropertyDescriptor descriptor) {
		this(parent, descriptor, SWT.NONE);
	}

	/**
	 * Creates a new property page cell editor parented under the given control.
	 * The cell editor value is <code>null</code> initially, and has no 
	 * validator.
	 *
	 * @param parent The parent control
	 * @param descriptor The property descriptor for this cell
	 * @param style the style bits
	 */
	public PropertyPageCellEditor(
		Composite parent,
		PropertyPagePropertyDescriptor descriptor,
		int style) {
		super(parent, style);
		propertyDescriptor = descriptor;
	}

	/**
	 * Gets the property descriptor for this cell editor.
	 * 
	 * @return the property descriptor
	 */
	private PropertyPagePropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	/**
	 * Opens the {@link org.eclipse.gmf.runtime.common.ui.dialogs.PropertiesDialog}. Always
	 * returns null. The UI is updated by the model event when the property
	 * is modified by the property dialog.
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {

		// Invoke the property dialog
		PropertiesDialog dialog =
			new PropertiesDialog(
				getControl().getShell(),
				new PreferenceManager());

		// handle invokation of cell editor from collection editor
		PropertyPagePropertyDescriptor realDescriptor = null;
		if (getValue() instanceof ElementValue) {
			Object element = ((ElementValue) getValue()).getElement();
			if (element instanceof PropertyPagePropertyDescriptor) {
				realDescriptor = (PropertyPagePropertyDescriptor) element;
			}
		}

		List pages = null;
		if (realDescriptor != null) {
			pages = realDescriptor.createPropertyPages();
		} else {
			pages = getPropertyDescriptor().createPropertyPages();
		}

		for (Iterator i = pages.iterator(); i.hasNext();) {
			PropertyPage page = (PropertyPage) i.next();

			// handle invokation of cell editor from collection editor
			if (realDescriptor != null) {
				final IPropertySource source =
					realDescriptor.getPropertySource();
				page.setElement(new IAdaptable() {
					public Object getAdapter(Class adapter) {
						if (adapter.equals(IPropertySource.class)) {
							return source;
						}
						return null;
					}
				});
			}

			dialog.getPreferenceManager().addToRoot(
				new PreferenceNode(StringStatics.BLANK, page));
		}

		dialog.create();
		dialog.open();

		// refresh property for collection editor
		for (Iterator i = pages.iterator(); i.hasNext();) {
			PropertyPage page = (PropertyPage) i.next();
			IAdaptable adaptable = page.getElement();
			if (adaptable != null) {
				IPropertySource source =
					(IPropertySource) adaptable.getAdapter(
						IPropertySource.class);
				if (source instanceof IExtendedPropertySource) {
					Object element =
						((IExtendedPropertySource) source).getElement();

					IPropertySource propertySource =
						PropertiesService.getInstance().getPropertySource(
						
							element);
					assert null != propertySource;

					for (Iterator j =
						Arrays
							.asList(propertySource.getPropertyDescriptors())
							.iterator();
						j.hasNext();
						) {
						IPropertyDescriptor descriptor =
							(IPropertyDescriptor) j.next();
						if (descriptor
							.getId()
							.equals(getPropertyDescriptor().getId())) {
							// apply new value in cell editor
							setValue(
								new ElementValue(
									source,
									propertySource.getPropertyValue(
										descriptor.getId())));
							fireApplyEditorValue();
							break;
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Returns the text widget
	 * @return the text widget
     */
	protected Text getText() {
		return null;
	}

	/**
	 * Determines if there is text to copy.
	 * 
	 * @return <code>true</code> if the text selection is not empty, 
	 * <code>false</code>otherwise
     */
	public boolean isCopyEnabled() {
		return false;
	}

	/**
	 * Determines if there is text to cut
	 * 
	 * @return <code>true</code> if the text selection is not empty, 
	 * <code>false</code>otherwise
     */
	public boolean isCutEnabled() {
		return false;
	}

	/**
	 * Determines if there is text to delete
	 * 
	 * @return <code>true</code> if the text selection is not empty, 
	 * <code>false</code>otherwise
     */
	public boolean isDeleteEnabled() {
		return false;
	}

	/**
	 * Determines if there is a text widget on which text can be pasted
	 * 
	 * @return <code>true</code> if the text selection is not empty, 
	 * <code>false</code>otherwise
     */
	public boolean isPasteEnabled() {
		return false;
	}

	/**
	 * Determines if there is a text widget in which text can be saved
	 * 
	 * @return <code>true</code> if the text widget is not disposed, 
	 * <code>false</code>otherwise
     */
	public boolean isSaveAllEnabled() {
        return false;
	}

	/**
	 * Determines if there is text to be selected.
	 * 
	 * @return <code>true</code> if select all is possible,
	 *  <code>false</code> otherwise
     */
	public boolean isSelectAllEnabled() {
        return false;
	}

	/**
	 * Copies the selected text
     */
	public void performCopy() {
        // default implementation do nothing
	}

	/**
	 * Cuts the selected text to the clipboard. 
     */
	public void performCut() {
        /* not suppoerted */
	}

	/**
	 * Deletes the selected text or, if there is no selection,
	 * the character next character from the current position. 
     */
	public void performDelete() {
        /* not suppoerted */
	}

	/**
	 * Pastes the the clipboard contents over the selected text. 
     */
	public void performPaste() {
        /* not suppoerted */
	}

	/**
	 * Selects all of the text
     */
	public void performSelectAll() {
        /* not suppoerted */
	}
	
	/**
	 * Getter method for value
	 * @return the value of this cell editor
     */
	protected Object getCellObjectValue() {
		return super.doGetValue();
	}

    /**
     * @see org.eclipse.gmf.runtime.common.ui.services.properties.extended.MultiButtonCellEditor#initButtons()
     */
    protected void initButtons() {
        // 'set' button
        IPropertyAction setAction = new IPropertyAction() {

            public Object execute(Control owner) {
                return openDialogBox(owner);
            }
        };
        addButton("...", setAction); //$NON-NLS-1$
    }

}
