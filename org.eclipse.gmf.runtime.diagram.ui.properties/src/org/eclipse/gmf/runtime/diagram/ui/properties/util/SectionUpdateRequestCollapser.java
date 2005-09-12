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

package org.eclipse.gmf.runtime.diagram.ui.properties.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.core.util.RequestCollapser;

/**
 * The receiver will collapse all requests into one and invoke it by the
 * user-interface thread at the next reasonable opportunity. Each thread which
 * calls this collapser is suspended until the runnable completes.
 * 
 * @author nbalaba
 */
public class SectionUpdateRequestCollapser
	extends RequestCollapser {

	private Map sectionsUpdateRequests = null;

	private List requestors = null;

	protected void executeRequest(Runnable request) {
		Display.getDefault().syncExec(request);
	}

	/**
	 * posts the request from the client to run in the thread at the next
	 * possible chance. The posted request will overwrite any previous one
	 * 
	 * @param runnable
	 *            the Runnable request to run
	 */
	public synchronized void postRequest(Runnable runnable) {
		notify();
	}

	/**
	 * @param requester
	 * @param request
	 */
	public synchronized void postRequest(Object requester, Runnable request) {
		if (!requestors.contains(requester))
			requestors.add(requester);
		sectionsUpdateRequests.put(requester, request);
		postRequest(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.internal.util.RequestCollapser#getRequest()
	 */
	protected synchronized Runnable getRequest() {
		Runnable request = null;

		if (requestors.size() > 0) {
			Object requester = requestors.get(0);
			requestors.remove(0);
			request = (Runnable) sectionsUpdateRequests.get(requester);
			sectionsUpdateRequests.remove(requester);
		}
		return request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.internal.util.RequestCollapser#start()
	 */
	public synchronized void start() {
		sectionsUpdateRequests = new HashMap();
		requestors = new ArrayList();
		super.start();
	}

	/**
	 * stops this automaton pbject by interrupting the thread
	 */
	public synchronized void stop() {
		super.stop();
		sectionsUpdateRequests = null;
		requestors = null;
	}
}