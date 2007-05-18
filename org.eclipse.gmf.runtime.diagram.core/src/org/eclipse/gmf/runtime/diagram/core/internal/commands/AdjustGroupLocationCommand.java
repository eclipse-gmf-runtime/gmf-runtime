/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
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
 */
public class AdjustGroupLocationCommand
    extends AbstractTransactionalCommand {

    private View groupView;

    /**
     * Creates a new instance.
     */
    public AdjustGroupLocationCommand(TransactionalEditingDomain domain,
            View groupView) {
        super(domain, DiagramCoreMessages.AdjustLocation_Label,
            getWorkspaceFiles(groupView));
        this.groupView = groupView;
    }

    protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
            IAdaptable info)
        throws ExecutionException {

        int minChildX = Integer.MAX_VALUE;
        int minChildY = Integer.MAX_VALUE;

        for (Iterator iterator = groupView.getChildren().iterator(); iterator
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

            Integer x = (Integer) ViewUtil.getStructuralFeatureValue(groupView,
                NotationPackage.eINSTANCE.getLocation_X());
            ViewUtil.setStructuralFeatureValue(groupView,
                NotationPackage.eINSTANCE.getLocation_X(), new Integer(x
                    .intValue()
                    + minChildX));

            for (Iterator iterator = groupView.getChildren().iterator(); iterator
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

            Integer y = (Integer) ViewUtil.getStructuralFeatureValue(groupView,
                NotationPackage.eINSTANCE.getLocation_Y());
            ViewUtil.setStructuralFeatureValue(groupView,
                NotationPackage.eINSTANCE.getLocation_Y(), new Integer(y
                    .intValue()
                    + minChildY));

            for (Iterator iterator = groupView.getChildren().iterator(); iterator
                .hasNext();) {
                View childView = (View) iterator.next();
                y = (Integer) ViewUtil.getStructuralFeatureValue(childView,
                    NotationPackage.eINSTANCE.getLocation_Y());
                ViewUtil.setStructuralFeatureValue(childView,
                    NotationPackage.eINSTANCE.getLocation_Y(), new Integer(y
                        .intValue()
                        - minChildY));
            }
        }
        return CommandResult.newOKCommandResult();
    }

}
