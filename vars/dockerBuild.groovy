def call(String imageName, String buildArgs="") {
def customImage = docker.build("${imageName}:${env.BUILD_ID}", "${buildArgs} .")
    customImage.push()
}