BlockExtenders
==============

This is the official GitHub page for Block Extenders!

[Minecraft Forums page](http://www.minecraftforum.net)

###Compiling (with ForgeGradle)
1. Get a fresh install of ForgeGradle
2. Put the `java` and `resources` folders in `ForgeGradleDir/src/main` (delete the folders that are already in there)
3. Create a folder called `libs` in your ForgeGradle folder and put BuildCraft IndustrialCraft and CoFHCore in it
4. Add this to the `build.gradle` file:
```
dependencies {
        compile fileTree(dir: 'libs', include: '*.jar')
}
```
5. Open a commandprompt in your ForgeGradle folder and execute this: `gradlew build`
6. Find your fresh copy of Block Extenders in `ForgeGradleDir/build/libs`!

###Contributing
####Submitting a Pull Request
1. If you haven't already, create a Github account.
2. Click the `Fork` icon at the top-right of this page (below your username).
3. Make the changes that you want to.
4. Click `Pull Request` at the right-hand side of the gray bar directly below your fork's name.
5. Click `Click to create a pull request for this comparison`, enter your PR's title, and make a description of what's changed.
6. Click `Send pull request`, and you're done!

####Creating an issue
1. Go to [the issues page](http://github.com/pahimar/Equivalent-Exchange-3/issues).
2. Click `New Issue` right below `Star` and `Fork`.
3. Enter your Issue's title (something that summarizes your issue), and then create a detailed description.
4. Click `Submit new issue`, and you're done!
