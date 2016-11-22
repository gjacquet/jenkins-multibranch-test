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
import jenkins.model.JenkinsLocationConfiguration

def env = System.getenv()

JenkinsLocationConfiguration locationDescriptor = Jenkins.instance.getDescriptor(JenkinsLocationConfiguration)
locationDescriptor.url = env.JENKINS_URL
locationDescriptor.adminAddress = env.JENKINS_ADMIN_EMAIL
locationDescriptor.save()
