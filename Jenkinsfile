pipeline {
    agent {
        docker {
            image 'maven:3.8.2-adoptopenjdk-11' 
            args '-v /root/.m2:/root/.m2' 
        }
    }
    stages {
        stage("build messenger project @ repo level git-cloud") {
            steps {
                echo 'cd to test app folder...'
                sh 'cd ./test'
                echo 'do clean maven build...'
                sh 'mvn -B -DskipTests clean package'
            }
        }
    }
}