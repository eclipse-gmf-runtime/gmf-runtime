/******************************************************************************
 * Copyright (c) 2002, 2023 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.figures;
/**
 * @author choang
 *
 *<br>Implements the ILayoutManager interface using the gravity Layout
 *<br>algorithm.   The first child in the parent figure will be laid out
 *<br>in the direction of the gravity.  @see #setGravity(GravityDirection)
 *<br> for more details.
 * 
 */
public class GravityConstrainedFlowLayout extends ConstrainedToolbarLayout {

	GravityDirectionType gravity = GravityDirectionType.WEST;


    /**
	* Method setGravity will update the gravity used for the layout.
	* <br>For example if the gravity is set to EAST for a figure that has 3 
	* <br>children [1][2][3] then the children
	* <br>will be layout out as follows [3][2][1].  If ther gravity is North then
	* <br>the same figure children wil be laid out as follow:<br>
	* <br>[1]
	* <br>[2]
	* <br>[3]
	* <br>with the child figure [1] at the top.
	* 
	* @param gravity the enumeration <code>GravityDirectionType</code> indicating 
	* which direction the flow layout is oriented.
	*/
	public void setGravity(GravityDirectionType gravity) {
		this.gravity = gravity;

		if (gravity == GravityDirectionType.SOUTH) {
			setHorizontal(false);
			setReversed(true);
		} else if (gravity == GravityDirectionType.EAST) {
		    setHorizontal(true);
			setReversed(true);
		} else if (gravity == GravityDirectionType.NORTH) {
		    setHorizontal(false);
			setReversed(false);
		} else if (gravity == GravityDirectionType.WEST) {
		    setHorizontal(true);
			setReversed(false);
		}

	}

	/**
	 * Method getGravity used for this layout
	 * @return GravityDirectionType
	 */
	public GravityDirectionType getGravity() {
		return this.gravity;
	}	

}
