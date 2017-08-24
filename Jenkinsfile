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
    	  sh "mkdir -p /var/www/html/rectangles/all/${env.BRANCH_NAME}"
    	  sh "cp dist/rectangle_${env.BRANCH_NAME}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all/${env.BRANCH_NAME}/"	 
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
     		sh "wget http://192.168.1.3:9000/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.BRANCH_NAME}.${env.BUILD_NUMBER}.jar"
     		sh "java -jar rectangle_${env.BRANCH_NAME}.${env.BUILD_NUMBER}.jar 2 33"

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
     		sh "wget http://192.168.1.3:9000/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.BRANCH_NAME}.${env.BUILD_NUMBER}.jar"
     		sh "java -jar rectangle_${env.BRANCH_NAME}.${env.BUILD_NUMBER}.jar 2 33"
     		echo "+ [B]ranch is ${env.BRANCH_NAME}"

     	}
     }


    stage('promote to green'){
    		
    		agent{
    			label 'apache'
    		}
    		when {
    			branch 'master'
    		}
    		steps{
    		sh 'mkdir /var/www/html/rectangles/green'
    		sh 'cp /var/www/html/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.BRANCH_NAME}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green'
    		}

      }



     stage('Promote Development Branch to Master') {
      agent {
        label 'apache'
      }
      when {
        branch 'development'
      }
      steps {
        echo "Stashing Any Local Changes"
        sh 'git stash'
        echo "Checking Out Development Branch"
        sh 'git checkout development'
        echo 'Checking Out Master Branch'
        sh 'git pull origin'
        sh 'git checkout master'
        echo 'Merging Development into Master Branch'
        sh 'git merge development'
        echo 'Pushing to Origin Master'
        sh 'git push origin master'
        	} 
    	}
	}



  }
