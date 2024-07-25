@Library ('dockerBuild') _

pipeline {
    agent {
    // Equivalent to "docker build -f Dockerfile.build --build-arg version=1.0.2 ./build/
    dockerfile {
        filename 'Dockerfile.agent'
        label 'dind-jenkins-agent'
        args '--network jenkins-minikube -v /var/run/docker.sock:/var/run/docker.sock'
    }
}

    stages {
        stage('Checkout') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'main', url: 'https://github.com/nanditechbytes/jenkins-k8-deployment.git'
            }

        }
        stage('Identify User') {
            steps {
                script {
                    // Output the current user
                    sh 'whoami'
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    dockerBuild ('devops091/dotnet-app')
                }
            }

        }
    }
}
