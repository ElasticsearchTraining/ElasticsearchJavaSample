[![Build Status](https://travis-ci.org/ElasticsearchTraining/ElasticsearchJavaSample.svg?branch=master)]() [![Coverage Status](https://coveralls.io/repos/ElasticsearchTraining/ElasticsearchJavaSample/badge.svg?branch=master&service=github)]() [![Heroku](https://heroku-badge.herokuapp.com/?app=elasticsearchjavasample&root=swagger/)]()

ElasticsearchJavaSample
=======================

This is a sample project to demonstrate the ElasticSearch Java API's. I use this project as part of trainings on ElasticSearch. 
I have developed the project using Codenvy (IDE), Travis CI (CI-CD), Coveralls (Coverage) and deployed it on Heroku (PaaS).

Project Details:
----------------
- dropwizard v0.8
- dropwizard-swagger bundle v0.7
- Java v1.7
- ElasticSearch Java API v1.7.1
- Maven v3.2.5

This project has been tested with ElasticSearch Server v1.7.1.

Prerequisites:
--------------
- Git Bash / Client
- Java v1.7
- Maven v3.2.5 
- cURL or Postman or similar browser extension

Build Steps: 
------------
- Clone this repository.
- Ensure the path to Java and Maven are set.
- Run the command "mvn clean package" from the Elastic-Sample folder.
- Either use the IDE or execute the built fat JAR. Pass the following program arguements "server local.yaml".
- Once the application is running, open [http://localhost/swagger](http://localhost/swagger) in the browser. Swagger
should appear as shown below.

![Swagger](swagger.png?raw=true "Swagger") 


Tips:
-----
- Run ElasticSearch Server locally. Ensure the cluster.name in elasticsearch.yml is "elasticsearch".
- Refer the [Elasticsearch Samples Gist](https://gist.github.com/rajanm/3fdbc7999f0120ce5e87) for scripts
to create indices and documents.
- Install and use the Head plugin in ElasticSearch to view the indices, documents, queries, cluster and node status etc.
- Update Maven settings to download the dependencies to a local folder on disk (instead of a network folder).
- If you are behind a firewall, update the proxy settings for Git client and Maven.

Thanks:
-------
- Many thanks to [federecio](https://github.com/federecio) for the [dropwizard-swagger](https://github.com/federecio/dropwizard-swagger) 
bundle. It saves a lot of time and issues in dropwizard and swagger integration.
