/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Abstract semantic provider implementation
 * 
 * @author melaasar
 * 
 * @deprecated Use the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}
 *             to get semantic commands.
 */
public class AbstractSemanticProvider
	extends AbstractProvider
	implements ISemanticProvider, SemanticRequestTypes {

	/**
	 * NonExecutable ICommand
	 */
	private static class EmptyCommand extends AbstractCommand {
		/**
		 * Constructor for NonExecutableCommand.
		 * @param label
		 */
		public EmptyCommand() {
			super(null);
		}

		protected CommandResult doExecute(IProgressMonitor progressMonitor) {
			return null;
		}
	}

	/**
	 * NonExecutable ICommand
	 */
	private static class NonExecutableCommand extends EmptyCommand {
		/**
		 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isExecutable()
		 */
		public boolean isExecutable() {
			return false;
		}
	}

	/**
	 * An <code>NonExecutableCommand</code> instance
	 */
	protected static final EmptyCommand EMPTY_COMMAND =
		new NonExecutableCommand();

	/**
	 * An <code>NonExecutableCommand</code> instance
	 */
	protected static final NonExecutableCommand NON_EXECUTABLE_COMMAND =
		new NonExecutableCommand();

	/**
	 * Determines if the provider understands the request type
	 * @param semanticRequest
	 * @return boolean
	 */
	protected boolean understandsRequest(SemanticRequest semanticRequest) {
		return false;
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(IOperation)
	 */
	public final boolean provides(IOperation operation) {
		if (operation instanceof GetCommandOperation) {
			GetCommandOperation commandOp = (GetCommandOperation) operation;
			SemanticRequest semanticRequest = commandOp.getSemanticRequest();
			return understandsRequest(semanticRequest)
				? supportsRequest(semanticRequest)
				: false;
		}
		return false;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.ISemanticProvider#getCommand(SemanticRequest)
	 */
	public ICommand getCommand(	SemanticRequest semanticRequest) {

		//since these can throw UnsupportedOperationException we must wrap
		//in try catch so that it will return null when we cannot find
		//a command
		//TODO RATLC00515969 No valid command when trying to get a command
		//for adding note and text elements on activity diagrams
		try {
			if (SEMREQ_CREATE_COMPONENT_ELEMENT	== semanticRequest.getRequestType()) {
				return getCreateComponentElementCommand((CreateComponentElementRequest) semanticRequest);
			}

			if (SEMREQ_CREATE_RELATIONSHIP_ELEMENT == semanticRequest.getRequestType()) {
				CreateRelationshipElementRequest request = (CreateRelationshipElementRequest) semanticRequest;
				return (request.getTarget() == null)
					? NON_EXECUTABLE_COMMAND
					: getCreateRelationshipElementCommand((CreateRelationshipElementRequest) semanticRequest);
			}

			if (SEMREQ_DESTROY_ELEMENT == semanticRequest.getRequestType()) {
				return getDestroyElementCommand((DestroyElementRequest) semanticRequest);
			}

			if (SEMREQ_MOVE_ELEMENT == semanticRequest.getRequestType()) {
				return getMoveElementCommand((MoveElementRequest) semanticRequest);
			}

			if (SEMREQ_REORIENT_RELATIONSHIP_SOURCE	== semanticRequest.getRequestType()) {
				return getReorientRelationshipSourceCommand((ReorientRelationshipRequest) semanticRequest);
			}

			if (SEMREQ_REORIENT_RELATIONSHIP_TARGET == semanticRequest.getRequestType()) {
				return getReorientRelationshipTargetCommand((ReorientRelationshipRequest) semanticRequest);
			}
			
			if (SEMREQ_DUPLICATE_ELEMENTS == semanticRequest.getRequestType()) {
				return getDuplicateElementsCommand((DuplicateElementsRequest) semanticRequest);
			}

		} catch (UnsupportedOperationException e) {
			//TODO RATLC00515969 No valid command when trying to get a command
			//for adding note and text elements on activity diagrams
			//I am not logging here, since this try catch should be removed
			//after RATLC00515969 is fixed 
		}

		return null;
	}

	/**
	 * Determines if the provider supports the given request
	 * @param semanticRequest
	 * @return boolean
	 */
	protected boolean supportsRequest( SemanticRequest semanticRequest ) {

		if (SEMREQ_CREATE_COMPONENT_ELEMENT == semanticRequest.getRequestType()) {
			CreateComponentElementRequest request =	(CreateComponentElementRequest) semanticRequest;
			if (request.getContextObject() != null) {
				return supportsCreateComponentElementRequest((CreateComponentElementRequest) semanticRequest);
			}
		} 
		else if ( SEMREQ_CREATE_RELATIONSHIP_ELEMENT == semanticRequest.getRequestType()) {
			CreateRelationshipElementRequest request = (CreateRelationshipElementRequest) semanticRequest;
			if (request.getSource() != null) {
				return supportsCreateRelationshipElementRequest(request, request.getTarget() == null);
			}
		} 
		else if (SEMREQ_DESTROY_ELEMENT == semanticRequest.getRequestType()) {
			return supportsDestroyElementRequest((DestroyElementRequest) semanticRequest);
		}
		else if (SEMREQ_MOVE_ELEMENT == semanticRequest.getRequestType()) {
			return supportsMoveElementRequest((MoveElementRequest) semanticRequest);
		}	
		else if (SEMREQ_REORIENT_RELATIONSHIP_SOURCE == semanticRequest.getRequestType()) {
			return supportsReorientRelationshipSourceRequest((ReorientRelationshipRequest) semanticRequest);
		}
		else if (SEMREQ_REORIENT_RELATIONSHIP_TARGET == semanticRequest.getRequestType()) {
			return supportsReorientRelationshipTargetRequest((ReorientRelationshipRequest) semanticRequest);
		}
		else if (SEMREQ_DUPLICATE_ELEMENTS == semanticRequest.getRequestType()) {
			return supportsDuplicateElementsRequest((DuplicateElementsRequest) semanticRequest);
		}
		
		return false;
	}

	/**
	 * Determines if the provider supports the given <code>CreateComponentElementRequest</code>
	 * @param semanticRequest
	 * @return boolean
	 */
	protected boolean supportsCreateComponentElementRequest( CreateComponentElementRequest semanticRequest) {
		return false;
	}

	/**
	 * Determines if the provider supports the given <code>CreateRelationshipElementRequest</code>
	 * @param semanticRequest
	 * @param sourceOnly Should only inspect the source as the target is not known yet
	 * @return boolean
	 */
	protected boolean supportsCreateRelationshipElementRequest(CreateRelationshipElementRequest semanticRequest, boolean sourceOnly) {
		return false;
	}

	/**
	 * Determines if the provider supports the given <code>ModelOperationContext</code>
	 * @param semanticRequest
	 * @return boolean
	 */
	protected boolean supportsDestroyElementRequest( DestroyElementRequest semanticRequest) {
		return false;
	}

	/**
	 * Determines if the provider supports the given <code>MoveElementRequest</code>
	 * @param semanticRequest
	 * @return boolean
	 */
	protected boolean supportsMoveElementRequest(MoveElementRequest semanticRequest) {
		return false;
	}

	/**
	 * Determines if the provider supports reorienting the relaship source as in
	 * the given <code>ReorientRelationshipRequest</code>
	 * @param semanticRequest
	 * @return boolean
	 */
	protected boolean supportsReorientRelationshipSourceRequest(ReorientRelationshipRequest semanticRequest) {
		return false;
	}

	/**
	 * Determines if the provider supports reorienting the relaship target as in
	 * the given <code>ReorientRelationshipRequest</code>
	 * @param semanticRequest
	 * @return boolean
	 */
	protected boolean supportsReorientRelationshipTargetRequest(ReorientRelationshipRequest semanticRequest) {
		return false;
	}

	/**
	 * Determines if the provider supports duplicating the elements as in
	 * the given <code>DuplicateElementsRequest</code>
	 * @param semanticRequest
	 * @return boolean
	 */
	protected boolean supportsDuplicateElementsRequest(DuplicateElementsRequest semanticRequest) {
		return false;
	}

	/**
	 * Returns a command.to satisfy the given <code>CreateComponentElementRequest</code>
	 * @param semanticRequest
	 * @return ICommand
	 */
	protected ICommand getCreateComponentElementCommand(CreateComponentElementRequest semanticRequest) {
		return null;
	}

	/**
	 * Returns a command.to satisfy the given <code>CreateRelationshipElementRequest</code>
	 * @param semanticRequest
	 * @return ICommand
	 */
	protected ICommand getCreateRelationshipElementCommand(CreateRelationshipElementRequest semanticRequest) {
		return null;
	}

	/**
	 * Returns a command.to satisfy the given <code>DestroyElementRequest</code>
	 * @param semanticRequest
	 * @return ICommand
	 */
	protected ICommand getDestroyElementCommand(DestroyElementRequest semanticRequest) {
		return null;
	}

	/**
	 * Returns a command.to satisfy the given <code>MoveElementRequest</code>
	 * @param semanticRequest
	 * @return ICommand
	 */
	protected ICommand getMoveElementCommand(MoveElementRequest semanticRequest) {
		return null;
	}

	/**
	 * Returns a command.to reorient the relatioship source as requested in
	 * the given <code>ReorientRelationshipRequest</code>
	 * @param semanticRequest
	 * @return ICommand
	 */
	protected ICommand getReorientRelationshipSourceCommand(ReorientRelationshipRequest semanticRequest) {
		return null;
	}

	/**
	 * Returns a command.to reorient the relatioship target as requested in
	 * the given <code>ReorientRelationshipRequest</code>
	 * @param semanticRequest
	 * @return ICommand
	 */
	protected ICommand getReorientRelationshipTargetCommand(ReorientRelationshipRequest semanticRequest) {
		return null;
	}

	/**
	 * Returns a command to duplicated elements as requested. NOTE: The
	 * <code>allDuplicatedElementsMap</code> map must be filled when the
	 * command returns executes.
	 * 
	 * @param request
	 *            the <code>DuplicateElementsRequest</code>
	 * @return Returns the command to duplicate the elements. This command must
	 *         also populate the <code>allDuplicatedElementsMap</code> map in
	 *         the request passed in when it is executed.
	 */
	protected ICommand getDuplicateElementsCommand(
			DuplicateElementsRequest semanticRequest) {
		return null;
	}
}
