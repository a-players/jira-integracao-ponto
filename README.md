# Introduction

Jira plugin that integrates Tempo with the Norber system

# Development Environment

1. [Install Open JDK 8] (https://adoptopenjdk.net/releases.html)
2. [Install Atlassian SDK] (https://developer.atlassian.com/server/framework/atlassian-sdk/set-up-the-atlassian-plugin-sdk-and-build-a-project/)
3. Configure Maven to use Atlassian SDK, version and create ATLAS_HOME env:

## Linux:

    ```bash

    JAVA_HOME=/usr/lib/jvm/adoptopenjdk-8-hotspot/
    export JAVA_HOME
    M2_HOME=/usr/share/atlassian-plugin-sdk-8.2.8/apache-maven-3.5.4

    ATLAS_HOME=/usr/share/atlassian-plugin-sdk-8.2.8
    export ATLAS_HOME

    export PATH=${M2_HOME}/bin:${PATH}
    ```

## Windows:

    ```
    $JAVA_HOME='C:\adoptopenjdk-8-hotspot\'
    [Environment]::SetEnvironmentVariable('JAVA_HOME', $JAVA_HOME)

    $M2_HOME='C:\atlassian-plugin-sdk-8.2.8\apache-maven-3.5.4\'
    $PATH=[Environment]::GetEnvironmentVariable('PATH')
    $ATLAS_HOME='C:\atlassian-plugin-sdk-8.2.8\'
    [Environment]::SetEnvironmentVariable('ATLAS_HOME', $ATLAS_HOME)
    [Environment]::SetEnvironmentVariable('PATH', "${M2_HOME}bin;$PATH")
    ```

# Building plugin

## Linux:

    ```./build-bundle.sh
    ```

## Windows:

    ```./build-bundle.ps1
    ```

# Deploying plugin

1. Download the integracaoponto-x.x.x.jar file from folder backend/dist in this repository
2. Login to Jira with administrator privileges.
3. Navigate to 'Manage apps' and click on the link 'Upload app'
4. Select the downloaded jar
5. Click on upload
