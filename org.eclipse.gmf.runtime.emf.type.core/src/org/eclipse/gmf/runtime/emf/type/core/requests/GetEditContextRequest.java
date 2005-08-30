/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.requests;

/**
 * Request to get the context for editing a model element.
 * <P>
 * The context is used to obtain the <code>IEditHelper</code> with which to
 * perform the editing.
 * 
 * @author ldamus
 */
public class GetEditContextRequest extends AbstractEditCommandRequest {

	/**
	 * The edit request.
	 */
	private final IEditCommandRequest editRequest;

	/**
	 * The edit helper context for this request.
	 */
	private final Object editHelperContext;
	
	/**
	 * The resulting edit context.
	 */
	private Object editContext;

	/**
	 * Constructs a new request.
	 * 
	 * @param editRequest
	 *            the edit request whose context is being requested
	 */
	public GetEditContextRequest(IEditCommandRequest editRequest,
			Object editHelperContext) {
		this.editRequest = editRequest;
		this.editHelperContext = editHelperContext;
		addParameters(editRequest.getParameters());
	}

	/**
	 * Gets the edit helper context for this request, which is always
	 * <code>null</code>.
	 */
	public Object getEditHelperContext() {
		return editHelperContext;
	}

	/**
	 * Gets the edit command request for which the edit context is being sought.
	 * 
	 * @return the edit command request
	 */
	public IEditCommandRequest getEditCommandRequest() {
		return editRequest;
	}
	
	/**
	 * Gets the requested edit context.
	 * 
	 * @return the edit context
	 */
	public Object getEditContext() {
		return editContext;
	}
	
	/**
	 * Sets the requested edit context. Clients asked to get a command for this
	 * request should also set the edit context if the command they will return
	 * will create or return a different context.
	 * 
	 * @param editContext
	 *            the new edit context
	 */
	public void setEditContext(Object editContext) {
		this.editContext = editContext;
	}

}
