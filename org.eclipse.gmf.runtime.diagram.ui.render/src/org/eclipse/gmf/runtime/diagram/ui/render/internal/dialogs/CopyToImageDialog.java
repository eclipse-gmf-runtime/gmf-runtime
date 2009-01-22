/******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.internal.dialogs;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.gmf.runtime.common.ui.util.WindowUtil;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;


/**
 * Dialog used by the copy diagram to image file action to prompt the user 
 * for a destination and image format.
 * 
 * @author Anthony Hunter, cmahoney
 */
public class CopyToImageDialog extends Dialog {

	/**
	 * the text entered into the folder text field
	 */
	private String folder = null;

	/**
	 * the text entered into the file name text field
	 */
	private String fileName = null;

	/**
	 * the image format selected in the image format pulldown field
	 */
	private ImageFileFormat imageFormat = null;

	/**
	 * true to overwrite the existing file.
	 */
	private boolean overwriteExisting = false;

	/**
	 * true to export to HTML.
	 */
	private boolean exportToHTML = false;
	
	/**
	 * the folder text field
	 */
	private Text folderText = null;

	/**
	 * the file name text field
	 */
	private Text fileNameText = null;

	/**
	 * the image format pulldown field
	 */
	private Combo imageFormatCombo = null;

	/**
	 * the overwrite existing file checkbox.
	 */
	private Button overwriteExistingCheckbox = null;

	/**
	 * the export to HTML checkbox.
	 */
	private Button exportToHTMLCheckbox = null;
	
	/**
	 * the message image field, displays the error (X) icon when the file 
	 * name or folder is invalid
	 */
	private Label messageImageLabel = null;

	/**
	 * the message field, displays an error message when the file name or 
	 * folder is invalid
	 */
	private Label messageLabel = null;

	/**
	 * true if the contents of the file name text field is a valid file name.
	 */
	private boolean fileNameValid = true;

	/**
	 * true if the contents of the folder text field is a valid folder.
	 */
	private boolean folderValid = true;

	/**
	 * the path argument passed into the dialog.
	 */
	private IPath path;

	/**
	 * the dialog window title
	 */
	private static final String DIALOG_TITLE = DiagramUIMessages.CopyToImageDialog_title;;

	/**
	 * the folder label text
	 */
	private static final String FOLDER_LABEL = DiagramUIMessages.CopyToImageDialog_folder_label; 

	/**
	 * the file name label text
	 */
	private static final String FILE_NAME_LABEL = DiagramUIMessages.CopyToImageDialog_filename_label; 

	/**
	 * the image format label text
	 */
	private static final String IMAGE_FORMAT_LABEL = DiagramUIMessages.CopyToImageDialog_imageformat_label; 

	/**
	 * the browse button text
	 */
	private static final String BROWSE_LABEL = DiagramUIMessages.CopyToImageDialog_browse_label;

	/**
	 * the overwrite existing file checkbox text
	 */
	private static final String OVERWRITE_EXISTING_LABEL = DiagramUIMessages.CopyToImageDialog_overwriteExisting_label;

	/**
	 * the export to HTML file checkbox text
	 */
	private static final String EXPORT_TO_HTML_LABEL = DiagramUIMessages.CopyToImageDialog_exportToHTML_label;

	/**
	 * the directory dialog text
	 */
	private static final String DIRECTORY_DIALOG_TEXT = DiagramUIMessages.CopyToImageDialog_DirectoryDialog_text;

	/**
	 * the directory dialog message
	 */
	private static final String DIRECTORY_DIALOG_MESSAGE = DiagramUIMessages.CopyToImageDialog_DirectoryDialog_message;

	/**
	 * an error message
	 */
	private static final String FOLDER_BLANK_MESSAGE = DiagramUIMessages.CopyToImageDialog_validateFolderText_folderBlank;

	/**
	 * an error message
	 */
	private static final String FOLDER_INVALID_MESSAGE = DiagramUIMessages.CopyToImageDialog_validateFolderText_folderInvalid;

	/**
	 * an error message
	 */
	private static final String FOLDER_NOT_EXIST_MESSAGE = DiagramUIMessages.CopyToImageDialog_validateFolderText_folderNotExist;

	/**
	 * The default image filename.
	 */
	private static final String DEFAULT_IMAGE_FILENAME = DiagramUIMessages.CopyToImageDialog_filename_default;
	
	/**
	 * The empty string.
	 */
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * The string with a dot for creation of a file extension.
	 */
	private static final String DOT_STRING = "."; //$NON-NLS-1$

	/**
	 * The string with a html file extension.
	 */
	private static final String HTML_STRING = "html"; //$NON-NLS-1$

	/**
	 * The id for the persistent settings for this dialog.
	 */
	private static final String DIALOG_SETTINGS_ID = "CopyToImageDialog"; //$NON-NLS-1$

	/**
	 * The id for the persistent folder setting for this dialog.
	 */
	private static final String DIALOG_SETTINGS_FOLDER = "CopyToImageDialog.folder"; //$NON-NLS-1$

	/**
	 * The id for the persistent image format setting for this dialog.
	 */
	private static final String DIALOG_SETTINGS_IMAGE_FORMAT = "CopyToImageDialog.imageFormat"; //$NON-NLS-1$

	/**
	 * The id for the persistent overwrite existing setting for this dialog.
	 */
	private static final String DIALOG_SETTINGS_OVERWRITE = "CopyToImageDialog.overwriteExisting"; //$NON-NLS-1$

	/**
	 * The id for the persistent overwrite existing setting for this dialog.
	 */
	private static final String DIALOG_SETTINGS_HTML = "CopyToImageDialog.exportToHTML"; //$NON-NLS-1$
	
	/**
	 * The root of the filesystem
	 */
	private static final String DEFAULT_FILESYSTEM_ROOT = "/"; //$NON-NLS-1$
	
	/**
	 * Creates an instance of the copy to image dialog.
	 * @param shell the parent shell
	 * @param path the default path to store the image or null
	 * @param fileName A default filename without the file extension or null
	 *  if the default file name should be used.
	 */
	public CopyToImageDialog(Shell shell, IPath path, String fileName) {
		super(shell);
		
		this.path = path;
		
		initDialogSettings();
		
		if (fileName != null) {
			this.fileName = fileName;
		} else {
			this.fileName = DEFAULT_IMAGE_FILENAME;
		}
	}

	/**
	 * Creates and returns the contents of the upper part 
	 * of this dialog (above the button bar).
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		createFolderGroup(composite);
		createFileNameGroup(composite);
		createImageFormatGroup(composite);
		createOverwriteExistingGroup(composite);
		createGenerateHTMLGroup(composite);
		createMessageGroup(composite);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, 
            "org.eclipse.gmf.runtime.diagram.ui.render.egmf0400"); //$NON-NLS-1$

		return composite;
	}

	/**
	 * Configures the shell in preparation for opening this window
	 * in it.
	 * @see org.eclipse.jface.window.Window#configureShell(Shell)
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(DIALOG_TITLE);
	}

	/**
	 *	Create the folder group in the dialog.
	 *	@param parent the parent widget
	 */
	private void createFolderGroup(Composite parent) {
		Composite composite = createComposite(parent, 4);
		createLabel(composite, FOLDER_LABEL);

		folderText = new Text(composite, SWT.BORDER);
		folderText.setText(folder);
		folderText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validateFolderText();
			}
		});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 250;
		folderText.setLayoutData(gridData);

		Button button = new Button(composite, SWT.PUSH);
		button.setText(BROWSE_LABEL);
		button.setLayoutData(WindowUtil.makeButtonData(button));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				handleBrowseButtonPressed();
			}
		});
	}

	/**
	 *	Create the file name group in the dialog.
	 *	@param parent the parent widget
	 */
	private void createFileNameGroup(Composite parent) {
		Composite composite = createComposite(parent, 2);
		createLabel(composite, FILE_NAME_LABEL);

		fileNameText = new Text(composite, SWT.BORDER);
		updateFileNameText(false);
		fileNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validateFileNameText();
			}
		});
		GridData gridData =
			new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		gridData.widthHint = 250;
		fileNameText.setLayoutData(gridData);
	}

	/**
	 *	Create the image format group in the dialog.
	 *	@param parent the parent widget
	 */
	private void createImageFormatGroup(Composite parent) {
		Composite composite = createComposite(parent, 2);
		createLabel(composite, IMAGE_FORMAT_LABEL);

		imageFormatCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		imageFormatCombo.setItems(getImageFormatItems());
		imageFormatCombo.setText(imageFormat.getName());
		imageFormatCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				imageFormat =
					ImageFileFormat.resolveImageFormat(
						imageFormatCombo.getSelectionIndex());
                
                // update filename to reflect new format
				if (!exportToHTML) {
					updateFileNameText(true);
				}
			}
		});
		GridData gridData =
			new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL);
		gridData.widthHint = 250;
		imageFormatCombo.setLayoutData(gridData);

	}
	
	private void updateFileNameText(boolean validate) {
		String extension = exportToHTML ? HTML_STRING : imageFormat.getName().toLowerCase();
		fileNameText.setText(fileName + DOT_STRING + extension);
		if (validate)
			validateFileNameText();
	}

	/**
	 * Create the overwrite existing file group in the dialog.
	 * @param parent the parent widget
	 */
	private void createOverwriteExistingGroup(Composite parent) {
		Composite composite = createComposite(parent, 1);

		overwriteExistingCheckbox = new Button(composite, SWT.CHECK | SWT.LEFT);
		overwriteExistingCheckbox.setText(OVERWRITE_EXISTING_LABEL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		overwriteExistingCheckbox.setLayoutData(data);
		overwriteExistingCheckbox.setSelection(overwriteExisting);
		overwriteExistingCheckbox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				overwriteExisting = overwriteExistingCheckbox.getSelection();
			}
		});
	}

	private void createGenerateHTMLGroup(Composite parent) {
		Composite composite = createComposite(parent, 1);
		exportToHTMLCheckbox = new Button(composite, SWT.CHECK | SWT.LEFT);
		exportToHTMLCheckbox.setText(EXPORT_TO_HTML_LABEL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		exportToHTMLCheckbox.setLayoutData(data);
		exportToHTMLCheckbox.setSelection(exportToHTML);
		exportToHTMLCheckbox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				exportToHTML = exportToHTMLCheckbox.getSelection();
				updateFileNameText(false);
			}
		});
	}

	/**
	 *	Create the message group in the dialog used to display error messages.
	 *	@param parent the parent widget
	 */
	private void createMessageGroup(Composite parent) {
		Composite composite = createComposite(parent, 2);

		messageImageLabel = new Label(composite, SWT.NONE);
		messageImageLabel.setImage(
			JFaceResources.getImage(DLG_IMG_MESSAGE_ERROR));
		messageImageLabel.setVisible(false);

		messageLabel = new Label(composite, SWT.NONE);
		GridData gridData =
			new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL);
		gridData.widthHint = 250;
		messageLabel.setLayoutData(gridData);
		messageLabel.setVisible(false);
	}

	/**
	 * utility method to create a composite widget
	 * @param parent the parent widget
	 * @param columns the number of columns in the grid layout for the new
	 * composite.
	 * @return the new composite widget
	 */
	private Composite createComposite(Composite parent, int columns) {
		Composite composite = new Composite(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = columns;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.makeColumnsEqualWidth = false;

		GridData data =
			new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL);

		composite.setLayoutData(data);
		composite.setLayout(gridLayout);

		return composite;
	}

	/**
	 * utility method to create a label widget
	 * @param parent the parent widget
	 * @param text the text for the label
	 * @return the new label widget
	 */
	private Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		return label;
	}

	/**
	 * get the supported image formats from the enumerated type.
	 * @return array of supported image formats.
	 */
	private String[] getImageFormatItems() {
		String[] items = new String[ImageFileFormat.VALUES.length];
		for (int i = 0; i < ImageFileFormat.VALUES.length; i++) {
			items[i] = ImageFileFormat.VALUES[i].getName();
		}
		return items;
	}

	/**
	 * Returns the destination image file selected by the user.
	 * @return folder and filename, including image format extension, to
	 * the selected destination image file.
	 */
	public IPath getDestination() {
		StringBuffer extension = new StringBuffer(DOT_STRING);
		if (!exportToHTML) {
			extension.append(imageFormat.getName().toLowerCase());
		} else {
			extension.append(HTML_STRING);
		}
		StringBuffer f = new StringBuffer(fileName);
		if (!f.toString().endsWith(extension.toString())) {
			f.append(extension);
		}
		return new Path(folder).append(f.toString());
	}

	/**
	 * Returns the destination image file format selected by the user.
	 * @return the selected image file format.
	 */
	public ImageFileFormat getImageFormat() {
		return imageFormat;
	}

	/**
	 * Returns if the existing file should be overwritten without warning.
	 * @return true if the existing file should be overwritten without warning.
	 */
	public boolean overwriteExisting() {
		return overwriteExisting;
	}
	
	public boolean exportToHTML() {
		return exportToHTML;
	}

	/**
	 * handle a browse button pressed selection.
	 */
	private void handleBrowseButtonPressed() {
		DirectoryDialog dialog =
			new DirectoryDialog(Display.getCurrent().getActiveShell());
		dialog.setMessage(DIRECTORY_DIALOG_MESSAGE);
		dialog.setText(DIRECTORY_DIALOG_TEXT);

		String dirName = folderText.getText();
		if (!dirName.equals(EMPTY_STRING)) {
			File aPath = new File(dirName);
			if (aPath.exists())
				dialog.setFilterPath(new Path(dirName).toOSString());
		}

		String selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			folderText.setText(selectedDirectory);
		}
	}

	/**
	 * validate the folder text field.
	 */
	private void validateFolderText() {

		if (folderText.getText().equals(EMPTY_STRING)) {
			setDialogErrorState(FOLDER_BLANK_MESSAGE);
			folderValid = false;
			return;
		}

		IPath aPath = new Path(EMPTY_STRING);
		if (!aPath.isValidPath(folderText.getText())) {
			setDialogErrorState(FOLDER_INVALID_MESSAGE);
			folderValid = false;
			return;
		}

		File file = new File(folderText.getText());
		if (!file.exists()) {
			setDialogErrorState(FOLDER_NOT_EXIST_MESSAGE);
			folderValid = false;
			return;
		}

		folderValid = true;
		folder = folderText.getText();
		if (fileNameValid) {
			setDialogOKState();
		} else {
			validateFileNameText();
		}
	}

	/**
	 * validate the file name text field.
	 */
	private void validateFileNameText() {
		IStatus nameStatus =
			ResourcesPlugin.getWorkspace().validateName(
				fileNameText.getText(),
				IResource.FILE);

		if (!nameStatus.isOK()) {
			setDialogErrorState(nameStatus.getMessage());
			fileNameValid = false;
			return;
		}

		fileNameValid = true;
        
        IPath filePath = (new Path(fileNameText.getText())).removeFileExtension();
		fileName = filePath.toString();
		if (folderValid) {
			setDialogOKState();
		} else {
			validateFolderText();
		}
	}

	/**
	 * Set the dialog into error state mode. The error image (x) label and 
	 * error label are made visible and the ok button is disabled.
	 * @param message the error message
	 */
	private void setDialogErrorState(String message) {
		messageLabel.setText(message);
		messageImageLabel.setVisible(true);
		messageLabel.setVisible(true);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		getButton(IDialogConstants.CANCEL_ID).getShell().setDefaultButton(
			getButton(IDialogConstants.CANCEL_ID));
	}

	/**
	 * Set the dialog into ok state mode. The error image (x) label and 
	 * error label and made not visible and the ok button is enabled.
	 */
	private void setDialogOKState() {
		messageImageLabel.setVisible(false);
		messageLabel.setVisible(false);
		getButton(IDialogConstants.OK_ID).setEnabled(true);
		getButton(IDialogConstants.OK_ID).getShell().setDefaultButton(
			getButton(IDialogConstants.OK_ID));
	}

	/**
	 * Retrieves the persistent settings for this dialog.
	 * @return the persistent settings for this dialog.
	 */
	private IDialogSettings getDialogSettings() {
		IDialogSettings settings = DiagramUIRenderPlugin.getInstance()
			.getDialogSettings();
		settings = settings.getSection(DIALOG_SETTINGS_ID);
		if (settings == null)
			settings = DiagramUIRenderPlugin.getInstance().getDialogSettings()
				.addNewSection(DIALOG_SETTINGS_ID);
		return settings;
	}

	/**
	 * Initialize the settings for this dialog.
	 */
	private void initDialogSettings() {
		IDialogSettings dialogSettings = getDialogSettings();

		String persistentFolder = dialogSettings.get(DIALOG_SETTINGS_FOLDER);
		if (persistentFolder != null) {
			folder = persistentFolder;
		} else {

			if (path == null) {
				// By default, the folder will be the root of the filesystem,
				// where ever that may be on a system.
				folder = DEFAULT_FILESYSTEM_ROOT;
			} else {
				folder = path.toOSString();
			}
		}

		String persistentImageFormat =
			dialogSettings.get(DIALOG_SETTINGS_IMAGE_FORMAT);
		if (persistentImageFormat == null) {
			imageFormat = ImageFileFormat.getDefaultImageFormat();
		} else {
			imageFormat =
				ImageFileFormat.resolveImageFormat(persistentImageFormat);
		}

		overwriteExisting =
			dialogSettings.getBoolean(DIALOG_SETTINGS_OVERWRITE);
		exportToHTML =
			dialogSettings.getBoolean(DIALOG_SETTINGS_HTML);
	}

	/**
	 * Retrieves the persistent settings for this dialog.
	 */
	private void saveDialogSettings() {
		IDialogSettings dialogSettings = getDialogSettings();
		if (path == null && !folder.trim().equals(DEFAULT_FILESYSTEM_ROOT))
			dialogSettings.put(DIALOG_SETTINGS_FOLDER, folder);
		else {
			if (!path.toOSString().equals(folder)) {
				// only persist the folder if the user changed the value.
				// We like to save diagrams in a folder different to the
				// <workspace>/project_name folder and have the Save As
				// Image File dialog remember this setting and display
				// it as the new default.
				dialogSettings.put(DIALOG_SETTINGS_FOLDER, folder);
			}
		}
		dialogSettings.put(
			DIALOG_SETTINGS_IMAGE_FORMAT,
			imageFormat.getName().toLowerCase());
		dialogSettings.put(DIALOG_SETTINGS_OVERWRITE, overwriteExisting);
		dialogSettings.put(DIALOG_SETTINGS_HTML, exportToHTML);
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		super.okPressed();
		saveDialogSettings();
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control abuttonBar = super.createButtonBar(parent);
		validateFolderText();
		return abuttonBar;
	}

}

