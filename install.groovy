pipeline {
    agent any
    environment {
        // AWS credentials (Assuming they're needed in some part of your pipeline)
        AWS_ACCESS_KEY_ID     = credentials('aws-access-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('aws-secret-access-key')
        // Define SSH credentials ID here for reuse
        SSH_KEY_ID            = 'ssh-credentials-id'
    }
    stages {
        stage('Git Checkout') {
            steps {
                // Checkout the code from GitHub
                git branch: 'main', url: 'https://github.com/Sonal-BP/jekinstasks.git'
            }
        }
        stage('Setup SSH Known Hosts') {
            steps {
                // Create .ssh directory if it does not exist
                sh '''
                mkdir -p ~/.ssh
                ssh-keyscan -H 172.31.22.71 >> ~/.ssh/known_hosts
                ssh-keyscan -H 172.31.17.0 >> ~/.ssh/known_hosts
                ssh-keyscan -H 172.31.46.193 >> ~/.ssh/known_hosts
                '''
            }
        }
        stage('Install Jenkins') {
            steps {
                sshagent([env.SSH_KEY_ID]) {
                    // Install Jenkins
                    sh 'ansible-playbook jenkinsinstall.yaml -i inventory.ini'
                }
            }
        }
        stage('Install Nexus') {
            steps {
                sshagent([env.SSH_KEY_ID]) {
                    // Install Nexus Repository Manager
                    sh 'ansible-playbook nexusinstall.yaml -i inventory.ini'
                }
            }
        }
        stage('Install MicroK8s') {
            steps {
                sshagent([env.SSH_KEY_ID]) {
                    // Install MicroK8s
                    sh 'ansible-playbook microk8s.yaml -i inventory.ini'
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
