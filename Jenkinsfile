pipeline{
 		
    agent none
    environment{
    	MAJOR_VERSION = 1
    }

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
    	  sh "cp dist/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all/${env.BRANCH_NAME}/"	 
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
     		sh "wget http://192.168.1.3:9000/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
     		sh "java -jar rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar 2 33"

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
     		sh "wget http://192.168.1.3:9000/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
     		sh "java -jar rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar 2 33"
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
    		sh 'mkdir -p  /var/www/html/rectangles/green/'
    		sh "cp /var/www/html/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green/"
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
        echo  'checking out'
	        sh 'git checkout development'
        sh 'git pull origin'
      
        sh 'git checkout master'

        sh 'git merge development'
        sh 'git push origin master'
        echo  'tagging '
        sh "git tag rectangle-${MAJOR_VERSION}.${env.BUILD_NUMBER}"
        sh "git push origin rectangle-${MAJOR_VERSION}.${env.BUILD_NUMBER}"
        	} 
    	}


	}

	post {
        success {
            emailext(
                subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] Ran!",
                body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]' Ran!":</p>
                    <p>Check console output at <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]/a></p>""",
                to: "s8n11c@gmail.com"
            )
        }
        }



  }
