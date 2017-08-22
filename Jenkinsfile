pipeline{
 		
    agent none


    options{
    	buildDiscarder(logRotator(numToKeepStr: '2',artifactNumToKeepStr: '1'))
    }

    stages {
    	agent {
          label 'debian_docker'
        }
	stage('Build') {
	    steps {
		echo 'Building.... '
		sh 'ant '
	    }
      }

    stage('Testing '){
    	agent {
          label 'debian_docker'
        }
    	steps {
    	echo 'testing'
    	sh 'ant -f test.xml -v '
    	junit 'reports/result.xml'
    	}	

      }  

    stage('Deploying'){
        agent {
          label 'apache'
        }

    	steps{
    	  echo 'deploying' 
    	  sh "cp dist/rectangle_.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/"	 
    	}
     }  

    }


    post  {
    	always{
    		archiveArtifacts artifacts:  'dist/*.jar' ,fingerprint: true
    	}
    }

  }
