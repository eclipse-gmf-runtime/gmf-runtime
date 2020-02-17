/******************************************************************************
 * Copyright (c) 2010 Obeo and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Mariot Chauvin <mariot.chauvin@obeo.fr> - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.tests.runtime.emf.ui.properties.sections;

import junit.framework.TestCase;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.ui.properties.sections.PropertySheetEntry;
import org.eclipse.gmf.runtime.emf.ui.properties.sections.UndoableModelPropertySheetEntry;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 *@author mchauvin 
 */
public class UndoableModelPropertySectionTest extends TestCase {

	
	/**
	 * @see bug 303184
	 */
	public void testEditorValueConsistentIfOperationIsCanceled() {
		
		TransactionalEditingDomain domain = createEditingDomain();
	   
		final Resource resource = domain.getResourceSet().createResource(URI.createURI("test.ecore"));
		
		final EPackage testPackage = createEPackage("test");
		final EPackage goodPackage = createEPackage("good one");
		final EPackage shouldBeCanceledPackage = createEPackage("should be canceled");
		
		
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {
				resource.getContents().add(testPackage);
				resource.getContents().add(goodPackage);
				resource.getContents().add(shouldBeCanceledPackage);
			}
		});
		
		
		final UndoableModelPropertySheetEntryForTest root = new UndoableModelPropertySheetEntryForTest(OperationHistoryFactory.getOperationHistory());
	    root.setEditingDomain(domain);
	    IPropertySourceProvider provider = new IPropertySourceProvider() {
			public IPropertySource getPropertySource(Object value) {
				return new PropertySourceForTests((EPackage) value);
			}
		}; 
	    root.setPropertySourceProvider(provider);
	    final EPackage[] values = {testPackage};
	    root.setValues(values);
	    
	    
	    final UndoableModelPropertySheetEntryForTest entry = (UndoableModelPropertySheetEntryForTest) root.createChildEntry();
	    entry.setValues(values);
	    entry.setPropertySourceProvider(provider);
	    
	    entry.setValue(goodPackage);
	    assertEquals(goodPackage, entry.getValueForTest());
	   
	    addPostCommitListenerToCancelTransaction(domain);
	    
	    entry.setValue(shouldBeCanceledPackage);
	    assertEquals(goodPackage, entry.getValueForTest());
	}
	
	 private TransactionalEditingDomain createEditingDomain() {
	        return TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
	 }
	
	 private EPackage createEPackage(final String name) {
			final EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
			ePackage.setName(name);
			return ePackage;
	 }
	 
	 private void addPostCommitListenerToCancelTransaction(TransactionalEditingDomain domain) {
		    domain.addResourceSetListener(new ResourceSetListenerImpl(NotificationFilter.ANY) {
	            @Override
	            public boolean isPrecommitOnly() {
	                return true;
	            }

				@Override
	            public Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException {
	                throw new RollbackException(Status.CANCEL_STATUS);
	            }
	        });
	 }
	 
	private final class UndoableModelPropertySheetEntryForTest extends UndoableModelPropertySheetEntry {

		public UndoableModelPropertySheetEntryForTest(IOperationHistory operationHistory) {
			super(operationHistory);
			descriptor = new TextPropertyDescriptor("id", "display");
		}
		
		@Override
		public void setValue(Object newValue) {
			super.setValue(newValue);
		}
		
		public Object getValueForTest() {
			return editValue;
		}

		@Override
		public PropertySheetEntry createChildEntry() {
			UndoableModelPropertySheetEntryForTest child  = new UndoableModelPropertySheetEntryForTest(getOperationHistory());
			child.parent = this;
			return child;
		}		
	}
	
	private final class PropertySourceForTests implements IPropertySource {

		private boolean isSet;
		
		private EPackage ePackage;
		
		public PropertySourceForTests(EPackage ePackage) {
			this.ePackage = ePackage;
		}
		
		public Object getEditableValue() {
			return ePackage.getName();
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			return null;
		}

		public Object getPropertyValue(Object id) {
			return ePackage.getName();
		}

		public boolean isPropertySet(Object id) {
			return isSet;
		}

		public void resetPropertyValue(Object id) {
			isSet = false;
		}

		public void setPropertyValue(Object id, Object value) {
			this.ePackage.setName((String) value);
			isSet = true;
		}
		
	}
}
