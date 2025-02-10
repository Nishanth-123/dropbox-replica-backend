## A dropbox replica kinda

### Build and run

#### Prerequisites

- Java 17
- Maven 
- PostgreSQL

#### Configurations

Ensure docker daemon is running and run the compose.yaml file in the root directory. Both database and localstack would be started. If mentioned ports in the compose.yaml are busy, you can mention some other ports and ensure same things are reflected in the `application.properties` file as well.

#### Schema installation

Schema is already run when we start the database using compose.yaml. So the following table would also get created

- file_info

#### From Editor (IntelliJ or Eclipse)

Import as *Existing Maven Project* and run it as *Spring Boot App*.


### Usage

- Run the application and go on http://localhost:8080/

### Resources

#### FilesResource
    POST /files/upload Uploads a new file in to aws and its meta data is stored in postgresql
    GET /files gets all files metadata.
    GET /files/{fileId} gets file from s3 using metadata from db.
    
## TODOs

- Test cases
- Authorization: Role based access.


