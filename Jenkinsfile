pipeline{
 		
 		agent { label 'debian_docker' }



    stages {
	stage('Build') {
	    steps {
		echo 'Building.... '
		sh 'ant '
	    }

      }

    }


    post  {
    	always{
    		archiveArtifacts artifacts:  'dist/*.jar' ,fingerprint: true
    	}
    }

  }
