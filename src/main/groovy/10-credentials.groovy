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

import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import hudson.util.Secret
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl

def env = System.getenv()

def store = com.cloudbees.plugins.credentials.SystemCredentialsProvider.instance.store
def addCredential = { BaseStandardCredentials credentials ->
    def current = store.getCredentials(Domain.global()).find { it.id == credentials.id }
    if (current) {
        store.updateCredentials(Domain.global(), current, credentials)
    } else {
        store.addCredentials(Domain.global(), credentials)
    }
}

def addUsernamePasswordCredentials = { String id, String username, String password, String description = '' ->
    def replacement = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, id, description, username, password)
    addCredential(replacement)
}

def addStringCredentials = { String id, String secret, String description = '' ->
    def replacement = new StringCredentialsImpl(CredentialsScope.GLOBAL, id, description, Secret.fromString(secret))
    addCredential(replacement)
}

addUsernamePasswordCredentials('github-username-password', env.GITHUB_USERNAME, env.GITHUB_TOKEN, 'Github user with admin rights')
addStringCredentials('github-secret-text', env.GITHUB_TOKEN, 'Github token with admin rights')
addStringCredentials('github-webhook-shared-secret-text', env.GITHUB_WEBHOOK_SHARED_SECRET, 'Github shared secret for webhooks')
