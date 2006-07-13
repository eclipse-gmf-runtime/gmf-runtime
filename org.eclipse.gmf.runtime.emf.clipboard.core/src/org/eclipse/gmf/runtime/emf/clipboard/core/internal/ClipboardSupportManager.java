/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.emf.clipboard.core.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.gmf.runtime.emf.clipboard.core.IClipboardSupportFactory;
import org.eclipse.gmf.runtime.emf.clipboard.core.internal.l10n.EMFClipboardCoreMessages;
import org.eclipse.osgi.util.NLS;


/**
 * Manager for the <tt>clipboardSupport</tt> extension point.
 *
 * @author Christian W. Damus (cdamus)
 */
public class ClipboardSupportManager {
	public static final String EP_CLIPBOARD_SUPPORT = "org.eclipse.gmf.runtime.emf.clipboard.core.clipboardSupport"; //$NON-NLS-1$
	static final String E_NSURI = "nsURI"; //$NON-NLS-1$
	static final String E_CLASS = "class"; //$NON-NLS-1$
	
	/** @deprecated need a context-based solution */
	static final String E_PRIORITY = "priority"; //$NON-NLS-1$
	
	/** @deprecated need a context-based solution */
	private static final List PRIORITIES = Arrays.asList(new String[] {
		"lowest", "low", "medium", "high", "highest"});  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
	
	private static final Map clipboardSupportMap = new java.util.HashMap();
	
	/** Not instantiable by clients. */
	private ClipboardSupportManager() {
		super();
	}

	/**
	 * Configures the extensions on the <tt>clipboardSupport</tt> extension
	 * point.
	 * <p>
	 * <b>Note</b> that this method must only be called by the plug-in class
	 * on initialization.
	 * </p>
	 * 
	 * @param configs the configuration elements representing extensions
	 */
	public static void configureExtensions(IConfigurationElement[] configs) {
		for (int i = 0; i < configs.length; i++) {
			try {
				Descriptor desc = new Descriptor(configs[i]);
				Descriptor previous = (Descriptor) clipboardSupportMap.get(
					desc.getEPackage());
				
				if ((previous == null)
						|| (previous.getPriority() < desc.getPriority())) {
					
					clipboardSupportMap.put(desc.getEPackage(), desc);
				}
			} catch (CoreException e) {
				ClipboardPlugin.getPlugin().log(e.getStatus());
			}
		}
	}
	
	/**
	 * Retrieves the clipboard support factory (if any) that handles the
	 * specified <code>EPackage</code>.
	 * 
	 * @param ePackage an <code>EPackage</code>
	 * @return the registered clipboard support factory, or <code>null</code>
	 *     if none was registered or it could not be initialized
	 */
	public static IClipboardSupportFactory lookup(EPackage ePackage) {
		IClipboardSupportFactory result = null;
		
		Descriptor desc = (Descriptor) clipboardSupportMap.get(ePackage);
		if (desc != null) {
			result = desc.getFactory();
		}
		
		return result;
	}
	
	/**
	 * Retrieves the clipboard support factory (if any) that handles the
	 * specified <code>EClass</code>.
	 * 
	 * @param eClass an <code>EClass</code>
	 * @return the registered clipboard support factory, or <code>null</code>
	 *     if none was registered or it could not be initialized
	 */
	public static IClipboardSupportFactory lookup(EClass eClass) {
		return lookup(eClass.getEPackage());
	}
	
	/**
	 * Retrieves the clipboard support factory (if any) that handles the
	 * specified <code>EObject</code>.
	 * 
	 * @param eObject an <code>EObject</code>
	 * @return the registered clipboard support factory, or <code>null</code>
	 *     if none was registered or it could not be initialized
	 */
	public static IClipboardSupportFactory lookup(EObject eObject) {
		return lookup(eObject.eClass().getEPackage());
	}
	
	/**
	 * Creates an error status with the specified <code>message</code>.
	 * 
	 * @param code the error code
	 * @param message the error message
	 * @return the status object
	 */
	static IStatus createErrorStatus(int code, String message) {
		return createErrorStatus(
			code,
			message,
			null);
	}
	
	/**
	 * Creates an error status with the specified <code>message</code>.
	 * 
	 * @param code the error code
	 * @param message the error message
	 * @param exception an exception to log
	 * @return the status object
	 */
	static IStatus createErrorStatus(int code, String message, Throwable exception) {
		return new Status(
			IStatus.ERROR,
			ClipboardPlugin.getPlugin().getSymbolicName(),
			code,
			message,
			exception);
	}
	
	/**
	 * Descriptor for an extension on the <tt>clipboardSupport</tt> point.
	 * The descriptor is responsible for lazily initializing the
	 * {@link IClipboardSupportFactory} on its first access.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private static final class Descriptor {
		private final EPackage ePackage;
		private IClipboardSupportFactory factory;
		private IConfigurationElement config;
		private int priority = 2;
		
		Descriptor(IConfigurationElement config) throws CoreException {
			this.config = config;
			
			String nsUri = config.getAttribute(E_NSURI);
			if ((nsUri == null) || (nsUri.length() == 0)) {
				throw new CoreException(createErrorStatus(
					ClipboardStatusCodes.CLIPBOARDSUPPORT_MISSING_NSURI,
					NLS.bind(EMFClipboardCoreMessages.missing_nsUri_ERROR_,
						new Object[] {
							ClipboardPlugin.EXTPT_CLIPBOARDSUPPORT,
							config.getDeclaringExtension().getNamespaceIdentifier()})));
			}
			
			ePackage = EPackage.Registry.INSTANCE.getEPackage(nsUri);
			if (ePackage == null) {
				throw new CoreException(createErrorStatus(
					ClipboardStatusCodes.CLIPBOARDSUPPORT_UNRESOLVED_NSURI,
					NLS.bind(EMFClipboardCoreMessages.unresolved_nsUri_ERROR_,
						new Object[] {
							ClipboardPlugin.EXTPT_CLIPBOARDSUPPORT,
							nsUri,
							config.getDeclaringExtension().getNamespaceIdentifier()})));
			}
			
			String className = config.getAttribute(E_CLASS);
			if ((className == null) || (className.length() == 0)) {
				throw new CoreException(createErrorStatus(
					ClipboardStatusCodes.CLIPBOARDSUPPORT_MISSING_CLASS,
					NLS.bind(EMFClipboardCoreMessages.missing_class_ERROR_,
						new Object[] {
							ClipboardPlugin.EXTPT_CLIPBOARDSUPPORT,
							config.getDeclaringExtension().getNamespaceIdentifier()})));
			}
			
			String priorityStr = config.getAttribute(E_PRIORITY);
			if (priorityStr != null) {
				priorityStr = priorityStr.toLowerCase();
				
				if (PRIORITIES.contains(priorityStr)) {
					priority = PRIORITIES.indexOf(priorityStr);
				}
			}
		}
		
		EPackage getEPackage() {
			return ePackage;
		}
		
		/** @deprecated need a context-based solution */
		int getPriority() {
			return priority;
		}
		
		IClipboardSupportFactory getFactory() {
			if ((factory == null) && (config != null)) {
				// we only keep the config element as long as we need it in
				//    order to attempt to instantiate the class
				factory = createFactory();
			}
			
			return factory;
		}
		
		private IClipboardSupportFactory createFactory() {
			IClipboardSupportFactory result = null;
			
			try {
				result = (IClipboardSupportFactory) config.createExecutableExtension(E_CLASS);
			} catch (CoreException e) {
				ClipboardPlugin.getPlugin().log(e.getStatus());
			} catch (Exception e) {
				// log any other exception, too (such as ClassCastException)
				ClipboardPlugin.getPlugin().log(createErrorStatus(
					ClipboardStatusCodes.CLIPBOARDSUPPORT_FACTORY_FAILED,
					NLS.bind(EMFClipboardCoreMessages.factory_failed_ERROR_,
						new Object[] {
							IClipboardSupportFactory.class.getName(),
							config.getAttribute(E_CLASS)}),
					e));
			} finally {
				// we won't try again to instantiate this class
				config = null;
			}
			
			return result;
		}
	}
}
