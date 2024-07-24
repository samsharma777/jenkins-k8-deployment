@Library ('dockerBuild') _

pipeline {
    agent {'ec2-fleet'}

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
                    dockerBuild ('devops091/dotnet-app',${env.BUILD_ID})
                }
            }

        }
    }
}
