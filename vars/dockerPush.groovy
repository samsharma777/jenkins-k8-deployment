def call(String registry, String registryCredential) {
    docker.withRegistry(registry, registryCredential) {
      def dockerImage = docker.image(imageName)
      dockerImage.push()
    }
}

