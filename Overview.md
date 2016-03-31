# Overview #

## Index ##

  * [Usage](http://code.google.com/p/events-on-fire/wiki/Usage)

## Features ##

  * **Asynchronous event handling** - all events will be handled by a dedicated thread.
  * **Automatic cleanup** - there is no need to unbind consumers. All links between producers and consumers will be cleaned by the garbage collector.
  * **No configuration needed** - everything works out of the box. Just bind and fire.
  * **Multiple handleEvents methods** - a class may contain many handleEvents methods, the right one is called at runtime
  * **Firing of events can be disabled per-thread**
  * **High-performance** - designed with performance in mind. Speed up you applications by spreading the work over multiple threads.

## Scenarios ##

  * **Swing** - fire messages across your application without the need of implementing the listener pattern ever again. Plus, your GUI will keep responsive without the need of implementing a SwingWorker.
  * **Web-Applications** - shift work to the background, without blocking the request.
  * **Business Logic** - Break your business logic into many small parts and communicate by asynchronous events.