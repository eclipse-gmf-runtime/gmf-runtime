package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractShapeTests;
import org.eclipse.jface.util.Assert;


public class DiagramEventBrokerTests extends AbstractShapeTests{
	
	private class TestListenningEditPart extends GraphicalEditPart {
		
		private boolean receivedTypeEvent = false;
        private DiagramEventBroker eventBroker;
        private DiagramEditPart diagremEditPart; 
		
		public boolean receivedTypeEvent(){
			return receivedTypeEvent ; 
		}
        
        protected Diagram getDiagramView() {
           return diagremEditPart.getDiagramView();
        }

        public TestListenningEditPart(DiagramEditPart dEP,DiagramEventBroker eventBroker, EObject model){
            super(model);
            this.eventBroker = eventBroker;
            this.diagremEditPart = dEP;
            addListenerFilter("Type",this,model,NotationPackage.eINSTANCE.getView_Type()); //$NON-NLS-1$
        }
		
		public IFigure createFigure() {
			return null;
		}
        
                
        /**
         * Adds a listener filter by adding the given listener to a passed notifier
         * 
         * @param filterId
         *            A unique filter id (within the same editpart instance)
         * @param listener
         *            A listener instance
         * @param element
         *            An element to add the listener to
         */
        protected void addListenerFilter(String filterId,
                NotificationListener listener,
                EObject element,
                EStructuralFeature feature) {
            if (element == null)
                return;
            Assert.isNotNull(filterId);
            Assert.isNotNull(listener);
            eventBroker.addNotificationListener(element,feature,listener);
        }
		
		/**
		 * Create an instance.
		 * 
		 * @param model
		 *            the underlying model.
		 */
		public TestListenningEditPart(EObject model) {
			super(model);
		}
		
		/**
		 * Handles the property changed event
		 * 
		 * @param event
		 *            the property changed event
		 */
		protected void handleNotificationEvent(Notification event) {
			if (NotationPackage.eINSTANCE.getView_Type().equals(event.getFeature()))
					receivedTypeEvent = true;
		}
		
		/**
		 * indicates if this edit part's model is a view or not 
		 * @return <code>true</code> or <code>false</code>
		 */
		public boolean hasNotationView(){
			return false;
		}
		
		/**
		 * This method adds all listeners to the semantic world (IUMLElement...etc)
		 * Override this method to add more semantic listeners down the hierarchy
		 * This method is called only if the semantic element is resolvable
		 */
		protected void addSemanticListeners() {
			// do not add any thing
		}
	}
	
	public DiagramEventBrokerTests(String arg0) {
		super(arg0);
	}

	public static Test suite() {
		return new TestSuite(DiagramEventBrokerTests.class);
	}

	protected void setTestFixture() {
		testFixture = new LogicTestFixture();
	}
	
	/** Return <code>(CanonicalTestFixture)getTestFixture();</code> */
	protected LogicTestFixture getLogicTestFixture() {
		return (LogicTestFixture)getTestFixture();
	}
    
     /**
     * Gets the diagram event broker from the editing domain.
     * 
     * @return the diagram event broker
     */
    public DiagramEventBroker getDiagramEventBroker(TransactionalEditingDomain theEditingDomain) {
        if (theEditingDomain != null) {
            return DiagramEventBroker.getInstance(theEditingDomain);
        }
        return null;
    }
	
	/**
	 * Test to verify that copy appearance properties is working properly
	 * @throws Exception
	 */
	public void testDiagramEventBroker()
		throws Exception {
		final View view  = getDiagramEditPart().getNotationView();
        DiagramEditPart diagramEP = getDiagramEditPart();
        
		TestListenningEditPart ep = 
			new TestListenningEditPart(diagramEP,getDiagramEventBroker(diagramEP.getEditingDomain()),view);
        
		final TransactionalEditingDomain editingDomain = ep.getEditingDomain();
		ep.activate();
		AbstractEMFOperation operation = new AbstractEMFOperation(
			editingDomain, "") { //$NON-NLS-1$
			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info)
				throws ExecutionException {
				view.setType("ddd"); //$NON-NLS-1$
				return Status.OK_STATUS;
			};
		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(operation,
					new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			e.printStackTrace();
			assertFalse(false);
		}
		flushEventQueue();
		assertTrue(ep.receivedTypeEvent());
	}
}
