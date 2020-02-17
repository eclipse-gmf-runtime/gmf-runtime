/******************************************************************************
 * Copyright (c) 2010, BonitaSoft S.A.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Mickael Istria, BonitaSoft S.A. - Initial implementation (bug 288695) 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This class implements an IAdaptable to be used with {@link CreateUnspecifiedTypeRequest}.
 * It delegates the {@link IAdaptable#getAdapter(Class)} to the {@link CreateRequest} that
 * realizes this creation.
 * 
 * @author Mickael Istria, BonitaSoft S.A.
 * @since 1.5
 */
public class CreateUnspecifiedAdapter implements IAdaptable {

	private List<CreateRequest> requests;

	public CreateUnspecifiedAdapter() {
		this.requests = new ArrayList<CreateRequest>();
	}
	
	/**
	 * Adapt delegating to the {@link CreateRequest} that was actually executed.
	 */
	public Object getAdapter(Class adapter) {
		for (CreateRequest request : requests) {
			Object newObject = request.getNewObject(); 
			if (newObject != null &&
				newObject instanceof List<?> &&
				! ((List<?>)newObject).isEmpty()) {
				IAdaptable adaptable = (IAdaptable) ((List<?>)newObject).get(0);
				View view = (View) adaptable.getAdapter(View.class);
				if (view != null && view.getElement() != null) {
					return adaptable.getAdapter(adapter);
				}
			}
		}
		return null;
	}

	/**
	 * @param request A new request to add to the list of potentially executed
	 * requests
	 */
	public void add(CreateRequest request) {
		this.requests.add(request);
	}

}