/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.services;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.providers.DiagramEventBrokerProvider;
import org.eclipse.gmf.runtime.diagram.core.services.eventbroker.CreateDiagramEventBrokerOperation;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateViewOperation;

/**
 * A service for manipulating Diagram Event broker creations
 * 
 * @author mmostafa
 */
public class DiagramEventBrokerService
    extends Service
    implements DiagramEventBrokerProvider {

    /**
     * The singleton instance of the Diagram Event Broker Factory service.
     */
    private final static DiagramEventBrokerService instance = new DiagramEventBrokerService();

    static {
        instance.configureProviders(DiagramPlugin.getPluginId(),
            "diagramEventBrokerProviders"); //$NON-NLS-1$
    }

    /**
     * Retrieves the singleton instance of the diagram Event Broker Factory
     * service
     * 
     * @return The diagram Event Broker Factory service singleton.
     */
    public static DiagramEventBrokerService getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.core.providers.DiagramEventBrokerFactory#createDiagramEventBroker(org.eclipse.emf.transaction.TransactionalEditingDomain)
     */
    public final DiagramEventBroker createDiagramEventBroker(
            TransactionalEditingDomain editingDomain) {
        return (DiagramEventBroker) executeUnique(ExecutionStrategy.FIRST,
            new CreateDiagramEventBrokerOperation(editingDomain));
    }

    protected static class ProviderDescriptor
        extends Service.ProviderDescriptor {

        private IConfigurationElement element;

        public ProviderDescriptor(IConfigurationElement element) {
            super(element);
            this.element = element;
        }

        /**
         * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
         */
        public boolean provides(IOperation operation) {
            if (!policyInitialized) {
                policy = getPolicy();
                policyInitialized = true;
            }
            if (policy != null)
                return policy.provides(operation);
            if (provider == null) {
                if (isSupportedInExtention(operation)) {
                    return getProvider().provides(operation);
                }
                return false;
            }
            return getProvider().provides(operation);
        }

        private boolean isSupportedInExtention(IOperation operation) {
            IConfigurationElement[] ids = element.getChildren("editingDomain");//$NON-NLS-1$
            if (null == ids || ids.length == 0)
                return false;
            String id = ((CreateDiagramEventBrokerOperation) operation)
                .getEditingDomain().getID();
            if (null == id)
                return false;
            for (int i = 0; i < ids.length; i++) {
                if (id.equals(ids[i].getAttribute("ID")))//$NON-NLS-1$
                    return true;
            }
            return false;
        }
    }

    /**
     * @see org.eclipse.gmf.runtime.common.core.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
     */
    protected Service.ProviderDescriptor newProviderDescriptor(
            IConfigurationElement _element) {
        return new ProviderDescriptor(_element);
    }

    /**
     * @see org.eclipse.gmf.runtime.common.core.service.Service#createPriorityCache()
     */
    protected Map createPriorityCache() {
        return new HashMap();
    }

    /**
     * @see org.eclipse.gmf.runtime.common.core.service.Service#getCacheKey(org.eclipse.gmf.runtime.common.core.service.IOperation)
     */
    protected Object getCachingKey(IOperation operation) {
        return ((CreateViewOperation) operation).getCachingKey();
    }
}
