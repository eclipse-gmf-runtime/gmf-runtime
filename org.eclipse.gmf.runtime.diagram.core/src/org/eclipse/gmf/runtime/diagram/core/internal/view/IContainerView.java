/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.view;

import java.util.List;

/**
 * A facade inteface for all connectable views
 * @deprecated View Facades are deprectaed and will be removed soon; use the 
 * notation view instead; you can reach it by calling getModel on a view EditPart
 * or by using the view service to create a new view
 * @author melaasar
 */
public interface IContainerView extends IView {

	/**
	 * Method getChildren.
	 * returns a list of children
	 * @return List of children (empty list if no children)
	 */
	public List getChildren();

	/**
	 * Method getChildViewByIdStr.
	 * returns the first child whose id matched the given id
	 * @param idStr the child's id
	 * @return IView the first matching child or null if no one was found
	 */
	public IView getChildByIdStr(String idStr);
	
	/**
	 * Method insertChild.
	 * inserts the given child at the end of the children collection
	 * @param child
	 * @throws IllegalArgumentException if the child is not of a valid type
	 */
	public void insertChild(IView child);
	
	/**
	 * Method insertChildAt.
	 * inserts the given child at the given index
	 * @param child
	 * @param index (zero-based)
	 * @throws IndexOutOfBoundsException if index is out of bounds
	 * @throws IllegalArgumentException if the child is not of a valid type
	 */
	public void insertChildAt(IView child, int index);
	
    /**
	 * Method repositionChildAt.
	 * reorders the child at the oldIndex to the newIndex
	 * @param view the child to reposition
	 * @param newIndex (zero-based)
	 * @throws IndexOutOfBoundsException if index is out of bounds
	 */
	public void repositionChildAt(IView child, int newIndex);
}
