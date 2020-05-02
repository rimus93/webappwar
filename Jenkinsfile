pipeline{
	agent none
	stages {
		stage('Building jobs') {
			agent {
				node {
				label 'master'
				}
			}
			steps {
				build 'seedjob'
			}
		}
		
		stage('Building and testing') {
			agent {
				node {
				label 'master'
				}
			}
			steps {
				build 'webapp-main'
			}
		}
		
		stage('Deploy') {
			agent {
				node {
				label 'slave'
				}
			}
			steps {
				build 'webapp-slave'
			}
		}
		
		stage('Verify') {
			agent {
				node{
				label 'master'
				}
			}
			steps {
				build 'webapp-verify'
			}
		}

	}
}




pipeline{
	agent none
	stages {
		stage('Building jobs') {
			steps {
				build 'seedjob'
			}
	agent {
		node {
		label 'master'
		}
	}
		}
		
		stage('Building and testing') {
			steps {
				build 'webapp-main'
			}
	agent {
		node{
		label 'master'
		}
	}
		}
		
		stage('Deploy') {
			steps {
				build 'webapp-slave'
			}
	agent {
		node{
		label 'slave'
		}
	}
		}
		
		stage('Verify') {
			steps {
				build 'webapp-verify'
			}
	agent {
		node{
		label 'master'
		}
	}
		}

	}
}
