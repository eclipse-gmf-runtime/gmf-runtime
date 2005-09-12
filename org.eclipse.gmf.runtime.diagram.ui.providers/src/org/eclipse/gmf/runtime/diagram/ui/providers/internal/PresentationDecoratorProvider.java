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

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.CreateDecoratorsOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorKeys;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Provides decorators for the presentation layer.
 *
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 */
public class PresentationDecoratorProvider
	extends AbstractProvider
	implements IDecoratorProvider {

	/* (non-Javadoc)
	 * @see com.ibm.xtools.presentation.services.decorator.IDecoratorProvider#createDecorators(com.ibm.xtools.presentation.services.decorator.IDecoratorTarget)
	 */
	public void createDecorators(IDecoratorTarget decoratorTarget) {
		EditPart ep = (EditPart)decoratorTarget.getAdapter(EditPart.class);
		if (ep != null && ep instanceof IPrimaryEditPart) {
			decoratorTarget.installDecorator(IDecoratorKeys.BOOKMARK,
				new BookmarkDecorator(decoratorTarget));
			Object model = ep.getModel();
			if(!(model instanceof View))
				return;
			if (((View)model).getElement() != null) {
				// needs to have a reference that isn't null (i.e. have a semantic element)
				decoratorTarget.installDecorator(IDecoratorKeys.UNRESOLVED_VIEW,
					new UnresolvedViewDecorator(decoratorTarget));
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.internal.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		Assert.isNotNull(operation);

		if (!(operation instanceof CreateDecoratorsOperation)) {
			return false;
		}

		IDecoratorTarget decoratorTarget = ((CreateDecoratorsOperation) operation)
			.getDecoratorTarget();
		return decoratorTarget.getAdapter(View.class) != null;
	}

}
