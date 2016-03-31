# events-on-fire #

The repeated implementation of the listener pattern for Java is a boring task which cannot be easily abstracted. Events-On-Fire is a way to get rid of this. Events-On-Fire offers a simple mechanism to fire events across your application without the need for any configuration and without the danger of memory leaks.

## A simple example ##

Imagine, you have some sort of mailbox that occasionally tends to receive mails and some narrators that like to read these mails.
```
Object mailbox = new MyMailbox();      // my producer
Object narrator1 = new MyNarrator();   // one of my consumers
Object narrator2 = new MyNarrator();   // another one of my consumers
```

First, implement an event handler in `MyNarrator`:
```
@EventHandler
public void handleEvent(Mail someMail) {
  // do fancy stuff here
}
```

Then, bind the narrators to the mailbox:
```
Events.bind(mailbox, narrator1);
Events.bind(mailbox, narrator2);
```

If the mailbox receives a mail, it simply fires it:
```
Events.fire(mailbox, someMail);
```

And all the narrators get notified.

There is no need to unbind consumers or producers. References will be cleared automatically by the garbage collector.

Need to bind to all mailboxes? Bind to a class and you get events of all producers:
```
Events.bind(MyMailbox.class, narrator1);
```

Check the [Usage Page](http://code.google.com/p/events-on-fire/wiki/Usage) for more details.

## Some more examples ##

### Event Logger ###

Do you want to log all events? Bind to the Object.class:
```
@EventHandler
public void logEvents(Object producer, Object event) {
  // any event goes here
}

Events.bind(Object.class, myLogger);
```

### Swing Event Thread ###

Should the event be called by `SwingEventThread`? By the action with the command "ok"?
```
@SwingEventHandler(event = ActionEvent.class, eachTag = "ok")
public void onOk() {
  // do something here
}

Events.bind(okButton, this);
```

### Pooled Event Handler ###

A lot of work that should not block your app? There is a thread pool, that can handle this:
```
@PooledEventHandler
public void search(SearchEvent e) {
  // do something that takes longer
}
```
