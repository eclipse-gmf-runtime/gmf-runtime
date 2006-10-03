/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NotationConnectionReorientEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.NoteAttachmentEditPart;

/**
 * This editpolicy disables reorienting of note attachments between two nodes
 * when neither is a note.
 * 
 * @author Cherie Revells
 */
public class NoteAttachmentReorientEditPolicy
    extends NotationConnectionReorientEditPolicy {

    protected Command getReorientConnectionSourceCommand(
            ReconnectRequest request) {

        ConnectionEditPart connectionEP = request.getConnectionEditPart();
        if (connectionEP instanceof NoteAttachmentEditPart
            && !isValidNoteAttachmentReorient(request.getTarget(), connectionEP.getTarget())) {
            return UnexecutableCommand.INSTANCE;
        }
        return super.getReorientConnectionSourceCommand(request);
    }

    protected Command getReorientConnectionTargetCommand(
            ReconnectRequest request) {

        ConnectionEditPart connectionEP = request.getConnectionEditPart();
        if (connectionEP instanceof NoteAttachmentEditPart
            && !isValidNoteAttachmentReorient(connectionEP.getSource(), request.getTarget())) {
            return UnexecutableCommand.INSTANCE;
        }
        return super.getReorientConnectionSourceCommand(request);

    }

    /**
     * Checks if the connection reorient is valid between the given source and target.
     * @param sourceEditPart
     * @param targetEditPart
     * @return true if the connection reorient is valid; false otherwise
     */
    private boolean isValidNoteAttachmentReorient(EditPart sourceEditPart,
            EditPart targetEditPart) {
        if (sourceEditPart instanceof NoteEditPart
            || targetEditPart instanceof NoteEditPart) {
            return true;
        }
        return false;
    }
}
