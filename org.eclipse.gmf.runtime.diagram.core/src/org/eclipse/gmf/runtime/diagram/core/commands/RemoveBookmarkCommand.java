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
package org.eclipse.gmf.runtime.diagram.core.commands;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramDebugOptions;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;

/**
 * Removes all bookmark(s) of a supplied object.
 * 
 * @author satif
 */
public class RemoveBookmarkCommand extends AbstractTransactionalCommand {
	
	private Set bookmarks;
	
	/**
	 * constructor
	 * 
     * @param editingDomain 	the editing domain
	 * @param label 			the command label
	 * @param bookmarks			the <code>Set</code> of <code>IMarker</code>s to delete
	 */
	public RemoveBookmarkCommand(TransactionalEditingDomain editingDomain, String label, Set bookmarks) {
		super(editingDomain, label, null);
		this.bookmarks = bookmarks;	
	}

	/**
	 * Deletes the <code>Set</code> of bookmarks 
	 */
	protected void deleteBookmarks() {
		if (getBookmarkedObjects() == null) 
			return;
		
		Iterator iterBookmarks = getBookmarkedObjects().iterator();
		
		while (iterBookmarks.hasNext()) {
			Object oBookmark = iterBookmarks.next();
			
			if (oBookmark instanceof IMarker) {
				try {
					((IMarker)oBookmark).delete();
				} catch (CoreException e) {
					Trace.catching(DiagramPlugin.getInstance(), DiagramDebugOptions.EXCEPTIONS_CATCHING, 
							getClass(), "deleteBookmarks", e); //$NON-NLS-1$
					Log.error(DiagramPlugin.getInstance(), IStatus.ERROR, "deleteBookmarks"); //$NON-NLS-1$
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand#doExecuteWithResult(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) 
		throws ExecutionException {
		
		deleteBookmarks();
		
		return CommandResult.newOKCommandResult();
	}

	/**
	 * @return <code>Set</code> of bookmarks to be deleted.
	 */
	public Set getBookmarkedObjects() {
		return bookmarks;
	}

	/**
	 * @param bookmarks set the <code>Set</code> of bookmarks to be deleted.
	 */
	public void setBookmarkedObjects(Set bookmarks) {
		this.bookmarks = bookmarks;
	}
}
