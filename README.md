Block Extenders
==============

This is the official GitHub page for Block Extenders!

[Minecraft Forums page](http://www.minecraftforum.net/topic/2210752-block-extenders/)

###Compiling Block Extenders
***
[Setup Java](#setup-java)

[Setup Gradle](#setup-gradle)

[Setup Git](#setup-git)

[Setup ForgeGradle](#setup-forgegradle)

[Setup Block Extenders](#setup-block-extenders)

[Compile Block Extenders](#compile-block-extenders)

####Setup Java
The Java JDK is used to compile Block Extenders.

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

####Setup Gradle
Gradle is used to execute the various build tasks when compiling Block Extender.

1. Download and install Gradle.
	* [Windows/Mac download link](http://www.gradle.org/downloads).  You only need the binaries, but choose whatever flavor you want.
		* Unzip the package and put it wherever you want, eg `C:\Gradle\Gradle-1.11`.
	* Linux: Installation methods for certain popular flavors of Linux are listed below.  If your distribution is not listed, follow the instructions specific to your package manager or install it manually [here](http://www.gradle.org/downloads).
		* Gentoo: `emerge dev-java/gradle-bin`
		* Archlinux: You'll have to install it from the [AUR](https://aur.archlinux.org/packages/gradle).
		* Ubuntu/Debian: `apt-get install gradle`
		* Fedora: Install Gradle manually from its website (see above), as Fedora ships a "broken" version of Gradle.  Use `yum install gradle` only if you know what you're doing.
2. Windows: Set environment variables for Gradle.
	* Go back to `Environment Variables` and then create a new system variable.
	* For `Variable Name`, input `GRADLE_HOME`.
	* For `Variable Value`, input something similar to `C:\Gradle-1.11` exactly as shown (or wherever your Gradle installation is), and click `Ok`.
	* Scroll down to `Path` again, append `;%GRADLE_HOME%\bin` EXACTLY AS SHOWN, and click `Ok`.  Once again, double-check the location.
3. Open up your command line and run `gradle`.  If it says "Welcome to Gradle [version].", then you're good to go.  If not, try the steps again.

####Setup Git
Git is used to clone Block Extenders and update your local copy.

1. Download and install Git [here](http://git-scm.com/download/).
	* *Optional*: Download and install a Git GUI client, such as Github for Windows/Mac, SmartGitHg, TortoiseGit, etc.  A nice list is available [here](http://git-scm.com/downloads/guis).

####Setup ForgeGradle
ForgeGradle allows Gradle to know what it needs to do.  This section assumes you know how to operate the command line.

1. Open up your command line.
2. Navigate to a place where you want to download ForgeGradle's source (eg `C:\Github\ForgeGradle\`) by executing `mkdir [folder location]` and then `cd [folder location]`.  This location is known as `mcdev` from now on.
3. Execute `git clone https://github.com/MinecraftForge/ForgeGradle.git`.  This will download Block Extender's source into `mcdev`.
4. Right now, you should have a directory structure that looks like:

***
	mcdev
	\-gradle
	\-src
	|-Other misc ForgeGradle files (should include `build.gradle`)
***

####Setup BlockExtenders
This allows ForgeGradle to know what it's compiling.

1. Clone Block Extenders just like you did with ForgeGradle.  All you have to change are the folder locations and `git clone` command.
2. Copy the `java` and `resources` folders from the Block Extenders source to `mcdev\src\main` (delete the folders that are already there).
3. Create a folder called `libs` in `mcdev` and put [BuildCraft](https://github.com/BuildCraft/BuildCraft), [Iron Chests](https://github.com/cpw/ironchest), [Industrial Craft 2](http://ic2api.player.to:8080/job/IC2_experimental/), [ComputerCraft](http://www.computercraft.info/download/), and [CoFHCore](http://teamcofh.com/index.php?page=downloads) in it.
	* Note: As Block Extenders is currently only for 1.6.4, please download 1.6.4 versions of the above mods.
4. Your directory structure should now look like:

***
	mcdev
	\-gradle
	\-src
		\-java
		\-resources
	\-libs
		\-Mods that you just downloaded
	|-Other misc ForgeGradle files (should include `build.gradle`)
***

`5.` Add this to `build.gradle` below the closing bracket of `minecraft`:

```
dependencies {
        compile fileTree(dir: 'libs')
}
```

`6.` Open a command line in your ForgeGradle folder and execute `gradlew build` or `gradlew.bat build` if using Windows.

`7.` Find your fresh copy of Block Extenders in `mcdev/build/libs`!

###Contributing
####Submitting a Pull Request
1. If you haven't already, create a Github account.
2. Click the `Fork` icon at the top-right of this page (below your username).
3. Make the changes that you want to.
4. Click `Pull Request` at the right-hand side of the gray bar directly below your fork's name.
5. Click `Click to create a pull request for this comparison`, enter your PR's title, and make a description of what's changed.
6. Click `Send pull request`, and you're done!

####Creating an issue
1. Go to [the issues page](https://github.com/Dynious/BlockExtenders/issues).
2. Click `New Issue` right below `Star` and `Fork`.
3. Enter your Issue's title (something that summarizes your issue), and then create a detailed description.
4. Click `Submit new issue`, and you're done!
