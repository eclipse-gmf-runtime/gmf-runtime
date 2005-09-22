/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;

/**
 * A service for manipulating editors
 * 
 * @author melaasar
 */
public class EditorService
	extends Service
	implements IEditorProvider {

	/**
	 * The singleton instance of the editor service.
	 */
	private final static EditorService instance = new EditorService();

	/**
	 * Constructs a new editor service.
	 */
	protected EditorService() {
		super(true);
	}

	/**
	 * Retrieves the singleton instance of the editor service.
	 * 
	 * @return The editor service singleton.
	 */
	public static EditorService getInstance() {
		return instance;
	}

	/**
	 * Executes the specified operation using the FIRST execution strategy.
	 * 
	 * @return The result of executing the model operation.
	 * @param operation
	 *            The model operation to be executed.
	 */
	private Object execute(IOperation operation) {
		List results = execute(ExecutionStrategy.FIRST, operation);
		return results.isEmpty() ? null
			: results.get(0);
	}

	/**
	 * Opens an editor with the given editor input
	 * 
	 * @param editorInput
	 *            the editor input object
	 * 
	 * @see IEditorProvider#openEditor(IEditorInput)
	 */
	public IEditorPart openEditor(IEditorInput editorInput) {
		assert null != editorInput;

		IEditorPart editor = (IEditorPart) execute(new OpenEditorOperation(
			editorInput));
		return editor;
	}

	/** a map of all registered editors */
	private Map editorsMap;

	/**
	 * Returns the editorsMap.
	 * 
	 * @return Map of editors
	 */
	protected Map getEditorsMap() {
		if (editorsMap == null)
			editorsMap = new HashMap();
		return editorsMap;
	}

	/**
	 * Method registerEditor registers the editor with the editor service should
	 * be called by the editor upon initialization
	 * 
	 * @param editor
	 *            to be registered in the editor service
	 */
	public void registerEditor(IEditorPart editor) {
		assert null != editor;

		String editorId = editor.getEditorSite().getId();
		List editors = (List) getEditorsMap().get(editorId);
		if (editors == null) {
			editors = new ArrayList();
			getEditorsMap().put(editorId, editors);
		}
		editors.add(editor);
	}

	/**
	 * Method unregisterEditor unregisters the editor from the editor service
	 * should be called by the editor upon getting disposed
	 * 
	 * @param editor
	 *            to unregister from the editor service
	 */
	public void unregisterEditor(IEditorPart editor) {
		assert null != editor;

		String editorId = editor.getEditorSite().getId();
		List editors = (List) getEditorsMap().get(editorId);
		assert null != editors;
		assert (editors.contains(editor));
		editors.remove(editor);
	}

	/**
	 * Returns all registered editors with given id
	 * 
	 * @param editorId
	 *            returned editors must have this String id
	 * @return List editors matching the String editorId
	 */
	public List getRegisteredEditors(String editorId) {
		return (List) getEditorsMap().get(editorId);
	}

	/**
	 * Return a list of all editor parts
	 * 
	 * @return List of IEditorPart editors.
	 */
	public List getRegisteredEditorParts() {
		List allEditors = new ArrayList();
		Iterator iter = getEditorsMap().values().iterator();

		while (iter.hasNext())
			allEditors.addAll((Collection) iter.next());

		return allEditors;
	}

}