[![Build Status](https://travis-ci.org/hprange/woinject.svg?branch=master)](https://travis-ci.org/hprange/woinject)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

WOInject
========

WOInject is an extension of the Google Guice framework created to enable the use of dependency injection with WebObjects applications and frameworks. Dependency injection (DI) is a useful technique for program modularization leading to more testable code. WOInject allows to write better APIs and decoupled code reducing the hurdle of wiring things together.

**Version**: 1.4.1

Requirements
------------

* [Guice](http://code.google.com/p/google-guice/) 5.0.1
* [ByteBuddy](https://bytebuddy.net) 1.10.20
* [Project Wonder](http://wiki.objectstyle.org/confluence/display/WONDER/Home) 5
* WebObjects 5

Features
--------

* **Minimal configuration**: no configuration of VM agents, XML or properties is required. Initialize your application using the WOInject classes and the application is ready for dependency injection.
* **Deep integration**: take advantage of dependency injection and Guice's AOP support without inheriting any special classes.
* **Special scopes**: provide scopes to inject objects per session or request.
* **Type safe**: make use of Java 5 features and Guice approach to provide type safe binding configuration.


Installation
------------

Maven users have to add the dependency declaration:

```xml
<dependency>
	<groupId>com.woinject</groupId>
	<artifactId>woinject</artifactId>
	<version>1.4.1</version>
</dependency>
```

Non Maven users have to:

1. Download the woinject-1.4.1-bin.zip package.
2. Add the woinject.jar and the other required libraries to the build path.

Usage
-----

ApplicationRunner.java

```java
package my.app;

import com.woinject.WOInject;

public class ApplicationRunner {
	public static void main(String[] args) {
		WOInject.init("my.app.Application", args);
	}
}
```

Application.java

```java
package my.app;

import com.google.inject.Module;
import com.woinject.InjectableApplication;

public class Application extends InjectableApplication {
	@Override
	protected Module[] modules() {
		return new Module[] { new MyModule() };
	}
}
```

About
-----

* **Site**: http://hprange.github.com/woinject
* **E-mail**: hprange at gmail.com
* **Twitter**: @hprange