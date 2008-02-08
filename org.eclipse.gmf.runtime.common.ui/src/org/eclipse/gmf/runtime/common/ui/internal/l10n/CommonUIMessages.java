/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.common.ui.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class CommonUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages";//$NON-NLS-1$

	private CommonUIMessages() {
		// Do not instantiate
	}

	public static String XToolsUIPlugin__ERROR__startupErrorMessage;
	public static String XToolsUIPlugin__ERROR__shutDownErrorMessage;
	public static String ClearAction_label;
	public static String MoveAction_label;
	public static String ExplorerFilterSettingsAction_dialogTitle;
	public static String ExplorerFilterSettingsAction_dialogMessage;
	public static String SelectionComposite_invalidElementSelected;
	public static String SelectionComposite_nothingSelectable;
	public static String ShowRelatedElementsDialog_ExpansionGroup_Text;
	public static String ShowRelatedElementsDialog_ExpansionGroup_RadioButton_Both;
	public static String ShowRelatedElementsDialog_ExpansionGroup_Label_Levels;
	public static String ShowRelatedElementsDialog_ExpansionGroup_CheckBox_Expand_Indefinitely;
	public static String ShowRelatedElementsDialog_Title;
	public static String ShowRelatedElementsDialog_Incoming;
	public static String ShowRelatedElementsDialog_Outgoing;
	public static String ShowRelatedElementsDialog_AllConnected;
	public static String ShowRelatedElementsDialog_MAX_VIEWER_WIDTH;
	public static String ShowRelatedElementsDialog_VIEWER_HEIGHT;
	public static String ShowRelatedElementsDialog_LevelsValidation_Title;
	public static String ShowRelatedElementsDialog_LevelsValidation_Message;
	public static String ShowRelatedElementsDialog_ExpandIndefinitelyMessageBox_Title;
	public static String ShowRelatedElementsDialog_ExpandIndefinitelyMessageBox_Message_Sentence1;
	public static String ShowRelatedElementsDialog_ExpandIndefinitelyMessageBox_Message_Sentence2;
	public static String ShowHideRelationshipsDialog_Button_OK;
	public static String ShowHideRelationshipsDialog_Button_Cancel;
	public static String ShowRelatedElementsDialog_RelationshipTypes;
	public static String ShowRelatedElementsPredefinedComposite_CustomQuery;
	public static String ShowRelatedElementsPredefinedComposite_Delete;
	public static String ShowRelatedElementsPredefinedComposite_SaveAs;
    public static String ShowRelatedElementsPredefinedComposite_SaveAs_Title;
	public static String ShowRelatedElementsPredefinedComposite_UserCustomQuery;
	public static String ShowRelatedElementsPredefinedComposite_ShowDetails;
	public static String ShowRelatedElementsPredefinedComposite_HideDetails;
	public static String ShowRelatedElementsPredefinedComposite_SpecialCharacter;
	//public static String ShowRelatedElementsPredefinedComposite_SaveAs;
	public static String ShowRelatedElementsPredefinedComposite_ChooseName;
	public static String ShowRelatedElementsPredefinedComposite_DefaultQuery;
	public static String ShowHideRelationshipsDialog_Title;
	public static String ShowHideRelationshipsDialog_Description;
	public static String ShowHideRelationshipsDialog_MAX_VIEWER_WIDTH;
	public static String ShowHideRelationshipsDialog_MAX_VIEWER_HEIGHT;
	public static String ShowHideRelationshipsDialog_TEXT_AREA_HEIGHT;
	public static String ShowHideRelationshipsDialog_Label_Legend;
	public static String ShowHideRelationshipsDialog_Label_LegendShow;
	public static String ShowHideRelationshipsDialog_Label_LegendHide;
	public static String ShowHideRelationshipsDialog_Label_LegendLeave;
	public static String TreeInlineTextEditor_errorDialogTitle;
	public static String WorkbenchPartActivator_ErrorMessage;
	public static String GlobalAction_infoDialogMessage;
	public static String UIModificationValidator_ModificationMessage;
	public static String ToggleAutomaticScrollingAction_label;
	public static String ShowAllHiddenCategoriesAction_label;
	public static String HideActiveCategoryAction_label;
	public static String ToggleTabVisibilityAction_HideCategoryTab_label;
	public static String ToggleTabVisibilityAction_ShowCategoryTab_label;
	public static String OutputView_Clear_Tooltip;
	public static String OutputView_ToggleAutomaticScrolling_Tooltip;
	public static String OutputView_HideActiveCategory_Tooltip;
	public static String OutputView_ShowAllHiddenCategories_Tooltip;
	public static String FileModificationValidator_EditProblemDialogTitle;
	public static String FileModificationValidator_EditProblemDialogMessage;
	public static String FileModificationValidator_SaveProblemDialogTitle;
	public static String FileModificationValidator_SaveProblemDialogMessage;
	public static String FileModificationValidator_OK;
	public static String FileModificationValidator_FileIsReadOnlyErrorMessage;
	public static String PopupDialog_title;
	public static String PopupDialog_message;
	public static String FilterWarningDialog_title;
	public static String FilterWarningDialog_modifyOptions;
	public static String FilterWarningDialog_dontShowAgain;
	public static String FilterWarningDialog_filteredOut;
	public static String PropertiesDialog_title;
	public static String ActionAbandonedDialog_title;
	public static String SaveAllDirtyEditorsDialog_title;
	public static String SaveAllDirtyEditorsDialog_message;
	public static String FileModificationValidator_OutOfSyncMessage;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CommonUIMessages.class);
	}
}
