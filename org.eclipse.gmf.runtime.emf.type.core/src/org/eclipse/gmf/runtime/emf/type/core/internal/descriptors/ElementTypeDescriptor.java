/******************************************************************************
 * Copyright (c) 2005, 2015 IBM Corporation, Christian W. Damus, and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Christian W. Damus - bug 457888
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal.descriptors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IElementTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Descriptor for an element type that has been defined in XML using the
 * <code>elementTypes</code> extension point.
 * 
 * @author ldamus
 */
public abstract class ElementTypeDescriptor implements IElementTypeDescriptor {

	/**
	 * The configuration element describing this element type.
	 */
	private IConfigurationElement configElement;

	/**
	 * The identifier of this element type.
	 */
	private final String id;

	/**
	 * The display name of this element type.
	 */
	private String name;

	/**
	 * The URL of the icon for this element type.
	 */
	private URL iconURL;

	/**
	 * The name of the element factory kind. Identifies the factory that should
	 * be used to create the new element type.
	 */
	private String kindName;

	/**
	 * The map of custom parameters that may be associated with this element
	 * type. Keyed on parameter name, each value is the string specified in the
	 * XML parameter value attribute.
	 */
	private final Map params = new HashMap();

	/**
	 * Constructs a new element type descriptor for <code>elementType</code>.
	 * 
	 * @param element
	 *            type the element type
	 */
	protected ElementTypeDescriptor(IElementType elementType) {
		this.id = elementType.getId();
		this.iconURL = elementType.getIconURL();
		this.name = elementType.getDisplayName();
	}
	
	/**
	 * Constructs a new element type descriptor.
	 * 
	 * @param configElement
	 *            the configuration element
	 * @throws CoreException
	 *             when the type ID or display name have not been correctly
	 *             specified in the configuration element
	 */
	protected ElementTypeDescriptor(IConfigurationElement configElement)
		throws CoreException {

		assert configElement != null;

		this.configElement = configElement;

		// ID
		this.id = configElement.getAttribute(ElementTypeXmlConfig.A_ID);
		if (id == null) {
			throw EMFTypePluginStatusCodes.getTypeInitException(StringStatics.BLANK,
				EMFTypeCoreMessages.type_reason_no_id_WARN_, null);
		}

		// NAME
		this.name = configElement.getAttribute(ElementTypeXmlConfig.A_NAME);

		// ICON
		String imageFilePath = configElement
			.getAttribute(ElementTypeXmlConfig.A_ICON);
		if (imageFilePath != null) {
			String pluginId = configElement.getDeclaringExtension()
				.getContributor().getName();
			if (pluginId != null) {
				this.iconURL = getUrlFromPlugin(pluginId, imageFilePath);
			}
		}

		kindName = configElement.getAttribute(ElementTypeXmlConfig.A_KIND);

		if (kindName == null || kindName.length() < 1) {
			// use the default kind name
			kindName = IElementType.class.getName();
		}

		IConfigurationElement[] paramConfigElements = configElement
			.getChildren(ElementTypeXmlConfig.E_PARAM);

		for (int i = 0; i < paramConfigElements.length; i++) {
			IConfigurationElement nextParamConfig = paramConfigElements[i];
			String paramName = nextParamConfig
				.getAttribute(ElementTypeXmlConfig.A_NAME);

			if (paramName == null) {
				throw EMFTypePluginStatusCodes.getTypeInitException(getId(),
					EMFTypeCoreMessages.type_reason_no_param_name_WARN_, null);
			}

			String paramValue = nextParamConfig
				.getAttribute(ElementTypeXmlConfig.A_VALUE);
			if (paramValue == null) {
				throw EMFTypePluginStatusCodes.getTypeInitException(getId(),
						EMFTypeCoreMessages.type_reason_no_param_value_WARN_, null);
			}
			params.put(paramName, paramValue);

		}
	}

	/**
	 * Gets the URL given image file path in a specific plugin.
	 * 
	 * @param pluginId
	 *            the plugin ID
	 * @param imageFilePath
	 *            the image file path
	 * @return the URL, or <code>null</code> if it can't be found
	 */
	private URL getUrlFromPlugin(String pluginId, String imageFilePath) {

		Bundle bundle = Platform.getBundle(pluginId);
		URL result = bundle.getEntry(imageFilePath);

		if (result == null) {
			try {
				result = new URL(imageFilePath);
			} catch (MalformedURLException e) {
				result = null;
			}
		}
		return result;
	}

	/**
	 * Gets the element type identifier.
	 * 
	 * @return the element type identifier.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the element type icon URL.
	 * 
	 * @return the element type icon URL
	 */
	public URL getIconURL() {
		return iconURL;
	}

	/**
	 * Gets the element type display name.
	 * 
	 * @return the element type display name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of the element factory kind. Identifies the factory that
	 * should be used to create the new element type.
	 * 
	 * @return the element factory kind name
	 */
	public String getKindName() {
		return kindName;
	}

	/**
	 * Gets the configuration element for this element type.
	 * 
	 * @return the configuration element
	 */
	public IConfigurationElement getConfigElement() {
		return configElement;
	}
	
	/**
	 * Queries whether the element type is defined dynamically, not statically.
	 * In practice, this means that it was
	 * {@linkplain ElementTypeRegistry#register(org.eclipse.gmf.runtime.emf.type.core.IMetamodelType)
	 * added} to the registry at run-time, not via the extension point.
	 * 
	 * @return whether the described element type is a dynamic definition
	 * 
	 * @since 1.9
	 */
	public boolean isDynamic() {
		return getConfigElement() == null;
	}

	/**
	 * Gets the element type for this descriptor. Will cause the element type to
	 * be created if this is the first time it is called.
	 * <P>
	 * May cause plugin containng the element type factory and element type
	 * class to be loaded.
	 * 
	 * @return the element type
	 */
	public abstract IElementType getElementType();

	/**
	 * Gets the value for the parameter named <code>paramName</code>.
	 * 
	 * @param paramName
	 *            the parameter name
	 * @return the parameter value
	 */
	public String getParamValue(String paramName) {
		return (String) params.get(paramName);
	}
}
