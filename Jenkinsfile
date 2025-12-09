//Archivo de Jenkins para un pipeline básico que incluye etapas de checkout, build, archive y deploy.

pipeline {
    agent any
    stages {
        stage('Checkout'){ steps{ checkout scm } }
        stage('Build'){ steps{ sh 'echo "Build ejecutado"' } }
        stage('Archive'){ steps{ archiveArtifacts artifacts: '**/*' } }
        stage('Deploy'){ steps{ sh 'echo "Desplegando..."' } }
    }
}