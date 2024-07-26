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
                 withCredentials([
                    string(credentialsId: 'kubeconfig', variable: 'KUBE_CONFIG')
                ]) {
                    script {
                // Check if the secret already exists
                     def secretExists = sh(script: 'kubectl get secret https-cert-secret --ignore-not-found', returnStatus: true) == 0
                
                     if (!secretExists) {
                    // If the secret does not exist, create it
                     sh """
                        kubectl create secret generic https-cert-secret --from-literal=password=${env.CERT_PASSWORD}
                        """
                        echo "Secret 'https-cert-secret' created."
                    } else {
                        echo "Secret 'https-cert-secret' already exists. Skipping creation."
                }
              }
            }
          }
        }
        stage('Create Docker Registry Secret') {
            steps {
                 withCredentials([
                    string(credentialsId: 'kubeconfig', variable: 'KUBE_CONFIG')
                ]) {
                    script {

                        def secretExists = sh(script: 'kubectl get secret ${APP_NAME} --ignore-not-found', returnStatus: true) == 0
                        if (!secretExists) {
                        sh """
                        kubectl create secret docker-registry ${APP_NAME} \
                        --docker-server=https://index.docker.io/v1/ \
                        --docker-username=${USER} \
                        --docker-password=${PASSWORD} \
                        --docker-email=${DOCKER_HUB_EMAIL}|| true
                        """
                        echo "Secret ${APP_NAME} created."
                        } else {
                        echo "Secret ${APP_NAME} already exists. Skipping creation."
                        }
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                 withCredentials([
                    string(credentialsId: 'kubeconfig', variable: 'KUBE_CONFIG')
                ]) {
                    script {
                        // Replace image name in deployment.yaml
                        sh """
                        sed -i '' 's|<docker-image>|${IMAGE_NAME}:${BUILD_ID}|g' deployment.yml 
                        sed -i '' 's|my-registry-secret|${APP_NAME}|g' deployment.yml
                        sed -i '' 's|sample-app|${APP_NAME}|g' deployment.yml
                        sed -i '' 's|sample-app|${APP_NAME}|g' service.yml
                        """

                        // Apply deployment and service files
                        sh 'kubectl apply -f deployment.yml'
                        sh 'kubectl apply -f service.yml'
                    }
                }
            }
        }
        stage('Verify Deployment') {
            steps {
                withCredentials([
                    string(credentialsId: 'kubeconfig', variable: 'KUBE_CONFIG')
                ])  {
                        script {
                         def deployStatus = sh(script: 'kubectl rollout status deployment/$APP_NAME-deployment', returnStatus: true)
                            if (deployStatus != 0) {
                            echo "Deployment failed or is not completed. Triggering rollback."
                            sh 'kubectl rollout undo deployment/${APP_NAME}-deployment'
                            error "Deployment failed. Rolled back to the previous version."
                        }

                            // Check if the pods are running
                         def podStatus = sh(script: 'kubectl get pods -l app=${APP_NAME} -o jsonpath="{.items[0].status.phase}"', returnStdout: true).trim()
                         if (podStatus != 'Running') {
                            echo "Pods are not in Running state. Triggering rollback."
                            sh 'kubectl rollout undo deployment/${APP_NAME}-deployment'
                            error "Pods are not in Running state. Rolled back to the previous version."
                         }

                        // Check if the service is available
                         def serviceStatus = sh(script: 'kubectl get services ${APP_NAME}-service -o jsonpath="{.spec.ports[0].nodePort}"', returnStdout: true).trim()
                            if (!serviceStatus) {
                            echo "Service is not available. Triggering rollback."
                            sh 'kubectl rollout undo deployment/${APP_NAME}-deployment'
                            error "Service is not available. Rolled back to the previous version."
                         }

                            echo "Deployment and service are successfully created and running."
                    }
                }
            }
        }
    }
}
