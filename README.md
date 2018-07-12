# DaniHttp
[![Build Status](https://travis-ci.org/dani909/DaniHttp.svg?branch=master)](https://travis-ci.org/dani909/DaniHttp)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.dani09/dani-http/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.dani09/dani-http)
[![JCenter](https://api.bintray.com/packages/dani09/DaniHttp/DaniHttp/images/download.svg) ](https://bintray.com/dani09/DaniHttp/DaniHttp/_latestVersion)

## Installation

### Maven
First add JCenter to your pom

```xml
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>
```

and then the Dependency

```xml
<dependencies>
    <dependency>
        <groupId>de.dani09</groupId>
        <artifactId>dani-http</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

### Gradle

First add JCenter

```groovy
repositories {  
   jcenter()  
}
```

and then the Dependency

```groovy
dependencies {
    compile 'de.dani09:dani-http:0.1.0'
}
```

If you are using Gradle with the Kotlin DSL you will have to use this instead

```groovy
dependencies {
    compile("de.dani09:dani-http:0.1.0")
}
```

### sbt

First activate JCenter
```sbtshell
useJCenter := true
```

and then the Dependency

```sbtshell
libraryDependencies += "de.dani09" % "dani-http" % "0.1.0"
```

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
