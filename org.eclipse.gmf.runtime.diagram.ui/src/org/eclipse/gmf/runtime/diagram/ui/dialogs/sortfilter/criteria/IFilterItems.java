/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
