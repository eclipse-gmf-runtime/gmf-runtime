/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import org.eclipse.gmf.runtime.common.core.command.IModificationValidator;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.common.ui.resources.FileModificationValidator;

/**
 * A validator responsible for doing the actual validation on files. The
 * validation determines whether files may be modified. It delegates to
 * IWorkspace's validateEdit and supplies a UI context from the active shell if
 * available.
 * 
 * @author wdiu, Wayne Diu
 */
public class UIModificationValidator
	implements IModificationValidator {

	/**
	 * Modification message
	 */
	private static final String MODIFICATION = ResourceManager
		.getI18NString("UIModificationValidator.ModificationMessage"); //$NON-NLS-1$

	/**
	 * Error status code. The OK status code is defined by Eclipse's Status
	 * class.
	 */
	private static final Status ERROR_STATUS = new Status(Status.ERROR,
		Platform.PI_RUNTIME, 1, StringStatics.BLANK, null);

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.IModificationValidator#validateEdit(org.eclipse.core.resources.IFile[])
	 */
	public IStatus validateEdit(IFile[] files) {
		boolean ok = FileModificationValidator.getInstance().okToEdit(files,
			MODIFICATION);

		return ok ? Status.OK_STATUS
			: ERROR_STATUS;
	}

}