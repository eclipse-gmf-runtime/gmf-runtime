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

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.dialogs.PropertiesDialog;
import org.eclipse.gmf.runtime.common.ui.services.properties.PropertiesService;

/**
 * Cell editor for properties that can be modified via a property page.
 * This cell editor is composed of an elipsis button for editing via
 * a property page, and a text widget for editing directly.
 * 
 * @author ldamus
 */
public class PropertyPageCellEditor extends DialogCellEditor {

	/**
	 * The text widget
	 */
	private Text text;

	/**
	 * My modify listener for modifications to the text
	 */
	private ModifyListener modifyListener;

	/**
	 * Flag which is <code>true</code> if there is a selection in the text
	 * widget, <code>false</code> otherwise. Used to notify that the
	 * copy/paste menu item enablement has changed
	 */
	private boolean isSelection = false;

	/**
	 * Flag which is <code>true</code> if the text is deletable, 
	 * <code>false</code> otherwise. Used to notify that the
	 * delete menu item enablement has changed
	 */
	private boolean isDeleteable = false;

	/**
	 * Flag which is <code>true</code> if the text is selectable, 
	 * <code>false</code> otherwise. Used to notify that the
	 * select all menu item enablement has changed
	 */
	private boolean isSelectable = false;

	/**
	 * My selection provider which provides a selection to the
	 * {@link PropertyDialogAction}
	 */
	ISelectionProvider selectionProvider;

	/**
	 * My property descriptor
	 */
	private final PropertyPagePropertyDescriptor propertyDescriptor;

	/**
	 * The value of this cell editor; initially <code>null</code>.
	 */
	private Object value = null;

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
	 * Creates a text widget in the cell editor
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#createContents(Composite)
	 */
	protected Control createContents(Composite cell) {
		setText(new Text(cell, getStyle()));

		// Add a key listener to the text widget
		text.addKeyListener(new KeyAdapter() {

			// On key pressed, check the menu item enablement
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
				if ((getControl() == null) || getControl().isDisposed())
					return;
				checkSelection();
				checkDeleteable();
				checkSelectable();
			}
		});

		// Add a traverse listener to the text widget
		text.addTraverseListener(new TraverseListener() {

			// On key traversed, disable the escape and return operations
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE
					|| e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});

		// Add a mouse listener to the text widget
		text.addMouseListener(new MouseAdapter() {

			// On mouse up, check the menu item enablement
			public void mouseUp(MouseEvent e) {
				checkSelection();
				checkDeleteable();
				checkSelectable();
			}
		});

		text.setFont(cell.getFont());
		text.setBackground(cell.getBackground());
		text.setText(StringStatics.BLANK);
		text.addModifyListener(getModifyListener());

		return getText();
	}

	/**
	 * Updates the contents of the text field with <code>aValue</code>.
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#updateContents(Object)
	 */
	protected void updateContents(Object aValue) {

		if (getText() == null) {
			return;
		}

		String aText = StringStatics.BLANK;
		if (aValue != null) {
			aText = aValue.toString();
		}
		getText().setText(aText);
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
		return text;
	}

	/**
	 * Sets the text widget
	 * @param text The text widget to set
	 */
	private void setText(Text text) {
		this.text = text;
	}

	/**
	 * Notifies that this cell editor has focus. Put focus on my text widget
	 * and calculate the the menu item enablement.
	 */
	protected void doSetFocus() {
		if (getText() != null) {
			getText().selectAll();
			getText().setFocus();
			checkSelection();
			checkDeleteable();
			checkSelectable();
		}
	}

	/**
	 * Gets the text value in my text widget
	 * 
	 * @return The text in my text widget
	 */
	protected Object doGetValue() {
		String aText = getText().getText();

		// handle value from collection editor dialog
		if (value instanceof ElementValue) {
			((ElementValue) value).setValue(aText);
			return value;
		}

		return aText;
	}

	/**
	 * Sets the text value in my text widget. <code>value</code> must be
	 * a <code>String</code>.
	 *
	 * @param aValue a text string (type <code>String</code>)
	 */
	protected void doSetValue(Object aValue) {
		this.value = aValue;

		assert null != getText();
		getText().removeModifyListener(getModifyListener());
		getText().setText(aValue.toString());
		getText().addModifyListener(getModifyListener());
	}

	/**
	 * Returns my modify listener.
	 * 
	 * @return my modify listener
	 */
	private ModifyListener getModifyListener() {
		if (modifyListener == null) {
			modifyListener = new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					editOccured(e);
				}
			};
		}
		return modifyListener;
	}

	/**
	 * Processes a modify event that occurred in my text widget.
	 * Performs validation and sets the error message
	 * accordingly, and then reports a change using <code>fireEditorValueChanged</code>.
	 *
	 * @param e The modify event
	 */
	protected void editOccured(ModifyEvent e) {

		String aValue = text.getText();
		if (aValue == null) {
			aValue = StringStatics.BLANK;
		}
		Object typedValue = aValue;
		boolean oldValidState = isValueValid();
		boolean newValidState = isCorrect(typedValue);
		if (typedValue == null && newValidState) {
			assert (false) : "Validator isn't limiting the cell editor's type range"; //$NON-NLS-1$
		}
		if (!newValidState) {
			// try to insert the current value into the error message.
			setErrorMessage(
				MessageFormat.format(
					getErrorMessage(),
					new Object[] { aValue }));
		}
		valueChanged(oldValidState, newValidState);
	}

	/**
	 * Checks to see if the "deleteable" state (can delete/
	 * nothing to delete) has changed and if so fire an
	 * enablement changed notification.
	 */
	private void checkDeleteable() {
		boolean oldIsDeleteable = isDeleteable;
		isDeleteable = isDeleteEnabled();
		if (oldIsDeleteable != isDeleteable) {
			fireEnablementChanged(DELETE);
		}
	}

	/**
	 * Checks to see if the "selectable" state (can select)
	 * has changed and if so fire an enablement changed notification.
	 */
	private void checkSelectable() {
		boolean oldIsSelectable = isSelectable;
		isSelectable = isSelectAllEnabled();
		if (oldIsSelectable != isSelectable) {
			fireEnablementChanged(SELECT_ALL);
		}
	}

	/**
	 * Checks to see if the selection state (selection /
	 * no selection) has changed and if so fire an
	 * enablement changed notification.
	 */
	private void checkSelection() {
		boolean oldIsSelection = isSelection;
		isSelection = text.getSelectionCount() > 0;
		if (oldIsSelection != isSelection) {
			fireEnablementChanged(COPY);
			fireEnablementChanged(CUT);
		}
	}

	/**
	 * Determines if there is text to copy.
	 * 
	 * @return <code>true</code> if the text selection is not empty, 
	 * <code>false</code>otherwise
	 */
	public boolean isCopyEnabled() {
		if (text == null || text.isDisposed())
			return false;
		return text.getSelectionCount() > 0;
	}

	/**
	 * Determines if there is text to cut
	 * 
	 * @return <code>true</code> if the text selection is not empty, 
	 * <code>false</code>otherwise
	 */
	public boolean isCutEnabled() {
		if (text == null || text.isDisposed())
			return false;
		return text.getSelectionCount() > 0;
	}

	/**
	 * Determines if there is text to delete
	 * 
	 * @return <code>true</code> if the text selection is not empty, 
	 * <code>false</code>otherwise
	 */
	public boolean isDeleteEnabled() {
		if (text == null || text.isDisposed())
			return false;
		return text.getSelectionCount() > 0
			|| text.getCaretPosition() < text.getCharCount();
	}

	/**
	 * Determines if there is a text widget on which text can be pasted
	 * 
	 * @return <code>true</code> if the text selection is not empty, 
	 * <code>false</code>otherwise
	 */
	public boolean isPasteEnabled() {
		if (text == null || text.isDisposed())
			return false;
		return true;
	}

	/**
	 * Determines if there is a text widget in which text can be saved
	 * 
	 * @return <code>true</code> if the text widget is not disposed, 
	 * <code>false</code>otherwise
	 */
	public boolean isSaveAllEnabled() {
		if (text == null || text.isDisposed())
			return false;
		return true;
	}

	/**
	 * Determines if there is text to be selected.
	 * 
	 * @return <code>true</code> if select all is possible,
	 *  <code>false</code> otherwise
	 */
	public boolean isSelectAllEnabled() {
		if (text == null || text.isDisposed())
			return false;
		return text.getCharCount() > 0;
	}

	/**
	 * Copies the selected text
	 */
	public void performCopy() {
		text.copy();
	}

	/**
	 * Cuts the selected text to the clipboard. 
	 */
	public void performCut() {
		text.cut();
		checkSelection();
		checkDeleteable();
		checkSelectable();
	}

	/**
	 * Deletes the selected text or, if there is no selection,
	 * the character next character from the current position. 
	 */
	public void performDelete() {
		if (text.getSelectionCount() > 0)
			text.insert(StringStatics.BLANK);
		else {
			// remove the next character
			int pos = text.getCaretPosition();
			if (pos < text.getCharCount()) {
				text.setSelection(pos, pos + 1);
				text.insert(StringStatics.BLANK);
			}
		}
		checkSelection();
		checkDeleteable();
		checkSelectable();
	}

	/**
	 * Pastes the the clipboard contents over the selected text. 
	 */
	public void performPaste() {
		text.paste();
		checkSelection();
		checkDeleteable();
		checkSelectable();
	}

	/**
	 * Selects all of the text
	 */
	public void performSelectAll() {
		text.selectAll();
		checkSelection();
		checkDeleteable();
	}
	
	/**
	 * Getter method for value
	 * @return the value of this cell editor
	 */
	protected Object getCellObjectValue() {
		return value;
	}
	
}
