def call(String imageName, String buildArgs = "") {
    docker.withRegistry("docker.io", "devops091/rajunandi91!") {
        def customImage = docker.build("${imageName}:${env.BUILD_ID}", buildArgs + " .")
        customImage.push()
    }
}
