job('webapp-main') {
    description 'Main job'
	parameters {
		nodeParam('HOST') {
		defaultNodes(['master'])
		}
	}
    scm {
           git {
           		remote {
                	url('https://github.com/rimus93/webappwar')
            	}
				branch('*/master')
            }
	}
	steps {
		maven('clean deploy -DaltDeploymentRepository=nexus::default::http://nexus.localdomain/repository/maven-snapshots/')
		shell('tar -czpf webappwar-$BUILD_NUMBER.tar.gz $WORKSPACE/target/*.war')
		}
	publishers {
        sonar {}
        archiveArtifacts {
            pattern('*.tar.gz')
            onlyIfSuccessful()
        }
        extendedEmail {
            recipientList('rimus93@yandex.ru')
            triggers {
                failure {
                	subject('$DEFAULT_SUBJECT')
                    content('$DEFAULT_CONTENT')
                    sendTo {
                        recipientList()
                    }
                }
                stillUnstable {
                    subject('$DEFAULT_SUBJECT')
                    content('$DEFAULT_CONTENT')
                    sendTo {
                        recipientList()
                    }
                }
            }
		}
		downstream('webapp-slave', 'SUCCESS')
	}
}

job('webapp-slave') {
    description 'Deploy job'
	parameters {
		nodeParam('HOST') {
		defaultNodes(['slave1_fulldistr'])
		}
	}
    steps {
	shell('''
	cp /home/jenkins/.jenkins/workspace/webappwar/target/mywebapp.war /home/jenkins/.jenkins/workspace/webapp-slave/mywebapp.war
	mkdir -p /apps/wildfly19/standalone/backups
	rm -rf /apps/wildfly19/standalone/backups/*
	cp -R /apps/wildfly19/standalone/deployments/* /apps/wildfly19/standalone/backups/
	cp /home/jenkins/.jenkins/workspace/webapp-slave/mywebapp.war /apps/wildfly19/standalone/deployments/
	''')
	}
	publishers {
		extendedEmail {
            recipientList('rimus93@yandex.ru')
            triggers {
                failure {
                	subject('$DEFAULT_SUBJECT')
                    content('$DEFAULT_CONTENT')
                    sendTo {
                        recipientList()
                    }
                }
                stillUnstable {
                    subject('$DEFAULT_SUBJECT')
                    content('$DEFAULT_CONTENT')
                    sendTo {
                        recipientList()
                    }
                }
            }
		}
	downstream('webapp-verify', 'SUCCESS')
	}
}

job('webapp-verify') {
	description 'Verify deploy job'
	parameters {
		nodeParam('HOST') {
		defaultNodes(['slave1_fulldistr'])
		}
	}
	quietPeriod(60)
	steps {
		httpRequest('http://127.0.0.1:8090/mywebapp/') {
			httpMode('POST')
			returnCodeBuildRelevant()
			logResponseBody()
		}
	}
	publishers {
		extendedEmail {
            recipientList('rimus93@yandex.ru')
            triggers {
                failure {
                	subject('$DEFAULT_SUBJECT')
                    content('$DEFAULT_CONTENT')
                    sendTo {
                        recipientList()
                    }
                }
                stillUnstable {
                    subject('$DEFAULT_SUBJECT')
                    content('$DEFAULT_CONTENT')
                    sendTo {
                        recipientList()
                    }
                }
				success {
					subject('$DEFAULT_SUBJECT')
                    content('$DEFAULT_CONTENT')
                    sendTo {
                        recipientList()
                    }
				}
			}
		}
	}
}
