/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.commands.core.edithelpers;

import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;

/**
 * Edit helper for modifying model elements using the MSL action protocol.
 * <P>
 * Provides default commands for create, delete, move, and set that use the
 * basic EMF APIs to do their work.
 * <P>
 * Clients using MSL should subclass this edit helper when contributing new
 * element types. Each command will execute using the MSL action protocol.
 * <P>
 * This edit helper is contributed to the <code>ElementTypeRegistry</code> for
 * all <code>EModelElement</code> s that don't specify their own element type.
 * 
 * @author ldamus
 * 
 * @deprecated Use {@link AbstractEditHelper} instead.
 */
public class MSLEditHelper extends AbstractEditHelper {

	// doesn't add any behaviour to the superclass
}