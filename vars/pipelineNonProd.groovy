def call(Map config = [:]) {

  pipeline {
    agent any
    
    stages{
      stage('Checkout'){
        steps {
            git branch: "${config.repo.branch}",
                        credentialsId: "${config.repo.credentials}",
                        url: "${config.repo.url}"
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
    }
  }

}
