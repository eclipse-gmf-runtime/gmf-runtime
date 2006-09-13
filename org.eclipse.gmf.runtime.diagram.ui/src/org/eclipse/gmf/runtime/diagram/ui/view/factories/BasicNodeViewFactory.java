/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.view.factories; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This is the base factory class for all Node views, it will 
 * create the view and decorate it using the default decorations
 * you can subclass it to add more decorations, or customize the 
 * way it looks, like adding new style
 * @see #createView(IAdaptable, View, String, int, boolean, String)
 * @see #decorateView(View, View, IAdaptable, String, int, boolean)
 * @see #createStyles(View)
 * @author mmostafa
 * 
 */
public class BasicNodeViewFactory extends AbstractViewFactory {
	private static final Map options = new HashMap();	
    static {
        options.put(Transaction.OPTION_UNPROTECTED, Boolean.TRUE);
        options.put(Transaction.OPTION_NO_NOTIFICATIONS, Boolean.TRUE);
        options.put(Transaction.OPTION_NO_TRIGGERS, Boolean.TRUE);
    }

	/**
	 * factory method, that will be called by the view service to creat
	 * the view
	 * @param semanticAdapter semanitc element of the view, it can be null
     * @param containerView the view to contain the connection
     * @param semanticHint a semantic hint to reflect the view type, it can be empty 
     * @param index position with parent's child collection 
     * @param persisted persisted flag, this will indicate if the created view
	 * will be a presisted or transient view, transient views never get serialized
	 */
	public View createView(final IAdaptable semanticAdapter,
						   final View containerView,
						   final String semanticHint,
						   final int index,
		boolean persisted, final PreferencesHint preferencesHint) {
		setPreferencesHint(preferencesHint);
		final Node node = createNode();
		List styles = createStyles(node);
        if (styles.size() > 0) {
            node.getStyles().addAll(styles);
        }
		
        LayoutConstraint layoutConstraint = createLayoutConstraint();
        if (layoutConstraint != null) {
            node.setLayoutConstraint(layoutConstraint);
        }

		EObject semanticEl = semanticAdapter==null ? null : (EObject)semanticAdapter.getAdapter(EObject.class);
		if (semanticEl==null)
			// enforce a set to NULL
			node.setElement(null);
		else if (requiresElement(semanticAdapter,containerView)){
			node.setElement(semanticEl);
		}
		
		node.setType(semanticHint);
		
	    // decorate view assumes that the view had been inserted already, so
		// we had to call insert child before calling decorate view
		ViewUtil.insertChildView(containerView, node, index, persisted);		
		
        TransactionalEditingDomain domain = getEditingDomain(semanticEl,
            containerView);
        
        if (domain != null) {
            // try to reuse the transaction
            if (isUnProtectedSilentTransactionInProgress(domain)){
                // decorate view had to run as a silent operation other wise
                // it will generate too many events
                decorateView(containerView, node, semanticAdapter,
                    semanticHint, index, true);
                
            }else{
        		AbstractEMFOperation operation = new AbstractEMFOperation(
                    domain, StringStatics.BLANK, options) {
        
        			protected IStatus doExecute(IProgressMonitor monitor,
        					IAdaptable info)
        				throws ExecutionException {
        				// decorate view had to run as a silent operation otherwise
        				// it will generate too many events
        				decorateView(containerView, node, semanticAdapter,
        					semanticHint, index, true);
        
        				return Status.OK_STATUS;
        			}
        		};
        		try {
        			operation.execute(new NullProgressMonitor(), null);
        		} catch (ExecutionException e) {
        			Trace.catching(DiagramUIPlugin.getInstance(),
        				DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
        				"createView", e); //$NON-NLS-1$
        			Log
        				.warning(DiagramUIPlugin.getInstance(),
        					DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
        					"createView", e); //$NON-NLS-1$
        		}
            }
        }
		return node;
	}
    
    /**
     * This method is responsible for decorating the created view, it get called
     * by the Factory method
     * 
     * @link #createView(IAdaptable, View, String, int, boolean), it will
     *       intiliaze the view with the default preferences also it will create
     *       the default elements of the <code>View</code> if it had any
     * @param containerView
     *            the container of the view
     * @param view
     *            the view itself
     * @param semanticAdapter
     *            the semantic elemnent of the view (it could be null)
     * @param semanticHint
     *            the semantic hint of the view
     * @param index
     *            the index of the view
     * @param persisted
     *            flag indicating the the view was created as persisted or not
     */
	protected void decorateView(View containerView,
								View view,
								IAdaptable semanticAdapter,
								String semanticHint,
								int index,
								boolean persisted){
		initializeFromPreferences(view);
	}

	/**
	 * Method used to create the layout constraint that will get set on the 
	 * created view. You can override this method in your own factory to change
	 * the default constraint. This method is called by @link #createView(IAdaptable, View, String, int, boolean) 
	 * @return a new layout constraint for the view
	 */
	protected LayoutConstraint createLayoutConstraint() {
		return null;
	}

	
	/**
	 * method to create the Notation meta model <code>Node</code>
	 * object that represents the view, you can override this method in your
	 * own factories to create you own node while extends the <code>Node</code> class
	 * this method is invoked by @link #createView(IAdaptable, View, String, int, boolean)
	 * @return the created <code>Node</code>
	 */
	protected Node createNode(){
		return NotationFactory.eINSTANCE.createNode();
	}
    
     /**
     * Checks if the current active transaction is a unprotected amd silent
     * 
     * @param domain , the domain to use during the check
     * @return <code>true</code> if the current active transaction is unprotected and silent 
     */
    protected static boolean isUnProtectedSilentTransactionInProgress(TransactionalEditingDomain domain) {
        if (domain instanceof InternalTransactionalEditingDomain){
            InternalTransactionalEditingDomain internalEditingDomain = 
                (InternalTransactionalEditingDomain)domain;
            InternalTransaction transaction = internalEditingDomain.getActiveTransaction();
            if (transaction!=null && !transaction.isReadOnly()) {
                Object unprotectedMode = transaction.getOptions().get(Transaction.OPTION_UNPROTECTED); 
                if (unprotectedMode != null && unprotectedMode == Boolean.TRUE) {
                    // check for silent
                    Object noNotificationMode = transaction.getOptions().get(Transaction.OPTION_NO_NOTIFICATIONS);
                    Object noTriggersMode = transaction.getOptions().get(Transaction.OPTION_NO_TRIGGERS);
                    if (unprotectedMode != null && noNotificationMode == Boolean.TRUE &&
                        noTriggersMode !=null &&  noTriggersMode == Boolean.TRUE           ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
	

}