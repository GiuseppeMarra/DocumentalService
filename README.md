# DocumentalService

This project is an initial attempt to create a documental service that, in perspective, should provide an API (don't know what kind of) similar to the one used by systems like Google Drive or Microsoft OneDrive.

It's a joke for me and, now, i will use the readme as a diary. See you soon :)

**Commit 29/05**: After a month the project is changed a lot. 

Now multiple users are managed. 

A token authorization scheme has been implemented. Users, at the time of the login, will be provided with a JWT token. Since then, all the requests must be attached with a authorization header field in the form of "Bearer "+token. The token is used by the system for both authentication and authorization. The TokenManager (now simply implemented by a java class) keeps record of tokens, corresponding users and their allowed files (owned or shared with). No expiration scheme is implemented now, even though it will be a TODO in following releases. The token management will be transferred to a key-value in memory datastore (e.g. Redis). 

The file, now, contains information about its owner and other users it is shared with. However, the sharing features are not implemented yet (TODO).

The middleware model has been separated from the api model (both for performance and security reason).
Finally, a TestServices class has been provided; it can be used as test for the services but also as use case samples.


**Commit 29/04**: I started to put some irons in the fire. I created a DocDrive class that, at this moment, is an entry point to the system. I mapped (nearly one-to-one) the methods in DocDrive with the method of a DAO interface. I instantiated this interface with a MongoDB implementation. So the project, at this stage, has a dependency with a mongo db instance (localhost:27017). I put nearly all the CRUD methods (miss the delete). I think this is starting to be fun :)

**First Commit** : By this first commit, it is simply a collection of methods that simulate the touch, mkdir, ls and cd commands of a Unix command line.
I used this approach to have an initial simply way to deal with file management and not to lost with devil early design optimization.
