import com.d3von.GitUtils

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

    def gitUtils = new GitUtils()
    gitUtils.commitAndPush(
        steps,
        config.repoUrl,
        config.branch,
        config.credentialsId,
        config.commitMessage,
        config.changes
    )
}
