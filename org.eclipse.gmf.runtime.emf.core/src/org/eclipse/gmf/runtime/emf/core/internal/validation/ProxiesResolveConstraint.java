/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.emf.core.internal.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentsEList;

import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCorePlugin;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;

/**
 * Implements a validator that collects model reference errors and forwards them on to
 * the EMF model validation service.
 *
 * @author Steve Gutz (sgutz)
 */
public class ProxiesResolveConstraint
	extends AbstractModelConstraint {
	
	/** The rule ID for resource file path errors */
	public static final String	RESOURCE_RULE_ID	= "org.eclipse.gmf.runtime.emf.core.ResourceFixup"; //$NON-NLS-1$
	/** The rule ID for element ID errors */
	public static final String	ELEMENT_RULE_ID		= "org.eclipse.gmf.runtime.emf.core.IdFixup"; //$NON-NLS-1$

	public static final String DIAGNOSTIC_SOURCE = EMFCorePlugin.getPluginId();
	
	/**
	 * Initializes me.
	 */
	public ProxiesResolveConstraint() {
		super();
	}

	private String getObjectLabel(EObject eObject)
	{
		return EMFCoreUtil.getQualifiedName(eObject,true);
	}
	
	private String getFeatureLabel(EStructuralFeature eStructuralFeature)
	{
		return eStructuralFeature.getName();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	public IStatus validate(IValidationContext ctx) {
    	EObject target = ctx.getTarget();
    	
    	EStructuralFeature feature = null;
    	EObject proxyObject = null;
    	
		for (EContentsEList.FeatureIterator i = (EContentsEList.FeatureIterator) target
			.eCrossReferences().iterator(); i.hasNext();) {
			
			EObject eCrossReferenceObject = (EObject) i.next();
			
			if (!i.feature().isDerived() && eCrossReferenceObject.eIsProxy()) {
				feature = i.feature();
				proxyObject = eCrossReferenceObject;
				ctx.addResult(proxyObject);
				break;
			}
		}
		
		if (feature != null) {
			ResourceSet rSet = target.eResource().getResourceSet();
			
			// Load the resource for the broken element
			URI proxyURI = ((InternalEObject)proxyObject).eProxyURI().trimFragment();
			Resource resource = rSet.getResource(proxyURI, false);

			// If we found a resource and it is loaded then the ID must be
			// incorrect
			if (resource != null && resource.isLoaded()) {
				
				// If we have been asked to find this case through the element fixup
				//  constraint then we will report this problem.
				if (ctx.getCurrentConstraintId().equals(ELEMENT_RULE_ID)) {
					return ctx.createFailureStatus(new Object[] {
						getFeatureLabel(feature), getObjectLabel(target),
						getObjectLabel(proxyObject)});
				}
				
				return ctx.createSuccessStatus();
			}
			// If we have been asked to find this case through the resource fixup
			//  constraint then we will report this problem.
			else if (ctx.getCurrentConstraintId().equals(RESOURCE_RULE_ID)) {
				return ctx.createFailureStatus(new Object[] {
					getFeatureLabel(feature), getObjectLabel(target),
					getObjectLabel(proxyObject)});
			}
		}
    	return ctx.createSuccessStatus();
	}

// cmcgee: KEEPING THIS CODE AROUND FOR FUTURE INSPECTION
//	/**
//	 * A simple status implementation to convert an EMF disagnostic
//	 *
//	 * @author Steve Gutz (sgutz)
//	 */
//	private final class FixupStatus implements IConstraintStatus {
//		
//		Diagnostic diagnostic;
//		IModelConstraint constraint;
//
//		public FixupStatus(Diagnostic diagnostic, IModelConstraint constraint) {
//			this.diagnostic = diagnostic;
//			this.constraint = constraint;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.core.runtime.IStatus#getChildren()
//		 */
//		public IStatus[] getChildren() {
//			List l = diagnostic.getChildren();
//			List paredList = new ArrayList();
//			for (int iCtr = l.size() - 1; iCtr >= 0; iCtr--) {
//				Diagnostic child = (Diagnostic) l.get(iCtr);
//
//				// Only collect diagnostic errors for things we care about
//				if (child.getData().size() == 3) {
//					paredList.add(child);
//				}
//			}
//
//			IStatus[] converted = new FixupStatus[paredList.size()];
//			int iCtr = 0;
//			for (Iterator it = paredList.iterator(); it.hasNext();) {
//				converted[iCtr++] = new FixupStatus((Diagnostic) it.next(), constraint);
//			}
//			return converted;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.core.runtime.IStatus#getCode()
//		 */
//		public int getCode() {
//			return diagnostic.getCode();
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.core.runtime.IStatus#getMessage()
//		 */
//		public String getMessage() {
//			String msg = diagnostic.getMessage();
//			String source = ProxyUtil.getProxyQName( (InternalEObject)diagnostic.getData().get(0));
//			String target = ProxyUtil.getProxyQName( (InternalEObject)diagnostic.getData().get(2));
//
//			boolean foundSource = false;
//			int index = 0;
//			while ( index != -1 && index < msg.length()) {
//				
//				index = msg.indexOf('\'', index);
//				if (index > -1) {
//					int nextQuoteIndex = msg.indexOf('\'', index+1);
//					if( nextQuoteIndex != -1 ) {
//						String sub = msg.substring(index, nextQuoteIndex);
//						
//						if( sub.indexOf( '@') != -1 && sub.indexOf( '}') != -1) {
//							if( !foundSource ) {
//								msg = msg.substring(0, index+1)
//										+ source + msg.substring(nextQuoteIndex);
//								nextQuoteIndex = index+source.length()+1;
//								foundSource = true;
//							} else {
//								msg = msg.substring(0, index+1)
//								+ target + msg.substring(nextQuoteIndex);
//								nextQuoteIndex = index+target.length()+1;
//							}
//						}
//						index = nextQuoteIndex+1;
//					}
//				}
//			}
//			return msg;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.core.runtime.IStatus#getSeverity()
//		 */
//		public int getSeverity() {
//			return diagnostic.getSeverity();
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.core.runtime.IStatus#getException()
//		 */
//		public Throwable getException() {
//			return null;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.core.runtime.IStatus#getPlugin()
//		 */
//		public String getPlugin() {
//			return MSLPlugin.getPluginId();
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.core.runtime.IStatus#isMultiStatus()
//		 */
//		public boolean isMultiStatus() {
//			List l = diagnostic.getChildren();
//			if (l == null || l.size() == 0)
//				return false;
//			return true;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.core.runtime.IStatus#isOK()
//		 */
//		public boolean isOK() {
//			return false;
//		}
//	
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.core.runtime.IStatus#matches(int)
//		 */
//		public boolean matches(int severityMask) {
//			return (diagnostic.getSeverity() & severityMask) != 0;
//		}
//
//		/* (non-Javadoc)
//		 * @see org.eclipse.gmf.runtime.emf.core.IValidationStatus#getTarget()
//		 */
//		public EObject getTarget() {
//			if (diagnostic.getData() == null)
//				return null;
//
//			Object obj = diagnostic.getData().get(0);
//			if (obj instanceof EObject)
//				return (EObject) obj;
//			return null;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.eclipse.gmf.runtime.emf.core.IValidationStatus#getRelatedObjects()
//		 */
//		public Set getRelatedObjects() {
//			HashSet set = new HashSet();
//			set.add(diagnostic.getData().get(2));
//			return set;
//		}
//
//		/* (non-Javadoc)
//		 * @see org.eclipse.emf.validation.model.IConstraintStatus#getConstraint()
//		 */
//		public IModelConstraint getConstraint() {
//			return constraint;
//		}
//
//		/* (non-Javadoc)
//		 * @see org.eclipse.emf.validation.model.IConstraintStatus#getResultLocus()
//		 */
//		public Set getResultLocus() {
//			if (diagnostic.getData() == null || diagnostic.getData().size() > 0) {
//				Object o = diagnostic.getData().get(0);
//				
//				// If the first object is an EObject, then we assume that this
//				//  is the EObject for which there is a problem. See the
//				//  javadoc for emf.common.util.Diagnostic.getData() for more
//				//  information.
//				if (o instanceof EObject) {
//					return Collections.singleton(o);
//				}
//			}
//			
//			return Collections.EMPTY_SET;
//		}
//	}
//	
//	/**
//	 * Overrides the basic EObjectValidator's proxy-resolvableness constraint
//	 * to check only those features that are not derived.
//	 *
//	 * @author Christian W. Damus (cdamus)
//	 */
//	private static final class EFixupValidator
//		extends EObjectValidator {
//
//		public boolean validate_EveryProxyResolves(EObject eObject,
//				DiagnosticChain diagnostics, Map context) {
//			boolean result = true;
//			for (EContentsEList.FeatureIterator i = (EContentsEList.FeatureIterator) eObject
//				.eCrossReferences().iterator(); i.hasNext();) {
//				
//				EObject eCrossReferenceObject = (EObject) i.next();
//				
//				if (!i.feature().isDerived() && eCrossReferenceObject.eIsProxy()) {
//					result = false;
//					if (diagnostics != null) {
//						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
//							ProxiesResolveConstraint.DIAGNOSTIC_SOURCE,
//							EObjectValidator.EOBJECT__EVERY_PROXY_RESOLVES,
//							EcorePlugin.INSTANCE.getString(
//								"_UI_UnresolvedProxy_diagnostic", //$NON-NLS-1$
//								new Object[] {
//									getFeatureLabel(i.feature(), context),
//									getObjectLabel(eObject, context),
//									getObjectLabel(
//										eCrossReferenceObject,
//										context)}),
//								new Object[] {
//									eObject,
//									i.feature(),
//									eCrossReferenceObject}));
//					} else {
//						break;
//					}
//				}
//			}
//			return result;
//		}
//	}
}
