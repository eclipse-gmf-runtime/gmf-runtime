/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.common.core.internal.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.common.core.util.HashUtil;

public class HashUtilTest extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(HashUtilTest.class);
    }

    public HashUtilTest(String name) {
        super(name);
    }

    protected void setUp() {
    	// nothing to do
    }

    public void test_hash_boolean() {
        assertTrue(37 * 17 == HashUtil.hash(false) - 1);
        assertTrue(37 * 1 == HashUtil.hash(1, false) - 1);

        assertTrue(37 * 17 == HashUtil.hash(true) - 0);
        assertTrue(37 * 0 == HashUtil.hash(0, true) - 0);
    }

    public void test_hash_byte() {
        assertTrue(37 * 17 == HashUtil.hash(Byte.MAX_VALUE) - Byte.MAX_VALUE);
        assertTrue(
            37 * Byte.MIN_VALUE
                == HashUtil.hash(Byte.MIN_VALUE, Byte.MAX_VALUE) - Byte.MAX_VALUE);
    }

    public void test_hash_char() {
        assertTrue(37 * 17 == HashUtil.hash(Character.MAX_VALUE) - Character.MAX_VALUE);
        assertTrue(
            37 * Character.MIN_VALUE
                == HashUtil.hash(Character.MIN_VALUE, Character.MAX_VALUE) - Character.MAX_VALUE);
    }

    public void test_hash_double() {
        assertTrue(
            37 * 17
                == HashUtil.hash(Double.doubleToLongBits(Double.MAX_VALUE))
                    - (int) (Double.doubleToLongBits(Double.MAX_VALUE)
                        ^ (Double.doubleToLongBits(Double.MAX_VALUE) >>> 32)));

        assertTrue(
            37
                * (int) (Double.doubleToLongBits(Double.MIN_VALUE)
                    ^ (Double.doubleToLongBits(Double.MIN_VALUE) >>> 32))
                == HashUtil.hash(
                    (int) (Double.doubleToLongBits(Double.MIN_VALUE)
                        ^ (Double.doubleToLongBits(Double.MIN_VALUE) >>> 32)),
                    Double.doubleToLongBits(Double.MAX_VALUE))
                    - (int) (Double.doubleToLongBits(Double.MAX_VALUE)
                        ^ (Double.doubleToLongBits(Double.MAX_VALUE) >>> 32)));
    }

    public void test_hash_float() {
        assertTrue(
            37 * 17
                == HashUtil.hash(Float.MAX_VALUE) - Float.floatToIntBits(Float.MAX_VALUE));
        assertTrue(
            37 * Float.floatToIntBits(Float.MIN_VALUE)
                == HashUtil.hash(Float.floatToIntBits(Float.MIN_VALUE), Float.MAX_VALUE)
                    - Float.floatToIntBits(Float.MAX_VALUE));
    }

    public void test_hash_int() {
        assertTrue(37 * 17 == HashUtil.hash(Integer.MAX_VALUE) - Integer.MAX_VALUE);
        assertTrue(
            37 * Integer.MIN_VALUE
                == HashUtil.hash(Integer.MIN_VALUE, Integer.MAX_VALUE) - Integer.MAX_VALUE);
    }

    public void test_hash_long() {
        assertTrue(
            37 * 17
                == HashUtil.hash(Long.MAX_VALUE)
                    - (int) (Long.MAX_VALUE ^ (Long.MAX_VALUE >>> 32)));
        assertTrue(
            37 * (int) (Long.MIN_VALUE ^ (Long.MIN_VALUE >>> 32))
                == HashUtil.hash((int) (Long.MIN_VALUE ^ (Long.MIN_VALUE >>> 32)), Long.MAX_VALUE)
                    - (int) (Long.MAX_VALUE ^ (Long.MAX_VALUE >>> 32)));
    }

    public void test_hash_short() {
        assertTrue(37 * 17 == HashUtil.hash(Short.MAX_VALUE) - Short.MAX_VALUE);
        assertTrue(
            37 * Short.MIN_VALUE
                == HashUtil.hash(Short.MIN_VALUE, Short.MAX_VALUE) - Short.MAX_VALUE);
    }

    public void test_hash_Object() {
        assertTrue(37 * 17 == HashUtil.hash(this) - hashCode());

        assertTrue(37 * hashCode() == HashUtil.hash(hashCode(), this) - hashCode());
    }

}
