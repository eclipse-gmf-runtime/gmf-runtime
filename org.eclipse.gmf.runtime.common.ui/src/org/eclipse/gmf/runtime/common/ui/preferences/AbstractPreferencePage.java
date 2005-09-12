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

package org.eclipse.gmf.runtime.common.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.help.WorkbenchHelp;

/**
 * Abstract class for preference pages. Subclasses must set the preference store
 * in the constructor by calling <@link
 * PreferencePage#setPreferenceStore(org.eclipse.jface.preference.IPreferenceStore)>
 * or implement <@link PreferencePage#doGetPreferenceStore()> to return the
 * appropriate plugin's preference store.
 * 
 * @author schafe, cmahoney
 */
public abstract class AbstractPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	/**
	 * Help ID string
	 */
	private String helpContextId = null;

	/**
	 * Constructor that calls the superclass and passses in the GridLayout
	 * layout style constant.
	 */
	public AbstractPreferencePage() {
		super(GRID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		initHelp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {

		//initializing help context id, this is consistent
		//with Eclipse preference pages
		if (helpContextId != null)
			WorkbenchHelp.setHelp(parent, helpContextId);
		return super.createContents(parent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {

		Composite parent = getFieldEditorParent();
		addFields(parent);

	}

	/**
	 * Adds the field editors to this composite.
	 * 
	 * @param parent the parent Composite that the field editors will be added
	 * to
	 */
	protected abstract void addFields(Composite parent);

	/**
	 * Initialize the context sensitive help id for this preference page.
	 */
	abstract protected void initHelp();

	/**
	 * Sets the context sensitive help id for this preference page.
	 * 
	 * @param id String ID for the context sensitive help
	 */
	protected void setPageHelpContextId(String id) {
		this.helpContextId = id;
	}

}