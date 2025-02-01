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
    git branch: config.branch,
      credentialsId: config.credentialsId,
      changelog: false,
      url: config.repoUrl
    
    config.changes()

    // Commit and push
    withCredentials([usernamePassword(
      credentialsId: config.credentialsId,
      usernameVariable: 'GIT_USERNAME',
      passwordVariable: 'GIT_PASSWORD'
    )]) {
        sh """
          ls
          git config user.name "d3von45"
          git config user.email "khanhduy.nguyen4520@gmail.com"
          git add .
          git commit -m "${config.commitMessage}"
          git branch
          git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${config.repoUrl.replace('https://', '')} ${config.branch}
        """
      }
   }
 
}
