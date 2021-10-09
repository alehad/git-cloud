pipeline {
    agent {
        docker {
            image 'maven:3.8.1-adoptopenjdk-11' 
        }
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
                    sh 'docker build -t alehad/msgr-test:lts .'
                }
            }
        }
    }
}