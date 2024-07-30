def call(String secret_type, String secret_name, String cert_password) {
    // Check if the secret already exists
    def secretExists = sh(script: "kubectl get secret ${secret_name} --ignore-not-found", returnStatus: true) == 0

    if (!secretExists) {
        // If the secret does not exist, create it
        sh """
            kubectl create secret ${secret_type} ${secret_name} --from-literal=password=${cert_password}
        """
        echo "Secret ${secret_name} created."
    } else {
        echo "Secret ${secret_name} already exists. Skipping creation."
    }
}
