// REF: https://<your-jenkins-server>/plugin/job-dsl/api-viewer/index.html
final fullRepoName = 'pingcap-inc/exterprise-extension'
final jobName = 'pr-verify'
final ciGroovyPath = "pipelines/${fullRepoName}/latest/pr-verify.groovy"

pipelineJob("${fullRepoName}/${jobName}") {
    logRotator {
        daysToKeep(30)
    }
    parameters {
        // Ref: https://docs.prow.k8s.io/docs/jobs/#job-environment-variables
        stringParam("BUILD_ID")
        stringParam("PROW_JOB_ID")
        stringParam("JOB_SPEC")
    }
    properties {
        // priority(0) // 0 fast than 1
        githubProjectUrl("https://github.com/${fullRepoName}")
    }
 
    definition {
        cpsScm {
            lightweight(true)
            scriptPath(ciGroovyPath)
            scm {
                git{
                    remote {
                        url('https://github.com/PingCAP-QE/ci.git')
                    }
                    branch('main')
                    extensions {
                        cloneOptions {
                            depth(1)
                            shallow(true)
                            timeout(5)
                        } 
                    }
                }
            }
        }
    }
}
