/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.clipboard.core.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Yasser Lulu 
 */
public class MergedEObjectInfo {

	public String mergedEObjectID;

	public EObject mergedEObject;

	public List targetEObjects;

	/**
	 * 
	 */
	public MergedEObjectInfo() {
		targetEObjects = new ArrayList();
	}
}