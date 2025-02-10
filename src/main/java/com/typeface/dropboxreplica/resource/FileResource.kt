package com.typeface.dropboxreplica.resource

import com.typeface.dropboxreplica.entity.FileInfo
import com.typeface.dropboxreplica.exception.SdkException
import com.typeface.dropboxreplica.service.FileService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.util.UUID

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class FileResource(
    private val fileService: FileService,
) {

    @RequestMapping("/upload")
    fun uploadFile(@RequestParam file: MultipartFile): FileInfo{
        val fileUrl = fileService.uploadFileToS3(file, file.originalFilename!!)
        val fileInfo = FileInfo(
            id= UUID.randomUUID().toString(),
            name=file.originalFilename!!,
            url=fileUrl,
            size=file.size,
            fileType=file.contentType!!,
        )
        fileService.uploadFileInfoToDb(fileInfo)
        return fileInfo
    }

    @RequestMapping("/")
    fun listAllFiles(): List<FileInfo>{
        return fileService.getAllFiles()
    }

    @RequestMapping("/{fileId}")
    fun downloadFile(@PathVariable fileId: String): ResponseEntity<InputStreamResource>{

        val (fileInfo,s3Object) = fileService.downloadFile(fileId)
        val result = kotlin.runCatching {
            val inputStream = s3Object.objectContent

            val bytes = inputStream.readBytes()
            val byteArrayInputStream = ByteArrayInputStream(bytes)
            val inputResource = InputStreamResource(byteArrayInputStream)

            val headers = HttpHeaders()
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${fileInfo.name}\"")
            headers.contentType = MediaType.parseMediaType(fileInfo.fileType)

            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.size.toLong())
                .body(inputResource)
        }
        println("Error downloading file: ${result.exceptionOrNull()!!.message},   error: ${result.exceptionOrNull()!!.stackTrace}")
        throw SdkException("Error downloading file: $fileId")
    }

}