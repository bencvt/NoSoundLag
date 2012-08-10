package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Packet62LevelSound extends Packet
{
    /** e.g. step.grass */
    private String soundName;
    private int field_73577_b;
    private int field_73578_c = Integer.MAX_VALUE;
    private int field_73575_d;

    /** 1 is 100%. Can be more. */
    private float volume;

    /** 63 is 100%. Can be more. */
    private int pitch;

    public Packet62LevelSound() {}

    public Packet62LevelSound(String soundName, double x, double y, double z, float volume, float pitch)
    {
        this.soundName = soundName;
        this.field_73577_b = (int)(x * 8.0D);
        this.field_73578_c = (int)(y * 8.0D);
        this.field_73575_d = (int)(z * 8.0D);
        this.volume = volume;
        this.pitch = (int)(pitch * 63.0F);

        if (this.pitch < 0)
        {
            this.pitch = 0;
        }

        if (this.pitch > 255)
        {
            this.pitch = 255;
        }
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.soundName = readString(par1DataInputStream, 32);
        this.field_73577_b = par1DataInputStream.readInt();
        this.field_73578_c = par1DataInputStream.readInt();
        this.field_73575_d = par1DataInputStream.readInt();
        this.volume = par1DataInputStream.readFloat();
        this.pitch = par1DataInputStream.readUnsignedByte();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.soundName, par1DataOutputStream);
        par1DataOutputStream.writeInt(this.field_73577_b);
        par1DataOutputStream.writeInt(this.field_73578_c);
        par1DataOutputStream.writeInt(this.field_73575_d);
        par1DataOutputStream.writeFloat(this.volume);
        par1DataOutputStream.writeByte(this.pitch);
    }

    public String func_73570_d()
    {
        return this.soundName;
    }

    public double func_73572_f()
    {
        return (double)((float)this.field_73577_b / 8.0F);
    }

    public double func_73568_g()
    {
        return (double)((float)this.field_73578_c / 8.0F);
    }

    public double func_73569_h()
    {
        return (double)((float)this.field_73575_d / 8.0F);
    }

    public float func_73571_i()
    {
        return this.volume;
    }

    public float func_73573_j()
    {
        return (float)this.pitch / 63.0F;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        // begin modified code
        int blockX = SoundMuffler.getBlockCoord(func_73572_f());
        int blockY = SoundMuffler.getBlockCoord(func_73568_g());
        int blockZ = SoundMuffler.getBlockCoord(func_73569_h());
        if (SoundMuffler.removeAndCheckMuffle(soundName, blockX, blockY, blockZ)) {
            par1NetHandler.handleLevelSound(this);
        }
        // end modified code
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 24;
    }
}
