/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

/**
 * The interface to be implemented by all editparts with IView as a model that
 * support editing capabilities
 * 
 * @author Vishy Ramaswamy
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
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