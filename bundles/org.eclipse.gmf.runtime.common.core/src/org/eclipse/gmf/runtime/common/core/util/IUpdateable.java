/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
 */
public interface IUpdateable {
    /**
     * Tests if an element is updatable
     * @param adaptable element to test
     * @return boolean true if element is updatable, otherwise false
     */
    public boolean isUpdateable(IAdaptable adaptable);
}
