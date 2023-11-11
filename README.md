# goProController
This is a library made in kotlin to control goPro cameras follwing the documentation given in https://gopro.github.io/OpenGoPro/ble_2_0

At this moment the project still in developmnet with several features in TBD

This library, using the BLE tools of the Android framwork allow you to:
- Search nearby go pro cammeras.
- Connect to a selected camera
- Change mode between photo, video and time-lapse
- Change resolution
- Change frame rate of the video
- Change the speed of the video


The lower layer of the library called Infraetructure is in charge of the connection with the camera, write, read ble characteristics and subcribe to their changes. Also manages the wifi connection to acces to the media storage (TBD).

Also we have three feature packages inside the library: connection, commands and media
- Connection is in charge of connect with the camera.
- Commands is in charge of reading and changing the configuration of the camera
- Media is in charge of retrieving the media data (videos, photos, etc) (TBD)
 
