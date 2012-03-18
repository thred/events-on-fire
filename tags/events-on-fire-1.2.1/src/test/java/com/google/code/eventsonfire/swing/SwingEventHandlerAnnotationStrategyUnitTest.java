package com.google.code.eventsonfire.swing;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.code.eventsonfire.EventHandlerInfo;

@Test
public class SwingEventHandlerAnnotationStrategyUnitTest
{

    private SwingEventHandlerAnnotationStrategy strategy;

    @BeforeTest
    public void init()
    {
        strategy = new SwingEventHandlerAnnotationStrategy();
    }

    @Test
    public void empty()
    {
        Collection<EventHandlerInfo> infos = new LinkedHashSet<EventHandlerInfo>();

        strategy.scan(infos, new Object()
        {

            @SuppressWarnings("unused")
            @SwingEventHandler
            public void eventHandler(JButton producer, String event)
            {
                // intentionally left blank
            }

        }.getClass());

        assert (infos.size() == 1);

        EventHandlerInfo info = infos.iterator().next();

        assert (info instanceof SwingEventHandlerAnnotationInfo);

        SwingEventHandlerAnnotationInfo eventHandlerInfo = (SwingEventHandlerAnnotationInfo) info;

        assert ("eventHandler".equals(eventHandlerInfo.getMethod().getName()));

        assert (eventHandlerInfo.getProducerTypes() != null);
        assert (eventHandlerInfo.getProducerTypes().length == 1);
        assert (eventHandlerInfo.getProducerTypes()[0] == JButton.class);

        assert (eventHandlerInfo.getEventTypes() != null);
        assert (eventHandlerInfo.getEventTypes().length == 1);
        assert (eventHandlerInfo.getEventTypes()[0] == String.class);
    }

    @Test
    public void overwritten()
    {
        Collection<EventHandlerInfo> infos = new LinkedHashSet<EventHandlerInfo>();

        strategy.scan(infos, new Object()
        {

            @SuppressWarnings("unused")
            @SwingEventHandler(producer = JButton.class, event = String.class)
            public void eventHandler(Object producer, Object event)
            {
                // intentionally left blank
            }

        }.getClass());

        assert (infos.size() == 1);

        EventHandlerInfo info = infos.iterator().next();

        assert (info instanceof SwingEventHandlerAnnotationInfo);

        SwingEventHandlerAnnotationInfo eventHandlerInfo = (SwingEventHandlerAnnotationInfo) info;

        assert ("eventHandler".equals(eventHandlerInfo.getMethod().getName()));

        assert (eventHandlerInfo.getProducerTypes() != null);
        assert (eventHandlerInfo.getProducerTypes().length == 1);
        assert (eventHandlerInfo.getProducerTypes()[0] == JButton.class);

        assert (eventHandlerInfo.getEventTypes() != null);
        assert (eventHandlerInfo.getEventTypes().length == 1);
        assert (eventHandlerInfo.getEventTypes()[0] == String.class);
    }

    @Test
    public void multiple()
    {
        Collection<EventHandlerInfo> infos = new LinkedHashSet<EventHandlerInfo>();

        strategy.scan(infos, new Object()
        {

            @SuppressWarnings("unused")
            @SwingEventHandler(producer = {JButton.class, JMenuItem.class}, event = {Integer.class, String.class})
            public void eventHandler(Object producer, Object event)
            {
                // intentionally left blank
            }

        }.getClass());

        assert (infos.size() == 1);

        EventHandlerInfo info = infos.iterator().next();

        assert (info instanceof SwingEventHandlerAnnotationInfo);

        SwingEventHandlerAnnotationInfo eventHandlerInfo = (SwingEventHandlerAnnotationInfo) info;

        assert ("eventHandler".equals(eventHandlerInfo.getMethod().getName()));

        assert (eventHandlerInfo.getProducerTypes() != null);
        assert (eventHandlerInfo.getProducerTypes().length == 2);
        assert (eventHandlerInfo.getProducerTypes()[0] == JButton.class);
        assert (eventHandlerInfo.getProducerTypes()[1] == JMenuItem.class);

        assert (eventHandlerInfo.getEventTypes() != null);
        assert (eventHandlerInfo.getEventTypes().length == 2);
        assert (eventHandlerInfo.getEventTypes()[0] == Integer.class);
        assert (eventHandlerInfo.getEventTypes()[1] == String.class);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void invalidProducer()
    {
        Collection<EventHandlerInfo> infos = new LinkedHashSet<EventHandlerInfo>();

        strategy.scan(infos, new Object()
        {

            @SuppressWarnings("unused")
            @SwingEventHandler(producer = JButton.class)
            public void eventHandler(JMenuItem producer, Object event)
            {
                // intentionally left blank
            }

        }.getClass());
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void invalidEvent()
    {
        Collection<EventHandlerInfo> infos = new LinkedHashSet<EventHandlerInfo>();

        strategy.scan(infos, new Object()
        {

            @SuppressWarnings("unused")
            @SwingEventHandler(event = String.class)
            public void eventHandler(Integer event)
            {
                // intentionally left blank
            }

        }.getClass());
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void invalidReturnType()
    {
        Collection<EventHandlerInfo> infos = new LinkedHashSet<EventHandlerInfo>();

        strategy.scan(infos, new Object()
        {

            @SuppressWarnings("unused")
            @SwingEventHandler
            public String eventHandler(String event)
            {
                return null;
            }

        }.getClass());
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void invalidParameters()
    {
        Collection<EventHandlerInfo> infos = new LinkedHashSet<EventHandlerInfo>();

        strategy.scan(infos, new Object()
        {

            @SuppressWarnings("unused")
            @SwingEventHandler
            public void eventHandler(JButton producer, String event, String invalid)
            {
                // intentionally left blank
            }

        }.getClass());
    }

}
