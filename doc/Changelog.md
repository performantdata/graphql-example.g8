# Changelog
## v0.2.0, 2024-xx-xx
### project
- Add this Changelog file. Start using annotated tags.
- Switch to a CC-BY license.

- Rename the project from `sangria-example.g8` to `graphql-example.g8`,
  to reflect the change from a Sangria implementation to a general,
  best-in-class, GraphQL implementation (currently Caliban).

- Update the Giter8 plugin from 0.13.1 to 0.16.2. Use `sbt-scripted` to
  create a Giter8 test.

- Update sbt from 1.5.5 to 1.9.8.

- Drop the notion of HTTP implementation options.  Replace Akka HTTP
  with http4s.

### generated project
- Add a `static` subproject for static site generation.
  Use standard front-end tools, like `npm` and Vite, for incorporating JavaScript libraries into the GraphiQL page.

- Switch to using `sbt-git` for versioning (the generated project).
- Update sbt from 1.5.5 to 1.9.9.

- Extract to `project/Versions.scala` library version numbers that are
  shared among subprojects.

- Target Java 17, instead of Java 11.
- Target Scala 3.4.0, instead of 2.13.11.
- Upgrade `sbt-native-packager` from 1.9.4 to 1.9.16.
- Upgrade GraphiQL from 3.0.10 to 3.1.1.
- Upgrade H2 from 1.4.200 to 2.1.214.
- Upgrade Jackson from 2.12.5 to 2.14.2.
- Upgrade Liquibase from 4.4.3 to 4.27.0.
- Upgrade Log4J from 2.14.1 to 2.20.0.
- Upgrade Pureconfig from 0.16.0 to 0.17.6.
- Upgrade Scala-logging from 3.9.4 to 3.9.5.
- Upgrade Slick from 3.3.3 to 3.5.0.

### generated Docker image
- Use a Debian slim image as the base to the Docker image.  This makes
  for a smaller (75MB) image than when using the
  `eclipse-temurin:16-focal` (485MB) one.  This is coupled with the use
  of `jlink` to make something closer to a minimal image.

- Use the default user for the Debian image (`demiourgos728`) instead
  of the `daemon` user.

- Suppress creation of the Windows `.bat` scripts in the Docker image.
- Set the `LANG` environment variable in the image to `C.UTF-8`.
- Add some common image LABELs.
