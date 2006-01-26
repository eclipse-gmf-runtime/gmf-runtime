/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.service;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.osgi.framework.Bundle;

import com.ibm.icu.util.StringTokenizer;

/**
 * Concrete subclasses can be used to assist in parsing service provider
 * descriptors to filter out and delay loading of service providers that do not
 * apply.
 * <P>
 * This abstract class contains a set of useful utilities for such concrete
 * subclasses.
 * 
 * @author melaasar, mmostafa
 * @canBeSeenBy %partners
 */
public class AbstractProviderConfiguration {
	/**
	 * The name of the 'object' XML attribute.
	 */
	protected static final String OBJECT = "object"; //$NON-NLS-1$
	
	/**
	 * The name of the 'id' XML attribute.
	 */
	protected static final String ID = "id"; //$NON-NLS-1$
	
	/**
	 * The name of the 'class' XML attribute.
	 */
	protected static final String CLASS = "class"; //$NON-NLS-1$
 
	/**
	 * The name of the 'method' XML attribute.
	 */
	protected static final String METHOD = "method"; //$NON-NLS-1$
	
	/**
	 * The name of the 'method' XML attribute.
	 */
	protected static final String STATIC_METHOD = "staticMethod"; //$NON-NLS-1$
	
	/**
	 * The name of the 'name' XML attribute.
	 */
	protected static final String NAME = "name"; //$NON-NLS-1$
	
	/**
	 * The name of the 'value' XML attribute.
	 */
	protected static final String VALUE = "value"; //$NON-NLS-1$
	
	/**
	 * The name of the 'notValue' XML attribute.
	 */
	protected static final String NOT_VALUE = "notValue"; //$NON-NLS-1$

	/**
	 * The name of the 'null' XML attribute value.
	 */
	protected static final String NULL = "null"; //$NON-NLS-1$
	
	/**
	 * the name of the context param
	 */
	protected static final String contextParam = "%Context"; //$NON-NLS-1$

	/**
	 * A map to store previously successful class lookups.
	 */
	private static Map isAssignableTable = new HashMap();

	/** 
	 * A map to store previously failed class lookups.
	 */
	private static Map isNotAssignableTable = new HashMap();
	
	/**
	 * a map of classes that get asked for methods they do not contain, by
	 * the provider, the map is a class to a Set of method signatures
	 */
	private static ClassToMethodSignaturesSetMap passiveClasses = 
		new ClassToMethodSignaturesSetMap();
	
	/**
	 * a class to cach passive classes, passive classes are the classes we asked 
	 * for a method with a specific signature and they faild to find it. The cach used 
	 * so in the next time we can tell if the method does not exists oin the class
	 * without calling getMethod by reflection, which improves the performance
	 * @author mmostafa
	 *
	 */
	private static class ClassToMethodSignaturesSetMap{
		
		/**
		 * internal map for the cach, it is a map of Class to Set of method signature Strings
		 */
		Map classToMethodSignaturesSetMap = new HashMap();
		
		/**
		 * adds a class and a method signature to the passive class cach
		 * @param clazz		the class
		 * @param signature	the method signature
		 */
		public void addMethod(Class clazz, String signature){
			Set signatures = (Set)classToMethodSignaturesSetMap.get(clazz);
			if (signatures==null){
				signatures = new HashSet();
				classToMethodSignaturesSetMap.put(clazz,signatures);
			}
			signatures.add(signature);
		}
		
		/**
		 * check if the class and the method signatrue are contained in the apssive collection,
		 * which means we do  need need to call get method oon the class becuase we will not 
		 * find it, this helps improving the performance.
		 * @param clazz
		 * @param signature
		 * @return
		 */
		public boolean contains(Class clazz, String signature){
			Set signatures = (Set)classToMethodSignaturesSetMap.get(clazz);
			if (signatures==null)
				return false;
			return signatures.contains(signature);
		}
	}
	
	/**
	 * internal class used to cach Methods, so we do not call getMethod too often 
	 * @author mmostafa
	 *
	 */
	private static class ClassToMethodSignatureToMethodCach{
		
		/**
		 * internal map to hold the cached data, it is a map of Class => Map
		 * of Singature string => method
		 */
		Map classToMethodSignatureToMethod = new HashMap();
		
		/**
		 * adds a <code>Method</code> to the cach
		 * @param clazz		the class we got the method from 
		 * @param methodSignature	the method signature
		 * @param method	the <code>Method</code>
		 */
		public void addMethod(Class clazz,String methodSignature, Method method ){
			Map signatureToMethodMap = (Map)classToMethodSignatureToMethod.get(clazz);
			if (signatureToMethodMap==null){
				signatureToMethodMap = new HashMap();
				classToMethodSignatureToMethod.put(clazz,signatureToMethodMap);
			}
			signatureToMethodMap.put(methodSignature,method);
		}
		
		/**
		 * gets a method from the cach using the class that owns it and the method 
		 * signature.
		 * @param clazz		the class that owns the method
		 * @param methodSignature	the method signature
		 * @return	the <code>Method</code> if found any, otherwise null
		 */
		public Method getMethod(Class clazz,String methodSignature){
			Map signatureToMethodMap  = (Map)classToMethodSignatureToMethod.get(clazz);
			if (signatureToMethodMap !=null){
				return (Method)signatureToMethodMap.get(methodSignature);
			}
			return null;
		}
		
	}
	
	/**
	 * map for class to Method signature to method cach
	 */
	private static ClassToMethodSignatureToMethodCach
		classToMethodSignatureToMethodCach = new ClassToMethodSignatureToMethodCach();
	
	
	
	/**
	 * Gets the class name of <code>object</code>.
	 * @param object the object for which the class name is to be found.
	 * @return the class name
	 */
	static String getClassName( Object object ) {
	    String cn = object.getClass().getName();
	    return cn.substring( cn.lastIndexOf('.')+1);
	}
	
	/**
	 * A descriptor for an XML configuration element that identifies a class by
	 * name and optionally its methods.
	 */
	public static class ObjectDescriptor {
		/** 
		 * The name of the class.
		 */
		private String contextClassName;
		
		/**
		 * The ID of the plugin that contains the class.
		 */
		private String contextClassPlugin;
		
		/**
		 * <code>true</code> if a syntax error has occurred,
		 * <code>false</code> otherwise.
		 */ 
		private boolean syntaxError; 
		
		/**
		 * A list of method descriptors for the class.
		 */
		private final List methods;
		
		/**
		 * A list of method descriptors for the class.
		 */
		private final List staticMethods;

		/**
		 * Creates a new object descriptor from its configuration element.
		 * 
		 * @param configElement
		 *            The configuration element.
		 */
		public ObjectDescriptor(IConfigurationElement configElement) {
			this(configElement, CLASS);
		}

		/**
		 * Creates a new object descriptor from its configuration element.
		 * 
		 * @param configElement
		 *            The configuration element.
		 * @param classNameTag
		 *            The name of the 'class' XML attribute.
		 */
		public ObjectDescriptor(
			IConfigurationElement configElement,
			String classNameTag) {

			 String s = configElement.getAttribute(classNameTag);
			 if (s != null) {
				 int start = s.indexOf("(");//$NON-NLS-1$
				 if (start != -1) {
					contextClassName = s.substring(0, start).trim();
				 	int end = s.indexOf(")");//$NON-NLS-1$
					if (end != -1 && end > start+1)
						contextClassPlugin = s.substring(start+1, end);
				 } else
					contextClassName = s.trim();
			 }

			 IConfigurationElement[] methodConfigs =
				configElement.getChildren(METHOD);
			 
			 IConfigurationElement[] staticMethodConfigs =
				configElement.getChildren(STATIC_METHOD);

			if (methodConfigs.length != 0) {
				methods = new ArrayList(methodConfigs.length);
				for (int i = 0; i < methodConfigs.length; i++) {
					String name = methodConfigs[i].getAttribute(NAME);
					if (name != null) {
						try {
							MethodDescriptor methodDescriptor =
								new MethodDescriptor(name);
							methodDescriptor.setCall(name.intern());
							ValueDescriptor value =
								new ValueDescriptor(methodConfigs[i]);
							if (value != null)
								methods.add(new MethodValueEntry(methodDescriptor, value));
						} catch (Exception e) {
							syntaxError = true;
							Log.error(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.SERVICE_FAILURE,	configElement.getDeclaringExtension().getNamespace()+ ".plugin.xml extension [" + configElement.getDeclaringExtension().getExtensionPointUniqueIdentifier() + "]: invalid syntax for method [" + name + "]");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						}
					} else {
						syntaxError = true;
						Log.error(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.SERVICE_FAILURE,	configElement.getDeclaringExtension().getNamespace()+ ".plugin.xml extension [" + configElement.getDeclaringExtension().getExtensionPointUniqueIdentifier() + "] : missing method name");  //$NON-NLS-1$ //$NON-NLS-2$ 
					}
				}
			} 
			else
				methods = Collections.EMPTY_LIST;
			
			
			if (staticMethodConfigs.length != 0) {
				staticMethods = new ArrayList(staticMethodConfigs.length);
				for (int i = 0; i < staticMethodConfigs.length; i++) {
					String name = staticMethodConfigs[i].getAttribute(NAME);
					if (name != null) {
						try {
							StaticMethodDescriptor methodDescriptor =
								new StaticMethodDescriptor(name);
							methodDescriptor.setCall(name.intern());
							ValueDescriptor value =
								new ValueDescriptor(staticMethodConfigs[i]);
							if (value != null)
								staticMethods.add(new MethodValueEntry(methodDescriptor, value));
						} catch (Exception e) {
							syntaxError = true;
							Log.error(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.SERVICE_FAILURE,	configElement.getDeclaringExtension().getNamespace()+ ".plugin.xml extension [" + configElement.getDeclaringExtension().getExtensionPointUniqueIdentifier() + "]: invalid syntax for method [" + name + "]");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						}
					} else {
						syntaxError = true;
						Log.error(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.SERVICE_FAILURE,	configElement.getDeclaringExtension().getNamespace()+ ".plugin.xml extension [" + configElement.getDeclaringExtension().getExtensionPointUniqueIdentifier() + "] : missing method name");  //$NON-NLS-1$ //$NON-NLS-2$ 
					}
				}
			}else
				staticMethods = Collections.EMPTY_LIST;
			
			
			
			if (contextClassName != null)
				contextClassName = contextClassName.intern();
			if (contextClassPlugin != null)
				contextClassPlugin = contextClassPlugin.intern();
		}

		/**
		 * Tests if the object descriptor applies to the given context object.
		 * 
		 * @param object
		 *            The context object.
		 * @return <code>true</code> if it applies; <code>false</code>
		 *         otherwise
		 */
		public boolean sameAs(Object object) {
			if (syntaxError)
				return false;

			Object targetObject = object;
			if (contextClassName != null) {
				if (!isAssignableTo(object.getClass(), contextClassName)) {
					targetObject = getAdapter(object, contextClassName, contextClassPlugin);
					if (targetObject == null)
						return false;
				}
			} 
			
			for(Iterator iter = methods.iterator(); iter.hasNext();) {
				MethodValueEntry entry = (MethodValueEntry)iter.next();
				Object methodValue = invokeMethod(entry.method, targetObject);
				
				if (methodValue == null || !entry.value.sameAs(methodValue))
					return false;
			}
			
			for(Iterator iter = staticMethods.iterator(); iter.hasNext();) {
				MethodValueEntry entry = (MethodValueEntry)iter.next();
				Object methodValue = invokeStaticMethod((StaticMethodDescriptor)entry.method, targetObject);
				
				if (methodValue == null || !entry.value.sameAs(methodValue))
					return false;
			}
			
			return true;
		}
	}

	/**
	 * A descriptor for an XML configuration element that identifies a method by
	 * name and its formal parameters.
	 */
	private static class MethodDescriptor {
		
		/**
		 * The method call.
		 */
		private String call;
		
		/**
		 * The method name.
		 */
		private String name;
		
		/**
		 * The array of method parameters.
		 */
		private Object parameterObjects[];
		
		/**
		 * The array of method parameter types.
		 */
		private Class parameterTypes[];
		
		/**
		 * The next cascading method descriptor.
		 */
		private MethodDescriptor next;

		/**
		 * The list of method parameters.
		 */
		private List parameters;
		
		/**
		 * the method signature
		 *
		 */
		private String signature = null;
		
		
		protected MethodDescriptor(){
			// empty 
		}

		/**
		 * Creates a new method descriptor from a string representing the
		 * method's full cascading invocation with parameters.
		 * <P>
		 * The format of the string is:
		 * <P>
		 * <code>method_name([params])[.method_name([params])]*</code>
		 * <P>
		 * Where:
		 * <UL>
		 * <LI>the <i>params </i> are comma-separated string literals without
		 * double quotes.</LI>
		 * <LI>only string <i>params </i> are allowed (no texual representation
		 * of non-string params are allowed)</LI>
		 * </UL>
		 * <P>
		 * For example:
		 * <P>
		 * <code>getPropertyValue(Source_Connection).getName()</code>
		 * 
		 * @param string
		 *            the method invocation string
		 */
		public MethodDescriptor(String string) {
			// set method name
			string = parseName(string.trim());
			// set method parameters
			string = parseParameterList(string.trim());

			// fill the parameter objects and types arrays
			if (parameters != null && !parameters.isEmpty()) {
				Collections.reverse(parameters);
				parameterObjects = parameters.toArray();
				parameterTypes = new Class[parameterObjects.length];
				for (int i = 0; i < parameterObjects.length; i++) {
					String p = (String) parameterObjects[i];
					int objIndex = p.indexOf("[object]"); //$NON-NLS-1$
					boolean isObject = objIndex >= 0;
					int parseAsIndex = p.indexOf(":::"); //$NON-NLS-1$
					try {
						if (isObject && (parseAsIndex >= 0))
							// assume order: [object] before type:::param
							assert (objIndex < parseAsIndex);
						if (parseAsIndex >= 0) {
							// "type:::param"
							String parseAs =
								p.substring((isObject ? 8 : 0), parseAsIndex);
							String value =
								p.substring(parseAsIndex + 3, p.length());
							if (parseAs.equalsIgnoreCase("int")) { //$NON-NLS-1$
								parameterTypes[i] = Integer.class;
								parameterObjects[i] = Integer.decode(value);
							} else if (parseAs.equalsIgnoreCase("bool")) { //$NON-NLS-1$
								parameterTypes[i] = Boolean.class;
								parameterObjects[i] = Boolean.valueOf(value);
							} else if (parseAs.equalsIgnoreCase("double")) { //$NON-NLS-1$
								parameterTypes[i] = Double.class;
								parameterObjects[i] = Double.valueOf(value);
							}
							// if [object] present, set type to Object
							if (isObject)
								parameterTypes[i] = Object.class;
						} else if (isObject) { // "[object]param"
							String value = p.substring(8, p.length());
							parameterTypes[i] = Object.class;
							parameterObjects[i] = value;
						} else // "param"
							parameterTypes[i] = String.class;
					} catch (Exception e) {
						String value =
							p.substring(
								((parseAsIndex >= 0) ? parseAsIndex + 3 : 0),
								p.length());
						parameterObjects[i] = value;
						parameterTypes[i] = String.class;
					}
				}
			}
			parameters = null;

			// set method parameters
			if (string.length() != 0) {
				if (string.charAt(0) != '.')
					throw new IllegalArgumentException();
				next = new MethodDescriptor(string.substring(1).trim());
			}
			
		 if (this.name != null)
				name = name.intern();
		}

		/**
		 * Parses and returns the method name in a method invocation string.
		 * 
		 * @param string
		 *            the method invocation string
		 * @return the method name
		 */
		protected String parseName(String string) {
			int index = string.indexOf('(');
			if (index == -1)
				throw new IllegalArgumentException(); 
			name = string.substring(0, index).trim();
			return string.substring(index + 1);
		}

		/**
		 * Parses a method invocation string for the list of parameters, which
		 * are placed in the <code>parameters</code> field.
		 * 
		 * @param string
		 *            the method invocation string
		 * @return the end part of the method invocation string that has not
		 *         been parsed.
		 */
		protected String parseParameterList(String string) {
			int index = -1;
			String paramStr = null;
			while (paramStr == null) {
				index = string.indexOf(')', index + 1);
				if (index == -1)
					throw new IllegalArgumentException(); 
				if (index == 0 || string.charAt(index - 1) != '\\')
					paramStr = string.substring(0, index);
			}
			if (paramStr.length() != 0) {
				parameters = new ArrayList();
				parseParameters(paramStr.trim());
			}
			return string.substring(index + 1);
		}

		/**
		 * Parses a string containing a list of method parameters and stores
		 * them in the <code>parameters</code> field.
		 * 
		 * @param string
		 *            the comma-separated list of method parameters.
		 */
		private void parseParameters(String string) {
			int index = string.indexOf(',');
			if (index != -1 && string.charAt(index - 1) != '\\') {
				parseParameters(string.substring(index + 1).trim());
				parameters.add(string.substring(0, index));
			} else
				parameters.add(string);
		}

		/**
		 * Returns the method name.
		 * 
		 * @return the method name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Sets the method name.
		 * @param the method name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Returns an array of string params.
		 * 
		 * @return the parameters
		 */
		public Object[] getParameters() {
			return parameterObjects;
		}

		/**
		 * Returns an array of parameter classes.
		 * 
		 * @return the parameter types
		 */
		public Class[] getParameterTypes() {
			return parameterTypes;
		}
		
		/**
		 * sets the the array of params.
		 * @param paramters
		 */
		protected void setParameters(Object[] paramters) {
			parameterObjects = paramters;
		}

		/**
		 * sets the the array of parameter types.
		 * @param paramtersTypes
		 */
		public void setParameterTypes(Class[] paramterTypes) {
			this.parameterTypes = paramterTypes;
		}

		/**
		 * Returns the next cascading method descriptor, if any.
		 * 
		 * @return the next method descriptor, or <code>null</code> if there
		 *         is none
		 */
		public MethodDescriptor getNext() {
			return next;
		}
		
		/**
		 * sets the next cascading method descriptor, if any.
		 * @param next
		 */
		protected void setNext(MethodDescriptor next) {
			this.next = next;
		}

		/**
		 * Gets the method call.
		 * 
		 * @return the method call.
		 */
		public String getCall() {
			return call;
		}
		
		/**
		 * Sets the method call.
		 * 
		 * @param call
		 *            the new method call
		 */
		public void setCall(String call) {
			this.call = call;
		}
		
		/**
		 * Gets the Paramters List
		 * @return The list of method parameters.
		 */
		protected List getParamtersList(){
			return parameters;
		}
		
		/**
		 * Sets the Paramters List
		 * @param Parameterlist
		 */
		protected void setParamtersList(List parameters1){
			this.parameters = parameters1;
		}
		
		/**
		 * utility method used to get the signature of the method this method descriptor
		 * descripe.
		 * @return the signature of the method
		 */
		public String getSignature(){
			if (this.signature==null){
				StringBuffer sb = 
					new StringBuffer();
				sb.append(name);
				sb.append('(');
				if(parameterTypes!=null)
					for(int index= 0 ; index < parameterTypes.length ; index++){
						Class clazz = parameterTypes[index];
						sb.append(clazz.getName());
						if(index<parameterTypes.length-1)
							sb.append(',');
					}
				sb.append(')');
				signature = sb.toString();
			}
			return signature;
			
		}
	}

	
	private static class StaticMethodDescriptor extends MethodDescriptor {
		/**
		 * the plugin Name
		 */
		private String pluginID;
		
		/**
		 * the Class Name
		 */
		private String className;
		
		/**
		 * Creates a new method descriptor from a string representing the
		 * method's full cascading invocation with parameters.
		 * <P>
		 * The format of the string is:
		 * <P>
		 * <code>PluginID\ClassName.method_name([params])[.method_name([params])]*</code>
		 * <P>
		 * Where:
		 * <UL>
		 * <LI>the <i>params </i> are comma-separated string literals without
		 * double quotes.</LI>
		 * <LI>only string <i>params </i> are allowed (no texual representation
		 * of non-string params are allowed)</LI>
		 * <LI>to identify a parameter as the current context you put %
		 * </UL>
		 * <P>
		 * For example:
		 * <P>
		 * <code>MyPluginID\MyClass.MyStaticFunction(%,"some value")</code>
		 * 
		 * @param string
		 *            the method invocation string
		 */
		public StaticMethodDescriptor(String string) {
			// set plugin ID
			string = parsePluginID(string.trim());
			// set class Name 
			string = parseClassName(string.trim());
			// set method name
			string = parseName(string.trim());
			// set method parameters
			string = parseParameterList(string.trim());

			List parameters = getParamtersList();
			
			// fill the parameter objects and types arrays
			if (parameters != null && !parameters.isEmpty()) {
				Collections.reverse(parameters);
				Object[] parameterObjects = parameters.toArray();
				Class[] parameterTypes = new Class[parameterObjects.length];
				for (int i = 0; i < parameterObjects.length; i++) {
					String p = (String) parameterObjects[i];
					int objIndex = p.indexOf("[object]"); //$NON-NLS-1$
					boolean isObject = objIndex >= 0;
					int parseAsIndex = p.indexOf(":::"); //$NON-NLS-1$
					try {
						if (isObject && (parseAsIndex >= 0))
							// assume order: [object] before type:::param
							assert (objIndex < parseAsIndex);
						if (parseAsIndex >= 0) {
							// "type:::param"
							String parseAs =
								p.substring((isObject ? 8 : 0), parseAsIndex);
							String value =
								p.substring(parseAsIndex + 3, p.length());
							if (parseAs.equalsIgnoreCase("int")) { //$NON-NLS-1$
								parameterTypes[i] = Integer.class;
								parameterObjects[i] = Integer.decode(value);
							} else if (parseAs.equalsIgnoreCase("bool")) { //$NON-NLS-1$
								parameterTypes[i] = Boolean.class;
								parameterObjects[i] = Boolean.valueOf(value);
							} else if (parseAs.equalsIgnoreCase("double")) { //$NON-NLS-1$
								parameterTypes[i] = Double.class;
								parameterObjects[i] = Double.valueOf(value);
							}
							// if [object] present, set type to Object
							if (isObject)
								parameterTypes[i] = Object.class;
						} else if (isObject) { // "[object]param"
							String value = p.substring(8, p.length());
							parameterTypes[i] = Object.class;
							parameterObjects[i] = value;
						} else if (p.startsWith(contextParam)){// "param" 
							parameterTypes[i] = getParameterType(p);
							parameterObjects[i] = "%Context"; //$NON-NLS-1$
						}
						else
							parameterTypes[i] = String.class;
					} catch (Exception e) {
						String value =
							p.substring(
								((parseAsIndex >= 0) ? parseAsIndex + 3 : 0),
								p.length());
						parameterObjects[i] = value;
						parameterTypes[i] = String.class;
					}
				}
				setParameters(parameterObjects);
				setParameterTypes(parameterTypes);
			}
			parameters = null;

			// set method parameters
			if (string.length() != 0) {
				if (string.charAt(0) != '.')
					throw new IllegalArgumentException();
				setNext(new MethodDescriptor(string.substring(1).trim()));
			}
			
		 if (getName() != null)
				setName(getName().intern());
		}

		
		/**
		 * parse the passed paramter to extract the paramter's class
		 * @param p		the parapemter 
		 * @return
		 */
		private Class getParameterType(String parameter) {
			int startIndex = parameter.indexOf("["); //$NON-NLS-1$
			int endIndex = parameter.indexOf("]"); //$NON-NLS-1$
			if(startIndex==-1 || endIndex==-1)
				throw new IllegalArgumentException(); 
			String parameterTypeString= parameter.substring(startIndex+1,endIndex).trim();
			
			endIndex = parameterTypeString.indexOf('/');
			if(endIndex==-1 || endIndex==parameterTypeString.length()-1)
				throw new IllegalArgumentException(); 
			String parameterPluginID = parameterTypeString.substring(0,endIndex).trim();
			String parameterClassName = parameterTypeString.substring(endIndex + 1);
			Class clazz = loadClass(parameterClassName,parameterPluginID);
			if(clazz==null)
				clazz =  Object.class;
			return clazz;
		}

		/**
		 * Parses and returns the Plugin ID in a method invocation string.
		 * 
		 * @param string
		 *            the method invocation string
		 * @return the plugin name
		 */
		private String parsePluginID(String string) {
			int index = string.indexOf('/');
			if (index == -1)
				throw new IllegalArgumentException(); 
			pluginID = string.substring(0, index).trim();
			return string.substring(index + 1);
		}
		
		/**
		 * Parses and returns the Plugin ID in a method invocation string.
		 * 
		 * @param string
		 *            the method invocation string
		 * @return the plugin name
		 */
		private String parseClassName(String string) {
			int index = string.indexOf('(');
			if (index == -1)
				throw new IllegalArgumentException(); 
			index = string.lastIndexOf('.',index);
			if (index == -1)
				throw new IllegalArgumentException(); 
			className = string.substring(0, index).trim();
			return string.substring(index + 1);
		}
		
	public String getPluginID(){
			return pluginID;
		}
		
		public String getClassName(){
			return className;
		}
	
	}
	
	
	/**
	 * A descriptor for an XML configuration element that identifies a method
	 * result by its type and <code>toString()</code> value.
	 */
	private static class ValueDescriptor {
		
		/**
		 * The valid value literals.
		 */
		private Set valueLiterals;
		
		/**
		 * The invalid valud literals.
		 */
		private Set notValueLiterals;
		
		/**
		 * The valid value objects.
		 */
		private List valueObjects;
		
		/**
		 * The invalid value objects.
		 */
		private List notValueObjects;

		/**
		 * Creates a new value descriptor from its configuration element.
		 * 
		 * @param configElement
		 *            The configuration element.
		 */
		public ValueDescriptor(IConfigurationElement configElement) {
			valueLiterals = new HashSet();
			String s = configElement.getAttribute(VALUE);
			if (s != null)
				parseValueLiteralString(s, valueLiterals);

			notValueLiterals = new HashSet();
			s = configElement.getAttribute(NOT_VALUE);
			if (s != null)
				parseValueLiteralString(s, notValueLiterals);

			IConfigurationElement[] valueConfigs = configElement.getChildren(VALUE);
			valueObjects = new ArrayList(valueConfigs.length);
			for (int i=0; i<valueConfigs.length; i++)
				valueObjects.add(new ObjectDescriptor(valueConfigs[i]));

			IConfigurationElement[] notValueConfigs = configElement.getChildren(NOT_VALUE);
			notValueObjects = new ArrayList(notValueConfigs.length);
			for (int i=0; i<notValueConfigs.length; i++)
				notValueObjects.add(new ObjectDescriptor(notValueConfigs[i]));
		}
		
		/**
		 * Parse the string <code>s</code>, which is a comma-separated list
		 * of value literals and place them in the given <code>list</code>.
		 * 
		 * @param s
		 *            the string to be parsed
		 * @param list
		 *            the set of literal string values from <code>s</code>.
		 */
		private void parseValueLiteralString(String s, Set list) {
			// parse the string comma-separated string literals ignoring escaped commas
			int start = 0;
			int end = s.indexOf(',');
			while (end != -1) {
				if (s.charAt(end-1) == '\\') {
					s = s.substring(0, end-1) + s.substring(end);
					end = s.indexOf(',', end);
					continue;
				}
				list.add(s.substring(start, end).trim().intern());
				start = end +1;
				end = s.indexOf(',', start);
			}
			list.add(s.substring(start).trim().intern());
		}
		
		/**
		 * Returns <code>true</code> if I am the same as <code>object</code>,
		 * <code>false</code> otherwise.
		 * 
		 * @param object
		 *            the object to be tested
		 * @return <code>true</code> if I am the same as <code>object</code>,
		 *         <code>false</code> otherwise.
		 */
		public boolean sameAs(Object object) {
			if (!valueLiterals.isEmpty()) {
				if (!valueLiterals.contains(object.toString()))
					return false;
			}
			if (!notValueLiterals.isEmpty()) {
				if (notValueLiterals.contains(object.toString()))
					return false;
			}
			if (!valueObjects.isEmpty()) {
				if (!isObjectinList(object, valueObjects))
					return false;
			}
			if (!notValueObjects.isEmpty()) {
				if (isObjectinList(object, notValueObjects))
					return false;
			}
			return true;
		}
		
		/**
		 * Answers whether or not an object in <code>list</code> is the
		 * {@link #sameAs(Object)}<code>object</code>.
		 * 
		 * @param object
		 *            the object to find
		 * @param list
		 *            the list of objects
		 * @return <code>true</code> if an object in <code>list</code> is
		 *         the {@link #sameAs(Object)}<code>object</code>,
		 *         <code>false</code> otherwise.
		 */
		private boolean isObjectinList(Object object, List list) {
			Iterator i = list.iterator();
			while (i.hasNext()) {
				if (((ObjectDescriptor)i.next()).sameAs(object))
					return true;
			}
			return false;
		}
	}

	/**
	 * Describes a method value using a method descriptor and a value descriptor.
	 */
	private static class MethodValueEntry {
		
		/**
		 * The method descriptor.
		 */
		public MethodDescriptor method;
		
		/**
		 * The value descriptor.
		 */
		public ValueDescriptor value;

		/**
		 * Creates a new method value entry.
		 * @param method the method descriptor
		 * @param value the value descriptor
		 */
		public MethodValueEntry(MethodDescriptor method, ValueDescriptor value) {
			super();
			this.method = method;
			this.value = value;
		}
	}

	/**
	 * A helper method to return a list of objects whose ids are given in a
	 * comma-separated string and whose instances are given in an object map.
	 * 
	 * @param objectsIds
	 *            A comma-separated object ids string
	 * @param objectMap
	 *            A map of object ids to their instances
	 * @param configElement
	 *            The configuration element, used for error logging
	 * @return a list of object instances whose ids are given or
	 *         <code>null</code> if no ids matched any instances
	 */
	protected static List getObjectList(String objectsIds, Map objectMap, IConfigurationElement configElement) {
		if (objectsIds == null)
			return null;
		StringTokenizer ids = new StringTokenizer(objectsIds.trim(), ","); //$NON-NLS-1$
		if (!ids.hasMoreTokens())
			return null;
		
		List objectList = new ArrayList();
		while (ids.hasMoreTokens()) {
			String objectId = ids.nextToken().trim();
			Object objectVal = objectMap.get(objectId);
			if (objectVal != null)
				objectList.add(objectVal);
			else {
				Log.error(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.SERVICE_FAILURE, configElement.getDeclaringExtension().getNamespace()+ ".plugin.xml extension [" + configElement.getDeclaringExtension().getExtensionPointUniqueIdentifier() + "]: object id (" + objectId + ") is not in the list " + objectMap.keySet());  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		return objectList;
	}

	/**
	 * Parses the comma-separated <code>s</code> string and returns a set of
	 * the individual entries in the string.
	 * 
	 * @param s
	 *            A comma-separated string
	 * @return a set of the individual entries in the string.
	 */
	protected static Set getStrings(String s) {
		if (s == null)
			return null;
		Set stringList = new HashSet();
		StringTokenizer ids = new StringTokenizer(s.trim(), ","); //$NON-NLS-1$
		while (ids.hasMoreTokens()) {
			stringList.add(ids.nextToken().trim());
		}
		return stringList.isEmpty() ? null : stringList;
	}

	/**
	 * Tests if an object matches at least one in the list of object descriptors
	 * passed.
	 * 
	 * @param object
	 *            the object for which to find a match
	 * @param objects
	 *            the list of object in which to find a match
	 * @return <code>true</code> if there was a match, <code>false</code>
	 *         otherwise
	 */
	protected static boolean objectMatches(Object object, List objects) {
		if (object != null) {
			for (Iterator i = objects.iterator(); i.hasNext();) {
				ObjectDescriptor desc = (ObjectDescriptor) i.next();
				if (desc.sameAs(object))
					return true;
			}
		}
		return false;
	}

	/**
	 * A utility method to load a class using its name and a given class loader.
	 * 
	 * @param className
	 *            The class name
	 * @param bundle
	 *            The class loader
	 * @return The loaded class or <code>null</code> if could not be loaded
	 */
	 /*protected static Class loadClass(String className, Bundle bundle) {
		try {
			return bundle.loadClass(className);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}*/
	
	/**
	 * A utility method to load a class using its name and a given class loader.
	 * 
	 * @param className
	 *            The class name
	 * @param bundle
	 *            The class loader
	 * @return The loaded class or <code>null</code> if could not be loaded
	 */
	protected static Class loadClass(String className, String pluginId) {
		StringBuffer keyStringBuf = new StringBuffer(className.length()
			+ pluginId.length() + 2); // 2 is for . and extra.
		keyStringBuf.append(pluginId);
		keyStringBuf.append('.');
		keyStringBuf.append(className);
		String keyString = keyStringBuf.toString();
		WeakReference ref = (WeakReference) successLookupTable.get(keyString);
		Class found = (ref != null) ? (Class) ref.get()
			: null;
		if (found == null) {
			if (ref != null)
				successLookupTable.remove(keyString);
			if (!failureLookupTable.contains(keyString)) {
				try {
					Bundle bundle = getPluginBundle(pluginId);
					if (bundle!=null){
						found = bundle.loadClass(className);
						successLookupTable.put(keyString, new WeakReference(found));
					}else{
						failureLookupTable.add(keyString);
					}
				} catch (ClassNotFoundException e) {
					failureLookupTable.add(keyString);
				}
			}
		}
		return found;
	}
	
	
	/**
	 * Given a bundle id, it checks if the bundle is found and activated. If it
	 * is, the method returns the bundle, otherwise it returns <code>null</code>.
	 * 
	 * @param pluginId
	 *            the bundle ID
	 * @return the bundle, if found
	 */
	protected static Bundle getPluginBundle(String pluginId) {
		Bundle bundle = Platform.getBundle(pluginId);
		if (null != bundle && bundle.getState() == org.osgi.framework.Bundle.ACTIVE)
			return bundle;
		return null;
	}

	/**
	 * Tests if the given class is assignable to the given class name. Optimized
	 * to look first in a cache of previously retrieved results.
	 * 
	 * @param clazz
	 *            the class to be tested
	 * @param className
	 *            the class name to test against
	 * @return <code>true</code> if the class is assignable to the class name,
	 *         <code>false</code> otherwise.
	 */
	protected static boolean isAssignableTo(Class clazz, String className) {
		if (clazz == null)
			return false;

		if ( contains(isNotAssignableTable, clazz, className) ) {
			return false;
		}
		
		if ( contains(isAssignableTable, clazz, className) ) {
			return true;
		}
	
		boolean result = isAssignableToNoCache(clazz,className);
		
		if (result) {
			add(isAssignableTable, clazz, className);
		} else {
			add(isNotAssignableTable, clazz, className);
		}

		return result;
	}

	/**
	 * Tests if the given class is assignable to the given class name.
	 * 
	 * @param clazz
	 *            the class to be tested
	 * @param className
	 *            the class name to test against
	 * @return <code>true</code> if the class is assignable to the class name,
	 *         <code>false</code> otherwise.
	 */
	private static boolean isAssignableToNoCache(Class clazz, String className) {
// mgoyal: This approach isn't safe to use as it can cause incorrect
// plugin load. Documenting this approach for further analysis. Don't
// remove or uncomment this.
//		try {
//			if(clazz.getName().equals(className))
//				return true;
//			
//			ClassLoader clsLoader = clazz.getClassLoader();
//			if(clsLoader != null) {
//				Class testCls = clsLoader.loadClass(className);
//				if(testCls != null && testCls.isAssignableFrom(clazz))
//					return true;
//			}
//			return false;
//		} catch (ClassNotFoundException e) {
//			return false;
//		}
//		
		
		// test the class itself
		if (clazz.getName().equals(className))
			return true;
		
		// test all the interfaces the class implements
		Class[] interfaces = clazz.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (checkInterfaceHierarchy(interfaces[i], className))
				return true;
		}
		
		// test superclass
		return isAssignableTo(clazz.getSuperclass(), className);
	}

	/**
	 * A map of classes that have been successfully loaded, keyed on the class
	 * name optionally prepended by the plugin ID, if specified.
	 */
	private static Map successLookupTable = new HashMap();
	
	/**
	 * A map of classes that could not be loaded, keyed on the class name
	 * optionally prepended by the plugin ID, if specified.
	 */
	private static Set failureLookupTable = new HashSet();

	/**
	 * Gets an adapter for <code>object</code> to the class described by
	 * <code>className</code> qualified by the optional <code>pluginId</code>.
	 * 
	 * @param object
	 *            the object to be adapted
	 * @param className
	 *            the name of the adapter class
	 * @param pluginId
	 *            the optional plugin ID (can be <code>null/code>)
	 * @return the adapted object, or <code>null</code> if it couldn't be found
	 */
	protected static Object getAdapter(Object object, String className, String pluginId) {
		if (!(object instanceof IAdaptable))
			return null;
		if(pluginId != null) {
			Class theClass = loadClass(className,pluginId);
			return theClass != null ? ((IAdaptable) object).getAdapter(theClass) : null;
		}
		return null;
	}

	/**
	 * A utility method to invoke a cascading list of methods.
	 * 
	 * @param methodDescriptor
	 *            the first method descriptor
	 * @param object
	 *            The object to invoke the method on
	 * @return the value of the invokation
	 */
	protected static Object invokeMethod(MethodDescriptor methodDescriptor, Object object) {
		String methodSignature = null;
		Class clazz =null;
		try {
			if (methodDescriptor == null || object == null)
				return null;
			methodSignature = methodDescriptor.getSignature();
			clazz = object.getClass();
			if (passiveClasses.contains(clazz,methodSignature))
				return null;
			Method method = classToMethodSignatureToMethodCach.
				getMethod(clazz,methodSignature);
			if(method==null){
				method = clazz.getMethod(methodDescriptor.getName(),
										 methodDescriptor.getParameterTypes());
				classToMethodSignatureToMethodCach.addMethod(clazz,methodSignature,method);
			}
			Object valueObj = 
				method.invoke(object, methodDescriptor.getParameters());
			if (methodDescriptor.getNext() == null)
				return valueObj == null ? NULL : valueObj;
			return invokeMethod(methodDescriptor.getNext(), valueObj);
		} catch (Exception e) {
			passiveClasses.addMethod(clazz,methodSignature);
			return null;
		}
	}
	
	/**
	 * A utility method to invoke a cascading list of methods.
	 * 
	 * @param StaticMethodDescriptor
	 *            the static method descriptor
	 * @param object
	 *            The context object to use (it could be null)
	 * @return the value of the invokation
	 */
	protected static Object invokeStaticMethod(StaticMethodDescriptor methodDescriptor, Object object) {
		try {
			if (methodDescriptor == null)
				return null;
			
			Object[] valuesCopy = (Object[])methodDescriptor.getParameters().clone();
			for (int i = 0; i < valuesCopy.length; i++) {
				if(valuesCopy[i].equals(contextParam)){
					valuesCopy[i]=object;
				}
			}
			
			Method method = getStaticMethod(methodDescriptor);
			Object valueObj = 
				method.invoke(object, valuesCopy);

			if (methodDescriptor.getNext() == null)
				return valueObj == null ? NULL : valueObj;
			return invokeMethod(methodDescriptor.getNext(), valueObj);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * utility method used to get a static method object
	 * @param pluginID			the plugin that owns the class
	 * @param className			the class to use to call hte static method
	 * @param methodName		the method to get
	 * @param ParameterTypes	the parameter types 
	 * @return					the  method object
	 */
	private static Method getStaticMethod(StaticMethodDescriptor staticMethodDescriptor) {
		Class theClass = loadClass(staticMethodDescriptor.getClassName(),
									staticMethodDescriptor.getPluginID());
		if (theClass==null)
			return null;
		Method theMethod = null;
		try {
			String methodSignature = staticMethodDescriptor.getSignature(); 
			theMethod = classToMethodSignatureToMethodCach.getMethod(theClass,methodSignature);
			if(theMethod==null){
			   theMethod = theClass.getMethod(staticMethodDescriptor.getName(),
											staticMethodDescriptor.getParameterTypes());
				classToMethodSignatureToMethodCach.addMethod(theClass,methodSignature,theMethod);
			}
		} catch (SecurityException e) {
			// no special handling needed;
		} catch (NoSuchMethodException e) {
			// no special handling needed;
		}
		return theMethod;
	}

	/**
	 * Check the interfaces the whole way up. If one of them matches
	 * <code>className</code> return <code>true</code>. Optimized to look
	 * first in a cache of previously retrieved results.
	 * 
	 * @param interfaceToCheck
	 *            The interface whose name we are testing.
	 * @param className
	 *            the name of the interface to we are trying to match
	 * @return <code>true</code> if one of the interfaces in the hierarchy
	 *         matches <code>className</code>,<code>false</code>
	 *         otherwise.
	 */
	private static boolean checkInterfaceHierarchy(Class interfaceToCheck, String className) {
		
		if ( contains(isNotAssignableTable, interfaceToCheck, className) ) {
			return false;
		}
		
		if ( contains(isAssignableTable, interfaceToCheck, className) ) {
			return true;
		}
		
		boolean result = checkInterfaceHierarchyNoCache(interfaceToCheck,className);
		
		if (result) {
			add(isAssignableTable, interfaceToCheck, className);
		} else {
			add(isNotAssignableTable, interfaceToCheck, className);
		}
		
		return result;
	}

	/**
	 * Check the interfaces the whole way up. If one of them matches
	 * <code>className</code> return <code>true</code>.
	 * 
	 * @param interfaceToCheck
	 *            The interface whose name we are testing.
	 * @param className
	 *            the name of the interface to we are trying to match
	 * @return <code>true</code> if one of the interfaces in the hierarchy
	 *         matches <code>className</code>,<code>false</code>
	 *         otherwise.
	 */
	private static boolean checkInterfaceHierarchyNoCache(Class interfaceToCheck, String className) {
		if(interfaceToCheck.getName().equals(className))
			return true;
		Class[] superInterfaces = interfaceToCheck.getInterfaces();
		for (int i = 0; i < superInterfaces.length; i++) {
			if(checkInterfaceHierarchy(superInterfaces[i], className))
				return true;
		}
		return false;
	}
	
	/**
	 * Determines whether the <code>map</code> contains an entry for the
	 * <key,value>pair.
	 * 
	 * @param map
	 *            the map in which to find the key and value
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return <code>true</code> if the map contains the key/value pair,
	 *         <code>false</code> otherwise
	 */
	private static boolean contains(Map map, Object key, String value) {
		
		boolean result = false;
		
		Object val = map.get(key);
		if (val != null) {
			Set values = (Set)val;
			result = values.contains(value);
		}
		
		return result;
	}
	
	/**
	 * Adds the <key,value>pair to the <code>map</code>.
	 * 
	 * @param map
	 *            the map in which to add the value
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	private static void add(Map map, Object key, String value) {
		
		Set values = (Set)map.get(key);
		if (values == null) {
			values = new HashSet();
			map.put(key, values);
		}
		
		values.add(value);
	}
}
