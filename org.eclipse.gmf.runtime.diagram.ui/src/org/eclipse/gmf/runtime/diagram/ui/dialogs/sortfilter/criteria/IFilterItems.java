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

package org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter.criteria;

import java.util.Map;

/**
 * Interface for describing filtering criteria for the Sort/Filter
 * dialog.
 * 
 * @author jcorchis
 */
public interface IFilterItems {

	/**
	 * A filter must consist of a <code>Map</code>. The keys are the 
	 * <code>String</code>s appearing in the Sort/Filter dialog filtering lists.
	 * The values are <code>Integer</code>s whose values are bit-wise distinct. 
	 * @return map 
	 */
	public Map getFilterMap();

}
