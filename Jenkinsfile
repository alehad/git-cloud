pipeline {
    agent {
        docker {
            image 'maven:3.8.1-adoptopenjdk-11' 
        }
    }
    environment {
        registry = 'alehad/msgr-test'
        jenkins_credentials = 'hub.docker.id' // user defined in jenkins credentials
        app_docker_image = ''
    }
    stages {
        stage('build test project') {
            steps {
                dir('test') {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
        }
        stage('create docker image') {
            steps {
                dir('test') {
                    script {
                        app_docker_image = docker.build registry
                    }
                }
            }
        }
    }
}