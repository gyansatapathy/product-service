# Product Services

## To Run the app locally:
- Java version required: 11.0.13
- ./gradlew.sh bootRun / gradlew.bat bootRun

## To run the app in a container:
### Everytime code gets checked in a docker image gets pushed to the docker hub
- Docker hub: https://hub.docker.com/r/gyaniscool/product-service/tags
- Start the app: docker run -p -d 8888:8080 gyaniscool/product-service:[insert_latest_tag_from_docker_hub]
- UI Repository: https://github.com/gyansatapathy/product-service
- Note: Port has to be 8888 because UI calls at this port in the prod build.