pipeline{
 		
    agent none


    options{
    	buildDiscarder(logRotator(numToKeepStr: '2',artifactNumToKeepStr: '1'))
    }

    stages {


	stage('Build') {
		agent {
          label 'debian_docker' && 'apache'
        }

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
    	  sh "mkdir -p /var/www/html/rectangles/"
    	  sh "cp dist/rectangle_${env.BUILD_NUMBER}.jar /var/www/html/rectangles/"	 
    	}
     }  

     stage('Running on Centos'){
     	agent{
     		docker 'centos:latest'
     	}

     	steps{
     		echo "running oncentos"
     		sh "wget http://192.168.1.3:9000/rectangles/rectangle_${env.BUILD_NUMBER}.jar"
     		sh "java -jar rectangle_${env.BUILD_NUMBER}.jar  2 10"
     	}
     }

    }




  }
