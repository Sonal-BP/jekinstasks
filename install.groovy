pipeline {
    agent any
    environment {
        // Define environment variables here if needed
        AWS_ACCESS_KEY_ID     = credentials('aws-access-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('aws-secret-access-key')
        SSH_KEY_ID            = 'ssh-credentials-id'
    }
    stages {
        stage('Git Checkout') {
            steps {
                // Checkout the code from GitHub
                git branch: 'main', url: 'https://github.com/Sonal-BP/jekinstasks.git'
            }
        }
        stage('Install Jenkins') {
            steps {
                script {
                    // Use sshagent to execute commands on remote servers
                     sshagent([env.SSH_KEY_ID]) {
                         sh 'ansible-playbook nexusinstall.yaml -i inventory.ini'
                    }
                }
            }
        }
        stage('Install Nexus') {
            steps {
                script {
                    // Use sshagent to execute commands on remote servers
                    sshagent(['ssh-credentials-id']) {
                        sh 'ansible-playbook nexusinstall.yaml -i inventory.ini'
                    }
                }
            }
        }
        stage('Install MicroK8s') {
            steps {
                script {
                    // Use sshagent to execute commands on remote servers
                    sshagent(['ssh-credentials-id']) {
                        sh 'ansible-playbook microk8s.yaml -i inventory.ini'
                    }
                }
            }
        }
    }
    post {
        always {
            echo 'Pipeline execution completed.'
        }
    }
}
