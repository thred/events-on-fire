package com.google.code.eventsonfire.swing;

import com.google.code.eventsonfire.Events;

public class SwingEvents
{

    static
    {
        Events.registerStrategy(new SwingEventHandlerAnnotationStrategy());
    }
    
}
