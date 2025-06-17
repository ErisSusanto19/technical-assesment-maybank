pipeline {

    agent {
        any {
            tools {
                docker 'docker-latest'
            }
        }
    }

    stages {
        stage('Build Spring Boot App') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-21'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                echo 'Building the application...'
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Docker Image') {
            steps {
                echo 'Building the Docker image...'
                script {
                    def customImage = docker.build("simple-backend:build-${env.BUILD_NUMBER}")
                }
            }
        }
    }
}