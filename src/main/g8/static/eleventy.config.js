const pluginWebc = require("@11ty/eleventy-plugin-webc");
const EleventyVitePlugin = require("@11ty/eleventy-plugin-vite");

/* Eleventy configuration.
 *
 * This code tells Eleventy
 * - the directory from which to read its files,
 * - the directory from which to copy files verbatim,
 * - the directory to which it should output,
 * - to process .webc components, and
 * - to post-process with Vite.
 *
 * It also creates a "relativeRoot" filter, to be used to relativize the HTML links to other assets, so that Parcel
 * can understand them.
 */
module.exports = function(eleventyConfig) {
  // Enable Vite post-processing.
  eleventyConfig.addPlugin(EleventyVitePlugin);

  // Enable Eleventy's WebC component system.
  eleventyConfig.addPlugin(pluginWebc);

  // Copy verbatim files.
  eleventyConfig.addPassthroughCopy({"src/main/public": "."});
  eleventyConfig.addPassthroughCopy("**/*.jsx");
  eleventyConfig.setServerPassthroughCopyBehavior("copy");

  return {
    dir: {
      input: "src/main/assets",
      layouts: "_layouts",
      output: "target/web/public/main",
      templateFormats: ["html", "md", "njk"]
    }
  }
};
