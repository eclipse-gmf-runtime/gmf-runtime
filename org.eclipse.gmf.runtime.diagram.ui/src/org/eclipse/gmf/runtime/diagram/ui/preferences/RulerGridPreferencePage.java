/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.preferences;

import java.text.ParseException;

import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gmf.runtime.common.ui.preferences.AbstractPreferencePage;
import org.eclipse.gmf.runtime.common.ui.preferences.ComboFieldEditor;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.NumberFormat;


/**
 * Diagram Ruler & Grid preference page.
 * 
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class RulerGridPreferencePage
	extends AbstractPreferencePage {

	private class DoubleFieldEditor extends StringFieldEditor {
		
		private double minValidValue = 00.009;
		private double maxValidValue = 99.999;
		
		public DoubleFieldEditor(String pref, String label, Composite parent ) {
			super(pref,label,parent);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.preference.StringFieldEditor#doCheckState()
		 */
		protected boolean doCheckState() {
			Text text = getTextControl();

			if (text == null)
				return false;
			
			try {
				NumberFormat numberFormatter = NumberFormat.getInstance();				
				Double pageHeight = forceDouble(numberFormatter.parse(text.getText()));
				double number = pageHeight.doubleValue();
				number = convertToBase(number);
				if (number >= minValidValue && number <= maxValidValue) {
					clearErrorMessage();
					return true;
				} else {
					showErrorMessage();
					return false;
				}
			} catch (NumberFormatException e1) {
				showErrorMessage();
			} catch (ParseException e2) {
				showErrorMessage();
			}

			return false;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.preference.StringFieldEditor#doLoadDefault()
		 */
		protected void doLoadDefault() {
			Text text = getTextControl();
			if (text != null) {
				double value = getPreferenceStore().getDefaultDouble(getPreferenceName());
				NumberFormat numberFormatter = NumberFormat.getNumberInstance();
				text.setText(numberFormatter.format(value));
			}
			valueChanged();
		}
		
		/* (non-Javadoc)
		 * Method declared on FieldEditor.
		 */
		protected void doLoad() {
			Text text = getTextControl();			
			if (text != null) {
				double value = getPreferenceStore().getDouble(getPreferenceName());
				NumberFormat numberFormatter = NumberFormat.getNumberInstance();
				text.setText(numberFormatter.format(value));				
			}
		}		
		
		protected void doStore() {
			NumberFormat numberFormatter = NumberFormat.getInstance();				
			Double gridWidth;
			try {
				gridWidth = forceDouble(numberFormatter.parse(getTextControl().getText()));
				getPreferenceStore().setValue(getPreferenceName(), gridWidth.doubleValue());				
			} catch (ParseException e) {
				showErrorMessage();
			}
			
		}		
	}

	private int oldUnits = -1;

	private static final int INCHES = 0;
	private static final int CENTIMETERS = 1;
	private static final int PIXELS = 2;

	// Conversion from inch to centimeter
	private static final double INCH2CM = 2.54;
	
	private String RULER_GROUP_LABEL = DiagramUIMessages.GridRulerPreferencePage_rulerGroup_label;
	private String SHOW_RULERS_LABEL = DiagramUIMessages.GridRulerPreferencePage_showRulers_label;
	private String RULER_UNITS_LABEL = DiagramUIMessages.GridRulerPreferencePage_rulerUnits_label;
	private String RULER_UNITS_IN_LABEL = DiagramUIMessages.GridRulerPreferencePage_rulerUnits_inch_label;
	private String RULER_UNITS_CM_LABEL = DiagramUIMessages.GridRulerPreferencePage_rulerUnits_cm_label;
	private String RULER_UNITS_PIXEL_LABEL = DiagramUIMessages.GridRulerPreferencePage_rulerUnits_pixel_label;

	private String GRID_GROUP_LABEL = DiagramUIMessages.GridRulerPreferencePage_gridGroup_label;
	private String SHOW_GRID_LABEL = DiagramUIMessages.GridRulerPreferencePage_showGrid_label;
	private String SNAP_TO_GRID_LABEL = DiagramUIMessages.GridRulerPreferencePage_snapToGrid_label;
	private String GRID_SPACING_LABEL = DiagramUIMessages.GridRulerPreferencePage_gridSpacing_label;
	
	// Ruler Field Editors
	private BooleanFieldEditor showRulers = null;
    private ComboFieldEditor rulerUnits;

    // Grid Field Editors
    private BooleanFieldEditor showGrid = null;
	private BooleanFieldEditor snapToGrid = null;
	private DoubleFieldEditor gridSpacing = null;
	private Label gridUnits = null;

	private String convertUnits(int fromUnits, int toUnits ) {
		String valueStr = gridSpacing.getStringValue();
		if( fromUnits == toUnits ) {
			return valueStr;
		}
		
		//Double value = Double.valueOf( valueStr );
		NumberFormat numberFormatter = NumberFormat.getInstance();		
		Double value = new Double(0.125);
		try {
			value = forceDouble(numberFormatter.parse(valueStr));
		} catch (ParseException e) {
			// Use the default
		}
		double pixelValue = 0;

		switch( fromUnits ) {
			case INCHES:
				pixelValue = value.doubleValue() * Display.getDefault().getDPI().x;
				break;
			case CENTIMETERS:
				pixelValue = value.doubleValue() * Display.getDefault().getDPI().x / INCH2CM;
				break;
			case PIXELS:
				pixelValue = value.intValue();
		}
		
		double returnValue = 0;
		
		switch( toUnits ) {
			case INCHES:
				returnValue = pixelValue / Display.getDefault().getDPI().x;
				break;
			case CENTIMETERS:
				returnValue = pixelValue * INCH2CM / Display.getDefault().getDPI().x;
				break;
			case PIXELS:
				returnValue = pixelValue;
		}
		
		return numberFormatter.format(returnValue);		
	}

	
	/**
	 * 
	 * converts the current units used to a base unit value to be used (e.g. in validation)
	 * 
	 * @param number Units to be converted to the base unit
	 * @return
	 */
	private double convertToBase(double number) {
		
		double returnValue = 0;
		switch( getUnits() ) {
			case INCHES:
				returnValue = number;
				break;
			case CENTIMETERS:
				returnValue = number / INCH2CM;
				break;
			case PIXELS:
				returnValue = number / Display.getDefault().getDPI().x;
		}
		return returnValue;
	}

	private void updateUnits() {
		
		int units = getUnits();

		switch( units )
		{
			case INCHES:
				gridUnits.setText(RULER_UNITS_IN_LABEL);
				break;
				
			case CENTIMETERS:
				gridUnits.setText(RULER_UNITS_CM_LABEL);
				break;

			case PIXELS:
				gridUnits.setText(RULER_UNITS_PIXEL_LABEL);
				break;
		}

		gridSpacing.setStringValue( convertUnits( oldUnits, units ) );
		oldUnits = units;
	}

	private int getUnits() {
		int units = rulerUnits.getComboControl().getSelectionIndex();
		
		// IF no selection has been made
		if( units == -1 ) {
			// Read the preference store
			units = getPreferenceStore().getInt(IPreferenceConstants.PREF_RULER_UNITS);
			oldUnits = units;
		}
		return units;
	}

	private void addRulerFields( Composite parent ) {

		// Create a Group to hold the ruler fields
    	Group group = new Group(parent, SWT.NONE);
		group.setText(RULER_GROUP_LABEL);
 
		GridLayout gridLayout = new GridLayout(2, false);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
        
        // Add the fields to the group
		showRulers = new BooleanFieldEditor(
			IPreferenceConstants.PREF_SHOW_RULERS,
			SHOW_RULERS_LABEL, group);
		addField(showRulers);

    	rulerUnits = new ComboFieldEditor(
    		IPreferenceConstants.PREF_RULER_UNITS,
        	RULER_UNITS_LABEL,
			group,
			ComboFieldEditor.INT_TYPE,
			false,
			0,
			0,
			true);
    	addField(rulerUnits);
    	
    	Combo rulerUnitsCombo;
    	rulerUnitsCombo = rulerUnits.getComboControl();
    	rulerUnitsCombo.add(RULER_UNITS_IN_LABEL);
    	rulerUnitsCombo.add(RULER_UNITS_CM_LABEL);
    	rulerUnitsCombo.add(RULER_UNITS_PIXEL_LABEL);
    	
    	rulerUnitsCombo.addSelectionListener( new SelectionListener() {
    		public void widgetDefaultSelected(SelectionEvent e){
    			//do nothing
    			}
    		public void widgetSelected(SelectionEvent e){
    			updateUnits();
    		}
    	});
    	
		group.setLayoutData(gridData);
		group.setLayout(gridLayout);
	}
	
	private void addGridFields( Composite parent ) {
		
		// Create a Group to hold the grid fields
    	Group group = new Group(parent, SWT.NONE);
		group.setText(GRID_GROUP_LABEL);

		GridLayout gridLayout = new GridLayout(2, false);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;

		showGrid = new BooleanFieldEditor(
			IPreferenceConstants.PREF_SHOW_GRID,
			SHOW_GRID_LABEL, group);
		addField(showGrid);

		snapToGrid = new BooleanFieldEditor(
			IPreferenceConstants.PREF_SNAP_TO_GRID,
			SNAP_TO_GRID_LABEL, group);
		addField(snapToGrid);
		
		addGridSpacing( group );

		group.setLayoutData(gridData);
		group.setLayout(gridLayout);
	}

	private void addGridSpacing( Composite parent ) {
	
		Composite group = new Composite(parent, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout(3, false);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;

		gridSpacing = new DoubleFieldEditor(
			IPreferenceConstants.PREF_GRID_SPACING,
			GRID_SPACING_LABEL, group);
		gridSpacing.setTextLimit(10);
		addField(gridSpacing);
		
		GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalSpan = 1;
		
		gridUnits = new Label(group, SWT.LEFT);
		gridUnits.setLayoutData(gridData2);
		
		updateUnits();
		
		group.setLayoutData(gridData);
		group.setLayout(gridLayout);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.preferences.AbstractPreferencePage#addFields(org.eclipse.swt.widgets.Composite)
	 */
	protected void addFields(Composite parent) {
		addRulerFields( parent );
		addGridFields( parent );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.preferences.AbstractPreferencePage#initHelp()
	 */
	protected void initHelp() {
		// TODO: Implement to support context help
	}

	/**
	 * Initializes the default preference values for the preferences.
	 * 
	 * @param IPreferenceStore preferences
	 */
	public static void initDefaults(IPreferenceStore preferenceStore) {
		preferenceStore.setDefault(IPreferenceConstants.PREF_SHOW_RULERS, false);
		preferenceStore.setDefault(IPreferenceConstants.PREF_RULER_UNITS, RulerProvider.UNIT_INCHES);
		preferenceStore.setDefault(IPreferenceConstants.PREF_SHOW_GRID, false);
		preferenceStore.setDefault(IPreferenceConstants.PREF_SNAP_TO_GRID, true);
		preferenceStore.setDefault(IPreferenceConstants.PREF_GRID_SPACING, 0.125);
	}
	
	/**
	 * The NumberFormatter.parse() could return a Long or Double
	 * We are storing all values related to the page setup as doubles
	 * so we call this function when ever we are getting values from
	 * the dialog.
	 * @param number
	 * @return
	 */
	private Double forceDouble(Number number) {
		if (number instanceof Long)
			return new Double(number.doubleValue());
		return (Double) number;
	}	

}
