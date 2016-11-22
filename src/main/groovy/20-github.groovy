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

import jenkins.model.Jenkins
import org.jenkinsci.plugins.github.config.GitHubPluginConfig
import org.jenkinsci.plugins.github.config.GitHubServerConfig
import org.jenkinsci.plugins.github.config.HookSecretConfig

def env = System.getenv()

def descriptor = (GitHubPluginConfig) Jenkins.instance.getDescriptor(GitHubPluginConfig.GITHUB_PLUGIN_CONFIGURATION_ID)
descriptor.overrideHookUrl = true
descriptor.hookUrl = new URL("${env.JENKINS_WEBHOOKS_BASE_URL}/${env.GITHUB_WEBHOOK_PATH}/")
descriptor.hookSecretConfig = new HookSecretConfig('github-webhook-shared-secret-text')

def config = new GitHubServerConfig('github-secret-text')
config.manageHooks = true
config.apiUrl = ''
config.clientCacheSize = 25

descriptor.configs = [ config ]

descriptor.save()
