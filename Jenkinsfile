pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.ci.yml'
    }

    options {
        timestamps()
        ansiColor('xterm')
    }

    triggers {
        githubPush()  // Auto-trigger on every push
    }

    stages {

        stage('Checkout') {
            steps {
                echo "🔄 Checking out latest code..."
                checkout scm
                sh 'pwd && ls -la'
            }
        }

        stage('Docker Info') {
            steps {
                sh 'docker version'
                sh 'docker compose version'
            }
        }

        stage('Stop Previous Containers') {
            steps {
                echo "🧹 Stopping any running containers..."
                sh "docker compose -f ${DOCKER_COMPOSE_FILE} down --remove-orphans || true"
            }
        }

        stage('Prepare .env') {
            steps {
                echo "🧾 Creating .env file for frontend..."
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
                    echo "✅ .env created successfully"
                    '''
                }
            }
        }

        stage('Build and Deploy') {
            steps {
                echo "🚀 Building and starting containers..."
                sh "docker compose -f ${DOCKER_COMPOSE_FILE} up -d --build"
            }
        }

        stage('Smoke Test') {
            steps {
                echo "🧪 Checking if app is up..."
                sh '''
                sleep 10
                if curl -I http://localhost:8081 2>/dev/null | grep -q "200"; then
                    echo "✅ App is live!"
                else
                    echo "⚠️ App did not respond as expected."
                fi
                '''
            }
        }
    }

    post {
        success {
            echo "✅ Deployment completed successfully! Visit: http://<your-ec2-ip>:8081"
        }
        failure {
            echo "❌ Build or deployment failed. Check logs."
        }
    }
}
