FROM jenkins:2.19.2-alpine

USER root

RUN apk update \
    && apk add libstdc++ \
    && rm -rf /var/cache/apk/*

USER jenkins

RUN install-plugins.sh \
        job-dsl:1.53 \
        ec2:1.36 \
        slack:2.0.1 \
        junit:1.19 \
        xunit:1.102 \
        jacoco:2.1.0 \
        envinject:1.93.1 \
        toolenv:1.1 \
        build-timeout:1.17.1 \
        dependency-check-jenkins-plugin:1.4.4 \
        matrix-auth:1.4 \
        groovy:1.29 \
        grails:1.7 \
        parameterized-trigger:2.32 \
        nodejs:0.2.1 \
        sbt:1.5 \
        golang:1.1 \
        ghprb:1.33.1 \
        sonar:2.5 \
        rebuild:1.25 \
        timestamper:1.8.7 \
        ansicolor:0.4.2 \
        build-monitor-plugin:1.10+build.201610041454 \
        dashboard-view:2.9.10 \
        antisamy-markup-formatter:1.5 \
        github-organization-folder:1.5 \
        workflow-multibranch:2.9.2 \
        workflow-aggregator:2.4 \
        config-file-provider:2.13 \
        maven-plugin:2.14 \
        artifactory:2.8.1 \
        pipeline-maven:0.3 \
        pipeline-utility-steps:1.1.6 \
        active-directory:2.0 \
        pipeline-model-definition:0.5 \
        docker-workflow:1.9 \
        docker-commons:1.5 \
        build-user-vars-plugin:1.5 \
        ssh-slaves:1.11 \
        saml:0.12 \
        openid:2.2 \
        blueocean:1.0.0-b11

ENV JENKINS_URL http://somehost.example.com
ENV JENKINS_ADMIN_EMAIL gjacquet@example.com
ENV JENKINS_WEBHOOKS_BASE_URL http://somehost-hook.example.com

ENV GITHUB_ORGANIZATION gjacquet
ENV GITHUB_REPOSITORY jenkins-multibranch-test
ENV GITHUB_USER gjacquet
ENV GITHUB_TOKEN token
ENV GITHUB_WEBHOOK_PATH github-webhook

COPY src/main/groovy/*.groovy /usr/share/jenkins/ref/init.groovy.d/
