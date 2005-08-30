/*
 * Licensed Materials - Use restricted, please refer to the "Samples Gallery" terms
 * and conditions in the IBM International Program License Agreement.
 *
 * © Copyright IBM Corporation 2005. All Rights Reserved. 
 */
package org.eclipse.gmf.examples.runtime.diagram.decorator.provider;

import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.CreateDecoratorsOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.notation.Node;


/**
 * @author sshaw
 *
 * Decorator provider for the review decorator class
 */
public class ReviewDecoratorProvider
	extends AbstractProvider
	implements IDecoratorProvider {

	/** The key used for the mood decoration */
	public static final String REVIEW = "Review_Decorator"; //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see com.ibm.xtools.presentation.services.decorator.IDecoratorProvider#createDecorators(com.ibm.xtools.presentation.services.decorator.IDecoratorTarget)
	 */
	public void createDecorators(IDecoratorTarget decoratorTarget) {
		Node node = ReviewDecorator.getDecoratorTargetNode(decoratorTarget);
		if (node != null) {
			decoratorTarget.installDecorator(REVIEW, new ReviewDecorator(decoratorTarget));
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
		return ReviewDecorator.getDecoratorTargetNode(decoratorTarget) != null;
	}

}
