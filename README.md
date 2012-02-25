WOInject
========

WOInject is a dependency injection framework for WebObjects. It extends
the powerful Google Guice framework and adds capabilities specific for
WO applications. Dependency injection is a useful technique for program
modularization leading to more testable code. WOInject allows you to write
better APIs and decoupled code reducing the hurdle of wiring things
together.

**Version**: 1.0-SNAPSHOT

Requirements
------------

* [Guice](http://code.google.com/p/google-guice/) 3.0
* [Project Wonder](http://wiki.objectstyle.org/confluence/display/WONDER/Home) 5
* WebObjects 5

Features
--------

* **Minimal configuration**: initialize your application using the WOInject
classes and the application is ready for dependency injection. No configuration
of VM agents, XML or properties is required.
* **Deep integration**: delegate the creation of objects like components,
enterprise objects, sessions and direct actions to Guice. Take advantage of
dependency injection and AOP support without inheriting any special classes.
* **Special scopes**: provide scopes to inject objects per session
(@WOSessionScoped) or request (@WORequestScoped).
* **Type safe**: make use of Java 5 features and Guice approach to provide
type safe binding configuration.


Installation
------------

Maven users have to add the dependency declaration:

	<dependency>
		<groupId>com.woinject</groupId>
		<artifactId>woinject</artifactId>
		<version>1.0-SNAPSHOT</version>
	</dependency>

Non Maven users have to:

1. Download the woinject.jar.
2. Add the woinject library to the build path.

Usage
-----

	package my.package;

	import com.google.inject.Module;
	import com.woinject.InjectableApplication;
	import com.woinject.WOInject;

	public class Application extends InjectableApplication {
		public static void main(String[] args) {
			WOInject.main(args, "my.package.Application");
		}

		@Override
		protected Module[] modules() {
			return new Module[] { new MyModule() };
		}
	}

About
-----

* **Site**: http://hprange.github.com/woinject
* **E-mail**: hprange at gmail.com
* **Twitter**: @hprange