/*
 * SPDX-FileCopyrightText: 2023-2024 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.glimpse.ext

import android.content.Intent
import org.lineageos.glimpse.models.Media
import org.lineageos.glimpse.models.MediaType.IMAGE
import org.lineageos.glimpse.models.MediaType.VIDEO

private const val IMAGE_ALL = "image/*"
private const val VIDEO_ALL = "video/*"
private const val ALL = "*/*"

fun buildShareIntent(vararg medias: Media) = Intent().apply {
    assert(medias.isNotEmpty()) { "No media" }

    if (medias.size == 1) {
        action = Intent.ACTION_SEND
        medias[0].let {
            putExtra(Intent.EXTRA_STREAM, it.uri)
            type = it.mimeType
        }
    } else {
        action = Intent.ACTION_SEND_MULTIPLE
        putParcelableArrayListExtra(
            Intent.EXTRA_STREAM,
            medias.map { it.uri }.toCollection(ArrayList())
        )
        type = when {
            medias.all { it.mediaType == IMAGE } -> IMAGE_ALL
            medias.all { it.mediaType == VIDEO } -> VIDEO_ALL
            else -> {
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(IMAGE_ALL, VIDEO_ALL))
                ALL
            }
        }
    }
    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
}

fun buildEditIntent(media: Media) = Intent().apply {
    action = Intent.ACTION_EDIT
    setDataAndType(media.uri, media.mimeType)
    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
}

fun buildUseAsIntent(media: Media) = Intent().apply {
    action = Intent.ACTION_ATTACH_DATA
    setDataAndType(media.uri, media.mimeType)
    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
}
