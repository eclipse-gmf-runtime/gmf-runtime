/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.clipboard.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A data structure that holds info needed through the copy process
 * @author Yasser Lulu 
 */
class CopyObjects {

	Collection originalObjects;

	Map copyParent2CopyMap = new HashMap();

	Map copyParent2ObjectsMap = new HashMap();

	Map copyAlwaysMap = new HashMap();

	Set combinedCopyAlwaysSet = new HashSet();

	Map objectsToCopyParentMap = new HashMap();

	Set totalCopyObjects = new HashSet();

	/**
	 * Initializes me with my original objects.
	 * 
	 * @param originalObjects the objects originally selected for copying
	 */
	public CopyObjects(Collection originalObjects) {
		this.originalObjects = originalObjects;
	}
	
	void clear(){
		originalObjects.clear();
		copyParent2CopyMap.clear();
		copyAlwaysMap.clear();
		combinedCopyAlwaysSet.clear();
		objectsToCopyParentMap.clear();
		totalCopyObjects.clear();		
	}

}