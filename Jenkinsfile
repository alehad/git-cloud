pipeline {
    agent {
        docker {
            image 'maven:3.8.1-adoptopenjdk-11' 
        }
    }
    stages {
        stage('build') {
            steps {
                dir('test') {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
        }
    }
}