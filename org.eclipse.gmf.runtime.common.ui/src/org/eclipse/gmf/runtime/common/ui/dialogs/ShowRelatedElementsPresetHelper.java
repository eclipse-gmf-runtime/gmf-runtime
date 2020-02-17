/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;

/**
 * Show Related Elements Presets helper.
 * 
 * Includes methods for serializing and deserializing and also a converter
 * interface for the serializer and deserializer.
 * 
 * @author wdiu, Wayne Diu
 */
public class ShowRelatedElementsPresetHelper {

	/**
	 * Key separator for serialzing
	 */
	public static final String KEY_SEPARATOR = StringStatics.COLON;

	/**
	 * Value separator for serialzing
	 */
	public static final String VALUE_SEPARATOR = StringStatics.COMMA;

	/**
	 * Methods for the serializer and deserializer which should be implemented
	 * to convert the serialized String into an object and to convert the object
	 * into a serialzed String.
	 */
	public interface IConversionMethods {

		/**
		 * Convert the string to an object for the ShowRelatedElementsPreset's
		 * custom data.
		 * 
		 * @param string
		 *            to be converted
		 * @return Object converted from string
		 */
		public Object convertSerializableStringToCustomData(String string);

		/**
		 * Convert the objet into a serializable string for the serialization
		 * 
		 * @param object
		 *            to be converted
		 * @return String converted from object
		 */
		public String convertCustomDataToSerializableString(Object object);
	}

	/**
	 * Do not instantiate this private class
	 */
	private ShowRelatedElementsPresetHelper() {
		//do nothing
	}

	/**
	 * Convenience method to find a preset from the list.
	 * 
	 * @param list
	 *            List of ShowRelatedElementsPreset objects.
	 * @param name
	 *            name to match
	 * 
	 * @return the first matching ShowRelatedElementsPreset.
	 */
	public static ShowRelatedElementsPreset findPresetFromList(List list,
			String name) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			assert (obj instanceof ShowRelatedElementsPreset);
			ShowRelatedElementsPreset preset = (ShowRelatedElementsPreset) obj;
			if (preset.getName().equals(name))
				return preset;
		}

		return null;

	}

	/**
	 * Return a list of ShowRelatedElementsPreset objects.
	 * 
	 * @param nameToIgnore
	 *            ignore this name when reading in the presets. Can be null if
	 *            you do not want to ignore anything and read everything in.
	 * @param settings
	 *            string array of settings
	 * @param conversionMethod
	 *            see the IConversionMethods class above
	 * 
	 * @return List of the presets. Items in the List are
	 *         ShowRelatedElementsPreset objects
	 */
	public static List readPresets(String nameToIgnore, String[] settings,
			IConversionMethods conversionMethod) {
		java.util.List presets = new ArrayList();

		if (settings == null)
			return presets;

		String name, ids, custom, type, levels;
		ShowRelatedElementsPreset preset;

		for (int i = 0; i < settings.length; i++) {
			preset = null;

			String[] split = settings[i].split(KEY_SEPARATOR);

			//TODO make sure it has a next token
			name = split[0];
			if (nameToIgnore != null
				&& name.toUpperCase().equals(nameToIgnore.toUpperCase())) {
				//skip;
				continue;
			}

			//TODO make sure it has a next token
			ids = split[1];
			java.util.List readIds = new ArrayList();
			String[] splitIds = ids.split(VALUE_SEPARATOR);
			for (int j = 0; j < splitIds.length; j++) {
				if (!splitIds[j].equals(StringStatics.SPACE))
					readIds.add(splitIds[j]);
			}

			//TODO make sure it has a next token
			custom = split[2];
			/*
			 * java.util.List readCustoms = new ArrayList(); String[]
			 * splitCustoms = custom.split(VALUE_SEPARATOR); for (int j = 0; j <
			 * splitCustoms.length; j++) { if
			 * (!splitCustoms[j].equals(StringStatics.SPACE))
			 * readCustoms.add(splitCustoms[j]); }
			 */

			//TODO make sure it has a next token
			type = split[3];

			levels = split[4];

			//TODO better error checking when parsing int
			preset = new ShowRelatedElementsPreset(name, false, Integer
				.parseInt(type), Integer.parseInt(levels));
			preset.addIds(readIds);
			if (conversionMethod == null) {
				preset.setCustom(custom);
			} else {
				preset.setCustom(conversionMethod
					.convertSerializableStringToCustomData(custom));
			}

			presets.add(preset);
		}
		return presets;
	}

	/**
	 * Convert the presets to a string array used for serializing
	 * 
	 * @param presets
	 *            List of ShowRelatedElementsPresets objects
	 * @param conversionMethod
	 *            see the IConversionMethods class above
	 * 
	 * @return String array that contains the data of the presets
	 */
	public static String[] convertPresetsToString(java.util.List presets,
			IConversionMethods conversionMethod) {

		String[] array = new String[presets.size()];
		int i = 0;
		Iterator it = presets.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			assert (obj instanceof ShowRelatedElementsPreset);
			ShowRelatedElementsPreset preset = (ShowRelatedElementsPreset) obj;
			StringBuilder stringBuilder = new StringBuilder(preset.name
					+ ShowRelatedElementsPresetHelper.KEY_SEPARATOR);
			//String string = ;

			Iterator idsIt = preset.getIds().iterator();
			while (idsIt.hasNext()) {
				obj = idsIt.next();
				assert (obj instanceof String);

				stringBuilder.append((String) obj);

				if (idsIt.hasNext())
					stringBuilder.append(ShowRelatedElementsPresetHelper.VALUE_SEPARATOR);

			}

			stringBuilder.append(ShowRelatedElementsPresetHelper.KEY_SEPARATOR);

			stringBuilder.append((conversionMethod != null) ? conversionMethod
				.convertCustomDataToSerializableString(preset.getCustom())
				: StringStatics.BLANK);

			stringBuilder.append(ShowRelatedElementsPresetHelper.KEY_SEPARATOR
				+ preset.getExpansionType()
				+ ShowRelatedElementsPresetHelper.KEY_SEPARATOR
				+ preset.getLevels());

			array[i] = stringBuilder.toString();
			i++;
		}
		return array;
	}

}