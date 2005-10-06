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

package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.ResourceManager;

/**
 * Show related elements dialog box, called from an action.
 * 
 * When OK is clicked, returns Window.OK and result of the dialog is saved.
 * 
 * When Cancel is clicked, returns Window.Cancel and result of dialog is not
 * saved.
 * 
 * This dialog does not include a listbox for models.
 * 
 * @author Wayne Diu, wdiu
 */
public class ShowRelatedElementsDialog
	extends Dialog
	implements IShowRelatedElementsWithDetails {

	/* Controls */

	/**
	 * The root element
	 */
	private SelectableElement rootElement;

	/**
	 * The help context id
	 */
	private String helpContextId;

	/**
	 * ShowRelatedElementsComposite
	 */
	private ShowRelatedElementsComposite showRelatedElementsComposite;

	/**
	 * True if the details pane is shown, false if hidden
	 */
	protected boolean detailsShown = false;

	/**
	 * For storing whether the details pane was shown or hidden
	 */
	IDialogSettings dialogSettings = CommonUIPlugin.getDefault()
		.getDialogSettings();

	/**
	 * Dialog settings key True if the details pane is shown, false if hidden
	 */
	protected static final String DIALOG_SETTINGS_KEY = "ShowRelatedElementsDetails"; //$NON-NLS-1$

	/**
	 * List of queries.
	 */
	protected List queriesList;

	/**
	 * Cached levels of expansion -1 for indefinite. Default 1
	 */
	protected int cachedExpandLevels = 1;

	/**
	 * Cached expansion types. See ExpansionTypes for values. Default both
	 */
	protected int cachedExpansionType = ExpansionType.BOTH.getOrdinal();

	/**
	 * Cached relationship types.
	 */
	protected List cachedRelationshipTypes;

	/**
	 * Predefined composite placeholder.
	 */
	protected Composite placeholderForShowRelatedElementsComposite = null;

	/**
	 * Predefined composite.
	 */
	protected ShowRelatedElementsPredefinedComposite showRelatedElementsPredefinedComposite;

	/**
	 * Composite body that contains the interesting controls.
	 */
	protected Composite body;

	/**
	 * Preferred viewer width, cached
	 */
	protected int preferredViewerWidth = -1;

	/**
	 * Tree viewer control's width for the hint
	 */
	public static int MAX_VIEWER_WIDTH = 360;

	static {
		try {
			MAX_VIEWER_WIDTH = Integer.parseInt(ResourceManager
				.getI18NString("ShowRelatedElementsDialog.MAX_VIEWER_WIDTH")); //$NON-NLS-1$
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
	 * Constructor takes the parent shell and root SelectableElement to add
	 * into the viewer.
	 * 
	 * @param parentShell
	 *            the parent Shell
	 * @param aRootElement
	 *            the root SelectableElement to add into the viewer
	 */
	public ShowRelatedElementsDialog(Shell parentShell,
			SelectableElement aRootElement) {
		this(parentShell, aRootElement, null);
	}

	/**
	 * Constructor takes the parent shell and root SelectableElement to add
	 * into the viewer.  It also sets a list of predefined queries.
	 * 
	 * @param parentShell
	 *            the parent Shell
	 * @param aRootElement
	 *            the root SelectableElement to add into the viewer
	 * @param aQueriesList
	 *            list of hardcoded queries
	 */
	public ShowRelatedElementsDialog(Shell parentShell,
			SelectableElement aRootElement, List aQueriesList) {
		super(parentShell);
		this.rootElement = aRootElement;
		this.queriesList = aQueriesList;

		initDialogSettings();
	}

	/**
	 * Make the predefined composite
	 * 
	 * @param parent
	 *            parent Composite
	 * @param root
	 *            root SelectableElement
	 * @param queries
	 *            saved queries List
	 * @param viewerWidth
	 *            desired width of listbox viewer
	 * @return the predefined queries composite
	 */
	protected ShowRelatedElementsPredefinedComposite makePredefinedComposite(
			Composite parent, SelectableElement root, List queries,
			int viewerWidth) {
		return new ShowRelatedElementsPredefinedComposite(parent, this, root,
			queries, viewerWidth, detailsShown);
	}

	/**
	 * Cache the preferred viewer width after figuring it out from the longest
	 * string length of the root element.
	 * 
	 * @return int, preferred viewer width
	 */
	protected int getPreferredViewerWidth() {
		if (preferredViewerWidth == -1) {
			//viewerWidth is based on the right side's control
			preferredViewerWidth = SelectableElement
				.calculateLongestStringLength(rootElement, getShell()) + 96;
			if (preferredViewerWidth > MAX_VIEWER_WIDTH)
				preferredViewerWidth = MAX_VIEWER_WIDTH;
		}

		return preferredViewerWidth;
	}

	/**
	 * Suggested that you override makeShowRelatedElementsComposite instead,
	 * unless you have a good reason for overriding the plcaeholder composite
	 * too.
	 * 
	 * @param parent
	 *            the parent Composite that we will be adding the placeholder
	 *            into
	 * @param root
	 *            root SelectableElement
	 * @return the composite that we made inside a placeholder
	 */
	protected ShowRelatedElementsComposite makeShowRelatedElementsCompositeInPlaceholder(
			Composite parent, SelectableElement root) {
		if (placeholderForShowRelatedElementsComposite == null) {
			placeholderForShowRelatedElementsComposite = new Composite(parent,
				SWT.NULL);
			GridLayout gridLayout = new GridLayout(1, false);
			gridLayout.marginHeight = 0;
			gridLayout.marginWidth = 0;
			placeholderForShowRelatedElementsComposite.setLayout(gridLayout);
		}

		if (detailsShown) {
			ShowRelatedElementsComposite sreComposite = makeShowRelatedElementsComposite(
				placeholderForShowRelatedElementsComposite, root,
				getPreferredViewerWidth());
			sreComposite.setDetailsChangedListener(this);
			placeholderForShowRelatedElementsComposite
				.setLayoutData(new GridData(GridData.FILL_VERTICAL
					| GridData.VERTICAL_ALIGN_BEGINNING));
			return sreComposite;
		} else {
			placeholderForShowRelatedElementsComposite
				.setLayoutData(new GridData(0, 0));
			return null;

		}

	}

	/**
	 * Make the show related elements composite
	 * 
	 * @param parent
	 *            parent Composite we will be adding into.
	 * @param root
	 *            root SelectableElement
	 * @param aViewerWidth
	 *            int hint for viewer width
	 * @return the composite that we made
	 */
	protected ShowRelatedElementsComposite makeShowRelatedElementsComposite(
			Composite parent, SelectableElement root, int aViewerWidth) {
		return new ShowRelatedElementsComposite(parent, root, aViewerWidth);
	}

	/**
	 * Make the composites in the dialog
	 * 
	 * @param parent
	 *            the parent Composite
	 * @return the parent Composite
	 */
	protected Control createDialogArea(Composite parent) {
		body = new Composite(parent, SWT.NONE);
		body.setLayout(new GridLayout(2, false));

		showRelatedElementsPredefinedComposite = makePredefinedComposite(body,
			rootElement, queriesList, getPreferredViewerWidth());

		showRelatedElementsComposite = makeShowRelatedElementsCompositeInPlaceholder(
			body, rootElement);

		assert (!detailsShown || (detailsShown && showRelatedElementsComposite != null));

		if (showRelatedElementsComposite != null) {
			this.showRelatedElementsComposite.getTreeViewer().refresh();
		}

		getShell().setText(
			ResourceManager.getI18NString("ShowRelatedElementsDialog.Title")); //$NON-NLS-1$

		//set context sensitive help
		if (helpContextId != null)
			PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, helpContextId);

		return parent;
	}

	/**
	 * Create contents and give focus to the OK button
	 * 
	 * @param parent
	 *            the parent Composite
	 * @return Control created from superclass
	 */
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);

		getButton(IDialogConstants.OK_ID).setFocus();

		return control;
	}

	/**
	 * OK button click handler.
	 * 
	 * Dialog is not closed if validation fails. Otherwise, the dialog closes.
	 */
	protected void okPressed() {
		//do not continue if levels box is not ok
		if (getShowRelatedElementsComposite() != null) {
			if (getShowRelatedElementsComposite().validate(true) != null)
				return;
			getShowRelatedElementsComposite().saveCachedValues();
			saveCachedValues();
		}

		setReturnCode(Window.OK);
		close();
	}

	/**
	 * Cancel button click handler
	 * 
	 * Dialog is not closed if validation fails. Otherwise, the dialog closes.
	 */
	protected void cancelPressed() {
		//dialog settings should be saved even when cancel pressed
		setReturnCode(Window.CANCEL);
		close();
	}

	/**
	 * Returns if the expand indefinitely box was checked, even if it is
	 * disposed.
	 * 
	 * @return true if checked, false if not checked
	 */
	public boolean getExpandIndefinitely() {
		return (cachedExpandLevels == -1);
	}

	/**
	 * Returns the int value in the expand levels box, even if it is disposed
	 * and even if the user did not enter an int.
	 * 
	 * @return int with the number of expand levels. 1 is returned if the user
	 *         did not enter an int and this was not caught earlier.
	 */
	public int getExpandLevels() {
		return cachedExpandLevels;
	}

	/**
	 * Returns if the selection to consumer radio or both radio was selected
	 * This will be deprecated when all SRE implementors implement the all
	 * connected option. Use getExpansionType() instead.
	 * 
	 * @return true if selected, false if not selected
	 */
	public boolean getConsumerToSelection() { //useOutgoingRelationships
		return (cachedExpansionType == ExpansionType.INCOMING.getOrdinal() || cachedExpansionType == ExpansionType.BOTH
			.getOrdinal());
	}

	/**
	 * Returns if the selection to supplier radio or both radio was selected
	 * This will be deprecated when all SRE implementors implement the all
	 * connected option. Use getExpansionType() instead.
	 * 
	 * @return true if selected, false if not selected
	 */
	public boolean getSelectionToSupplier() { //useIncomingRelationships
		return (cachedExpansionType == ExpansionType.OUTGOING.getOrdinal() || cachedExpansionType == ExpansionType.BOTH
			.getOrdinal());
	}

	/**
	 * Returns the expansion type
	 * 
	 * @return true if selected, false if not selected
	 */
	public ExpansionType getExpansionType() {
		return ExpansionType.VALUES[cachedExpansionType];
	}

	/**
	 * Returns list of selected RelationshipTypes.
	 * 
	 * @return List of relationship types that were from SelectableElement
	 *         objects under the root that had a SelectedType of SELECTED.
	 */
	public List getSelectedRelationshipTypes() {
		return (cachedRelationshipTypes != null) ? cachedRelationshipTypes
			: getRootElement().getSelectedElementTypes();
	}

	/**
	 * Returns the root element of the RelatedElements that were in the viewer
	 * control at the left of the dialog.
	 * 
	 * @return SelectableElement with the root element.
	 */
	public SelectableElement getRootElement() {
		return rootElement;
	}

	/**
	 * Method getShowRelatedElementsComposite.
	 * 
	 * @return the ShowRelatedElementsComposite contained in the dialog
	 */
	public ShowRelatedElementsComposite getShowRelatedElementsComposite() {
		return this.showRelatedElementsComposite;
	}

	/**
	 * Initialize the context sensitive help id.
	 * 
	 * @param helpId
	 *            the help context id string
	 */
	public void initHelpContextId(String helpId) {
		this.helpContextId = helpId;
	}

	/**
	 * Save the cached values of the composite in our own cache.
	 */
	protected void saveCachedValues() {
		showRelatedElementsComposite.saveCachedValues();
		if (getShowRelatedElementsComposite().getExpandIndefinitely()) {
			cachedExpandLevels = -1;
		} else {
			cachedExpandLevels = getShowRelatedElementsComposite()
				.getExpandLevel();
		}

		cachedExpansionType = getShowRelatedElementsComposite()
			.getExpansionType().getOrdinal();

		cachedRelationshipTypes = getShowRelatedElementsComposite()
			.getSelectedRelationshipTypes();
	}

	/**
	 * Toggle showing or hiding details.
	 */
	public void showOrHideDetails() {
		if (detailsShown) {
			detailsShown = false;
			saveCachedValues();

			//don't just hide it
			showRelatedElementsComposite.dispose();

			showRelatedElementsComposite = makeShowRelatedElementsCompositeInPlaceholder(
				body, rootElement);

		} else {
			detailsShown = true;

			showRelatedElementsComposite = makeShowRelatedElementsCompositeInPlaceholder(
				body, rootElement);
		}

		placeholderForShowRelatedElementsComposite.pack();

		placeholderForShowRelatedElementsComposite.getParent().pack();
		getShell().pack();

	}

	/**
	 * Update relationshpips according to the preset
	 * 
	 * @param preset
	 *            ShowRelatedElementsPreset containing new relationhips.
	 *  
	 */
	public void updateRelationships(ShowRelatedElementsPreset preset) {
		if (detailsShown) {
			showRelatedElementsComposite.updateRelationships(preset);
		} else {
			//do not change settings if the preset is null,
			//because that happens when there is a custom setting
			if (preset == null)
				return;

			//just cache it
			cachedExpandLevels = preset.getLevels();
			cachedExpansionType = preset.getExpansionType();

			cachedRelationshipTypes = new ArrayList();
			rootElement.getHintsThatMatchTheseIds(cachedRelationshipTypes,
				preset.getIds());
		}

	}

	/**
	 * Return the current preset settings
	 * 
	 * @return custom ShowRelatedElementsPreset settings
	 */
	public ShowRelatedElementsPreset getCurrentSettings() {
		if (detailsShown) {
			return showRelatedElementsComposite.getCurrentSettings();
		}
		return null;
	}

	/**
	 * Notify the composite the details were changed
	 */
	public void detailsChanged() {
		if (showRelatedElementsPredefinedComposite != null) {
			showRelatedElementsPredefinedComposite.detailsChanged();
		}

	}

	/**
	 * Return the predefined composite.
	 * 
	 * @return the ShowRelatedElementsPredefinedComposite
	 */
	protected ShowRelatedElementsPredefinedComposite getPredefinedComposite() {
		return showRelatedElementsPredefinedComposite;
	}

	/**
	 * Read in dialog settings and update based on that.
	 * 
	 * For now, only take care of the detailsShown instance variable for
	 * deciding whether or not we should show the details pane.
	 *  
	 */
	protected void initDialogSettings() {
		detailsShown = dialogSettings.getBoolean(DIALOG_SETTINGS_KEY);
	}

	/**
	 * Store the dialog settings from our current dialog.
	 * 
	 * For now, only take care of the detailsShown instance variable for
	 * deciding whether or not we should show the details pane.
	 *  
	 */
	protected void saveDialogSettings() {
		dialogSettings.put(DIALOG_SETTINGS_KEY, detailsShown);
	}

	/**
	 * Save settings before close
	 * 
	 * @return boolean, superclass result
	 */
	public boolean close() {
		saveDialogSettings();
		return super.close();
	}
}