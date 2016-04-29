# DocumentalService

This project is an initial attempt to create a documental service that, in perspective, should provide an API (don't know what kind of) similar to the one used by systems like Google Drive or Microsoft OneDrive.

It's a joke for me and, now, i will use the readme as a diary. See you soon :)




Commit 29/04: I started to put some flesh on fire. I created a DocDrive class that, at this moment, is an entry point to the system. I mapped (nearly one-to-one) the methods in DocDrive with the method of a DAO interface. I instantiated this interface with a MongoDB implementation. So the project, at this stage, has a dependency with a mongo db instance (localhost:27017). I put nearly all the CRUD methods (miss the delete). I think this is starting to be fun :)

First Commit : By this first commit, it is simply a collection of methods that simulate the touch, mkdir, ls and cd commands of a Unix command line.
I used this approach to have an initial simply way to deal with file management and not to lost with devil early design optimization.
