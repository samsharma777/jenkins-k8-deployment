def call(String imageName, String registryCredential) {
    docker.withRegistry(imageName, registryCredential) {
      def dockerImage = docker.image(imageName)
      dockerImage.push()
    }
}

