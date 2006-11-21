/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;
import org.eclipse.gmf.runtime.common.ui.util.WindowUtil;

/**
 * The common Show Related Elements Predefined Queries Composite. It contains a
 * list of queries you may select from.
 * 
 * @author Wayne Diu, wdiu
 */
public class ShowRelatedElementsPredefinedComposite
	extends Composite {

	/**
	 * Save As button
	 */
	protected Button saveAs;

	/**
	 * Delete button
	 */
	protected Button delete;

	/**
	 * Details button
	 */
	protected Button details;

	/**
	 * Show Details button
	 */
	protected IShowRelatedElementsWithDetails showDetails;

	/**
	 * Root SelectableElement
	 */
	protected SelectableElement rootElement;

	/**
	 * If the Details is displayed
	 */
	protected boolean isDetails = false;

	/**
	 * Predefined list
	 */
	protected List predefinedBox;

	/**
	 * Data for the predefined queries
	 */
	protected java.util.List predefined = new ArrayList();

	/**
	 * Data for the hardcoded queries
	 */
	protected java.util.List queries;

	/**
	 * Dialog settings for storing presets.
	 */
	protected final IDialogSettings dialogSettings = CommonUIPlugin
		.getDefault().getDialogSettings();

	/**
	 * Tree viewer control's height for the hint
	 */
	public static int VIEWER_HEIGHT = 225;

	/**
	 * Viewer width hint for the listbox viewer
	 */
	protected int viewerWidth = 175;
	
	/**
	 * Max length of preset name
	 */
	private static int PRESET_NAME_LENGTH = 256;

	static {
		try {
			VIEWER_HEIGHT = Integer.parseInt(
				CommonUIMessages.ShowRelatedElementsDialog_VIEWER_HEIGHT);
		} catch (NumberFormatException e) {
			Trace.catching(CommonUIPlugin.getDefault(),
				CommonUIDebugOptions.EXCEPTIONS_CATCHING, CommonUIPlugin
					.getDefault().getClass(), "NumberFormatException", e); //$NON-NLS-1$
			/* already initialized with defaults */
			Log
				.error(
					CommonUIPlugin.getDefault(),
					CommonUIStatusCodes.RESOURCE_FAILURE,
					"Failed to parse Show Related Elements Composite's localized size", e); //$NON-NLS-1$
		}
	}

	/**
	 * Save as dialog. Cached.
	 */
	protected InputDialog saveAsDialog = new InputDialog(
		Display.getDefault().getActiveShell(),
			CommonUIMessages.ShowRelatedElementsPredefinedComposite_SaveAs_Title, 
			CommonUIMessages.ShowRelatedElementsPredefinedComposite_ChooseName, 
			StringStatics.BLANK, new IInputValidator() {  

			public String isValid(String newText) {
				if (containsSpecialCharacter(newText) || newText.length() == 0) {
					return CommonUIMessages.ShowRelatedElementsPredefinedComposite_SpecialCharacter; 
				}
				return null;
			}
		})

	{

		protected Control createDialogArea(Composite parent) {
			Control control = super.createDialogArea(parent);
			Text text = getText();
			assert null != text;
			text.setTextLimit(30);
			text.setTextLimit(PRESET_NAME_LENGTH);
			return control;
		}
	};

	/**
	 * Opening a hardcoded query's display name
	 */
	protected static final String SPECIAL_OPENING = "["; //$NON-NLS-1$

	/**
	 * Closing a hardcoded query's display name
	 */
	protected static final String SPECIAL_CLOSING = "]"; //$NON-NLS-1$

	/**
	 * Dialog settings key
	 */
	protected static final String DIALOG_SETTINGS_KEY = "ShowRelatedElementsPresets"; //$NON-NLS-1$

	/**
	 * Default string
	 */
	protected static final String DEFAULT_STRING = SPECIAL_OPENING
		+ CommonUIMessages.ShowRelatedElementsPredefinedComposite_DefaultQuery + SPECIAL_CLOSING; 

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            the parent Composite that we will add our composite into
	 * @param sreWithDetails
	 *            for sending updated events
	 * @param root
	 *            SelectableElement root
	 * @param queriesList
	 *            list of hardcoded queries
	 * @param width
	 *            int Width of composite to show
	 * @param detailsShown
	 *            boolean true if details are shown, false if not shown
	 */
	public ShowRelatedElementsPredefinedComposite(Composite parent,
			IShowRelatedElementsWithDetails sreWithDetails,
			SelectableElement root, java.util.List queriesList, int width,
			boolean detailsShown) {
		super(parent, SWT.NULL);
		showDetails = sreWithDetails;
		rootElement = root;
		this.queries = queriesList;
		this.viewerWidth = width;
		this.isDetails = detailsShown;

		createContents();
	}

	/**
	 * Set the text of the show or hide details button depending on whether or
	 * not the details are shown.
	 */
	private void setDetailsText() {
		assert null != details;
		if (!isDetails) {
			details
				.setText(CommonUIMessages.ShowRelatedElementsPredefinedComposite_ShowDetails);
		} else {
			details
				.setText(CommonUIMessages.ShowRelatedElementsPredefinedComposite_HideDetails);
		}
	}

	/**
	 * Creates content for this composite. This includes the List of predefined
	 * queries and several.
	 */
	protected void createContents() {

		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.setLayout(new GridLayout(1, false));

		new Label(this, SWT.NULL)
			.setText(CommonUIMessages.ShowRelatedElementsPredefinedComposite_CustomQuery); 

		predefinedBox = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		gd.heightHint = VIEWER_HEIGHT;
		gd.widthHint = viewerWidth;
		predefinedBox.setLayoutData(gd);

		Composite buttons;

		if (showDetails == null) {
			buttons = new Composite(this, SWT.NONE);
			buttons.setLayout(new GridLayout(2, true));
			gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER
				| GridData.VERTICAL_ALIGN_END);
			gd.horizontalSpan = 2;
			buttons.setLayoutData(gd);

			makePredefinedSettingsButtons(buttons);
		} else {
			Composite moreButtons = new Composite(this, SWT.NONE);
			GridLayout gridLayout = new GridLayout(2, false);
			gridLayout.marginWidth = 0;
			moreButtons.setLayout(gridLayout);
			moreButtons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_END));

			buttons = new Composite(moreButtons, SWT.NONE);
			gridLayout = new GridLayout(2, true);
			gridLayout.marginWidth = 0;
			buttons.setLayout(gridLayout);
			buttons.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING));

			makePredefinedSettingsButtons(buttons);

			details = new Button(moreButtons, SWT.PUSH);
			setDetailsText();
			details.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END
				| GridData.GRAB_HORIZONTAL));

			details.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					isDetails = !isDetails;
					saveAs.setEnabled(isDetails);
					showDetails.showOrHideDetails();
					handlePredefinedBoxSelection();
					setDetailsText();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});

			gd = WindowUtil.makeButtonData(details);
			gd.horizontalAlignment = GridData.END;
			gd.grabExcessHorizontalSpace = true;
			details.setLayoutData(gd);

		}

		//delete made by makePredefinedSettingsButtons
		assert null != delete;
		delete.setEnabled(false);

		contributeComposite(this);

		predefinedBox.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handlePredefinedBoxSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

		});

		contributeToList();
		predefinedBox.select(predefinedBox.getItemCount() - 1);

	}

	/**
	 * Handle when the user clicks on something in the predefined queries List.
	 * 
	 * It could involve sending a message to update the details. Also update the
	 * delete button enablement.
	 */
	protected void handlePredefinedBoxSelection() {

		if (predefinedBox.getSelectionIndex() == predefinedBox.getItemCount() - 1) {
			if (showDetails != null)
				showDetails.updateRelationships(null);
			delete.setEnabled(false);
		} else if (predefinedBox.getSelectionIndex() != predefinedBox
			.getItemCount() - 1) {
			String string = predefinedBox.getSelection()[0];
			ShowRelatedElementsPreset preset = ShowRelatedElementsPresetHelper
				.findPresetFromList(predefined, string);
			if (preset == null) {
				preset = ShowRelatedElementsPresetHelper.findPresetFromList(
					queries, string.substring(0, string
						.indexOf(StringStatics.SPACE + DEFAULT_STRING)));
			}
			assert null != preset;
			if (showDetails != null)
				showDetails.updateRelationships(preset);

			//do not allow delete for anything with [ or ]
			//just need to check one of them
			delete.setEnabled(!containsSpecialCharacter(string));
		}
	}

	/**
	 * Contribute to the predefined queries List. Read in data. Add items to the
	 * List.
	 */
	protected void contributeToList() {
		//clear the model
		predefined.clear();

		//clear the ui
		predefinedBox.removeAll();

		java.util.List readPresets = readPresets(null);

		predefined.addAll(readPresets);

		//do not sort

		//add the hardcoded queries
		Iterator it;
		if (queries != null) {
			it = queries.iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				assert (obj instanceof ShowRelatedElementsPreset);
				predefinedBox.add(((ShowRelatedElementsPreset) obj).getName()
					+ StringStatics.SPACE + DEFAULT_STRING);
			}
		}

		//add the user's predefined queries
		it = predefined.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			assert (obj instanceof ShowRelatedElementsPreset);
			predefinedBox.add(((ShowRelatedElementsPreset) obj).getName());

		}

		//do not use messageformat since the ordering must be like this for
		//consistency with the rest
		predefinedBox
			.add(CommonUIMessages.ShowRelatedElementsPredefinedComposite_UserCustomQuery 
				+ StringStatics.SPACE
				+ SPECIAL_OPENING
				+ CommonUIMessages.ShowRelatedElementsPredefinedComposite_DefaultQuery + SPECIAL_CLOSING); 
	}

	/**
	 * Returns if the text string contains a special character.
	 * 
	 * @param text
	 *            string to check for special characters
	 * @return true if it contains a special character, false if it doesn't
	 */
	protected boolean containsSpecialCharacter(String text) {
		return (text.indexOf(ShowRelatedElementsPresetHelper.KEY_SEPARATOR) != -1
			|| text.indexOf(ShowRelatedElementsPresetHelper.VALUE_SEPARATOR) != -1
			|| text.indexOf(SPECIAL_CLOSING) != -1 || text
			.indexOf(SPECIAL_OPENING) != -1);
	}

	/**
	 * Return a list of ShowRelatedElementsPreset objects.
	 * 
	 * @param nameToIgnore
	 *            ignore this name when reading in the presets. Can be null if
	 *            you do not want to ignore anything and read everything in.
	 * @return List of the presets. Items in the List are
	 *         ShowRelatedElementsPreset objects
	 */
	protected java.util.List readPresets(String nameToIgnore) {
		return ShowRelatedElementsPresetHelper.readPresets(nameToIgnore,
			dialogSettings.getArray(DIALOG_SETTINGS_KEY), null);
	}

	/**
	 * Convert the presets to a string array used for serializing
	 * 
	 * @param presets
	 *            List of ShowRelatedElementsPresets objects
	 * @return String array that contains the data of the presets
	 */
	protected String[] convertPresetsToString(java.util.List presets) {
		return ShowRelatedElementsPresetHelper.convertPresetsToString(presets,
			null);
	}

	/**
	 * Handle a save as for a predefined setting.
	 */
	protected void saveAs() {
		assert null != showDetails;

		if (saveAsDialog.open() == Window.OK) {
			//save as
			//name;ids,ids,ids;custom,custom,custom;typeint;int"
			java.util.List presets = readPresets(saveAsDialog.getValue());

			//serialize it back out
			ShowRelatedElementsPreset newPreset = showDetails
				.getCurrentSettings();
			assert null != newPreset;
			newPreset.setName(saveAsDialog.getValue());

			presets.add(newPreset);

			dialogSettings.put(DIALOG_SETTINGS_KEY,
				convertPresetsToString(presets));

			//TODO will be more efficient to just do the delta instead of
			// complete refresh
			contributeToList();
			predefinedBox.select(predefinedBox.getItemCount() - 2);
			delete.setEnabled(true);
		}
	}

	/**
	 * Make the buttons for the predefined settings list.
	 * 
	 * @param buttons
	 *            composite I will be adding the buttons to.
	 */
	protected void makePredefinedSettingsButtons(Composite buttons) {
		saveAs = new Button(buttons, SWT.PUSH);
		saveAs.setText(CommonUIMessages.ShowRelatedElementsPredefinedComposite_SaveAs); 
		saveAs.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				saveAs();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		saveAs.setLayoutData(WindowUtil.makeButtonData(saveAs));

		delete = new Button(buttons, SWT.PUSH);
		delete.setText(CommonUIMessages.ShowRelatedElementsPredefinedComposite_Delete);

		delete.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				delete();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		delete.setLayoutData(WindowUtil.makeButtonData(delete));

	}

	/**
	 * Handle a delete button press to delete the predefined setting.
	 */
	protected void delete() {
		//do not allow deleting anything with [ in the name
		//handled by change selection

		assert (predefinedBox.getSelection().length != 0);

		String selectedText = predefinedBox.getSelection()[0];

		ShowRelatedElementsPreset preset = ShowRelatedElementsPresetHelper
			.findPresetFromList(predefined, selectedText);

		assert (selectedText.indexOf(SPECIAL_OPENING) == -1);

		assert null != preset;

		//more efficient than rereading it and redoing entire ui

		//delete it from the model
		predefined.remove(preset);

		//delete it from the ui
		predefinedBox.remove(predefinedBox.getSelectionIndex());

		dialogSettings.put(DIALOG_SETTINGS_KEY,
			convertPresetsToString(predefined));

		//go back to the default query
		predefinedBox.setSelection(predefinedBox.getItemCount() - 1);
		delete.setEnabled(false);

		showDetails.updateRelationships(null);
	}

	/**
	 * Contribute a composite to the expansion group
	 * 
	 * @param parent
	 *            the parent expansion group composite that we are going to
	 *            contribute to.
	 */
	protected void contributeComposite(Composite parent) {
		//do nothing, subclasses may override
	}

	/**
	 * Details were changed, so update the predefined box to select the first
	 * item.
	 */
	protected void detailsChanged() {
		predefinedBox.select(predefinedBox.getItemCount() - 1);
		//do not showDetails.updateRelationships(null);
		//because the user is in the middle of updating it
	}

}