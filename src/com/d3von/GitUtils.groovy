package com.d3von

class GitUtils {
    static void commitAndPush(String repoUrl, String branch, String credentialsId, String commitMessage, Closure changes) {
        dir('deployments') {
            checkout([
                $class: 'GitSCM',
                branches: [[name: branch]],
                extensions: [],
                userRemoteConfigs: [[
                    url: repoUrl,
                    credentialsId: credentialsId
                ]]
            ])

            // Apply changes
            changes()

            // Commit and push
            withCredentials([usernamePassword(
                credentialsId: credentialsId,
                usernameVariable: 'GIT_USERNAME',
                passwordVariable: 'GIT_PASSWORD'
            )]) {
                sh """
                    git config user.name "d3von45"
                    git config user.email "khanhduy.nguyen4520@gmail.com"
                    git add .
                    git commit -m "${commitMessage}"
                    git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${repoUrl.replace('https://', '')} ${branch}
                """
            }
        }
    }
}
