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

package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.ObjectAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.ElementTypeLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.SelectExistingElementForSourceOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.SelectExistingElementForTargetOperation;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

/**
 * <p>
 * A command that pops up a menu which can allow the user to select the type of
 * connection to be created and whether they want to create a new type or select
 * an existing element for the other end of the connection.
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
 */
public class PromptForConnectionAndEndCommand
	extends PopupMenuCommand {

	/**
	 * Label provider of the first popup menu with the relationship types.
	 */
	protected class ConnectionLabelProvider
		extends ElementTypeLabelProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			String theInputStr = null;
			if (isDirectionReversed())
				theInputStr = DiagramUIMessages.ConnectionHandle_Popup_CreateXFrom;
			else
				theInputStr = DiagramUIMessages.ConnectionHandle_Popup_CreateXTo;

			String text = NLS.bind(theInputStr, super
				.getText(element));

			return text;

		}
	}

	/**
	 * Label provider of the second popup (submenus) for the type of the other
	 * end.
	 */
	protected class EndLabelProvider
		extends ElementTypeLabelProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			if (element instanceof IElementType) {
				String theInputStr = DiagramUIMessages.ConnectionHandle_Popup_NewX;
				String text = NLS.bind(theInputStr,
					super.getText(element));
				return text;
			} else {
				return element.toString();
			}
		}
	}

	/**
	 * Label provider of the first and only popup for the type of the other end
	 * when there is only one connection type (e.g. a single relationship type
	 * palette tool is used).
	 */
	protected class ConnectionAndEndLabelProvider
		extends ElementTypeLabelProvider {

		/** the known connection item */
		private Object connectionItem;

		/**
		 * Creates a new <code>ConnectionAndEndLabelProvider</code>.
		 * 
		 * @param connectionType
		 *            the single known connection type
		 */
		protected ConnectionAndEndLabelProvider(Object connectionItem) {
			this.connectionItem = connectionItem;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			String theInputStr = null;
			if (element instanceof IElementType) {
				if (isDirectionReversed())
					theInputStr = DiagramUIMessages.ConnectionHandle_Popup_CreateXFromNewY;
				else
					theInputStr = DiagramUIMessages.ConnectionHandle_Popup_CreateXToNewY;
				String text = NLS.bind(theInputStr, new Object[] {
					super.getText(connectionItem), super.getText(element)});
				return text;
			} else {
				if (isDirectionReversed())
					theInputStr = DiagramUIMessages.ConnectionHandle_Popup_CreateXFromY;
				else
					theInputStr = DiagramUIMessages.ConnectionHandle_Popup_CreateXToY;
				String text = NLS.bind(theInputStr, new Object[] {
					super.getText(connectionItem), super.getText(element)});
				return text;
			}
		}

		/**
		 * Gets the connection item.
		 * 
		 * @return the connection item
		 */
		protected Object getConnectionItem() {
			return connectionItem;
		}

	}

	/**
	 * This can be added to the content list to add a 'select existing' option.
	 */
	private static String EXISTING_ELEMENT = DiagramUIMessages.ConnectionHandle_Popup_ExistingElement;

	/** Label provider of the popup menu for the connection types. */
	private static ConnectionLabelProvider connectionLabelProvider;

	/** Label provider of the submenus for the other end element. */
	private static EndLabelProvider endLabelProvider;

	/** Adapts to the connection type result. */
	private ObjectAdapter connectionAdapter = new ObjectAdapter();

	/** Adapts to the other end type result. */
	private ObjectAdapter endAdapter = new ObjectAdapter();

	/**
	 * The request to create a connection. It may contain the connection type or
	 * it may be a <code>CreateUnspecifiedTypeConnectionRequest</code>.
	 */
	private CreateConnectionRequest request;

	/** The container editpart to send the view request to. */
	private IGraphicalEditPart containerEP;

	/**
	 * Creates a new <code>PromptForConnectionAndEndCommand</code>.
	 * 
	 * @param request
	 *            The request to create a connection. It may contain the
	 *            connection type or it may be a
	 *            <code>CreateUnspecifiedTypeConnectionRequest</code>.
	 * @param containerEP
	 *            The container edit part, where the view and element request to
	 *            create the other end is sent. This is used only for testing
	 *            that a type is valid for the other end.
	 */
	public PromptForConnectionAndEndCommand(CreateConnectionRequest request,
			IGraphicalEditPart containerEP) {

		super(DiagramUIMessages.Command_GetRelationshipTypeAndEndFromUser_Label,
			Display.getCurrent().getActiveShell());
		this.request = request;
		this.containerEP = containerEP;
	}

	/**
	 * Gets a list of all the connection items that will represent the
	 * connection choices and will appear in the first part of the popup menu.
	 * 
	 * <p>
	 * If the objects in this are not <code>IElementTypes</code> or they
	 * require a special label provider, then
	 * {@link #getConnectionLabelProvider()} should be overridden to provide
	 * this.
	 * </p>
	 * <p>
	 * When this command has executed, the connection adapter result ({@link #getConnectionAdapter()})
	 * will be populated with the connection item chosen.
	 * </p>
	 * 
	 * @return the list of connection items to appear in the popup menu
	 */
	protected List getConnectionMenuContent() {
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
				// Cycle through and make sure each connection type is
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

		} else if (request instanceof CreateConnectionViewAndElementRequest) {
			if (((CreateConnectionViewAndElementRequest) request)
				.getStartCommand() != null) {
				validRelTypes
					.add(((CreateRelationshipRequest) ((CreateConnectionViewAndElementRequest) request)
						.getConnectionViewAndElementDescriptor()
						.getCreateElementRequestAdapter().getAdapter(
							CreateRelationshipRequest.class)).getElementType());
			}
		} else if (request instanceof CreateConnectionViewRequest) {
			if (((CreateConnectionViewRequest) request).getStartCommand() != null) {
				Object type = ((CreateConnectionViewRequest) request)
					.getConnectionViewDescriptor().getElementAdapter()
					.getAdapter(IElementType.class);
				if (type != null) {
					validRelTypes.add(type);
				}
			}
		}
		return validRelTypes;
	}

	/**
	 * Gets a list of all the end items that will represent the other end
	 * choices and will appear in the submenu popup of the given connection
	 * item.
	 * 
	 * <p>
	 * If the objects in this are not <code>IElementTypes</code> or they
	 * require a special label provider, then {@link #getEndLabelProvider()}
	 * should be overridden to provide this.
	 * </p>
	 * <p>
	 * When this command has executed, the end adapter result ({@link #getEndAdapter()})
	 * will be populated with the end item chosen.
	 * </p>
	 * 
	 * @param connectionItem
	 *            the connection item for which this will be a submenu
	 * @return the list of end items to appear in the popup menu
	 */
	protected List getEndMenuContent(Object connectionItem) {
		if (connectionItem instanceof IElementType) {
			IElementType connectionType = (IElementType) connectionItem;
			List menuContent = isDirectionReversed() ? ModelingAssistantService
				.getInstance().getTypesForSource(getKnownEnd(), connectionType)
				: ModelingAssistantService.getInstance().getTypesForTarget(
					getKnownEnd(), connectionType);

			menuContent = filterUnsupportedNodeTypes(menuContent);

			if (!menuContent.isEmpty()
				&& supportsExistingElement(connectionType)) {
				menuContent.add(EXISTING_ELEMENT);
			}

			return menuContent;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * Gets the content to be used in the popup menu from the Modeling Assistant
	 * Service and creates the popup menu.
	 * 
	 * @return the top-level popup menu
	 */
	protected PopupMenu createPopupMenu() {

		final List connectionMenuContent = getConnectionMenuContent();

		if (connectionMenuContent == null || connectionMenuContent.isEmpty()) {
			return null;
		} else if (connectionMenuContent.size() == 1) {
			List menuContent = getEndMenuContent(connectionMenuContent.get(0));
			if (menuContent == null || menuContent.isEmpty()) {
				return null;
			}

			ILabelProvider labelProvider = getConnectionAndEndLabelProvider(connectionMenuContent
				.get(0));
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
						resultList.add(connectionMenuContent.get(0));
						resultList.add(endResult);
						return resultList;
					}
				}
			};
		} else {
			List menuContent = new ArrayList();
			for (Iterator iter = connectionMenuContent.iterator(); iter
				.hasNext();) {
				Object connectionItem = iter.next();

				List subMenuContent = getEndMenuContent(connectionItem);

				if (subMenuContent.isEmpty()) {
					continue;
				}

				PopupMenu subMenu = new PopupMenu(subMenuContent,
					getEndLabelProvider());

				menuContent.add(new PopupMenu.CascadingMenu(connectionItem,
					subMenu));
			}
			if (!menuContent.isEmpty()) {
				return new PopupMenu(menuContent, getConnectionLabelProvider());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isExecutable()
	 */
	public boolean isExecutable() {
		return createPopupMenu() != null;
	}

	/**
	 * Pops up the dialog with the content provided. If the user selects 'select
	 * existing', then the select elements dialog also appears.
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
				connectionAdapter.setObject(resultList.get(0));

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
	 * @param connectionType
	 * @return true if the supported by the modeling assistant service; false
	 *         otherwise
	 */
	private boolean supportsExistingElement(IElementType connectionType) {
		if (isDirectionReversed()) {
			if (ModelingAssistantService.getInstance().provides(
				new SelectExistingElementForSourceOperation(getKnownEnd(),
					connectionType))) {
				return true;
			}
		} else if (ModelingAssistantService.getInstance().provides(
			new SelectExistingElementForTargetOperation(getKnownEnd(),
				connectionType))) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the connectionAdapter.
	 * 
	 * @return Returns the connectionAdapter.
	 */
	public ObjectAdapter getConnectionAdapter() {
		return connectionAdapter;
	}

	/**
	 * Gets the endAdapter.
	 * 
	 * @return Returns the endAdapter.
	 */
	public IAdaptable getEndAdapter() {
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
	protected boolean isDirectionReversed() {
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

	/**
	 * Gets the label provider that is to be used in the first menu of the popup
	 * where the user is to choose the connection to be created.
	 * 
	 * @return the connection label provider
	 */
	protected ILabelProvider getConnectionLabelProvider() {
		if (connectionLabelProvider == null) {
			connectionLabelProvider = new ConnectionLabelProvider();
		}
		return connectionLabelProvider;
	}

	/**
	 * Gets the label provider that is to be used in the second menu of the
	 * popup where the user is to choose the end (could be source or target) to
	 * be created.
	 * 
	 * @return the end label provider
	 */
	protected ILabelProvider getEndLabelProvider() {
		if (endLabelProvider == null) {
			endLabelProvider = new EndLabelProvider();
		}
		return endLabelProvider;
	}

	/**
	 * Gets the label provider that is to be used when there is only one option
	 * for the connection type so the popup menu consists of a single menu
	 * identifying the connection type to be created and options for the other
	 * end of which the user must choose
	 * 
	 * @param connectionItem
	 *            the single known connection item
	 * @return the label provider
	 */
	protected ILabelProvider getConnectionAndEndLabelProvider(
			Object connectionItem) {
		return new ConnectionAndEndLabelProvider(connectionItem);
	}

}