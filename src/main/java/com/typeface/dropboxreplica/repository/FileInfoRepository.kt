package com.typeface.dropboxreplica.repository


import com.typeface.dropboxreplica.entity.FileInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FileInfoRepository : JpaRepository<FileInfo, Long>{

    override fun findAll(): List<FileInfo>

    fun findById(id: String): FileInfo?
}
