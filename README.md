eReefs POJO Shared Library
==========================
<p align="right">
    <a href="https://github.com/open-AIMS/ereefs-pojo/actions" target="_blank">
        <img src="https://github.com/open-AIMS/ereefs-pojo/workflows/push/badge.svg"
             alt="Github Actions push status">
    </a>
    <a href="https://github.com/open-AIMS/ereefs-pojo/actions" target="_blank">
        <img src="https://github.com/open-AIMS/ereefs-pojo/workflows/release/badge.svg"
             alt="Github Actions push status">
    </a>
</p>


A shared library for accessing core eReefs POJOs (plain old Java objects) from a repository. A POJO
is an `Entity` (also referred to as a _Data Object_) within the system which captures the values
of a domain-specific object. This library provides both file-based (for development and testing)
and MongoDB-based (for Production and Test) implementations.


## Table of contents

- [Repository overview](#repository-overview)
- [Background](#background)
    - [POJOs](#background-pojo)
    - [Support Classes](#background-support)
- [Development](#dev)
    - [Guidelines](#dev-guidelines)
    - [Virtual Machine](#dev-vm)
    - [Supporting Services](#dev-services)
    - [Testing](#dev-testing)
- [Deployment](#deploy)
    - [Publish library](#deploy-publish)
    - [Using library](#deploy-use)

## <a id="repository-overview"></a>Repository overview
```
.
|-- env
|   |-- dev                             <-- Scripts useful for development/testing.
|   |-- services                        <-- Scripts and configuration for deploying services  
|                                           required for development/testing.
|-- src
|   |-- main
|       |-- java                        <-- Source code for the library.
|       |-- resources                   <-- Resources included in the packaged library.
|   |-- test
|       |-- java                        <-- Unit test cases.
|       |-- resources                   <-- Resources referenced by test cases.
|
|-- vagrant                             <-- Vagrant-specific files.
|-- pom.xml                             <-- The Maven definition file for this project.
|-- README.md                           <-- This file.
|-- Vagrantfile                         <-- The Vagrant VM definition for this project, that
                                            references the Vagrant-specific files in the 'vagrant'
                                            directory.
```

## <a id="background"></a>Background

This library groups a each [POJO or Entity](#background-pojo) (and any closely related
specialisations if applicable) into its own package, along with any
[support classes](#background-support) related to that POJO.

### <a id="background-pojo"></a>POJOs
This library provides support for a number of POJOs shared across applications in the AIMS eReefs
Platform:

- [Download Definition](#background-pojo-download-definition)
- [Product Definition](#background-pojo-product-definition)
- [Metadata](#background-pojo-metadata)
- [Job](#background-pojo-job)
- [Task](#background-pojo-task)

#### <a id="background-pojo-download-definition"></a>Download Definition
> package: [au.gov.aims.ereefs.pojo.definition.download](src/main/java/au/gov/aims/ereefs/pojo/definition/download)

Represents a source for downloadable datasets that are consumed by the AIMS eReefs Platform. Each
eReefs model consumed by the platform has a `Download Definition` entity, such as `GBR4_v2`,
`GBR4_BGC_924`, and `GBR1_2.0`.

#### <a id="background-pojo-product-definition"></a>Product Definition
> package: [au.gov.aims.ereefs.pojo.definition.product](src/main/java/au/gov/aims/ereefs/pojo/definition/product)

Represents an identifiable output of the AIMS eReefs Platform. For example, `Fresh Water Exposure
for GBR1 dataset`. A `Product` will normally consist of one or (normally) more files of varying
file type depending on the actual `Product`.

#### <a id="background-pojo-metadata"></a>Metadata
> package: [au.gov.aims.ereefs.pojo.metadata](src/main/java/au/gov/aims/ereefs/pojo/metadata)

Represents the metadata associated with a specific file or group of files in the system.

Examples:
- The `GBR4_v2` dataset downloaded from eReefs groups data by month, so each Netcdf file contains a
  month worth of data. Each file has a single Metadata entity.

#### <a id="background-pojo-job"></a>Job
> package: [au.gov.aims.ereefs.pojo.job](src/main/java/au/gov/aims/ereefs/pojo/job)

Represents a grouping of [Tasks](#background-pojo-task) that are to be executed. Currently, a `Job` is created
by a System Actor called the `Job Planner`, which implements a restriction where only one (1) active
`Job` can exist in the system at any time.

Status | Description
--- | ---
`created` | starting state signifying that the `Job` has been created (likely by the `Job Planner`) and is awaiting approval.
`approval_requested` | resting state signifying that the `Job` is awaiting approval by a team member, the timing of which is outside the control of the system.
`approved` | transient state signifying that the `Job` has been approved by a team member but execution has not yet commenced.
`denied` | end state signifying a team member chose to reject the approval request.
`running` | transient state signifying [Tasks](#background-pojo-task) are being executed.
`terminating` | transient state signifying that the [Tasks](#background-pojo-task) are being terminated, but the termination process has not yet completed.
`terminated` | end state signifying one or more [Tasks](#background-pojo-task) have completed with a status of `terminated`.
`completed` | end state signifying all [Tasks](#background-pojo-task) have completed with a status of `success`.
`failed` | end state signifying one or more [Tasks](#background-pojo-task) have completed with a status of `failed`.

#### <a id="background-pojo-task"></a>Task
> package: [au.gov.aims.ereefs.pojo.task](src/main/java/au/gov/aims/ereefs/pojo/task)

Represents a single executable instruction to be performed by a `TaskHandler` (a System Actor, such
as `ncAggregate` or `ncAnimate`). A `Task` will relate to a specific
[Product](#background-pojo-product-definition).

Significant properties of a `Task` are:

- `dependsOn` - a list of other `Tasks` that must be executed successfully prior to the `Task`
  being executed.
- `status` - see the table below.

Status | Description
--- | ---
`created` | starting state signifying that the `Task` was created (likely by the `Job Planner`) and is available for execution.
`assigned` | transient state signifying that the `Task` is available for processing by the next available `Task Handler` (eg: `ncAggregate` or `ncAnimate`), but execution has not yet commenced.
`running` | transient state signifying that execution has commenced.
`success` | end state signifying execution has completed without an error being reported by the `Handler`.
`failed` | end state signifying execution has stopped and the `Handler` has reported an error.
`terminated` | end state signifying an external actor (System or Human) has interrupted execution of the `Task`, either before or during execution.

### <a id="background-support"></a>Support Classes
Each POJO is accompanied by a number of interfaces and classes for reading, writing and possibly
manipulating the POJO. The following focused on the support interfaces/classes for the
[Metadata](#background-pojo-metadata) POJO for a concrete explanation.

- [Pojo](src/main/java/au/gov/aims/ereefs/pojo/Pojo.java) - the base implementation of a POJO,
  containing properties/methods applicable to all POJOs.
- [Metadata](src/main/java/au/gov/aims/ereefs/pojo/metadata/Metadata.java) - specialisation of the
  base POJO, adding properties/methods applicable to a Metadata POJO.
- [PojoDao](src/main/java/au/gov/aims/ereefs/pojo/PojoDao.java) - the base, generic interface for
  classes that provide read/write functionality related to the POJO. DAO stands for "Data Access
  Object". This interface defined methods applicable to all POJOs/DAOs.
- [MetadataDao](src/main/java/au/gov/aims/ereefs/pojo/metadata/MetadataDao.java) - specialised
  interface that adds methods applicable to interacting with Metadata POJOs.
- [MetadataDaoFileImpl](src/main/java/au/gov/aims/ereefs/pojo/metadata/MetadataDaoFileImpl.java) -
  specialisation of the MetadataDao interface for file-based persistence of MetadataPojos.
- [MetadataDaoMongoDbImpl](src/main/java/au/gov/aims/ereefs/pojo/metadata/MetadataDaoMongoDbImpl.java) -
  specialisation of the MetadataDao interface for MongoDB-based persistence of MetadataPojos.


## <a id="dev"></a>Development

### <a id="dev-guidelines"></a>Guidelines

Please follow the documented [Standard Github workflow](https://aimsks.atlassian.net/wiki/spaces/DEV/pages/1736911/Standard+Git+workflow)
and [Standard JIRA workflow](https://aimsks.atlassian.net/wiki/spaces/DEV/pages/7372886/Standard+JIRA+workflow)
when working on this project.

### <a id="dev-vm"></a>Virtual Machine
This repository includes files for provisioning a virtual machine (Vagrant using VirtualBox) for
Windows-based developers.

To provision the virtual machine defined in this project, Vagrant and VirtualBox must already be
installed (outside of the scope of this README). Once this pre-requisite is met, the VM is
provisioned with the following commands from the project root directory.
```bash
vagrant up
```
Once provisioning is complete, connect to the VM via SSH:
```bash
vagrant ssh
```

The remainder of this documentation will reference `<project root>`. When using the virtual machine,
this refers to the path `/vagrant`.

### <a id="dev-services"></a>Supporting Services
This repository includes scripts for starting and stopping a MongoDB instance for development and
testing purposes.

To start the MongoDB instance:
```bash
<project root>/env/services/mongodb/start.sh
```

To stop the MongoDB instance:
```bash
<project root>/env/services/mongodb/stop.sh
```

### <a id="dev-testing"></a>Testing
The `<project root>/src/test` folder defines test cases and data for comprehensive testing of the
library. Note that these tests require access to a MongoDB instance (see above).

To execute the tests:
```bash
<project root>/env/dev/maven-test.sh
```
This script executes the tests within a Maven Docker container that is connected to the same
Docker network as the MongoDB instance started above (that is: `ereefs-pojo-network`). The script
sets the environment variable `MONGODB_URL` accordingly:
```
MONGODB_URL="mongodb://pojo:pojo@ereefs-pojo-mongodb"
```
If the environmental variable is not specified, the default value is:
```
MONGODB_URL="mongodb://localhost:27017"
```
To use a different MongoDB instance, or to set up the IDE to access a different MongoDB, set/change
the environment variable accordingly.

## <a id="deploy"></a>Deployment

### <a id="deploy-publish"></a>Publish library
This repository uses [Github Actions](https://github.com/open-AIMS/ereefs-pojo/actions) for
publishing this library, and [Github Packages](https://github.com/open-AIMS/ereefs-pojo/packages) for
hosting the published library.

#### Git Push
When changes are pushed to Github, the [push](/.github/workflows/push.yml) Github Action is
executed. This workflow uses Maven to run the Tests defined for the project, with the results
published in the [Actions](https://github.com/open-AIMS/ereefs-pojo/actions) tab in Github.

#### Git Release
When development is complete, the library can be packaged and stored in
[Github Packages](https://github.com/open-AIMS/ereefs-pojo/packages) for use by other applications via
Maven.

1. Increment the version in [pom.xml](/pom.xml) based on the numbering convention of the project:

        <version>1.6.5</version>

   **WARNING:** the version in [pom.xml](/pom.xml) file must be unique, or this workflow will fail.

2. Commit and push the changes.

3. Use the [Github release](https://github.com/open-AIMS/ereefs-pojo/releases) mechanism to create
   and publish a release. This will execute the [release](/.github/workflows/release.yml) Github Action
   to package and deploy the library to [Github Packages](https://github.com/open-AIMS/ereefs-pojo/packages).
   Logs are published in the [Actions](https://github.com/open-AIMS/ereefs-pojo/actions) tab in Github.

### <a id="deploy-use"></a>Using library in an application
The following instructions apply ONLY to the application that is using this library via Maven, NOT to this
library.

1. Add the following snippet to the `pom.xml` file for the application, telling Maven where to find
   the packaged library:

        <repository>
            <id>github_openaims</id>
            <name>GitHub Open-AIMS Maven repo</name>
            <url>https://maven.pkg.github.com/open-AIMS/*</url>
        </repository>

2. Generate a [Github personal access token](https://github.com/settings/tokens). This is required by Maven to
   download the Github Package, even though the repository is Public. By default, Maven uses the file
   `$HOME/.m2/settings.xml` to contain credentials for accessing repositories. Add the following text to this
   file, remembering to replace the values for `username` and `password`.

        <server>
            <id>github_openaims</id>
            <username> replace with your username </username>
            <password> replace with your personal access token </password>
        </server>

   An example can be found in the [ncAggregate](https://github.com/open-AIMS/ereefs-ncaggregate) project.
