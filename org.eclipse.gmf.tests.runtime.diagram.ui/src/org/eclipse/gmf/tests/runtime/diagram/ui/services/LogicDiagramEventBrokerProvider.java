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

package org.eclipse.gmf.tests.runtime.diagram.ui.services;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractDiagramEventBrokerProvider;
import org.eclipse.gmf.runtime.diagram.core.services.eventbroker.CreateDiagramEventBrokerOperation;


public class LogicDiagramEventBrokerProvider extends AbstractDiagramEventBrokerProvider {

    public DiagramEventBroker createDiagramEventBroker(TransactionalEditingDomain editingDomain) {
        return new LogicDiagramEventBroker(editingDomain);
    }

    public boolean provides(CreateDiagramEventBrokerOperation operation) {
        return operation.getEditingDomain().getID().equals("org.eclipse.gmf.examples.runtime.diagram.logicEditingDomain"); //$NON-NLS-1$
    }
}
