# Introduction #

In events-on-fire the event handling is based on an annotations instead of an interface. This allows different types of method signatures.

# Annotations #

Currently events-on-fire supports three different annotations:
  * **`@EventHandler`**, the default one, the handler is called by the global event handler thread
  * **`@PooledEventHandler`**, when triggered, the handler is executed by a thread pool
  * **`@SwingEventHandler`**, calls the handler using the Swing event thread (`SwingUtilities.invokeLater(...)`)

All these annotations support following parameters:
  * **producer**: one or more classes of supported producers. Usually optional, but a consumer may be bound to multiple producers.
  * **event**: one or more classes of supported events. Usually optional, because this is defined by the events-parameter of the method.
  * **eachTag**: optional, each of the specified tags must be present when the fire method was called
  * **anyTag**: optional, at least one of the specified tags must be present when the fire method was called

# Methods #

...TBD...

```
@EventHandler(event = MyEvent.class)
public void eventHandler() {
  ...
}
```



```
@EventHandler(event = MyEvent.class)
public void eventHandler(String... tags) {
  ...
}
```


```
@EventHandler
public void eventHandler(MyEvent event) {
  ...
}
```

```
@EventHandler
public void eventHandler(MyEvent event, String... tags) {
  ...
}
```


```
@EventHandler
public void eventHandler(MyProducer producer, MyEvent event) {
  ...
}
```


```
@EventHandler
public void eventHandler(MyProducer producer, MyEvent event, String... tags) {
  ...
}
```