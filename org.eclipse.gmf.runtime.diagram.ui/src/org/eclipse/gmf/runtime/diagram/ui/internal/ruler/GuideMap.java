/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.ruler;

import org.eclipse.gmf.runtime.notation.Guide;


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
