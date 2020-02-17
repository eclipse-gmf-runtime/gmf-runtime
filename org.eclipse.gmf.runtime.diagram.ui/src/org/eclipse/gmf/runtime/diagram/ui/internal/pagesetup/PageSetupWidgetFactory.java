/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
		GridData groupGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_HORIZONTAL);
		button.setLayoutData(groupGridData);
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
		Group group = new Group(parent, SWT.NULL);
		group.setText(label);
		GridLayout layout = new GridLayout(2, true);
		group.setLayout(layout);
				
		GridData groupGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_HORIZONTAL);
		groupGridData.horizontalSpan = 2;
		group.setLayoutData(groupGridData);
		
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
		
		group.setLayout(new GridLayout(5, true));
		
		GridData paperSizeGridData = new GridData(GridData.FILL_HORIZONTAL);
		paperSizeGridData.horizontalSpan = 5;
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
		
		group.setLayout(new GridLayout(5, true));
		
		GridData paperSizeGridData = new GridData(GridData.FILL_HORIZONTAL);
		paperSizeGridData.horizontalSpan = 5;
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
        
        GridData gridData = new GridData(GridData.GRAB_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
    
        label.setLayoutData(gridData);
		
		return label;
	}
	
	/**
	 * Create a filler label to fill space between fields.
	 * 
	 * @param group Group group hosting the label
	 * @return Label created label
	 */
	public static Label createLabelFiller(Group group) {
		Label label = new Label(group, SWT.LEFT);
		
		GridData gridData = new GridData(GridData.GRAB_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
	
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
		scGridData.horizontalSpan = 4;
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
		int MARGIN_HEIGHT_HINT = 40;
		
		Text text = new Text(group, SWT.BORDER);
		GridData phGridData = new GridData(
			GridData.FILL_HORIZONTAL
			| GridData.HORIZONTAL_ALIGN_BEGINNING);
		phGridData.widthHint = MARGIN_HEIGHT_HINT;
		text.setLayoutData(phGridData);
				
		return text;
	}
	
	/**
	 * Create a text field allowing the user to specify page height.
	 * 
	 * @param group
	 * @return Text
	 */
	public static Text createTextHeight(Group group) {
		int MARGIN_HEIGHT_HINT = 40;
		
		Text text = new Text(group, SWT.BORDER);
		GridData phGridData = new GridData(
			GridData.FILL_HORIZONTAL
			| GridData.HORIZONTAL_ALIGN_BEGINNING);
		phGridData.widthHint = MARGIN_HEIGHT_HINT;
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
