ElasticsearchJavaSample
=======================

This is a sample project to demonstrate the ElasticSearch Java API's. I use this project as part of trainings on ElasticSearch.

Project Details:
----------------
- dropwizard v0.8
- dropwizard-swagger bundle v0.7
- Java v1.7
- ElasticSearch Java API v1.5.2
- Maven v3.2.5

This project has been tested with ElasticSearch Server v1.5.2.

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
- Run ElasticSearch Server locally. Set up the cluster.name in elasticsearch.yml as "elasticsearch-local".
- Refer the [Elasticsearch Samples Gist](https://gist.github.com/rajanm/3fdbc7999f0120ce5e87) for scripts
to create indices and documents.
- Install and use the Head plugin in ElasticSearch to view the indices, documents, queries, cluster and node status etc.
- Update Maven settings to download the dependencies to a local folder on disk (instead of a network folder).
- If behind a firewall, update the proxy settings for Git client and Maven.

Thanks:
-------
- Many thanks to [federecio](https://github.com/federecio) for the [dropwizard-swagger](https://github.com/federecio/dropwizard-swagger) 
bundle. It saves a lot of time and issues in dropwizard and swagger integration.
