/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

/**
 * A list of keys defining the GMF diagram editpolicy roles. An editpolicy is
 * installed on an editpart using a role (a String identifier), if another
 * editpolicy is installed on the same editpart with the same role then it will
 * override the previous one installed. If an editpolicy is installed that may
 * need to be overridden, add its role here.
 * 
 * @author cmahoney
 */
public interface EditPolicyRoles {

	/**
	 * The key used to install a <i>decoration</i> EditPolicy, one that handles
	 * creation and removal of decorations.
	 */
	public static final String DECORATION_ROLE = "DecorationPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>drop element</i> EditPolicy.
	 */
	public static final String DRAG_DROP_ROLE = "DragDropPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>connection handles</i> EditPolicy, one that
	 * adds or changes the behavior of connection handles.
	 */
	public static final String CONNECTION_HANDLES_ROLE = "ConnectionHandlesPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>property handler</i> EditPolicy, one that
	 * handles property change requests.
	 */
	public static final String PROPERTY_HANDLER_ROLE = "PropertyHandlerPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install an <i>open</i> EditPolicy. The OPEN_ROLE policy
	 * is typically installed on edit part whose underlying data can only be
	 * examined/modified by opening another editor window. For example, if an
	 * edit part represented another diagram, then an "open" request on that
	 * edit part should result in the corresponding diagram being opened in
	 * another editor window.
	 */
	public static final String OPEN_ROLE = "OpenPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>show/hide connection labels</i> EditPolicy
	 */
	public static final String CONNECTION_LABELS_ROLE = "ConnectionLabelsPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>semantic</i> EditPolicy.
	 */
	public static final String SEMANTIC_ROLE = "SemanticPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>refresh pagebreaks</i> EditPolicy.
	 */
	public static final String REFRESH_PAGEBREAKS_ROLE = "RefreshPagebreaksPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>refresh connections</i> EditPolicy.
	 */
	public static final String REFRESH_CONNECTIONS_ROLE = "RefreshConnectionsPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>show elements</i> EditPolicy, one that
	 * handles Show Related Elements and Show / Hide Relationships.
	 */
	public static final String SHOW_ELEMENTS_ROLE = "ShowElementsPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>sort filter</i> EditPolicy. The sort filter
	 * role creates and opens the sort filter dialog if it is given content by
	 * its children via the sort filter content role.
	 */
	public static final String SORT_FILTER_ROLE = "SortFilterPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>sort filter</i> content EditPolicy. The
	 * sort filter content role understands how to provide content for the sort
	 * filter dialog.
	 */
	public static final String SORT_FILTER_CONTENT_ROLE = "Sortfilter_contentPolicy"; //$NON-NLS-1$

	/**
	 * The key used by edit policies which modify sorting and filtering.
	 */
	public static final String MODIFY_SORT_FILTER_ROLE = "ModifySortFilterPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>popup bar</i> EditPolicy. The popup bar
	 * will be activated during mouse hover for the editpart.
	 */
	public static final String POPUPBAR_ROLE = "PopupBarEditPolicy"; //$NON-NLS-1$

	/**
	 * Key used to install a canonical edit policy. This edit policy will ensure
	 * that the host's model children are in sync with a specifc set of semantic
	 * children.
	 */
	public static final String CANONICAL_ROLE = "Canonical"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>ShowAsAlternateViewPolicy</i> EditPolicy
	 * which understands notational requests such as:
	 * <code>REQ_SHOW_AS_ALTERNATE_VIEW</code>
	 */
	public static final String SHOW_ALTERNATE_VIEW_ROLE = "ShowAsAlternateViewPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>creation</i> EditPolicy which understands
	 * notational requests such as: <code>REQ_CREATE</code> where the request
	 * is of type <code>CreateViewRequest</code>
	 */
	public static final String CREATION_ROLE = "CreationPolicy"; //$NON-NLS-1$

	/**
	 * The key used to install a <i>SnapFeedbackPolicy</i> EditPolicy which
	 * understands how to handle snap feedback when shapes are moved and
	 * snapping is enabled
	 */
	public static final String SNAP_FEEDBACK_ROLE = "Snap Feedback"; //$NON-NLS-1$

	/**
	 * The key used to install an <i>TetherConnectionEditPolicy</i> which
	 * provides a permanent tether between a connection and a shape.
	 */
	public static final String TETHER_ROLE = "TetherRole"; //$NON-NLS-1$
	
}
