# DaniHttp
[![Build Status](https://travis-ci.org/dani909/DaniHttp.svg?branch=master)](https://travis-ci.org/dani909/DaniHttp)

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
