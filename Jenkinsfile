pipeline {
  environment {
    dockerimagename = "nanditechbytes/react-app"
    dockerImage = ""
    KUBECTL_HOME = "${env.WORKSPACE}/kubectl"
  }
  agent any
  stages {
    stage('Checkout Source') {
      steps {
        git branch: 'main', url: 'https://github.com/nanditechbytes/jenkins-k8-deployment.git', credentialsId: 'GitHub'
      }
    }
    stage('Install kubectl') {
      steps {
        script {
          sh '''
            curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
            chmod +x ./kubectl
          '''
        }
      }
    }
    stage('Build image') {
      steps{
        script {
          dockerImage = docker.build dockerimagename
        }
      }
    }
    stage('Pushing Image') {
      environment {
          registryCredential = 'dockerhub-credentials'
      }
      steps{
        script {
          docker.withRegistry( 'https://registry.hub.docker.com', registryCredential ) {
            dockerImage.push("latest")
          }
        }
      }
    }
    stage('Deploying React.js container to Kubernetes') {
      steps {
        withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
          script {
            sh '''
              export PATH=${KUBECTL_HOME}:$PATH
              kubectl apply -f deployment.yaml
              kubectl apply -f service.yaml
            '''
          }
        }
      }
    }
  }
}
