def call(String registryCredential) {
        def docker.withRegistry( '', registryCredential ) {
    dockerImage.push()
    }
}

