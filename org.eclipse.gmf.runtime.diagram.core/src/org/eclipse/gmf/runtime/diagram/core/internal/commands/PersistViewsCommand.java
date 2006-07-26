/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2006.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Yasser Lulu
 * 
 */
public class PersistViewsCommand
    extends AbstractTransactionalCommand {

    private List views;

    /**
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param view
     */
    public PersistViewsCommand(TransactionalEditingDomain editingDomain,
            List views) {
        super(editingDomain, DiagramCoreMessages.AddCommand_Label, null);
        this.views = views;
    }

    /**
     * Creates a new instance.
     * 
     * @param domain
     * @param label
     * @param options
     * @param affectedFiles
     */
    public PersistViewsCommand(TransactionalEditingDomain editingDomain,
            List views, Map options) {
        super(editingDomain, DiagramCoreMessages.AddCommand_Label, options,
            null);
        this.views = views;
    }

    /**
     * persisted the views, by moving them from the transient Feature
	 * to the persisted feature. This command supports persisteing Views or  Edges
     * @return the persisted views.
     */
    protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        try {
            Iterator it = views.iterator();
            while (it.hasNext()) {
                View view = (View) it.next();
                EObject container = view.eContainer();
                if (view instanceof Edge) {
                    Diagram diagram = (Diagram) container;
                    diagram.persistEdges();
                } else if (container instanceof View) {
                    ((View) container).persistChildren();
                }
            }
            return CommandResult.newOKCommandResult(views);
        } catch (Exception e) {
            Log.error(DiagramPlugin.getInstance(), IStatus.ERROR, e
                .getMessage(), e);
            return CommandResult.newErrorCommandResult(e.getMessage());
        }
    }
}
