/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.ObjectAdapter;
import org.eclipse.gmf.runtime.diagram.ui.commands.PopupMenuCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.SelectExistingElementForSourceOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.SelectExistingElementForTargetOperation;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * <p>
 * A command that pops up a menu which can allow the user to select the type of
 * connector to be created and whether they want to create a new type or
 * select an existing element for the other end of the connector.
 * </p>
 * 
 * <p>
 * The <code>getRelationshipTypeAdapter()</code> method returns an adaptable
 * to the relationship type result.
 * </p>
 * 
 * <p>
 * The <code>getEndAdapter()</code> method returns an adaptable to the end
 * type result.
 * </p>
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class GetConnectorTypeAndEndCommand
	extends PopupMenuCommand {

	/**
	 * Label provider of the first popup menu with the relationship types.
	 */
	private class ConnectorLabelProvider
		extends ElementTypeLabelProvider {

		/**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			String theInputStr = null;
			if (isDirectionReversed())
				theInputStr = PresentationResourceManager
					.getI18NString("ConnectorHandle.Popup.CreateXFrom"); //$NON-NLS-1$
			else
				theInputStr = PresentationResourceManager
					.getI18NString("ConnectorHandle.Popup.CreateXTo"); //$NON-NLS-1$

			String text = MessageFormat.format(theInputStr, new Object[] {super
				.getText(element)});

			return text;

		}
	}

	/**
	 * Label provider of the second popup (submenus) for the type of the other
	 * end.
	 */
	private class EndLabelProvider
		extends ElementTypeLabelProvider {

		/**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			if (element instanceof IElementType) {
				String theInputStr = PresentationResourceManager
					.getI18NString("ConnectorHandle.Popup.NewX"); //$NON-NLS-1$
				String text = MessageFormat.format(theInputStr,
					new Object[] {((IElementType) element).getDisplayName()});
				return text;
			} else {
				return element.toString();
			}
		}
	}

	/**
	 * Label provider of the first and only popup for the type of the other end
	 * when there is only one connector type (e.g. a single relationship type
	 * palette tool is used).
	 */
	private class EndWithKnownConnectorTypeLabelProvider
		extends ElementTypeLabelProvider {

		/** the known connector type */
		private IElementType connectorType;

		/**
		 * Creates a new <code>EndWithKnownConnectorTypeLabelProvider</code>.
		 * 
		 * @param connectorType
		 */
		protected EndWithKnownConnectorTypeLabelProvider(IElementType connectorType) {
			this.connectorType = connectorType;
		}

		/**
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			String theInputStr = null;
			if (element instanceof IElementType) {
				if (isDirectionReversed())
					theInputStr = PresentationResourceManager
						.getI18NString("ConnectorHandle.Popup.CreateXFromNewY"); //$NON-NLS-1$
				else
					theInputStr = PresentationResourceManager
						.getI18NString("ConnectorHandle.Popup.CreateXToNewY"); //$NON-NLS-1$
				String text = MessageFormat.format(theInputStr, new Object[] {
					connectorType.getDisplayName(),
					((IElementType) element).getDisplayName()});
				return text;
			} else {
				if (isDirectionReversed())
					theInputStr = PresentationResourceManager
						.getI18NString("ConnectorHandle.Popup.CreateXFromY"); //$NON-NLS-1$
				else
					theInputStr = PresentationResourceManager
						.getI18NString("ConnectorHandle.Popup.CreateXToY"); //$NON-NLS-1$
				String text = MessageFormat.format(theInputStr, new Object[] {
					connectorType.getDisplayName(), super.getText(element)});
				return text;
			}

		}
	}

	/**
	 * This can be added to the content list to add a 'select existing' option.
	 */
	private static String EXISTING_ELEMENT = PresentationResourceManager
		.getI18NString("ConnectorHandle.Popup.ExistingElement"); //$NON-NLS-1$

	/** Label provider of the popup menu for the connector types. */
	private ConnectorLabelProvider connectorLabelProvider;

	/** Label provider of the submenus for the other end element. */
	private EndLabelProvider endLabelProvider;

	/** Adapts to the connector type result. */
	private ObjectAdapter connectorTypeAdapter = new ObjectAdapter();

	/** Adapts to the other end type result. */
	private ObjectAdapter endAdapter = new ObjectAdapter();

	/**
	 * The request to create a connection. It may contain the connector type
	 * or it may be a <code>CreateUnspecifiedTypeConnectionRequest</code>.
	 */
	private CreateConnectionRequest request;


	/** The container editpart to send the view request to. */
	private IGraphicalEditPart containerEP;

	/**
	 * Creates a new <code>GetConnectorTypeAndEndCommand</code>.
	 * 
	 * @param request
	 *            The request to create a connection. It may contain the
	 *            connector type or it may be a
	 *            <code>CreateUnspecifiedTypeConnectionRequest</code>.
	 * @param containerEP
	 *            The container edit part, where the view and element request to
	 *            create the other end is sent. This is used only for testing
	 *            that a type is valid for the other end.
	 */
	public GetConnectorTypeAndEndCommand(CreateConnectionRequest request, IGraphicalEditPart containerEP) {

		super(PresentationResourceManager
			.getI18NString("Command.GetRelationshipTypeAndEndFromUser.Label"), //$NON-NLS-1$
			Display.getCurrent().getActiveShell());
		this.request = request;
		this.containerEP = containerEP;
		this.endLabelProvider = this.new EndLabelProvider();
		this.connectorLabelProvider = this.new ConnectorLabelProvider();
	}

	/**
	 * Gets the content to be used in the popup menu from the Modeling Assistant
	 * Service and creates the popup menu.
	 * 
	 * @return the top-level popup menu
	 */
	private PopupMenu createPopupMenu() {

		List validRelTypes = new ArrayList();
		if (request instanceof CreateUnspecifiedTypeConnectionRequest) {
			List allRelTypes = null;
			if (((CreateUnspecifiedTypeConnectionRequest) request)
				.useModelingAssistantService()) {
				allRelTypes = isDirectionReversed() ? ModelingAssistantService
					.getInstance().getRelTypesOnTarget(getKnownEnd())
					: ModelingAssistantService.getInstance()
						.getRelTypesOnSource(getKnownEnd());
			} else {
				allRelTypes = ((CreateUnspecifiedTypeConnectionRequest) request)
					.getElementTypes();
			}

			if (isDirectionReversed()) {
				validRelTypes = allRelTypes;
			} else {
				// Cycle through and make sure each connector type is
				// supported
				// for starting a connection on the source.
				for (Iterator iter = allRelTypes.iterator(); iter.hasNext();) {
					IElementType rType = (IElementType) iter.next();
					if (((CreateConnectionRequest) ((CreateUnspecifiedTypeConnectionRequest) request)
						.getRequestForType(rType)).getStartCommand() != null) {
						validRelTypes.add(rType);
					}
				}
			}

		} else if (request instanceof CreateConnectorViewAndElementRequest) {
			if (((CreateConnectorViewAndElementRequest) request)
				.getStartCommand() != null) {
				validRelTypes
					.add(((CreateRelationshipRequest) ((CreateConnectorViewAndElementRequest) request)
						.getConnectorViewAndElementDescriptor()
						.getCreateElementRequestAdapter().getAdapter(
							CreateRelationshipRequest.class))
						.getElementType());
			}
		} else if (request instanceof CreateConnectorViewRequest) {
			if (((CreateConnectorViewRequest) request).getStartCommand() != null) {
				Object type = ((CreateConnectorViewRequest) request)
					.getConnectorViewDescriptor().getElementAdapter()
					.getAdapter(IElementType.class);
				if (type != null) {
					validRelTypes.add(type);
				}
			}
		}
		
		if (validRelTypes.isEmpty()) {
			return null;
		} else if (validRelTypes.size() == 1) {
			final IElementType rType = (IElementType) validRelTypes.get(0);
			List menuContent = isDirectionReversed() ? ModelingAssistantService
				.getInstance().getTypesForSource(getKnownEnd(), rType)
				: ModelingAssistantService.getInstance().getTypesForTarget(
					getKnownEnd(), rType);
			menuContent = filterUnsupportedNodeTypes(menuContent);
			if (!menuContent.isEmpty()) {
				if (supportsExistingElement(rType)) {
					menuContent.add(EXISTING_ELEMENT);
				}

				ILabelProvider labelProvider = this.new EndWithKnownConnectorTypeLabelProvider(
					rType);
				return new PopupMenu(menuContent, labelProvider) {

					/**
					 * @see org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu#getResult()
					 */
					public Object getResult() {
						Object endResult = super.getResult();
						if (endResult == null) {
							return null;
						} else {
							List resultList = new ArrayList(2);
							resultList.add(rType);
							resultList.add(endResult);
							return resultList;
						}
					}
				};
			}
		} else {
			List menuContent = new ArrayList();
			for (Iterator iter = validRelTypes.iterator(); iter.hasNext();) {
				IElementType rType = (IElementType) iter.next();

				List subMenuContent = isDirectionReversed() ? ModelingAssistantService
					.getInstance().getTypesForSource(getKnownEnd(), rType)
					: ModelingAssistantService.getInstance().getTypesForTarget(
						getKnownEnd(), rType);
				subMenuContent = filterUnsupportedNodeTypes(subMenuContent);
				if (subMenuContent.isEmpty()) {
					continue;
				}
				if (supportsExistingElement(rType)) {
					subMenuContent.add(EXISTING_ELEMENT);
				}

				PopupMenu subMenu = new PopupMenu(subMenuContent,
					endLabelProvider);

				menuContent.add(new PopupMenu.CascadingMenu(rType, subMenu));
			}
			if (!menuContent.isEmpty()) {
				return new PopupMenu(menuContent, connectorLabelProvider);
			}
		}
		return null;
	}

	/**
	 * Returns a new list with all the types from the list given that can be
	 * created.
	 * 
	 * @param allTypes
	 *            a list of <code>IElementTypes</code>.
	 */
	private List filterUnsupportedNodeTypes(List allTypes) {
		List validTypes = new ArrayList();
		for (Iterator iter = allTypes.iterator(); iter.hasNext();) {
			IElementType type = (IElementType) iter.next();
			Request createRequest = CreateViewRequestFactory
				.getCreateShapeRequest(type, containerEP
					.getDiagramPreferencesHint());
			
			EditPart target = containerEP.getTargetEditPart(createRequest);
			if (target != null) {
				Command cmd = target.getCommand(createRequest);
				if (cmd != null && cmd.canExecute()) {
					validTypes.add(type);
				}
			}
		}
		return validTypes;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isExecutable()
	 */
	public boolean isExecutable() {
		return createPopupMenu() != null;
	}

	/**
	 * Pops up the dialog with the content provided. If the user selects 'select
	 * existing', then the select elements dialog also appears.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		PopupMenu popup = createPopupMenu();

		if (popup == null) {
			return newErrorCommandResult(getLabel());
		}

		setPopupMenu(popup);

		CommandResult cmdResult = super.doExecute(progressMonitor);
		if (!cmdResult.getStatus().isOK()) {
			return cmdResult;
		}

		Object result = cmdResult.getReturnValue();
		if (result instanceof List) {
			List resultList = (List) result;
			if (resultList.size() == 2) {
				connectorTypeAdapter.setObject(resultList.get(0));

				Object targetResult = resultList.get(1);

				if (targetResult.equals(EXISTING_ELEMENT)) {
					targetResult = isDirectionReversed() ? ModelingAssistantService
						.getInstance().selectExistingElementForSource(
							getKnownEnd(), (IElementType) resultList.get(0))
						: ModelingAssistantService.getInstance()
							.selectExistingElementForTarget(getKnownEnd(),
								(IElementType) resultList.get(0));
					if (targetResult == null) {
						return newCancelledCommandResult();
					}
				}
				endAdapter.setObject(targetResult);
				return newOKCommandResult();
			}
		}
		return newErrorCommandResult(getLabel());
	}

	/**
	 * Checks if the <code>ModelingAssistantService</code> supports the
	 * ability to open a dialog for the user to select an existing element
	 * 
	 * @param connectorType
	 * @return true if the supported by the modeling assistant service; false
	 *         otherwise
	 */
	private boolean supportsExistingElement(IElementType connectorType) {
		if (isDirectionReversed()) {
			if (ModelingAssistantService.getInstance().provides(
				new SelectExistingElementForSourceOperation(getKnownEnd(),
					connectorType))) {
				return true;
			}
		} else if (ModelingAssistantService.getInstance().provides(
			new SelectExistingElementForTargetOperation(getKnownEnd(),
				connectorType))) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the connectorTypeAdapter.
	 * 
	 * @return Returns the connectorTypeAdapter.
	 */
	public ObjectAdapter getConnectorTypeAdapter() {
		return connectorTypeAdapter;
	}

	/**
	 * Gets the endAdapter.
	 * 
	 * @return Returns the endAdapter.
	 */
	public ObjectAdapter getEndAdapter() {
		return endAdapter;
	}

	/**
	 * Returns true if the request is a reversed
	 * <code>CreateUnspecifiedTypeConnectionRequest</code>.
	 * 
	 * @return Returns true if the request is a reversed
	 *         <code>CreateUnspecifiedTypeConnectionRequest</code>; false
	 *         otherwise
	 */
	private boolean isDirectionReversed() {
		return (request instanceof CreateUnspecifiedTypeConnectionRequest && ((CreateUnspecifiedTypeConnectionRequest) request)
			.isDirectionReversed());
	}

	/**
	 * Gets the known end, which even in the case of a reversed
	 * <code>CreateUnspecifiedTypeConnectionRequest</code>, is the source
	 * editpart.
	 * 
	 * @return the known end
	 */
	private EditPart getKnownEnd() {
		return request.getSourceEditPart();
	}
}