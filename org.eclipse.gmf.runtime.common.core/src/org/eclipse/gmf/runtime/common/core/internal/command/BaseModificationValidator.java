/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
