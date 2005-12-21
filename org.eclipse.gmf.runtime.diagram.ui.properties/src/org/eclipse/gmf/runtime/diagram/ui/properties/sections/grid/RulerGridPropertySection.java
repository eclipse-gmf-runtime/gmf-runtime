package org.eclipse.gmf.runtime.diagram.ui.properties.sections.grid;

import java.text.NumberFormat;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesImages;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesMessages;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance.ColorPalettePopup;
import org.eclipse.gmf.runtime.diagram.ui.properties.views.TextChangeHelper;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.common.ui.properties.internal.provisional.AbstractPropertySection;
import org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertyConstants;
import org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetPage;


public class RulerGridPropertySection
	extends AbstractPropertySection {

	// Groups
	private Group displayGroup;
	private Group measurementGroup;
	private Group gridlineGroup;
	
	// Buttons
	private Button gridVisibilityButton;
	private Button gridOrderButton;
	private Button snapToGridButton;
	private Button restoreButton;

	private Button rulerVisibilityButton;

	private Button lineColorButton;	
	
	// Labels
	private static final String GRID_ON_LABEL = DiagramUIPropertiesMessages.Grid_On_Label_Text;
	private static final String GRID_LEVEL_LABEL = DiagramUIPropertiesMessages.Grid_Level_Label_Text;
	private static final String SNAP_TO_GRID_LABEL = DiagramUIPropertiesMessages.Snap_To_Grid_Label_Text;
	private static final String RULER_ON_LABEL = DiagramUIPropertiesMessages.Ruler_On_Label_Text;
	private static final String RULER_UNITS_LABEL = DiagramUIPropertiesMessages.Ruler_Units_Label_Text;
	private static final String GRID_SPACING_LABEL = DiagramUIPropertiesMessages.Grid_Spacing_Label_Text;
	private static final String VISIBILITY_LABEL = DiagramUIPropertiesMessages.Display_Group_Label_Text;
	private static final String MEASUREMENT_LABEL = DiagramUIPropertiesMessages.Measurement_Group_Label_Text;
	private static final String GRIDLINE_LABEL = DiagramUIPropertiesMessages.Gridline_Group_Label_Text;
	private static final String LINE_COLOR_LABEL = DiagramUIPropertiesMessages.Line_Color_Label_Text;
	private static final String LINE_STYLE_LABEL = DiagramUIPropertiesMessages.Line_Style_Label_Text;
	private static final String RESTORE_LABEL = DiagramUIPropertiesMessages.Restore_Defaults_Label_Text;

	// Unit labels
	private static final String INCHES_LABEL = DiagramUIPropertiesMessages.Inches_Label_Text;
	private static final String CENTIMETERS_LABEL = DiagramUIPropertiesMessages.Centimeters_Label_Text;
	private static final String PIXEL_LABEL = DiagramUIPropertiesMessages.Pixel_Label_Text;
	
	// Line Style labels
	private static final String  SOLID_LABEL = DiagramUIPropertiesMessages.Solid_Label_Text;
	private static final String  DASH_LABEL = DiagramUIPropertiesMessages.Dash_Label_Text;
	private static final String  DOT_LABEL = DiagramUIPropertiesMessages.Dot_Label_Text;
	private static final String  DASH_DOT_LABEL = DiagramUIPropertiesMessages.Dash_Dot_Label_Text;
	private static final String  DASH_DOT_DOT_LABEL = DiagramUIPropertiesMessages.Dash_Dot_Dot_Label_Text;
	
	// Ruler unit drop down
	private CCombo rulerUnitCombo;

	// Line style drop down
	private CCombo lineStyleCombo;
	
	// Text widget to display and set value of the property
	private Text textWidget;

	private RGB lineColor = null;	

	// For changing ruler units
	private static final int INCHES = 0;
	private static final int CENTIMETERS = 1;
	private static final int PIXELS = 2;

	// Conversion from inch to centimeter
	private static final double INCH2CM = 2.54;
	
	// Valid grid spacing range
	private double minValidValue = 00.009;
	private double maxValidValue = 99.999;
	
	// Listener for workspace property changes
	private PropertyStoreListener propertyListener = new PropertyStoreListener();
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		initializeControls(parent);
	}

	/**
	 * 
	 * Sets up controls with proper layouts and groups
	 * @param parent
	 */
	private void initializeControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		// Top row composite
		Composite topComposite = getWidgetFactory().createComposite(parent);
		topComposite.setLayout(new GridLayout(2, false));
		
		// Create the groups for this section
		createDisplayGroup(topComposite);
		createMeasurementGroup(topComposite);
		
		// Bottom row composite
		Composite bottomComposite = getWidgetFactory().createComposite(parent);
		bottomComposite.setLayout(new GridLayout(2, false));

		// Create grid line settings
		createGridlineGroup(bottomComposite);
		
		Composite extraComposite = getWidgetFactory().createComposite(bottomComposite);
		extraComposite.setLayout(new GridLayout(1, false));

		// Create snap to grid checkbox
		snapToGridButton = getWidgetFactory().createButton(
			extraComposite, SNAP_TO_GRID_LABEL, SWT.CHECK);
		snapToGridButton.addSelectionListener(new SelectionAdapter() {
	
			public void widgetSelected(SelectionEvent event) {
				// Set the snap to grid workspace property			
				setWorkspaceProperty(WorkspaceViewerProperties.SNAPTOGRID, snapToGridButton.getSelection());
			}
		});
		
		// Create restore to preferences defaults
		restoreButton = getWidgetFactory().createButton(
			extraComposite, RESTORE_LABEL, SWT.PUSH);
		restoreButton.addSelectionListener(new SelectionAdapter() {
	
			public void widgetSelected(SelectionEvent event) {
				restorePreferenceValues();
			}

			private static final int LIGHT_GRAY_RGB = 12632256;
			
			private void restorePreferenceValues() {
				IPreferenceStore preferenceStore =
					(IPreferenceStore) ((DiagramEditor) getPart()).getDiagramEditPart().getDiagramPreferencesHint().getPreferenceStore();
				
				IPreferenceStore wsPrefStore = getWorkspaceViewerProperties();
				
				if (wsPrefStore.getBoolean(WorkspaceViewerProperties.GRIDORDER) == false) {
					wsPrefStore.setValue(WorkspaceViewerProperties.GRIDORDER, true);			
				} 
				if (wsPrefStore.getInt(WorkspaceViewerProperties.GRIDLINECOLOR) != LIGHT_GRAY_RGB) {
					wsPrefStore.setValue(WorkspaceViewerProperties.GRIDLINECOLOR, LIGHT_GRAY_RGB);			
				} 
				if (wsPrefStore.getInt(WorkspaceViewerProperties.GRIDLINESTYLE) != SWT.LINE_DOT) {
					wsPrefStore.setValue(WorkspaceViewerProperties.GRIDLINESTYLE, SWT.LINE_DOT);			
				}
				if (wsPrefStore.getBoolean(WorkspaceViewerProperties.VIEWRULERS) != preferenceStore.getBoolean(IPreferenceConstants.PREF_SHOW_RULERS)) {
					wsPrefStore.setValue(WorkspaceViewerProperties.VIEWRULERS, preferenceStore.getBoolean(IPreferenceConstants.PREF_SHOW_RULERS));
				}
				if (wsPrefStore.getBoolean(WorkspaceViewerProperties.VIEWGRID) != preferenceStore.getBoolean(IPreferenceConstants.PREF_SHOW_GRID)) {
					wsPrefStore.setValue(WorkspaceViewerProperties.VIEWGRID, preferenceStore.getBoolean(IPreferenceConstants.PREF_SHOW_GRID));
				}
				
				if ((wsPrefStore.getInt(WorkspaceViewerProperties.RULERUNIT) != preferenceStore.getInt(IPreferenceConstants.PREF_RULER_UNITS)) || 
						(wsPrefStore.getDouble(WorkspaceViewerProperties.GRIDSPACING) != preferenceStore.getDouble(IPreferenceConstants.PREF_GRID_SPACING))) {
					wsPrefStore.setValue(WorkspaceViewerProperties.RULERUNIT, preferenceStore.getInt(IPreferenceConstants.PREF_RULER_UNITS));						
					wsPrefStore.setValue(WorkspaceViewerProperties.GRIDSPACING, preferenceStore.getDouble(IPreferenceConstants.PREF_GRID_SPACING));			
				}
				
				// reset the input values
				setInput(getPart(),null);
			}
		});		
	}

	private void createLineColorControl(Composite composite) {
		
		lineColorButton = new Button(composite, SWT.PUSH);
		lineColorButton.setImage(DiagramUIPropertiesImages.get(DiagramUIPropertiesImages.IMG_LINE_COLOR));

		lineColorButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				changeLineColor(event);
			}

			/**
			 * Change line color property value
			 */
			private void changeLineColor(SelectionEvent event) {
				lineColor = changeColor(event, lineColorButton,null, DiagramUIPropertiesImages.DESC_LINE_COLOR);
				if (lineColor != null) 
					setWorkspaceProperty(WorkspaceViewerProperties.GRIDLINECOLOR, FigureUtilities.RGBToInteger(lineColor).intValue());
			}			
		});

		FormData data = new FormData();
		data.left = new FormAttachment(0,80);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		lineColorButton.setLayoutData(data);
		lineColorButton.setEnabled(true);
		
		createLabelWidget(composite, LINE_COLOR_LABEL, lineColorButton);
	}

	private void createLineStyleControl(Composite composite) {
		lineStyleCombo = getWidgetFactory().createCCombo(composite,
			SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		lineStyleCombo.setItems(getStyles());
		lineStyleCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				updateLineStyle();
			}

			private void updateLineStyle() {
				int style = lineStyleCombo.getSelectionIndex();
				setWorkspaceProperty(WorkspaceViewerProperties.GRIDLINESTYLE, style + SWT.LINE_SOLID);
			}
		});
		
		FormData data = new FormData();
		data.left = new FormAttachment(0,80);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		lineStyleCombo.setLayoutData(data);
		
		createLabelWidget(composite, LINE_STYLE_LABEL, lineStyleCombo);	
	}	
	
	/**
	 * @param event -
	 *            selection event
	 * @param button -
	 *            event source
	 * @param preferenceId -
	 *            id of the preference of the default color value for that property
	 * @param imageDescriptor -
	 *            the image to draw overlay on the button after the new
	 *            color is set
	 * @return - new RGB color, or null if none selected
	 */
	private RGB changeColor(SelectionEvent event, Button button,
			String preferenceId, ImageDescriptor imageDescriptor) {

		ColorPalettePopup popup = new ColorPalettePopup(button.getParent()
			.getShell(), preferenceId, IDialogConstants.BUTTON_BAR_HEIGHT);

		Rectangle r = button.getBounds();
		Point location = button.getParent().toDisplay(r.x, r.y);
		popup.open(new Point(location.x, location.y + r.height));
		return popup.getSelectedColor();

	}	
	
	/**
	 * Creates group with ruler units and grid spacing controls
	 * @param composite
	 */
	private void createMeasurementGroup(Composite composite) {

		measurementGroup = getWidgetFactory().createGroup(composite, MEASUREMENT_LABEL);		
		measurementGroup.setLayout(new GridLayout(2, true));
				
		// Create ruler unit combo
		getWidgetFactory().createLabel(measurementGroup, RULER_UNITS_LABEL);
		
		rulerUnitCombo = getWidgetFactory().createCCombo(measurementGroup,
			SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		rulerUnitCombo.setItems(getUnits());
		rulerUnitCombo.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent event) {
				int oldUnits = getWorkspacePropertyInt(WorkspaceViewerProperties.RULERUNIT);
				int newUnits = rulerUnitCombo.getSelectionIndex();
				
				// Order of the changes is important so that there is no  
				// interim point with a 1 pixel grid spacing
				if (oldUnits < newUnits) {
					updateSpacing(oldUnits,newUnits);
					updateRulerUnits();
				} else {
					updateRulerUnits();
					updateSpacing(oldUnits,newUnits);
				}
			}

			private void updateSpacing(int fromUnits, int toUnits) {				
				String currentUnits = convertUnits(fromUnits, toUnits);
				//textWidget.setText(currentUnits);
				setWorkspaceProperty(WorkspaceViewerProperties.GRIDSPACING, new Double(currentUnits).doubleValue());
			}

			private void updateRulerUnits() {
				int units = getCurrentRulerUnit();
				setWorkspaceProperty(WorkspaceViewerProperties.RULERUNIT, units);
			}
		});


		// Create grid spacing text field
		getWidgetFactory().createLabel(measurementGroup, GRID_SPACING_LABEL);
		textWidget = getWidgetFactory().createText(measurementGroup, StringStatics.BLANK, SWT.BORDER);
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,false);
		textWidget.setLayoutData(data);
		startTextWidgetEventListener();		
		
	
	}
	
	/**
	 * 
	 * converts fromUnits to toUnits (e.g. inches to pixels)
	 * 
	 * @param fromUnits
	 * @param toUnits
	 * @return equivalent number of toUnits for the given fromUnits
	 */
	private String convertUnits(int fromUnits, int toUnits ) {
		
		String valueStr = textWidget.getText();
		if( fromUnits == toUnits ) {
			return valueStr;
		}
		
		Double value = new Double(valueStr);
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
				returnValue = Math.round(pixelValue);
		}
		NumberFormat numberFormatter = NumberFormat.getInstance();
		return numberFormatter.format(returnValue);		
		
	}

	/**
	 * A helper to listen for events that indicate that a text field has been
	 * changed.
	 */
	private TextChangeHelper textListener = new TextChangeHelper() {
		boolean textModified = false;
		/**
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.KeyDown :
					textModified = true;
					if (event.character == SWT.CR)
						textChanged((Control)event.widget);
					break;
				case SWT.FocusOut :
					textChanged((Control)event.widget);
					break;
			}
		}
		
		public void textChanged(Control control) {
			if (textModified) {
				String currentText = ((Text) control).getText();
				try {
					
					double value = new Double(currentText).doubleValue();
					double pixels = convertToBase(value);
					if (pixels >= minValidValue && pixels <= maxValidValue) {
						setWorkspaceProperty(WorkspaceViewerProperties.GRIDSPACING, new Double(currentText).doubleValue());
					} else {
						resetGridSpacing();
					}
					
				} catch (NumberFormatException e) {
					resetGridSpacing();
				}
				textModified = false;
			}
		}

		private void resetGridSpacing() {
			// Set grid spacing back to original value
			textWidget.setText(getWorkspaceProperty(WorkspaceViewerProperties.GRIDSPACING));
			textWidget.selectAll();
		}		
	};
	
	/**
	 * 
	 * converts the current units used to a base unit value to be used (e.g. in validation)
	 * 
	 * @param number Units to be converted to the base unit
	 * @return
	 */
	private double convertToBase(double number) {
		
		double returnValue = 0;
		switch( getCurrentRulerUnit() ) {
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
	
	
	private int getCurrentRulerUnit() {		
		return rulerUnitCombo.getSelectionIndex();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#setInput(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		
		// Set up workspace property listener 
		initWorkspacePropertyListener();
		
		textWidget.setText(getWorkspaceProperty(WorkspaceViewerProperties.GRIDSPACING));
		rulerVisibilityButton.setSelection(getBooleanWorkspaceProperty(WorkspaceViewerProperties.VIEWRULERS));
		gridVisibilityButton.setSelection(getBooleanWorkspaceProperty(WorkspaceViewerProperties.VIEWGRID));
		gridOrderButton.setSelection(getBooleanWorkspaceProperty(WorkspaceViewerProperties.GRIDORDER));
		snapToGridButton.setSelection(getBooleanWorkspaceProperty(WorkspaceViewerProperties.SNAPTOGRID));
		
		int rulerValue = getValue(WorkspaceViewerProperties.RULERUNIT);
		int styleValue = getValue(WorkspaceViewerProperties.GRIDLINESTYLE)-1;
		rulerUnitCombo.setText(getUnits()[rulerValue]);
		lineStyleCombo.setText(getStyles()[styleValue]);
		
	}

	/**
	 * @param property
	 * @return the integer value of the string property
	 */
	private int getValue(String property) {
		int value;
		String valueString = getWorkspaceProperty(property);

		if (valueString.equals(StringStatics.BLANK)) {
			value = 0;
		} else {
			value = new Integer(getWorkspaceProperty(property)).intValue();
		}
		return value;
	}

	private String[] getUnits() {		
		return new String[]{INCHES_LABEL,CENTIMETERS_LABEL,PIXEL_LABEL};
	}

	private String[] getStyles() {		
		return new String[]{SOLID_LABEL,DASH_LABEL,DOT_LABEL,DASH_DOT_LABEL,DASH_DOT_DOT_LABEL};
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#dispose()
	 */
	public void dispose() {
		stopTextWidgetEventListener();
		removeWorkspacePropertyListener();
		super.dispose();
	}	

	/**
	 * Start listening to the text widget events
	 */
	private void startTextWidgetEventListener() {
		getListener().startListeningTo(getTextWidget());
		getListener().startListeningForEnter(getTextWidget());
	}

	/**
	 * Stop listening to text widget events
	 */
	private void stopTextWidgetEventListener() {
		getListener().stopListeningTo(getTextWidget());
	}
	
	/**
	 * @return Returns the textWidget.
	 */
	private Text getTextWidget() {
		return textWidget;
	}
		
	/**
	 * Create a label for property name
	 * 
	 * @param parent -
	 *            parent composite
	 * @return - label to show property name
	 */
	private CLabel createLabelWidget(Composite parent, String labelText, Control control) {
		CLabel label = getWidgetFactory().createCLabel(parent, labelText);
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(control,
			-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(control, 0, SWT.CENTER);
		label.setLayoutData(data);
		return label;
	}
	
	/**
	 * @return Returns the listener.
	 */
	private TextChangeHelper getListener() {
		return textListener;
	}
	
	/**
	 * Creates group with ruler and grid visibility and grid order controls
	 * @param composite
	 */
	private void createDisplayGroup(Composite composite) {	

		displayGroup = getWidgetFactory().createGroup(composite, VISIBILITY_LABEL);		
		displayGroup.setLayout(new GridLayout(1, true));
		
		rulerVisibilityButton = getWidgetFactory().createButton(
			displayGroup, RULER_ON_LABEL, SWT.CHECK);
		rulerVisibilityButton.addSelectionListener(new SelectionAdapter() {
	
			public void widgetSelected(SelectionEvent event) {
				// Set ruler visibility workspace property
				setWorkspaceProperty(WorkspaceViewerProperties.VIEWRULERS, rulerVisibilityButton.getSelection());
			}
		});

		
		gridVisibilityButton = getWidgetFactory().createButton(
			displayGroup, GRID_ON_LABEL, SWT.CHECK);
		gridVisibilityButton.addSelectionListener(new SelectionAdapter() {
	
			public void widgetSelected(SelectionEvent event) {
				// Set grid visibility workspace property
				setWorkspaceProperty(WorkspaceViewerProperties.VIEWGRID, gridVisibilityButton.getSelection());
			}
		});
		
		gridOrderButton = getWidgetFactory().createButton(
			displayGroup, GRID_LEVEL_LABEL, SWT.CHECK);
		gridOrderButton.addSelectionListener(new SelectionAdapter() {
	
			public void widgetSelected(SelectionEvent event) {
				// Set grid level workspace property				
				setWorkspaceProperty(WorkspaceViewerProperties.GRIDORDER, gridOrderButton.getSelection());
			}
		});
				
	}

	/**
	 * Creates group with line color and style controls
	 * @param composite
	 */
	private void createGridlineGroup(Composite composite) {	

		gridlineGroup = getWidgetFactory().createGroup(composite, GRIDLINE_LABEL);		
		gridlineGroup.setLayout(new GridLayout(1, true));

		Composite sectionComposite3 = getWidgetFactory().createFlatFormComposite(gridlineGroup);
		createLineColorControl(sectionComposite3);

		Composite sectionComposite4 = getWidgetFactory().createFlatFormComposite(gridlineGroup);
		createLineStyleControl(sectionComposite4);
				
	}
	
	
	private void setWorkspaceProperty(String property, boolean setting) {
		getWorkspaceViewerProperties().setValue(property, setting);
	}
	
	private void setWorkspaceProperty(String property, int setting) {
		getWorkspaceViewerProperties().setValue(property, setting);
	}
	
	private void setWorkspaceProperty(String property, double setting) {
		getWorkspaceViewerProperties().setValue(property, setting);
	}

	private String getWorkspaceProperty(String property) {
		return getWorkspaceViewerProperties().getString(property);
	}

	private int getWorkspacePropertyInt(String property) {
		return getWorkspaceViewerProperties().getInt(property);
	}
	
	private boolean getBooleanWorkspaceProperty(String property) {
		return getWorkspaceViewerProperties().getBoolean(property);
	}

	private IPreferenceStore getWorkspaceViewerProperties() {
		DiagramEditor editor = (DiagramEditor) getPart();
		DiagramGraphicalViewer viewer = (DiagramGraphicalViewer) editor.getDiagramGraphicalViewer();
		return viewer.getWorkspaceViewerPreferenceStore();
	}

	
	/**
	 * Listener for the workspace preference store.
	 */
	private class PropertyStoreListener implements IPropertyChangeListener {
		
		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
		 */
		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			handleWorkspacePropertyChanged(event);			
		}
	}
	
	/**
	 * Handles workspace preoperty changes
	 * @param event
	 */
	private void handleWorkspacePropertyChanged(PropertyChangeEvent event) {
		if (WorkspaceViewerProperties.VIEWGRID.equals(event.getProperty())) {		
			if (! gridVisibilityButton.isDisposed()) {
				gridVisibilityButton.setSelection(getEventBoolean(event));
			}
		} else if (WorkspaceViewerProperties.VIEWRULERS.equals(event.getProperty())) {			
			if (! rulerVisibilityButton.isDisposed()) {
				rulerVisibilityButton.setSelection(getEventBoolean(event));
			}
		} else if (WorkspaceViewerProperties.SNAPTOGRID.equals(event.getProperty())) {			
			if (! snapToGridButton.isDisposed()) {
				snapToGridButton.setSelection(getEventBoolean(event));
			}
		} else if (WorkspaceViewerProperties.GRIDORDER.equals(event.getProperty())) {
			if (! gridOrderButton.isDisposed()) {
				gridOrderButton.setSelection(getEventBoolean(event));
			}
		} else if (WorkspaceViewerProperties.GRIDSPACING.equals(event.getProperty())) {
			if (! textWidget.isDisposed()) {
				textWidget.setText(getEventString(event));
			}
		} else if (WorkspaceViewerProperties.RULERUNIT.equals(event.getProperty())) { 			
			if (! rulerUnitCombo.isDisposed()) {
				rulerUnitCombo.select(Integer.parseInt(getEventString(event)));
			}
		} else if (WorkspaceViewerProperties.GRIDLINESTYLE.equals(event.getProperty())) {
			if (! lineStyleCombo.isDisposed()) {
				lineStyleCombo.select(Integer.parseInt(getEventString(event))-1);
			}
		}		
	}
	
	private boolean getEventBoolean(PropertyChangeEvent event) {
		Boolean newValue = (Boolean) event.getNewValue();
		return newValue.booleanValue();
	}

	private String getEventString(PropertyChangeEvent event) {
		return event.getNewValue().toString();
	}

	
	/**
	 * Initializes the preferenceStore property change
	 * listener.
	 */
	private void initWorkspacePropertyListener() {
		getWorkspaceViewerProperties().addPropertyChangeListener(propertyListener);
	}
	
	/**
	 * This method removes all listeners to the notational world (views, figures, editpart...etc)
	 * Override this method to remove notational listeners down the hierarchy
	 */
	private void removeWorkspacePropertyListener() {
		getWorkspaceViewerProperties().removePropertyChangeListener(propertyListener);
		propertyListener = null;
	}

}
