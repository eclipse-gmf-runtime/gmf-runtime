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

package org.eclipse.gmf.runtime.diagram.ui.services.editpart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.RootEditPart;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.DefaultCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.DefaultConnectorEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.DefaultNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.CreateRootEditPartOperation;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.EditPartOperation;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.EditPartProviderConfiguration;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.IEditPartProvider;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.Ratio;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A service that supports the creation of editpart elements.  Default editparts will be created
 * if no sub-implementation creates one.
 * @see #createGraphicEditPart(View)
 */
/*
 * @canBeSeenBy %partners
 */
final public class EditPartService
	extends Service
	implements IEditPartProvider, EditPartFactory {
	
	/**
	 * A descriptor for <code>ISemanticProvider</code> defined
	 * by a configuration element.
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {

		/** the provider configuration parsed from XML */
		private EditPartProviderConfiguration providerConfiguration;

		/**
		 * Constructs a <code>ISemanticProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element The configuration element describing the provider.
		 */
		public ProviderDescriptor(IConfigurationElement element) {
			super(element);

			this.providerConfiguration =
				EditPartProviderConfiguration.parse(element);
			Assert.isNotNull(providerConfiguration);
		}

		/**
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			if (getPolicy() != null)
				return getPolicy().provides(operation);
			if (provider == null) {
				if (isSupportedInExtention(operation)) {
					providerConfiguration = null;
					return getProvider().provides(operation);
				}
				return false;
			}
			return getProvider().provides(operation);
		}

		/**
		 * Cheks if the operation is supported by the XML extension
		 * @param operation
		 * @return <code> true</code> or <code>false</code>
		 */
		private boolean isSupportedInExtention(IOperation operation) {
			if (operation instanceof CreateGraphicEditPartOperation) {
				CreateGraphicEditPartOperation o = (CreateGraphicEditPartOperation) operation;
				return providerConfiguration.supports(o.getView());
			} else if (operation instanceof CreateRootEditPartOperation) {
				return providerConfiguration.supportsRootEditPart();
			}
			return false;
		}
		
		/** 
		 * the default implementation is overriden here to make it easier to debug
		 * XML providers, now when you select the ProviderDescriptor in the debug
		 * window the provider class name will be displayed
		 * @return the provider class name
		 */
		public String toString() {
			return getElement().getAttribute("class"); 	 //$NON-NLS-1$
		}
	}

	/** singelton instance. */
	private final static EditPartService instance = new EditPartService();

	/**
     * constructor
	 */
	private EditPartService() {
		super(true, false);
		configureProviders(DiagramUIPlugin.getPluginId(), "editpartProviders"); //$NON-NLS-1$
	}
	
	/**
	 * Signleton constructor.
	 * @return the Signleton instance
	 */
	public static EditPartService getInstance() {
		return instance;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
		IConfigurationElement element) {
		return new ProviderDescriptor(element);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#createPriorityCache()
	 */
	protected Map createPriorityCache() {
		return new HashMap();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#getCachingKey(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	protected Object getCachingKey(IOperation operation) {
		return ((EditPartOperation)operation).getCachingKey();
	}

	/** 
	 * Creates an <code>IGraphicalEditPart</code> instance by forwarding a <code>CreateGraphicEditPartOperation</code>
	 * to the registered providers.  The supplied parameter is the editpart's constructor
	 * parameter. <P>
	 * The following <i>default</i> editparts are created if none is created by a provider.
	 * <UL>
	 * <LI> <code>IDiagramView</code> ... <code>DiagramEditPart</code>
	 * <LI> <code>ILabelView</code< ... <code>LabelEditPart</code>
	 * <LI> <code>ITextCompartmentView</code> ... <code>TextCompartmentEditPart</code>
	 * </UL>
	 * @param view the view element <i>controlled</i> by the created editpart
	 * @return an instance.
	 */
	public IGraphicalEditPart createGraphicEditPart(View view) {
		if (view == null)
			return null;

		IGraphicalEditPart result = null;
		CreateGraphicEditPartOperation createGraphicEditPartOperation = 
			new CreateGraphicEditPartOperation(view);
		result =
				(IGraphicalEditPart) execute(createGraphicEditPartOperation);
		
		if (result == null) {
			if (view instanceof Node) {
				if (((Node)view).getLayoutConstraint() instanceof Ratio) {
					result = new DefaultCompartmentEditPart(view);					
				} else {
					result = new DefaultNodeEditPart(view);
				}
			} else if (view instanceof Edge) {
				result = new DefaultConnectorEditPart(view);
			} else if (view instanceof Diagram) {
				result = new DiagramEditPart(view);
			}
		}
		return result;
	}

	/**
	  * Executes the specified operation using the FIRST execution strategy;
	  * the first provider capable of honoring the supplied operation.
	  * @param operation
	  * @return the provider's return value (or <tt>null</tt> if there was no
	  * provider able to honor the supplied operation.
	  */
	private Object execute(IOperation operation) {
		List results = execute(ExecutionStrategy.FIRST, operation);
		return results.isEmpty() ? null : results.get(0);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, final Object model) {
		return (EditPart)MEditingDomainGetter.getMEditingDomain((View)model).runAsRead( new MRunnable() {
			public Object run() {
				return createGraphicEditPart((View)model);
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.IEditPartProvider#createRootEditPart()
	 */
	public RootEditPart createRootEditPart() {
		RootEditPart result;
		CreateRootEditPartOperation createRootEditPartOperation = new CreateRootEditPartOperation();
		result = (RootEditPart) execute(createRootEditPartOperation);

		// provide default implementation
		return (result == null) ? new DiagramRootEditPart()
			: result;
	}
	
}
