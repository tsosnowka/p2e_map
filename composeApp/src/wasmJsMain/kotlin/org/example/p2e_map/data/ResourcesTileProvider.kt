package org.example.p2e_map.data

import kotlinx.io.Buffer
import kotlinx.io.RawSource
import ovh.plrapps.mapcompose.core.TileStreamProvider
import p2e_map.composeapp.generated.resources.Res

class ResourcesTileProvider(
    val basePath: String = "drawable/tiles"
) : TileStreamProvider {

    override suspend fun getTileStream(
        row: Int,
        col: Int,
        zoomLvl: Int
    ): RawSource? {
        val path = "$basePath/$zoomLvl/$row/$col.png"
        return kotlin.runCatching {
            val bytes = Res.readBytes(path)
            Buffer().apply { write(bytes) }
        }.getOrNull()
    }
}