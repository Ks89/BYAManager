# BYAManager

IMMAGINE LOGO

BYAManager is a personal project that i realized in 2011, and updated more times during the last years. It's a Download Manager dedicated to the Apple World, that i developed for the blog biteyourapple.net.
This software is a GUI to download iOS firmwares and iTunes versions with some extra features. 
It's legal, obviously, because all files are on Apple server. BYAManager bring files directly from Apple server for you.

The entire list of features is available [HERE](Link al div features)

BYAManager uses BYAUpdater to update itself automatically!

**This software requires Java 8 or above.**


## Changelogs
The complete list is available [HERE](LINK A README-CHANGELOG.md)


## News
- 04/06/2015 - **BYAMaager 0.8.0** [DOWNLOAD HERE](https://github.com/Ks89/BYAManager/releases/tag/v.0.8.0) and [HERE](http://ks89-jailbreak.blogspot.it/2015/04/byamanager-080.html)
- 04/03/2015 - **BYAMaager 0.7.0** [DOWNLOAD HERE](https://github.com/Ks89/BYAManager/releases/tag/v.0.7.0) and [HERE](http://ks89-jailbreak.blogspot.it/2015/04/byamanager-070.html)
- 04/02/2015 - **BYAMaager 0.6.0** public [HERE](http://ks89-jailbreak.blogspot.it/2015/04/bya-manager-060.html)
- 03/30/2015 - **BYAMaager 0.5.6** public [HERE](http://ks89-jailbreak.blogspot.it/2015/03/byamanager-056.html) 
- 03/29/2015 - **BYAMaager 0.5.5** public [HERE](http://ks89-jailbreak.blogspot.it/2015/03/changelog-0.html)
- 10/21/2014 - **BYAMaager 0.5.4** public [HERE](http://ks89-jailbreak.blogspot.it/2014/10/changelog-0.html)
- 09/29/2014 - **BYAMaager 0.5.3** public [HERE](http://ks89-jailbreak.blogspot.it/2014/09/byamanager-053.html)  
- 04/06/2012 - **BYAMaager 0.5.2** public [HERE](http://www.biteyourapple.net/2012/05/12/esclusiva-bya-bya-manager-0-5-2/#more-43579) and [HERE](http://ks89-jailbreak.blogspot.it/2012/04/anteprima-byamanager-052.html)
- 04/05/2012 - **BYAMaager 0.5.2 RC1** public via Twitter and auto-update
- 03/28/2012 - **BYAMaager 0.5.1** public [HERE](http://www.biteyourapple.net/2012/04/06/esclusiva-bya-bya-manager-0-5-1/#more-43275) and [HERE](http://ks89-jailbreak.blogspot.it/2012/03/anteprima-byamanager-051.html)
- 03/19/2012 - **BYAMaager 0.5.0.0** public [HERE](http://ks89-jailbreak.blogspot.it/2012/01/anteprima-byamanager-050.html)
- 03/19/2012 - **BYAMaager 0.5.0 beta 1** only for 3 beta testers. Yep! beta1 after a RC ;)
- 03/19/2012 - **BYAMaager 0.5.0 RC2** public via Twitter and auto-update
- 03/19/2012 - **BYAMaager 0.5.0 RC1** public via Twitter and auto-update
- 03/13/2012 - **BYAMaager 0.5.0.0 Beta5** only for beta testers
- 03/09/2012 - **BYAMaager 0.5.0.0 Beta3** only for beta testers 
- 02/24/2012 - **BYAMaager 0.4.1.0 (o 0.5.0.0 Beta2)** only for beta testers
- 01/06/2012 - **BYAMaager 0.4.0 (o Beta 4)** public [HERE](http://www.biteyourapple.net/2012/01/06/esclusiva-bya-biteyourapple-manager-0-4-0/) and [HERE](http://ks89-jailbreak.blogspot.it/2012/01/anteprima-byamanager-beta4.html)
- 12/28/2011 - **BYAMaager Beta 3** public [HERE](http://www.biteyourapple.net/2011/12/28/esclusiva-bya-biteyourapple-manager-beta-3/) and [HERE](http://ks89-jailbreak.blogspot.it/2011/10/bya-manager-beta3-preview.html)
- 10/24/2011 - **BYAMaager Beta 2** public [HERE](http://ks89-jailbreak.blogspot.it/2011/10/bya-manager-beta2.html) 
- 10/10/2011 - **BYAMaager Beta 1** public [HERE](http://ks89-jailbreak.blogspot.it/2011/10/bya-manager-beta1.html)
- 10/03/2011 - **BYAMaager Beta 0.3** private
- 08/04/2011 - **BYAMaager Alpha 4** private
- 07/31/2011 - **BYAMaager Alpha 3** private

## Features

####You can:
* download files via http and https
* download one or more files at the same time at full speed, with multiple processes to bypass server's limitations
* play/pause/stop/cancel downloads, using a list of available iOS Firmware and iTunes versions in a local database
* enable/disable sha1 check
* serach new BYAManager's versions or new database versions
* change settings, for example: path of the download folder, set a proxy, change measurement unit, and so no
* restore settings to default values
* open the download folder, directly from BYAManager
* re-download files in the list directly in the browser, or in other programs
* control some important features esecuting BYAManager via Command Line with a complete tutorial esecuting BYAManager.jar --help
* move a completed download into the iTunes folder (only on Mac and Windows) to be able to update/restore your device from iTunes without re-download the ipsw.
*

####BYAManager does:
* automatically check and update itself every 24 hours, with BYAUpdater
* automatically check and update the databse every 24 hours
* download files at full speed using multiple process
* check the interity os a file with hash algoririthm sha1.
* during the startup of the program, can try to restore incompleted/damaged files
* check if a file has been download before
* Log every actions in a file behind the executable called BYAManager.log
* 
* 

## Future extensions
* Create a database for device's commercial names, to remove the requirement to update the software every new iDevice produced
* Replace the Download button with an improved version
* Add a pattern to the sha check 
* Improve the logic to check the free space on the disk
* Create a custom GUI for the popup in the system tray
* Dinamically update the list of available firmwares and itunes version, without restart BYAManager
* Add in the GUI some informations to help the user to understand the state of every process
* Remove borders in the JWindow
* Create a new modern GUI to release BYAManager 1.0
* Add the donate button in the GUI
* Add social newtorks integration: twitter, facebook and so on


## Images


## Usage

#### Dowload files
1. Choose the tab if you want to download firmwares or itunes
2. Chosse che correct version
3. Click on Download to start

In the menu bar you can choose actions, like play/pause/cancel or the more powerfull versions: pause all/play all/cancel all.
Clicking on a table line, a popup menu will appear. From thi menu you can select actions, like disable SHA1-check, play/pause/cancel, open in broswer, open downlaoded folder.
When a download completes, you can move this file directly into the iTunes firmware's folder, clicking on the "Move" button in the table


####


#### Using Command Line Interface (CLI)


## License

Copyright 2011-2015 Stefano Cappa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

<br/>
**Created by Stefano Cappa**
