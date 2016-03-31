# events-on-fire usage

[Back to Overview](http://code.google.com/p/events-on-fire/wiki/Overview)

# Usage #

## Include the library ##

### Include events-on-fire manually ###

Download the latest release:
  * [events-on-fire-1.2.2-dist.tar.gz](http://code.google.com/p/events-on-fire/downloads/detail?name=events-on-fire-1.2.2-dist.tar.gz&can=2&q=), distribution, contains jar, sources and java-doc, tar.gz format
  * [events-on-fire-1.2.2-dist.zip](http://code.google.com/p/events-on-fire/downloads/detail?name=events-on-fire-1.2.2-dist.zip&can=2&q=), distribution, contains jar, sources and java-doc, zip format
  * [events-on-fire-1.2.2-complete.jar](http://code.google.com/p/events-on-fire/downloads/detail?name=events-on-fire-1.2.2-complete.jar&can=2&q=), just the jar, but contains the sources

Add the jar manually to your build and execution path.

### Include events-on-fire via Maven ###

Add the following dependency:

```
<dependency>
  <groupId>com.google.code.events-on-fire</groupId>
  <artifactId>events-on-fire</artifactId>
  <version>1.2.2</version>
</dependency>
```

## Basic example ##

You can use any object as event. Usually you create a class that holds all the stuff you need for handling the event, but it may be enough to use a String.

### Create a consumer (your listener) ###

Add a method for the event handler to your class. Tag it with the @EventHandler annotation:

```
public class MyClass {
  ...
  @EventHandler
  public void onEvent(String event) {
    // handle the event
    ...
  }
  ...
}
```

There are more variations of signatures of the event handler method. Check EventHandlerVariations for more...

### Bind the consumer to a producer ###

```
Events.bind(producer, consumer);
```

The producer usually is your controller. In Swing this may be your Frame or a Component. In Web-Applications this may be the instance of a service.

The consumer is the class with the event handler. Keep in mind, that you have to handle the instance of the consumer. The references in the Events class are weak. If there is no reference left, the consumer will get cleaned up by the garbage collector.

### Fire the event ###

```
Events.fire(producer, "some event");
```

Immediately all the event handlers of the consumers, which are bonded to the producer will get called. The consumer will only get called, if it has a event handler method that fits the event.