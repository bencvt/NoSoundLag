When playing multiplayer Minecraft, are you annoyed that when you place a block,
it takes a second for the sound to happen? Or that the sound of your own
footsteps lag behind? Install this mod and the sound lag will disappear.

## Installation

Works exactly like any other Minecraft client mod.

Patch the contents of the zip file into your minecraft.jar, being sure to remove
remove the META-INF folder. I recommend a utility like
[Magic Launcher](http://www.minecraftforum.net/topic/939149-magiclauncher/);
manually copying .class files is for the birds.

This mod does *not* require ModLoader or any other mod management system.

## Compatibility

Should be compatible with every mod that does not overwrite:

- `axc.class` (EntityClientPlayerMP)
- `fc.class` (Packet15Place)
- `dn.class` (Packet62LevelSound)

## More info

Minecraft 1.3 involved a lot of internal changes to reduce the amount of server
code duplicated by the client. One unfortunate side effect is that if you're on
a noticeably laggy connection, your block placement and footstep sounds will
lag as well. The server controls those sounds now, not the client.

A proper fix will involve changing both the server and the client, which doesn't
appear to be coming from Mojang right away. There is an official bug report
filed for it: **[MC-30](https://mojang.atlassian.net/browse/MC-30)**.

In the meantime, this client mod exists. It does not require any server-side
modifications, so it's not perfect: a stray extra footstep might sneak through
from time to time.

NoSoundLag is open source! Check the official GitHub project for the changelog,
build instructions, and full source code:
[github.com/bencvt/NoSoundLag](https://github.com/bencvt/NoSoundLag)
