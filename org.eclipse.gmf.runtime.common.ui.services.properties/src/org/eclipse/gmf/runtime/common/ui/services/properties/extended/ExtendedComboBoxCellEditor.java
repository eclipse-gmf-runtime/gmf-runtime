/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Tauseef A. Israr Created on: Sep 6, 2002
 * 
 * A cell editor that presents a list of items in a combo box. The cell editor's
 * value is an integer zero-based index
 *  
 */

public class ExtendedComboBoxCellEditor extends CellEditor {

    /**
     * The list of items to present in the combo box.
     */
    final private String[] items;


    /**
     * The zero-based index of the selected item.
     */
    private int selection;

    /**
     * The custom combo box control.
     */
    private CCombo comboBox;

    /**
     * This keeps track of the list of model objects.
     */
    protected List list;
    
    /** The combo box item which maps to an empty string in the text box */
    final private String emptyItem;
    
    /**
     * Create a list of <code>String</code> items.
     * 
     * @param list
     * @param labelProvider
     * @return list of <code>String</code> items
     */
    public static String[] createItems(List list, ILabelProvider labelProvider) {
        String[] result;

        // If there are objects to populate...
        //
        if (list != null && list.size() > 0) {
            // Create an new array..
            //
            result = new String[list.size()];

            // Fill in the array with label/value pair items.
            //
            int i = 0;
            for (Iterator objects = list.iterator(); objects.hasNext(); ++i) {
                Object object = objects.next();
                result[i] = labelProvider.getText(object);
            }

        } else {
            result = new String[] { labelProvider.getText(null) };
        }

        return result;
    }

    /**
     * Convenience constructor for ExtendedComboBoxCellEditor
     * 
     * @param parent
     *            the parent control
     * @param list
     *            the list of strings for the combo box
     * @param labelProvider -
     *            label rpovider to create an array of strings from the list
     * @param style
     *            the style
     */

    public ExtendedComboBoxCellEditor(Composite parent, List list,
            ILabelProvider labelProvider, int style) {
        this(parent, createItems(list, labelProvider), null, style);
        this.list = list;
    }



    /**
     * Creates a new cell editor with a combo containing the given list of
     * choices and parented under the given control.
     * 
     * @param parent
     *            the parent control
     * @param items
     *            the list of strings for the combo box
     * @param style
     *            the style
     */
    public ExtendedComboBoxCellEditor(Composite parent, String[] items, int style) {
        this(parent, items, null, style);
       
    }
    
    /**
     * Creates a new cell editor with a combo containing the given list of
     * choices and parented under the given control.
     * 
     * @param parent
     *            the parent control
     * @param items
     *            the list of strings for the combo box
     * @param emptyItem
     *            the combo box item which maps to an empty string in the text
     *            box
     * @param style
     *            the style
     */
    public ExtendedComboBoxCellEditor(Composite parent, String[] items,
            String emptyItem, int style) {
        super(parent, style);
        assert null != items : "list of items cannot be null"; //$NON-NLS-1$
        this.items = items;   
        this.emptyItem = emptyItem;
        selection = 0;
        populateComboBoxItems();
    }

    /**
     * Applies the currently selected value and deactiavates the cell editor
     */
    void applyEditorValueAndDeactivate() {
    	//	must set the selection before getting value
    	selection = comboBox.getSelectionIndex();
    	Object newValue = doGetValue();
    	markDirty();
    	setValueValid(isCorrect(newValue));
    	if (!isValueValid()) {

    		// try to insert the current value into the error message.
    		setErrorMessage(
    			MessageFormat.format(getErrorMessage(), new Object[] {newValue})); 
    	}
    	fireApplyEditorValue();
    	deactivate();
    }
    
    /**
     * @see org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets.Composite)
     */
    protected Control createControl(Composite parent) {

        comboBox = new CCombo(parent, getStyle());
        comboBox.setFont(parent.getFont());
        comboBox.setBackground(parent.getBackground());

        comboBox.addKeyListener(new KeyAdapter() {

            // hook key pressed - see PR 14201
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);
            }
        });

        comboBox.addSelectionListener(new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent event) {
                // called when combo box or text field selected
                applyEditorValueAndDeactivate();
            }

            public void widgetSelected(SelectionEvent e) {
                // called when combo box selected
                widgetDefaultSelected(e);
            }
        });

         comboBox.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE
                        || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });

        if(emptyItem != null)
        	comboBox.setText(emptyItem);
        
        return comboBox;
    }



    /**
     * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
     */
    protected void doSetFocus() {
        comboBox.setFocus();
    }

    /**
     * Sets the minimum width of the cell to 30 pixels to make sure the arrow
     * button is visible even when the list contains long strings.
     */
    public LayoutData getLayoutData() {
        LayoutData layoutData = super.getLayoutData();
        layoutData.minimumWidth = Math.max(30, layoutData.minimumWidth);
        return layoutData;
    }

    public Object doGetValue() {
    	
    	    if (list != null && selection >= 0) {
    	        // Get the index into the list via this call to super.

    	        return selection < list.size() && selection >= 0 ? list.get(selection) : null;
    	    }
    	
    	    if (getStyle() == SWT.READ_ONLY && selection >= 0) 
                return new Integer(selection);
    	    
            if(selection < 0)
                return comboBox.getText();
            
            return comboBox.getItem(selection);
    	}


    public void doSetValue(Object value) {

        if (list != null) {
            // Set the index of the object value in the list via this call to
            // super.
            //
            int index = list.indexOf(value);
            if (index != -1) {
                doSetValue1(new Integer(index));
            }
        } else
            doSetValue1(value);
    }

    /**
     * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
     */
    private void doSetValue1(Object value) {
        assert null != comboBox : "comboBox cannot be null"; //$NON-NLS-1$
        if (value instanceof Integer) {
            selection = ((Integer) value).intValue();
            comboBox.select(selection);
        }
        if (getStyle() != SWT.READ_ONLY) {
            comboBox.setText(value.toString());
        }
    }

    /**
     * Add the items to the combo box.
     */
    private void populateComboBoxItems() {
        if (comboBox != null && items != null) {
            for (int i = 0; i < items.length; i++)
                comboBox.add(items[i], i);

            setValueValid(true);
        }
    }
}