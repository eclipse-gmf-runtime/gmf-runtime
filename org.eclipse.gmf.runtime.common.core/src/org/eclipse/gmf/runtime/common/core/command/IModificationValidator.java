/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.command;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;

/**
 * A validator responsible for doing the actual validation on files.  The
 * validation determines whether files may be modified.
 * 
 * @author wdiu, Wayne Diu
 */
public interface IModificationValidator {
	
	/**
	 * Validates whether the array of IFile objects may be modified.
	 * 
	 * @param files an array of the IFile objects that will be modified 
	 * @return IStatus which contains a code describing whether or not the modificationn may proceed
	 */
	public IStatus validateEdit(IFile files[]);
}
