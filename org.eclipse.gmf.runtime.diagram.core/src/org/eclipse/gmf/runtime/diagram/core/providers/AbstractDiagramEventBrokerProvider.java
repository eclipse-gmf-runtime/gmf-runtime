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

package org.eclipse.gmf.runtime.diagram.core.providers;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.core.services.eventbroker.CreateDiagramEventBrokerOperation;

/**
 * The Diagram Event Broker Factory Interface
 * This factory interface allows clients of the Diagram layer to provide
 * Their own Diagram event broker instances.
 * @author MMostafa
 */
abstract public class AbstractDiagramEventBrokerProvider
    extends AbstractProvider
    implements DiagramEventBrokerProvider {

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
     */
    public boolean provides(IOperation operation) {
        if (operation instanceof CreateDiagramEventBrokerOperation)
            return provides((CreateDiagramEventBrokerOperation) operation);
        return false;
    }

    /**
     * Indicates whether this provider provides the specified operation.
     * @return <code>true</code> if this provider provides the operation;
     *         <code>false</code> otherwise.
     * @param operation
     *            The operation in question.
     */
    abstract public boolean provides(CreateDiagramEventBrokerOperation operation);
}
