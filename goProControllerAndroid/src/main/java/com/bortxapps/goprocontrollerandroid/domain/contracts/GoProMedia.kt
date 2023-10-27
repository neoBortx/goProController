package com.bortxapps.goprocontrollerandroid.domain.contracts

import arrow.core.Either
import com.bortxapps.goprocontrollerandroid.domain.GoProError
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItems

interface GoProMedia {
    suspend fun getMediaList(): Either<GoProError, MediaItems>
    suspend fun getMediaInfo(fileName: String): Either<GoProError, MediaItems>
    suspend fun getMediaVideo(fileName: String): Either<GoProError, ByteArray>
    suspend fun getMediaImage(fileName: String): Either<GoProError, ByteArray>
    suspend fun getMediaThumbnail(fileName: String): Either<GoProError, ByteArray>
    suspend fun getMediaScreenNail(fileName: String): Either<GoProError, ByteArray>
}