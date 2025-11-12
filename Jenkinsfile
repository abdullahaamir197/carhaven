pipeline {
  agent any

  parameters {
    choice(name: 'MODE', choices: ['down', 'up'], description: 'Bring the environment up or down')
  }

  environment {
    COMPOSE_FILE = 'docker-compose.ci.yml'
  }

  options { timestamps(); ansiColor('xterm') }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        sh 'pwd && ls -la'
      }
    }

    stage('Prepare .env (only when up)') {
      when { expression { params.MODE == 'up' } }
      steps {
        withCredentials([
          string(credentialsId: 'supabase-url',        variable: 'SUPABASE_URL'),
          string(credentialsId: 'supabase-key',        variable: 'SUPABASE_KEY'),
          string(credentialsId: 'supabase-project-id', variable: 'SUPABASE_PROJECT_ID')
        ]) {
          sh '''
            cat > .env <<EOF
VITE_SUPABASE_URL=${SUPABASE_URL}
VITE_SUPABASE_PUBLISHABLE_KEY=${SUPABASE_KEY}
VITE_SUPABASE_PROJECT_ID=${SUPABASE_PROJECT_ID}
EOF
            echo "Created .env for Vite build"
          '''
        }
      }
    }

    stage('Docker Info') {
      steps {
        sh 'docker version || true'
        sh 'docker compose version || true'
      }
    }

    stage('Down (default)') {
      steps {
        sh 'docker compose -f ${COMPOSE_FILE} down --remove-orphans || true'
      }
    }

    stage('Up (on demand)') {
      when { expression { params.MODE == 'up' } }
      steps {
        sh 'docker compose -f ${COMPOSE_FILE} up -d'
      }
    }

    stage('Smoke Test') {
      when { expression { params.MODE == 'up' } }
      steps {
        sh 'sleep 5 || true'
        sh 'curl -I http://localhost:8081 || true'
      }
    }
  }

  post {
    always {
      sh 'docker compose -f ${COMPOSE_FILE} ps || true'
    }
  }
}
