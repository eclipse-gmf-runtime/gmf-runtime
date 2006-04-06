/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.emf.ui.preferences;

import java.util.Arrays;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;


/**
 * An enumeration of destinations for display of live validation problems.
 *
 * @author Christian W. Damus (cdamus)
 * @deprecated
 */
public class ValidationLiveProblemsDestination extends EnumeratedType {

	private static final long serialVersionUID = 1L;

	/**
     * An internal unique identifier for this enumerated type.
     */
    private static int nextOrdinal = 0;

    public static final ValidationLiveProblemsDestination DIALOG = new ValidationLiveProblemsDestination("Dialog"); //$NON-NLS-1$
    public static final ValidationLiveProblemsDestination CONSOLE = new ValidationLiveProblemsDestination("Console"); //$NON-NLS-1$

    /**
     * The list of values for this enumerated type.
     */
    private static final ValidationLiveProblemsDestination[] VALUES =
        { DIALOG, CONSOLE };

    /**
     * Gets the preference setting for live validation problems destination.
     * 
     * @return the preference setting
     */
    public static ValidationLiveProblemsDestination getPreferenceSetting() {
        int ordinal =
            MslUIPlugin.getDefault().getPreferenceStore().getInt(
                 IPreferenceConstants.VALIDATION_LIVE_PROBLEMS_DISPLAY);

        switch (ordinal) {
            case 0 :
                return DIALOG;
            case 1 :
                return CONSOLE;
            default :
                break;
        }
        
        return DIALOG;
    }

    /**
     * Constructor for ValidationLiveProblemsDestination.
     * 
     * @param name The name for the ValidationLiveProblemsDestination type
     */
    private ValidationLiveProblemsDestination(String name) {
        super(name, nextOrdinal++);
    }

    /**
     * Retrieves the list of constants for this enumerated type.
     * 
     * @return The list of constants for this enumerated type.
     */
    protected List getValues() {
        return Arrays.asList(VALUES);
    }
}
