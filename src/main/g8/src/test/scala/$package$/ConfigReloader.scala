package $package$

import com.typesafe.config.ConfigFactory

/** A thread-safe reloader for Typesafe Config. */
object ConfigReloader {
  /** Reload the Typesafe Config, executing the given function.
    *
    * @param f A function to execute before synchronization locks on the configuration loading are released.
    */
  def reload(f: => Unit): Unit = this.synchronized {
    ConfigFactory.invalidateCaches()
    f
  }
}
