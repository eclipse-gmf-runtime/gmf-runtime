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


package org.eclipse.gmf.runtime.diagram.ui.util;

import org.eclipse.jface.util.Assert;

/**
 * 
 * Ordinal based typesafe enum for drawing constants
 * 
 * Created On: Jun 9, 2003
 * @author tisrar
 */
public final class DrawConstant implements Comparable {

    //Ordinal of next drawConstant to be created.
    private static int nextOrdinal = 0;

    //The ordinal of this drawconstant.
    private final int ordinal = nextOrdinal++;

    //the name of this DrawConstant
    private final String name;

    //The one and only constructor
    private DrawConstant(String name) {
        this.name = name;
    }

    /**
     * The name of this draw constant.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        Assert.isTrue(o instanceof DrawConstant);
        return ordinal - ((DrawConstant)o).ordinal;
    }
    
    
    /**
     * Compass-direction North (up).
     */
    public static final DrawConstant NORTH = new DrawConstant("NORTH"); //$NON-NLS-1$
    
    /**
     * Compass-direction south (down).
     */
    public static final DrawConstant SOUTH = new DrawConstant("SOUTH");//$NON-NLS-1$
    
    
   
    /**
     * Compass-direction east (right).
     */
    public static final DrawConstant EAST = new DrawConstant("EAST");//$NON-NLS-1$
    
    
    /**
     * Compass-direction west (left).
     */
    public static final DrawConstant WEST = new DrawConstant("WEST");//$NON-NLS-1$
       
    /**
     * Compass-direction east (right).
     */
    public static final DrawConstant SOUTH_EAST = new DrawConstant("SOUTH_EAST");//$NON-NLS-1$
    
    /**
     * Compass-direction north-east
     */
    public static final DrawConstant NORTH_EAST = new DrawConstant("NORTH_EAST");//$NON-NLS-1$
    
    /**
     * Compass-direction south west
     */
    public static final DrawConstant SOUTH_WEST = new DrawConstant("SOUTH_WEST");//$NON-NLS-1$
    
    /**
     * Compass-direction north west.
     */
    public static final DrawConstant NORTH_WEST = new DrawConstant("NORT_WEST");//$NON-NLS-1$
        
    /**
     * Box-orientation constant used to specify the top of a box.
     */
    public static final DrawConstant TOP = new DrawConstant("TOP");//$NON-NLS-1$
    
    /**
     * Box-orientation constant used to specify the bottom of a box
     */
    public static final DrawConstant BOTTOM = new DrawConstant("BOTTOM");//$NON-NLS-1$
    
    /**
     * Box-orientation constant used to specify the left side of a box.
     */
    public static final DrawConstant LEFT = new DrawConstant("LEFT");//$NON-NLS-1$
    
    
    /**
     * Box-orientation constant used to specify the right side of a box
     */
    public static final DrawConstant RIGHT = new DrawConstant("RIGHT");//$NON-NLS-1$
        
    
    /**
     *  Box-orientation constant used to specify the center of a box
     */
	public static final DrawConstant CENTER = new DrawConstant("CENTER");//$NON-NLS-1$
    
    /**
     * Orientation horizontal constant
     */
    public static final DrawConstant HORIZONTAL = new DrawConstant("HORIZONTAL");//$NON-NLS-1$
    
    
    /**
     * Orientation vertical constant
     */
    public static final DrawConstant VERTICAL = new DrawConstant("VERTICAL");//$NON-NLS-1$
    
    /**
     * Invalid Compass-direction .
     */
    public static final DrawConstant INVALID = new DrawConstant("INVALID"); //$NON-NLS-1$

}
