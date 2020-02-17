/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal.requests;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextManager;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;

/**
 * @author Yasser Lulu
 *
 */
public final class RequestCacheEntries {
	

	public static final String Cache_Maps = "Cache_Maps";//$NON-NLS-1$

	public static final String Client_Context = "Client_Context";//$NON-NLS-1$

	public static final String Element_Type = "Element_Type";//$NON-NLS-1$	

	public static final String EditHelper_Advice = "EditHelper_Advice";//$NON-NLS-1$

	public static final String Affected_Files = "Affected_Files";//$NON-NLS-1$

	public static final String CrossRefAdapter = "CrossRefAdapter";//$NON-NLS-1$
	
	public static final String Checked_Elements = "Checked_Elements";//$NON-NLS-1$
	public static final String Dependent_Elements = "Dependent_Elements";//$NON-NLS-1$
	
	private RequestCacheEntries() {
		//
	}

	public static final void initializeEObjCache(EObject eObj, Map map) {
		IClientContext clientContext = ClientContextManager.getInstance()
			.getClientContextFor(eObj);
		map.put(Client_Context, clientContext);
		IElementType type = ElementTypeRegistry.getInstance().getElementType(
			eObj, clientContext);
		map.put(Element_Type, type);
		IEditHelperAdvice[] advices = ElementTypeRegistry.getInstance()
			.getEditHelperAdvice(eObj, clientContext);
		map.put(EditHelper_Advice, advices);		
	}

}
