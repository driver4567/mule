/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.xml.transformer;

import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.transformer.types.DataTypeFactory;

/**
 * <code>ObjectToXml</code> converts any object to XML using Xstream. Xstream uses
 * some clever tricks so objects that get marshalled to XML do not need to implement
 * any interfaces including Serializable and you don't even need to specify a default
 * constructor. If <code>MuleMessage</code> is configured as a source type on this
 * transformer by calling <code>setAcceptMuleMessage(true)</code> then the MuleMessage
 * will be serialised. This is useful for transports such as TCP where the message
 * headers would normally be lost.
 */

public class ObjectToXml extends AbstractXStreamTransformer
{

    public ObjectToXml()
    {
        this.registerSourceType(DataTypeFactory.OBJECT);
        this.setReturnDataType(DataTypeFactory.XML_STRING);
    }

    public boolean isAcceptMuleMessage()
    {
        return this.sourceTypes.contains(MULE_MESSAGE_DATA_TYPE);
    }

    public void setAcceptMuleMessage(boolean value)
    {
        if (value)
        {
            this.registerSourceType(DataTypeFactory.MULE_MESSAGE);
        }
        else
        {
            this.unregisterSourceType(DataTypeFactory.MULE_MESSAGE);
        }
    }

    @Override
    public Object transformMessage(MuleEvent event, String outputEncoding) throws TransformerException
    {
        Object src = event.getMessage().getPayload();
        /*
         * If the MuleMessage source type has been registered that we can assume that
         * the whole message is to be serialised to Xml, not just the payload. This
         * can be useful for protocols such as tcp where the protocol does not
         * support headers, thus the whole messgae needs to be serialized
         */
        if (this.isAcceptMuleMessage())
        {
            src = event.getMessage();
        }
        return this.getXStream().toXML(src);
    }
}
