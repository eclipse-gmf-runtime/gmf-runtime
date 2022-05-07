/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NoteAttachmentReorientEditPolicy;

/**
 * By default, reorienting of connections that do not have any semantic meaning
 * (e.g. note attachments) will be supported between any two nodes by a
 * <code>GraphicalNodeEditPolicy</code>. The intention of this editpolicy is
 * to provide a place where this can be disabled. To disable reorienting of a
 * connection between two nodes, return an unexecutable command. For an example
 * implementation, see {@link NoteAttachmentReorientEditPolicy}.
 * 
 * @author Cherie Revells
 */
abstract public class NotationConnectionReorientEditPolicy
    extends AbstractEditPolicy {

    public Command getCommand(Request request) {
        if (REQ_RECONNECT_SOURCE.equals(request.getType())
            && connectionSourceHasChanged((ReconnectRequest) request)) {
            return getReorientConnectionSourceCommand((ReconnectRequest) request);
        } else if (REQ_RECONNECT_TARGET.equals(request.getType())
            && connectionTargetHasChanged((ReconnectRequest) request)) {
            return getReorientConnectionTargetCommand((ReconnectRequest) request);
        }

        return super.getCommand(request);
    }

    /**
     * Subclasses may override to return an unexecutable command if reorienting
     * the connection to the new source should be disabled. Otherwise, this
     * command should return null so as not to interfere.
     * 
     * @param request
     *            the request to change the source of a connection
     * @return an unexecutable command if this gesture should be disabled; null
     *         otherwise
     */
    protected Command getReorientConnectionSourceCommand(
            ReconnectRequest request) {
        return null;
    }

    /**
     * Subclasses may override to return an unexecutable command if reorienting
     * the connection to the new target should be disabled. Otherwise, this
     * command should return null so as not to interfere.
     * 
     * @param request
     *            the request to change the target of a connection
     * @return an unexecutable command if this gesture should be disabled; null
     *         otherwise
     */
    protected Command getReorientConnectionTargetCommand(
            ReconnectRequest request) {
        return null;
    }

    /**
     * Has the connection source changed? If not, then it is not necessary to
     * return a command that will change the connection's source.
     * 
     * @param request
     *            the request to reconnect the source of a connection
     * @return true if the source has changed; false otherwise
     */
    private boolean connectionSourceHasChanged(ReconnectRequest request) {
        return !request.getConnectionEditPart().getSource().equals(
            request.getTarget());
    }

    /**
     * Has the connection target changed? If not, then it is not necessary to
     * return a command that will change the connection's target.
     * 
     * @param request
     *            the request to reconnect the target of a connection
     * @return true if the target has changed; false otherwise
     */
    private boolean connectionTargetHasChanged(ReconnectRequest request) {
        return !request.getConnectionEditPart().getTarget().equals(
            request.getTarget());
    }

    public boolean understandsRequest(Request request) {
        if ((REQ_RECONNECT_SOURCE.equals(request.getType()) || REQ_RECONNECT_TARGET
            .equals(request.getType()))) {
            return true;
        }
        return false;
    }

}