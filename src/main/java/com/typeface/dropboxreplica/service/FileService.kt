package com.typeface.dropboxreplica.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.S3Object
import com.typeface.dropboxreplica.entity.FileInfo
import com.typeface.dropboxreplica.exception.SdkException
import com.typeface.dropboxreplica.repository.FileInfoRepository
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile


@Component
class FileService(
    private val fileInfoRepository: FileInfoRepository,
    private val s3Client: AmazonS3Client
) {

    fun uploadFileToS3(file: MultipartFile, fileName: String): String {
        val result = kotlin.runCatching {
            if(!doesBucketExist()){
                createBucket()
            }
            file.inputStream.use {
                s3Client.putObject(PutObjectRequest(BUCKET_NAME, fileName, it, ObjectMetadata()))
            }
        }
        if(result.isSuccess){
            return fileName
        }
        val exception = result.exceptionOrNull()!!
        println("Error uploading file to S3: ${exception.message}, ${exception.stackTrace}" )

        throw SdkException("Error uploading file to S3")
    }

    fun uploadFileInfoToDb(fileInfo: FileInfo){
        val result = kotlin.runCatching {
            fileInfoRepository.save(fileInfo)
        }
        if(result.isFailure){
            val exception = result.exceptionOrNull()!!
            println("Error uploading file info to db: ${exception.message}, ${exception.stackTrace}" )

            throw SdkException("Error uploading file to db")
        }
    }

    fun getAllFiles(): List<FileInfo> {
        val result = kotlin.runCatching {
            fileInfoRepository.findAll()
        }
        if(result.isSuccess){
            return result.getOrNull()!!
        }
        val exception = result.exceptionOrNull()!!
        println("Error retrieving files from database: ${exception.message}, ${exception.stackTrace}" )
        throw  SdkException("Error retrieving files from database")
    }

    fun downloadFile(fileID: String): Pair<FileInfo, S3Object>  {
        val fileInfo = fileInfoRepository.findById(fileID)

        fileInfo?.let {
            return fileInfo to s3Client.getObject(GetObjectRequest(BUCKET_NAME, fileInfo.url))

        }
        throw SdkException("File not found")
    }

    private fun doesBucketExist(): Boolean {
        val listBucketsResponse = s3Client.listBuckets()

        return listBucketsResponse.any { it.name == BUCKET_NAME }
    }

    private fun createBucket() {
        s3Client.createBucket(BUCKET_NAME)
    }

    companion object {
        const val BUCKET_NAME = "typeface-bucket"
    }
}