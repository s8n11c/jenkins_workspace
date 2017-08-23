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

	    post {
    	success{
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
    	  sh "mkdir -p /var/www/html/rectangles/all"
    	  sh "cp dist/rectangle_${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all"	 
    	}
     }  

     stage('Running on Debian'){
     	agent{
     		docker { 
     		image 'debian_test'
     		label 'master'}
     	}

     	steps{
     		echo "running on debian container"
     		sh "wget http://192.168.1.3:9000/rectangles/all/rectangle_${env.BUILD_NUMBER}.jar"
     		sh "java -jar rectangle_${env.BUILD_NUMBER}.jar 2 33"

     	}
     }
	stage('Running on CentOS'){
     	agent{
     		docker { 
     		image 'cemmersb/centos-jdk8'
     		label 'master'}
     	}

     	steps{
     		echo "running on centos container"
     		sh "wget http://192.168.1.3:9000/rectangles/all/rectangle_${env.BUILD_NUMBER}.jar"
     		sh "java -jar rectangle_${env.BUILD_NUMBER}.jar 2 33"

     	}
     }


    stage('promot to green'){
    		
    		agent{
    			label 'apache'
    		}

    		steps{
    		sh 'cp /var/www/html/rectangles/all/rectangle_${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green/rectangle_${env.BUILD_NUMBER}.jar'
    		}

      }


    }




  }
