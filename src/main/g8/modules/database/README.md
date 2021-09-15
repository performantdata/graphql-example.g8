# User data service database module

This subproject provides the database interface for the user data service.
Here is where the database server type is chosen,
the database schema is managed via [Liquibase][liquibase],
and the corresponding Scala classes are generated via source code from [Slick code generation][slickgen].


[liquibase]: https://www.liquibase.org/
[slickgen]: https://scala-slick.org/doc/3.3.3/code-generation.html
