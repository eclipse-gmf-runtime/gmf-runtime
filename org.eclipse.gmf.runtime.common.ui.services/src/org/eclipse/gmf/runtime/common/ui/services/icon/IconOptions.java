/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.icon;

/**
 * Class for setting and testing flags used by the icon service
 *  
 * @author Michael Yee
 */
public final class IconOptions {
    private int flags;

    /**
     * Constructor for IconOptions
     */
    public IconOptions() {
        this(0);
    }

    /**
     * Constructor for IconOptions
     * @param flags the flags
     */
    private IconOptions(int flags) {
        this.flags = flags;
    }

	/** no option */
    public static final IconOptions NONE =
        new IconOptions(0);
        
    /** set this if the stereotype image of an element's stereotype is requested */
    public static final IconOptions GET_STEREOTYPE_IMAGE_FOR_ELEMENT =
        new IconOptions(1 << 1);

    /** 
     * set this if no default image should be returned if the requested 
     * stereotype image is not found
     */
    public static final IconOptions NO_DEFAULT_STEREOTYPE_IMAGE =
        new IconOptions(1 << 2);

    /**
	 * set this if the image representing a stereotype is requested (use this to
	 * find the icon for a stereotype without an element).
	 */
	public static final IconOptions GET_IMAGE_REPRESENTING_STEREOTYPE = new IconOptions(
		1 << 3);

    /**
	 * Returns the flags as an integer bit flag
	 * 
	 * @return int the flags as an integer bit flag
	 */
    public int intValue() {
        return flags;
    }

    /**
     * Tests if the specified option is set
     * @param flags     the flags as an integer bit flag
     * @param option    the specified option
     * @return boolean  <code>true</code> if this option is set, otherwise <code>false</code>
     */
    public static boolean isSet(int flags, IconOptions option) {
        if ((flags & option.flags) != 0)
            return true;
        return false;
    }

    /**
     * Sets the specified option
     * @param option the specified option
     */
    public void set(IconOptions option) {
        flags = flags | option.flags;
    }
}
