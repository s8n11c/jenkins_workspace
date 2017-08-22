pipeline{
    agent any
   



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
    		archive 'dist/*.jar'
    	}
    }

  }
