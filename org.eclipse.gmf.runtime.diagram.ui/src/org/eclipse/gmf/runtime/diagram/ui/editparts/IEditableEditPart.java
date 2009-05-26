/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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