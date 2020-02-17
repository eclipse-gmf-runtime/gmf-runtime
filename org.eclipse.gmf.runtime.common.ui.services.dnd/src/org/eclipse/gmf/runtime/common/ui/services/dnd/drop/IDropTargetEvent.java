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

package org.eclipse.gmf.runtime.common.ui.services.dnd.drop;

import org.eclipse.swt.dnd.TransferData;

/**
 * Interface that contains the information relevant to the drop target event.
 * 
 * @author Vishy Ramaswamy
 */
public interface IDropTargetEvent {

	/**
	 * The operation being performed.
	 * 
	 * @see org.eclipse.swt.dnd.DND#DROP_NONE
	 * @see org.eclipse.swt.dnd.DND#DROP_MOVE
	 * @see org.eclipse.swt.dnd.DND#DROP_COPY
	 * @see org.eclipse.swt.dnd.DND#DROP_LINK
	 * @return int constant of operation being performed
	 */
	public int getCurrentOperation();

	/**
	 * A bitwise OR'ing of the operations that the DragSource can support (e.g.
	 * DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK). The detail value must be
	 * a member of this list or DND.DROP_NONE.
	 * 
	 * @see org.eclipse.swt.dnd.DND#DROP_NONE
	 * @see org.eclipse.swt.dnd.DND#DROP_MOVE
	 * @see org.eclipse.swt.dnd.DND#DROP_COPY
	 * @see org.eclipse.swt.dnd.DND#DROP_LINK
	 * @return int of bitwise or of supported operations
	 */
	public int getOperations();

	/**
	 * A list of the types of data that the DragSource is capable of providing.
	 * The currentDataType must be a member of this list.
	 * 
	 * @return TransferData[] array of the types of data that the DragSource is
	 *         capable of providing.
	 */
	public TransferData[] getDataTypes();

	/**
	 * The type of data that will be dropped.
	 * 
	 * @return TransferData representation of the data that will be dropped
	 */
	public TransferData getCurrentDataType();
}