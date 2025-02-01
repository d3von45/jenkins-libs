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

      stage('Commit Deployments Change'){
        steps {
          script {
            commitToRepo(
              commitMessage: 'Updated version number',
              changes: {
                sh 'echo "version=1.2.3" > version.txt'
              }
            )
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
