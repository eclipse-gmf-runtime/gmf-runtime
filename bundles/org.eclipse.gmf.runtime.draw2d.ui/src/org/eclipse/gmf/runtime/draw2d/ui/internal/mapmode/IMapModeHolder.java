/******************************************************************************
 * Copyright (c) 2006, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;


/**
 * An interface to be implemented by classes that wrap an IMapMode
 * in order to allow clients a read access to the underlying IMapMode
 * 
 * @author Yasser Lulu
 *
 */
public interface IMapModeHolder
    extends IMapMode {
    
    /**
     * An accessor method for the wrapped IMapMode
     * 
     * @return IMapMode the held IMapMode
     */
    IMapMode getMapMode();

}
