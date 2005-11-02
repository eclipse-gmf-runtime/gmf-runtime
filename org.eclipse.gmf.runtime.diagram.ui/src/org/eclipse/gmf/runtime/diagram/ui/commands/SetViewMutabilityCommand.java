/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Command that sets the view's mutability. if a view is Mutability state is used
 * by the Diagram Listener to decide if a view can move from the transient childern 
 * list of its container to the persisted list. so a mutable view will never be persisted
 * keep in mind that the mutability state of the view is a transient state so as soon as the 
 * model is closed and oppened again all views will be immutable.
 * @author mhanner
 */
public class SetViewMutabilityCommand extends Command {
	
	/** cached non-persisted views. */
	private List _viewAdapters = Collections.EMPTY_LIST;
	
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
		super( DiagramResourceManager.getI18NString("SetViewMutabilityCommand.Label"));//$NON-NLS-1$
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
		SetMutability(_immutable);
	}
	
	/** Set the mutability flag on all views. */
	private void SetMutability(final boolean immutable) {
		MEditingDomainGetter.getMEditingDomain(_viewAdapters).runAsUnchecked(new MRunnable() {
			public Object run() {
				Iterator adapters = _viewAdapters.iterator();
				while (adapters.hasNext()) {
					IAdaptable adapter = (IAdaptable)adapters.next();
					View notationView = (View)adapter.getAdapter(View.class);
					if (notationView != null) {
						notationView.setMutable(!immutable);
					}
				}
				return null; 
			}
		});
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
		SetMutability(_immutable);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		SetMutability(!_immutable);
	}

}
