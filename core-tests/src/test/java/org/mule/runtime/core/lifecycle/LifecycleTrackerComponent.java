/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.lifecycle;

import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.MuleException;
import org.mule.runtime.core.api.component.Component;
import org.mule.runtime.core.api.construct.FlowConstruct;
import org.mule.runtime.core.api.construct.FlowConstructAware;
import org.mule.runtime.core.management.stats.ComponentStatistics;

import org.mockito.Mockito;

/**
 * @author David Dossot (david@dossot.net)
 */
public class LifecycleTrackerComponent extends AbstractLifecycleTracker implements FlowConstructAware, Component
{

    private FlowConstruct flowConstruct;

    public void springInitialize()
    {
        getTracker().add("springInitialize");
    }

    public void springDestroy()
    {
        getTracker().add("springDestroy");
    }

    public void setFlowConstruct(final FlowConstruct flowConstruct)
    {
        getTracker().add("setService");
        this.flowConstruct = flowConstruct;
    }

    public FlowConstruct getFlowConstruct()
    {
        return flowConstruct;
    }

    public ComponentStatistics getStatistics()
    {
        return Mockito.mock(ComponentStatistics.class);
    }

    public MuleEvent process(MuleEvent event) throws MuleException
    {
        return event;
    }
}
