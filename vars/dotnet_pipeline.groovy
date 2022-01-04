
def call() {
    
    pipeline {
        agent any
        
//         environment {
//             projFile = "/Users/anthony/Projects/RiderProjects/otandp-app-server/His.API.ForMobile/His.API.ForMobile.csproj"
//             projPublishDir = "/Users/anthony/Projects/RiderProjects/otandp-app-server/His.API.ForMobile/bin/Release/netcoreapp3.1/publish/"
//             projVersionFile = "/Users/anthony/Projects/RiderProjects/otandp-app-server/His.API.ForMobile/bin/Release/netcoreapp3.1/publish/His.API.ForMobile.dll"
//             projDeployDir = "/Users/anthony/Deployment/MAPP/His.API.ForMobile/"
//         }
        
        stages{
    
            stage('Restore packages') {
                steps {
                  sh """
                     dotnet restore ${env.projFile}
                  """
                }
            }
            
            stage('Clean') {
                steps {
                    sh """
                       dotnet clean ${env.projFile}
                    """
                }
            }
               
            stage('Build') {
               steps{
                  sh """
                     dotnet build ${env.projFile} -c Release
                  """
                }
            }
             
            stage('Publish'){
                 steps{
                   sh """
                      dotnet publish ${env.projFile} -c Release
                   """
                }
            }
            
            stage('Archive') {
                steps {
                    script {
                        version = sh(returnStdout: true, script: "exiftool ${env.projVersionFile} | grep -i 'file version' | tail -1 | cut -d \":\" -f 2").trim()
                        sh "cp -R ${env.projPublishDir} ${env.projDeployDir}v${version}"
                    }
                }
                
            }
        }
     }
 }
 
 return this;