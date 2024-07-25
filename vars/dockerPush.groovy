def call(String imageName, String registryCredential) {
        def docker.withRegistry( imageName, registryCredential ) {
    dockerImage.push()
    }
}

