/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.processor.strategy;

import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.processor.MessageProcessor;
import org.mule.runtime.core.api.processor.MessageProcessorBuilder;
import org.mule.runtime.core.api.processor.MessageProcessorChainBuilder;

import java.util.List;

import javax.resource.spi.work.WorkManager;

/**
 * This strategy uses a {@link WorkManager} to schedule the processing of each message processors in a new
 * worker thread.
 */
public class ThreadPerProcessorProcessingStrategy extends AsynchronousProcessingStrategy
{

    @Override
    public void configureProcessors(List<MessageProcessor> processors,
                                    org.mule.runtime.core.api.processor.StageNameSource nameSource,
                                    MessageProcessorChainBuilder builder,
                                    MuleContext muleContext)
    {
        for (int i = 0; i < processors.size(); i++)
        {
            MessageProcessor processor = processors.get(i);

            builder.chain(createAsyncMessageProcessor(nameSource, muleContext));

            if (processor instanceof MessageProcessor)
            {
                builder.chain(processor);
            }
            else if (processor instanceof MessageProcessorBuilder)
            {
                builder.chain((MessageProcessorBuilder) processor);
            }
            else
            {
                throw new IllegalArgumentException(
                    "MessageProcessorBuilder should only have MessageProcessor's or MessageProcessorBuilder's configured");
            }
        }
    }
}
