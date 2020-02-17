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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Location;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command will relocate the group so that the group's location reflects
 * the location of the top-most and left-most shape. The locations of all the
 * children will also be updated so that they are relative to the new location
 * of the group. It can be used after a shape in the group has been moved,
 * resized, or deleted.
 * 
 * @author crevells
 * @since 2.1
 */
public class UpdateGroupLocationCommand
    extends AbstractTransactionalCommand {

    private View groupView;

    /**
     * Creates a new instance.
     */
    public UpdateGroupLocationCommand(TransactionalEditingDomain domain,
            View groupView) {
        super(domain, DiagramCoreMessages.UpdateLocation_Label,
            getWorkspaceFiles(groupView));
        this.groupView = groupView;
    }

    /**
     * Gets the group view to be updated.
     * 
     * @return the group view
     */
    protected View getGroupView() {
        return groupView;
    }

    protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
            IAdaptable info)
        throws ExecutionException {

        updateGroupLocation(getGroupView());

        // update container groups in case of nested groups
        EObject container = getGroupView().eContainer();
        while (container instanceof View
            && ViewType.GROUP.equals(((View) container).getType())) {
            updateGroupLocation((View) container);
            container = ((View) container).eContainer();
        }
        return CommandResult.newOKCommandResult();
    }

    /**
     * Updates the group location and the children locations as required.
     */
    private void updateGroupLocation(View group) {
        int minChildX = Integer.MAX_VALUE;
        int minChildY = Integer.MAX_VALUE;

        for (Iterator iterator = group.getChildren().iterator(); iterator
            .hasNext();) {
            View childView = (View) iterator.next();

            if (childView instanceof Node) {
                LayoutConstraint layoutConstraint = ((Node) childView)
                    .getLayoutConstraint();
                if (layoutConstraint instanceof Location) {
                    Location childLocation = (Location) layoutConstraint;

                    if (childLocation.getX() < minChildX) {
                        minChildX = childLocation.getX();
                    }
                    if (childLocation.getY() < minChildY) {
                        minChildY = childLocation.getY();
                    }
                }
            }
        }

        if (minChildX != 0) {

            // The group's x location must change and consequently all the
            // children x locations as they are relative to the group's
            // location.

            Integer x = (Integer) ViewUtil.getStructuralFeatureValue(group,
                NotationPackage.eINSTANCE.getLocation_X());
            ViewUtil.setStructuralFeatureValue(group, NotationPackage.eINSTANCE
                .getLocation_X(), new Integer(x.intValue() + minChildX));

            for (Iterator iterator = group.getChildren().iterator(); iterator
                .hasNext();) {
                View childView = (View) iterator.next();
                x = (Integer) ViewUtil.getStructuralFeatureValue(childView,
                    NotationPackage.eINSTANCE.getLocation_X());
                ViewUtil.setStructuralFeatureValue(childView,
                    NotationPackage.eINSTANCE.getLocation_X(), new Integer(x
                        .intValue()
                        - minChildX));
            }
        }

        if (minChildY != 0) {

            // The group's y location must change and consequently all the
            // children y locations as they are relative to the group's
            // location.

            Integer y = (Integer) ViewUtil.getStructuralFeatureValue(group,
                NotationPackage.eINSTANCE.getLocation_Y());
            ViewUtil.setStructuralFeatureValue(group, NotationPackage.eINSTANCE
                .getLocation_Y(), new Integer(y.intValue() + minChildY));

            for (Iterator iterator = group.getChildren().iterator(); iterator
                .hasNext();) {
                View childView = (View) iterator.next();
                y = (Integer) ViewUtil.getStructuralFeatureValue(childView,
                    NotationPackage.eINSTANCE.getLocation_Y());
                ViewUtil.setStructuralFeatureValue(childView,
                    NotationPackage.eINSTANCE.getLocation_Y(), Integer.valueOf(y
                        .intValue()
                        - minChildY));
            }
        }
    }

}
