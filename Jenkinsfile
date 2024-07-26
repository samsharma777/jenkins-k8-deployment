@Library('dockerBuild') _

pipeline {
    agent any

    environment {
        CERT_PASSWORD = credentials('CERT_PASSWORD')
        IMAGE_NAME = "devops091/dotnet-app"
        PASSWORD = credentials('dockerhub')
        REGISTRY = "https://index.docker.io/v1/"
        USER = "devops091"
        APP_NAME = "sample-app"
        DOCKER_HUB_EMAIL = "nanditechbytes@gmail.com"
    }

    stages {
        stage('Checkout') {
            steps {
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
                dockerPush("${env.USER}", "${env.PASSWORD}", "${env.IMAGE_NAME}", "${env.BUILD_ID}")
                }
            }
        }
        stage('Cleanup') {
            steps {
                sh "docker rmi $IMAGE_NAME:$BUILD_ID"
            }
        }
        stage('Create Kubernetes Secret') {
            steps {
                kubeconfig(credentialsId: 'kubeconfig') {
                    script {
                        sh """
                        kubectl create secret generic https-cert-secret --from-literal=password=${env.CERT_PASSWORD} || true
                        """
                    }
                }
            }
        }
        stage('Create Docker Registry Secret') {
            steps {
                kubeconfig(credentialsId: 'kubeconfig') {
                    script {
                        sh """
                        kubectl create secret docker-registry ${env.APP_NAME} \
                        --docker-server=https://index.docker.io/v1/ \
                        --docker-username=${env.USER} \
                        --docker-password=${env.PASSWORD} \
                        --docker-email=${env.DOCKER_HUB_EMAIL}|| true
                        """
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                kubeconfig(credentialsId: 'kubeconfig') {
                    script {
                        // Replace image name in deployment.yaml
                        sh """
                        sed -i 's|<docker-image>|${IMAGE_NAME}:${BUILD_ID}|g' deployment.yaml
                        """

                        // Apply deployment and service files
                        sh 'kubectl apply -f deployment.yaml'
                        sh 'kubectl apply -f service.yaml'
                    }
                }
            }
        }
        stage('Verify Deployment') {
            steps {
                kubeconfig(credentialsId: 'kubeconfig') {
                    script {
                        def deployStatus = sh(script: 'kubectl rollout status deployment/$APP_NAME-deployment', returnStatus: true)
                        if (deployStatus != 0) {
                            echo "Deployment failed or is not completed. Triggering rollback."
                            sh 'kubectl rollout undo deployment/$APP_NAME-deployment'
                            error "Deployment failed. Rolled back to the previous version."
                        }

                        // Check if the pods are running
                        def podStatus = sh(script: 'kubectl get pods -l app=$APP_NAME -o jsonpath="{.items[0].status.phase}"', returnStdout: true).trim()
                        if (podStatus != 'Running') {
                            echo "Pods are not in Running state. Triggering rollback."
                            sh 'kubectl rollout undo deployment/$APP_NAME-deployment'
                            error "Pods are not in Running state. Rolled back to the previous version."
                        }

                        // Check if the service is available
                        def serviceStatus = sh(script: 'kubectl get services $APP_NAME-service -o jsonpath="{.status.loadBalancer.ingress[0].ip}"', returnStdout: true).trim()
                        if (!serviceStatus) {
                            echo "Service is not available. Triggering rollback."
                            sh 'kubectl rollout undo deployment/$APP_NAME-deployment'
                            error "Service is not available. Rolled back to the previous version."
                        }

                        echo "Deployment and service are successfully created and running."
                    }
                }
            }
        }
    }
}
