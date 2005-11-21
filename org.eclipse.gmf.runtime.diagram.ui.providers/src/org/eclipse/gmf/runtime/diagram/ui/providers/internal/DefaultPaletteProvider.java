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

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;


/**
 * The defaul palette provider. It reads XML palette contributions from the
 * provider's extension point and contributes them to an editor's palette based
 * on different contribution criteria
 * 
 * The provider class should not be subclassed since it does its contribution
 * totally from XML However, if programatic contribution is required, then the
 * <code>IPaletteProvider</code> interface should be implemented directly
 * instead
 * 
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 * @deprecated Made public. Use
 *             {@link org.eclipse.gmf.runtime.diagram.ui.providers.DefaultPaletteProvider}.
 *             To be deleted after Dec 21, 2005.
 */
public class DefaultPaletteProvider
	extends org.eclipse.gmf.runtime.diagram.ui.providers.DefaultPaletteProvider {
	// empty
}
