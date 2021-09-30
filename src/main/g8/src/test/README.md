# $name$ tests

The tests are run with [ScalaTest][scalatest],
and currently with [Akka HTTP's TestKit][akka-http-testkit].

## database setup

Tests of the service necessarily run against a database.
We use H2, with a local file.
Since we want to utilize a new, empty directory for that file, in order to avoid accidental file clashes,
we create a temporary directory
Since the configuration of the JDBC URI needs to happen in the Typesafe Config files,

[scalatest]: https://www.scalatest.org/
[akka-http-testkit]: https://doc.akka.io/docs/akka-http/current/routing-dsl/testkit.html
