/***************************************************************************
 Licensed Materials - Property of IBM
 (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.

 US Government Users Restricted Rights - Use, duplication or disclosure
 restricted by GSA ADP Schedule Contract with IBM Corp.
***************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.preferences;

import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.gef.rulers.RulerProvider;
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

import org.eclipse.gmf.runtime.common.ui.preferences.AbstractPreferencePage;
import org.eclipse.gmf.runtime.common.ui.preferences.ComboFieldEditor;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;


/**
 * Presentation Ruler & Grid preference page.
 * 
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class RulerGridPreferencePage
	extends AbstractPreferencePage {

	private class DoubleFieldEditor extends StringFieldEditor {
		
		private double minValidValue = 00.001;
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

	private String RULER_GROUP_LABEL = PresentationResourceManager
	.getI18NString("GriRulerPreferencePage.rulerGroup.label"); //$NON-NLS-1$
	private String SHOW_RULERS_LABEL = PresentationResourceManager
	.getI18NString("GridRulerPreferencePage.showRulers.label"); //$NON-NLS-1$
	private String RULER_UNITS_LABEL = PresentationResourceManager
	.getI18NString("GridRulerPreferencePage.rulerUnits.label"); //$NON-NLS-1$
	private String RULER_UNITS_IN_LABEL = PresentationResourceManager
	.getI18NString("GridRulerPreferencePage.rulerUnits.inch.label"); //$NON-NLS-1$
	private String RULER_UNITS_CM_LABEL = PresentationResourceManager
	.getI18NString("GridRulerPreferencePage.rulerUnits.cm.label"); //$NON-NLS-1$
	private String RULER_UNITS_PIXEL_LABEL = PresentationResourceManager
	.getI18NString("GridRulerPreferencePage.rulerUnits.pixel.label"); //$NON-NLS-1$

	private String GRID_GROUP_LABEL = PresentationResourceManager
	.getI18NString("GridRulerPreferencePage.gridGroup.label"); //$NON-NLS-1$
	private String SHOW_GRID_LABEL = PresentationResourceManager
	.getI18NString("GridRulerPreferencePage.showGrid.label"); //$NON-NLS-1$
	private String SNAP_TO_GRID_LABEL = PresentationResourceManager
	.getI18NString("GridRulerPreferencePage.snapToGrid.label"); //$NON-NLS-1$
	private String GRID_SPACING_LABEL = PresentationResourceManager
	.getI18NString("GridRulerPreferencePage.gridSpacing.label"); //$NON-NLS-1$
	
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
			// Inches
			case 0:
				pixelValue = value.doubleValue() * Display.getDefault().getDPI().x;
				break;
			case 1: // cm
				pixelValue = value.doubleValue() * Display.getDefault().getDPI().x / 2.54;
				break;
			case 2: // pixels
				pixelValue = value.intValue();
		}
		
		double returnValue = 0;
		
		switch( toUnits ) {
			case 0:
				returnValue = pixelValue / Display.getDefault().getDPI().x;
				break;
			case 1:
				returnValue = pixelValue * 2.54 / Display.getDefault().getDPI().x;
				break;
			case 2:
				returnValue = pixelValue;
		}
		
		return numberFormatter.format(returnValue);		
	}

	private void updateUnits() {
		
		int units = rulerUnits.getComboControl().getSelectionIndex();
		
		// IF no selection has been made
		if( units == -1 ) {
			// Read the preference store
			units = getPreferenceStore().getInt(IPreferenceConstants.PREF_RULER_UNITS);
			oldUnits = units;
		}

		switch( units )
		{
			case 0: // Inches
				gridUnits.setText(RULER_UNITS_IN_LABEL);
				break;
				
			case 1: // CM
				gridUnits.setText(RULER_UNITS_CM_LABEL);
				break;

			case 2: // Pixels
				gridUnits.setText(RULER_UNITS_PIXEL_LABEL);
				break;
		}

		gridSpacing.setStringValue( convertUnits( oldUnits, units ) );
		oldUnits = units;
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
