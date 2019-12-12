import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.dsl.GeneratedItems
import javaposse.jobdsl.dsl.ScriptRequest
import javaposse.jobdsl.plugin.JenkinsJobManagement
import org.junit.ClassRule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class JobScriptsSpec extends Specification {

    @Shared
    @ClassRule
    JenkinsRule jenkinsRule = new JenkinsRule()

    Map<String, List<String>> expectedJobs = [
        "testmavenjob.groovy" : [
            "testmavenjob"
        ]

    ]

    @Unroll
    def 'test script #file.name'(File file) {
        given:
        def jobManagement = new JenkinsJobManagement(System.out, [:], new File('.'))

        when:
        GeneratedItems generatedItems = new DslScriptLoader(jobManagement).runScripts([new ScriptRequest(file.text)])

        then:
        noExceptionThrown()
        expectedJobs[file.name] == generatedItems.jobs.collect { it.jobName }

        where:
        file << jobFiles
    }

    static List<File> getJobFiles() {
        List<File> files = []
        new File('jobs').eachFileRecurse {
            if (it.name.endsWith('.groovy')) {
                files << it
            }
        }
        files
    }
}
