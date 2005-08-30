/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.editor;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * An interface for manipulating editors
 * 
 * @author melaasar
 */
public interface IEditorProvider
	extends IProvider {

	/**
	 * Opens an editor with the given editor input
	 * 
	 * @param editorInput
	 *            the editor input object
	 * @return the opened IEditorPart
	 */
	public IEditorPart openEditor(IEditorInput editorInput);

}