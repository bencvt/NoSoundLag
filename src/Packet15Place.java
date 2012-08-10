package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;

public class Packet15Place extends Packet
{
    private int xPosition;
    private int yPosition;
    private int zPosition;

    /** The offset to use for block/item placement. */
    private int direction;
    private ItemStack itemStack;

    /** The offset from xPosition where the actual click took place */
    private float xOffset;

    /** The offset from yPosition where the actual click took place */
    private float yOffset;

    /** The offset from zPosition where the actual click took place */
    private float zOffset;

    public Packet15Place() {}

    public Packet15Place(int x, int y, int z, int direction, ItemStack itemStack, float xOff, float yOff, float zOff)
    {
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
        this.direction = direction;
        this.itemStack = itemStack;
        this.xOffset = xOff;
        this.yOffset = yOff;
        this.zOffset = zOff;
        // begin modified code
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            // single player world, do nothing
        } else if (x == -1 && y == -1 && z == -1 && direction == 255) {
            // special case: using an item (e.g., eating)
            // just let the sound lag happen for this
        } else if (itemStack != null
                && itemStack.getItem() != null
                && itemStack.getItem() instanceof ItemBlock) {
            // placing a block: eliminate sound lag if we can
            int blockId = ((ItemBlock) itemStack.getItem()).getBlockID();
            if (blockId >= 0
                    && blockId < Block.blocksList.length
                    && Block.blocksList[blockId] != null
                    && Block.blocksList[blockId].stepSound != null) {
                // x/y/z are for the adjacent block, we want the actual block
                int actualX = x;
                int actualY = y;
                int actualZ = z;
                switch (direction) {
                case 0: actualY--; break; // below
                case 1: actualY++; break; // above
                case 2: actualZ--; break; // north
                case 3: actualZ++; break; // south
                case 4: actualX--; break; // west
                case 5: actualX++; break; // east
                }
                StepSound stepSound = Block.blocksList[blockId].stepSound;
                // parameter math copied from ItemBlock.tryPlaceIntoWorld()
                Minecraft.getMinecraft().sndManager.playSound(
                        stepSound.getStepSound(),
                        (float) actualX + 0.5F,
                        (float) actualY + 0.5F,
                        (float) actualZ + 0.5F,
                        (stepSound.getVolume() + 1.0F) / 2.0F,
                        stepSound.getPitch() * 0.8F);
                SoundMuffler.muffle(stepSound.getStepSound(), actualX, actualY, actualZ);
            }
        }
        // end modified code
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.xPosition = par1DataInputStream.readInt();
        this.yPosition = par1DataInputStream.read();
        this.zPosition = par1DataInputStream.readInt();
        this.direction = par1DataInputStream.read();
        this.itemStack = readItemStack(par1DataInputStream);
        this.xOffset = (float)par1DataInputStream.read() / 16.0F;
        this.yOffset = (float)par1DataInputStream.read() / 16.0F;
        this.zOffset = (float)par1DataInputStream.read() / 16.0F;
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.xPosition);
        par1DataOutputStream.write(this.yPosition);
        par1DataOutputStream.writeInt(this.zPosition);
        par1DataOutputStream.write(this.direction);
        writeItemStack(this.itemStack, par1DataOutputStream);
        par1DataOutputStream.write((int)(this.xOffset * 16.0F));
        par1DataOutputStream.write((int)(this.yOffset * 16.0F));
        par1DataOutputStream.write((int)(this.zOffset * 16.0F));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handlePlace(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 19;
    }

    public int getXPosition()
    {
        return this.xPosition;
    }

    public int getYPosition()
    {
        return this.yPosition;
    }

    public int getZPosition()
    {
        return this.zPosition;
    }

    public int getDirection()
    {
        return this.direction;
    }

    public ItemStack getItemStack()
    {
        return this.itemStack;
    }

    /**
     * Returns the offset from xPosition where the actual click took place
     */
    public float getXOffset()
    {
        return this.xOffset;
    }

    /**
     * Returns the offset from yPosition where the actual click took place
     */
    public float getYOffset()
    {
        return this.yOffset;
    }

    /**
     * Returns the offset from zPosition where the actual click took place
     */
    public float getZOffset()
    {
        return this.zOffset;
    }
}
