# Raviolini Framework

Raviolini is a lightweight framework for building CRUD RESTful APIs in Java 8.

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
