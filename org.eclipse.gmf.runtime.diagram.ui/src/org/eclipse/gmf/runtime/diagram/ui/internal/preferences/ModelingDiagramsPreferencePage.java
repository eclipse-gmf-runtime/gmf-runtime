/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.preferences.DiagramsPreferencePage;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;

/**
 * The diagrams preference page used for the Modeling preferences.
 * 
 * TODO: Move to Modeler / Visualizer common plugin.
 * 
 * @author cmahoney
 */
public class ModelingDiagramsPreferencePage
	extends DiagramsPreferencePage {

	private String DELETING_ELEMS_FROM_DS = DiagramResourceManager
		.getI18NString("DiagramsPreferencePage.DeletingElementsFromDiagramSurfaceGroup.label"); //$NON-NLS-1$

	private String PROMPT_ON_DEL_FROM_MODEL = DiagramResourceManager
		.getI18NString("DiagramsPreferencePage.PromptOnDelFromModel.label"); //$NON-NLS-1$

	private String PROMPT_ON_DEL_FROM_DIAGRAM = DiagramResourceManager
		.getI18NString("DiagramsPreferencePage.PromptOnDelFromDiagram.label"); //$NON-NLS-1$


	private BooleanFieldEditor promptOnDelFromDiagram = null;

	private BooleanFieldEditor promptOnDelFromModel = null;

	/**
	 * Creates a new instance and initializes the preference store.
	 */
	public ModelingDiagramsPreferencePage() {
		super();
		setPreferenceStore(DiagramUIPlugin.getInstance().getPreferenceStore());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.preferences.AbstractPreferencePage#addFields(org.eclipse.swt.widgets.Composite)
	 */
	protected void addFields(Composite parent) {
		super.addFields(parent);

		Group deletingElemsFromDSGroup = new Group(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		deletingElemsFromDSGroup.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		deletingElemsFromDSGroup.setLayoutData(gridData);
		deletingElemsFromDSGroup.setText(DELETING_ELEMS_FROM_DS);

		Composite deletingElemsFromDSGroupComposite = new Composite(
			deletingElemsFromDSGroup, SWT.NONE);

		// Add 1st Check Box
		promptOnDelFromDiagram = new BooleanFieldEditor(
			IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_DIAGRAM,
			PROMPT_ON_DEL_FROM_DIAGRAM, deletingElemsFromDSGroupComposite);
		addField(promptOnDelFromDiagram);

		// Add 2nd Check Box
		promptOnDelFromModel = new BooleanFieldEditor(
			IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_MODEL,
			PROMPT_ON_DEL_FROM_MODEL, deletingElemsFromDSGroupComposite);
		addField(promptOnDelFromModel);
	}

	/**
	 * Initializes the default preference values for this preference store.
	 * 
	 * @param IPreferenceStore
	 *            preferenceStore
	 */
	public static void initDefaults(IPreferenceStore preferenceStore) {
		DiagramsPreferencePage.initDefaults(preferenceStore);

		preferenceStore.setDefault(
			IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_DIAGRAM, true);
		preferenceStore.setDefault(
			IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_MODEL, true);
	}

}
