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

package org.eclipse.gmf.runtime.common.core.util;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Tests if an element is updatable
 * 
 * @author Michael Yee
 * @canBeSeenBy %partners
 */
public interface IUpdateable {
    /**
     * Tests if an element is updatable
     * @param adaptable element to test
     * @return boolean true if element is updatable, otherwise false
     */
    public boolean isUpdateable(IAdaptable adaptable);
}
