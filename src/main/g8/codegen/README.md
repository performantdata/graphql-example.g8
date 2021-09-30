# customized Slick code generation

This subproject compiles a custom Scala class that's used for Slick code generation in the base project.
It addresses a few shortcomings in the standard code generator:

- It works around a Scala 2.13 order-of-initialization runtime issue
  that comes from the early initialization in the generated Slick 3.3.3 code.
  Early initialization has further been removed in Scala 3.

- It creates per-table value classes to represent the single-column, primary key identifiers
  that are commonly used in database design.
  This eliminates the problem of passing the wrong identifier in Scala code,
  which can happen because the identifiers are otherwise usually of all the same integer or string type.
