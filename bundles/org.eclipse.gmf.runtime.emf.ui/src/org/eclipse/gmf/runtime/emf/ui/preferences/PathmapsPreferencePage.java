/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.preferences;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gmf.runtime.emf.core.internal.resources.PathmapManager;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;
import org.eclipse.gmf.runtime.emf.ui.internal.l10n.EMFUIMessages;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.osgi.util.NLS;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferenceLinkArea;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

/**
 * Preference page for specifying the path variables that should be considered
 * for modeling.
 * <p>
 * Path variable are created on the "Linked Resources" preference page, and
 * selected for modeling using this page.
 * </p>
 * <p>
 * This class may be instantiated by clients, but is not intended to be
 * subclassed.
 * </p>
 * 
 * @author Chris McGee
 * @autor Christian W. Damus (cdamus)
 */
public class PathmapsPreferencePage
    extends PreferencePage
    implements IWorkbenchPreferencePage {

    private static final String NAME_ATTRIBUTE = "name"; //$NON-NLS-1$

    private IPathVariableManager pathVariableManager = ResourcesPlugin
        .getWorkspace().getPathVariableManager();

    private Composite pathVariablesComposite;
    
    private CheckboxTableViewer pathVariables;

    private PathVariableContentProvider pathVariablesContent;

    private Button newVariable;

    private Button editVariable;

    private Button removeVariable;

    /** Path variable changes since last time the Apply button was pressed. */
    private Map variableChanges = new HashMap();

    private Object addedToken = new Object();

    private Object changedToken = new Object();

    private Object removedToken = new Object();

    protected void initHelp() {
        // No context-sensitive help for now.
    }

    protected Control createContents(Composite parent) {
        GridData gridData = null;
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setFont(parent.getFont());

        composite.setLayout(new GridLayout(2, false));

        PreferenceLinkArea pathVariablesArea = new PreferenceLinkArea(
            composite,
            SWT.NONE,
            "org.eclipse.ui.preferencePages.LinkedResources", //$NON-NLS-1$
            EMFUIMessages.PathmapsPreferencePage_mainDescription,
            (IWorkbenchPreferenceContainer) getContainer(), null);
        gridData = new GridData(GridData.FILL_HORIZONTAL
            | GridData.FILL_VERTICAL);
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = false;
        gridData.horizontalSpan = 2;
        pathVariablesArea.getControl().setLayoutData(gridData);

        Label pathVariablesLabel = new Label(composite, SWT.LEFT);
        gridData = new GridData(GridData.FILL_HORIZONTAL
            | GridData.FILL_VERTICAL);
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = false;
        gridData.horizontalSpan = 2;
        gridData.verticalIndent = 20;
        pathVariablesLabel.setLayoutData(gridData);
        pathVariablesLabel
            .setText(EMFUIMessages.PathmapsPreferencePage_availablePathVariables);

        pathVariablesComposite = new Composite(composite, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL
            | GridData.FILL_VERTICAL);
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 1;
        pathVariablesComposite.setLayoutData(gridData);
        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        pathVariablesComposite.setLayout(gridLayout);

        pathVariables = CheckboxTableViewer.newCheckList(pathVariablesComposite,
            SWT.MULTI);

        pathVariablesContent = new PathVariableContentProvider();
        pathVariables.setContentProvider(pathVariablesContent);
        pathVariables.setLabelProvider(new PathVariableLabelProvider());
        pathVariables.setComparator(new PathVariableViewerComparator());
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        // These two hard coded values were borrowed from similar code in
        // org.eclipse.ui.internal.ide.dialogs.PathVariablesGroup
        gridData.heightHint = pathVariables.getTable().getItemHeight() * 7;
        gridData.widthHint = 332;
        pathVariables.getTable().setLayoutData(gridData);

        Composite buttonComposite = new Composite(composite, SWT.NONE);
        buttonComposite.setLayout(new GridLayout(1, false));
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = false;
        gridData.grabExcessVerticalSpace = false;
        gridData.horizontalSpan = 1;
        gridData.verticalAlignment = GridData.BEGINNING;
        buttonComposite.setLayoutData(gridData);

        newVariable = new Button(buttonComposite, SWT.CENTER);
        newVariable.setText(EMFUIMessages.PathmapsPreferencePage_newVariable);
        setButtonLayoutData(newVariable);

        editVariable = new Button(buttonComposite, SWT.CENTER);
        editVariable.setText(EMFUIMessages.PathmapsPreferencePage_editVariable);
        setButtonLayoutData(editVariable);

        removeVariable = new Button(buttonComposite, SWT.CENTER);
        removeVariable
            .setText(EMFUIMessages.PathmapsPreferencePage_removeVariable);
        setButtonLayoutData(removeVariable);

        pathVariables
            .addSelectionChangedListener(new ISelectionChangedListener() {

                public void selectionChanged(SelectionChangedEvent event) {
                    pathVariableSelected(event.getSelection());
                }
            });

        pathVariables.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                pathVariableChecked(event, (PathVariableEntry) event
                    .getElement());
            }
        });

        newVariable.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                addPathVariable();
            }
        });

        editVariable.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                editPathVariable(pathVariables.getSelection());
            }
        });

        removeVariable.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                removePathVariable(pathVariables.getSelection());
            }
        });

        initializeContents();

        applyDialogFont(composite);

        pathVariableSelected(pathVariables.getSelection());

        return composite;
    }

    /**
     * Responds to the user's gesture to either check or uncheck the specified
     * <code>entry</code> in the path variables list. This may, according to
     * the status of the path variable entry, result in the user's change being
     * reverted (e.g., in the case of attempting to uncheck a variable
     * registered on the extension point). This works around the inability in
     * SWT to disable the checkbox of an item in a check-table (see <a
     * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=76509">bug 76509</a>
     * for details).
     * 
     * @param event
     *            the (un)check event
     * @param entry
     *            the path variable entry that was (un)checked
     */
    private void pathVariableChecked(CheckStateChangedEvent event,
            PathVariableEntry entry) {
        if (event.getChecked()) {
            // validate the check
            if (validateSelection(entry, false)) {
                entry.setSelected(true);
            } else {
                event.getCheckable().setChecked(entry, false);
            }
        } else {
            // validate the uncheck
            if (validateDeselection(entry, false)) {
                entry.setSelected(false);
            } else {
                event.getCheckable().setChecked(entry, true);
            }
        }
    }

    /**
     * Handles the selection of zero or more path variables in the list,
     * updating the enablement state of the "Edit..." and "Remove" buttons.
     * 
     * @param selection
     *            the new path variables list selection
     */
    private void pathVariableSelected(ISelection selection) {
        IStructuredSelection ssel = (IStructuredSelection) selection;

        editVariable.setEnabled(validateEdit(ssel, false));
        removeVariable.setEnabled(validateRemove(ssel, false));
    }

    /**
     * Updates the map of pending path variable changes to indicate that the
     * specified variable has been added by the user.
     * 
     * @param variableName
     *            the name of the added variable
     */
    private void markAdded(String variableName) {
        Object currentChange = variableChanges.get(variableName);

        if (currentChange == removedToken) {
            // if we previously removed this variable's value, then it will
            // appear to be a change when we sync on apply
            variableChanges.put(variableName, changedToken);
        } else if (currentChange != changedToken) {
            // shouldn't have been a "changed" if we thought we were adding
            variableChanges.put(variableName, addedToken);
        }
    }

    /**
     * Queries whether the specified path variable has an add change pending, to
     * be applied when the OK/Apply button is pressed.
     * 
     * @param variableName
     *            the path variable name
     * @return <code>true</code> if the variable has a pending change that is
     *         an add; <code>false</code>, otherwise
     */
    boolean isAdded(String variableName) {
        return variableChanges.get(variableName) == addedToken;
    }

    /**
     * Updates the map of pending path variable changes to indicate that the
     * specified variable has been removed by the user.
     * 
     * @param variableName
     *            the name of the removed variable
     */
    private void markRemoved(String variableName) {
        Object currentChange = variableChanges.get(variableName);

        if (currentChange == addedToken) {
            // it was added since the last apply? Just forget about it, then
            variableChanges.remove(variableName);
        } else {
            variableChanges.put(variableName, removedToken);
        }
    }

    /**
     * Queries whether the specified path variable has a remove change pending,
     * to be applied when the OK/Apply button is pressed.
     * 
     * @param variableName
     *            the path variable name
     * @return <code>true</code> if the variable has a pending change that is
     *         a removal; <code>false</code>, otherwise
     */
    boolean isRemoved(String variableName) {
        return variableChanges.get(variableName) == removedToken;
    }

    /**
     * Updates the map of pending path variable changes to indicate that the
     * specified variable's value has been changed by the user.
     * 
     * @param variableName
     *            the name of the changed variable
     */
    private void markChanged(String variableName) {
        Object currentChange = variableChanges.get(variableName);

        if (currentChange == addedToken) {
            // do nothing in this case. If it was added, changing it doesn't
            // change the fact that it's a new variable
        } else {
            variableChanges.put(variableName, changedToken);
        }
    }

    /**
     * Queries whether the specified path variable has a change of value
     * pending, to be applied when the OK/Apply button is pressed.
     * 
     * @param variableName
     *            the path variable name
     * @return <code>true</code> if the variable has a pending change that is
     *         a value change; <code>false</code>, otherwise
     */
    boolean isChanged(String variableName) {
        return variableChanges.get(variableName) == changedToken;
    }

    /**
     * Queries whether the current pending path variables (not yet applied to
     * the workspace and GMF path map manager) has a variable referencing the
     * specified location. Note that this does not consider path variables that
     * are pending removal or others that are currently defined in the workspace
     * and/or GMF that are not visible.
     * 
     * @param location
     *            a location
     * @return <code>true</code> if any of the path variables showing in the
     *         preference page has the specified location; <code>false</code>,
     *         otherwise
     */
    boolean isLocationDefined(IPath location) {
        for (Iterator iter = pathVariablesContent.entries.iterator(); iter
            .hasNext();) {
            if (location.equals(((PathVariableEntry) iter.next())
                .getLocationPath())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Handles the pushing of the "New..." button, to create a new path map
     * variable.
     */
    private void addPathVariable() {
        NewPathVariableDialog dlg = NewPathVariableDialog.openNew(this);
        if (dlg != null) {
            String name = dlg.getVariableName();
            IPath location = dlg.getVariableLocation();

            // prepare data for synchronization on apply
            markAdded(name);

            // by default, check the variable (if the user created it in this
            // pref page, assume that it should be used for GMF modeling)
            PathVariableEntry entry = new PathVariableEntry(name, location);
            entry.setSelected(true);
            pathVariablesContent.add(entry);
            pathVariables.setChecked(entry, true);

            // select the new path variable
            pathVariables.setSelection(new StructuredSelection(entry));
        }
    }

    /**
     * Handles the pushing of the "Edit..." button, to edit the path variable
     * contained in the specified <code>selection</code>.
     * 
     * @param selection
     *            the current selection in the path variables list (should
     *            contain a single {@link PathVariableEntry})
     */
    private void editPathVariable(ISelection selection) {
        PathVariableEntry entry = null;

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) selection;

            if (!ssel.isEmpty()) {
                entry = (PathVariableEntry) ssel.getFirstElement();
            }
        }

        if (entry != null) {
            String oldName = entry.getName();
            NewPathVariableDialog dlg = NewPathVariableDialog.openEdit(this,
                oldName, entry.getLocation());

            if (dlg != null) {
                String newName = dlg.getVariableName();
                IPath newLocation = dlg.getVariableLocation();
                boolean nameChanged = !oldName.equals(newName);

                if (nameChanged) {
                    // changing the name is like removing the old name
                    // and adding the new name

                    // prepare data for synchronization on apply
                    markAdded(newName);
                    markRemoved(oldName);
                } else {
                    // prepare data for synchronization on apply
                    markChanged(oldName);
                }

                entry.setName(newName);
                entry.setLocation(newLocation);

                pathVariables.update(entry,
                    nameChanged ? new String[] {NAME_ATTRIBUTE}
                        : null);
            }
        }
    }

    /**
     * Handles the pushing of the "Remove" button, to remove the path
     * variable(s) contained in the specified <code>selection</code>.
     * 
     * @param selection
     *            the current selection in the path variables list (should
     *            contain one or more {@link PathVariableEntry}s of which none
     *            is registered on the extension point)
     */
    private void removePathVariable(ISelection selection) {
        Iterator entries = null;

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) selection;

            if (!ssel.isEmpty()) {
                entries = ssel.iterator();
            }
        }

        if (entries != null) {
            while (entries.hasNext()) {
                PathVariableEntry entry = (PathVariableEntry) entries.next();
                String name = entry.getName();

                // prepare data for synchronization on apply
                markRemoved(name);

                pathVariablesContent.remove(entry);
            }
        }
    }

    /**
     * Validates an attempt to check a previously unchecked path variable in the
     * list, optionally showing an error explaining the reason why this is not
     * permitted.
     * 
     * @param entry
     *            a path variable that the user attempted to check
     * @param showError
     *            whether to show any potential error message in the title area
     * @return whether the checking of this variable is permitted
     */
    private boolean validateSelection(PathVariableEntry entry, boolean showError) {
        String name = entry.getName();

        if (!PathmapManager.isCompatiblePathVariable(name)) {
            if (showError) {
                setMessage(
                    EMFUIMessages.PathmapsPreferencePage_incompatiblePathVariableErrorMessage,
                    ERROR);
            }
            return false;
        }

        if (PathmapManager.isRegisteredPathVariable(name)) {
            if (showError) {
                setMessage(
                    EMFUIMessages.PathmapsPreferencePage_registeredPathVariableErrorMessage,
                    ERROR);
            }
            return false;
        }

        return true;
    }

    /**
     * Validates an attempt to uncheck a previously checked path variable in the
     * list, optionally showing an error explaining the reason why this is not
     * permitted.
     * 
     * @param entry
     *            a path variable that the user attempted to uncheck
     * @param showError
     *            whether to show any potential error message in the title area
     * @return whether the unchecking of this variable is permitted
     */
    private boolean validateDeselection(PathVariableEntry entry,
            boolean showError) {
        if (entry.isRequired()) {
            if (showError) {
                setMessage(
                    EMFUIMessages.PathmapsPreferencePage_registeredPathVariableErrorMessage,
                    ERROR);
            }
            return false;
        }

        return true;
    }

    /**
     * Queries whether it is permitted to edit the specified
     * <code>selection</code> of path variables. Editing is only permitted for
     * a single selection that is not a registered path variable.
     * 
     * @param selection
     *            the current selection in the path variables list
     * @param showError
     *            whether to show any potential error message in the title area
     * @return whether the editing of this selection is permitted
     */
    private boolean validateEdit(IStructuredSelection selection,
            boolean showError) {
        if (selection.isEmpty() || (selection.size() > 1)) {
            return false;
        }

        String name = ((PathVariableEntry) selection.getFirstElement())
            .getName();

        if (PathmapManager.isRegisteredPathVariable(name)) {
            if (showError) {
                setMessage(
                    EMFUIMessages.PathmapsPreferencePage_registeredPathVariableErrorMessage,
                    ERROR);
            }

            return false;
        }

        return true;
    }

    /**
     * Queries whether it is permitted to remove the specified
     * <code>selection</code> of path variables. Removal is only permitted
     * when the selection is not empty and does not contain any registered path
     * variable.
     * 
     * @param selection
     *            the current selection in the path variables list
     * @param showError
     *            whether to show any potential error message in the title area
     * @return whether the editing of this selection is permitted
     */
    private boolean validateRemove(IStructuredSelection selection,
            boolean showError) {
        if (selection.isEmpty()) {
            return false;
        }

        for (Iterator iter = selection.iterator(); iter.hasNext();) {
            String name = ((PathVariableEntry) iter.next()).getName();

            if (PathmapManager.isRegisteredPathVariable(name)) {
                if (showError) {
                    setMessage(
                        EMFUIMessages.PathmapsPreferencePage_registeredPathVariableErrorMessage,
                        ERROR);
                }

                return false;
            }
        }

        return true;
    }

    /**
     * Loads the contents of the Path Variables list, additionally setting the
     * check state of each variable.
     */
    private void initializeContents() {
        setMessage(null);

        variableChanges.clear();

        Set currentVariables = PathmapManager.getPathVariableReferences();

        Set allVariables = new HashSet();
        Set checkedVariables = new HashSet();

        Set pathVariableNames = new HashSet();
        pathVariableNames.addAll(Arrays.asList(pathVariableManager
            .getPathVariableNames()));
        pathVariableNames.addAll(PathmapManager.getAllPathVariables());

        for (Iterator iter = pathVariableNames.iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            PathVariableEntry entry;

            if (PathmapManager.isRegisteredPathVariable(name)) {
                String value = PathmapManager.getRegisteredValue(name);

                try {
                    URI uri = URI.createURI(value);

                    if (uri.isFile()) {
                        // show the user a familiar file system path instead
                        // of a URI
                        value = uri.toFileString();
                    }
                } catch (RuntimeException e) {
                    // the value is not a valid URI. Nothing for us to
                    // do; that is a problem for the plug-in developer
                    // who registered this path map. We'll show the
                    // value as is
                }

                entry = new PathVariableEntry(name, value);
                checkedVariables.add(entry);
                allVariables.add(entry);
            } else if (PathmapManager.isCompatiblePathVariable(name)) {

                entry = new PathVariableEntry(name, pathVariableManager
                    .getValue(name));

                if (currentVariables.contains(entry.getName())) {
                    checkedVariables.add(entry);
                    entry.setSelected(true);
                }

                allVariables.add(entry);
            }
        }

        pathVariables.setInput(allVariables);
        pathVariables.setCheckedElements(checkedVariables.toArray());
    }

    public void init(IWorkbench workbench) {
        // No initialization is necessary.
    }

    protected void performDefaults() {
        initializeContents();
        super.performDefaults();
    }

    /**
     * Applies the current check state of every path variable to the GMF
     * {@link PathmapManager}'s list of path variable references and saves the
     * preference store.
     */
    public boolean performOk() {
        Set currentVariables = PathmapManager.getPathVariableReferences();

        try {
            // first, process the removed workspace path variables
            for (Iterator iter = variableChanges.keySet().iterator(); iter
                .hasNext();) {
                String name = (String) iter.next();

                if (isRemoved(name)) {
                    if (pathVariableManager.isDefined(name)) {
                        pathVariableManager.setValue(name, null);
                    }

                    PathmapManager.removePathVariableReference(name);

                    iter.remove(); // successfully processed this change
                }
            }

            // next, process the current set of path variable references to
            // add/remove them according to the user's preferences
            Object[] variables = pathVariablesContent.getElements(null);
            for (int i = 0; i < variables.length; i++) {
                PathVariableEntry entry = (PathVariableEntry) variables[i];
                String name = entry.getName();

                if (isChanged(name) || isAdded(name)
                    && !pathVariableManager.isDefined(name)) {
                    // set the workspace path variable's new value, now
                    pathVariableManager.setValue(name, new Path(entry
                        .getLocation()));

                    // successfully processed this change
                    variableChanges.remove(name);
                }

                if (entry.isSelected() && !currentVariables.contains(name)) {
                    PathmapManager.addPathVariableReference(name);
                } else if (!entry.isSelected()
                    && currentVariables.contains(name)) {
                    PathmapManager.removePathVariableReference(name);
                }
            }

            PathmapManager.updatePreferenceStore();

            return true;
        } catch (CoreException e) {
            ErrorDialog.openError(getShell(),
                EMFUIMessages.PathmapsPreferencePage_promptTitle,
                EMFUIMessages.PathmapsPreferencePage_updateFailed, e
                    .getStatus());
            return false;
        }
    }

    /**
     * A content provider for the Path Variables list.
     */
    private static class PathVariableContentProvider
        implements IStructuredContentProvider {

        private Set entries;

        private TableViewer table;

        PathVariableContentProvider() {
            entries = new HashSet();
        }

        /**
         * Adds a path variable to the list.
         * 
         * @param entry
         *            the new path variable
         */
        void add(PathVariableEntry entry) {
            if (!entries.contains(entry)) {
                entries.add(entry);
                table.add(entry);
            }
        }

        /**
         * Removes a path variable from the list.
         * 
         * @param entry
         *            the path variable to remove
         */
        void remove(PathVariableEntry entry) {
            if (entries.contains(entry)) {
                entries.remove(entry);
                table.remove(entry);
            }
        }

        public Object[] getElements(Object inputElement) {
            return entries.toArray();
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            entries = (Set) newInput;
            table = (TableViewer) viewer;
        }

        public void dispose() {
            // nothing to clean up
        }
    }

    /**
     * A label provider for the Path Variables list.
     */
    private static class PathVariableLabelProvider
        implements ITableLabelProvider, IColorProvider {

        private Image lockImage = null;

        PathVariableLabelProvider() {
            super();
        }

        /**
         * Shows a lock icon for registered path variables.
         */
        public Image getColumnImage(Object element, int columnIndex) {
            PathVariableEntry entry = (PathVariableEntry) element;
            String name = entry.getName();

            if (PathmapManager.isRegisteredPathVariable(name)) {
                return getLockImage();
            } else if (!isDirectory(entry.getLocation())) {
                return PlatformUI.getWorkbench()
                    .getSharedImages()
                    .getImage(ISharedImages.IMG_OBJS_WARN_TSK);
            }

            return null;
        }

        /**
         * Queries whether the specified location references a directory that
         * exists.
         * 
         * @param location
         *            a location
         * @return <code>true</code> if the location exists in the filesystem
         *         and is a directory
         */
        private boolean isDirectory(String location) {
            File file = new File(location);

            return file.exists() && file.isDirectory();
        }

        /**
         * Obtains the lazily-initialized lock image.
         * 
         * @return the lock image
         */
        private Image getLockImage() {
            if (lockImage == null) {
                lockImage = MslUIPlugin
                    .imageDescriptorFromPlugin(MslUIPlugin.getPluginId(),
                        "/icons/full/lock.gif").createImage(); //$NON-NLS-1$
            }

            return lockImage;
        }

        /**
         * Path variables are displayed in the same way as in the Linked
         * Resources preference page.
         */
        public String getColumnText(Object element, int columnIndex) {
            if (columnIndex != 0) {
                return null;
            }

            PathVariableEntry entry = (PathVariableEntry) element;

            // use the TextProcessor's default separators for file paths
            // if the entry is not required, because only if it is, will
            // it possibly be a URI
            String pathString = entry.isRequired() ? TextProcessor.process(
                entry.getLocation(), MslUIPlugin.URI_BIDI_SEPARATORS)
                : TextProcessor.process(entry.getLocation());

            return NLS.bind(
                EMFUIMessages.PathmapsPreferencePage_variablePattern, entry
                    .getName(), pathString);
        }

        public void dispose() {
            if (lockImage != null) {
                lockImage.dispose();
                lockImage = null;
            }
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
            return null;
        }

        public Color getForeground(Object element) {
            return null;
        }
    }

    /**
     * A sorter for the Path Variables list. All registered path maps sort to
     * the bottom of the list to keep them out of the user's way.
     */
    private static class PathVariableViewerComparator
        extends ViewerComparator {

        PathVariableViewerComparator() {
            super();
        }

        /**
         * We sort by <code>name</code>.
         */
        public boolean isSorterProperty(Object element, String property) {
            return NAME_ATTRIBUTE.equals(property);
        }

        /**
         * Registered variables are in a higher category than user variables.
         */
        public int category(Object element) {
            // sort statically-registered variables to the end of the list
            return PathmapManager
                .isRegisteredPathVariable(((PathVariableEntry) element)
                    .getName()) ? 1
                : 0;
        }
    }

    /**
     * Data model for a path variable in the Path Variables list.
     */
    private static final class PathVariableEntry {

        private String name;

        private String location;

        private IPath locationPath;

        private final boolean required;

        private boolean selected;

        /**
         * Initializes a user-defined path variable with the name and location
         * path.
         * 
         * @param name
         *            the variable name
         * @param location
         *            the location
         */
        PathVariableEntry(String name, IPath location) {
            this(name, location.toPortableString(), false);

            this.locationPath = location;
        }

        /**
         * Initializes a registered path variable with the name and location
         * derived from the URI.
         * 
         * @param name
         *            the variable name
         * @param location
         *            the location URI
         */
        PathVariableEntry(String name, String location) {
            this(name, location, true);
        }

        private PathVariableEntry(String name, String location, boolean required) {
            this.name = name;
            this.location = location;
            this.required = required;
            selected = required;
        }

        /**
         * Queries whether this path variable is required (a registered path
         * variable that the user cannot edit, remove, or uncheck).
         * 
         * @return whether I am required
         */
        boolean isRequired() {
            return required;
        }

        /**
         * Obtains the path variable name.
         * 
         * @return my name
         */
        String getName() {
            return name;
        }

        /**
         * Sets the path variable name, if it is editable.
         * 
         * @param name
         *            the new name
         */
        void setName(String name) {
            if (!isRequired()) {
                this.name = name;
            }
        }

        /**
         * Obtains the path variable location.
         * 
         * @return my location
         */
        String getLocation() {
            return location;
        }

        /**
         * Obtains the path variable location, as an {@link IPath}.
         * 
         * @return my location
         */
        IPath getLocationPath() {
            return locationPath;
        }

        /**
         * Sets the path variable name, if it is editable.
         * 
         * @param location
         *            the new location
         */
        void setLocation(IPath location) {
            if (!isRequired()) {
                this.locationPath = location;
                this.location = location.toPortableString();
            }
        }

        /**
         * Queries whether the path variable is checked. Required (registered)
         * path variables are always checked.
         * 
         * @return whether I am checked
         */
        boolean isSelected() {
            return selected;
        }

        /**
         * Sets whether the path variable is checked, if it is not registered.
         * 
         * @param selected
         *            whether I am checked
         */
        void setSelected(boolean selected) {
            if (!isRequired()) {
                this.selected = selected;
            }
        }

        /**
         * Displays path variable's debug string.
         */
        public String toString() {
            return getName() + " - " + getLocation(); //$NON-NLS-1$
        }
    }
}
