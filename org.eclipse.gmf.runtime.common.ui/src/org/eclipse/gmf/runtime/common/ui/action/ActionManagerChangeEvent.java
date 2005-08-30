/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action;

import java.util.EventObject;

/**
 * Represent an event that is fired when an action manager changes. Instances of
 * this class have an associated action manager (the source of the event) and
 * action (the action that was run).
 * 
 * @author khussey
 * 
 * @see org.eclipse.gmf.runtime.common.ui.action.IActionManagerChangeListener
 */
public class ActionManagerChangeEvent extends EventObject {

    /**
     * The action that was run.
     */
    private final IRepeatableAction action;

    /**
     * Constructs a new action manager change event for the specified action
     * manager.
     * 
     * @param source The action manager that changed.
     */
    public ActionManagerChangeEvent(ActionManager source) {
        this(source, null);
    }

    /**
     * Constructs a new action manager change event for the specified action
     * manager and action.
     * 
     * @param source The action manager that changed.
     * @param action The action that has been run.
     */
    public ActionManagerChangeEvent(
        ActionManager source,
        IRepeatableAction action) {

        super(source);

        this.action = action;
    }

    /**
     * Retrieves the value of the <code>action</code> instance variable.
     * 
     * @return The value of the <code>action</code> instance variable.
     */
    public IRepeatableAction getAction() {
        return action;
    }

    /**
     * Sets the <code>source</code> instance variable to the specified value.
     * 
     * @param source The new value for the <code>source</code> instance
     *                variable.
     */
    protected void setSource(ActionManager source) {
        assert null != source;

        this.source = source;
    }

}
