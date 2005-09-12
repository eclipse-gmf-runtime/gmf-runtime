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

package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IDiagramPreferenceSupport;
import org.eclipse.gmf.runtime.diagram.ui.internal.pagesetup.ILabels;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;


/**
 * Page Setup Dialog allowing the user to set printing preferences.  It consists
 * of two parts:
 * 
 * 1. PSSelectionConfigurationBlock allowing the user to choose workspace or diagram settings.
 * 2. PSConfigurationBlock allowing the user to configure printing preferences.
 * 
 * @author etworkow
 */
public class PageSetupDialog extends Dialog implements ILabels {

	private static PageSetupConfigBlock fPrinterConfigurationBlock;
	private static PageSetupSelectionConfigBlock fSelectionConfigurationBlock;
	
	private ArrayList fConfigBlocks = new ArrayList();
	
	/**
	 * Creates an instance on PageSetupDialog.
	 * 
	 * @param parentShell parent shell
	 */
	public PageSetupDialog(Shell parentShell) {
		super(parentShell);
		
		IPreferenceStore diagramViewerStore = getDiagramViewerStore();
		IPreferenceStore globalStore = getGlobalPreferencesStore();
		
		fSelectionConfigurationBlock = new PageSetupSelectionConfigBlock(diagramViewerStore, globalStore, this);
		fPrinterConfigurationBlock = new PageSetupConfigBlock(diagramViewerStore, this);
		
		fConfigBlocks.add(fPrinterConfigurationBlock);
		fConfigBlocks.add(fSelectionConfigurationBlock);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
	 */
	protected void cancelPressed() {
		super.cancelPressed();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		//super.createDialogArea(parent);
		
		fSelectionConfigurationBlock.createContents(parent);
		fPrinterConfigurationBlock.createContents(parent);
		
		enableConfigurationBlocks();
		
		getShell().setText(LABEL_TITLE_PAGE_SETUP);
		
		return parent;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		Button useDiagramSettings = (Button) fSelectionConfigurationBlock.getControl(PageSetupControlType.BUTTON_USE_DIAGRAM_SETTINGS);
		
		if ( useDiagramSettings.getSelection()) {
			fPrinterConfigurationBlock.save();
			fSelectionConfigurationBlock.save();
			super.okPressed();
		}
		else {
			fSelectionConfigurationBlock.save();
			super.okPressed();
		}
	}

	/**
	 * Returns a reference to the OK button in Page Setup Dialog.
	 * 
	 * @return Button reference to the OK button
	 */
	public Button getOkButton() {
		return super.getButton(OK);
	}
	
	/**
	 * Returns a reference to configuration block.
	 * 
	 * @return PageSetupConfigBlock configuration block making up Page Setup Dialog
	 */
	protected PageSetupConfigBlock getConfigurationBlock() {
		return fPrinterConfigurationBlock;
	}
	
	/**
	 * Returns a reference to configuration block.
	 * 
	 * @return PageSetupSelectionConfigBlock configuration block making up Page Setup Dialog
	 */
	protected static PageSetupSelectionConfigBlock getSelectionConfigurationBlock() {
		return fSelectionConfigurationBlock;
	}
	
	private void enableConfigurationBlocks() {
		Button bUseWorkspaceSettings = (Button) fSelectionConfigurationBlock.getControl(PageSetupControlType.BUTTON_USE_WORKSPACE_SETTINGS);
		
		if (bUseWorkspaceSettings.getSelection()) {
			fSelectionConfigurationBlock.enableButtonConfigure();
			fPrinterConfigurationBlock.disableAllControls();
		} else {
			fSelectionConfigurationBlock.disableButtonConfigure();
			fPrinterConfigurationBlock.enableAllControls();
		}
	}
	
	private static IDiagramGraphicalViewer getDiagramGraphicalViewer() {
		IWorkbenchPart page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (page instanceof IDiagramWorkbenchPart) {
			return ((IDiagramWorkbenchPart)page).getDiagramGraphicalViewer();
		}
		return null;
	}

	
	/**
	 * Returns a reference to a preference store unique to a diagram viewer.
	 * 
	 * @return IPreferenceStore unique preference store for currently selected
	 *         diagram viewer
	 */
	private static IPreferenceStore getDiagramViewerStore() {
		IPreferenceStore store = null;

		IDiagramGraphicalViewer viewer = getDiagramGraphicalViewer();
		if (viewer instanceof DiagramGraphicalViewer) {
			store = ((DiagramGraphicalViewer) viewer)
				.getWorkspaceViewerPreferenceStore();
		}

		return store;
	}

	/**
	 * Returns a reference to the global preference store for the active
	 * diagram, if no preferences are specified for the current diagram editor
	 * then null is returned.
	 * 
	 * @return IPreferenceStore the preference store for currently selected
	 *         diagram; null, if there are no preferences for this diagram
	 */
	private static IPreferenceStore getGlobalPreferencesStore() {
		IDiagramGraphicalViewer viewer = getDiagramGraphicalViewer();
		if (viewer.getRootEditPart() instanceof IDiagramPreferenceSupport) {
			PreferencesHint preferencesHint = ((IDiagramPreferenceSupport) viewer
				.getRootEditPart()).getPreferencesHint();
			IPreferenceStore store = (IPreferenceStore) preferencesHint
				.getPreferenceStore();

			IPreferenceStore defaultStore = (IPreferenceStore) PreferencesHint.USE_DEFAULTS
				.getPreferenceStore();

			if (!store.equals(defaultStore)) {
				return store;
			}
		}
		return null;
	}
}
