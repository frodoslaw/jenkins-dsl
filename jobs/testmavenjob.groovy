mavenJob('test-maven-job') {
    displayName('TestMavenJob')
    logRotator(-1, 1000, -1, 5)
    concurrentBuild(false)
    jdk('(Default)')
    label('java8')
    scm {
        git {
            remote {
                url('ssh://git@xx.yy.zz:2022/testmavenjob.git')
                // jenkins credentials
                credentials('d045fd4a-1dce-4b86-acd4-96d7b53c2293')
            }
            branches('**')
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    goals('clean install -Pci')
    wrappers {
        timeout {
            elastic(400, 3, 60)
            abortBuild()
        }
        timestamps()
        colorizeOutput('xterm')
    }
    publishers {
        warnings(['Maven'], [:]) {
            excludePattern()
        }
    }

    configure { project ->
        project / publishers << 'hudson.plugins.sonar.SonarPublisher' {
            installationName('sonar-new-v5')
        }
        project / publishers << 'org.jfrog.hudson.ArtifactoryRedeployPublisher' { 
            details {
                artifactoryUrl('https://artifactory.xx.yy.zz')
                artifactoryName('-750674546@1360664731248')
                repositoryKey('libs-release-local')
                snapshotsRepositoryKey('libs-snapshot-local')
            }
            deployBuildInfo(true)
            deployArtifacts(true)
            evenIfUnstable(true)
        }
        project / publishers << 'org.jenkinsci.plugins.stashNotifier.StashNotifier' {}
    }
}
