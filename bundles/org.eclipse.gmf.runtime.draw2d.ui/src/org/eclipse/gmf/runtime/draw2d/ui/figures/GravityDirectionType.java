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

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * An enumeration of Gravity Direction types.
 * 
 * @author choang
 */
public class GravityDirectionType extends EnumeratedType {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int nextOrdinal = 0;

    /**
     * Constant for a north orientation
     */
    public static final GravityDirectionType NORTH = new GravityDirectionType("North"); //$NON-NLS-1$

    /**
     * Constant for a east orientation
     */
    
    public static final GravityDirectionType EAST = new GravityDirectionType("East"); //$NON-NLS-1$

    /**
     * Constant for a south orientation
     */
    public static final GravityDirectionType SOUTH = new GravityDirectionType("South"); //$NON-NLS-1$
    
    /**
     * Constant for a west orientation
     */
    public static final GravityDirectionType WEST = new GravityDirectionType("West"); //$NON-NLS-1$

    /**
     * The list of values for this enumerated type.
     */
    private static final GravityDirectionType[] VALUES =
        { NORTH, EAST, SOUTH, WEST };

    /**
     * Constructs a new gravity direction kind type with the specified name and
     * ordinal.
     * 
     * @param name The name of the new gravity direction kind type.
     * @param ordinal The ordinal for the new gravity direction kind type.
     */
    protected GravityDirectionType(String name, int ordinal) {
        super(name, ordinal);
    }

    /**
     * Constructs a new gravity direction type with the specified name.
     * 
     * @param name The name of the new gravity direction kind type.
     */
    private GravityDirectionType(String name) {
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
