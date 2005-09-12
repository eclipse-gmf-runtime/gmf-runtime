/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
