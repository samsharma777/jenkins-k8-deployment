@Library ('dockerBuild') _

pipeline {
    agent any

    environment {
        CERT_PASSWORD = credentials('CERT_PASSWORD')
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
                    dockerBuild ('devops091/dotnet-app',"--build-arg CERT_PASSWORD=${env.CERT_PASSWORD}")
                }
            }

        }
    }
}
