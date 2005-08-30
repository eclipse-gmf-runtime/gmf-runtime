/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.services.palette;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * An enumeration of palette types.
 * 
 * @author schafe  
 */
public class PaletteType extends EnumeratedType {

    /**
     * An internal unique identifier for palette types.     
     */
    private static int nextOrdinal = 0;

    /**
     * The MODELER palette type.     
     */
    public static final PaletteType MODELER = new PaletteType("MODELER"); //$NON-NLS-1$

    /**
     * The UMLVIEWER palette type.     
     */
    public static final PaletteType UMLVIEWER = new PaletteType("UMLVIEWER"); //$NON-NLS-1$
    

    /**
     * The list of values for this enumerated type.    
     */
    private static final PaletteType[] VALUES = { MODELER, UMLVIEWER };

    /**
     * Constructs a new layout type with the specified name and ordinal.
     * 
     * @param name The name of the new layout type.
     * @param ordinal The ordinal for the new layout type.     
     */
    protected PaletteType(String name, int ordinal) {
        super(name, ordinal);
    }

    /**
     * Constructs a new layout type with the specified name.
     * 
     * @param name The name of the new layout type.     
     */
    private PaletteType(String name) {
        this(name, nextOrdinal++);
    }

    /**
     * Retrieves the list of constants for this enumerated type.
     * 
     * @return The list of constants for this enumerated type.
     * 
     * @see EnumeratedType#getValues()    
     */
    protected List getValues() {
        return Collections.unmodifiableList(Arrays.asList(VALUES));
    }

}
