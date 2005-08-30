/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.edit;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * An executable object that encapsulates an operation in an MSL-managed EMF
 * resource.  MRunnables are used by the methods of the {@link MEditingDomain}
 * class that read from and write to MSL resources.
 * 
 * @see MEditingDomain#runAsRead(MRunnable)
 * @see MEditingDomain#runAsWrite(MRunnable)
 * 
 * @author rafikj
 * @author cdamus
 */
public abstract class MRunnable {

	// only assigned a value once the MRunnable has been executed
	private IStatus status = Status.OK_STATUS;

	/** Whether I am abandoned (pending cancellation). */
	private boolean abandoned;

	/**
	 * Executes the read or write operation. The result of this method is
	 * returned by the <code>MEditingDomain.runAs...()</code> method that
	 * invokes the <code>MRunnable</code>.
	 * 
	 * @return the result of the read or write operation
	 */
	public abstract Object run();

	/**
	 * <p>
	 * Assigns a <code>status</code> containing error and/or warning messages
	 * from resource validation. Note that, if errors are reported by
	 * validation, then any changes made to the model by this
	 * <code>MRunnable</code> are rolled back. Validation of changes is only
	 * performed by the {@link MEditingDomain#runAsWrite(MRunnable)}method (read
	 * operations are not permitted to update the resource).
	 * </p>
	 * <p>
	 * Note that the <code>status</code> will be "OK" after the
	 * <code>run()</code> method completes if the
	 * <code>MEditingDomain.runAsWrite(MRunnable)</code> method is re-entered
	 * (i.e., called within the context of a <code>runAsWrite()</code>
	 * invocation). Changes are only validated when the outermost operation on
	 * the execution stack completes. "Piggybacking" operations are implicitly
	 * OK.
	 * </p>
	 * 
	 * @param status
	 *            a status object (possibly a
	 *            {@link IStatus#isMultiStatus() multi-status}) indicating zero
	 *            or more error and/or warning conditions detected by validation
	 *            of resource changes
	 * 
	 * @see MEditingDomain#runAsWrite(MRunnable)
	 */
	public void setStatus(IStatus status) {
		this.status = status;
	}

	/**
	 * <p>
	 * Obtains the status of the last invocation of this <code>MRunnable</code>.
	 * This will be OK if the receiver has not yet been executed.
	 * </p>
	 * 
	 * @return the status of my last run, or an OK status if I have never been
	 *         executed
	 */
	public IStatus getStatus() {
		return status;
	}

	/**
	 * Abandons me. An abandoned <code>MRunnable</code> causes the current
	 * write operation to be cancelled (rolled back) when the {@link #run()}
	 * method returns.
	 * <p>
	 * Abandoning only applies to write operations. Calling this method more
	 * than once has no effect.
	 * </p>
	 * 
	 * @see MEditingDomain#runAsWrite(MRunnable)
	 * @see #isAbandoned()
	 */
	public final void abandon() {
		abandoned = true;
	}

	/**
	 * Queries whether I am abandoned. An abandoned <code>MRunnable</code>
	 * causes the current write operation to be cancelled (rolled back) when the
	 * {@link #run()}method returns.
	 * <p>
	 * Abandoning only applies to write operations.
	 * </p>
	 * 
	 * @see MEditingDomain#runAsWrite(MRunnable)
	 * @see #abandon()
	 */
	public final boolean isAbandoned() {
		return abandoned;
	}
}