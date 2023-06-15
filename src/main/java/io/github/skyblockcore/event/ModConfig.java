package io.github.skyblockcore.event;

public class ModConfig {
    private boolean dev;
    private boolean unknownPlayer;
    private boolean location;

    public boolean isDev() {
        return dev;
    }
    public boolean isUnknownPlayer() {
        return unknownPlayer;
    }
    public boolean isLocation() {
        return location;
    }


    public void setDev(boolean dev) {
        this.dev = dev;
    }
    public void setUnknownPlayer(boolean unknownPlayer) {
        this.unknownPlayer = unknownPlayer;
    }
    public void setLocation(boolean location) {
        this.location = location;
    }
}


/*
* Hi, and welcome to the config setup process
* here we keep all the cool things that will create the config lines
* the config is located in .minecraft/config/SkyblockCoreConfig.json
*
* How to make a NEW config line (for purposes we will be creating a new value called "thing")
*
* 1: delete the current config!

* 2: go to SkyBlockCore and add a line at 77

config.setThing(false);


* 3: pick if you want it to build as true, or false THINK OF THE END USER!!!

* 4: Above duplicate each thing you see for your new value

   private boolean thing;

    public boolean isThing() {
        return thing;
    }

  public void setThing(boolean thing) {
        this.thing = thing;
    }


 * okay now you should have a usable thing!
 * To use do this
 * 5:
             ModConfig config = SkyBlockCore.getThing();
            if (config != null && config.isThing()) {
                wow you just made a config value!
            }
 + That's it! you've made a new config value, now follow the contributing guide on how to open a pr and show us your work!
 */