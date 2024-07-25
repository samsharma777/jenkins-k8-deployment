def call(String imageName, String buildArgs="", String registry, String credentials_id) {

docker.withRegistry("${registry}", "${credentials_id}") {   
def customImage = docker.build("${imageName}:${env.BUILD_ID}", "${buildArgs} .")
    customImage.push()
 }
}