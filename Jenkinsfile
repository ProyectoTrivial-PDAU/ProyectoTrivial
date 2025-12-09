//Archivo de Jenkins para un pipeline básico que incluye etapas de checkout, build, archive y deploy.

pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'echo "Build ejecutado en Linux"'
                    } else {
                        bat 'echo "Build ejecutado en Windows"'
                    }
                }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: '**/*'
            }
        }

        stage('Deploy') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'echo "Desplegando en Linux..."'
                    } else {
                        bat 'echo "Desplegando en Windows..."'
                    }
                }
            }
        }
    }
}
