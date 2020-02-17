/******************************************************************************
 * Copyright (c) 2005, 2015 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal.descriptors;

import org.eclipse.gmf.runtime.emf.type.core.AdviceBindingInheritance;
import org.eclipse.gmf.runtime.emf.type.core.IAdviceBindingDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;


/**
 * Descriptor for edit helper advice. Used to prevent premature loading 
 * of the plugins that define the element matcher, edit helper advice and metamodel
 * descriptor classes.
 * 
 * @author ldamus
 */
public interface IEditHelperAdviceDescriptor extends IAdviceBindingDescriptor {
	
	/**
	 * A compatibility shim that wraps public-API binding descriptors in the
	 * legacy internal API. This is necessary because several public API classes
	 * publish API in terms of the internal {@link IEditHelperAdviceDescriptor}
	 * interface.
	 */
	class Shim implements IEditHelperAdviceDescriptor {
		private final IAdviceBindingDescriptor delegate;

		private Shim(IAdviceBindingDescriptor delegate) {
			super();

			this.delegate = delegate;
		}
		
		/**
		 * Obtains the legacy {@link IEditHelperAdviceDescriptor} view of an
		 * advice-binding {@code descriptor}, shimming it if necessary.
		 * 
		 * @param descriptor
		 *            an advice-binding descriptor of the public API
		 * 
		 * @return the {@code descriptor} if it implements the legacy API or
		 *         else a shim that does
		 */
		public static IEditHelperAdviceDescriptor cast(IAdviceBindingDescriptor descriptor) {
			return (descriptor == null) ? null
					: (descriptor instanceof IEditHelperAdviceDescriptor) ? (IEditHelperAdviceDescriptor) descriptor
							: new Shim(descriptor);
		}
		
		/**
		 * Obtains the public API {@link IAdviceBindingDescriptor} view of an
		 * advice-binding {@code descriptor}, un-shimming it if is is a shim to
		 * restore the client's original instance.
		 * 
		 * @param descriptor
		 *            an advice-binding descriptor of the public or legacy API
		 * 
		 * @return the {@code descriptor} if it not a shim, otherwise the
		 *         descriptor that it shims
		 */
		public static IAdviceBindingDescriptor uncast(IAdviceBindingDescriptor descriptor) {
			return (descriptor == null) ? null : (descriptor instanceof Shim) ? ((Shim) descriptor).delegate
					: descriptor;
		}

		public String getId() {
			return delegate.getId();
		}

		public String getTypeId() {
			return delegate.getTypeId();
		}

		public IElementMatcher getMatcher() {
			return delegate.getMatcher();
		}

		public IContainerDescriptor getContainerDescriptor() {
			return delegate.getContainerDescriptor();
		}

		public IEditHelperAdvice getEditHelperAdvice() {
			return delegate.getEditHelperAdvice();
		}

		public AdviceBindingInheritance getInheritance() {
			return delegate.getInheritance();
		}
	}
}