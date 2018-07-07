# Hyperdroid - Virtual Smartphone Over IP

## Introduction

Smartphones are getting smarter every day, so are the apps that run on them.Sophisticated apps need more computational power as compared to normal apps.Smartphones are getting more powerful but are not as ahead in development as the appsthat run on them. The hardware is not developing as quickly as the apps are getting developed. Although smartphones are capable of high computational tasks, but they
struggle too much in terms of providing computational power and running on full throttle drains the battery faster.

Another significant drawback of existing smartphone technology is the limitations imposed by the performance of smartphones. An analysis of the same has proved that the minor fraction of processing is caused due to rendering GUI by the CPU & GPU. While majority remains in the mathematical computation of the application running on the CPU & GPU. It can be inferred from above limitations that a distributed network architecture using virtualized resources could be used to solve the above problems. While choice of hardware being conventional desktops computer or servers as they are easily upgradable and significant improvement in CPU performance as compared to that of CPU integrated in modern smartphone.

## Implementation 

### Cloudlet Approach

A cloudlet is a trusted, resource rich; computer or collection of computers wellconnected to the internet. A cloudlet is available for use for nearby mobile devices.Rather than relying on a distant cloud, the resource poverty of a mobile device can be addressed by using a nearby resource-rich cloudlet. The need for real-time interactive response can be met by low latency, one-hop, and high-bandwidth wireless access to the cloudlet. The mobile device functions as a thin client, with all significant computation occurring in the nearby cloudlet. Physical proximity of the cloudlet is essential: the endto-end response time of applications executing in the cloudlet needs to be fast (few milliseconds) and predictable. For example, the nominal bandwidths of the fastest currently-available wireless LAN (802.11n) and wireless Internet (HSPDA) technologies are 400 Mbps and 2Mbps respectively. If no cloudlet is available nearby, the mobile device can gracefully degrade to a fallback mode that involves a distant cloud or, in the worst case, solely its own resources. Full functionality and performance can return later, when nearby cloudlet is discovered.

A cloudlet can be viewed as a data center in a box, it is self-managing requiring little more than power, Internet connectivity, and access control for setup. This simplicity of management corresponds to an appliance model of computing resources and makes it trivial to deploy on a business premises such as a coffee shop or a doctor's office. Internally, a cloudlet may be viewed as a cluster of multicore computers, with gigabit internal connectivity and a high-bandwidth wireless LAN.

### System Architecture

The idea of project is leveraging the massive processing power of the cloud for complex computations of smartphones. Linux based host machine runs multiple Virtual Machines, each Virtual Machine can only be engaged with a single user at a time. Firebase is used to store user’s as well as VM’s data. 
Detailed description of major components of architecture is as follows: 

```Firebase``` Firebase is a mobile and web application development platform owned by Google. Under the hood features of Hyperdroid such as user authentication, virtualmachine availability and dynamic allocation of virtual machines are implemented using firebase. A specially designed background service is responsible for capturing relevant objective information of VM in firebase. Objective information includes VM’s status and IP address provided to VM.

```Client``` Term client means the smartphone which user owns. Hyperdroid client app needs to be installed on the client device to interact with VM. Frames are rendered on client app.

```Virtual Machine``` Virtual Machines are used to run Android x86 images on the server. Each Virtual machine would be allocated to a single user at a time. If another user tries to connect to the engaged VM then the request would get rejected. Once the user logs out of the system VM gets free for another user. Allocation of VM to the user is completely dynamic. Oracle Virtual Box is hypervisor used to manage Virtual Machines. Multiple Virtual Machines which may or may not have same specifications can be created and managed.


**1. Setting up VNC server:** VNCserver is used to share interactive UI of the system in the cloud [7][8]. VNCviewer is used to interact with shared UI. VNC Client-Server provides facility to interact with the remote system in the cloud. DroidVNC is used as VNCserver in Hyperdroid implementation.

**2. Building Background service:** Specially designed background service called “Hyperdroid Background service” is built using Android Studio to handle background task. Background service checks the status of Virtual Machine and manages dynamic allocation of Virtual Machines to Users.

**3. Building Client application:** Hyperdroid Client application is built using Android Studio to access the system from the user’s smartphone.The user needs to install the application on the smartphone, the application provides following functionalities:
  - **New user Registration:** New User can register for service by entering the email ID and password in the app.
  - **User Login and Logout:** User can login using the same email ID and password used during registration. The user can log out from the system by pressing logout button in the menu.
  - **VNC Viewer:** User can instantly open the VNC viewer by clicking the Connect
button to access the VNC viewer. VNC Viewer is used interact with the system.

## Testing

**1. Building Benchmarking application:** A special benchmarking application called “Hyperdroid Benchmark” is developed using Android Studio to get performance score of various smartphone models and the system itself. The application would execute 4
algorithms. Each algorithm’s execution time would be calculated for their respective worst-case input. System evaluation is based on comparative study and analysis of the scores of the system and various smartphone models.

**2. Building Benchmarking II Application:** A special benchmarking application called “Hyperdroid Benchmark II - Image Stitching” is developed using Android Studio to get performance score of various smartphone models and the system itself. Since, previous benchmarking application couldn't exploit advantages of multiple processing cores this method is developed for fair evaluation. The designed application runs on multi-core processor to stitch images to form a single panoramic image. The evaluation parameter for this test is response time measures in milliseconds of benchmark 2 app running on Android x86 OS in the cloud looks on client device and
