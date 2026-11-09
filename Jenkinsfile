pipeline {
    agent { label '!windows' }
    options {
        skipStagesAfterUnstable()
        disableConcurrentBuilds()
        timeout(time: 15, unit: 'MINUTES')
    }
    stages {
        stage('Build') {
            steps { sh 'mvn -B -DskipTests clean package' }
        }
        stage('Test') {
            steps { sh 'mvn -B verify' }
            post {
                always { junit 'target/surefire-reports/TEST-*.xml' }
                success { archiveArtifacts artifacts: 'target/site/jacoco/**', fingerprint: true }
            }
        }
        stage('Deliver') {
            steps {
                sh '''
                    mkdir -p target/resultados
                    java -cp target/classes Main 0.20 6 > target/resultados/caso1.txt
                    java -cp target/classes Main 0.45 15 > target/resultados/caso2.txt
                    java -cp target/classes Main 0.495 4 > target/resultados/caso3.txt
                '''
                archiveArtifacts artifacts: 'target/*.jar,target/resultados/*.txt', fingerprint: true
            }
        }
    }
}
