# DaniHttp
[![Build Status](https://travis-ci.org/dani909/DaniHttp.svg?branch=master)](https://travis-ci.org/dani909/DaniHttp)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.dani09/dani-http/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.dani09/dani-http)
[![JCenter](https://api.bintray.com/packages/dani09/DaniHttp/DaniHttp/images/download.svg) ](https://bintray.com/dani09/DaniHttp/DaniHttp/_latestVersion)

## Installation

Replace X.Y.Z with the Version you want to use

### Maven

Add this dependency to your Pom

```xml
<dependencies>
    <dependency>
        <groupId>de.dani09</groupId>
        <artifactId>dani-http</artifactId>
        <version>X.Y.Z</version>
    </dependency>
</dependencies>
```

### Gradle

Add this dependency to your build.gradle

```groovy
dependencies {
    compile 'de.dani09:dani-http:X.Y.Z'
}
```

If you are using Gradle with the Kotlin DSL you will have to add this 
to your build.gradle.kts instead

```groovy
dependencies {
    compile("de.dani09:dani-http:X.Y.Z")
}
```

### sbt

Add this dependency to your build.sbt

```sbtshell
libraryDependencies += "de.dani09" % "dani-http" % "X.Y.Z"
```

## Documentation

Javadoc and KDoc can be found at [dani909.github.io/DaniHttp](https://dani909.github.io/DaniHttp)

## Usage
First import the Library

```java
import de.dani09.http;
```

and then see where your IDE's Autocomplete feature will bring you.

An Example:

```java
class HttpExample {
    public static void main(String[] args) {
        HttpResponse response = Http.get("http://example.com")
                .addRequestHeader("Some-Header", "SomeValue")
                .execute();
        
        System.out.println(response.getResponseCode());
        System.out.println(response.getResponseHeader("Some-Response-Header"));
        System.out.println(response.getResponseString());
    }
}
```
