package net.minecraft.src;

import java.util.Arrays;
import java.util.HashSet;

import net.minecraft.client.Minecraft;

/**
 * Handle block placement packets: play the sound immediately and remember to ignore
 * the subsequent sound packet the server will send us later on.
 * 
 * It would perhaps be cleaner to add the hook in PlayerControllerMP.onPlayerRightClick().
 * However this would likely introduce mod compatibility issues; PlayerControllerMP is
 * patched by many more mods than Packet15Place.
 * 
 * @author bencvt
 */
public abstract class Packet15PlaceHook {

    public static void onBlockPlace(int adjacentX, int adjacentY, int adjacentZ, int direction, ItemStack itemStack) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            // single player world, do nothing
            return;
        }
        if (adjacentX == -1 && adjacentY == -1 && adjacentZ == -1 && direction == 255) {
            // special case: using an item (e.g., eating)
            // just let the sound lag happen for this
            return;
        }
        if (itemStack == null || itemStack.getItem() == null
                || !(itemStack.getItem() instanceof ItemBlock)) {
            // only interested in block placement
            return;
        }
        int placeBlockId = ((ItemBlock) itemStack.getItem()).getBlockID();
        if (placeBlockId < 0 || placeBlockId >= Block.blocksList.length
                || Block.blocksList[placeBlockId] == null
                || Block.blocksList[placeBlockId].stepSound == null) {
            return;
        }
        if (doesBlockPreventRightClick(adjacentX, adjacentY, adjacentZ)) {
            return;
        }

        int placeX = adjacentX;
        int placeY = adjacentY;
        int placeZ = adjacentZ;
        switch (direction) {
        case 0: placeY--; break; // below
        case 1: placeY++; break; // above
        case 2: placeZ--; break; // north
        case 3: placeZ++; break; // south
        case 4: placeX--; break; // west
        case 5: placeX++; break; // east
        }

        StepSound stepSound = Block.blocksList[placeBlockId].stepSound;
        // parameter math copied from ItemBlock.tryPlaceIntoWorld()
        Minecraft.getMinecraft().sndManager.playSound(
                stepSound.getStepSound(),
                (float) placeX + 0.5F,
                (float) placeY + 0.5F,
                (float) placeZ + 0.5F,
                (stepSound.getVolume() + 1.0F) / 2.0F,
                stepSound.getPitch() * 0.8F);
        SoundMuffler.muffleBlock(stepSound.getStepSound(), placeX, placeY, placeZ);
    }

    public static boolean doesBlockPreventRightClick(int x, int y, int z) {
        return BLOCKIDS_PREVENTING_RIGHT_CLICK.contains(Minecraft.getMinecraft().theWorld.getBlockId(x, y, z));
    }
    /**
     * List of block ids that intercept right-click when attempting to place a block adjacent to it.
     * I.e., the block's class overrides onBlockActivated() to return true when the player is holding an ItemBlock.
     */
    public static final HashSet<Integer> BLOCKIDS_PREVENTING_RIGHT_CLICK = new HashSet<Integer>();
    private static void _add(Block block) { BLOCKIDS_PREVENTING_RIGHT_CLICK.add(block.blockID); }
    static {
        _add(Block.bed);
        _add(Block.brewingStand);
        _add(Block.button);
        _add(Block.cake);
        _add(Block.cauldron);
        _add(Block.chest);
        _add(Block.dispenser);
        _add(Block.doorSteel);
        _add(Block.doorWood);
        _add(Block.dragonEgg);
        _add(Block.enchantmentTable);
        _add(Block.enderChest);
        _add(Block.fenceGate);
        _add(Block.stoneOvenActive);
        _add(Block.stoneOvenIdle);
        _add(Block.jukebox); // actually, returns false if there is no record inside. Just let this sound lag.
        _add(Block.lever);
        _add(Block.music);
        _add(Block.redstoneRepeaterActive);
        _add(Block.redstoneRepeaterIdle);
        _add(Block.trapdoor);
        _add(Block.workbench);
    }
}
