package com.bortxapps.goprocontrollerandroid.feature.base

import com.bortxapps.goprocontrollerandroid.domain.GoProError

class RepositoryException (val goProError: GoProError): Throwable()