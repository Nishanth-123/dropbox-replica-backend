package com.typeface.dropboxreplica.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class FileInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var dbId: Long? = null,
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var url: String = "",
    var size: Long = 0L,
    var fileType: String = "",
    var created: LocalDateTime = LocalDateTime.now(),
)
