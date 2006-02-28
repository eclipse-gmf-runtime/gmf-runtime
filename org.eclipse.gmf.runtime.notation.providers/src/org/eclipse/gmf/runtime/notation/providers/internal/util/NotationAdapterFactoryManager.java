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

package org.eclipse.gmf.runtime.notation.providers.internal.util;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLAdapterFactoryManager;
import org.eclipse.gmf.runtime.emf.core.internal.util.MetamodelManager;
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

		MetamodelManager.register(NotationPackage.eINSTANCE,
			NotationEditPlugin.INSTANCE);

		MSLAdapterFactoryManager.register(notationFactory);
	}
}