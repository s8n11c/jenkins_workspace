pipeline{
 		
    agent none


    options{
    	buildDiscarder(logRotator(numToKeepStr: '2',artifactNumToKeepStr: '1'))
    }

    stages {


	stage('Build') {
		agent {
          label 'debian_docker'
        }

	    steps {
		echo 'Building.... '
		sh 'ant '
	    }

	    post {
    	always{
    		archiveArtifacts artifacts:  'dist/*.jar' ,fingerprint: true
    		}
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




  }
