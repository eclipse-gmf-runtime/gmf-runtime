/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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