/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Command that sets the view's mutability. if a view is Mutability state is used
 * by the Diagram Listener to decide if a view can move from the transient childern 
 * list of its container to the persisted list. so a mutable view will never be persisted
 * keep in mind that the mutability state of the view is a transient state so as soon as the 
 * model is closed and opened again all views will be immutable.
 * @author mhanner
 */
public class SetViewMutabilityCommand extends Command {
	
	/** cached non-persisted views. */
	private List _viewAdapters = Collections.emptyList();
	
	/** immutable flag. */
	private boolean _immutable = true;

	/**
	 * Creates a command instance.
	 * @param viewAdapter an <code>IAdaptable</code> that adapts to <code>View</code>
	 * @param immutable immutable state
	 */
	public SetViewMutabilityCommand(IAdaptable viewAdapter, boolean immutable) {
		this( Collections.singletonList(viewAdapter), immutable );
	}
	
	/**
	 * Creates a command instance.
	 * @param viewAdapters a list of <code>IAdaptable</code> objects that adapts to <code>View</code>
	 * @param immutable immutable state
	 */
	public SetViewMutabilityCommand(List viewAdapters, boolean immutable) {
		super( DiagramUIMessages.SetViewMutabilityCommand_Label);
		Assert.isNotNull(viewAdapters);
		_viewAdapters = viewAdapters;
		_immutable = immutable;
	}

	/**
	 * Convenience method returning a command to make the supplied views mutable.
	 * @param viewAdapters views to be associated with the command 
	 * @return <code>SetViewMutabilityCommand</code>
	 */
	public static SetViewMutabilityCommand makeMutable( List viewAdapters ) {
		return new SetViewMutabilityCommand(viewAdapters, false);
	}

	/**
	 * Convenience method returning a command to make the supplied view mutable.
	 * @param viewAdapter view to be associated with the command 
	 * @return <code>SetViewMutabilityCommand</code>
	 */
	public static SetViewMutabilityCommand makeMutable( IAdaptable viewAdapter) {
		return new SetViewMutabilityCommand(viewAdapter, false);
	}

	
	/**
	 * Convenience method returning a command to make the supplied views immutable. 
	 * @param viewAdapters views to be associated with the command 
	 * @return <code>SetViewMutabilityCommand</code>
	 */
	public static SetViewMutabilityCommand makeImmutable( List viewAdapters ) {
		return new SetViewMutabilityCommand(viewAdapters, true);
	}

	/**
	 * Convenience method returning a command to make the supplied view immutable. 
	 * @param viewAdapter views to be associated with the command 
	 * @return <code>SetViewMutabilityCommand</code>
	 */
	public static SetViewMutabilityCommand makeImmutable( IAdaptable viewAdapter) {
		return new SetViewMutabilityCommand(viewAdapter, true);
	}
	
	/**
	 * gets an unmodifiable copy of the cached view adapters. 
	 * @return view adapters
	 */
	protected List getViewAdapters() {
		return Collections.unmodifiableList(_viewAdapters);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		setMutability(_immutable);
	}
	
	/** Set the mutability flag on all views. */
	private void setMutability(final boolean immutable) {
		if (!_viewAdapters.isEmpty()) {
			
			TransactionalEditingDomain editingDomain = TransactionUtil
				.getEditingDomain(((IAdaptable) _viewAdapters.get(0))
					.getAdapter(View.class));
			if (editingDomain != null) {

				Map options = Collections.singletonMap(
					Transaction.OPTION_UNPROTECTED, Boolean.TRUE);
				AbstractEMFOperation operation = new AbstractEMFOperation(
					editingDomain, StringStatics.BLANK, options) {

					protected IStatus doExecute(IProgressMonitor monitor,
							IAdaptable info)
						throws ExecutionException {
						Iterator adapters = _viewAdapters.iterator();
						while (adapters.hasNext()) {
							IAdaptable adapter = (IAdaptable) adapters.next();
							View notationView = (View) adapter
								.getAdapter(View.class);
							if (notationView != null) {
								notationView.setMutable(!immutable);
							}
						}
						return Status.OK_STATUS;
					}
				};
				try {
					operation.execute(new NullProgressMonitor(), null);
				} catch (ExecutionException e) {
					Trace.catching(DiagramUIPlugin.getInstance(),
						DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
						"setMutability", e); //$NON-NLS-1$
					Log.warning(DiagramUIPlugin.getInstance(),
						DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
						"setMutability", e); //$NON-NLS-1$

				}

			}
		}
	}

	/** 
	 * Returns the view that would be affected if this
	 * command were executed, undone, or redone.
	 * @return views adapter Collection
	 */
	public Collection getAffectedObjects() {
		return getViewAdapters();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		setMutability(_immutable);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		setMutability(!_immutable);
	}

}
