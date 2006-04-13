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

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n;


/**
 * Messages for the Editor.
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

	public static String Editor_error_no_provider;
	public static String Editor_error_save_title;
	public static String Editor_error_save_message;
	public static String Editor_error_save_deleted_title;
	public static String Editor_error_save_deleted_message;
	public static String Editor_error_init;
	public static String Editor_error_save_outofsync_title;
	public static String Editor_error_save_outofsync_message;
	public static String Editor_error_activated_outofsync_title;
	public static String Editor_error_activated_outofsync_message;
	public static String Editor_error_activated_deleted_save_title;
	public static String Editor_error_activated_deleted_save_message;
	public static String Editor_error_activated_deleted_save_button_save;
	public static String Editor_error_activated_deleted_save_button_close;
	public static String Editor_error_activated_deleted_close_title;
	public static String Editor_error_activated_deleted_close_message;
	public static String Editor_error_refresh_outofsync_title;
	public static String Editor_error_refresh_outofsync_message;
	public static String Editor_error_revert_title;
	public static String Editor_error_revert_message;
	public static String Editor_error_setinput_title;
	public static String Editor_error_setinput_message;
	public static String Editor_error_validateEdit_title;
	public static String Editor_error_validateEdit_message;
	public static String Editor_error_open_message;
	public static String Editor_error_create_file_title;
	public static String Editor_error_create_file_message;
	public static String Editor_error_dialog_internal_message;
	public static String AbstractDocumentProvider_error_save_inuse;
	public static String Editor_statusline_state_readonly_label;
	public static String Editor_statusline_state_writable_label;
	public static String Editor_statusline_mode_insert_label;
	public static String Editor_statusline_mode_overwrite_label;
	public static String Editor_statusline_mode_smartinsert_label;
	public static String Editor_statusline_position_pattern;
	public static String Editor_statusline_error_label;
	public static String AbstractDocumentProvider_ok;
	public static String AbstractDocumentProvider_error;
	public static String DocumentProviderRegistry_error_extension_point_not_found;

	public static String DiagramInputDocumentProvider_createElementInfo;
	public static String DiagramInputDocumentProvider_updateCache;
	public static String DiagramInputDocumentProvider_isReadOnly;
	public static String DiagramInputDocumentProvider_isModifiable;
	public static String Editor_error_saveas_title;
	public static String Editor_warning_save_delete;
	public static String NullProvider_error;
	public static String Editor_error_saving_message1;
	public static String Editor_error_saving_message2;
	public static String Editor_error_saving_title1;
	public static String Editor_error_saving_title2;
	
	public static String Diagram_UNABLE_TO_LOAD_RESOURCE;
	public static String Diagram_NO_DIAGRAM_IN_RESOURCE;


	static {
		NLS.initializeMessages(BUNDLE_NAME, EditorMessages.class);
	}
}
