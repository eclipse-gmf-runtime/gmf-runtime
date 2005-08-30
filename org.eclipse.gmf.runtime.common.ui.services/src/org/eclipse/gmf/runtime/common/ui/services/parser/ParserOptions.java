/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.parser;

/**
 * This is a wrapper used for defining, setting, and retrieving flags that can 
 * be used for the parser operations that take a flags int as a parameter.
 *  
 * @author chmahone, "unSet" method added by choang
 */
public final class ParserOptions {
    private int flags;

    /**
     * Default constructor to initialize ParserOptions with no custom flags.
     */
    public ParserOptions() {
        flags = 0;
    }

    /**
     * Constructor that takes flags in as a parameter.
     * 
     * @param flags that describe how the text should be parsed.  Some flags
     * are defined in this class.
     */
    public ParserOptions(int flags) {
        this.flags = flags;
    }

    // Caution: When adding parser options be sure that there are
    // no overlapping options.  Some of the options are used by 
    // more than one parser.

    /**
     * Default.  No parser options.
     */
    public static final ParserOptions NONE = new ParserOptions(0);

    // ListItemParser options
    /**
     * Display the visibility of the item.
     */
    public static final ParserOptions VISIBILITY_STYLE_TEXT =
        new ParserOptions(1 << 1);

    /**
     * Display the Stereotype.
     */
    public static final ParserOptions STEREOTYPE_STYLE_TEXT =
        new ParserOptions(1 << 2);

    // NameParser options
    /**
     * Show parent name.
     */
    public static final ParserOptions SHOW_PARENT_NAME =
        new ParserOptions(1 << 3);

    // OperationParser options    
    /**
     * Show signature.
     */
    public static final ParserOptions SHOW_SIGNATURE =
        new ParserOptions(1 << 4);

    /**
     * Show type.
     */
    public static final ParserOptions SHOW_TYPE = new ParserOptions(1 << 5);

    /**
     * Show alias.
     */
    public static final ParserOptions SHOW_ALIAS = new ParserOptions(1 << 6);
    
    /**
     * Show that the element is derived.
     */
    public static final ParserOptions SHOW_DERIVED = new ParserOptions(1 << 7);
    
    // MessageParser options
    /**
     * Show number of the message.
     */
    public static final ParserOptions SHOW_SEQUENCE_NUMBER = new ParserOptions(1 << 8);

    // PatternsTemplateArgumentParser options
    /**
     * Show that there are bound arguments.
     */
    public static final ParserOptions BIND_STYLE_TEXT = new ParserOptions(1 << 9);
    
    /**
     * Show the type.
     */
    public static final ParserOptions TYPE_STYLE_TEXT = new ParserOptions(1 << 10);
    
    /**
     * Indicates that placeholders should be ignored - if there are no data, no
     * placeholder will be generated. An example would be guillemets for the
     * stereotype list. If there are no stereotypes, the guillemets would be the
     * placeholder.
     */
    public static final ParserOptions IGNORE_PLACEHOLDERS = new ParserOptions(1 << 11);
    
    /**
     * Indicates that message signatures should use the "<parameter name> = <value>" notation.
     */
    public static final ParserOptions USE_PARAMETER_NAMES = new ParserOptions(1 << 12);
    
    /**
     * Returns the options as an int so they can be passed to operations.
     * @return int the options in int form
     */
    public int intValue() {
        return flags;
    }

    /**
     * Checks if the specified option is set in the flags supplied.
     * @param flags     int representing the flags
     * @param option    one of the defined ParserOptions
     * @return boolean  true if this option is set; false otherwise
     */
    public static boolean isSet(int flags, ParserOptions option) {
        if ((flags & option.flags) != 0)
            return true;
        return false;
    }

    /**
     * Sets an option in this ParserOptions.
     * @param option    one of the defined ParserOptions
     */
    public void set(ParserOptions option) {
        flags = flags | option.flags;
    }
    
    /**
     * UnSets an option in the this ParserOptions.
     * @param option one of the defined ParserOptions
     */
    public void unSet(ParserOptions option){
    	flags = flags &~ option.intValue();
    }

}
