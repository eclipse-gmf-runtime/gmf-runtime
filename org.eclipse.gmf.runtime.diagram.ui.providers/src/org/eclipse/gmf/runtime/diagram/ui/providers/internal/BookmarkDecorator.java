/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.resources.IBookmark;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.resources.FileChangeManager;
import org.eclipse.gmf.runtime.common.ui.resources.IFileObserver;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.l10n.DiagramUIProvidersPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.AbstractDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.PlatformUI;


/**
 * Provides bookmark decorations for views 
 * 
 * @author Michael Yee
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 */
public class BookmarkDecorator
	extends AbstractDecorator {

	/**
	 * Listner class for bookmark decorators
	 */
	public class BookmarkObserver
		implements IFileObserver {
		/**
		 * Map of id's and their list of decorators
		 */
		private HashMap mapOfIdsToDecorators = null;

		/**
		 * Flag to indicate if already registered with the file change manager
		 */
		private boolean isRegistered = false;
		
		/**
		 * Registers the specified decorator. Nothing happens if already
		 * registered. The decorator is registered against its view id
		 * 
		 * @param decorator the input bookmark decorator
		 */
		private void registerDecorator(BookmarkDecorator decorator) {
			/* Return if invalid decorator */
			if ( decorator == null ) {
				return;
			}
			
			/* Initialize the map */
			if ( mapOfIdsToDecorators == null ) {
				mapOfIdsToDecorators = new HashMap();
			}			

			/* Return if the decorator has invalid view id */
			String decoratorViewId = decorator.getViewId();
			if ( decoratorViewId == null ) {
				return;
			}
			
			/* Add to the list */
			List list = (List)mapOfIdsToDecorators.get(decoratorViewId);
			if (list == null) {
				list = new ArrayList(2);
				list.add(decorator);
				mapOfIdsToDecorators.put(decoratorViewId, list);
			} else if (!list.contains(decorator)) {
				list.add(decorator);
			}
			
			/* Register with the file change manager */
			if ( !isRegistered() ) {
				FileChangeManager.getInstance().addFileObserver(this);
				isRegistered = true;
			}
		}

		/**
		 * Unregisters the specified decorator. Nothing happens if already
		 * unregistered.
		 * 
		 * @param decorator the input bookmark decorator
		 */
		private void unregisterDecorator(BookmarkDecorator decorator) {
			/* Return if invalid decorator */
			if ( decorator == null ) {
				return;
			}
			
			/* Return if the decorator has invalid view id */
			String decoratorViewId = decorator.getViewId();
			if ( decoratorViewId == null ) {
				return;
			}
			
			if (mapOfIdsToDecorators != null) {
				List list = (List)mapOfIdsToDecorators.get(decoratorViewId);
				if ( list != null ) {
					list.remove(decorator);
					if ( list.isEmpty()) {
						mapOfIdsToDecorators.remove(decoratorViewId);
					}				
				}
				
				if ( mapOfIdsToDecorators.isEmpty() ) {
					mapOfIdsToDecorators = null;
				}			
			}
			
			if (mapOfIdsToDecorators == null) {
				/* Unregister with the file change manager */
				if ( isRegistered() ) {
					FileChangeManager.getInstance().removeFileObserver(this);
					isRegistered = false;
				}			
			}			
		}
		
		/*
		 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileObserver#handleFileRenamed(org.eclipse.core.resources.IFile, org.eclipse.core.resources.IFile)
		 */
		public void handleFileRenamed(IFile oldFile, IFile file) {
			//Empty Code
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileObserver#handleFileMoved(org.eclipse.core.resources.IFile, org.eclipse.core.resources.IFile)
		 */
		public void handleFileMoved(IFile oldFile, IFile file) {
			//Empty Code
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileObserver#handleFileDeleted(org.eclipse.core.resources.IFile)
		 */
		public void handleFileDeleted(IFile file) {
			//Empty Code
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileObserver#handleFileChanged(org.eclipse.core.resources.IFile)
		 */
		public void handleFileChanged(IFile file) {
			//Empty Code
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileObserver#handleMarkerAdded(org.eclipse.core.resources.IMarker)
		 */
		public void handleMarkerAdded(IMarker marker) {
			//Empty Code
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileObserver#handleMarkerDeleted(org.eclipse.core.resources.IMarker, java.util.Map)
		 */
		public void handleMarkerDeleted(IMarker marker, final Map attributes) {
			if ( mapOfIdsToDecorators == null ) {
				return;
			}
			
			Assert.isTrue(!marker.exists());
			// Extract the element guid from the marker and retrieve
			// corresponding view
            String elementId = (String) attributes
            .get(IBookmark.ELEMENT_ID);
            List list = elementId != null ? (List)mapOfIdsToDecorators.get(elementId) : null;
            if ( list != null && !list.isEmpty() ) {
                refreshDecorators(list);
            }
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.resources.IFileObserver#handleMarkerChanged(org.eclipse.core.resources.IMarker)
		 */
		public void handleMarkerChanged(final IMarker marker) {
			if ( mapOfIdsToDecorators == null ) {
				return;
			}
			
			Assert.isTrue(marker.exists());
			// Extract the element ID list from the marker and retrieve
			// corresponding view
            
            String elementId = marker.getAttribute(
                IBookmark.ELEMENT_ID, StringStatics.BLANK);
            List list = elementId != null ? (List)mapOfIdsToDecorators.get(elementId) : null;
            
            if ( list != null && !list.isEmpty() ) {
                refreshDecorators(list);
            }
		}
        
        /**
         * Refreshes decorators asynchronously on the UI thread (required
         * because GEF can only be used on the UI thread) so that getting a read
         * transaction won't contribute to deadlock while the workspace is
         * locked.
         * 
         * @param decorators
         *            the decorators to be refreshed
         */
        private void refreshDecorators(final List decorators) {
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
                public void run() {
                    try {
                        TransactionUtil.getEditingDomain(
                            getDecoratorTarget().getAdapter(View.class)).runExclusive(
                            new Runnable() {
    
                            public void run() {
                                Iterator iter = decorators.iterator();
                                while (iter.hasNext()) {
                                    IDecorator decorator = (IDecorator)iter.next();
                                    if ( decorator != null ) {
                                        decorator.refresh();
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        Trace.catching(DiagramProvidersPlugin.getInstance(),
                            DiagramProvidersDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                            "refreshDecorators()", //$NON-NLS-1$
                            e);
                    }
                }
            });
        }
		
		/**
		 * Returns the flag if this observer is registered
		 * @return the isRegistered.
		 */
		private boolean isRegistered() {
			return isRegistered;
		}
	}
	
	/**
	 * Resource listener for bookmark creation and removal 
	 */
	private static BookmarkObserver fileObserver = null;
	
	/**
	 * Attribute for the view id
	 */
	private String viewId = null;
	
	/**
	 * Creates a new <code>BookmarkDecorator</code>.
	 * @param decoratorTarget
	 */
	public BookmarkDecorator(IDecoratorTarget decoratorTarget) {
		super(decoratorTarget);

		/* Set the id */		
		try {
			final View view = (View) getDecoratorTarget().getAdapter(View.class);
			TransactionUtil.getEditingDomain(view).runExclusive(new Runnable() {

				public void run() {

					BookmarkDecorator.this.viewId = view != null ? ViewUtil
						.getIdStr(view)
						: null;
				}
				});
		} catch (Exception e) {
			Trace.catching(DiagramProvidersPlugin.getInstance(),
				DiagramProvidersDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"BookmarkDecorator::Constructor", //$NON-NLS-1$
				e);
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator#refresh()
	 */
	public void refresh() {
		removeDecoration();

		View view = (View) getDecoratorTarget().getAdapter(View.class);
		EditPart editPart = (EditPart) getDecoratorTarget().getAdapter(
			EditPart.class);
		if (!(editPart instanceof IPrimaryEditPart) || view == null || view.eResource()==null) {
			return;
		}

		IResource resource = getResource(view);
		// make sure we have a resource and that it exists in an open project
		if (resource == null || !resource.exists()) {
			return;
		}

		// query for all the bookmarks of the current resource
		IMarker[] bookmarks = null;
		try {
			bookmarks = resource.findMarkers(IBookmark.TYPE, true,
				IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			Trace.catching(DiagramProvidersPlugin.getInstance(),
				DiagramProvidersDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"getDecorations", e); //$NON-NLS-1$
			Log.error(DiagramProvidersPlugin.getInstance(), IStatus.ERROR, e
				.getMessage());
		}
		if (bookmarks == null) {
			return;
		}

		// find the bookmark containing the element's GUID
		IMarker foundMarker = null;
		String elementId = ((XMLResource) view.eResource()).getID(view);
		if (elementId == null) {
			return;
		}
		
		for (Iterator i = Arrays.asList(bookmarks).iterator(); i.hasNext();) {
			IMarker marker = (IMarker) i.next();
			String attribute = marker.getAttribute(IBookmark.ELEMENT_ID,
				StringStatics.BLANK);
			if (attribute.equals(elementId)) {
				foundMarker = marker;
				break;
			}
		}
		if (foundMarker == null) {
			return;
		}

		// add the bookmark decoration
		if (editPart instanceof ShapeEditPart) {
			IMapMode mm = MapModeUtil.getMapMode(((ShapeEditPart)editPart).getFigure());
			setDecoration(getDecoratorTarget().addShapeDecoration(
				DiagramUIProvidersPluginImages.get(DiagramUIProvidersPluginImages.IMG_BOOKMARK),
				IDecoratorTarget.Direction.NORTH_EAST, mm.DPtoLP(-4), true));
		} else if (view instanceof Edge) {
			setDecoration(getDecoratorTarget().addConnectionDecoration(
				DiagramUIProvidersPluginImages.get(DiagramUIProvidersPluginImages.IMG_BOOKMARK), 50, true));
		}
	}

	/**
	 * Gets the underlying resource of the given view
	 * @param view the given view
	 * @return the view's resource, otherwise <code>null</code>
	 */
	private static IResource getResource(View view) {
		Resource model = view.eResource();
		if (model != null) {
           return WorkspaceSynchronizer.getFile(model);
		}
		return null;
	}

	/** 
	 * Starts listening to events on the decoratorTarget element's container.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator#activate()
	 */
	public void activate() {
		View view = (View) getDecoratorTarget().getAdapter(View.class);
		if (view == null) return;
		Diagram diagramView = view.getDiagram();
		if (diagramView == null) return;
		IFile file = WorkspaceSynchronizer.getFile(diagramView.eResource());
		// It does not make sense to add a file observer if the resource
		//  is not persisted or the uri is not in the form of file:///
		if (file != null) {
			if ( fileObserver == null ) {
				fileObserver = new BookmarkObserver();
			}

			fileObserver.registerDecorator(this);
		}
	}

	/**
	 * Stops the listener and removes the decoration if it is being displayed.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator#deactivate()
	 */
	public void deactivate() {
		if ( fileObserver != null ) {
			fileObserver.unregisterDecorator(this);
			if (!fileObserver.isRegistered()) {
				fileObserver = null;
			}
		}
	
		super.deactivate();
	}
	
	/**
	 * Returns the view id
	 * @return the viewId.
	 */
	private String getViewId() {
		return viewId;
	}
}