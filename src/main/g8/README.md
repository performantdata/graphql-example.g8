# $name$

This project creates a [GraphQL][GraphQL]-based service that provides access to a user database,
of the type that one might find as the basis of any authorization subsystem.

## running the service
### local Docker

One can publish a local Docker image in the usual way (that [`sbt-native-packager`][sbt-native-packager] uses):
```shell
sbt docker:publishLocal
```
The server listens on port 8080. Deploy it in the usual way:
```shell
docker run -p 8080:8080 $name;format="normalize"$
```
Using the above port number, http://localhost:8080/graphiql should return the GraphiQL UI when viewed from a browser.

## documentation

API documentation of the included classes is available in the usual way by running:
```shell
sbt doc
```

## implementation

Besides GraphQL, the technologies used to implement the service include:

- [Scala][Scala], a mixed-paradigm, object-oriented and functional programming language
  with an expressive type syntax and compile-time type checking

A collection of Scala libraries that use functional programming with effects:
- [Caliban][Caliban], for implementing a GraphQL server and client
- [http4s][http4s], for implementing HTTP server interfaces


- [GraphiQL][graphiql], a Web-based UI for testing

- [Liquibase][liquibase], for database schema generation and versioning.
  (This only works for JDBC-compatible databases.)
  There is [a change set file](src/main/resources/liquibase.xml) that can be modified by the user.

- [PureConfig][pureconfig], for software configuration.
- [Scala Logging][scala-logging], for logging API.
- [Log4J2][log4j2] with [YAML configuration](src/main/resources/log4j2.yaml), for logging backend.
- Docker image generation and publication

There are also points of variance, where a particular technology can be selected from those available:

- The database may be one of:
  - [H2][h2]

Selection can be made in `build.sbt` using the available setting keys, e.g.:
```sbt
import SangriaExample._
database := H2
```
See the [`SangriaExample` class](project/SangriaExample.scala) for the available settings.

## updating the project

This project is generated from [a template][template].
The code in [`template.sbt`](template.sbt) and [the `sangria` directory](src/main/scala/$package;format="package-dir"$/sangria)
should not need to be changed by users of the template.
If that's _not_ true, point that out to the maintainers.

As such, one should be able to replace these files with those generated by a future version of the template
that's been given the same parameter values.
(This isn't a guarantee, but it is a secondary goal of the template project.)

## license

See [docs/template-license.md](docs/template-license.md).

[Caliban]: https://ghostdogpr.github.io/caliban/
[gitter]: https://gitter.im/sangria-graphql/sangria
[graphiql]: https://github.com/graphql/graphiql#readme
[GraphQL]: https://graphql.org/
[h2]: https://h2database.com/
[http4s]: https://http4s.org/
[liquibase]: https://www.liquibase.org/
[log4j2]: https://logging.apache.org/log4j/2.x/
[pureconfig]: https://pureconfig.github.io/
[sbt-native-packager]: https://www.scala-sbt.org/sbt-native-packager/
[Scala]: https://www.scala-lang.org/
[scala-logging]: https://github.com/lightbend/scala-logging
[template]: https://github.com/performantdata/graphql-example.g8
