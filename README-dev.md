## Build instructions

If you just want to install the mod, skip this section. Just download the zip
file and follow the installation instructions in the main `README.md`.

 1. Install and configure [MCP](http://mcp.ocean-labs.de/index.php/MCP_Releases).
    Make sure you're able to recompile Minecraft before the next step.
 2. Copy everything from this repo's `src/` directory to
    `src/minecraft/net/minecraft/src/`
 3. Recompile, reobfuscate, and package the jar/zip.
    You can use the `build-nosoundlag.sh` script for this.

## Testing

If you're playing around with this mod and want to simulate a laggy connection,
you might find my [lagproxy](https://github.com/bencvt/lagproxy) utility useful.
Example:

 1. Start a standalone Minecraft server running on the standard port 25565
 2. Start the proxy, e.g.: `./lagproxy.py 9999 localhost 25565 0.5`
 3. Start your Minecraft client and add a server with the address localhost:9999
 4. Voilà, instant lag
