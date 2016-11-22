import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import hudson.util.Secret
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl

def env = System.getenv()

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
