/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.notation.providers.internal.util;

import org.eclipse.emf.common.notify.AdapterFactory;

import org.eclipse.gmf.runtime.emf.core.internal.util.MSLAdapterFactoryManager;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLMetaModelManager;
import org.eclipse.gmf.runtime.notation.NotationEditPlugin;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.provider.NotationItemProviderAdapterFactory;

/**
 * UML2 and notation adapter factories.
 * 
 * @author rafikj
 */
public class NotationAdapterFactoryManager {

	private static AdapterFactory notationFactory = new NotationItemProviderAdapterFactory();

	/**
	 * Init factories.
	 */
	public static void init() {

		MSLMetaModelManager.register(NotationPackage.eINSTANCE,
			NotationEditPlugin.INSTANCE);

		MSLAdapterFactoryManager.register(notationFactory);
	}
}