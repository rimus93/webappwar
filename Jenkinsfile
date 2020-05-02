node ('master') {    
      stage('Creating Jobs') {
        build job: 'seedjob'
      }
    
      stage('Preparation (Checking out)') {
        echo ("-> Preparation stage started.")
        git branch: 'master', url: 'https://github.com/rimus93/webappwar'
        echo ("-> Preparation stage finished.")
    }
