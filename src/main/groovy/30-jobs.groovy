/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import groovy.xml.MarkupBuilder

import javax.xml.transform.stream.StreamSource
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
                    excludes()
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
def jobName = 'multibranch-test'
def job = jenkins.model.Jenkins.instance.getItemByFullName(jobName, hudson.model.AbstractItem)
if (!job) {
    jenkins.model.Jenkins.instance.createProjectFromXML(jobName, stream)
} else {
    job.updateByXml(new StreamSource(stream))
}
