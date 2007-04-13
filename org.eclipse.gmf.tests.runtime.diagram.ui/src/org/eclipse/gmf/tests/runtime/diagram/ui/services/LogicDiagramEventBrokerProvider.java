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
