/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.l10n;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;

/**
 * <p>
 * AbstractUIResourceManager extends the AbstractResourceManager to provide UI
 * capabilities including fonts and images.   
 * </p>
 * <p>
 * Subclasses may override, if necessary, resource initialization methods
 * <code>
 * 	initializeImageResources()
 * 	initializeFontResources()
 * </code>
 * 
 * The <code>initilaizeUIResources()</code> method should be overridden by
 * subclasses in order to add/remove initialization of any additional/
 * redundant resource types
 * </p>
 * <p>
 * The subclass should override if necessary the default names of the message
 * resource bundle file (default MessageBundle) and font registry bundle file
 * (fonts), using methods:
 * <code>
 * 		getMessageBundleName()
 * 		getFontRegistryName()
 * </code>
 * </p>
 * <p>
 * On the file system side the convention is to store all resources associated with the given
 * cluster in the designated i18n package. That includes messages.properties, fonts.properties,
 * /icon directory to store images. For example, com.ibm.diagram.collaboration cluster will store
 * all of it's resources in the:
 * com/
 * 	rational/
 * 		diagram/
 * 			collaboration/
 * 				l10n/
 * 					 messages.properties
 * 					 fonts.properties
 * 					 icons/	
 * </p>
 * <p>
 * Synchronization aspects
 * 
 * The instances of this class are immutable, once created and initialized. However, clients still have
 * the ability to modify fontRegistry and imageRegistry.It is not allowed to replace existing images in 
 * the image registry. An attempt to do so will result in the exception being thrown.
 * @see org.eclipse.jface.resource.FontRegistry
 * @see org.eclipse.jface.resource.ImageRegistry
 * @see java.util.ResourceBundle
 * 
 * @author wdiu, Wayne Diu, refactored from common.core's AbstractResourceManager
 * @canBeSeenBy %partners
 * 
 */
public abstract class AbstractUIResourceManager extends AbstractResourceManager {

	private static final String IMAGES = "icons"; //$NON-NLS-1$

	private static final String FONTS = ".fonts"; // $NON-NLS-1$ //$NON-NLS-1$

	private static final String TRANSLATE_PATH_ARGUMENT = "$nl$"; //$NON-NLS-1$
	
	private static final String MISSING_IMAGE_RESOURCE_MESSAGE = "Attempt to access missing image resource ({0})."; //$NON-NLS-1$
	
	/** 
	 * The name of the path to the images, relative to the client
	 * l10n package. The default is "/icons".
	 */
	private String imagePathName = null;

	/** 
	 * The name of the font registry file. The default is "fonts".
	 */
	private String fontRegistryName = null;
	
	/**
	 *  image registry 
	 */
	private ImageRegistry imageRegistry = null;

	/**
	 * font registry
	 */
	private FontRegistry fontRegistry = null;
	
	/**
	 * image instance used as a return value when image with the given name
	 * is not found in the image registry 
	 */
	private Image missingImage = null;
	
	/**
	 * Map for created fonts
	 */
	private Map fonts = null;
	
	
	/**
	 * Create a resource manager instance and initialize resources it will manage.
	 * Subclasses should be declared final and have a singleton instance. If names
	 * of the fonts bundle or image path are different from the default the
	 * subclasses should override:
	 * getImagePathDefaultName() or
	 * getFontRegistryDefaultName()
	 */
	protected AbstractUIResourceManager() {
		super();

		imagePathName = getImagePathDefaultName();
		fontRegistryName = getFontRegistryDefaultName();
		
		initializeUIResources();
	}	
	

	/**
	 * Returns the imageRegistry.
	 * An image registry maintains a mapping between symbolic image names 
	 * and SWT image objects or special image descriptor objects which
	 * defer the creation of SWT image objects until they are needed.
	 * <p>
	 * An image registry owns all of the image objects registered
	 * with it, and automatically disposes of them when the SWT Display
	 * that creates the fonts is disposed. Because of this, clients do not 
	 * need to (indeed, must not attempt to) dispose of these images themselves.
	 * </p>
	 * <p>
	 * Clients may instantiate this class (it was not designed to be subclassed).
	 * </p>
	 * <p>
	 *
	 * <p>
	 * Unlike the FontRegistry, it is an error to replace images. As a result
	 * there are no events that fire when values are changed in the registry
	 * </p>     
	 * @return the image registry
	 */
	protected ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			forceInitializeImageResources();
		}
		return imageRegistry;
	}

	/**
	 * Returns the fontRegistry value. 
	 * A font registry maintains a mapping between symbolic font names 
	 * and SWT fonts.
	 * <p>
	 * A font registry owns all of the font objects registered
	 * with it, and automatically disposes of them when the SWT Display
	 * that creates the fonts is disposed. Because of this, clients do 
	 * not need to (indeed, must not attempt to) dispose of font 
	 * objects themselves.
	 * 
	 * If the font registry is not being used by the client application
	 * the subclass should override <code>initializeResources()</code>
	 * method to exclude font registry from being initialized.
	 * 
	 * Consult org.eclipse.jface.resource.FontRegistry javadoc for the specification
	 * on the format of the font registry file
	 * 
	 * @return org.eclipse.jface.resource.FontRegistry - font registry
	 */

	protected FontRegistry getFontRegistry() {
		return fontRegistry;
	}

	/**
	 * Initialize imageRegistry with the default value.
	 * 
	 * This method provides single assignment point to the private variable
	 * imageRegistry.
	 * 
	 * Client subclasses can extend the functionality of this method by
	 * overriding it and adding images or image descriptors to the
	 * imageRegistry. This ca be done by calling super.initializeImageRegistry()
	 * and then use the convinience methods:
	 * <code>createImage(java.lang.String)</code> and
	 * <code>createImageDescriptor(java.lang.String)</code> to create images
	 * and image descriptors to populate imageRegistry with values.
	 * 
	 * Cached instance of an <code>Image</code> will take up more memory
	 * space, but will be shared among the clients. Cached instance of an
	 * <code>ImageDescriptor</code> will take less memory for storage, but a
	 * new instance of an image will created each time a client retrieves the
	 * image from the store.
	 * 
	 * It is recommended to use image registry to cache all images - the image
	 * registry will free all the image resources automatically when application
	 * exist.
	 * 
	 * It is not allowed to replace an image registry value, once cached. The
	 * exception will thrown when clients will attempt to do that.
	 * 
	 * @see org.eclipse.jface.resource.ImageRegistry
	 * @see #createImage(java.lang.String)
	 * @see #createImageDescriptor(java.lang.String)
	 * 
	 * @deprecated There is no longer any need to call this method. The image
	 *             registry undergoes lazy initialization the first time it is
	 *             accessed.
	 */
	protected void initializeImageResources() {
		// RATLC00537762
		// Initializing the image resources during plugin activation causes the
		// main thread to timeout, so we now wait until resources are actually
		// required before initializing the image resources.
	}
	
	/**
	 * Initialize imageRegistry with the default value. 
	 * 
	 * This method provides single assignment point to the private variable imageRegistry.
	
	 * Client subclasses can extend the functionality of this method by overriding it and
	 * adding images or image descriptors to the imageRegistry. 
	 * This ca be done by calling super.initializeImageRegistry() and then use the convinience
	 * methods:
	 * <code>createImage(java.lang.String)</code> 
	 * 	and 
	 * <code>createImageDescriptor(java.lang.String)</code>
	 * to create images and image descriptors to populate imageRegistry with values.
	 * 
	 * Cached instance of an <code>Image</code> will take up more memory space, but will 
	 * be shared among the clients. Cached instance of an <code>ImageDescriptor</code> 
	 * will take less memory for storage, but a new instance of an image will created each 
	 * time a client retrieves the image from the store.
	 * 
	 * It is recommended to use image registry to cache all images - the image registry will
	 * free all the image resources automatically when application exist.
	 * 
	 * It is not allowed to replace an image registry value, once cached. The exception
	 * will thrown when clients will attempt to do that.
	 * @see org.eclipse.jface.resource.ImageRegistry
	 * @see #createImage(java.lang.String)
	 * @see #createImageDescriptor(java.lang.String)
	 */
	protected void forceInitializeImageResources() {
		// Perform the image resource initialization in the UI thread.
		// See RATLC00248220.
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				imageRegistry = createImageRegistry();

				// create an instance of the Image that will be used as the default 
				// return value whenever an image resource is not found in the image
				// registry
				missingImage =
					ImageDescriptor.getMissingImageDescriptor().createImage();

				// make sure that the image will be disposed of when the display is gone
				Display.getCurrent().disposeExec(new Runnable() {
					public void run() {
						missingImage.dispose();
					}
				});
			}
		});

	}

	/**
	 * Initialize font registry with the default value
	 * 
	 * The font registry is initialized from a property file. To consult on 
	 * exact format of the file see javadoc for 
	 * org.eclipse.jface.resource.FontRegistry.
	 * 
	 * In case if the fonts property file is missing an EmptyFontRegistry 
	 * instance will be created as a default return value.
	 * @see org.eclipse.jface.resource.FontRegistry
	 * @see #createFontRegistry()
	 */
	protected void initializeFontResources() {
		fontRegistry = createFontRegistry();
	}

	/**
	 * Creates an image registry
	 * 
	 * Subclasses should populate the image registry with instances of <code>
	 * org.eclipse.swt.graphics.Image</code>  or 
	 * <code>org.eclipse.jface.resource.ImageDescriptor</code>
	 * from their /icon directory.
	 * 
	 * The image registry will dispose of the Image instances when application exits
	 * @return org.eclipse.jface.resource.ImageRegistry - image registry
	 */
	protected ImageRegistry createImageRegistry() {
		return new ImageRegistry();
	}

	/**
	 * Creates font registry
	 * 
	 * If the property file for the font registry is missing an EmptyFontRegistry
	 * instance will be created and returned as a default value.
	 * 
	 * @return org.eclipse.jface.resource.FontRegistry - font registry
	 */
	protected FontRegistry createFontRegistry() {
		try {
			return new FontRegistry(getFontRegistryName(), getClass().getClassLoader());
		} catch (MissingResourceException mre) {
			Trace.catching(CommonCorePlugin.getDefault(), CommonCoreDebugOptions.EXCEPTIONS_CATCHING, getClass(), "createFontRegistry", mre); //$NON-NLS-1$
			Log.error(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.L10N_FAILURE, "createFontRegistry", mre); //$NON-NLS-1$
			return new EmptyFontRegistry(getFontRegistryName());
		}
	}
	
	/**
	 * Returns the font in JFace's font registry with the given
	 * symbolic font name.
	 * Convenience method equivalent to
	 * <pre>
	 * JFaceResources.getFontRegistry().get(symbolicName)
	 * </pre>
	 *
	 * If an error occurs, returns the default font.
	 * @param symbolicName java.lang.String the symbolic font name
	 * @return - the font with the sybolic name
	 */
	public Font getFont(String symbolicName) {
		return getFontRegistry().get(symbolicName);
	}

	/**
	 * Returns the Image from the image store. Returns a newly created default 
	 * image if the image is not in the registry
	 * 
	 * @return  image for the given key
	 * @param   key java.lang.String the key to retrieve the value
	 */
	public Image getImage(String key) {

		Image image = getImageRegistry().get(key);
		if (image == null) {

			createCachedImage(key);
			image = getImageRegistry().get(key);
			if (image == null) {
				Log.warning(
					CommonCorePlugin.getDefault(),
					CommonCoreStatusCodes.L10N_FAILURE,
					MessageFormat.format(
						MISSING_IMAGE_RESOURCE_MESSAGE,
						new Object[] { key }));
				return missingImage;

			}
		}
		return image;

	}

	/**
	 * Determines if the image with the given key is cached in the image
	 * registry.
	 * @param  key     the key to the image
	 * @return boolean <code>true</code> if the image with the given key is
	 * 			cached in the image registry, otherwise <code>false</code>.
	 */
	public boolean containsCachedImage(String key) {
		return getImageRegistry().get(key) != null;
	}

	/**
	 * Convience method used to get the URL of a plugin image resource
	 * @param filename - the filename of the resource
	 * @return the URL for the resource specified in the filename
	 */
	public URL createImageURL(String filename) {
		/* 
		 * prefix path with "$nl$" and use Plugin.find() to search for the 
		 * locale specific file
		 */
		IPath path =
			new Path(TRANSLATE_PATH_ARGUMENT).append(
				getImagePathName() + filename);
		return getPlugin().find(path);
	}

	/**
	* Convenience method that creates and returns a new image descriptor from a 
	* file.
	* It is important to note that if the image object is not being cached to the
	* image registry - the client must take care of diposing of the resource.
	* 
	* This method will attempt to find the translated image by first 
	* searching in the locale specific "/nl" subdirectory of the plugin and 
	* fragments.
	* 
	* @see org.eclipse.swt.graphics.Image#dispose()    
	* @see org.eclipse.core.runtime.Plugin#find(org.eclipse.core.runtime.IPath)
	* @param filename java.lang.String - the file name
	* @return - the translated image descriptor object if found, otherwise
	* 			 the untranslated image descriptor
	*/
	public ImageDescriptor createImageDescriptor(String filename) {
		URL url = createImageURL(filename);
		return ImageDescriptor.createFromURL(url);
	}

	/**
	* Convenience method that creates and returns a new image from a file.
	* It is important to note that if the image object is not being cached to the
	* image registry - the client must take care of diposing of the resource.
	* @see org.eclipse.swt.graphics.Image#dispose()
	* @param filename java.lang.String the file name
	* @return image object
	 */
	public Image createImage(String filename) {
		return createImageDescriptor(filename).createImage();
	}

	/**    
	* Constructs a new cursor given a device, image and mask
	* data describing the desired cursor appearance, with the x
	* and y co-ordinates 0 and 0 (that is, the point
	* within the area covered by the cursor which is considered
	* to be where the on-screen pointer is "pointing").
	* @param imageFileSource java.lang.String source image file name
	* @param imageFileMask   java.lang.String mask image file name
	* @return  - cursor object
	*/
	public Cursor createCursor(String imageFileSource, String imageFileMask) {
		return new Cursor(
			null,
			createImageDescriptor(imageFileSource).getImageData(),
			createImageDescriptor(imageFileMask).getImageData(),
			0,
			0);
	}

	/**
	 * A convinience method that creates an Image instance fron the file and
	 * stores it into the image registry
	 * @param imageName java.lang.String image file name
	 */
	public void createCachedImage(String imageName) {
		getImageRegistry().put(imageName, createImage(imageName));
	}

	/**
	 * A convinience method that creates an ImageDescriptor instance fron the file and
	 * stores it into the image registry
	 * @param imageName java.lang.String image file name
	 */
	public void createCachedImageDescriptor(String imageName) {
		getImageRegistry().put(imageName, createImageDescriptor(imageName));
	}

	/**
	 * A convinience method that stores an ImageDescriptor into the image registry
	 * @param key the key
	 * @param imageDescriptor the image descriptor
	 */
	public void createCachedImageDescriptor(String key, ImageDescriptor imageDescriptor) {
		getImageRegistry().put(key, imageDescriptor);
	}

	/**
	 * Returns the name of the font registry bundle, including the package path.
	 * E.g. for fonts.properties file located at 
	 * com.ibm.diagrams.collaboration.l10n
	 * the name returned will be com.ibm.diagrams.collaboration.l10n.fonts
	 * @return - font registry name
	 */

	protected String getFontRegistryName() {
		return fontRegistryName;
	}

	
	/**
	 * Returns default name for the font registry. Subclasses should override
	 * if the font registry name differs from the default
	 * @return - default name for the font registry
	 */
	protected String getFontRegistryDefaultName() {
		return getPackageName() + FONTS;
	}
	
	/**
	 * Returns the Font based on the FontData given; creates a new Font
	 * (and caches it) if this is a new one being requested; otherwise, 
	 * returns a cached Font.
	 * 
	 * The FontRegistry from the parent AbstractResourceManager class
	 * could not be used because if the Font didn't exist it returns a 
	 * default font.
	 * 
	 * @param device the device to create the font on
	 * @param fd FontData from which to find or create a Font
	 * @return the Font
	 */
	public Font getFont(Device device, FontData fd) {
		if (fonts == null)
			fonts = new HashMap();
			
		Object value = fonts.get(fd.toString());
		if (value != null) {
			return (Font) value;
		}
		Font newFont = new Font(device, fd);
		fonts.put(fd.toString(), newFont);
		return newFont;
	}
	
	/**
	 * Returns the name of the path to the images
	 * E.g. for image files located at 
	 * com.ibm\diagrams\collaboration\l10n\icon 
	 * the name returned will be \icon
	 * @return - images path name
	 */
	protected String getImagePathName() {
		return imagePathName;
	}	

	/**
	 * Returns default name for the imagePathName. Subclasses should override
	 * if the image path name differs from the default
	 * @return - default name for the image path
	 */
	protected String getImagePathDefaultName() {
		return IMAGES + IPath.SEPARATOR;
	}
	
	/** Image descriptor map. */
	protected Map imageDescriptors = new HashMap();

	/**
	* Creates a new image descriptor for the filename specified and adds
	* it to the map of image descriptors.
	* @param filename	the file name and key
	*/
	protected void addImageDescriptor(String filename) {
		imageDescriptors.put(filename, createImageDescriptor(filename));
	}

	/**
	* Gets an image descriptor for the key.  If the image descriptor
	* is not in the map it will attempt to create it and then put it
	* in the map.
	* @param key the key to retrieve the value
	* @return the ImageDescriptor
	*/
	public ImageDescriptor getImageDescriptor(String key) {
		
		if (imageDescriptors.get(key)==null){
			addImageDescriptor(key);
			return (ImageDescriptor)imageDescriptors.get(key);
		} else{
			return (ImageDescriptor)imageDescriptors.get(key);
		}
	}
	
	/**
	 * Load various UI resources - text, images, fonts. Do nothing by default.
	 * Subclasses should override this method to include initialization of the 
	 * particular resource types. 
	 */
	protected abstract void initializeUIResources();
}
