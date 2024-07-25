@Library('dockerBuild') _

pipeline {
    agent any

    environment {
        CERT_PASSWORD = credentials('CERT_PASSWORD')
        IMAGE_NAME = "devops091/dotnet-app"
        DOCKER_CREDENTIALS_ID = credentials('dockerhub')
        REGISTRY = "https://index.docker.io/v1/"
    }

    stages {
        stage('Checkout') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'main', url: 'https://github.com/nanditechbytes/jenkins-k8-deployment.git'
            }
        }
        stage('Build') {
            steps {
                script {
                    dockerBuild("${env.IMAGE_NAME}", "--build-arg CERT_PASSWORD=${env.CERT_PASSWORD}")
                }
            }
        }
        stage('Push') {
            steps {
                script {
                    dockerPush("${env.REGISTRY}", "${env.DOCKER_CREDENTIALS_ID}")
                }
            }
        }
        stage('Cleanup') {
            steps {
                sh "docker rmi $IMAGE_NAME:$BUILD_ID"
            }
        }
    }
}
