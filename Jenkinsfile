pipeline {

    agent any

    stages {
        stage('Build Spring Boot App') {
            steps {

                script {

                    docker.image('maven:3.9-eclipse-temurin-21').inside('-v $HOME/.m2:/root/.m2') {

                        echo 'Sekarang saya berada di dalam container Maven...'

                        sh 'mvn clean package -DskipTests'
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Membangun final image...'
                    def customImage = docker.build("simple-backend:build-${env.BUILD_NUMBER}")
                }
            }
        }
    }
}