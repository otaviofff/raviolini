# Raviolini Framework
[![](https://jitpack.io/v/otaviofff/raviolini.svg)](https://jitpack.io/#otaviofff/raviolini)

Raviolini is a lightweight framework for building RESTful APIs, in Java 8. You simply provide your domain object and, out of the box, Raviolini gives you authentication, caching, configuration, logging, persistence, serialization and validation on that object. Moreover, pre- and post-execution hooks wil let you extend Raviolini beyond the original scope of CRUD (Create, Read, Update, Delete) operations.

Raviolini is built on top of [Spark](https://github.com/perwendel/spark) (version 2.2) and [ORM Lite](https://github.com/j256/ormlite-core) (version 4.48). The former provides a lightweight HTTP foundation, while the latter provides both a database abstraction layer and object-relational mapping.

## Install

There are only two simple steps for you to install Raviolini in your project.

First, add the [JitPack](https://jitpack.io/) repository to your build file (pom.xml):

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Second, add the dependency:

```xml
<dependencies>
    <dependency>
        <groupId>com.github.otaviofff</groupId>
        <artifactId>raviolini</artifactId>
        <version>0.0.11</version>
    </dependency>
</dependencies>
```

The above code assumes you use [Maven](https://github.com/apache/maven) to build your project. However, if you use [Gradle](https://github.com/gradle/gradle), [Sbt](https://github.com/sbt/sbt) or [Leiningen](https://github.com/technomancy/leiningen), then you should grab your code from [Raviolini on JitPack](https://jitpack.io/#otaviofff/raviolini/).

## Use

First, you need to code the resource you want to expose through your API. In this sample, our domain object is named `Dog`, and is defined by three simple attributes, namely `id`, `name` and `neutered`. Please note the `@JsonIgnore` annotation from [Jackson](https://github.com/FasterXML/jackson), as well as the `@DatabaseTable` and `@DatabaseField` annotations from [ORM Lite](https://github.com/j256/ormlite-core). Also note that `Dog` must implement the `Entity` interface, provided by [Raviolini](https://github.com/otaviofff/raviolini) itself. 

```java
package org.muttie.domain;

import org.raviolini.domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "dog")
public class Dog implements Entity {
    
    @DatabaseField(generatedId = true)
    private Integer id;
    
    @DatabaseField
    private String name;
    
    @DatabaseField
    private Boolean neutered;
    
    public Dog() {}
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @JsonIgnore
    @Override
    public Boolean isValid() {
        return (neutered != null && name != null && !name.isEmpty());
    }
}
```

Second, you need to code a front controller for your API. It will just make Raviolini listen to the HTTP port assinged, and add an HTTP router for the domain object you coded in the previous step. This router defines all valid URIs that will compose your final RESTful interface. Note that your `FrontController` has to extend `AbstractController`.

```java
package org.muttie.api;

import org.muttie.domain.Dog;
import org.raviolini.api.AbstractController;

public class FrontController extends AbstractController {

    public static void main(String[] args) {
        listenToAssignedPort();
        addRouter(Dog.class);
    }
}
```

Finally, you just need to create a configuration file (named ```application.properties```) in order to setup your application, database, cache, and authentication strategy. Optionally, you may also create another config file (named ```logging.properties```) to setup you logging preferences. Raviolini comes with [sample config files](https://github.com/otaviofff/raviolini/tree/master/src/main/resources) to help you out.

```properties
####################
#  Application
####################

application.name    = ${pom.name}
application.version = ${pom.version}

####################
#  Database
####################

raviolini.database.driver = relational
raviolini.database.engine = postgresql
raviolini.database.host   = localhost
raviolini.database.port   = 15432
raviolini.database.name   = db_name
raviolini.database.user   = db_user
raviolini.database.pass   = db_pass

####################
#  Cache
####################

raviolini.cache.driver = redis
raviolini.cache.host   = localhost
raviolini.cache.port   = 16379
raviolini.cache.pass   = cache_pass

####################
#  Auth
####################

raviolini.auth.driver  = basic
raviolini.auth.user    = api_user
raviolini.auth.pass    = api_pass
raviolini.auth.methods = GET,POST,PUT
```

The full sample application can be found at repository [raviolini-sample](https://github.com/otaviofff/raviolini-sample).

This is it. Your RESTful API is done. Enjoy your day =)

## Execute

### Create Object

Request:
```
POST /dog HTTP/1.1
Host: localhost:4567
Content-Type: application/json

{
    "name": "Madalena",
    "neutered": true
}
```

Response:
```
HTTP/1.1 201 Created
Date: Thu, 05 Nov 2015 12:17:48 GMT
Content-Type: text/plain

```
### Read Object

Request:
```
GET /dog/1 HTTP/1.1
Host: localhost:4567
Accept: application/json

```

Response:
```
HTTP/1.1 200 OK
Date: Thu, 05 Nov 2015 12:18:01 GMT
Content-Type: application/json

{
    "id": 1,
    "name": "Madalena",
    "neutered": true
}
```

### Update Object

Request:
```
PUT /dog/1 HTTP/1.1
Host: localhost:4567
Content-Type: application/json

{
    "id": 1,
    "name": "Madalena",
    "neutered": false
}
```

Response:
```
HTTP/1.1 200 OK
Date: Thu, 05 Nov 2015 12:18:01 GMT
Content-Type: text/plain

```

### Delete Object

Request:
```
DELETE /dog/1 HTTP/1.1
Host: localhost:4567

```

Response:
```
HTTP/1.1 200 OK
Date: Thu, 05 Nov 2015 12:18:01 GMT
Content-Type: text/plain

```

### Read List

Request:
```
GET /dog HTTP/1.1
Host: localhost:4567
Accept: application/json

```

Response:
```
HTTP/1.1 200 OK
Date: Thu, 05 Nov 2015 12:18:01 GMT
Content-Type: application/json

[
    {
        "id": 1,
        "name": "Madalena",
        "neutered": true
    },
    {
        "id": 2,
        "name": "Martin",
        "neutered": false
    }
]
```

## Learn

### Built-In Aspects

By default, Raviolini addresses seven major non-functional requirements for your API, namely authentication, caching, configuration, logging, persistence, serialization, and validation. And this doesn't come at the expense of flexibility at all. You may still configure each one of these aspects by swapping out their drivers. 

For example, you can define whether authentication will be based on Basic or Digest, whether caching will be powered by Redis or Memcached, whether persistence will be powered by PostgreSQL or MySQL, whether serialization will output JSON or XML, whether configuration will be read from File or Environment, and whether logging will output to File or Memory.

Many other drivers are available. Check them out below.

#### Authentication
- Package: [org.raviolini.aspects.security.auth](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/security/auth)
- Drivers: [Null](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/security/auth/drivers/NullAuthDriver.java), [Basic](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/security/auth/drivers/BasicAuthDriver.java), [Digest](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/security/auth/drivers/DigestAuthDriver.java)
- Learn more about [Authentication Handling](https://github.com/otaviofff/raviolini#authentication-handling)

#### Caching
- Package: [org.raviolini.aspects.data.caching](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/data/caching)
- Drivers: [Null](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/data/caching/drivers/NullCacheDriver.java), [Memcached](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/data/caching/drivers/MemcachedCacheDriver.java), [Redis](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/data/caching/drivers/RedisCacheDriver.java)

#### Configuration
- Package: [org.raviolini.aspects.io.configuration](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/io/configuration)
- Drivers: [Environment](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/io/configuration/drivers/EnvConfigDriver.java), [File](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/io/configuration/drivers/FileConfigDriver.java)

#### Logging
- Package: [org.raviolini.aspects.io.logging](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/io/logging)
- Drivers: [DatedFile](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/io/logging/drivers/DatedFileHandler.java), [java.util.logging.Handler](https://docs.oracle.com/javase/8/docs/api/java/util/logging/Handler.html) (e.g. [Console](https://docs.oracle.com/javase/8/docs/api/java/util/logging/ConsoleHandler.html), [File](https://docs.oracle.com/javase/8/docs/api/java/util/logging/FileHandler.html), [Memory](https://docs.oracle.com/javase/8/docs/api/java/util/logging/MemoryHandler.html), [Socket](https://docs.oracle.com/javase/8/docs/api/java/util/logging/SocketHandler.html), [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/logging/StreamHandler.html))

#### Persistence
- Package: [org.raviolini.aspects.data.database](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/data/database)
- Drivers: [Relational](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/data/database/drivers/RelationalDatabaseDriver.java) (e.g. PostgreSQL, MySQL)

#### Serialization
- Package: [org.raviolini.aspects.io.serialization](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/io/serialization)
- Drivers: [JSON](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/io/serialization/drivers/JsonSerializationDriver.java), [XML](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/io/serialization/drivers/XmlSerializationDriver.java)

#### Validation
- Package: [org.raviolini.aspects.data.validation](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/data/validation)

### Architecture
As depicted by the following UML diagram, Raviolini is composed of lightweight, loosely-coupled, cohesive packages, with no cycles in its dependency structure.  

![UML Package Diagram](https://dl.dropboxusercontent.com/u/111597/raviolini/packages-v2.png)

### Exception Handling

Raviolini embrances HTTP and its status codes.

| Code        | Message                | Description                                                                                                                    |
|-------------|------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| 400         | Bad Request            | API client sent an invalid HTTP request. This could be either a malformed URI, or an empty body for a POST/PUT request.        |
| 401         | Unauthorized           | API client didn't provide a valid access credential through HTTP header Authorization. A retry with a different credential will be welcomed.           |
| 403         | Forbidden              | API client won't have access to the targeted resource at all, regardless of its access credential. No retry should take place. |
| 404         | Not Found              | API client sent an HTTP request against a resource that doesn't exist on the server, at this point in time.                    |
| 405         | Method Not Allowed     | API client sent an HTTP request along with an unsupported HTTP verb. Raviolini handles GET, POST, PUT and DELETE only.         |
| 406         | Not Acceptable         | API client sent an HTTP GET request with an unsupported media-type on HTTP header Accept. Only JSON and XML are acceptable.    |
| 415         | Unsupported Media Type | API client sent an HTTP POST/PUT request with an unsupported media-type on HTTP header Content-Type. Only JSON and XML are.    |
| 500         | Internal Server Error  | API server experienced an exception with one of its components, such as Cache, Configuration, Database, or Log.                |


Less specific HTTP status codes, namely 400 (client-side error) and 500 (server-side error), should be interpreted along with the HTTP reponse message body, which will always contain more information about the exception thrown. This is usually an effective approach to troubleshooting your API, in addition to Raviolini logs, of course.

### Authentication Handling

Raviolini is a stateless engine, so are the authentication drivers it provides. Thus, Raviolini's ```Digest``` implmentation makes use of a signed timestamp ```nonce```, which is valid within the hour, as opposed to a one-off value that would have to be stored on the server. Hence, after this timestamp ```nonce``` expires, the client will get a 401 response message, with a new ```nonce``` on response header ```WWW-Authenticate```.  

As defined by [RFC2617](https://tools.ietf.org/html/rfc2617#section-3.2.1), the contents of the ```nonce``` are implementation dependent. The quality of the implementation depends on a good choice. And the strategy defined here is Raviolini's take on it. 

----------
Made in São Paulo, by [Otavio Ferreira](https://github.com/otaviofff/).

EOF
