/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.core.commands;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Location;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command ungroups a group view. It reparents the children of the group so
 * that their parent is the group's parent and it deletes the group view. The
 * children's locations are also changed to be relative to the location of their
 * new parent.
 * 
 * @author crevells
 * @since 2.1
 */
public class UngroupCommand
    extends AbstractTransactionalCommand {

    private Node group;

    /**
     * Creates a new instance.
     * 
     * @param editingDomain
     *            the editing domain
     * @param groupView
     *            the group view to ungroup
     */
    public UngroupCommand(TransactionalEditingDomain editingDomain,
            Node groupView) {
        this(editingDomain, groupView, null);
    }

    /**
     * Creates a new instance.
     * 
     * @param editingDomain
     *            the editing domain
     * @param groupView
     *            the group view to ungroup
     * @param options
     *            for the transaction in which this command executes, or
     *            <code>null</code> for the default options
     */
    public UngroupCommand(TransactionalEditingDomain editingDomain,
            Node group, Map options) {
        super(editingDomain, DiagramCoreMessages.UngroupCommand_Label, options,
            getWorkspaceFiles(group));
        this.group = group;
    }

    protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

        translateChildrenLocations();

        View parentView = (View) getGroup().eContainer();
        if (parentView != null) {
            parentView.getPersistedChildren().addAll(
                getGroup().getPersistedChildren());
        }
        
        DestroyElementCommand.destroy(getGroup());

        return CommandResult.newOKCommandResult();
    }

    /**
     * Translate the location of the children to no longer be relative to the
     * group's location.
     */
    protected void translateChildrenLocations() {
        Location groupLocation = (Location) getGroup().getLayoutConstraint();

        for (Iterator iter = getGroup().getChildren().iterator(); iter
            .hasNext();) {
            Object child = iter.next();
            if (child instanceof Node) {
                LayoutConstraint layoutConstraint = ((Node) child)
                    .getLayoutConstraint();
                if (layoutConstraint instanceof Location) {
                    Location location = (Location) layoutConstraint;
                    location.setX(location.getX() + groupLocation.getX());
                    location.setY(location.getY() + groupLocation.getY());
                }
            }
        }

    }

    /**
     * Returns the group to be ungrouped.
     * 
     * @return the group to be ungrouped.
     */
    protected Node getGroup() {
        return group;
    }

}
