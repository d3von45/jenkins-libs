def call(Map config = [:]) {

  pipeline {
    agent any
    
    stages{
      stage('Checkout'){
        steps {
            checkout scm
        }
      }
      
      stage('Sonar'){
        steps{
          sonarScan(config.sonarConfig ?: [:])
        }
      }

      stage('Docker'){
        steps{
          script {
            dockerBuild(config.dockerConfig ?: [:])
          }
        }
      }
    }

    post {
      always {
          cleanWs()
          script {
              currentBuild.result = currentBuild.result ?: 'SUCCESS'
          }
      }
      failure {
          slackSend channel: '#build-failures',
                          message: "Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
        }
    }
  }

}
