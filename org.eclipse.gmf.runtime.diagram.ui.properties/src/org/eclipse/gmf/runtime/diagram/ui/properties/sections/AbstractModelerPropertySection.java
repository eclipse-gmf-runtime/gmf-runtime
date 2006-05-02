/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.sections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.DemultiplexingListener;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.properties.PropertiesServiceAdapterFactory;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.DiagramPropertiesDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.DiagramPropertiesPlugin;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.DiagramPropertiesStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.DiagramUIPropertiesMessages;
import org.eclipse.gmf.runtime.diagram.ui.properties.util.SectionUpdateRequestCollapser;
import org.eclipse.gmf.runtime.diagram.ui.properties.views.IReadOnlyDiagramPropertySheetPageContributor;
import org.eclipse.gmf.runtime.diagram.ui.properties.views.PropertiesBrowserPage;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * An abstract implementation of a section in a tab in the tabbed property sheet
 * page for modeler.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:anthonyh@ca.ibm.com">anthonyh@ca.ibm.com </a>
 */
public abstract class AbstractModelerPropertySection
	extends AbstractPropertySection {

	private TabbedPropertySheetPage tabbedPropertySheetPage;
 
	/**
	 * model event listener
	 */
	protected DemultiplexingListener eventListener = new DemultiplexingListener(getFilter()) {

		protected void handleNotification(TransactionalEditingDomain domain,
				Notification notification) {
			update(notification, (EObject) notification.getNotifier());
		}
	};

	// properties provider to obtain properties of the objects on the list
	protected static final PropertiesServiceAdapterFactory propertiesProvider = new PropertiesServiceAdapterFactory();

	private boolean bIsCommandInProgress = false;

	/** value changed string */
	static protected String VALUE_CHANGED_STRING = DiagramUIPropertiesMessages.AbstractPropertySection_UndoIntervalPropertyString;

	/** object currently selected on either a diagram or a ME - a view */
	protected List input;

	/** eObject should gradually replace EElement */
	protected EObject eObject;

	private List eObjectList = new ArrayList();

	/**
	 * a flag indicating if this property section got disposed
	 */
	protected boolean disposed = false;
    
    private TransactionalEditingDomain editingDomain = null;
	
    /* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#setInput(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);

		if (!(selection instanceof IStructuredSelection)
			|| selection.equals(getInput()))
			return;

		input = new ArrayList();

		eObjectList = new ArrayList();
		for (Iterator it = ((IStructuredSelection) selection).iterator(); it
			.hasNext();) {
			Object next = it.next();
			
			// unwrap down to EObject and add to the eObjects list
			if (addToEObjectList(next)) {
				input.add(next);
			}
		}


		// RATLC000524513 Sometimes there is no eobject. For example if user
		// creates a constraint,
		// on a class there will be a connection shown on the diagram which
		// connects the constraint
		// with the class. The user can select this connection, even though it
		// does not have an
		// underlying eobject. Comments are similar. In this case we show only
		// the appearanced tab.
		if (false == eObjectList.isEmpty())
			setEObject((EObject) eObjectList.get(0));

	}

	/**
	 * Add next object in the selection to the list of EObjects if this object 
	 * could be adapted to an <code>EObject</code>
	 * @param object the object to add
	 * @return - true if the object is added, false otherwise 
	 */
	protected boolean addToEObjectList(Object object) {
		EObject adapted = unwrap(object);
		if (adapted != null){
			getEObjectList().add(adapted);
			return true;
		}		
		return false;

	}

	/**
	 * Unwarp the ME or diagram object down to the underlaying UML element
	 * 
	 * @param object -
	 *            object from a diagram or ME
	 * @return - underlaying UML element
	 */
	protected EObject unwrap(Object object) {
		return adapt(object);
	}

	/**
	 * Adapt the object to an EObject - if possible
	 * 
	 * @param object
	 *            object from a diagram or ME
	 * @return EObject
	 */
	protected EObject adapt(Object object) {
		if (object instanceof IAdaptable) {
			return (EObject) ((IAdaptable) object).getAdapter(EObject.class);
		}

		return null;
	}

	/**
	 * Determines if the page is displaying properties for this element
	 * 
	 * @param notification
	 *            The notification
	 * @param element
	 *            The element to be tested
	 * @return 'true' if the page is displaying properties for this element
	 */
	protected boolean isCurrentSelection(Notification notification,
			EObject element) {

		if (element == null)
			return false;

		if (eObjectList.contains(element))
			return true;

		if (eObjectList.size() > 0) {
			EObject eventObject = element;

			// check for annotations
			if (element instanceof EAnnotation) {
				eventObject = element.eContainer();
			} else {
				EObject container = element.eContainer();
				if (container != null && container instanceof EAnnotation) {
					eventObject = container.eContainer();
				}
			}

			if (eventObject == null) {
				// the annotation has been removed - check the old owner
				Object tmpObj = notification.getOldValue();
				if (tmpObj != null && tmpObj instanceof EObject) {
					eventObject = (EObject) tmpObj;
				} else {
					return false;
				}
			}

			if (eventObject != element) {
				return eObjectList.contains(eventObject);
			}

		}
		return false;
	}

	/**
	 * A utility method allows execute a piece of code wrapping it in the read
	 * call to the model.
	 * 
	 * @param code -
	 *            Runnable code to execute
	 */
	protected void executeAsReadAction(final Runnable code) {
		try {
			getEditingDomain().runExclusive(code);
		} catch (InterruptedException e) {
			Trace.catching(DiagramPropertiesPlugin.getDefault(),
				DiagramPropertiesDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"executeAsReadAction", e); //$NON-NLS-1$
		}
	}

	/**
	 * A utility method allows execute a list of commands by wrapping them\ in a
	 * composite command.
	 * 
	 * @param commands -
	 *            List of commands to execute
	 */
	protected CommandResult executeAsCompositeCommand(String actionName,
			List commands) {
		
		if (true == bIsCommandInProgress)
			return null;

		bIsCommandInProgress = true;

        CompositeCommand command = new CompositeCommand(actionName, commands);
        IOperationHistory history = OperationHistoryFactory.getOperationHistory();

        try {
            IStatus status = history.execute(command,
                new NullProgressMonitor(), null);

            if (status.getCode() == DiagramPropertiesStatusCodes.CANCELLED) {
			refresh();
            }

        } catch (ExecutionException e) {
            Trace.catching(DiagramPropertiesPlugin.getDefault(),
                DiagramPropertiesDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                "executeAsCompositeCommand", e); //$NON-NLS-1$
            Log.error(DiagramPropertiesPlugin.getDefault(),
                DiagramPropertiesStatusCodes.IGNORED_EXCEPTION_WARNING, e
                    .getLocalizedMessage(), e);
        }

		bIsCommandInProgress = false;

		return command.getCommandResult();

	}

	/**
	 * Returns currently selected view object
	 * 
	 * @return Returns the input.
	 */
	public List getInput() {
		return input;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#aboutToBeHidden()
	 */
	public void aboutToBeHidden() {
		super.aboutToBeHidden();
        
        TransactionalEditingDomain theEditingDomain = getEditingDomain();
        if (theEditingDomain != null) {
            theEditingDomain.removeResourceSetListener(getEventListener());
        }
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#aboutToBeShown()
	 */
	public void aboutToBeShown() {
        super.aboutToBeShown();

        TransactionalEditingDomain theEditingDomain = getEditingDomain();
        if (theEditingDomain != null) {
            theEditingDomain.addResourceSetListener(getEventListener());
        }
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#dispose()
	 */
	public void dispose() {
		super.dispose();
		/* 
		 * if (getUpdateRequestCollapser() != null) {
		 * getUpdateRequestCollapser().stop(); updateRequestCollapser = null; }
		 */
		disposed = true;

	}
	
	/**
	 * Returns currently selected view object
	 * 
	 * @return Returns the input.
	 */
	protected Object getPrimarySelection() {
		return (getInput() != null && !getInput().isEmpty() ? getInput().get(0)
			: null);
	}
	

	/**
	 * @return Returns the eObject.
	 */
	protected EObject getEObject() {
		return eObject;
	}

	/**
	 * @param object
	 *            The eObject to set.
	 */	
	protected void setEObject(EObject object) {
		this.eObject = object;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#getFilter()
	 */
	public NotificationFilter getFilter() {
        return NotificationFilter.createEventTypeFilter(Notification.SET).or(
            NotificationFilter.createEventTypeFilter(Notification.UNSET)).and(
            NotificationFilter.createNotifierTypeFilter(EObject.class));
    }


	/**
	 * Update if nessesary, upon receiving the model event. This event will only
	 * be processed when the reciever is visible (the default behavior is not to
	 * listen to the model events while not showing). Therefore it is safe to
	 * refresh the UI. Sublclasses, which will choose to override event
	 * listening behavior should take into account that the model events are
	 * sent all the time - regardless of the section visibility. Thus special
	 * care should be taken by the section that will choose to listen to such
	 * events all the time. Also, the default implementation of this method
	 * synchronizes on the GUI thread. The subclasses that overwrite it should
	 * do the same if they perform any GUI work (because events may be sent from
	 * a non-GUI thread).
	 * 
	 * @see #aboutToBeShown()
	 * @see #aboutToBeHidden()
	 * 
	 * @param notification notification object
	 * @param element element that has changed
	 */
	public void update(final Notification notification, final EObject element) {
		if (!isDisposed() && isCurrentSelection(notification, element)
			&& !isNotifierDeleted(notification)) {
			postUpdateRequest(new Runnable() {

				public void run() {
					if (!isDisposed()
						&& isCurrentSelection(notification, element)
						&& !isNotifierDeleted(notification))
						refresh();

				}
			});
		}
	}

	/**
	 * Returns whether or not the notifier for a particular notification has been
	 * deleted from its parent.
	 * 
	 * This is a fix for RATLC00535181.  What happens is that during deletion of
	 * an element from the diagram, the element first deletes related elements
	 * which causes a modification of the element itself.  When the modification occurs
	 * the event handling mechanism posts a request to the UI queue to refresh the UI.
	 * A race condition occurs where by the time the posted request runs, the element
	 * in question may or may not have already been deleted from its container.  If
	 * the element has been deleted from its container, we should not refresh the
	 * property section.
	 * 
	 * @param notification
	 * @return <code>true</code> if notification has been deleted from its parent, <code>false</code> otherwise
	 */
	protected boolean isNotifierDeleted(Notification notification) {
		if (!(notification.getNotifier() instanceof EObject)) {
			return false;
		}
		EObject obj = (EObject)notification.getNotifier();
		return obj.eResource() == null;
	}
	
	/**
	 * Use requset collapser to post update requests.
	 * 
	 * @param updateRequest -
	 *            runnable update code
	 */
	protected void postUpdateRequest(Runnable updateRequest) {
		getUpdateRequestCollapser().postRequest(this, updateRequest);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleElementModifiedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.EObject)
	 */
	public void handleElementModifiedEvent(final Notification notification,
			final EObject element) {
		update(notification, element);
	}

	/**
	 * @return Returns the eObjectList.
	 */
	protected List getEObjectList() {
		return eObjectList;
	}

	/**
	 * @return Returns the disposed.
	 */
	protected boolean isDisposed() {
		return disposed;
	}

	/**
	 * @return Returns the eventListener.
	 */
	protected DemultiplexingListener getEventListener() {
		return eventListener;
	}

	/**
	 * @return Returns a command
	 */
	protected ICommand createCommand(String name, Resource res,
			final Runnable runnable) {

		return createCommandInternal(name, res, runnable);
	}

	/**
	 * @return Returns a command
	 */
	protected ICommand createCommand(String name, EObject res,
			final Runnable runnable) {

		return createCommandInternal(name, res.eResource(), runnable);
	}

	/**
	 * @return Returns a command
	 */
	private ICommand createCommandInternal(String name, Resource res,
			final Runnable runnable) {

        ICommand command = new AbstractTransactionalCommand(getEditingDomain(), name,
            Collections.singletonList(WorkspaceSynchronizer.getFile(res))) {

            protected CommandResult doExecuteWithResult(
                    IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {

				runnable.run();

                return CommandResult.newOKCommandResult();
			}
		};

		return command;
	}

    /**
     * Gets the editing domain from my EObject input.
     * 
     * @return my editing domain
     */
    protected TransactionalEditingDomain getEditingDomain() {
        if (editingDomain == null) {
            EObject eObjectInput = getEObject();
            if (eObjectInput != null) {
                editingDomain = TransactionUtil.getEditingDomain(eObjectInput);
            } else if (!getEObjectList().isEmpty()) {
                editingDomain = TransactionUtil.getEditingDomain(getEObjectList().get(0));
            }
        }
        return editingDomain;
    }
    
    /**
     * Sets the editingDomain.
     * @param editingDomain The editingDomain to set.
     */
    protected void setEditingDomain(TransactionalEditingDomain editingDomain) {
        this.editingDomain = editingDomain;
    }

    /* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		this.tabbedPropertySheetPage = aTabbedPropertySheetPage;

	}

	/**
	 * Determine if the property sheet page contributor is read only.
	 * 
	 * Topic and Browse diagrams have properties that are read only, even
	 * theough the selection may be modifiable.
	 * 
	 * @return <code>true</code> if the contributor is read only.
	 */
	protected boolean isReadOnly() {
		if (tabbedPropertySheetPage instanceof PropertiesBrowserPage) {
			PropertiesBrowserPage propertiesBrowserPage = (PropertiesBrowserPage) tabbedPropertySheetPage;
			ITabbedPropertySheetPageContributor contributor = propertiesBrowserPage
				.getContributor();
			if (contributor instanceof IReadOnlyDiagramPropertySheetPageContributor) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the standard label width when labels for sections line up on the left
	 * hand side of the composite. We line up to a fixed position, but if a
	 * string is wider than the fixed position, then we use that widest string.
	 * 
	 * @param parent
	 *            The parent composite used to create a GC.
	 * @param labels
	 *            The list of labels.
	 * @return the standard label width.
	 */
	protected int getStandardLabelWidth(Composite parent, String[] labels) {
		int standardLabelWidth = STANDARD_LABEL_WIDTH;
		GC gc = new GC(parent);
		int indent = gc.textExtent("XXX").x; //$NON-NLS-1$
		for (int i = 0; i < labels.length; i++) {
			int width = gc.textExtent(labels[i]).x;
			if (width + indent > standardLabelWidth) {
				standardLabelWidth = width + indent;
			}
		}
		gc.dispose();
		return standardLabelWidth;
	}

	/**
	 * @return Returns the updateRequestCollapser.
	 */
	protected SectionUpdateRequestCollapser getUpdateRequestCollapser() {
		return DiagramPropertiesPlugin.getDefault()
			.getUpdateRequestCollapser();
	}
	
}