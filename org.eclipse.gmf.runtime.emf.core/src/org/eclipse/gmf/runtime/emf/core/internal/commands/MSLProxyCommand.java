/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;

/**
 * A proxy command makes an EObject proxy.
 * 
 * @author rafikj
 */
public class MSLProxyCommand
	extends MSLAbstractCommand {

	private EObject eObject = null;

	private URI proxyURI = null;

	/**
	 * Creates a proxy command.
	 */
	public static MCommand create(MSLEditingDomain domain, EObject eObject,
			URI proxyURI) {
		return new MSLProxyCommand(domain, eObject, proxyURI);
	}

	/**
	 * Constructor.
	 */
	private MSLProxyCommand(MSLEditingDomain domain, EObject eObject,
			URI proxyURI) {
		super(domain);
		this.eObject = eObject;
		this.proxyURI = proxyURI;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#dispose()
	 */
	public void dispose() {

		super.dispose();

		eObject = null;
		proxyURI = null;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#execute()
	 */
	public void execute() {
		((InternalEObject) eObject).eSetProxyURI(proxyURI);
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#undo()
	 */
	public void undo() {

		if (MSLConstants.PROXY_HACK) {

			if (eObject.eIsProxy()) {

				// attempt a resolve.
				final EObject resolved = EcoreUtil.resolve(eObject, domain
					.getResourceSet());

				if ((resolved != null) && (!resolved.eIsProxy())
					&& (resolved != eObject)) {

					final EClass eClass = resolved.eClass();

					assert eClass == eObject.eClass();

					if (eClass == eObject.eClass()) {

						// copy the guts of the resolved object to the proxy
						// object.
						domain.runSilent(new MRunnable() {

							public Object run() {

								for (Iterator i = eClass
									.getEAllStructuralFeatures().iterator(); i
									.hasNext();) {

									EStructuralFeature feature = (EStructuralFeature) i
										.next();

									if ((!feature.isChangeable())
										|| (feature.isDerived()))
										continue;

									if ((feature instanceof EReference)
										&& (((EReference) feature)
											.isContainer()))
										continue;

									if (feature.isMany()) {

										if (resolved.eIsSet(feature)) {

											Collection list = (Collection) resolved
												.eGet(feature);

											Collection newList = (Collection) eObject
												.eGet(feature);

											newList.clear();
											newList.addAll(list);

										} else if (eObject.eIsSet(feature)) {

											eObject.eUnset(feature);
										}

									} else if (resolved.eIsSet(feature)) {

										Object object = resolved.eGet(feature);

										eObject.eSet(feature, object);

									} else
										eObject.eUnset(feature);
								}

								// make the proxy object non proxy.
								((InternalEObject) eObject).eSetProxyURI(null);

								// remove the resolved object from its
								// container.
								EObject container = resolved.eContainer();

								if (container != null) {

									EReference reference = resolved
										.eContainmentFeature();

									if (reference.isMany())
										((Collection) container.eGet(reference))
											.remove(resolved);
									else
										container.eSet(reference, null);
								}

								// make the resolved object a proxy.
								((InternalEObject) resolved)
									.eSetProxyURI(proxyURI);

								return null;
							}
						});
					}
				}
			}

		} else
			eObject = EcoreUtil.resolve(eObject, domain.getResourceSet());
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#redo()
	 */
	public void redo() {
		((InternalEObject) eObject).eSetProxyURI(proxyURI);
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getParticipantTypes()
	 */
	public Set getParticipantTypes() {
		return eObject == null ? Collections.EMPTY_SET
			: Collections.singleton(EObjectUtil.getType(eObject));
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getType()
	 */
	public Type getType() {
		return MCommand.PROXY;
	}
}