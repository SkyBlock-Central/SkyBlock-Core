# Contributing

## Before you contribute
- Please make sure that your feature is something that should be in a core mod.
- Please check that your feature/bug fix isn't already fixed on the main branch/in an open pull-request.
- Please make sure that your feature idea complies with the [Hypixel Rules](https://hypixel.net/rules). (See these Hypixel forum posts for extra information: [Mods in SkyBlock](https://hypixel.net/threads/regarding-the-recent-announcement-with-mods-in-skyblock.4045481/), [QoL Modifications](https://hypixel.net/threads/update-to-disallowed-modifications-qol-modifications.4043482/), [Modifications Sending Invalid Clicks](https://hypixel.net/threads/update-regarding-modifications-sending-invalid-clicks.5130489/)) 

## Setting up a development environment

- Install Java Development Kit version 17 [Eclipse Temurin Download](https://adoptium.net/temurin/releases) for convenience, however any JDK will do.
- Install Git. [Windows download](https://git-scm.com/download/win)
- Install an IDE, such as [Jetbrains IntelliJ IDEA](https://www.jetbrains.com/idea/download).

### Software configuration

- Fork the SkyBlock-Core repository using the fork button on top right of the page and name the repository SkyBlock-Core
- Clone the forked repository using `git clone https://github.com/<YourUsername>/SkyBlock-Core`.
  - After you've committed all the necessary changes, make a pull request to the main branch of [SkyBlock-Core](https://github.com/SkyBlock-Central/SkyBlock-Core)
  - Use the main branch as a way to pull the latest changes from the main repository.
  - Set your project SDK and Gradle JVM to your JDK 17.
      - For gradle, this can be done by searching for `gradle jvm` in the CTRL + SHIFT + A dialog in IntelliJ.
      - For your project SDK, this can be done in the module settings (CTRL + ALT + SHIFT + S) in IntelliJ.
  - If you don't have the minecraft sources installed, you can run the `genSources` gradle task.
  - Run the `Minecraft Client` task to make sure everything works.
      - Note: If IntelliJ tells you there's an error in the configuration, set the module to `SkyBlock-Core.main`.

### Logging into Hypixel in a development environment

Use [DevAuth](https://github.com/DJtheRedstoner/DevAuth). You do **not** need to download the jar, just follow the configuration instructions in the [DevAuth README](https://github.com/DJtheRedstoner/DevAuth#configuration-file

### Hot Reloading

Hot reloading is possible, to do that, you can run the `Minecraft Client` task with the IntelliJ debugger. Though hot-reloading can sometimes cause issues, so restarting will sometime be required.