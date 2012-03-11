package com.google.code.eventsonfire.swing;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.google.code.eventsonfire.Events;
import com.google.code.eventsonfire.awt.AWTEvents;

public class SwingEvents extends AWTEvents
{

    /**
     * A custom implementation of the change listener interface using either the specified producer or the source of the
     * event.
     * 
     * @author ham
     */
    public static class EventsChangeListener extends AWTEvents.AbstractListener implements ChangeListener
    {

        /**
         * Creates a change listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsChangeListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void stateChanged(ChangeEvent event)
        {
            fire(event.getSource(), event);
        }
    }

    /**
     * A custom implementation of the list selection listener interface using either the specified producer or the source of the
     * event.
     * 
     * @author ham
     */
    public static class EventsListSelectionListener extends AWTEvents.AbstractListener implements ListSelectionListener
    {

        /**
         * Creates a list selection listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsListSelectionListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void valueChanged(ListSelectionEvent event)
        {
            fire(event.getSource(), event);
        }
    }

    /**
     * A custom implementation of the tree selection listener interface using either the specified producer or the source of the
     * event.
     * 
     * @author ham
     */
    public static class EventsTreeSelectionListener extends AWTEvents.AbstractListener implements TreeSelectionListener
    {

        /**
         * Creates a tree selection listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsTreeSelectionListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void valueChanged(TreeSelectionEvent event)
        {
            fire(event.getSource(), event);
        }
    }

    private static final ChangeListener DEFAULT_CHANGE_LISTENER = new EventsChangeListener(null);
    private static final ListSelectionListener DEFAULT_LIST_SELECTION_LISTENER = new EventsListSelectionListener(null);
    private static final TreeSelectionListener DEFAULT_TREE_SELECTION_LISTENER = new EventsTreeSelectionListener(null);

    static
    {
        Events.registerStrategy(new SwingEventHandlerAnnotationStrategy());
    }

    /**
     * Returns a change listener, that fires an event using the source of the event as producer.
     * 
     * @return the change listener
     */
    public static ChangeListener fireOnChange()
    {
        return DEFAULT_CHANGE_LISTENER;
    }

    /**
     * Creates a change listener, that fires an event using the specified producer and the specified tags. If the
     * producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the change listener
     */
    public static ChangeListener fireOnChange(Object producer, String... tags)
    {
        return new EventsChangeListener(producer, tags);
    }

    /**
     * Returns a list selection listener, that fires an event using the source of the event as producer.
     * 
     * @return the list selection listener
     */
    public static ListSelectionListener fireOnListSelection()
    {
        return DEFAULT_LIST_SELECTION_LISTENER;
    }

    /**
     * Creates a list selection listener, that fires an event using the specified producer and the specified tags. If the
     * producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the list selection listener
     */
    public static ListSelectionListener fireOnListSelection(Object producer, String... tags)
    {
        return new EventsListSelectionListener(producer, tags);
    }

    /**
     * Returns a tree selection listener, that fires an event using the source of the event as producer.
     * 
     * @return the tree selection listener
     */
    public static TreeSelectionListener fireOnTreeSelection()
    {
        return DEFAULT_TREE_SELECTION_LISTENER;
    }

    /**
     * Creates a tree selection listener, that fires an event using the specified producer and the specified tags. If the
     * producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the tree selection listener
     */
    public static TreeSelectionListener fireOnTreeSelection(Object producer, String... tags)
    {
        return new EventsTreeSelectionListener(producer, tags);
    }

}
