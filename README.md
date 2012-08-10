Basic usage
-----------
When playing multiplayer Minecraft, are you annoyed that when you place a block,
it takes a second for the sound to happen? Or that the sound of your own
footsteps lag behind? Install this mod and the sound lag will disappear.

Full description
----------------
Minecraft 1.3 involved a lot of internal changes to reduce the amount of server
code duplicated by the client. One unfortunate side effect is that if you're on
a noticeably laggy connection, your block placement and footstep sounds will
lag as well. The server controls those sounds now, not the client.

Hopefully Mojang will fix this soon (1.3.2 is coming). A proper fix will involve
changing both the server and the client.

In the meantime, this client mod exists. It does not require any server-side
modifications, so it's not perfect: a stray extra footstep might sneak through
from time to time.

Installation
------------
Works exactly like any other Minecraft client mod.

Patch the contents of the zip file into your minecraft.jar, being sure to remove
remove the META-INF folder. I recommend a utility like [Magic Launcher](http://www.minecraftforum.net/topic/939149-launcher-magic-launcher-098-mods-options-news/);
manually copying .class files is for the birds.

This mod does *not* require ModLoader or any other mod management system.

Compatibility
-------------
Should be compatible with every mod that does not overwrite:
- atf.class (EntityClientPlayerMP)
- eg.class (Packet15Place)
- cr.class (Packet62LevelSound)

Build instructions
------------------
If you just want to install the mod, skip this section. Just download the zip
file and follow the installation instructions above.
 1. Install and configure [MCP](http://mcp.ocean-labs.de/index.php/MCP_Releases).
    Make sure you're able to recompile Minecraft before the next step.
 2. Copy everything from this repo's `src/` directory to
    `src/minecraft/net/minecraft/src/`
 3. Recompile, reobfuscate, and package the jar/zip.
    You can use the `build-nosoundlag.sh` script for this.

Testing
-------
If you're playing around with this code and want to simulate a laggy connection,
you might find my [lagproxy](https://github.com/bencvt/lagproxy) utility useful.
Example:
 1. Start a standalone Minecraft server running on the standard port 25565
 2. Start the proxy, e.g.: `./lagproxy.py 9999 localhost 25565 0.5`
 3. Start your Minecraft client and add a server with the address localhost:9999
 4. Voilà, instant lag
