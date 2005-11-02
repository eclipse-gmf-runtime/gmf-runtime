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

package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;

/**
 * A command that will enable/disable the canonical editpolicy 
 * installed on the supplied editparts.
 * @author mhanner
 */
public class ToggleCanonicalModeCommand extends Command {

	/** enablement flag. */
	private boolean _enable;
	
	/** list of semantic elements canonical editpolicies are listening to. */
	private Collection _semanticHosts = new ArrayList();
	
	/**
	 * Create an instance.
	 * @param editParts collection of editparts who's canonical editpolicies will be affected.
	 * @param enable enablement flag
	 */
	public ToggleCanonicalModeCommand(Collection editParts, boolean enable) {
		super(DiagramResourceManager.getI18NString("ToggleCanonicalModeCommand.Label")); //$NON-NLS-1$
		Object[] editparts = new Object[editParts.size()];
		editParts.toArray(editparts);
		for ( int i = 0; i < editparts.length; i++ ) {
			EditPart editPart = (EditPart)editparts[i];
			if (editPart != null) {
				CanonicalEditPolicy editPolicy = getCanonicalEditPolicy(editPart);
				if ( editPolicy != null ) {
					_semanticHosts.add( new WeakReference(editPolicy.getSemanticHost()) );
				}
			}
		}
		_enable = enable;
	}
	
	/**
	 * Create an instance.
	 * @param element a semantic element
	 * @param enable enablement flag
	 */
	public ToggleCanonicalModeCommand( EObject element, boolean enable ) {
		super(DiagramResourceManager.getI18NString("ToggleCanonicalModeCommand.Label")); //$NON-NLS-1$
		_semanticHosts.add( new WeakReference(element) );
		_enable = enable;
	}
		
	/**
	 * Create an instance.
	 * @param target the target editpart
	 * @param enable the enablement flag
	 */
	public ToggleCanonicalModeCommand(EditPart target, boolean enable) {
		this( Collections.singletonList(target), enable);
	}
	
	/**
	 * <code>ToggleCanonicalModeCommand</code> factory method.  
	 * @param editParts collection of editparts who's canonical editpolicies will be affected.
	 * @param enable enablement flag
	 * @return a <code>ToggleCanonicalModeCommand</code> if at least one of the supplied editparts
	 * has a <code>CanonicalEditPolicy</code> installed on it, otherwise <tt>null</tt>.
	 */
	public static ToggleCanonicalModeCommand getToggleCanonicalModeCommand( Collection editParts, boolean enable) {
		ToggleCanonicalModeCommand cmd = new ToggleCanonicalModeCommand( editParts, enable );
		return cmd.getSemanticHosts().isEmpty() ? null : cmd;
	}
	
	/**
	 * <code>ToggleCanonicalModeCommand</code> factory method.  This copy constructor style factory
	 * will return a new command that shares the supplied commands semantic hosts.
	 * @param tcmd a <code>ToggleCanonicalModeCommand</code>
	 * @param enable enablement flag
	 * @return a <code>ToggleCanonicalModeCommand</code> if at least one of the supplied editparts
	 * has a <code>CanonicalEditPolicy</code> installed on it, otherwise <tt>null</tt>.
	 */
	public static ToggleCanonicalModeCommand getToggleCanonicalModeCommand( ToggleCanonicalModeCommand tcmd, boolean enable) {
		if ( tcmd == null  || tcmd.getSemanticHosts().isEmpty() ) {
			return null;
		}
		ToggleCanonicalModeCommand cmd = new ToggleCanonicalModeCommand( Collections.EMPTY_LIST, enable );
		cmd.setSemanticHosts( tcmd.getSemanticHosts() );
		return cmd;
	}

	/**
	 * Return the set of semantic hosts on which a canonical editpolicy is listening. 
	 * @return semantic hosts
	 */
	protected final Collection getSemanticHosts() {
		return _semanticHosts;
	}
	
	private final void setSemanticHosts( Collection hosts ) {
		_semanticHosts.clear();
		_semanticHosts.addAll(hosts);
	}
	
	/**
	 * Return the canonical editpolicy installed on the supplied editpart.
	 * @param editPart edit part to use
	 * @return the canoncial edit policy if there is any
	 */
	protected static CanonicalEditPolicy getCanonicalEditPolicy( EditPart editPart ) {
		return (CanonicalEditPolicy)editPart.getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
	}
	
	/** Removes the canonical editpolict from the target editpart. */ 
	public void execute() {
		DoEnable(_enable);
	}

	/** 
	 * Enables the canonical editpolicies listening of the list of
	 * semantic elements. 
	 * @param enable enablement flag
	 */
	private void DoEnable(boolean enable) {
		Iterator references = getSemanticHosts().iterator();
		while( references.hasNext() ) {
			WeakReference wr = (WeakReference)references.next();
			EObject semanticHost = (EObject)wr.get();
			if (semanticHost != null) {
				List ceps = CanonicalEditPolicy.getRegisteredEditPolicies(semanticHost);
				for ( int i = 0; i < ceps.size(); i++ ) {
					CanonicalEditPolicy cep = (CanonicalEditPolicy)ceps.get(i);
					cep.enableRefresh(enable); 
				}
			}
		}	
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		execute();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		DoEnable(!_enable);
	}
}
