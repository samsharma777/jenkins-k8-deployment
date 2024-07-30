````markdown
# Build and Deploy Sample Application with Jenkins Pipeline to Minikube Kubernetes Cluster

## Prerequisites

Ensure you have Homebrew installed on your Mac. If not, you can install it using:

```sh
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

## 1. Install Java

1. **Install Java using Homebrew:**

   ```sh
   brew install java11
   ```

   This will install OpenJDK 11.

2. **Set JAVA_HOME Environment Variable:** (Optional)

   After installation, you need to set the `JAVA_HOME` environment variable. The lines will be populated in the logs of java installation.
   The lines might look like the below examples.

   ```sh
   export PATH="/opt/homebrew/opt/openjdk@11/bin:$PATH" >> ~/.zshrc
   export JAVA_HOME=$(/usr/libexec/java_home -v 11)
   export PATH=$JAVA_HOME/bin:$PATH
   ```


## 2. Install Jenkins LTS

1. **Install Jenkins LTS using Homebrew:**

   ```sh
   brew install jenkins-lts
   ```

2. **Start Jenkins:**

   ```sh
   brew services start jenkins-lts
   ```

   Jenkins will start automatically and run as a background service.

3. **Access Jenkins:**

   Open your web browser and navigate to:

   ```
   http://localhost:8080
   ```

4. **Unlock Jenkins:**

   You’ll need to unlock Jenkins using the initial admin password. Find the password with:

   ```sh
   cat /Users/$(whoami)/.jenkins/secrets/initialAdminPassword
   ```

   Copy the password and paste it into the Jenkins unlock page.


## 4. Install Minikube

1. **To install the latest minikube stable release on x86-64 macOS using binary download::**

   ```sh
   curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-darwin-amd64
   sudo install minikube-darwin-amd64 /usr/local/bin/minikube
   ```

2. **Start Minikube:**

   ```sh
   minikube start
   ```

   This will start Minikube with default settings.

3. **Verify Minikube Status:**

   ```sh
   minikube status
   ```

## Conclusion

You have successfully installed Java, Jenkins LTS, and Minikube on your Mac using Homebrew. Ensure to check each tool's documentation for advanced configurations and troubleshooting.
