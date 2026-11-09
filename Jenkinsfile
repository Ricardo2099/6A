
pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {
        stage('Build') {
            steps {
                echo 'Compilando el proyecto 6A...'
                bat 'mvn -B clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo 'Ejecutando pruebas JUnit...'
                bat 'mvn -B test'
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Deliver') {
            steps {
                echo 'Generando artefactos...'
                archiveArtifacts artifacts: 'target/*.jar',
                                 fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'BUILD SUCCESS: Pipeline completado.'
        }
        failure {
            echo 'BUILD FAILURE: Se encontraron errores.'
        }
    }
}
