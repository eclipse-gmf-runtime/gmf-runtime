/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.commands;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.util.CrossReferenceAdapter;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypeDebugOptions;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.osgi.util.NLS;

/**
 * Command to create a model element using the EMF action protocol.
 * 
 * @author ldamus
 * @author Christian W. Damus (cdamus)
 */
public class DestroyElementCommand
	extends EditElementCommand {

	/**
	 * The element to be destroyed.
	 */
	private final EObject elementToDestroy;

	/**
	 * Constructs a new command to destroy a model element.
	 * 
	 * @param request
	 *            the destroy element request
	 */
	public DestroyElementCommand(DestroyElementRequest request) {

		super(request.getLabel(), request.getContainer(), request);
		this.elementToDestroy = request.getElementToDestroy();
	}
	
	/**
	 * Convenience method for destroying the specified object by executing a
	 * <code>DestroyElementCommand</code> on it, if it is attached to a
	 * resource.  Detached elements cannot be destroyed.
	 * <p>
	 * <b>Note</b> that the command will not be executed on the operation
	 * history.
	 * </b>
	 * 
	 * @param eObject an element to destroy
	 */
	public static void destroy(EObject eObject) {

		Resource resource = eObject.eResource();

		if (resource != null) {
			DestroyElementRequest destroy = new DestroyElementRequest(
					TransactionUtil.getEditingDomain(resource),
					eObject,
					false);
			
			IElementType context = ElementTypeRegistry.getInstance().getElementType(
					destroy.getEditHelperContext());
			ICommand command = context.getEditCommand(destroy);
		
			if (command != null && command.canExecute()) {
				try {
					command.execute(new NullProgressMonitor(), null);
				} catch (ExecutionException e) {
					Trace.catching(EMFTypePlugin.getPlugin(),
							EMFTypeDebugOptions.EXCEPTIONS_CATCHING,
							DestroyElementCommand.class, "destroy(EObject)", e); //$NON-NLS-1$
					Log.error(EMFTypePlugin.getPlugin(),
							EMFTypePluginStatusCodes.COMMAND_FAILURE,
							NLS.bind(EMFTypeCoreMessages.destroyCommandFailed,
									context.getDisplayName()),
							e);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand#doExecuteWithResult(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
	    throws ExecutionException {

		EObject destructee = getElementToDestroy();
		
		// only destroy attached elements
		if ((destructee != null) && (destructee.eResource() != null)) {
			// tear down incoming references
			tearDownIncomingReferences(destructee);
			
			// also tear down outgoing references, because we don't want
			//    reverse-reference lookups to find destroyed objects
			tearDownOutgoingReferences(destructee);
			
			// remove the object from its container
			EcoreUtil.remove(destructee);
			
			// in case it was cross-resource-contained
			Resource res = destructee.eResource();
			if (res != null) {
				res.getContents().remove(destructee);
			}
		}
		
		return CommandResult.newOKCommandResult();
	}
	
	/**
	 * Tears down references to the object that we are destroying, from all other
	 * objects in the resource set.
	 * 
	 * @param destructee the object being destroyed
	 */
	protected void tearDownIncomingReferences(EObject destructee) {
		CrossReferenceAdapter crossReferencer = CrossReferenceAdapter
			.getExistingCrossReferenceAdapter(destructee);
		if (crossReferencer != null) {
			Collection inverseReferences = crossReferencer
				.getInverseReferences(destructee);
			if (inverseReferences != null) {
				int size = inverseReferences.size();
				if (size > 0) {
					Setting setting;
					EReference eRef;
					Setting[] settings = (Setting[]) inverseReferences
						.toArray(new Setting[size]);
					for (int i = 0; i < size; ++i) {
						setting = settings[i];
						eRef = (EReference) setting.getEStructuralFeature();
						if (eRef.isChangeable() && (eRef.isDerived() == false)
							&& (eRef.isContainment() == false)
							&& (eRef.isContainer() == false)) {
							EcoreUtil.remove(setting.getEObject(), eRef,
								destructee);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Tears down outgoing unidirectional references from the object being
	 * destroyed to all other elements in the resource set.  This is required
	 * so that reverse-reference queries will not find the destroyed object.
	 * 
	 * @param destructee the object being destroyed
	 */
	protected void tearDownOutgoingReferences(EObject destructee) {
		for (Iterator iter = destructee.eClass().getEAllReferences().iterator(); iter.hasNext();) {
			EReference reference = (EReference) iter.next();
			
			// container/containment features are handled separately, and
			//   bidirectional references were handled via incomings
			if (reference.isChangeable() && !reference.isDerived()
					&& !reference.isContainer() && !reference.isContainment()
					&& (reference.getEOpposite() == null)) {
				
				if (destructee.eIsSet(reference)) {
					destructee.eUnset(reference);
				}
			}
		}
	}
	
	/**
	 * Gets the element to be destroyed.
	 * @return the element to be destroyed
	 */
	protected EObject getElementToDestroy() {
		return elementToDestroy;
	}

	public boolean canExecute() {
		return (elementToDestroy != null) && (elementToDestroy.eResource() != null);
	}
	
}