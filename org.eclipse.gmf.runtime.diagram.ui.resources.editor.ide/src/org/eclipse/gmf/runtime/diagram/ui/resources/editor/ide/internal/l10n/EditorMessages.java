/******************************************************************************
 * Copyright (c) 2000, 2005  IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n;


/**
 * Messages for the IDE Editor.
 * @author mgoyal
 *
 */
import org.eclipse.osgi.util.NLS;

/**
 * Helper class to get NLSed messages.
 */
final public class EditorMessages extends NLS {
	private static final String BUNDLE_NAME= EditorMessages.class.getName();

	private EditorMessages() {
		// Do not instantiate
	}

	public static String FileDocumentProvider_createElementInfo;
	public static String FileDocumentProvider_error_out_of_sync;
	public static String FileDocumentProvider_task_saving;
	public static String FileDocumentProvider_resetDocument;
	public static String FileDocumentProvider_resourceChanged;
	public static String FileDocumentProvider_handleElementContentChanged;
	
	public static String StorageDocumentProvider_createElementInfo;
	public static String StorageDocumentProvider_updateCache;
	public static String StorageDocumentProvider_isReadOnly;
	public static String StorageDocumentProvider_isModifiable;
	
	public static String WizardPage_DIAGRAM_CREATION_FAIL_EXC_;

	public static String WizardPage_Message_FileExists_ERROR_;

	public static String FileCreator_TaskTitle;
	public static String EditorWizardPage_DialogInternalErrorTitle;
	public static String EditorWizardPage_InvalidFilename; 
	
	public static String FileSaveAs_DialogTitle;
	public static String FileSaveAs_DialogMessageText;


	static {
		NLS.initializeMessages(BUNDLE_NAME, EditorMessages.class);
	}
}
