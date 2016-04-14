/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer.simple;

import org.mule.runtime.core.api.lifecycle.InitialisationException;
import org.mule.runtime.core.api.transformer.DiscoverableTransformer;
import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.config.i18n.CoreMessages;
import org.mule.runtime.core.transformer.AbstractTransformer;
import org.mule.runtime.core.transformer.types.DataTypeFactory;
import org.mule.runtime.core.util.BeanUtils;
import org.mule.runtime.core.util.ClassUtils;

import java.util.Map;

/**
 * Creates and object of type {@code getReturnDataType().getType()} and populates values of a
 * {@link java.util.Map} as bean properties on the object.
 * The bean class name can also be passed in as a property on the Map (which gets removed once read).
 * The {@link MapToBean#CLASS_PROPERTY} should be set as a fully qualified class name string.
 */
public class MapToBean extends AbstractTransformer implements DiscoverableTransformer
{
    /**
     * {@value}
     */
    public static final String CLASS_PROPERTY = "className";

    private int priorityWeighting = DiscoverableTransformer.DEFAULT_PRIORITY_WEIGHTING;

    public MapToBean()
    {
        registerSourceType(DataTypeFactory.create(Map.class));
        setReturnDataType(DataTypeFactory.OBJECT);
    }

    @Override
    public void initialise() throws InitialisationException
    {
        super.initialise();
        if(getReturnDataType().getType().equals(Object.class))
        {
            throw new InitialisationException(CoreMessages.propertiesNotSet("returnClass"), this);
        }
    }

    @Override
    protected Object doTransform(Object src, String encoding) throws TransformerException
    {
        try
        {
            Map props = (Map)src;
            String c = (String)props.remove(CLASS_PROPERTY);
            Class clazz = getReturnDataType().getType();
            if(c==null && clazz.equals(Object.class))
            {
                throw new TransformerException(CoreMessages.transforemrMapBeanClassNotSet());
            }
            else if (c!=null)
            {
                clazz = ClassUtils.loadClass(c, getClass());
            }

            Object result = ClassUtils.instanciateClass(clazz, ClassUtils.NO_ARGS);
            BeanUtils.populate(result, props);

            return result;
        }
        catch (Exception e)
        {
            throw new TransformerException(this, e);
        }
    }

    public int getPriorityWeighting()
    {
        return priorityWeighting;
    }

    public void setPriorityWeighting(int weighting)
    {
        priorityWeighting = weighting;
    }
}
