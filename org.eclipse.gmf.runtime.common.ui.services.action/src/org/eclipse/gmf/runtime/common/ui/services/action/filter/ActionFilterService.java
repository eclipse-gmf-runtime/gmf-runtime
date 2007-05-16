/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.filter;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.ActionManager;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionPlugin;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionStatusCodes;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.filter.IActionFilterProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * A service that provides action filters that can be used in evaluating action
 * expressions.
 * 
 * @author khussey
 *  
 */
public class ActionFilterService
	extends Service
	implements IActionFilterProvider, IOperationHistoryListener {
	
	private static class CacheKey {

		public String name;

		public String value;

		public CacheKey() {
			this(null,null);
		}

		public CacheKey(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public boolean equals(Object obj) {
			//This is our key, and it won't never be called except with another CacheKey,
			//therefore is no need to check for either null, or, instanceof CacheKey
			CacheKey other = (CacheKey) obj;
			return value.equals(other.value) && name.equals(other.name);
		}

		public int hashCode() {
			return name.hashCode() ^ value.hashCode();
		}
	}
	
	private static final CacheKey TEST_KEY = new CacheKey();

	/**
	 * A descriptor for action filter providers defined by a configuration
	 * element.
	 * 
	 * @author khussey
	 *  
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {

		/**
		 * The name of the 'name' XML attribute.
		 *  
		 */
		protected static final String A_NAME = "name"; //$NON-NLS-1$

		/**
		 * The name of the 'value' XML attribute.
		 *  
		 */
		protected static final String A_VALUE = "value"; //$NON-NLS-1$

		/**
		 * The name of the 'Attribute' XML element.
		 *  
		 */
		protected static final String E_ATTRIBUTE = "Attribute"; //$NON-NLS-1$

		/**
		 * Constructs a new action filter provider descriptor for the specified
		 * configuration element.
		 * 
		 * @param element
		 *            The configuration element describing the provider.
		 *  
		 */
		protected ProviderDescriptor(IConfigurationElement element) {
			super(element);
		}

		/**
		 * Indicates whether this provider descriptor provides the specified
		 * operation.
		 * 
		 * @return <code>true</code> if the name and value (optional) of the
		 *         specified operation matches the name and value (if provided)
		 *         of one of the attributes (if any) defined for this provider
		 *         descriptor or if this descriptor's policy or provider
		 *         provides the operation; <code>false</code> otherwise.
		 * @param operation
		 *            The operation in question.
		 * 
		 * @see IProvider#provides(IOperation)
		 *  
		 */
		public boolean provides(IOperation operation) {
			if (!getElement().isValid())
				return false;
			IConfigurationElement[] elements = getElement().getChildren(
				E_ATTRIBUTE);

			if (0 < elements.length) {
				TestAttributeOperation tao = (TestAttributeOperation) operation;

				for (int i = 0; i < elements.length; i++) {

					try {
						String name = elements[i].getAttribute(A_NAME);
						String value = elements[i].getAttribute(A_VALUE);

						if (name.equals(tao.getName())
							&& ((null == value) || value.equals(tao.getValue()))) {

							return true;
						} // if
					} catch (Exception e) {
						Trace.catching(CommonUIServicesActionPlugin.getDefault(),
							CommonUIServicesActionDebugOptions.EXCEPTIONS_CATCHING,
							getClass(), "provides", e); //$NON-NLS-1$
						Log.error(CommonUIServicesActionPlugin.getDefault(),
							CommonUIServicesActionStatusCodes.SERVICE_FAILURE, MessageFormat
								.format(INVALID_ELEMENT_MESSAGE_PATTERN,
									new Object[] {elements[i].getName()}), e);
					}
				} // for

				return false;
			} else {
				return super.provides(operation);
			} // else
		}
	}

	/**
	 * Prefix for action expressions that use the action filter service.
	 */
	protected final static String PREFIX = "@"; //$NON-NLS-1$

	/**
	 * The singleton instance of the action filter service.
	 *  
	 */
	private final static ActionFilterService instance = new ActionFilterService();

	static {
		instance.configureProviders(CommonUIServicesActionPlugin.getPluginId(), "actionFilterProviders"); //$NON-NLS-1$
	}

	/**
	 * The cached results (for optimization).
	 *  
	 */
	private final Map cachedResults = new HashMap();

	/**
	 * The cached selection (for optimization). The selection is cached only for
	 * the purpose of validating the results cache when the service is asked to
	 * test an attribute.
	 *  
	 */
//	private ISelection cachedSelection = StructuredSelection.EMPTY;
	/*
	 * RATLC00527385 cachedSelection should be a weakreference, as it 
	 * has a reference to ISelection, and it is not a selection listener.
	 */
	private WeakReference cachedSelection = null;

	/**
	 * Constructs a new action filter service.
	 *  
	 */
	protected ActionFilterService() {
		super(true);

        getOperationHistory().addOperationHistoryListener(this);
	}

	/**
	 * Retrieves the singleton instance of the action filter service.
	 * 
	 * @return The action filter service singleton.
	 *  
	 */
	public static ActionFilterService getInstance() {
		return instance;
	}

	/**
	 * Retrieves the value of the <code>cachedResults</code> instance
	 * variable.
	 * 
	 * @return The value of the <code>cachedResults</code> instance variable.
	 *  
	 */
	private Map getCachedResults() {
		return cachedResults;
	}

	/**
	 * Retrieves the value of the <code>cachedSelection</code> instance
	 * variable.
	 * 
	 * @return The value of the <code>cachedSelection</code> instance
	 *         variable.
	 *  
	 */
	private ISelection getCachedSelection() {
		if(cachedSelection != null) {
			Object sel = cachedSelection.get();
			if(sel != null)
				return (ISelection)sel;
		}
		return StructuredSelection.EMPTY;
	}

	/**
	 * Sets the <code>cachedSelection</code> instance variable to the
	 * specified value.
	 * 
	 * @param selection
	 *            The new value for the <code>cachedSelection</code> instance
	 *            variable.
	 *  
	 */
	private void setCachedSelection(ISelection selection) {
		this.cachedSelection = new WeakReference(selection);
	}

	/**
	 * Retrieves the action manager for this action filter service.
	 * 
	 * @return The action manager for this action filter service.
	 *  
	 */
	protected ActionManager getActionManager() {
		return ActionManager.getDefault();
	}
    
    /**
     * Returns the operation history from my action manager.
     * 
     * @return the operation history
     */
    protected IOperationHistory getOperationHistory() {
        return getActionManager().getOperationHistory();
    }

	/**
	 * Creates a new action filter provider descriptor for the specified
	 * configuration element.
	 * 
	 * @return A new action filter provider descriptor.
	 * @param element
	 *            The configuration element from which to create the descriptor.
	 *  
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
			IConfigurationElement element) {

		return new ProviderDescriptor(element);
	}

	/**
	 * Clears this action filter service's cached results.
	 * <P>
	 * Clients are strongly discouraged from using this method. It will degrade
	 * performance.
	 *  
	 */
	public final void clearCachedResults() {
		getCachedResults().clear();
	}

	/**
	 * Updates the cached results and selection based on the current selection.
	 *  
	 */	
	protected void updateCachedData() {
		ISelection selection = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow();

		if (null != window) {
			selection = window.getSelectionService().getSelection();
		}

		selection = (null == selection ? StructuredSelection.EMPTY
			: selection);
        ISelection cachedSel = getCachedSelection();
        if (cachedSel != selection) {
			if (!cachedSel.equals(selection)) {
				clearCachedResults();
			}
			setCachedSelection(selection);
		}
	}

	/**
	 * Returns whether the specific attribute matches the state of the target
	 * object.
	 * 
	 * @return <code>true</code> if the attribute matches; <code>false</code>
	 *         otherwise
	 * @param target
	 *            The target object.
	 * @param name
	 *            The attribute name.
	 * @param value
	 *            The attriute value.
	 * 
	 * @see org.eclipse.ui.IActionFilter#testAttribute(Object, String, String)
	 *  
	 */
	public boolean testAttribute(Object target, String name, String value) {

		updateCachedData();		
	
		TEST_KEY.name = name;
		TEST_KEY.value = value;	
		
		Boolean result = (Boolean) getCachedResults().get(TEST_KEY);

		if (null == result) {
			
			String normalizedname = name;
			if (name.startsWith(PREFIX)) {
				normalizedname = name.substring(1);
			}

			List results = execute(
				ExecutionStrategy.FIRST,
				new TestAttributeOperation(target, normalizedname, value));
			result = results.isEmpty() ? Boolean.FALSE
				: (Boolean) results.get(0);

			getCachedResults().put(new CacheKey(name, value), result);
		} // if

		return result.booleanValue();
	}
    
    /**
     * Clears my cache when my operation history changes.
     */
    public void historyNotification(OperationHistoryEvent event) {
        clearCachedResults();
        setCachedSelection(StructuredSelection.EMPTY);
    }   
}