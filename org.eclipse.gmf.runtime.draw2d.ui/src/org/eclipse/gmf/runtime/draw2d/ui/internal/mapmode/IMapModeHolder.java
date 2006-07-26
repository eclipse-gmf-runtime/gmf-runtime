/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2006.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
