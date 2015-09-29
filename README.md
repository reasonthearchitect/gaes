# Developing Golden Architecture Leveraging the Elastic Datastore

A shameless theft of the JHipster project with changes to meet a more enterprised micro service architecture.

Under development

Install npm and node.
Update npm and node

Run the command, from this folder':
sudo npm link

change to a different folder and run:
Start a project:
yo gaes

Create entity <ENTITY_NAME> being the name of your entity:
yo gaes:entity <ENTITY_NAME>

Create stack  <ENTITY_NAME> being the name of your entity:
yo gaes:stack <ENTITY_NAME>

type to run boot:
gradle

Go to 127.0.0.1:8080/v2/api-docs to se swagger.

127.0.0.1:8080/metrics for the metrics api

## Docker
You can build docker and run it locally with the following:

Note that you should find (if you are on Mac) the IP address that you are bound to as localhost will not work.

gradle buildDocker

To find your images:

docker images 

docker run -p 8888:8080 <yourImageName+snapshotRelease>

