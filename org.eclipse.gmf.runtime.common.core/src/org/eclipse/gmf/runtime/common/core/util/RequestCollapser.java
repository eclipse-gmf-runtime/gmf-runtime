/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.util;

import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;

/**
 * This class is used to collapse multiple requests by simply spawning a thread
 * the first time it receives a request, execute that request in the thread, and
 * once the thread finishes execution it will come back to execute the next
 * request if any. The request collapsing stems from the fact that while the
 * thread is busy executing one request, all the newly posted ones will be
 * ignored except for the most recent.
 * 
 * @author Yasser Lulu
 * @canBeSeenBy %partners
 */
public class RequestCollapser {

	/**
	 * the most recent runnable request posted by client
	 */
	private Runnable request;

	/**
	 * The thread used to post/execute requests
	 */
	private Thread thread;

	/**
	 * the thread class spawned to execute commands
	 */
	private class PostedThread
		extends Thread {

		public void run() {
			Runnable req = null;
			while (isInterrupted() == false) {
				req = null;
				synchronized (RequestCollapser.this) {
					while ((req = getRequest()) == null) {
						try {
							RequestCollapser.this.wait();
						} catch (InterruptedException ie) {
							Trace.catching(CommonCorePlugin.getDefault(),
								CommonCoreDebugOptions.EXCEPTIONS_CATCHING,
								getClass(), "run", //$NON-NLS-1$
								ie);
							Log.info(CommonCorePlugin.getDefault(),
								CommonCoreStatusCodes.OK,
								"PostedThread received interruption"); //$NON-NLS-1$
							return;
						}
					}
				}
				executeRequest(req);
			}
		}
	} //thread-class

	/**
	 * executes the runnable request
	 * 
	 * @param runnable
	 *            The request Runnable to execute
	 */
	protected void executeRequest(Runnable runnable) {
		runnable.run();
	}

	/**
	 * returns the most recently posted request and nullifies it afterwards so
	 * it doesn't return it again if invoked again immedialtely
	 * 
	 * @return Runnable The request or null if none has been posted
	 */
	protected synchronized Runnable getRequest() {
		Runnable req = request;
		request = null;
		return req;
	}

	/**
	 * Posts the request from the client to run in the thread at the next
	 * possible chance. The posted request will overwrite any previous one
	 * 
	 * @param runnable
	 *            the Runnable request to run
	 */
	public synchronized void postRequest(Runnable runnable) {
		this.request = runnable;
		notify();
	}

	/**
	 * Constructor for RequestCollapser.
	 */
	public RequestCollapser() {
		thread = new PostedThread();
		thread.setDaemon(true);
	}

	/**
	 * Start this automation object by starting the thread.
	 */
	public synchronized void start() {
		thread.start();
	}

	/**
	 * Stops this automation object by interrupting the thread.
	 */
	public synchronized void stop() {
		request = null;
		thread.interrupt();
		thread = null;
	}
}