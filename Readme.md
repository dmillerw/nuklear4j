[![Build Status](https://travis-ci.org/vurtun/nuklear.png)](https://travis-ci.org/vurtun/nuklear)
# Nuklear4j

Nuklear4j uses SWIG3 to generate the Java bindings. Even if this project focus on Java, it means bindings for other languages should be done easily (any volunteers ?)

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

## Building

```
apt-get install swig3.0 
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
make
```

## Gallery
![screenshot](https://cloud.githubusercontent.com/assets/8057201/11761525/ae06f0ca-a0c6-11e5-819d-5610b25f6ef4.gif)
![screen](https://cloud.githubusercontent.com/assets/8057201/13538240/acd96876-e249-11e5-9547-5ac0b19667a0.png)
![screen2](https://cloud.githubusercontent.com/assets/8057201/13538243/b04acd4c-e249-11e5-8fd2-ad7744a5b446.png)
![node](https://cloud.githubusercontent.com/assets/8057201/9976995/e81ac04a-5ef7-11e5-872b-acd54fbeee03.gif)
![skinning](https://cloud.githubusercontent.com/assets/8057201/14152357/25df939e-f6b3-11e5-8587-b19e863e0d1b.png)

##CREDITS:
Nuklear is developed by Micha Mettke and every direct or indirect contributor to the GitHub.
Nuklear4j is developed by Guillaume Legris

##LICENSE:
This software is under the MIT license.

