# Description

Simple PoC to demonstrate https://issues.jenkins-ci.org/browse/JENKINS-38277

Starting a docker container from the Dockerfile found in this repo will configure jenkins and create a multibranch
project.

With this configuration Jenkins will only register `PULL_REQUEST` and `REPOSITORY` events on the webhook. `PUSH` is not
currently registered.

![Webhook screenshot](https://raw.githubusercontent.com/gjacquet/jenkins-multibranch-test/master/images/hooks.png)

N.B. for some reason hooks are not registered on startup and you need to go to configuration and hit "Re-register hooks for all jobs" button.

At this point Jenkins logs will show entries like these ones:

```
Nov 22, 2016 11:09:01 PM org.jenkinsci.plugins.github.config.GitHubPluginConfig doReRegister
INFO: Called registerHooks() for 1 jobs
Nov 22, 2016 11:09:01 PM org.jenkinsci.plugins.github.webhook.WebhookManager$1 run
INFO: GitHub webhooks activated for job multibranch-test/master with [GitHubRepositoryName[host=github.com,username=gjacquet,repository=jenkins-multibranch-test]] (events: [PULL_REQUEST, REPOSITORY])
```

# Building
 
Simply run `docker build` in the root of the repository. E.g.

```bash
docker build . -t jenkins-guillaume
```


# Running

You need to define a number of environment variables for the proof of concept to work:
* GITHUB_ORGANIZATION: the organization (or github user) for which you want to create the project
* GITHUB_REPOSITORY: the name of the repository for which you want to create the project
* GITHUB_USER: the name of the github user scanning and fetching from github during the build
* GITHUB_TOKEN: the token to use for creating webhooks, scanning repositories and fetching sources
* JENKINS_WEBHOOKS_BASE_URL: the base url of the webhook. It must be accessible from github. I used ngrok for my test.

E.g.

```bash
docker run --rm \
    -p 8080:8080 \
    -e GITHUB_ORGANIZATION=gjacquet \
    -e GITHUB_REPOSITORY=jenkins-multibranch-test \
    -e GITHUB_USER=gjacquet \
    -e GITHUB_TOKEN=1234yourowntoken1234 \
    -e JENKINS_WEBHOOKS_BASE_URL=https://12345678.ngrok.io \
    jenkins-guillaume
```
Toto
