def call(String imageName, String buildArgs = "", String registry, String credentialsId) {
    docker.withRegistry("https://${registry}", credentialsId) {
        def customImage = docker.build("${imageName}:${env.BUILD_ID}", buildArgs + " .")
        customImage.push()
    }
}
