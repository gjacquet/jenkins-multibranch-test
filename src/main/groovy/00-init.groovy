import jenkins.model.Jenkins
import jenkins.model.JenkinsLocationConfiguration

def env = System.getenv()

JenkinsLocationConfiguration locationDescriptor = Jenkins.instance.getDescriptor(JenkinsLocationConfiguration)
locationDescriptor.url = env.JENKINS_URL
locationDescriptor.adminAddress = env.JENKINS_ADMIN_EMAIL
locationDescriptor.save()
