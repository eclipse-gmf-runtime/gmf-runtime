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


package org.eclipse.gmf.examples.runtime.emf.clipboard.actions;

import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edit.domain.EditingDomain;


/**
 * A command that records the changes made by an arbitrary {@link Runnable},
 * to be able to undo them, later.
 */
public class RecordingCommand
	extends AbstractCommand {

	private Runnable runnable;
	private Set notifiers;
	private ChangeRecorder recorder;
	private ChangeDescription change;
	
	/**
	 * Initializes me with my label and my runnable to execute.
	 * 
	 * @param domain my editing domain
	 * @param label my label
	 * @param runnable the change that I need to make
	 */
	public RecordingCommand(EditingDomain domain, String label, Runnable runnable) {
		super(label);
		this.runnable = runnable;
		recorder = new ChangeRecorder();
		notifiers = Collections.singleton(domain.getResourceSet());
	}

	/**
	 * I run the runnable when I execute the first time.
	 */
	public void execute() {
		try {
			recorder.beginRecording(notifiers);
			runnable.run();
		} finally {
			change = recorder.endRecording();
			runnable = null;
		}
	}
	
	/**
	 * I am ready to execute if I haven't recorded any changes, yet.
	 */
	protected boolean prepare() {
		return change == null;
	}
	
	/**
	 * Applies (undoes) changes recorded previously, recording the new changes
	 * meanwhile.
	 */
	private void applyChanges() {
		try {
			recorder.beginRecording(notifiers);
			change.apply();
		} finally {
			change = recorder.endRecording();
		}
	}
	
	/**
	 * I can undo if I have recorded any changes previously.
	 */
	public boolean canUndo() {
		return change != null;
	}
	
	/**
	 * Undoes by applying recorded changes.
	 */
	public void undo() {
		applyChanges();
	}

	/**
	 * Redoes by applying changes recorded in the last undo.
	 */
	public void redo() {
		applyChanges();
	}
	
	public void dispose() {
		change = null;
		recorder = null;
		notifiers = null;
		runnable = null;
	}
}
