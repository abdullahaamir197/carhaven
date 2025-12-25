

pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.ci.yml'
        APP_BASE_URL = 'http://localhost:8081'
    }

    options {
        timestamps()
        ansiColor('xterm')
    }

    triggers {
        githubPush() 
    }

    stages {
        stage('Checkout') {
            steps {
                echo "🔄 Checking out latest code..."
                checkout scm
            }
        }

        stage('Stop Previous Containers') {
            steps {
                echo "🧹 Cleaning up environment..."
                sh "docker compose -f ${DOCKER_COMPOSE_FILE} down --remove-orphans || true"
                // This fix prevents "Permission Denied" in the next run
                sh "sudo rm -rf selenium-tests/target" 
            }
        }

        stage('Prepare .env') {
            steps {
                withCredentials([
                    string(credentialsId: 'supabase-url', variable: 'SUPABASE_URL'),
                    string(credentialsId: 'supabase-key', variable: 'SUPABASE_KEY'),
                    string(credentialsId: 'supabase-project-id', variable: 'SUPABASE_PROJECT_ID')
                ]) {
                    sh '''
                    cat > .env <<EOF
VITE_SUPABASE_URL=${SUPABASE_URL}
VITE_SUPABASE_PUBLISHABLE_KEY=${SUPABASE_KEY}
VITE_SUPABASE_PROJECT_ID=${SUPABASE_PROJECT_ID}
EOF
                    '''
                }
            }
        }

        stage('Build and Deploy') {
            steps {
                sh "docker compose -f ${DOCKER_COMPOSE_FILE} up -d --build"
            }
        }

        stage('Smoke Test') {
            steps {
                echo "🧪 Waiting for app to respond at ${APP_BASE_URL}..."
                sh '''
                # Try for up to 60 seconds (12 tries * 5s)
                attempt_counter=0
                max_attempts=12
                until $(curl -sSf ${APP_BASE_URL} > /dev/null); do
                    if [ ${attempt_counter} -eq ${max_attempts} ];then
                      echo "❌ App failed to start in time"
                      exit 1
                    fi
                    printf '.'
                    attempt_counter=$(($attempt_counter+1))
                    sleep 5
                done
                echo "✅ App is live!"
                '''
            }
        }

        stage('UI Tests') {
            steps {
                echo "🧪 Running Selenium UI tests..."
                sh '''
                mkdir -p selenium-tests/target .m2
                # THE FIX: Added --user to avoid permission issues
                docker run --rm --network host \
                    --user $(id -u):$(id -g) \
                    -e BASE_URL="${APP_BASE_URL}" \
                    -v "$PWD/selenium-tests:/workspace" \
                    -v "$PWD/.m2":/root/.m2 \
                    markhobson/maven-chrome:latest \
                    /bin/bash -lc 'cd /workspace && mvn test -DbaseUrl=$BASE_URL' | tee selenium-tests/target/ui-tests.log
                '''
            }
        }
    }

    post {
        always {
            // Let the built-in JUnit plugin handle the status. 
            // This is safer than custom Groovy parsing.
            junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
            
            script {
                String recipient = sh(returnStdout: true, script: "git log -1 --pretty=format:'%ae'").trim()
                
                // Simplified status check to avoid Sandbox error
                def buildStatus = currentBuild.currentResult 
                def statusEmoji = (buildStatus == 'SUCCESS') ? '✅' : '❌'

                emailext (
                    to: recipient,
                    subject: "${statusEmoji} ${buildStatus}: Auto Suite Build #${env.BUILD_NUMBER}",
                    body: "Build ${env.BUILD_NUMBER} finished with status: ${buildStatus}\nCheck details here: ${env.BUILD_URL}",
                    mimeType: 'text/html'
                )
            }
        }
    }
}
