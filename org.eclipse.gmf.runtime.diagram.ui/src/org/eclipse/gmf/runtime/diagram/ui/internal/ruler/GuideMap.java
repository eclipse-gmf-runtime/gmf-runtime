/***************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004.  All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
***************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.ruler;

import com.ibm.xtools.notation.Guide;


/**
 * This is required to support the mapping between views and guides
 *
 * @author jschofie
 */
class GuideMap {
	
	private Guide _horizontal;
	private Guide _vertical;
	
	public Guide getHorizontal() {
		return _horizontal;
	}
	
	public void setHorizontal(Guide toSet) {
		_horizontal = toSet;
	}
	
	public Guide getVertical() {
		return _vertical;
	}
	
	public void setVertical(Guide toSet) {
		_vertical = toSet;
	}
}
