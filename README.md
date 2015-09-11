# goodbye-cameras

/*
 * Copyright (c) 2015 Ruibo Chen All rights reserved.
 * Ruibo Chen PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

What is this?
-------------

On the applications initial execution, it will fetch data from a website with addresses of known camera locations in nyc and it will build and propogate a database. 

The constants.txt file which contains database information that will allow the application to run properly.

The application features a simple GUI with support for dynamic queries. Ability to add and delete cameras is enabled. An archive log is hardcoded into the application to prevent inconsistencies in DUI transactions.

The addition of JxBrowser providers users with an interactive map of the camera locations.

Credits to TeamDev for the browser and photoenforced.com for the map and camera locations.



How to install application (Windows)
--------------------------
This installation assumes you have an update-to-date version of Java and has been configured in the environment variables otherwise, please refer to the last section of this tutorial.

1. Unpack GoodByeCameras.zip file into c:\GoodByeCameras

2. Press windows button and r to open Windows run command

3. type cmd and press enter to open Windows command prompt

4. type "cd c:\GoodByeCameras" in the windows command prompt

5. type "javac -cp c:/program/jxbrowser-5.4.jar;c:/program/license.jar;c:/program/mysql-connector-java-5.1.36-bin.jar;c:/program/rs2xml.jar *.java"

6. type "java -cp . App" constants.txt

7. and WALLAH!


How to use application
----------------------

1. Please login as root to have super privileges.

2. JComboBox allows you to perform searches or add cameras.

3. Go button executes the action in the JComboBox.

4. Load Visual Map button opens JxBrowser with a Google map of camera locations.

5. To delete, enter in a camera ID into the bottom right hand corner and hit delete.


Java Environment variable setup (Windows)
-----------------------------------------
1. First note down where your Java software installed. Go there and copy the path upto bin folder of JDK...
Like this.. C:\Program Files\Java\jdk1.8.0_45\bin 

Note: Your actual path may vary.. This is just an example.
 
2. Right click on your My Computer and select Properties. A window will appear.
 
3. Choose Advanced tab from that window.
 
4. Click on Environment Variables button located at the bottom of that window. That will bring a new Envrionment Variables window.
 
5. Double click on path variable located at System Variables as shown in below figure.  This will give you a small box type window.
 
6. In that box, go to Variable Value, and put a semicolon ( ; ) at the end of that value.
 
7. Now paste here the java/bin path you have copied.( e.g.C:\Program Files\Java\jdk1.6.0_13\bin)

It will look something like this. ..SystemRoot%;%SystemRoot%\System32\Wbem;C:\Program Files\Java\jdk1.6.0_13\bin
 
8. Now click.. OK.. OK.. and OK..  Thats it..
Now open the command prompt and type in javac.. if you see some java commands.. your path setting is success...
