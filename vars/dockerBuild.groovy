def call(String imageName ) {
def customImage = docker.build("${imageName}:${env.BUILD_ID}")
    customImage.push()
}