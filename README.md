# aidl-sensor-demo
This is an Android application to demonstrate how to use AIDL for inter-process communication (IPC). 

This project includes an Android library with a Service ("SensorService") which is declared in the Manifest file to run in a separate process. This 
services does a simple task of registering a sensor value change listener and notifying the client (in this case an Activity belonging to the parent application) 
which runs in a separate process.

To this end, I have used two aidl files to define: 

1) ISensorServer: This is implemented by SensorService (or your own server), which clients use to set their instances of callback, which the SensorService can use to notify it. 
It also includes another method to read sensor value once only without setting callbacks (not used in demo).

2) ISensorServerCallback: This interface is implemented by the client (here MainActivity), and its instance is passed over to ISensorServer (the service). The service
then uses this to notify clients.

Some common pitfalls:

* The aidl files must be hosted in server and client project's "main/aidl" directory.
* The aidl files must be hosted in exactly same package in both client and server. e.g. ProjectName/src/main/aidl/com/yourOrg/package in both client and server.
* Don't implement the ISensorServerCallback on Activity class directly (broke my head over it). Instead create an instance of "ISensorServerCallback.Stub()" and pass
it as callback to service. Otherwise, callback (setCallback in this case) will never called on service.
* Use Handler to send the data received from Service (over AIDl) to the MainThread to update UI.
* Compiler will prompt you to define AIDL method call parameters as "in" or "out". Use them carefully else values will always be received as default values. For e.g.
in this demo the ISensorServerCallback has a method which accepts the sensor readings as parameters and sends to Application, so it is defined as "in" since it
is intialized/set by the service and being passed over to client.

