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
