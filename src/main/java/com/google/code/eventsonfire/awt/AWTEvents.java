package com.google.code.eventsonfire.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.google.code.eventsonfire.Events;

public class AWTEvents
{

    /**
     * An abstract implementation for implementors of listener interfaces. If the producer is not specified, usually the
     * source of the event will be used. If the tags are not specified, the implementing listener will define it.
     * 
     * @author ham
     */
    public static abstract class AbstractListener
    {

        private final Object producer;
        private final String[] tags;

        /**
         * Creates an abstract implementation of the listener interface. If the producer is null, the source of the
         * event will be used as producer. If the tags is null or empty, the implementor will define the tag.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public AbstractListener(Object producer, String... tags)
        {
            super();

            this.producer = producer;
            this.tags = tags;
        }

        protected void fire(Object producerFallback, Object event, String... tagsFallback)
        {
            Object producer = (this.producer != null) ? this.producer : producerFallback;
            String[] tags = ((this.tags != null) && (this.tags.length > 0)) ? this.tags : tagsFallback;

            Events.fire(producer, event, tags);
        }
    }

    /**
     * A custom implementation of the action listener interface using either the specified producer or the source of the
     * event and the specified tags or the action command of the event as tag.
     * 
     * @author ham
     */
    public static class EventsActionListener extends AbstractListener implements ActionListener
    {

        /**
         * Creates an action listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer. If the tags is null or empty, the action command of the event will be used as
         * tag.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsActionListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent event)
        {
            fire(event.getSource(), event, event.getActionCommand());
        }
    }

    /**
     * A custom implementation of the item listener interface using either the specified producer or the source of the
     * event and the specified tags.
     * 
     * @author ham
     */
    public static class EventsItemListener extends AbstractListener implements ItemListener
    {

        /**
         * Creates an item listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsItemListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void itemStateChanged(ItemEvent event)
        {
            fire(event.getSource(), event);
        }
    }

    /**
     * A custom implementation of the focus listener interface using either the specified producer or the source of the
     * event and the specified tags or the action command of the event as tag. Reacts only to focus gained events.
     * 
     * @author ham
     */
    public static class EventsFocusGainedListener extends AbstractListener implements FocusListener
    {

        /**
         * Creates a focus listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsFocusGainedListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void focusGained(FocusEvent event)
        {
            fire(event.getSource(), event);
        }

        /**
         * {@inheritDoc}
         */
        public void focusLost(FocusEvent event)
        {
            // intentionally left blank
        }
    }

    /**
     * A custom implementation of the focus listener interface using either the specified producer or the source of the
     * event and the specified tags or the action command of the event as tag. Reacts only to focus lost events.
     * 
     * @author ham
     */
    public static class EventsFocusLostListener extends AbstractListener implements FocusListener
    {

        /**
         * Creates a focus listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsFocusLostListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void focusGained(FocusEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void focusLost(FocusEvent event)
        {
            fire(event.getSource(), event);
        }
    }

    /**
     * A custom implementation of the window listener interface using either the specified producer or the source of the
     * event and the specified tags or the action command of the event as tag. Reacts only to window activated events.
     * 
     * @author ham
     */
    public static class EventsWindowActivatedListener extends AbstractListener implements WindowListener
    {

        /**
         * Creates a window listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsWindowActivatedListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void windowActivated(WindowEvent event)
        {
            fire(event.getSource(), event);
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosed(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosing(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeactivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeiconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowIconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowOpened(WindowEvent event)
        {
            // intentionally left blank
        }

    }

    /**
     * A custom implementation of the window listener interface using either the specified producer or the source of the
     * event and the specified tags or the action command of the event as tag. Reacts only to window closed events.
     * 
     * @author ham
     */
    public static class EventsWindowClosedListener extends AbstractListener implements WindowListener
    {

        /**
         * Creates a window listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsWindowClosedListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void windowActivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosed(WindowEvent event)
        {
            fire(event.getSource(), event);
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosing(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeactivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeiconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowIconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowOpened(WindowEvent event)
        {
            // intentionally left blank
        }

    }

    /**
     * A custom implementation of the window listener interface using either the specified producer or the source of the
     * event and the specified tags or the action command of the event as tag. Reacts only to window closing events.
     * 
     * @author ham
     */
    public static class EventsWindowClosingListener extends AbstractListener implements WindowListener
    {

        /**
         * Creates a window listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsWindowClosingListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void windowActivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosed(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosing(WindowEvent event)
        {
            fire(event.getSource(), event);
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeactivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeiconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowIconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowOpened(WindowEvent event)
        {
            // intentionally left blank
        }

    }

    /**
     * A custom implementation of the window listener interface using either the specified producer or the source of the
     * event and the specified tags or the action command of the event as tag. Reacts only to window deactivated events.
     * 
     * @author ham
     */
    public static class EventsWindowDeactivatedListener extends AbstractListener implements WindowListener
    {

        /**
         * Creates a window listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsWindowDeactivatedListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void windowActivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosed(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosing(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeactivated(WindowEvent event)
        {
            fire(event.getSource(), event);
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeiconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowIconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowOpened(WindowEvent event)
        {
            // intentionally left blank
        }

    }

    /**
     * A custom implementation of the window listener interface using either the specified producer or the source of the
     * event and the specified tags or the action command of the event as tag. Reacts only to window deiconified events.
     * 
     * @author ham
     */
    public static class EventsWindowDeiconifiedListener extends AbstractListener implements WindowListener
    {

        /**
         * Creates a window listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsWindowDeiconifiedListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void windowActivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosed(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosing(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeactivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeiconified(WindowEvent event)
        {
            fire(event.getSource(), event);
        }

        /**
         * {@inheritDoc}
         */
        public void windowIconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowOpened(WindowEvent event)
        {
            // intentionally left blank
        }

    }

    /**
     * A custom implementation of the window listener interface using either the specified producer or the source of the
     * event and the specified tags or the action command of the event as tag. Reacts only to window iconified events.
     * 
     * @author ham
     */
    public static class EventsWindowIconifiedListener extends AbstractListener implements WindowListener
    {

        /**
         * Creates a window listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsWindowIconifiedListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void windowActivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosed(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosing(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeactivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeiconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowIconified(WindowEvent event)
        {
            fire(event.getSource(), event);
        }

        /**
         * {@inheritDoc}
         */
        public void windowOpened(WindowEvent event)
        {
            // intentionally left blank
        }

    }

    /**
     * A custom implementation of the window listener interface using either the specified producer or the source of the
     * event and the specified tags or the action command of the event as tag. Reacts only to window opened events.
     * 
     * @author ham
     */
    public static class EventsWindowOpenedListener extends AbstractListener implements WindowListener
    {

        /**
         * Creates a window listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsWindowOpenedListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void windowActivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosed(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowClosing(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeactivated(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowDeiconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowIconified(WindowEvent event)
        {
            // intentionally left blank
        }

        /**
         * {@inheritDoc}
         */
        public void windowOpened(WindowEvent event)
        {
            fire(event.getSource(), event);
        }

    }

    private static final ActionListener DEFAULT_ACTION_LISTENER = new EventsActionListener(null);
    private static final ItemListener DEFAULT_ITEM_LISTENER = new EventsItemListener(null);
    private static final FocusListener DEFAULT_FOCUS_GAINED_LISTENER = new EventsFocusGainedListener(null);
    private static final FocusListener DEFAULT_FOCUS_LOST_LISTENER = new EventsFocusLostListener(null);
    private static final WindowListener DEFAULT_WINDOW_ACTIVATED_LISTENER = new EventsWindowActivatedListener(null);
    private static final WindowListener DEFAULT_WINDOW_CLOSED_LISTENER = new EventsWindowClosedListener(null);
    private static final WindowListener DEFAULT_WINDOW_CLOSING_LISTENER = new EventsWindowClosingListener(null);
    private static final WindowListener DEFAULT_WINDOW_DEACTIVATED_LISTENER = new EventsWindowDeactivatedListener(null);
    private static final WindowListener DEFAULT_WINDOW_DEICONIFIED_LISTENER = new EventsWindowDeiconifiedListener(null);
    private static final WindowListener DEFAULT_WINDOW_ICONIFIED_LISTENER = new EventsWindowIconifiedListener(null);
    private static final WindowListener DEFAULT_WINDOW_OPENED_LISTENER = new EventsWindowOpenedListener(null);

    /**
     * Returns an action listener, that fires an event using the source of the event as producer and the action command
     * as tag.
     * 
     * @return the action listener
     */
    public static ActionListener fireOnAction()
    {
        return DEFAULT_ACTION_LISTENER;
    }

    /**
     * Creates an action listener, that fires an event using the specified producer and the specified tags. If the
     * producer is null, the source of the event will be used as producer. If the tags are null or empty, the action
     * command will be used as tag.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the action listener
     */
    public static ActionListener fireOnAction(Object producer, String... tags)
    {
        return new EventsActionListener(producer, tags);
    }

    /**
     * Returns an item listener, that fires an event using the source of the event as producer.
     * 
     * @return the item listener
     */
    public static ItemListener fireOnItemChanged()
    {
        return DEFAULT_ITEM_LISTENER;
    }

    /**
     * Creates an item listener, that fires an event using the specified producer and the specified tags. If the
     * producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the item listener
     */
    public static ItemListener fireOnItemChanged(Object producer, String... tags)
    {
        return new EventsItemListener(producer, tags);
    }

    /**
     * Returns a focus listener, that fires an event on focus gain using the source of the event as producer.
     * 
     * @return the focus listener
     */
    public static FocusListener fireOnFocusGained()
    {
        return DEFAULT_FOCUS_GAINED_LISTENER;
    }

    /**
     * Creates a focus listener, that fires an event on focus gain using the specified producer and the specified tags.
     * If the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the focus listener
     */
    public static FocusListener fireOnFocusGained(Object producer, String... tags)
    {
        return new EventsFocusGainedListener(producer, tags);
    }

    /**
     * Returns a focus listener, that fires an event on focus lost using the source of the event as producer.
     * 
     * @return the focus listener
     */
    public static FocusListener fireOnFocusLost()
    {
        return DEFAULT_FOCUS_LOST_LISTENER;
    }

    /**
     * Creates a focus listener, that fires an event on focus lost using the specified producer and the specified tags.
     * If the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the focus listener
     */
    public static FocusListener fireOnFocusLost(Object producer, String... tags)
    {
        return new EventsFocusLostListener(producer, tags);
    }

    /**
     * Returns a window listener, that fires an event on window activated using the source of the event as producer.
     * 
     * @return the focus listener
     */
    public static WindowListener fireOnWindowActivated()
    {
        return DEFAULT_WINDOW_ACTIVATED_LISTENER;
    }

    /**
     * Creates a window listener, that fires an event on window activated using the specified producer and the specified
     * tags. If the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the focus listener
     */
    public static WindowListener fireOnWindowActivated(Object producer, String... tags)
    {
        return new EventsWindowActivatedListener(producer, tags);
    }

    /**
     * Returns a window listener, that fires an event on window closed using the source of the event as producer.
     * 
     * @return the focus listener
     */
    public static WindowListener fireOnWindowClosed()
    {
        return DEFAULT_WINDOW_CLOSED_LISTENER;
    }

    /**
     * Creates a window listener, that fires an event on window closed using the specified producer and the specified
     * tags. If the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the focus listener
     */
    public static WindowListener fireOnWindowClosed(Object producer, String... tags)
    {
        return new EventsWindowClosedListener(producer, tags);
    }

    /**
     * Returns a window listener, that fires an event on window closing using the source of the event as producer.
     * 
     * @return the focus listener
     */
    public static WindowListener fireOnWindowClosing()
    {
        return DEFAULT_WINDOW_CLOSING_LISTENER;
    }

    /**
     * Creates a window listener, that fires an event on window closing using the specified producer and the specified
     * tags. If the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the focus listener
     */
    public static WindowListener fireOnWindowClosing(Object producer, String... tags)
    {
        return new EventsWindowClosingListener(producer, tags);
    }

    /**
     * Returns a window listener, that fires an event on window deactivated using the source of the event as producer.
     * 
     * @return the focus listener
     */
    public static WindowListener fireOnWindowDeactivated()
    {
        return DEFAULT_WINDOW_DEACTIVATED_LISTENER;
    }

    /**
     * Creates a window listener, that fires an event on window deactivated using the specified producer and the
     * specified tags. If the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the focus listener
     */
    public static WindowListener fireOnWindowDeactivated(Object producer, String... tags)
    {
        return new EventsWindowDeactivatedListener(producer, tags);
    }

    /**
     * Returns a window listener, that fires an event on window deiconified using the source of the event as producer.
     * 
     * @return the focus listener
     */
    public static WindowListener fireOnWindowDeiconified()
    {
        return DEFAULT_WINDOW_DEICONIFIED_LISTENER;
    }

    /**
     * Creates a window listener, that fires an event on window deiconified using the specified producer and the
     * specified tags. If the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the focus listener
     */
    public static WindowListener fireOnWindowDeiconified(Object producer, String... tags)
    {
        return new EventsWindowDeiconifiedListener(producer, tags);
    }

    /**
     * Returns a window listener, that fires an event on window iconified using the source of the event as producer.
     * 
     * @return the focus listener
     */
    public static WindowListener fireOnWindowIconified()
    {
        return DEFAULT_WINDOW_ICONIFIED_LISTENER;
    }

    /**
     * Creates a window listener, that fires an event on window iconified using the specified producer and the specified
     * tags. If the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the focus listener
     */
    public static WindowListener fireOnWindowIconified(Object producer, String... tags)
    {
        return new EventsWindowIconifiedListener(producer, tags);
    }

    /**
     * Returns a window listener, that fires an event on window opened using the source of the event as producer.
     * 
     * @return the focus listener
     */
    public static WindowListener fireOnWindowOpened()
    {
        return DEFAULT_WINDOW_OPENED_LISTENER;
    }

    /**
     * Creates a window listener, that fires an event on window opened using the specified producer and the specified
     * tags. If the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the focus listener
     */
    public static WindowListener fireOnWindowOpened(Object producer, String... tags)
    {
        return new EventsWindowOpenedListener(producer, tags);
    }

}
