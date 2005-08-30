/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.emf.core.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLStatusCodes;
import org.eclipse.gmf.runtime.emf.core.internal.util.Trace;
import org.eclipse.gmf.runtime.emf.core.resources.AbstractLogicalResourcePolicy;
import org.eclipse.gmf.runtime.emf.core.resources.CannotAbsorbException;
import org.eclipse.gmf.runtime.emf.core.resources.CannotSeparateException;
import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;
import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResourcePolicy;


/**
 * Manager of {@link ILogicalResourcePolicy} implementations registered on the
 * <tt>org.eclipse.gmf.runtime.emf.core.resourcePolicies</tt> extension point.
 *
 * @author Christian W. Damus (cdamus)
 */
public class LogicalResourcePolicyManager implements ILogicalResourcePolicy {
	private static final String EXT_POINT = "resourcePolicies"; //$NON-NLS-1$
	private static final String E_POLICY = "policy"; //$NON-NLS-1$
	private static final String E_EPACKAGE = "epackage"; //$NON-NLS-1$
	private static final String E_EFACTORY = "efactory"; //$NON-NLS-1$
	private static final String A_NSURI = "nsURI"; //$NON-NLS-1$
	private static final String A_CLASS = "class"; //$NON-NLS-1$
	
	private static final LogicalResourcePolicyManager INSTANCE =
		new LogicalResourcePolicyManager();
	
	private static final ILogicalResourcePolicy[] EMPTY_ARRAY =
		new ILogicalResourcePolicy[0];
	
	private final Map policyMap = new java.util.HashMap();
	private final Map factoryMap = new java.util.HashMap();
	
	/**
	 * Not instantiable by clients.
	 */
	private LogicalResourcePolicyManager() {
		super();
		initializePolicyExtensions();
	}

	/**
	 * Obtains the singleton policy manager instance.
	 * 
	 * @return the policy manager
	 */
	public static LogicalResourcePolicyManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Initializes me from the extension point.
	 */
	private void initializePolicyExtensions() {
		IConfigurationElement[] elements =
			Platform.getExtensionRegistry().getConfigurationElementsFor(
			MSLPlugin.getPluginId(), EXT_POINT);
		
		for (int i = 0; i < elements.length; i++) {
			if (E_POLICY.equals(elements[i].getName())) {
				initializePolicy(elements[i]);
			} else if (E_EFACTORY.equals(elements[i].getName())) {
				initializeFactory(elements[i]);
			} else {
				Log.log(
					MSLPlugin.getDefault(),
					IStatus.WARNING,
					MSLStatusCodes.LOGICAL_INVALID_POLICY_ELEMENT,
					ResourceManager.getInstance().formatMessage(
						"resourcePolicy.element_WARN_", //$NON-NLS-1$
						new Object[] {
							elements[i].getName(),
							elements[i].getNamespace()}),
					null);
			}
		}
		
		// convert the policy map's lists to arrays
		for (Iterator iter = policyMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry next = (Map.Entry) iter.next();
			List list = (List) next.getValue();
			
			next.setValue(list.toArray(new ILogicalResourcePolicy[list.size()]));
		}
	}
	
	private void initializePolicy(IConfigurationElement element) {
		PolicyDescriptor descriptor = new PolicyDescriptor(element);
		
		List nsUris = descriptor.getNamespaceUris();
		
		if (nsUris.isEmpty()) {
			Log.warning(
				MSLPlugin.getDefault(),
				MSLStatusCodes.LOGICAL_INVALID_POLICY_ELEMENT,
				ResourceManager.getInstance().formatMessage(
					"resourcePolicy.nsUris_WARN_", //$NON-NLS-1$
					new Object[] {element.getNamespace()}));
		} else {
			for (Iterator iter = nsUris.iterator(); iter.hasNext();) {
				String nsUri = (String) iter.next();
				
				List list = (List) policyMap.get(nsUri);
				if (list == null) {
					list = new java.util.ArrayList();
					policyMap.put(nsUri, list);
				}
				
				list.add(descriptor);
			}
		}
	}
	
	private void initializeFactory(IConfigurationElement element) {
		FactoryDescriptor descriptor = new FactoryDescriptor(element);
		factoryMap.put(descriptor.getNamespaceUri(), descriptor);
	}
	
	/**
	 * Gets the policies pertaining to the specified model element.
	 * 
	 * @param eObject an element
	 * @return the policies or an empty array if none (never <code>null</code>)
	 */
	ILogicalResourcePolicy[] getPolicies(EObject eObject) {
		return getPolicies(eObject.eClass().getEPackage().getNsURI());
	}
	
	/**
	 * Gets the policies pertaining to the specified metamodel.
	 * 
	 * @param nsUri a metamodel namespace URI
	 * @return the policies or an empty array if none (never <code>null</code>)
	 */
	ILogicalResourcePolicy[] getPolicies(String nsUri) {
		ILogicalResourcePolicy[] result =
			(ILogicalResourcePolicy[]) policyMap.get(nsUri);
		
		if (result == null) {
			result = EMPTY_ARRAY;
			policyMap.put(nsUri, result);
		}
		
		return result;
	}

	public boolean canSeparate(ILogicalResource resource, EObject eObject) {
		boolean result = true;
		ILogicalResourcePolicy[] policies = getPolicies(eObject);
		
		for (int i = 0; result && (i < policies.length); i++) {
			try {
				if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
					Trace.trace(
						MSLDebugOptions.RESOURCES,
						"Invoking canSeparate() on policy: " + policies[i]); //$NON-NLS-1$
				}
				
				result = policies[i].canSeparate(resource, eObject);
			} catch (RuntimeException e) {
				Trace.catching(getClass(), "canSeparate", e); //$NON-NLS-1$
				Log.warning(
					MSLPlugin.getDefault(),
					MSLStatusCodes.LOGICAL_POLICY_FAILED,
					ResourceManager.getI18NString("resource.policy_EXC_"), //$NON-NLS-1$
					e);
			}
		}
		
		return result;
	}

	public URI preSeparate(ILogicalResource resource, EObject eObject, URI uri)
		throws CannotSeparateException {
		
		URI result = uri;
		ILogicalResourcePolicy[] policies = getPolicies(eObject);
		
		for (int i = 0; i < policies.length; i++) {
			try {
				if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
					Trace.trace(
						MSLDebugOptions.RESOURCES,
						"Invoking preSeparate() on policy: " + policies[i]); //$NON-NLS-1$
				}
				
				URI suggestion = policies[i].preSeparate(resource, eObject, uri);
				
				if (result == null) {
					result = suggestion;
				}
			} catch (RuntimeException e) {
				Trace.catching(getClass(), "preSeparate", e); //$NON-NLS-1$
				Log.warning(
					MSLPlugin.getDefault(),
					MSLStatusCodes.LOGICAL_POLICY_FAILED,
					ResourceManager.getI18NString("resource.policy_EXC_"), //$NON-NLS-1$
					e);
			}
		}
		
		return result;
	}

	public void postSeparate(ILogicalResource resource, EObject eObject, URI uri) {
		ILogicalResourcePolicy[] policies = getPolicies(eObject);
		
		for (int i = 0; i < policies.length; i++) {
			try {
				if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
					Trace.trace(
						MSLDebugOptions.RESOURCES,
						"Invoking postSeparate() on policy: " + policies[i]); //$NON-NLS-1$
				}
				
				policies[i].postSeparate(resource, eObject, uri);
			} catch (RuntimeException e) {
				Trace.catching(getClass(), "postSeparate", e); //$NON-NLS-1$
				Log.warning(
					MSLPlugin.getDefault(),
					MSLStatusCodes.LOGICAL_POLICY_FAILED,
					ResourceManager.getI18NString("resource.policy_EXC_"), //$NON-NLS-1$
					e);
			}
		}
	}

	public void preAbsorb(ILogicalResource resource, EObject eObject)
		throws CannotAbsorbException {
		
		ILogicalResourcePolicy[] policies = getPolicies(eObject);
		
		for (int i = 0; i < policies.length; i++) {
			try {
				if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
					Trace.trace(
						MSLDebugOptions.RESOURCES,
						"Invoking preAbsorb() on policy: " + policies[i]); //$NON-NLS-1$
				}
				
				policies[i].preAbsorb(resource, eObject);
			} catch (RuntimeException e) {
				Trace.catching(getClass(), "preAbsorb", e); //$NON-NLS-1$
				Log.warning(
					MSLPlugin.getDefault(),
					MSLStatusCodes.LOGICAL_POLICY_FAILED,
					ResourceManager.getI18NString("resource.policy_EXC_"), //$NON-NLS-1$
					e);
			}
		}
	}

	public void postAbsorb(ILogicalResource resource, EObject eObject) {
		ILogicalResourcePolicy[] policies = getPolicies(eObject);
		
		for (int i = 0; i < policies.length; i++) {
			try {
				if (Trace.isEnabled(MSLDebugOptions.RESOURCES)) {
					Trace.trace(
						MSLDebugOptions.RESOURCES,
						"Invoking postAbsorb() on policy: " + policies[i]); //$NON-NLS-1$
				}
				
				policies[i].postAbsorb(resource, eObject);
			} catch (RuntimeException e) {
				Trace.catching(getClass(), "postAbsorb", e); //$NON-NLS-1$
				Log.warning(
					MSLPlugin.getDefault(),
					MSLStatusCodes.LOGICAL_POLICY_FAILED,
					ResourceManager.getI18NString("resource.policy_EXC_"), //$NON-NLS-1$
					e);
			}
		}
	}
	
	/**
	 * Instantiates the specified <code>EClass</code> in order to create an
	 * unloaded element.  This method will use a factory registered on the
	 * extension point for this purpose, if any.  Otherwise, it will use the
	 * package's default factory.
	 * 
	 * @param eClass the <code>EClass</code> to create
	 * @return a new instance of it
	 */
	public EObject create(EClass eClass) {
		EFactory factory = getEFactory(eClass.getEPackage());
		return factory.create(eClass);
	}
	
	/**
	 * Gets the most appropriate factory for the specified package.  This might
	 * be one that is registered on the extension point, or it might not.
	 * 
	 * @param epackage the <code>EPackage</code> to get a factory for
	 * @return the corresponding factory
	 */
	public EFactory getEFactory(EPackage epackage) {
		EFactory result;
		FactoryDescriptor descriptor = (FactoryDescriptor) factoryMap.get(
			epackage.getNsURI());
		
		if (descriptor == null) {
			result = epackage.getEFactoryInstance();
		} else {
			result = descriptor.instantiate();
		}
		
		return result;
	}
	
	/**
	 * Extension descriptor that lazily initializes the delegate policy instance
	 * when required.  It replaces itself by the delegate for efficiency.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private class PolicyDescriptor implements ILogicalResourcePolicy {
		private IConfigurationElement config;
		private List namespaceUris = new java.util.ArrayList();
		private ILogicalResourcePolicy delegate;
		
		PolicyDescriptor(IConfigurationElement config) {
			this.config = config;
			
			IConfigurationElement[] elements = config.getChildren();
			for (int i = 0; i < elements.length; i++) {
				if (E_EPACKAGE.equals(elements[i].getName())) {
					String nsUri = elements[i].getAttribute(A_NSURI);
					
					if ((nsUri != null) && (nsUri.length() > 0)) {
						namespaceUris.add(nsUri);
					}
				} else {
					Log.log(
						MSLPlugin.getDefault(),
						IStatus.WARNING,
						MSLStatusCodes.LOGICAL_INVALID_POLICY_ELEMENT,
						ResourceManager.getInstance().formatMessage(
							"resourcePolicy.element_WARN_", //$NON-NLS-1$
							new Object[] {
								elements[i].getName(),
								elements[i].getNamespace()}),
						null);
				}
			}
		}
		
		List getNamespaceUris() {
			return namespaceUris;
		}
		
		/**
		 * Ensures that my delegate is instantiated and replaces me in the
		 * manager's map.
		 * 
		 * @return the actual policy, or a null implementation if the real one
		 *     failed to initialize
		 */
		ILogicalResourcePolicy instantiate() {
			if (delegate == null) {
				try {
					delegate = (ILogicalResourcePolicy) config.createExecutableExtension(A_CLASS);
				} catch (CoreException e) {
					Trace.catching(getClass(), "instantiate", e); //$NON-NLS-1$
					
					Log.log(MSLPlugin.getDefault(), e.getStatus());
					
					delegate = new NullPolicy();
				}
				
				for (Iterator iter = getNamespaceUris().iterator(); iter.hasNext();) {
					String nsUri = (String) iter.next();
					
					ILogicalResourcePolicy[] policies = getPolicies(nsUri);
					for (int i = 0; i < policies.length; i++) {
						if (policies[i] == this) {
							policies[i] = delegate;
						}
					}
				}
			}
			
			return delegate;
		}

		public boolean canSeparate(ILogicalResource resource, EObject eObject) {
			instantiate();
			
			return delegate.canSeparate(resource, eObject);
		}

		public URI preSeparate(ILogicalResource resource, EObject eObject, URI uri)
			throws CannotSeparateException {
			
			instantiate();
			
			return delegate.preSeparate(resource, eObject, uri);
		}

		public void postSeparate(ILogicalResource resource, EObject eObject, URI uri) {
			instantiate();
			
			delegate.postSeparate(resource, eObject, uri);
		}

		public void preAbsorb(ILogicalResource resource, EObject eObject)
			throws CannotAbsorbException {
			
			instantiate();
			
			delegate.preAbsorb(resource, eObject);
		}

		public void postAbsorb(ILogicalResource resource, EObject eObject) {
			instantiate();
			
			delegate.postAbsorb(resource, eObject);
		}
		
		public String toString() {
			instantiate();
			
			return delegate.toString();
		}
	}
	
	/**
	 * Extension descriptor that lazily initializes the delegate factory instance
	 * when required.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private class FactoryDescriptor {
		private IConfigurationElement config;
		private String namespaceUri;
		private EFactory factory;
		
		FactoryDescriptor(IConfigurationElement config) {
			this.config = config;
			namespaceUri = config.getAttribute(A_NSURI);
		}
		
		String getNamespaceUri() {
			return namespaceUri;
		}
		
		/**
		 * Ensures that my delegate is instantiated and replaces me in the
		 * manager's map.
		 * 
		 * @return the actual factory, or the default implementation if the
		 *     configured one failed to initialize
		 */
		EFactory instantiate() {
			if (factory == null) {
				try {
					factory = (EFactory) config.createExecutableExtension(A_CLASS);
				} catch (CoreException e) {
					Trace.catching(getClass(), "instantiate", e); //$NON-NLS-1$
					
					Log.log(MSLPlugin.getDefault(), e.getStatus());
					
					factory = EPackage.Registry.INSTANCE.getEPackage(
						getNamespaceUri()).getEFactoryInstance();
				}
			}
			
			return factory;
		}
	}
	
	/**
	 * Placeholder for policy extensions that could not be instantiated.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private static final class NullPolicy extends AbstractLogicalResourcePolicy {
		NullPolicy() {
			super();
		}
	}
}
