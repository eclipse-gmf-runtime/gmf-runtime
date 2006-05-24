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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IPathVariableChangeEvent;
import org.eclipse.core.resources.IPathVariableChangeListener;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gmf.runtime.emf.core.internal.resources.PathmapManager;
import org.eclipse.gmf.runtime.emf.ui.internal.l10n.EMFUIMessages;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
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

	private ScrolledComposite referencedPathVariablesScroll;
	private TableViewer referencedPathVariables;
	private StringsContentProvider referencedPathVariablesContent;
	private ScrolledComposite pathVariablesScroll;
	private TableViewer pathVariables;
	private StringsContentProvider pathVariablesContent;
	private Button add;
	private Button remove;
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
		
		Composite pathVariablesComposite = new Composite(composite, SWT.NONE);
		pathVariablesComposite.setLayout(new GridLayout(1, false));
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		pathVariablesComposite.setLayoutData(gridData);
		
		Label pathVariablesLabel = new Label(pathVariablesComposite, SWT.LEFT);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 2;
		gridData.verticalIndent = 20;
		pathVariablesLabel.setLayoutData(gridData);
		pathVariablesLabel.setText(EMFUIMessages.PathmapsPreferencePage_availablePathVariables);
		
		pathVariablesScroll = new ScrolledComposite(
				pathVariablesComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		pathVariablesScroll.setExpandHorizontal(true);
		pathVariablesScroll.setExpandVertical(true);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		pathVariablesScroll.setLayoutData(gridData);
		pathVariables = new TableViewer(pathVariablesScroll, SWT.MULTI);
		pathVariablesScroll.setContent(pathVariables.getTable());
		
		TableColumn column = new TableColumn(pathVariables.getTable(), SWT.LEFT);
		column.setMoveable(false);
		column.setResizable(false);
		pathVariables.getTable().addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				pathVariables.getTable().getColumn(0).setWidth(
						pathVariables.getTable().getClientArea().width);
			}});
		pathVariablesContent = new StringsContentProvider();
		pathVariables.setContentProvider(pathVariablesContent);
		pathVariables.setLabelProvider(new StringsLabelProvider());
		pathVariables.setComparator(new StringsViewerComparator());
		
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
		remove = new Button(buttonComposite,SWT.CENTER);
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
		
		Composite referencedPathVariablesComposite = new Composite(composite, SWT.NONE);
		referencedPathVariablesComposite.setLayout(new GridLayout(1, false));
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		referencedPathVariablesComposite.setLayoutData(gridData);
		
		Label referencedPathVariablesLabel = new Label(referencedPathVariablesComposite, SWT.LEFT);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 1;
		gridData.verticalIndent = 20;
		referencedPathVariablesLabel.setLayoutData(gridData);
		referencedPathVariablesLabel.setText(EMFUIMessages.PathmapsPreferencePage_pathVariablesUsedInModeling);
		
		referencedPathVariablesScroll = new ScrolledComposite(
				referencedPathVariablesComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		referencedPathVariablesScroll.setExpandHorizontal(true);
		referencedPathVariablesScroll.setExpandVertical(true);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		referencedPathVariablesScroll.setLayoutData(gridData);
		referencedPathVariables = new TableViewer(referencedPathVariablesScroll, SWT.MULTI);
		referencedPathVariablesScroll.setContent(referencedPathVariables.getTable());
		
		column = new TableColumn(referencedPathVariables.getTable(), SWT.LEFT);
		column.setMoveable(false);
		column.setResizable(false);
		referencedPathVariables.getTable().addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				referencedPathVariables.getTable().getColumn(0).setWidth(
						referencedPathVariables.getTable().getClientArea().width);
			}});
		referencedPathVariablesContent = new StringsContentProvider();
		referencedPathVariables.setContentProvider(referencedPathVariablesContent);
		referencedPathVariables.setLabelProvider(new StringsLabelProvider(true));
		referencedPathVariables.setComparator(new StringsViewerComparator());
		
		// adjust the scroll bars whenever the preference page is resized
		composite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				adjustScrollpanes();
			}});
		
		pathVariables.getTable().addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseHover(MouseEvent e) {
				TableItem item = pathVariables.getTable().getItem(new Point(e.x, e.y));
				String tip = null;
				
				if (item != null) {
					String var = item.getText(0);
					tip = getValue(var, false);
				}
				
				pathVariables.getTable().setToolTipText(tip);
			}});
		
		pathVariables.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) { // prevent oscillation
					referencedPathVariables.setSelection(new StructuredSelection());
					remove.setEnabled(true);
					
					if (!validateAdditions((IStructuredSelection) event.getSelection(), true)) {
						add.setEnabled(false);
					} else {
						setMessage(null);
						add.setEnabled(true);
					}
				}
			}
		});
		
		referencedPathVariables.getTable().addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseHover(MouseEvent e) {
				TableItem item = referencedPathVariables.getTable().getItem(
						new Point(e.x, e.y));
				String tip = null;
				
				if (item != null) {
					String var = item.getText(0);
					tip = getValue(var, true);
				}
				
				referencedPathVariables.getTable().setToolTipText(tip);
			}});
		
		referencedPathVariables.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) { // prevent oscillation
					add.setEnabled(true);
					pathVariables.setSelection(new StructuredSelection());
					
					if (!validateRemovals((IStructuredSelection) event.getSelection(), true)) {
						remove.setEnabled(false);
					} else {
						setMessage(null);
						remove.setEnabled(true);
					}
				}
			}
		});
		
		add.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection =
					(IStructuredSelection) pathVariables.getSelection();
				
				for (Iterator iter = selection.iterator(); iter.hasNext();) {
					String name = (String) iter.next();
					pathVariablesContent.remove(name);
					referencedPathVariablesContent.add(name);
					adjustScrollpanes();
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
				Object[] items = pathVariablesContent.getElements(null);
				
				for (int i=items.length - 1; i >= 0; i--) {
					if (validateAdditions(new StructuredSelection(items[i]), false)) {
						String name = (String) items[i];
						pathVariablesContent.remove(name);
						referencedPathVariablesContent.add(name);
						adjustScrollpanes();
					}
				}
			}
		});
		
		remove.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection =
					(IStructuredSelection) referencedPathVariables.getSelection();
				
				for (Iterator iter = selection.iterator(); iter.hasNext();) {
					String name = (String) iter.next();
					referencedPathVariablesContent.remove(name);
					pathVariablesContent.add(name);
					adjustScrollpanes();
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
				Object[] items = referencedPathVariablesContent.getElements(null);
				
				for (int i=items.length - 1; i >= 0; i--) {
					if (validateRemovals(new StructuredSelection(items[i]), false)) {
						String name = (String) items[i];
						referencedPathVariablesContent.remove(name);
						pathVariablesContent.add(name);
						adjustScrollpanes();
					}
				}
			}
		});
		
		initializeContents();
		
		// In case of any changes to the path variables, we will refresh ourselves to show
		//  the up-to-date information.
		pathVariableChangeListener = new IPathVariableChangeListener() {
			public void pathVariableChanged(IPathVariableChangeEvent event) {
				referencedPathVariables.getTable().getDisplay().asyncExec(new Runnable() {
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
	
	private void adjustScrollpanes() {
		pathVariablesScroll.setMinSize(
				pathVariables.getTable().computeSize(SWT.DEFAULT, SWT.DEFAULT));
		pathVariablesScroll.layout();
		referencedPathVariablesScroll.setMinSize(
				referencedPathVariables.getTable().computeSize(SWT.DEFAULT, SWT.DEFAULT));
		referencedPathVariablesScroll.layout();
	}
	
	private String getValue(String pathVariable, boolean includeRegistered) {
		String result = null;
		
		if (includeRegistered && PathmapManager.isRegisteredPathVariable(pathVariable)) {
			String path = PathmapManager.getRegisteredValue(pathVariable);
			
			if (path != null) {
				URI uri = URI.createURI(path);
				uri = CommonPlugin.resolve(uri);
				
				if (uri.isFile()) {
					path = uri.toFileString();
				} else {
					path = uri.toString();
				}
				
				result = path;
			}
		} else {
			IPathVariableManager pathVarMgr =
				ResourcesPlugin.getWorkspace().getPathVariableManager();
			
			IPath path = pathVarMgr.getValue(pathVariable);
			
			if (path != null) {
				result = path.toOSString();
			}
		}
		
		return result;
	}
	
	private boolean validateAdditions(IStructuredSelection selection, boolean showError) {
		if (selection.isEmpty())
			return false;
		
		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			
			if (!PathmapManager.isCompatiblePathVariable(name)) {
				if (showError) {
					setMessage(EMFUIMessages.PathmapsPreferencePage_incompatiblePathVariableErrorMessage,ERROR);
				}
				return false;
			}
			
			if (PathmapManager.isRegisteredPathVariable(name)) {
				if (showError) {
					setMessage(EMFUIMessages.PathmapsPreferencePage_registeredPathVariableErrorMessage,ERROR);
				}
				return false;
			}
		}
		return true;
	}

	private boolean validateRemovals(IStructuredSelection selection, boolean showError) {
		if (selection.isEmpty())
			return false;
		
		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			
			if (PathmapManager.isRegisteredPathVariable(name)) {
				if (showError) {
					setMessage(EMFUIMessages.PathmapsPreferencePage_registeredPathVariableErrorMessage,ERROR);
				}
				return false;
			}
		}
		return true;
	}

	private void initializeContents() {
		setMessage(null);
		add.setEnabled(true);
		remove.setEnabled(true);
		
		referencedPathVariables.setInput(new HashSet(PathmapManager.getAllPathVariables()));
		
		Set currentVariables = PathmapManager.getPathVariableReferences();
		
		Set available = new HashSet();
		String[] pathVariableNames = ResourcesPlugin.getWorkspace().getPathVariableManager().getPathVariableNames();
		for (int i=0; i<pathVariableNames.length; i++) {
			if (!currentVariables.contains(pathVariableNames[i])) {
				available.add(pathVariableNames[i]);
			}
		}
		
		pathVariables.setInput(available);
	}

	public void init(IWorkbench workbench) {
		// No initialization is necessary.
	}
	
	protected void performDefaults() {
		initializeContents();
		super.performDefaults();
	}
	
	public boolean performOk() {
		Object[] nonReferencedPathVariables = pathVariablesContent.getElements(null);
		for (int i=0; i<nonReferencedPathVariables.length; i++) {
			String variableName = (String) nonReferencedPathVariables[i];
			PathmapManager.removePathVariableReference(variableName);
		}
		
		Set currentVariables = PathmapManager.getAllPathVariables();
		Object[] variablesToReference = referencedPathVariablesContent.getElements(null);
		for (int i=0; i<variablesToReference.length; i++) {
			String variableName = (String) variablesToReference[i];
			
			if (!currentVariables.contains(variableName)) {
				PathmapManager.addPathVariableReference(variableName);
			}
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
	
	private static class StringsContentProvider implements IStructuredContentProvider {
		private Set strings;
		private TableViewer table;
		
		StringsContentProvider() {
			strings = new HashSet();
		}
		
		void add(String string) {
			if (!strings.contains(string)) {
				strings.add(string);
				table.add(string);
			}
		}
		
		void remove(String string) {
			if (strings.contains(string)) {
				strings.remove(string);
				table.remove(string);
			}
		}
		
		public Object[] getElements(Object inputElement) {
			return strings.toArray();
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			strings = (Set) newInput;
			table = (TableViewer) viewer;
		}

		public void dispose() {
			// nothing to clean up
		}
	}
	
	private static class StringsLabelProvider implements ITableLabelProvider, IColorProvider {
		private final boolean isReferencedPathVariables;
		
		StringsLabelProvider() {
			this(false);
		}
		
		StringsLabelProvider(boolean isReferencedPathVariables) {
			this.isReferencedPathVariables = isReferencedPathVariables;
		}
		
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			return (columnIndex == 0) ? (String) element : null;
		}

		public void dispose() {
			// nothing to dispose (the colors are all shared system colors)
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void addListener(ILabelProviderListener listener) {
			// not using listeners
		}

		public void removeListener(ILabelProviderListener listener) {
			// not using listeners
		}

		public Color getBackground(Object element) {
			if (isReferencedPathVariables && PathmapManager.isRegisteredPathVariable((String) element)) {
				return Display.getDefault().getSystemColor(
						SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
			}
			
			return null;
		}

		public Color getForeground(Object element) {
			return null;
		}
	}
	
	private static class StringsViewerComparator extends ViewerComparator {
		StringsViewerComparator() {
			super();
		}
		
		public int category(Object element) {
			// sort statically-registered variables to the end of the list
			return PathmapManager.isRegisteredPathVariable((String) element)? 1 : 0;
		}
	}
}
