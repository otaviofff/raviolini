# Raviolini Framework

Raviolini is a lightweight framework for building CRUD RESTful APIs in Java 8.

## Usage

First, you need to code the resource you want to expose through your API. In this sample, our domain object is named `Dog`, and is defined by three simple attributes, namely `id`, `name` and `neutered`. Please note the `@JsonIgnore` annotation from [Jackson](https://github.com/FasterXML/jackson), as well as the `@DatabaseTable` and `@DatabaseField` annotations from [ORM Lite](https://github.com/j256/ormlite-core).

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

Second, you need to code a front controller for your API. It will just route the incoming HTTP request, based on the domain object you coded in the previous step.

```java
package org.muttie.api;

import org.muttie.domain.Dog;
import org.raviolini.api.RequestRouter;

public class FrontController {
    public static void main(String[] args) {
        RequestRouter<Dog> router = new RequestRouter<>();
        
        router.route(Dog.class);
    }
}
```

This is it. Your RESTful API is done. Enjoy your day =)

## Aspects

By default, Raviolini addresses six major non-functional requirements for your API, namely caching, configuration, logging, persistence, serialization, and validation. And this doesn't come at the expense of flexibility at all. You may still configure each one of these aspects by swapping out their drivers. For example, you can define whether caching will be powered by Redis or Memcached, whether persistence will be powered by PostgreSQL or MongoDB, whether serialization will output JSON or XML, and whether logging will output to File or Memory.

Many other drivers are available though. Check them out below.

### Caching
- **Package**: [org.raviolini.aspects.data.caching](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/data/caching)
- **Drivers**: [Null](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/data/caching/drivers/NullCacheDriver.java), [Redis](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/data/caching/drivers/RedisCacheDriver.java), Memcached

### Configuration
- **Package**: [org.raviolini.aspects.io.configuration](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/io/configuration)

### Logging
- **Package**: [org.raviolini.aspects.io.logging](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/io/logging)
- **Drivers**: [DatedFile](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/io/logging/drivers/DatedFileHandler.java), [java.util.logging.Handler](https://docs.oracle.com/javase/8/docs/api/java/util/logging/Handler.html) (e.g. [Console](https://docs.oracle.com/javase/8/docs/api/java/util/logging/ConsoleHandler.html), [File](https://docs.oracle.com/javase/8/docs/api/java/util/logging/FileHandler.html), [Memory](https://docs.oracle.com/javase/8/docs/api/java/util/logging/MemoryHandler.html), [Socket](https://docs.oracle.com/javase/8/docs/api/java/util/logging/SocketHandler.html), [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/logging/StreamHandler.html))

### Persistence
- **Package**: [org.raviolini.aspects.data.database](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/data/database)
- **Drivers**: [Relational](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/data/database/drivers/RelationalDatabaseDriver.java) (e.g. PostgreSQL, MySQL), MongoDB

### Serialization
- **Package**: [org.raviolini.aspects.io.serialization](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/io/serialization)
- **Drivers**: [JSON](https://github.com/otaviofff/raviolini/blob/master/src/main/java/org/raviolini/aspects/io/serialization/drivers/JsonSerializationDriver.java), XML

### Validation
- **Package**: [org.raviolini.aspects.data.validation](https://github.com/otaviofff/raviolini/tree/master/src/main/java/org/raviolini/aspects/data/validation)
