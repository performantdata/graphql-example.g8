# Static site generation subproject

Much of the user interface can be generated statically using a [Jamstack][Jamstack] approach.
This subproject contains the HTML and CSS files that comprise those static resources.

## directory structure

Although this isn't an [sbt][sbt] project,
the directory structure follows [that of a typical <code>sbt-web</code> project][sbt-web-layout]:
the template sources are located in `src/main/assets/`
and the static Web output is generated to `target/web/public/main/`.
See the link for details.
Web project configuration files—like `package.json` and `.eleventy.js`—are located in their usual places.

The directory `src/main/public/` contains static Web files that will be copied to the output verbatim.
There typically won't be much in here, since most files need to be processed.

## tooling

The static Web files are generated using [Eleventy][Eleventy].
Eleventy is configured to only support HTML-format templates.

[Eleventy]: https://www.11ty.dev/
[Jamstack]: https://en.wikipedia.org/wiki/Jamstack
[sbt]: https://www.scala-sbt.org/
[sbt-web-layout]: https://github.com/sbt/sbt-web/blob/main/README.md#file-directory-layout
