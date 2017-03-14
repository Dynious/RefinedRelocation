Refined Relocation
==============

# Further updates for Minecraft 1.7.10 are unlikely. For more recent versions, head over to [Refined Relocation 2](https://github.com/blay09/RefinedRelocation2).

This is the official GitHub page for Refined Relocation!

[Minecraft Forums page](http://www.minecraftforum.net/topic/2210752-/)

###Compiling Refined Relocation
***
[Setup Java](#setup-java)

[Setup Git](#setup-git)

[Setup Mercurial](#setup-mercurial)

[Setup Refined Relocation](#setup-refined-relocation)

####Setup Java
The Java JDK is used to compile Refined Relocation.

1. Download and install the Java JDK.
	* [Windows/Mac download link](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).  Scroll down, accept the `Oracle Binary Code License Agreement for Java SE`, and download it (if you have a 64-bit OS, please download the 64-bit version).
	* Linux: Installation methods for certain popular flavors of Linux are listed below.  If your distribution is not listed, follow the instructions specific to your package manager or install it manually [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).
		* Gentoo: `emerge dev-java/oracle-jdk-bin`
		* Archlinux: `pacman -S jdk7-openjdk`
		* Ubuntu/Debian: `apt-get install openjdk-7-jdk`
		* Fedora: `yum install java-1.7.0-openjdk`
2. Windows: Set environment variables for the JDK.
    * Go to `Control Panel\System and Security\System`, and click on `Advanced System Settings` on the left-hand side.
    * Click on `Environment Variables`.
    * Under `System Variables`, click `New`.
    * For `Variable Name`, input `JAVA_HOME`.
    * For `Variable Value`, input something similar to `C:\Program Files\Java\jdk1.7.0_51` (or wherever your Java JDK installation is), and click `Ok`.
    * Scroll down to a variable named `Path`, and double-click on it.
    * Append `;%JAVA_HOME%\bin` EXACTLY AS SHOWN and click `Ok`.  Make sure the location is correct; double-check just to make sure.
3. Open up your command line and run `javac`.  If it spews out a bunch of possible options and the usage, then you're good to go.  If not, try the steps again and make sure your `Path` variable is correct.

####Setup Git
Git is used to clone Refined Relocation and update your local copy.

1. Download and install Git [here](http://git-scm.com/download/).
	* *Optional*: Download and install a Git GUI client, such as Github for Windows/Mac, SmartGitHg, TortoiseGit, etc.  A nice list is available [here](http://git-scm.com/downloads/guis).

####Setup Mercurial
Mercurial is used to clone some dependencies of Refined Relocation.

1. Download and install Mercurial [here](http://mercurial.selenic.com/downloads/).

####Setup Refined Relocation
This allows ForgeGradle to know what it's compiling.

1. Open up your command line.
2. Navigate to a place where you want to download Refined Relocation's source (eg `C:\Github\RefinedRelocation\`) by executing `mkdir [folder location]` and then `cd [folder location]`.  This location is known as `mcdev` from now on.
3. Execute `git clone https://github.com/Dynious/RefinedRelocation.git`.  This will download Refined Relocation's source into `mcdev`.
4. Your directory structure should now look like:

***
	mcdev
	\-gradle
	\-src
		\-main
			\-java
			\-resources
	|-Other misc Refined Relocation files (should include `build.gradle`)
***

`5.` Open a command line in your ForgeGradle folder and execute `gradlew build` if using Windows.
	* Note: If you have [Gradle](http://www.gradle.org/) installed, use `gradle` instead.

`6.` Gradle will now install ForgeGradle, download all needed files and build Refined Relocation. This can take a while the first time.

`7.` Find your fresh copy of Refined Relocation in `mcdev/build/libs`!

###Contributing
####Submitting a Pull Request
1. If you haven't already, create a Github account.
2. Click the `Fork` icon at the top-right of this page (below your username).
3. Make the changes that you want to.
4. Click `Pull Request` at the right-hand side of the gray bar directly below your fork's name.
5. Click `Click to create a pull request for this comparison`, enter your PR's title, and make a description of what's changed.
6. Click `Send pull request`, and you're done!

####Creating an issue
1. Go to [the issues page](https://github.com/Dynious/RefinedRelocation/issues).
2. Click `New Issue` right below `Star` and `Fork`.
3. Enter your Issue's title (something that summarizes your issue), and then create a detailed description.
4. Click `Submit new issue`, and you're done!
