pipeline {

    agent any

    stages {

        stage('Build Production') {
            steps {
                //build app in production mode, package as .jar
                sh './mvnw -Pprod clean package'

            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh 'mvn sonar:sonar \
                -Dsonar.projectKey=team-8 \
                -Dsonar.host.url=https://sonarqube.seng.uvic.ca \
                -Dsonar.login=9afcdeafe37850a693af73c797e9bc8a6a12cf9e'
            } //submitted SonarQube taskId is automatically attached to the pipeline context
        }

        stage('Run Unit Tests') {
            steps {
                sh './mvnw test'
            }         
        }
    }

    post{
        success{
            echo 'All tests passed successfully.'
        }
        unstable{
            echo 'Some steps behaved unexpectedly.'
        }
        failure{
            echo 'Pipeline failed.'
            mail to: 'rbassot@shaw.ca',
                subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                body: "Something went wrong with this pipeline build..."
            mail to: 'nishchint34@gmail.com',
                subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                body: "Something went wrong with this pipeline build..."
            mail to: 'kunnapool@gmail.com',
                subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                body: "Something went wrong with this pipeline build..."
        }
    }
}

