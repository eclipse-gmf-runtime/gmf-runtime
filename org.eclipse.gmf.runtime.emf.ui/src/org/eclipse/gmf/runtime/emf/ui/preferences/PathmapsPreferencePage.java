/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.preferences;

import java.util.Iterator;

import org.eclipse.core.resources.IPathVariableChangeEvent;
import org.eclipse.core.resources.IPathVariableChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.resources.PathmapManager;
import org.eclipse.gmf.runtime.emf.ui.internal.l10n.EMFUIMessages;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferenceLinkArea;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

/**
 * Preference page for specifying the path variables that should be considered
 * for modeling.
 * <P>
 * Path variable are created on the "Linked Resources" preference page, and
 * selected for modeling using this page.
 * 
 * @author Chris McGee
 */
public class PathmapsPreferencePage
	extends PreferencePage implements IWorkbenchPreferencePage {

	private List referencedPathVariables;
	private List pathVariables;
	private Button add;
	private IPathVariableChangeListener pathVariableChangeListener;
	private boolean disposed = true;

	protected void initHelp() {
		// No context-sensitive help for now.
	}

	protected Control createContents(Composite parent) {
		GridData gridData = null;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		
		composite.setLayout(new GridLayout(3, false));
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		composite.setLayoutData(gridData);
		
        PreferenceLinkArea pathVariablesArea = new PreferenceLinkArea(
            composite,
            SWT.NONE,
            "org.eclipse.ui.preferencePages.LinkedResources", EMFUIMessages.PathmapsPreferencePage_mainDescription, //$NON-NLS-1$
            (IWorkbenchPreferenceContainer) getContainer(), null);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 3;
		pathVariablesArea.getControl().setLayoutData(gridData);
		
		Label pathVariablesLabel = new Label(composite, SWT.LEFT);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 2;
		gridData.verticalIndent = 20;
		pathVariablesLabel.setLayoutData(gridData);
		pathVariablesLabel.setText(EMFUIMessages.PathmapsPreferencePage_availablePathVariables);
		
		Label referencedPathVariablesLabel = new Label(composite, SWT.LEFT);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		gridData.verticalIndent = 20;
		referencedPathVariablesLabel.setLayoutData(gridData);
		referencedPathVariablesLabel.setText(EMFUIMessages.PathmapsPreferencePage_pathVariablesUsedInModeling);
		
		pathVariables = new List(composite,SWT.MULTI | SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		pathVariables.setLayoutData(gridData);
		
		Composite buttonComposite = new Composite(composite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(1, false));
		add = new Button(buttonComposite, SWT.CENTER);
		add.setText(EMFUIMessages.PathmapsPreferencePage_addChevron);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		add.setLayoutData(gridData);
		Button addAll = new Button(buttonComposite, SWT.CENTER);
		addAll.setText(EMFUIMessages.PathmapsPreferencePage_addAllChevron);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		addAll.setLayoutData(gridData);
		Button remove = new Button(buttonComposite,SWT.CENTER);
		remove.setText(EMFUIMessages.PathmapsPreferencePage_removeChevron);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		gridData.verticalIndent = 10;
		remove.setLayoutData(gridData);
		Button removeAll = new Button(buttonComposite, SWT.CENTER);
		removeAll.setText(EMFUIMessages.PathmapsPreferencePage_removeAllChevron);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan=1;
		removeAll.setLayoutData(gridData);
		
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		buttonComposite.setLayoutData(gridData);
		
		referencedPathVariables = new List(composite,SWT.MULTI | SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		referencedPathVariables.setLayoutData(gridData);
		
		pathVariables.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				referencedPathVariables.deselectAll();
				
				if (!validateSelections(pathVariables.getSelection())) {
					setMessage(EMFUIMessages.PathmapsPreferencePage_incompatiblePathVariableErrorMessage,ERROR);
					add.setEnabled(false);
				} else {
					setMessage(null);
					add.setEnabled(true);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// No action necessary
			}
		});
		
		referencedPathVariables.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				setMessage(null);
				add.setEnabled(true);
				pathVariables.deselectAll();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// No action necessary
			}
		});
		
		add.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				String[] selections = pathVariables.getSelection();
				
				for (int i=0; i<selections.length; i++) {
					referencedPathVariables.add(selections[i]);
					pathVariables.remove(selections[i]);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// No action is necessary
			}
		});
		
		addAll.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// No action necessary
			}

			public void widgetSelected(SelectionEvent e) {
				String[] items = pathVariables.getItems();
				
				for (int i=0; i<items.length; i++) {
					if (validateSelections(new String[]{items[i]})) {
						referencedPathVariables.add(items[i]);
						pathVariables.remove(items[i]);
					}
				}
			}
		});
		
		remove.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				String[] selections = referencedPathVariables.getSelection();
				
				for (int i=0; i<selections.length; i++) {
					pathVariables.add(selections[i]);
					referencedPathVariables.remove(selections[i]);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// No action is necessary
			}
		});
		
		removeAll.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// No action is necessary
			}

			public void widgetSelected(SelectionEvent e) {
				String[] items = referencedPathVariables.getItems();
				
				for (int i=0; i<items.length; i++) {
					pathVariables.add(items[i]);
					referencedPathVariables.remove(items[i]);
				}
			}
		});
		
		initializeContents();
		
		// In case of any changes to the path variables, we will refresh ourselves to show
		//  the up-to-date information.
		pathVariableChangeListener = new IPathVariableChangeListener() {
			public void pathVariableChanged(IPathVariableChangeEvent event) {
				referencedPathVariables.getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (!PathmapsPreferencePage.this.disposed) {
							performDefaults();
						}
					}
				});
			}
		};
		ResourcesPlugin.getWorkspace().getPathVariableManager().addChangeListener(pathVariableChangeListener);
		
		disposed = false;
		
		applyDialogFont(composite);
		
		return composite;
	}
	
	private boolean validateSelections(String[] selections) {
		if (selections.length == 0)
			return false;
		
		for (int i=0; i<selections.length; i++) {
			String selection = selections[i];
			
			if (!PathmapManager.isCompatiblePathVariable(selection)) {
				return false;
			}
		}
		return true;
	}

	private void initializeContents() {
		setMessage(null);
		add.setEnabled(true);
		referencedPathVariables.removeAll();
		pathVariables.removeAll();
		
		String[] pathVariableNames = ResourcesPlugin.getWorkspace().getPathVariableManager().getPathVariableNames();
		for (int i=0; i<pathVariableNames.length; i++) {
			pathVariables.add(pathVariableNames[i]);
		}
		
		for (Iterator i = PathmapManager.getPathVariableReferences().iterator(); i.hasNext();) {
			String pathVariable = (String)i.next();
			referencedPathVariables.add(pathVariable);
			pathVariables.remove(pathVariable);
		}
	}

	public void init(IWorkbench workbench) {
		// No initialization is necessary.
	}
	
	protected void performDefaults() {
		initializeContents();
		super.performDefaults();
	}
	
	public boolean performOk() {
		String[] nonReferencedPathVariables = pathVariables.getItems();
		for (int i=0; i<nonReferencedPathVariables.length; i++) {
			PathmapManager.removePathVariableReference(nonReferencedPathVariables[i]);
		}
		
		String[] variablesToReference = referencedPathVariables.getItems();
		for (int i=0; i<variablesToReference.length; i++) {
			PathmapManager.addPathVariableReference(variablesToReference[i]);
		}
		
		PathmapManager.updatePreferenceStore();
		
		return true;
	}
	
	public void dispose() {
		disposed = true;
		if (pathVariableChangeListener != null) {
			ResourcesPlugin.getWorkspace().getPathVariableManager().removeChangeListener(pathVariableChangeListener);
			pathVariableChangeListener = null;
		}
		super.dispose();
	}
}
