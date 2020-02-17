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
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Location;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command groups node views (i.e. shapes) together. It creates a new group
 * view and reparents the nodes passed in to be children of the group. The
 * nodes' locations are also changed to be relative to the location of the
 * group.
 * 
 * @author crevells, mmostafa
 * @since 2.1
 */
public class GroupCommand
    extends AbstractTransactionalCommand {

    private List nodes;

    /**
     * Creates a new instance.
     * 
     * @param editingDomain
     *            the editing domain
     * @param nodes
     *            A list of nodes (i.e. shape views) that are to be grouped. The
     *            nodes must all have the same parent.
     */
    public GroupCommand(TransactionalEditingDomain editingDomain, List nodes) {
        this(editingDomain, nodes, null);
    }

    /**
     * Creates a new instance.
     * 
     * @param editingDomain
     *            the editing domain
     * @param nodes
     *            A list of nodes (i.e. shape views) that are to be grouped. The
     *            nodes must all have the same parent.
     * @param options
     *            for the transaction in which this command executes, or
     *            <code>null</code> for the default options
     */
    public GroupCommand(TransactionalEditingDomain editingDomain, List nodes,
            Map options) {
        super(editingDomain, DiagramCoreMessages.GroupCommand_Label, options,
            getWorkspaceFiles(nodes));
        this.nodes = nodes;
    }

    /**
     * Creates the new group, reparents the nodes, and sets the locations of the
     * group and nodes as appropriate.\
     */
    protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

        Node group = NotationFactory.eINSTANCE.createNode();
        group.setType(ViewType.GROUP);
        group.setElement(null);

        View parentView = (View) ((View) getNodes().get(0)).eContainer();
        parentView.getPersistedChildren().add(group);

        int x = 0;
        int y = 0;
        boolean first = true;
        for (Iterator iter = getNodes().iterator(); iter.hasNext();) {
            Object view = iter.next();
            if (view instanceof Node) {
                LayoutConstraint layoutConstraint = ((Node) view)
                    .getLayoutConstraint();
                if (layoutConstraint instanceof Location) {
                    Location location = (Location) layoutConstraint;
                    if (first) {
                        x = location.getX();
                        y = location.getY();
                        first = false;
                    } else {
                        if (x > location.getX())
                            x = location.getX();
                        if (y > location.getY())
                            y = location.getY();
                    }
                }
                group.insertChild((View) view);
            }
        }

        Bounds groupBounds = NotationFactory.eINSTANCE.createBounds();
        groupBounds.setX(x);
        groupBounds.setY(y);
        groupBounds.setWidth(-1);
        groupBounds.setHeight(-1);
        group.setLayoutConstraint(groupBounds);

        translateChildrenLocations(x, y);
        return CommandResult.newOKCommandResult(group);
    }

    /**
     * Translate the location of the children to be relative to the group's
     * location.
     * 
     * @param x the group's location x-value
     * @param y the group's location y-value
     */
    private void translateChildrenLocations(int x, int y) {
        if (x == 0 && y == 0)
            return;
        for (Iterator iter = getNodes().iterator(); iter.hasNext();) {
            Object view = iter.next();
            if (view instanceof Node) {
                LayoutConstraint layoutConstraint = ((Node) view)
                    .getLayoutConstraint();
                if (layoutConstraint instanceof Location) {
                    Location location = (Location) layoutConstraint;
                    location.setX(location.getX() - x);
                    location.setY(location.getY() - y);
                }
            }
        }
    }

    /**
     * Gets the list of nodes to be grouped.
     * 
     * @return the list of nodes to be grouped
     */
    protected List getNodes() {
        return nodes;
    }

}
