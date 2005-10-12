/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.marker;

import java.util.Vector;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesPlugin;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesStatusCodes;
import org.eclipse.gmf.runtime.common.ui.services.internal.marker.IMarkerNavigationProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;

/**
 * This service is the distribution point for "gotoMarker" requests made by
 * Eclipse to an IEditorPart. In all editors, the implementation of the
 * gotoMarker() method simply forwards the request to this service.
 * <p>
 * Marker navigation providers are chosen based upon priority and the list of
 * marker types they support.
 * 
 * @author Kevin Cornell
 */
public class MarkerNavigationService
	extends Service
	implements IMarkerNavigationProvider {

	/** Remember the single instance of this service. */
	private final static MarkerNavigationService instance = new MarkerNavigationService();

	static {
		instance.configureProviders(CommonUIServicesPlugin.getPluginId(),
			"markerNavigationProviders"); //$NON-NLS-1$
	}

	/**
	 * Marker Navigation Provider Descriptor.
	 * <p>
	 * This class is a descriptor for an <code>IMarkerNavigationProvider</code>
	 * that is defined by an XML configuration element.
	 */
	protected static class MarkerNavigationProviderDescriptor
		extends Service.ProviderDescriptor {

		/** The name of the 'name' XML attribute. */
		protected static final String A_NAME = "name"; //$NON-NLS-1$

		/** The name of the 'MarkerType' XML element. */
		protected static final String E_MARKER_TYPE = "MarkerType"; //$NON-NLS-1$

		/** Keep a list of the marker types accepted by this provider. */
		private Vector markerTypes = new Vector();

		/**
		 * Constructs a <code>IMarkerNavigationProvider</code> descriptor for
		 * the specified configuration element.
		 * <p>
		 * The configuration element is examined to obtain the list of marker
		 * types that are supported by the corresponding provider.
		 * <p>
		 * 
		 * @param element
		 *            The configuration element describing the provider.
		 */
		protected MarkerNavigationProviderDescriptor(
				IConfigurationElement element) {
			super(element);

			// Get the list of 'MarkerType' names supported by the provider.
			IConfigurationElement[] elements = getElement().getChildren(
				E_MARKER_TYPE);
			for (int i = 0; i < elements.length; i++) {
				String markerType = elements[i].getAttribute(A_NAME);
				if (markerType != null) {
					markerTypes.add(markerType);
				}
			}
		}

		/**
		 * Determine if the corresponding provider accepts the operation.
		 * Providers are trivially rejected if they do not support the
		 * associated marker type. However, even if the marker type is
		 * supported, the provider can reject the given operation.
		 * 
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			boolean doesProvide = false;

			if (operation instanceof GotoMarkerOperation) {
				try {
					// Get source marker type from the "goto" operation.
					String markerType = ((GotoMarkerOperation) operation)
						.getMarker().getType();

					// If the marker type is supported, verify acceptance with
					// the provider.
					if (markerTypes.contains(markerType)) {
						doesProvide = super.provides(operation);
					}
				} catch (Exception e) {
					Trace.catching(CommonUIServicesPlugin.getDefault(),
						CommonUIServicesDebugOptions.EXCEPTIONS_CATCHING,
						getClass(), "provides", e); //$NON-NLS-1$
					Log.error(CommonUIServicesPlugin.getDefault(),
						CommonUIServicesStatusCodes.SERVICE_FAILURE, e
							.getLocalizedMessage(), e);
					doesProvide = false;
				}
			}

			return doesProvide;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.internal.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
			IConfigurationElement element) {
		return new MarkerNavigationProviderDescriptor(element);
	}

	/**
	 * The MarkerNavigationService constructor
	 */
	protected MarkerNavigationService() {
		super(true);
	}

	/**
	 * Gets the instance of MarkerNavigationService
	 * 
	 * @return Returns the <code>instance</code> variable
	 */
	public static MarkerNavigationService getInstance() {
		return instance;
	}

	/**
	 * Executes the <code>GotoMarkerOperation</code> operation using the FIRST
	 * strategy
	 * 
	 * @param operation
	 *            The <code>GotoMarkerOperation</code> operation
	 */
	private void execute(GotoMarkerOperation operation) {
		execute(ExecutionStrategy.FIRST, operation);
	}

	/**
	 * Perform the navigation to a marker in the associated editor.
	 * <p>
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.internal.marker.IMarkerNavigationProvider#gotoMarker(org.eclipse.ui.IEditorPart,
	 *      org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker(final IEditorPart editor, final IMarker marker) {
		assert null != editor;
		assert null != marker;

		// RATLC00524228
		// Do the operation in an asyncExec to work around a concurrent
		// modification problem in the core Eclipse Problems view.
		// When the Problems view is in the same part site as the Property
		// sheet (or any other Aurora view that shows model elements), the
		// marker navigation operation triggered by selection changed in the
		// Problems view causes the Model Explorer part to become active
		// and all AbstractActionHandlers to remove their selection listeners
		// from the Problems view's selection provider. Because this selection
		// provider stores listeners in an ArrayList instead of a JFace
		// ListenerList, a ConcurrentModificationException is thrown when the
		// selection provider attempts to notify the next selection listener
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				execute(new GotoMarkerOperation(editor, marker));
			}
		});
	}

}