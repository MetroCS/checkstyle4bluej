////////////////////////////////////////////////////////////////////////////////
// BlueJ Checkstyle extension:
//    Checks Java source code for adherence to a set of rules.
// Copyright (C) 2007  Stephen Edwards
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////
package com.puppycrawl.tools.checkstyle.plugins.bluej;

import java.util.*;
import bluej.extensions.BlueJ;

//-------------------------------------------------------------------------
/**
 * This class provides a Properties subclass that retrieves properties from
 * BlueJ first, then its ancestor properties object second.  It uses an
 * extension proxy object to access BlueJ's property settings for that
 * extension.
 * @author Stephen Edwards
 * @version $Id: BlueJPropertiesAdapter.java,v 1.1 2007/08/19 03:13:03 stedwar2 Exp $
 */
public class BlueJPropertiesAdapter
    extends Properties
{
    //~ Instance variables ....................................................

    private BlueJ bluej;


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Constructs an adapter that reads properties from the given BlueJ
     * object.
     * @param bluej The BlueJ proxy object to use to store and retrieve
     * property settings.  If this is null, then properties will instead
     * be stored locally.
     */
    public BlueJPropertiesAdapter(BlueJ bluej)
    {
        this(bluej, null);
    }


    // ----------------------------------------------------------
    /**
     * Constructs a <code>BlueJChecker</code>.
     * @param bluej The BlueJ proxy object to use to store and retrieve
     * property settings.  If this is null, then properties will instead
     * be stored locally.
     * @param parent The Properties settings to inherit from
     */
    public BlueJPropertiesAdapter(BlueJ bluej, Properties parent)
    {
        super(parent);
        this.bluej = bluej;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Look up a property value.  First, look in this object itself.
     * If the key is not found in this property list, then check this BlueJ
     * extension's stored settings, if this object has a BlueJ proxy object.
     * If there is not an extension-specific value, also check BlueJ's
     * properties.  Finally, if the value still isn't found, search the
     * ancestor Properties objects, just like
     * {@link Properties#getProperty(String)}.
     * The method returns <code>null</code> if the property is not found.
     *
     * @param   key   the property key
     * @return  the value associated with the key.
     */
    @Override
    public String getProperty(String key)
    {
        // Note that we only have to override this version, not the
        // 2-argument version, since the parent class implements the
        // 2-argment getProperty() in terms of the 1-argument getProperty().

        // First, look on this object
        Object localVal = get(key);
        if (localVal != null)
        {
            return localVal.toString();
        }

        // If not found, next look in BlueJ's settings
        if (bluej != null)
        {
            String result = bluej.getExtensionPropertyString(key, null);
            if (result == null)
            {
                result = bluej.getBlueJPropertyString(key, null);
            }
            if (result != null)
            {
                return result;
            }
        }

        // Finally, search the ancestors
        return super.getProperty(key);
    }


    // ----------------------------------------------------------
    /**
     * Stores the given property to BlueJ's settings for this extension,
     * if we have a live Bluej proxy to use.  Otherwise, fall back to
     * regular Properties class behavior.
     * @param key The property key
     * @param value The value to store
     * @return  the previous value of the specified key, or
     *          <code>null</code> if it did not have one.
     */
    @Override
    public synchronized Object setProperty( String key, String value )
    {
        if (bluej == null)
        {
            return super.setProperty(key, value);
        }

        Object localOld = get(key);
        if (localOld != null)
        {
            remove(key);
        }
        bluej.setExtensionPropertyString(key, value);
        return localOld;
    }


    // ----------------------------------------------------------
    /**
     * Get the BlueJ proxy object used to look up property values.
     * @return the proxy
     */
    public BlueJ getBlueJ()
    {
        return bluej;
    }


    // ----------------------------------------------------------
    /**
     * Set the BlueJ proxy object used to look up property values.
     * @param bluej the new proxy to use
     */
    public void setBlueJ(BlueJ bluej)
    {
        this.bluej = bluej;
    }
}
