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
package org.eclipse.gmf.runtime.diagram.core.edithelpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.resources.IBookmark;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.commands.RemoveBookmarkCommand;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramDebugOptions;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyDependentsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Edit helper advice that provides commands to remove associated bookmarks 
 * of all dependents as well as the main destructee.
 * 
 * @author satif
 */

public class RemoveBookmarksAdvice extends AbstractEditHelperAdvice {

	/**
	 * @param bookmarkedObject	the <code>EObject</code> to retrieve bookmarks for
	 * @param fileResourceMap cache of EResource to IFile (avoids repeatedly finding IFile for EResource)
	 * @return <code>Set</code> of <code>IMarker</code>s related to the <b>bookmarkedObject</b>
	 */
	private Set gatherSingleBookmark(EObject bookmarkedObject, Map fileResourceMap) {
		Resource eResource = null;
		
		if (bookmarkedObject == null || (eResource = bookmarkedObject.eResource()) == null)
			return null;
		
		IResource resource = null;
		
		if (fileResourceMap.containsKey(eResource))
			resource = (IFile)fileResourceMap.get(eResource);
		else {
			resource = WorkspaceSynchronizer.getFile(eResource);
			fileResourceMap.put(eResource, resource);
		}
		
		if (resource == null)
			return null;
		
		String elementID = ((XMLResource)eResource).getID(bookmarkedObject);
		
		IMarker[] bookmarks = new IMarker[0];
		
		try {
			bookmarks = resource.findMarkers(IBookmark.TYPE, true,
				IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			Trace.catching(DiagramPlugin.getInstance(), DiagramDebugOptions.EXCEPTIONS_CATCHING, 
					getClass(), "gatherSingleBookmark", e); //$NON-NLS-1$
			Log.error(DiagramPlugin.getInstance(), IStatus.ERROR, "gatherSingleBookmark"); //$NON-NLS-1$
		}
		
		Set retSet = new HashSet();

		for (int i = 0; i < bookmarks.length; i++) {
			
			IMarker bookmark = bookmarks[i];
			
			String bookmarkElementID = bookmark.getAttribute(IBookmark.ELEMENT_ID,StringStatics.BLANK);
			
			if (elementID.equals(bookmarkElementID))				
				retSet.add(bookmark);
		}
		
		return retSet;
	}
	
	/**
	 * @param bookmarkedObjects the <code>Set</code> of <code>EObject</code>s to retrieve bookmarks for
	 * @param fileResourceMap cache of EResource to IFile (avoids repeatedly finding IFile for EResource)
	 * @return <code>Set</code> of <code>IMarker</code>s related to the <b>bookmarkedObjects</b>
	 */
	private Set gatherAllBookmarks(Set bookmarkedObjects, HashMap fileResourceMap) {
		
		Iterator iterBObjects = bookmarkedObjects.iterator();
		
		Set retSet = new HashSet();
		
		while (iterBObjects.hasNext()) {
			Object oElement = iterBObjects.next();
			
			if (oElement instanceof EObject) {
				Set tempSet = gatherSingleBookmark((EObject)oElement, fileResourceMap);
				if (tempSet != null) {
					retSet.addAll(tempSet);
				}
			}
		}
		
		return retSet;
	}
	
	
	public ICommand getBeforeEditCommand(IEditCommandRequest request) {		
		return null;
	}
	
	public ICommand getAfterEditCommand(IEditCommandRequest request) {
		if (request instanceof DestroyElementRequest) {
			return getAfterDestroyElementCommand((DestroyElementRequest) request);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice#getAfterDestroyElementCommand(org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest)
	 */
	protected ICommand getAfterDestroyElementCommand(DestroyElementRequest request) {

		ICommand result = null;
		
		Object oInitialDestructee = request.getParameter(DestroyElementRequest.INITIAL_ELEMENT_TO_DESTROY_PARAMETER);
		
		if (oInitialDestructee != null && 
			oInitialDestructee instanceof EObject && 
			request.getElementToDestroy().equals(oInitialDestructee)) {
			
			Object oDependentElements = request.getParameter(DestroyElementRequest.DESTROY_DEPENDENTS_REQUEST_PARAMETER);
			
			if (oDependentElements == null)
				return null;
			
			HashMap fileResourceMap = new HashMap();
			Set	bookmarksItems = gatherAllBookmarks(
					((DestroyDependentsRequest)oDependentElements).getDependentElementsToDestroy(), fileResourceMap),
				bookmarksDestructee = gatherSingleBookmark(request.getElementToDestroy(), fileResourceMap);
			
			if (bookmarksDestructee != null) 
				bookmarksItems.addAll(bookmarksDestructee);
			
			if (!bookmarksItems.isEmpty()) {
				result = new RemoveBookmarkCommand(request.getEditingDomain(),
								request.getLabel(), bookmarksItems);
			}
			
		}
		
		return result;
	}
	
	
	
}
