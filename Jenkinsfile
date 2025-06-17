pipeline {
    agent any

    stages {
        stage('Build Spring Boot App') {
            steps {
                echo 'Building the application...'
                sh 'docker run --rm -v $WORKSPACE:/app -v /root/.m2:/root/.m2 -w /app maven:3.9-eclipse-temurin-21 mvn clean package -DskipTests'
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