/*
 * SPDX-FileCopyrightText: 2023 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.glimpse.models

import android.net.Uri
import android.provider.MediaStore

private const val VIDEO_FOLDER = "video/"
private val IMAGE_FOLDER = "image/"

enum class MediaType(
    val externalContentUri: Uri,
    val mediaStoreValue: Int,
) {
    IMAGE(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
    ),
    VIDEO(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
    );

    companion object {
        private val dashMimeTypes = listOf(
            "application/dash+xml",
        )

        private val hlsMimeTypes = listOf(
            "application/vnd.apple.mpegurl",
            "application/x-mpegurl",
            "audio/mpegurl",
            "audio/x-mpegurl",
        )

        private val smoothStreamingMimeTypes = listOf(
            "application/vnd.ms-sstr+xml",
        )

        fun fromMediaStoreValue(value: Int) = values().first {
            value == it.mediaStoreValue
        }

        fun fromMimeType(mimeType: String): MediaType? {
            return when {
                mimeType.startsWith(IMAGE_FOLDER) -> IMAGE
                mimeType.startsWith(VIDEO_FOLDER) -> VIDEO
                mimeType in dashMimeTypes -> VIDEO
                mimeType in hlsMimeTypes -> VIDEO
                mimeType in smoothStreamingMimeTypes -> VIDEO
                else -> null
            }
        }
    }
}
