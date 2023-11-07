# Configuration
- When your application will run in different environments, and potentially for different customers, keep the environment- and customer- specific values outside the app.
> Tip 55 - Parameterize Your App Using External Configuration

- Common things you will probably want to put in configuration data include:
1. Credentials for external services (database, third party APIs, and so on)
2. Logging levels and destinations 
3. Port, IP address, machine, and cluster names the app uses Environment-specific validation parameters 
4. Externally set parameters, such as tax rates 
5. Site-specific formatting details
6. License keys

## STATIC CONFIGURATION 
- Currently YAML and JSON are popular for this.
- If the information is structured, and is likely to be changed by the customer (sales tax rates, for example), 
  - it might be better to store it in a database table. 
  - And, of course, you can use both, splitting the configuration information according to use.

- Commonly, this data structure is made global, the thinking being that this makes it easier for any part of the code to get to the values it holds.
  - Instead, wrap the configuration information behind a (thin) API.
  - This decouples your code from the details of the representation of configuration.

## CONFIGURATION-AS-A-SERVICE 
- rather than in a flat file or database, we’d like to see it stored behind a service API
1. Multiple applications can share configuration information, with authentication and access control limiting what each can see 
2. Configuration changes can be made globally 
3. The configuration data can be maintained via a specialized UI 
4. The configuration data becomes dynamic

- Dynamic configuration does not need to stop and restart an application.
- Using a configuration service, components of the application could register for notifications of updates to parameters they use, 
  - and the service could send them messages containing new values if and when they are changed.

## DON’T WRITE DODO-CODE 
- Don’t let your project (or your career) go the way of the dodo.
![dodo-bird.jpg](images%2Fdodo-bird.jpg)

> Don't Overdo It