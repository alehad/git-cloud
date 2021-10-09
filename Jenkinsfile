pipeline {
    agent any
    environment {
        registry = 'alehad/msgr-test'
        jenkins_credentials = 'hub.docker.id' // docker hub user defined in jenkins credentials
        app_docker_image = ''
    }
    stages {
        stage('build test project') {
            agent {
                docker {
                    image 'maven:3.8.1-adoptopenjdk-11' 
                }
            }
            steps {
                dir('test') {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
        }
        stage('create docker image') {
            agent { label 'master' } // this ensures that jenkins will try to access docker running on same host jenkins is running
            steps {
                dir('test') {
                    script {
                        app_docker_image = docker.build registry
                    }
                }
            }
        }
        stage('upload docker image') {
            agent { label 'master' }
            steps {
                script {
                    docker.withRegistry('', 'hub.docker.id')
                    app_docker_image.push()
                }
            }
        }
    }
}