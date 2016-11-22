import groovy.xml.MarkupBuilder

import java.nio.charset.StandardCharsets

def env = System.getenv()

def writer = new StringWriter()
def xml = new MarkupBuilder(writer)

xml.'org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject' {
    icon(class: 'com.cloudbees.hudson.plugins.folder.icons.StockFolderIcon')

    healthMetrics {
        'com.cloudbees.hudson.plugins.folder.health.WorstChildHealthMetric'()
    }

    triggers {
        'com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger' {
            spec('* * * * *')
            interval(86400000)
        }
    }

    sources(class: 'jenkins.branch.MultiBranchProject$BranchSourceList') {
        data {
            'jenkins.branch.BranchSource' {
                source(class: 'org.jenkinsci.plugins.github_branch_source.GitHubSCMSource') {
                    id(UUID.randomUUID())
                    checkoutCredentialsId('github-username-password')
                    scanCredentialsId('github-username-password')
                    repoOwner(env.GITHUB_ORGANIZATION)
                    repository(env.GITHUB_REPOSITORY)
                    includes('*')
                    buildOriginBranch(true)
                    buildOriginBranchWithPR(true)
                    buildOriginPRMerge(false)
                    buildOriginPRHead(false)
                    buildForkPRMerge(true)
                    buildForkPRHead(false)
                }
                strategy(class: 'jenkins.branch.DefaultBranchPropertyStrategy') {
                    properties(class: 'empty-list')
                }
            }
        }
        owner(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject', reference: '../..')
    }
    factory(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory') {
        owner(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject', reference: '../..')
    }
    orphanedItemStrategy(class: 'com.cloudbees.hudson.plugins.folder.computed.DefaultOrphanedItemStrategy') {
        pruneDeadBranches(true)
        daysToKeep(2)
        numToKeep(20)
    }
}

def stream = new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8))
jenkins.model.Jenkins.instance.createProjectFromXML('multibranch-test', stream)
