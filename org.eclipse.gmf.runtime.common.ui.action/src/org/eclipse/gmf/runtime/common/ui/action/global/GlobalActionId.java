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

package org.eclipse.gmf.runtime.common.ui.action.global;

import org.eclipse.ui.actions.ActionFactory;

/**
 * Class with the global action id constants
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalActionId {

    /**
     * This class should not be instantiated since it is a static constant
     * class.
     */
    private GlobalActionId() {
		 /* private constructor */
    }

    /** Action id for the cut action */
    public static final String CUT = ActionFactory.CUT.getId();

    /** Action id for the copy action */
    public static final String COPY = ActionFactory.COPY.getId();

    /** Action id for the paste action */
    public static final String PASTE = ActionFactory.PASTE.getId();

    /** Action id for the move action */
    public static final String MOVE = ActionFactory.MOVE.getId();

    /** Action id for the rename action */
    public static final String RENAME = ActionFactory.RENAME.getId();

    /** Action id for the delete action */
    public static final String DELETE = ActionFactory.DELETE.getId();

    /** Action id for the select all action */
    public static final String SELECT_ALL = ActionFactory.SELECT_ALL.getId();

    /** Action id for the undo action */
    public static final String UNDO = ActionFactory.UNDO.getId();

    /** Action id for the redo action */
    public static final String REDO = ActionFactory.REDO.getId();

    /** Action id for the print action */
    public static final String PRINT = ActionFactory.PRINT.getId();

    /** Action id for the properties action */
    public static final String PROPERTIES = ActionFactory.PROPERTIES.getId();

    /** Action id for the refresh action */
    public static final String REFRESH = ActionFactory.REFRESH.getId();
    
    /** Action id for the revert action */
    public static final String REVERT = ActionFactory.REVERT.getId();

    /** Action id for the save action */
    public static final String SAVE = ActionFactory.SAVE.getId();

    /** Action id for the find action */
    public static final String FIND = ActionFactory.FIND.getId();

    /** Action id for the open action.
     * <p>The "open" action is not an eclipse retargetable action and therefore
     * no workbench action constant is defined. However, the string must be
     * defined here so that the ProviderDescriptor.provides() method (defined
     * in GlobalActionHandlerService) will find the action handler provider
     * with a defined XML property: actionId="open".</p> 
     */
    public static final String OPEN = "open"; //$NON-NLS-1$

    /** Action id for the close action */
    public static final String CLOSE = ActionFactory.CLOSE.getId();
}
