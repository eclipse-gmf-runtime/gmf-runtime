/*
 * Created on May 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.view;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import com.ibm.xtools.notation.Node;
import com.ibm.xtools.notation.View;


/**
 * @author melaasar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CreateNodeViewOperation extends CreateChildViewOperation 
{
	/**
	 * Constructor for CreateNodeViewOperation.
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param persisted
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateNodeViewOperation(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint,
		int index,
		boolean persisted, PreferencesHint preferencesHint) {
		super(
			semanticAdapter,
			containerView,
			semanticHint,
			index,
			persisted, preferencesHint);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateViewOperation#getViewKind()
	 */
	public Class getViewKind() {
		return Node.class;
	}

	/**
	 * @see com.ibm.xtools.common.service.IOperation#execute(IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IViewProvider) provider).createNodeView(
			getSemanticAdapter(),
			getContainerView(),
			getSemanticHint(),
			getIndex(),
			getPersisted(), getPreferencesHint());
	}

}
