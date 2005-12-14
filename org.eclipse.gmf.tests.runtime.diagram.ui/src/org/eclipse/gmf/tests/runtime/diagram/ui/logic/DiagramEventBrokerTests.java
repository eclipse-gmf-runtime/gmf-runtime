package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractShapeTests;


public class DiagramEventBrokerTests extends AbstractShapeTests{
	
	private class TestListenningEditPart extends GraphicalEditPart {
		
		private boolean receivedTypeEvent = false;
		
		public boolean receivedTypeEvent(){
			return receivedTypeEvent ; 
		}
		
		public IFigure createFigure() {
			return null;
		}
		
		/**
		 * Create an instance.
		 * 
		 * @param model
		 *            the underlying model.
		 */
		public TestListenningEditPart(EObject model) {
			super(model);
			addListenerFilter("Type",this,model,NotationPackage.eINSTANCE.getView_Type()); //$NON-NLS-1$
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
		return new TestSuite(LogicShapeTests.class);
	}

	protected void setTestFixture() {
		testFixture = new LogicTestFixture();
	}
	
	/** Return <code>(CanonicalTestFixture)getTestFixture();</code> */
	protected LogicTestFixture getLogicTestFixture() {
		return (LogicTestFixture)getTestFixture();
	}
	
	/**
	 * Test to verify that copy appearance properties is working properly
	 * @throws Exception
	 */
	public void testDiagramEventBroker()
		throws Exception {
		final View view  = getDiagramEditPart().getNotationView();
		TestListenningEditPart ep = 
			new TestListenningEditPart(view);
		final MEditingDomain editingDomain = MEditingDomainGetter.getMEditingDomain((EObject)ep.getModel());
		ep.activate();
		//start read action here 
		editingDomain.runInUndoInterval(new Runnable() {
			public void run() {
				try {
					editingDomain.runAsWrite(new MRunnable() {
						public Object run() {
							view.setType("ddd"); //$NON-NLS-1$
							return null;
						}});
				} catch (MSLActionAbandonedException e) {
					// do nothing
				}
			}});
		flushEventQueue();
		assertTrue(ep.receivedTypeEvent());
	}
}
