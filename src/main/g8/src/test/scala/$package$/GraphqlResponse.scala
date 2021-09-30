package $package$

import io.circe.Json

case class GraphqlResponse(data: Json, error: Option[Json])
