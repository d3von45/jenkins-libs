def call(Map config = [:]) {
    def defaults = [
        repoUrl: 'https://github.com/d3von45/test.git',
        branch: 'main',
        credentialsId: 'github',
        commitMessage: 'Automated commit from Jenkins',
        changes: { -> }
    ]
    config = defaults + config

    if (!config.repoUrl?.trim()) {
        error "Repository URL is required!"
    }
    if (!config.credentialsId?.trim()) {
        error "Credentials ID is required!"
    }

   dir('deployments') {
    checkout([
      $class: 'GitSCM',
      branches: [[name: config.branch]],
      extensions: [],
      userRemoteConfigs: [[
        url: config.repoUrl,
        credentialsId: config.credentialsId
      ]]
    ])
    
    config.changes()

    // Commit and push
    withCredentials([usernamePassword(
      credentialsId: config.credentialsId,
      usernameVariable: 'GIT_USERNAME',
      passwordVariable: 'GIT_PASSWORD'
    )]) {
        sh """
          git config user.name "d3von45"
          git config user.email "khanhduy.nguyen4520@gmail.com"
          git add .
          git commit -m "${config.commitMessage}"
          git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${repoUrl.replace('https://', '')} ${config.branch}
        """
      }
   }
 
}
