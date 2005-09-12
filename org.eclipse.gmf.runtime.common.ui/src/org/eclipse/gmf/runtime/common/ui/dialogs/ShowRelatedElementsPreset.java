/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

/**
 * Show Related Elements Preset
 * 
 * @author Wayne Diu, wdiu
 */
public class ShowRelatedElementsPreset {

	/**
	 * True if it's a default query, false if it's user defined
	 */
	protected boolean isDefault;

	/**
	 * Name
	 */
	protected String name;

	/**
	 * Identifier
	 */
	protected String id;

	/**
	 * List of IDs
	 */
	protected List ids = new ArrayList();

	/**
	 * Optional custom data
	 */
	protected Object custom;

	/**
	 * See ExpansionType enumeration for values
	 */
	protected int expansionType;

	/**
	 * Levels of expansion, -1 means expand indefinitely
	 */
	protected int levels;

	/**
	 * Optional layout type to use for this preset if functionality is
	 * available.
	 */
	protected Object layoutType;

	/**
	 * Return true if it's the default query, false otherwise.
	 * 
	 * @return true if it's the default query, false otherwise.
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * Return this preset's name
	 * 
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set this preset's name
	 * 
	 * @param newName
	 *            String name
	 */
	public void setName(String newName) {
		name = newName;
	}

	/**
	 * Retrieves the serializable non-language specific id of the preset.
	 * 
	 * @return The identifier for the preset.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Return custom data for this preset, which may be null
	 * 
	 * @return custom data
	 */
	public Object getCustom() {
		return custom;
	}

	/**
	 * Set custom data
	 * 
	 * @param newCustom
	 *            String with custom data
	 */
	public void setCustom(Object newCustom) {
		this.custom = newCustom;
	}

	/**
	 * Return List of serializable String ids
	 * 
	 * @return List of serializable String ids
	 */
	public List getIds() {
		return ids;
	}

	/**
	 * Add an id to the ID List
	 * 
	 * @param aID
	 *            String id to add to the ID List
	 */
	public void addId(String aID) {
		ids.add(aID);
	}

	/**
	 * Add a List of String ids to the ID List
	 * 
	 * @param idList
	 *            List of String ids to add to the ID List
	 */
	public void addIds(List idList) {
		ids.addAll(idList);
	}

	/**
	 * Return the int number of levels to expand, -1 means to expand
	 * indefinitely.
	 * 
	 * @return int number of levels to expand. -1 means to expand indefinitely.
	 */
	public int getLevels() {
		return levels;
	}

	/**
	 * Return the expansion type ordinal. See ExpansionType for more information
	 * 
	 * @return the expansion type ordinal.
	 */
	public int getExpansionType() {
		return expansionType;
	}

	/**
	 * Return the optional layout type.
	 * 
	 * @return A layout type or (null) if none set.
	 */
	public Object getLayoutType() {
		return layoutType;
	}

	/**
	 * Set the optional layout type.
	 * 
	 * @param type
	 *            A layout type or (null) for unset.
	 */
	public void setLayoutType(Object type) {
		this.layoutType = type;
	}

	/**
	 * Constructor.
	 * 
	 * @param newName
	 *            name of preset
	 * @param newDefault
	 *            true it's a hardcoded query, false if it's a user defined
	 *            query
	 * @param newType
	 *            expansion type. See ExpansionType.
	 * @param newLevels
	 *            number of levels to expand, -1 for indefinite.
	 */
	public ShowRelatedElementsPreset(String newName, boolean newDefault,
			int newType, int newLevels) {
		name = newName;
		isDefault = newDefault;
		expansionType = newType;
		levels = newLevels;
		this.id = newName;
	}

	/**
	 * Constructor that allows for serialization of the preset.
	 * 
	 * @param newName
	 *            name of preset
	 * @param newID
	 *            serializable and non-language specific identifier for the
	 *            preset
	 * @param newDefault
	 *            true it's a hardcoded query, false if it's a user defined
	 *            query
	 * @param newType
	 *            expansion type. See ExpansionType.
	 * @param newLevels
	 *            number of levels to expand, -1 for indefinite.
	 */
	public ShowRelatedElementsPreset(String newName, String newID,
			boolean newDefault, int newType, int newLevels) {
		name = newName;
		isDefault = newDefault;
		expansionType = newType;
		levels = newLevels;
		this.id = newID;
	}

	/**
	 * Constructor.
	 * 
	 * @param newName
	 *            name of preset
	 * @param newDefault
	 *            true it's a hardcoded query, false if it's a user defined
	 *            query
	 * @param newType
	 *            expansion type. See ExpansionType.
	 * @param newLevels
	 *            number of levels to expand, -1 for indefinite.
	 * @param idList
	 *            list of IDs
	 * @param newCustom
	 *            custom String data.
	 */
	public ShowRelatedElementsPreset(String newName, boolean newDefault,
			int newType, int newLevels, List idList, Object newCustom) {
		this(newName, newDefault, newType, newLevels);
		ids = idList;
		custom = newCustom;
	}

	/**
	 * Constructor that allows for serialization of the preset.
	 * 
	 * @param newName
	 *            name of preset
	 * @param newID
	 *            serializable and non-language specific identifier for the
	 *            preset
	 * @param newDefault
	 *            true it's a hardcoded query, false if it's a user defined
	 *            query
	 * @param newType
	 *            expansion type. See ExpansionType.
	 * @param newLevels
	 *            number of levels to expand, -1 for indefinite.
	 * @param idList
	 *            list of IDs
	 * @param newCustom
	 *            custom String data.
	 */
	public ShowRelatedElementsPreset(String newName, String newID,
			boolean newDefault, int newType, int newLevels, List idList,
			Object newCustom) {
		this(newName, newID, newDefault, newType, newLevels);
		ids = idList;
		custom = newCustom;
	}
}