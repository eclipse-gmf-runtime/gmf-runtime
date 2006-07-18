package org.eclipse.gmf.runtime.diagram.ui.editparts;

/**
 * This is a HAck to avoid breaking clients who still send the Unresolved event
 * It should be remomved and replaced by a refresh edit policy 
 * that will be installed by the clients
 *
 */
interface EventType {
    public static final int UNRESOLVE = 1003;
}