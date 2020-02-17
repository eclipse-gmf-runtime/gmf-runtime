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

package org.eclipse.gmf.runtime.common.ui.services.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.services.internal.marker.IMarkerNavigationProvider;

/**
 * This class contains the information needed to perform the navigation to an
 * Eclipse marker. It contains getters for the parameters for the
 * MarkerNavigationService.gotoMarker() method.
 * 
 * @author Kevin Cornell
 */
public class GotoMarkerOperation
	implements IOperation {

	/** Remember the editor instance opened for the marker's resource. */
	private IEditorPart editor;

	/** Remember the marker reference. */
	private IMarker marker;

	/**
	 * Constructor - Create and save the goto marker operation.
	 * <p>
	 * 
	 * @param anEditor
	 *            the editor instance created for the marker's resource
	 * @param aMarker
	 *            the marker information
	 */
	public GotoMarkerOperation(IEditorPart anEditor, IMarker aMarker) {
		assert null != anEditor;
		assert null != aMarker;

		this.editor = anEditor;
		this.marker = aMarker;
	}

	/**
	 * Retrieve the editor instance
	 * 
	 * @return the editor
	 */
	public IEditorPart getEditor() {
		return editor;
	}

	/**
	 * Retrieve the marker reference
	 * 
	 * @return the marker
	 */
	public IMarker getMarker() {
		return marker;
	}

	/**
	 * Execute the operation for the given provider
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		if (provider instanceof IMarkerNavigationProvider) {
			((IMarkerNavigationProvider) provider).gotoMarker(getEditor(),
				getMarker());
		}
		return null;
	}

}