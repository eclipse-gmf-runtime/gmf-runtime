/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.internal.command;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.gmf.runtime.common.core.command.IModificationValidator;

/**
 * A validator responsible for doing the actual validation on files.  The
 * validation determines whether files may be modified.  It delegates to
 * IWorkspace's validateEdit.
 * 
 * @author wdiu, Wayne Diu
 */
public class BaseModificationValidator implements IModificationValidator {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.IModificationValidator#validateEdit(org.eclipse.core.resources.IFile[])
	 */
	public IStatus validateEdit(IFile[] files) {
		return ResourcesPlugin.getWorkspace().validateEdit(
            files,
            null);
	}

}
