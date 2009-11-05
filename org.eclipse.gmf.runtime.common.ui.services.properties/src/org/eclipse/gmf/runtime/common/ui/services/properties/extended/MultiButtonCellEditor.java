/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
import java.util.ArrayList;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

/**
 * Cell editor that provides for a read-only label representation of the value
 * and multiple buttons at the end. The last button receives the focus. The
 * subclasses have to override the initButtons() method. The implementation of
 * that method should only make calls to the method addButton() to initialize
 * the desired buttons.
 * 
 * @author dmisic
 */
public abstract class MultiButtonCellEditor
	extends CellEditor {

	/**
	 * The cell editor control itself
	 */
	private Composite editor;

	/**
	 * Font used by all controls
	 */
	private Font font;

	/**
	 * The label part of the editor
	 */
	private Control label;

	/**
	 * Array of the editor's buttons
	 */
	private ArrayList buttonList;

	/**
	 * The value of the cell editor; initially null
	 */
	private Object value = null;

	/**
	 * Internal layout manager for multi button cell editors
	 */
	private class MultiButtonCellLayout
		extends Layout {

		/**
		 * @see org.eclipse.swt.widgets.Layout#computeSize(org.eclipse.swt.widgets.Composite,
		 *      int, int, boolean)
		 */
		protected Point computeSize(Composite composite, int wHint, int hHint,
				boolean flushCache) {

			// check the hints
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}

			// calculate size of the buttons area
			int height = 0;
			int sumWidth = 0;
			int count = buttonList.size();
			for (int i = 0; i < count; i++) {
				Point size = ((Button) buttonList.get(i)).computeSize(
					SWT.DEFAULT, SWT.DEFAULT, flushCache);
				sumWidth += size.x;
				height = Math.max(height, size.y);
			}

			// label size
			Point labelSize = label.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				flushCache);

			return new Point(sumWidth, Math.max(labelSize.y, height));
		}

		/**
		 * @see org.eclipse.swt.widgets.Layout#layout(org.eclipse.swt.widgets.Composite,
		 *      boolean)
		 */
		protected void layout(Composite composite, boolean flushCache) {
			Rectangle bounds = editor.getClientArea();
			int count = buttonList.size();
			int sumWidth = 0;
			int[] widthArray = new int[count];
			int start = 0;

			// calculate the aggregate width of the buttons
			for (int i = 0; i < count; i++) {
				Point size = ((Button) buttonList.get(i)).computeSize(
					SWT.DEFAULT, SWT.DEFAULT, flushCache);
				sumWidth += size.x;
				widthArray[i] = size.x;
			}

			// set the size for the label
			if (label != null) {
				label.setBounds(0, 0, bounds.width - sumWidth, bounds.height);
				start = bounds.width - sumWidth;
			}

			// set the size for the buttons
			for (int i = 0; i < count; i++) {
				Button button = (Button) buttonList.get(i);
				button.setBounds(start, 0, widthArray[i], bounds.height);
				start += widthArray[i];
			}
		}
	}

	/**
	 * @param parent
	 *            The parent control
	 */
	public MultiButtonCellEditor(Composite parent) {
		this(parent, SWT.NONE);
	}

	/**
	 * @param parent
	 *            The parent control
	 * @param style
	 *            The style bits
	 */
	public MultiButtonCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @see org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createControl(Composite parent) {
		buttonList = new ArrayList();
		font = parent.getFont();
		Color bg = parent.getBackground();

		// create the cell editor
		editor = new Composite(parent, getStyle());
		editor.setFont(font);
		editor.setBackground(bg);
		editor.setLayout(new MultiButtonCellLayout());

		// create the label
        if (isModifiable()) {
            label = (new Text(editor, SWT.LEFT));
        } else {
            label = (new Label(editor, SWT.LEFT));
        }
		label.setFont(font);
		label.setBackground(bg);
		updateLabel(value);

		// init the buttons (there must be at least one)
		initButtons();
		assert buttonList.size() > 0 : "button list size must > 0"; //$NON-NLS-1$

		setValueValid(true);

		return editor;
	}

    /**
     * Determine if the label in the cell editor is modifiable. The default is a
     * read-only label representation of the value.
     * 
     * @return <code>true</code> if the label is modifiable
     */
    protected boolean isModifiable() {
        return false;
    }
    
	/**
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	protected Object doGetValue() {
		return value;
	}

	/**
	 * This implementations sets focus on the last button
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	protected void doSetFocus() {
		((Button) buttonList.get(buttonList.size() - 1)).setFocus();
	}

	/**
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	protected void doSetValue(Object val) {
		this.value = val;
		updateLabel(val);
	}

	/**
	 * Creates and adds the button to the cell editor
	 * 
	 * @param buttonLabel
	 *            Button label
	 * @param buttonAction
	 *            The action to be executed when the button is invoked
	 */
	protected void addButton(String buttonLabel,
			final IPropertyAction buttonAction) {

		// create button
		Button button = new Button(editor, SWT.DOWN);
		button.setText(buttonLabel);
		button.setFont(font);

		// selection listener
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				Object newValue = buttonAction.execute(editor);
				if (newValue != null) {
					boolean newValidState = isCorrect(newValue);
					if (newValidState) {
						markDirty();
						doSetValue(newValue);
					} else {
						setErrorMessage(MessageFormat.format(getErrorMessage(),
							new Object[] {newValue.toString()}));
					}
					fireApplyEditorValue();
				}
			}
		});

		// key listener
		button.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (e.character == '\u001b') { // Escape char
					fireCancelEditor();
				}
			}
		});
		
        button.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = false;
                    getControl().traverse(SWT.TRAVERSE_TAB_PREVIOUS);
                }

                if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
                    e.doit = false;
                    getControl().traverse(SWT.TRAVERSE_TAB_NEXT);
                }
            }
        });		

		buttonList.add(button);
	}

	/**
	 * Updates the label showing the value. The default implementation converts
	 * the passed object to a string using <code>toString</code> and sets this
	 * as the text of the label widget.
	 * 
	 * @param val
	 *            The new value
	 */
	protected void updateLabel(Object val) {
		if (label == null)
			return;

		String text = ""; //$NON-NLS-1$
		if (val != null) {
			text = val.toString();
		}
        if (label instanceof Label) {
            ((Label)label).setText(text);
        } else if (label instanceof Text) {
            ((Text)label).setText(text);
        }
	}

	/**
	 * The subclasses have to override this method. The implementation should
	 * only make calls to the method addButton() to initialize the desired
	 * buttons. Note: the implementation of the IPropertyAction's execute method
	 * should return the new value for the editor or null if the value has not
	 * changed.
	 */
	protected abstract void initButtons();

	/**
     * Get the label widget.
	 * @return the label widget.
	 */
	protected Label getLabel() {
        return (label != null && label instanceof Label) ? (Label) label
            : null;
	}
	
    /**
     * Get the text widget in the case where the label is modifiable.
     * @return the label widget.
     */
	protected Text getText() {
	    return (label != null && label instanceof Text) ? (Text) label
            : null;
    }
}