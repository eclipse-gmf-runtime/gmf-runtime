/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.gmf.runtime.emf.ui.preferences;

import java.io.File;

import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gmf.runtime.emf.core.internal.resources.PathmapManager;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;
import org.eclipse.gmf.runtime.emf.ui.internal.l10n.EMFUIMessages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Modal dialog for manipulation of path variables for modeling pathmaps. This
 * dialog supports both creation of new path variables, using the
 * {@link #openNew(Shell)} method, and editing of existing path variables, using
 * the {@link #openEdit(Shell, String, String)} method. In either case, if the
 * user successfully closes the dialog (i.e., hits the OK button), then the name
 * and location of the variable are accessed via the {@link #getVariableName()}
 * and {@link #getVariableLocation()} methods, respectively.
 * <p>
 * Note that this dialog intentionally differs from that utilized by the Linked
 * Resources preference page because GMF's pathmaps apply more constraints to
 * path variables, in particular that:
 * </p>
 * <ul>
 * <li>a pathmap variable name must be a valid URI segment, because it is used
 * as is in <tt>pathmap://</tt> URIs</li>
 * <li>a pathmap variable must reference a folder, not a file</li>
 * </ul>
 * 
 * @author Christian W. Damus (cdamus)
 */
class NewPathVariableDialog
    extends TitleAreaDialog {

    private String variableName;

    private IPath variableLocation;

    private Text nameText;

    private Text locationText;

    private String initialName;

    private String initialLocation;

    private final IPathVariableManager pathMgr;

    private final String plainMsg;

    private final PathmapsPreferencePage page;

    /**
     * Not instantiable by clients.
     * 
     * @param page
     *            the path maps preference page that I serve
     * @param name
     *            the current name of the variable to be edited, or
     *            <code>null</code> if creating a new path variable
     * @param location
     *            the current location of the variable to be edited, or
     *            <code>null</code> if creating a new path variable
     */
    private NewPathVariableDialog(PathmapsPreferencePage page, String name,
            String location) {
        super(page.getShell());

        this.page = page;

        this.initialName = name;
        this.initialLocation = location;

        if (name != null) {
            // edit mode
            plainMsg = EMFUIMessages.PathVariableDialog_editMessage;
        } else {
            // new mode
            plainMsg = EMFUIMessages.PathVariableDialog_newMessage;
        }

        pathMgr = ResourcesPlugin.getWorkspace().getPathVariableManager();
    }

    /**
     * Opens the path-variable editing dialog in creation mode, to create a new
     * path variable.
     * 
     * @param page
     *            the path maps preference page that the dialog serves
     * @return the dialog instance, from which the path variable data can be
     *         extracted, if the user closed it with the "OK" button;
     *         <code>null</code>, otherwise (i.e., if the user canceled)
     */
    public static NewPathVariableDialog openNew(PathmapsPreferencePage page) {
        NewPathVariableDialog dlg = new NewPathVariableDialog(page, null, null);

        return (dlg.open() == IDialogConstants.OK_ID) ? dlg
            : null;
    }

    /**
     * Opens the path-variable editing dialog in edit mode, to modify an
     * existing path variable. Clients must account for the possibility that the
     * returned variable name may differ from the specified <code>name</code>,
     * because users may rename variables.
     * 
     * @param page
     *            the path maps preference page that the dialog serves
     * @return the dialog instance, from which the path variable data can be
     *         extracted, if the user closed it with the "OK" button;
     *         <code>null</code>, otherwise (i.e., if the user canceled)
     */
    public static NewPathVariableDialog openEdit(PathmapsPreferencePage page,
            String name, String location) {
        NewPathVariableDialog dlg = new NewPathVariableDialog(page, name,
            location);

        return (dlg.open() == IDialogConstants.OK_ID) ? dlg
            : null;
    }

    /**
     * Sets the dialog's window title according to whether it is in creation or
     * edit mode.
     */
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);

        if (initialName != null) {
            // edit mode
            newShell.setText(EMFUIMessages.PathVariableDialog_editTitle);
        } else {
            // new mode
            newShell.setText(EMFUIMessages.PathVariableDialog_newTitle);
        }
    }

    protected Control createDialogArea(Composite parent) {
        Composite result = (Composite) super.createDialogArea(parent);

        initializeDialogUnits(result);

        Composite composite = new Composite(result, SWT.NONE);
        composite.setLayout(new GridLayout(3, false));
        GridData data = null;
        data = new GridData(GridData.FILL_BOTH);
        data.grabExcessHorizontalSpace = true;
        data.grabExcessHorizontalSpace = true;
        data.horizontalIndent = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        data.verticalIndent = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        composite.setLayoutData(data);

        Label label = new Label(composite, SWT.LEFT);
        label.setText(EMFUIMessages.PathVariableDialog_nameLabel);
        data = new GridData(SWT.BEGINNING);
        label.setLayoutData(data);

        nameText = new Text(composite, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        nameText.setLayoutData(data);
        if (initialName != null) {
            nameText.setText(initialName);
        }

        // blank to occupy the upper-right corner
        new Label(composite, SWT.None);

        label = new Label(composite, SWT.NONE);
        label.setText(EMFUIMessages.PathVariableDialog_locationLabel);
        data = new GridData(SWT.BEGINNING);
        label.setLayoutData(data);

        locationText = new Text(composite, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        locationText.setLayoutData(data);
        if (initialLocation != null) {
            locationText.setText(initialLocation);
        }

        Button browseButton = new Button(composite, SWT.PUSH);
        browseButton.setText(EMFUIMessages.PathVariableDialog_browseButton);
        setButtonLayoutData(browseButton);

        browseButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dlg = new DirectoryDialog(getShell());
                dlg.setText(EMFUIMessages.PathVariableDialog_browseDialogTitle);
                dlg
                    .setMessage(EMFUIMessages.PathVariableDialog_browseDialogMessage);

                String folder = dlg.open();
                if (folder != null) {
                    locationText.setText(folder);
                }
            }
        });

        ModifyListener l = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateInputs();
            }
        };

        nameText.addModifyListener(l);
        locationText.addModifyListener(l);

        if (initialName != null) {
            // edit mode
            setTitle(EMFUIMessages.PathVariableDialog_editTitle);
            
            // select the location field text and set focus to it
            locationText.setSelection(0, locationText.getText().length());
            locationText.setFocus();
        } else {
            // new mode
            setTitle(EMFUIMessages.PathVariableDialog_newTitle);
        }

        setMessage(plainMsg);

        return result;
    }

    /**
     * Initially disables the OK button, because in either creation or edit
     * mode, the user will have to input some data before it can hit OK.
     */
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);

        // initially, the OK button is disabled because we haven't yet any input
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }

    /**
     * Validates the current values of the variable name and location entry
     * fields. A warning or error message is shown in the title area, if
     * appropriate, and the OK button is disabled if any input is invalid.
     * Moreover, in edit mode, the OK button is disabled if the user hasn't
     * changed either the variable name or the location.
     */
    private void validateInputs() {
        IStatus status = Status.OK_STATUS;
        boolean isError = false;

        String name = nameText.getText();
        String location = locationText.getText();
        boolean hasName = name.length() > 0;
        boolean hasLocation = location.length() > 0;

        if (hasName && !name.equals(initialName)) {
            status = validateName(name);
        }

        if (!status.isOK()) {
            isError = true;
            setMessage(status.getMessage(), IMessageProvider.ERROR);
        } else if (hasLocation && !location.equals(initialLocation)) {
            status = validateLocation(new Path(location));
            if (!status.isOK()) {
                isError = status.getSeverity() >= IStatus.ERROR;

                setMessage(status.getMessage(),
                    isError ? IMessageProvider.ERROR
                        : IMessageProvider.WARNING);
            }
        }

        if (status.isOK()) {
            setMessage(plainMsg);
        }

        if (initialName != null) {
            // edit mode. Check that either the name or the location is changed
            if (name.equals(initialName) && location.equals(initialLocation)) {
                // force OK button to be disabled
                hasName = false;
            }
        }

        // dialog not complete if error or missing an input
        getButton(IDialogConstants.OK_ID).setEnabled(
            !isError && hasName && hasLocation);
    }

    /**
     * Validates the specified variable <code>name</code>.
     * 
     * @param name
     *            the variable name to validate
     * @return the result of validation, which may be OK or may contain a
     *         warning or error message to display in the title area
     */
    private IStatus validateName(String name) {
        IStatus result;

        if (pathMgr.isDefined(name) && !page.isRemoved(name)
            || PathmapManager.isRegisteredPathVariable(name)
            || page.isAdded(name)) {
            result = new Status(IStatus.ERROR, MslUIPlugin.getPluginId(),
                EMFUIMessages.PathVariableDialog_alreadyDefined_ERROR_);
        } else if (!URI.validSegment(name)) {
            result = new Status(IStatus.ERROR, MslUIPlugin.getPluginId(),
                EMFUIMessages.PathVariableDialog_invalidSegment_ERROR_);
        } else {
            result = pathMgr.validateName(name);
        }

        return result;
    }

    /**
     * Validates the specified variable <code>location</code>.
     * 
     * @param location
     *            the variable location to validate
     * @return the result of validation, which may be OK or may contain a
     *         warning or error message to display in the title area
     */
    private IStatus validateLocation(IPath location) {
        IStatus result;
        File file = location.toFile();

        if (file.exists() && !file.isDirectory()) {
            result = new Status(IStatus.ERROR, MslUIPlugin.getPluginId(),
                EMFUIMessages.PathVariableDialog_notFolder_ERROR_);
        } else if (!file.exists()) {
            result = new Status(IStatus.ERROR, MslUIPlugin.getPluginId(),
                EMFUIMessages.PathVariableDialog_noSuchFolder_ERROR_);
        } else {
            result = pathMgr.validateValue(location);
        }

        if (result.isOK()) {
            if (page.isLocationDefined(location)) {
                result = new Status(IStatus.WARNING, MslUIPlugin
                    .getPluginId(),
                    EMFUIMessages.PathVariableDialog_sameLocation_WARN_);
            }
        }

        return result;
    }

    /**
     * Stores the variable name and location for retrieval by the client when
     * the user succeeds in closing the dialog by pressing the OK button.
     * 
     * @see #getVariableName()
     * @see #getVariableLocation()
     */
    protected void okPressed() {
        variableName = nameText.getText();
        variableLocation = new Path(locationText.getText());

        super.okPressed();
    }

    /**
     * Obtains the path variable name entered by the user.
     * 
     * @return the path variable name
     */
    String getVariableName() {
        return variableName;
    }

    /**
     * Obtains the path variable location entered by the user.
     * 
     * @return the path variable location
     */
    IPath getVariableLocation() {
        return variableLocation;
    }
}
