This is a sample project to demonstrate how to use the ElasticSearch Java API's.

This project uses the following:
- dropwizard v0.8
- dropwizard-swagger bundle v0.7
- Java v1.7
- ElasticSearch Java API v1.5.2
- Maven 3.2.5

This project has been tested with ElasticSearch Server v1.5.2.

Prerequisites:
- Java v1.7
- Maven v3.2.5 (If behind a firewall, update the conf/settings.xml in Maven to include the relevant proxy settings.)

To build the project: 
- Clone this repository.
- Ensure the path to Java and MAven are set.
- Run the command "mvn clean package" from the Elastic-Sample folder.
- Either use the IDE or execute the built fat JAR. Pass the following arguements "server local.yaml".

Tips:
- Run ElasticSearch Server locally
- Update Maven settings to download the dependencies to a local folder on disk.

Many thanks to [federecio](https://github.com/federecio) for the [dropwizard-swagger](https://github.com/federecio/dropwizard-swagger) bundle. It saves a lot of time
in integrating between dropwizard and swagger.
