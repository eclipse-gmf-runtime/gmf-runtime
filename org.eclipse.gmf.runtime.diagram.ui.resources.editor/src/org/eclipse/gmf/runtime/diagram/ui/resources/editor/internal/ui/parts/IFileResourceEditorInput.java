/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.ui.parts;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;


/**
 * An editor input that is file based.
 * 
 * @author wdiu, Wayne Diu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.resources.editor.*
 * @deprecated
 */
public interface IFileResourceEditorInput extends IEditorInput {
	
	/**
	 * The IFile for this editor input
	 */
	public IFile getFile();
}
