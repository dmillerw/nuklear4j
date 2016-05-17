
# Nuklear4j

Nuklear4j uses [SWIG 3](http://www.swig.org) to generate the Java bindings for the [Nuklear library](https://github.com/vurtun/nuklear). Even if this project focus on Java, it means bindings for other languages (Python, Lua, C#, ...) could be done easily.

##Original description
This is a minimal state immediate mode graphical user interface toolkit
written in ANSI C and licensed under public domain. It was designed as a simple
embeddable user interface for application and does not have any dependencies,
a default renderbackend or OS window and input handling but instead provides a very modular
library approach by using simple input state for input and draw
commands describing primitive shapes as output. So instead of providing a
layered library that tries to abstract over a number of platform and
render backends it only focuses on the actual UI.

## Features
- Immediate mode graphical user interface toolkit
- Fully skinnable and customizable
- The Java graphics backend uses AWT currently
- Works with Java 1.1 or newer

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
![screenshot](https://raw.githubusercontent.com/glegris/nuklear4j/resources/nuklear4j%20-%2013052016.png)

##CREDITS:
Nuklear is developed by Micha Mettke and every direct or indirect contributor to the GitHub.

Nuklear4j is developed by Guillaume Legris

##LICENSE:
This software is under the MIT license.

