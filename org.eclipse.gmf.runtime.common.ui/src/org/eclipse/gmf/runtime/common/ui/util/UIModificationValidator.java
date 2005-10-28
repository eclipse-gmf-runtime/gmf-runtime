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

package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import org.eclipse.gmf.runtime.common.core.command.IModificationValidator;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;
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
			CommonUIMessages.UIModificationValidator_ModificationMessage);

		return ok ? Status.OK_STATUS
			: ERROR_STATUS;
	}

}