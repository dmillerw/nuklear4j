
# Nuklear4j

Nuklear4j uses [SWIG 3](http://www.swig.org) to generate the Java bindings for the [Nuklear library](https://github.com/vurtun/nuklear). 

## Features
- Immediate mode graphical user interface toolkit
- Fully skinnable and customizable
- The Java graphics backend uses AWT currently
- Works with Java 5 or newer (for older versions, just remove annotations in the source code) 

## Building

```
apt-get install swig3.0 
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
make
```

## Test

```
make test
```

## Gallery
![screenshot](https://raw.githubusercontent.com/glegris/nuklear4j/master/resources/nuklear_snapshot-19042018.png)

## Credits
Nuklear is developed by Micha Mettke and every direct or indirect contributor to the GitHub.

Nuklear4j is developed by Guillaume Legris

## License
This software is under the MIT license.

## Original description
This is a minimal state immediate mode graphical user interface toolkit
written in ANSI C and licensed under public domain. It was designed as a simple
embeddable user interface for application and does not have any dependencies,
a default renderbackend or OS window and input handling but instead provides a very modular
library approach by using simple input state for input and draw
commands describing primitive shapes as output. So instead of providing a
layered library that tries to abstract over a number of platform and
render backends it only focuses on the actual UI.

