/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.commands;

import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import com.ibm.xtools.notation.View;

/**
 * A command to a set a property of a view
 *  
 * @author melaasar
 */
public class SetPropertyCommand extends AbstractModelCommand {

	static final private String CHANGE_PROPERTY_PATTERN = Messages.getString("Command.ChangeViewProperty.ChangePropertyPattern"); //$NON-NLS-1$ 

	private IAdaptable viewAdapter;
	private String propertyName;
	private String propertyId;
	private Object newValue;

	/**
	 * Creates a set property command with a given label
	 * @param label the command label
	 * @param viewAdapter	the view adapter of the <code>View<code> that owns the property
	 * @param propertyId	the property Id of the property to set
	 * @param newValue		the new value of the property
	 */
	public SetPropertyCommand(String label, IAdaptable viewAdapter, String propertyId, Object newValue) {
		super(label, null);
		this.viewAdapter = viewAdapter;
		this.propertyId = propertyId;
		this.newValue = newValue;
	}
	
	/**
	 * Creates a set property command with a default label based on property name
	 * @param viewAdapter	the view adapter of the <code>View<code> that owns the property
	 * @param propertyId	the property Id of the property to set
	 * @param propertyName  the property name 
	 * @param newValue		the new value of the property
	 */
	public SetPropertyCommand(IAdaptable viewAdapter, String propertyId, String propertyName, Object newValue) {
		this(null, viewAdapter, propertyId, newValue);
		this.propertyName = propertyName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		if (viewAdapter != null) {
			View view = (View) viewAdapter.getAdapter(View.class);
			if (view != null)
				return getWorkspaceFilesFor(view);
		}
		return super.getAffectedObjects();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getLabel()
	 */
	public String getLabel() {
		String label = super.getLabel();
		return (label != null)
			? label
			: (MessageFormat
				.format(CHANGE_PROPERTY_PATTERN, new Object[] { propertyName }));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		View view = (View) viewAdapter.getAdapter(View.class);
		if (view != null)
			ViewUtil.setPropertyValue(view,propertyId, newValue);
		return newOKCommandResult();
	}

	/**
	 * Returns the new value of the property.
	 * @return the new value
	 */
	protected Object getNewValue() {
		return newValue;
	}

	/**
	 * Returns the property Id.
	 * @return property Id
	 */
	protected Object getPropertyId() {
		return propertyId;
	}

	/**
	 * Returns the view Adapter that owns the property.
	 * @return the view adapter
	 */
	protected IAdaptable getViewAdapter() {
		return viewAdapter;
	}

	/**
	 * Gets the property name
	 * @return the property name
	 */
	protected String getPropertyName() {
		return propertyName;
	}

	/**
	 * Sets the newValue that will be set for the property.
	 * @param newValue The newValue to set
	 */
	protected void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	/**
	 * Sets the propertyId of the property.
	 * @param propertyId The propertyId to set
	 */
	protected void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * Sets the viewAdapter that owns the property.
	 * @param viewAdapter The viewAdapter to set
	 */
	protected void setViewAdapter(IAdaptable viewAdapter) {
		this.viewAdapter = viewAdapter;
	}

	/**
	 * Sets the property name
	 * @param string the property name
	 */
	protected void setPropertyName(String string) {
		propertyName = string;
	}

}
