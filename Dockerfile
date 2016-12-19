FROM jenkins:2.19.2-alpine

USER root

RUN apk update \
    && apk add libstdc++ \
    && rm -rf /var/cache/apk/*

USER jenkins

RUN install-plugins.sh \
        branch-api:2.0.0-beta-1 \
        scm-api:2.0.1-beta-1 \
        github-branch-source:2.0.0-beta-1 \
        workflow-multibranch:2.10-beta-1 \
        workflow-aggregator:2.4

ENV JENKINS_URL http://somehost.example.com
ENV JENKINS_ADMIN_EMAIL gjacquet@example.com
ENV JENKINS_WEBHOOKS_BASE_URL http://somehost-hook.example.com

ENV GITHUB_ORGANIZATION gjacquet
ENV GITHUB_REPOSITORY jenkins-multibranch-test
ENV GITHUB_USER gjacquet
ENV GITHUB_TOKEN token
ENV GITHUB_WEBHOOK_PATH github-webhook
ENV GITHUB_WEBHOOK_SHARED_SECRET topsecret

COPY src/main/groovy/*.groovy /usr/share/jenkins/ref/init.groovy.d/
