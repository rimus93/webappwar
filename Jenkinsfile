pipeline{
	agent none
	stages {
	
		stage('Building jobs') {
			steps {
				build 'seedjob'
			}
	agent {
		label 'master'
	}
		}
		
		stage('Building and testing') {
			steps {
				build 'webapp-main'
			}
	agent {
		label 'master'
	}
		}
		
		stage('Deploy') {
			steps {
				build 'webapp-slave'
			}
	agent {
		label 'slave'
	}
		}
		
		stage('Verify') {
			steps {
				build 'webapp-verify'
			}
	agent {
		label 'master'
	}
		}

	}
}
