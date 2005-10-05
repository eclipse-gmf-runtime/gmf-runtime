/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * The interface for all actions that could potentially be run with a progress
 * indicator.
 * 
 * @author khussey
 * @author ldamus
 */
public interface IActionWithProgress {
    /**
     * Enumerated type for work indicator type
     */
    public class WorkIndicatorType extends EnumeratedType {
        private static int nextOrdinal = 0;
    
        /** No work indicator. */
        public static final WorkIndicatorType NONE = new WorkIndicatorType("None"); //$NON-NLS-1$
    
        /** Busy work indicator. */
        public static final WorkIndicatorType BUSY = new WorkIndicatorType("Busy"); //$NON-NLS-1$
    
        /** Progress monitor work indicator. */
        public static final WorkIndicatorType PROGRESS_MONITOR = new WorkIndicatorType("Progress Monitor"); //$NON-NLS-1$
    
        /** Cancelable progress monitor work indicator. */
    	public static final WorkIndicatorType CANCELABLE_PROGRESS_MONITOR = new WorkIndicatorType("Cancelable Progress Monitor"); //$NON-NLS-1$
    	
        /**
         * The list of values for this enumerated type.
         */
        private static final WorkIndicatorType[] VALUES =
            { NONE, BUSY, PROGRESS_MONITOR, CANCELABLE_PROGRESS_MONITOR };
    
        /**
         * Constructor for WorkIndicatorType.
         * @param name The name for the WorkIndicatorType
         * @param ordinal The ordinal for theWorkIndicatorType
         */
        protected WorkIndicatorType(String name, int ordinal) {
            super(name, ordinal);
        }
    
        /**
         * Constructor for WorkIndicatorType.
         * @param name The name for the WorkIndicatorType
         */
        private WorkIndicatorType(String name) {
            this(name, nextOrdinal++);
        }
    
        /**
         * Retrieves the list of constants for this enumerated type.
         * @return The list of constants for this enumerated type.
         */
        protected List getValues() {
            return Collections.unmodifiableList(Arrays.asList(VALUES));
        }
    }

    /**
     * Retrieves the label for this action.
     * 
     * @return The label for this action.
     */
    public String getLabel();

    /**
     * Retrieves a Boolean indicating whether this action can be
     * run.
     * 
     * @return <code>true</code> if this action can be run;
     *          <code>false</code> otherwise.
     */
    public boolean isRunnable();

    /**
     * Refreshes various aspects of this action, such as its label
     * and whether or not it is enabled.
     */
    public void refresh();
    
    /**
     * Sets up the action. Should always be called before
     * {@link #run(IProgressMonitor)} is called.
     * @return <code>true</code> if the setup completed successfully,
     * 		   <code>false</code> otherwise.
     */
    public boolean setup();

    /**
     * Runs this action.
     * 
     * @param progressMonitor <code>IProgressMonitor</code> monitoring the execution of this action
     */
    public void run(IProgressMonitor progressMonitor);

    /**
     * Gets type of work indicator (progress monitor, hourglass, or none).
     * 
     * @return type of work indicator
     */
    public WorkIndicatorType getWorkIndicatorType();
}
