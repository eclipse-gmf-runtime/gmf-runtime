/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * PSWidgetFactory contains static methods to create widgets used in
 * PageSetupConfigBlock and PageSetupSelectionConfigBlock.
 * 
 * @author etworkow
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class PageSetupWidgetFactory {
	
	/**
	 * Create a radio button allowing the user to use inches as a unit of measurement.
	 * 
	 * @param parent Composite hosting the button
	 * @param label String butotn label
	 * @return Button radio button
	 */
	public static Button createRadioButtonInches(Composite parent, String label) {
		final Button button = createRadioButton(parent, label);
		return button;
	}
	
	/**
	 * Create a radio button allowing the user to use millimetres as a unit of measurement.
	 * 
	 * @param parent Composite hosting the button
	 * @param label String butotn label
	 * @return Button radio button
	 */
	public static Button createRadioButtonMillim(Composite parent, String label) {
		final Button button = createRadioButton(parent, label);
		return button;
	}
	
	/**
	 * Create a radio button with specified label.
	 * 
	 * @param parent Composite hosting the button
	 * @param label String butotn label
	 * @return Button radio button
	 */
	public static Button createRadioButton(Composite parent, String label) {
		final Button button = new Button(parent, SWT.RADIO);
		button.setText(label);
		return button;
	}
	
	/**
	 * Create a push button with specified label.
	 * 
	 * @param parent Composite hosting the button
	 * @param label String butotn label
	 * @return Button radio button
	 */
	public static Button createPushButton(Composite parent, String label) {
		final Button button = new Button(parent, SWT.PUSH | SWT.RIGHT);
		button.setText(label);
		return button;
	}
	
	/**
	 * Create a group with specified label.
	 * 
	 * @param parent Composite hosting the group
	 * @param label String butotn label
	 * @return Group created group
	 */
	public static Group createGroup(Composite parent, String label) {
		int numOfColumns = 2;
		Group group = new Group(parent, SWT.NULL);
		group.setText(label);
		
		group.setLayoutData(
			new GridData(
				GridData.FILL_VERTICAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
		group.setLayout(new GridLayout(numOfColumns, true));
		
		GridData sizeGridData = new GridData(GridData.FILL_HORIZONTAL);
		sizeGridData.horizontalSpan = 6;
		group.setLayoutData(sizeGridData);
		
		return group;
	}
	
	/**
	 * Create a group allowing the user to choose page size.
	 * 
	 * @param parent Composite hosting the group
	 * @param label String label for the group
	 * @return Group created group
	 */
	public static Group createGroupPaperSize(Composite parent, String label) {
		Group group = new Group(parent, SWT.NULL);
		group.setText(label);
		
		group.setLayout(new GridLayout(6, true));
		
		GridData paperSizeGridData = new GridData(GridData.FILL_HORIZONTAL);
		paperSizeGridData.horizontalSpan = 6;
		group.setLayoutData(paperSizeGridData);
	
		return group;
	}
	
	/**
	 * Create a group allowing the user to specify margins.
	 * 
	 * @param parent Composite hosting the group
	 * @param label String label for the group
	 * @return Group created group
	 */
	public static Group createGroupMargin(Composite parent, String label) {
		Group group = new Group(parent, SWT.NULL);
		group.setText(label);
		
		group.setLayout(new GridLayout(6, true));
		
		GridData paperSizeGridData = new GridData(GridData.FILL_HORIZONTAL);
		paperSizeGridData.horizontalSpan = 6;
		group.setLayoutData(paperSizeGridData);
		
		return group;
	}
	
	/**
	 * Create a label and add it to specified group.
	 * 
	 * @param group Group gorup hosting the label
	 * @param labelText String represents label for label
	 * @return Label created label
	 */
	public static Label createLabel(Group group, String labelText) {
		Label label = new Label(group, SWT.LEFT);
		label.setText(labelText);
		
		return label;
	}
	
	/**
	 * Create a unit label.
	 * 
	 * @param group Group group hosting the label
	 * @return Label created label
	 */
	public static Label createLabelUnit(Group group) {
		Label label = new Label(group, SWT.LEFT);
		
		GridData gridData = new GridData();
		gridData.widthHint = 40;
	
		label.setLayoutData(gridData);
		
		return label;
	}
	
	/**
	 * Create a combo allowing the user to choose a paper size.
	 * 
	 * @param group Group group that will host the combo size
	 * @return Combo creates combo displaying all supported paper sizes
	 */
	public static Combo createComboSize(Group group) {
		Combo combo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData scGridData =
				new GridData(
					GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);
		scGridData.horizontalSpan = 5;
		scGridData.grabExcessHorizontalSpace = true;
		combo.setLayoutData(scGridData);
		
		String[] names = { 
			PageSetupPageType.LETTER.getName(),
			PageSetupPageType.LEGAL.getName(),
			PageSetupPageType.EXECUTIVE.getName(),
			PageSetupPageType.llX17.getName(),
			PageSetupPageType.A3.getName(),
			PageSetupPageType.A4.getName(),
			PageSetupPageType.B4.getName(),
			PageSetupPageType.B5.getName(),
			PageSetupPageType.USER_DEFINED.getName(),
		};
		
		combo.setItems(names);
		
		return combo;
	}
		
	/**
	 * Create a text field allowing the user to specify margin value.
	 * 
	 * @param group Group group hosting the margin text field
	 * @return Text created text field
	 */
	public static Text createTextMargin(Group group) {
		int MARGIN_WIDTH_HINT = 40;
		
		Text text = new Text(group, SWT.BORDER | SWT.LEFT);
		
		GridData gridData = new GridData();
		gridData.widthHint = MARGIN_WIDTH_HINT;
		
		text.setLayoutData(gridData);
		
		return text;
	}
	
	/**
	 * Create a label that appears after margin text field and displays a label
	 * corresponding to current measurement units, either inches or millimmetres.
	 * 
	 * @param group Group group hosting the label
	 * @return Label created unit label
	 */
	public static Label createLabelUnitMargin(Group group) {
		Label label = new Label(group, SWT.LEFT);
		
		GridData gridData = new GridData();
		gridData.widthHint = 40;
	
		label.setLayoutData(gridData);

		return label;
	}
	
	/**
	 * Create a text field allowing the user to specify page height.
	 * 
	 * @param group
	 * @return Text
	 */
	public static Text createTextHeight(Group group) {
		Text text = new Text(group, SWT.BORDER);
		GridData phGridData = new GridData(
			GridData.FILL_HORIZONTAL
			| GridData.HORIZONTAL_ALIGN_BEGINNING);
		phGridData.widthHint = 40;
		text.setLayoutData(phGridData);
		
		return text;
	}
	
	/**
	 * Create a text field allowing the user to specify page height.
	 * 
	 * @param group Group group hosting the text field
	 * @return Text created text field
	 */
	public static Text createTextWidth(Group group) {
		int MARGIN_WIDTH_HINT = 40;
		
		Text text = new Text(group, SWT.BORDER);
		GridData pwGridData = new GridData(
			GridData.FILL_HORIZONTAL
			| GridData.HORIZONTAL_ALIGN_BEGINNING);
		pwGridData.widthHint = MARGIN_WIDTH_HINT;
		text.setLayoutData(pwGridData);
		
		return text;
	}
}
