/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.editor;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesPlugin;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesStatusCodes;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * An abstract implementation of an editor provider
 * 
 * @author melaasar
 */
public abstract class AbstractEditorProvider
	extends AbstractProvider
	implements IEditorProvider {

	/**
	 * Opens an editor with the given editor input
	 * 
	 * @param editorInput
	 *            the editor input object
	 * 
	 * @see IEditorProvider#openEditor(IEditorInput)
	 */
	public IEditorPart openEditor(IEditorInput editorInput) {
		try {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().openEditor(editorInput,
					getEditorId(editorInput));
		} catch (PartInitException e) {
			Trace
				.catching(CommonUIServicesPlugin.getDefault(),
					CommonUIServicesDebugOptions.EXCEPTIONS_CATCHING,
					CommonUIServicesPlugin.getDefault().getClass(),
					"openEditor", e); //$NON-NLS-1$
			Log.error(CommonUIServicesPlugin.getDefault(),
				CommonUIServicesStatusCodes.SERVICE_FAILURE, "openEditor", e); //$NON-NLS-1$
			return null;
		}
	}

	/**
	 * Determines if the provider can handle the given editor operation
	 * 
	 * @param operation
	 *            the given operation
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		return (operation instanceof OpenEditorOperation)
			&& canOpen(((OpenEditorOperation) operation).getEditorInput());
	}

	/**
	 * Returns the Editor id suitable for the given editor input
	 * 
	 * @param editorInput
	 *            the given editor input
	 * @return String the editor id for the given editor input
	 */
	abstract protected String getEditorId(IEditorInput editorInput);

	/**
	 * Method Determines if the editor provider can open an editor for the given
	 * editor input
	 * 
	 * @param editorInput
	 *            the given editor input
	 * @return boolean whether the provider can open an editor with the given
	 *         input
	 */
	abstract protected boolean canOpen(IEditorInput editorInput);

}