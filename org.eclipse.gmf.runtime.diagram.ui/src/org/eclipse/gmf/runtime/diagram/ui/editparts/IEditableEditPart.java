/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

/**
 * The interface to be implemented by all editparts with IView as a model that
 * support editing capabilities
 * 
 * @author Vishy Ramaswamy
 * @since 1.2
 */
public interface IEditableEditPart {
	/**
	 * Returns <code>true</code> if the EditPart is editable. Editparts are
	 * editable after {@link #enableEditMode()}is called, and until
	 * {@link #disableEditMode()}is called.
	 * 
	 * @return <code>true</code> when editable
	 */
	public boolean isEditModeEnabled();

	/**
	 * Disables edit mode of the EditPart. EditParts that are not editable do
	 * not provide commands to any requests.
	 */
	public void disableEditMode();

	/**
	 * Enables edit mode of the EditPart. EditParts that are editable provide
	 * commands to requests.
	 */
	public void enableEditMode();
}