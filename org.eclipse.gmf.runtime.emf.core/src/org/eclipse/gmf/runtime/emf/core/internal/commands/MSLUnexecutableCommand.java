/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.commands;

import org.eclipse.emf.common.CommonPlugin;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;

/**
 * A non-executable command.
 * 
 * @author nbennett
 */
public class MSLUnexecutableCommand
	extends MSLAbstractCommand {

	public static final MSLUnexecutableCommand INSTANCE = new MSLUnexecutableCommand();

	/**
	 * Constructor.
	 */
	private MSLUnexecutableCommand() {
		super(null);
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#canExecute()
	 */
	public boolean canExecute() {
		return false;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#canUndo()
	 */
	public boolean canUndo() {
		return false;
	}

	/**
	 * Throws an exception if it should ever be called.
	 * 
	 * @exception UnsupportedOperationException
	 *                always.
	 */

	public void execute() {

		RuntimeException e = new UnsupportedOperationException(
			CommonPlugin.INSTANCE.getString(
				"_EXC_Method_not_implemented", new String[] {this //$NON-NLS-1$
					.getClass().getName() + ".execute()"})); //$NON-NLS-1$

		Trace.throwing(MSLPlugin.getDefault(),
			MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "execute", e); //$NON-NLS-1$

		throw e;
	}

	/**
	 * Throws an exception if it should ever be called.
	 * 
	 * @exception UnsupportedOperationException
	 *                always.
	 */

	public void undo() {

		RuntimeException e = new UnsupportedOperationException(
			CommonPlugin.INSTANCE.getString(
				"_EXC_Method_not_implemented", new String[] {this //$NON-NLS-1$
					.getClass().getName() + ".undo()"})); //$NON-NLS-1$

		Trace.throwing(MSLPlugin.getDefault(),
			MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "undo", e); //$NON-NLS-1$

		throw e;
	}

	/**
	 * Throws an exception if it should ever be called.
	 * 
	 * @exception UnsupportedOperationException
	 *                always.
	 */

	public void redo() {

		RuntimeException e = new UnsupportedOperationException(
			CommonPlugin.INSTANCE.getString(
				"_EXC_Method_not_implemented", new String[] {this //$NON-NLS-1$
					.getClass().getName() + ".redo()"})); //$NON-NLS-1$

		Trace.throwing(MSLPlugin.getDefault(),
			MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "redo", e); //$NON-NLS-1$

		throw e;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#chain(org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand)
	 */
	public MCommand chain(MCommand command) {
		return this;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getType()
	 */
	public final Type getType() {
		return MCommand.UNEXECUTABLE;
	}
}