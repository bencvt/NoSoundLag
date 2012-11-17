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

        StepSound blockSound = Block.blocksList[placeBlockId].stepSound;
        String blockSoundId = blockSound.getPlaceSound();
        // parameter math copied from ItemBlock.onItemUse()
        Minecraft.getMinecraft().sndManager.playSound(
                blockSoundId,
                (float) placeX + 0.5F,
                (float) placeY + 0.5F,
                (float) placeZ + 0.5F,
                (blockSound.getVolume() + 1.0F) / 2.0F,
                blockSound.getPitch() * 0.8F);
        SoundMuffler.placeTrail.update(blockSoundId, placeX, placeY, placeZ);
    }

    public static boolean doesBlockPreventRightClick(int x, int y, int z) {
        return blockIdsConsumingRightClick.contains(Minecraft.getMinecraft().theWorld.getBlockId(x, y, z));
    }
    /**
     * List of block ids that intercept right-click when attempting to place a block adjacent to it.
     * I.e., the block's class overrides onBlockActivated() to return true when the player is holding an ItemBlock.
     */
    private static final HashSet<Integer> blockIdsConsumingRightClick =
            new HashSet<Integer>(Arrays.asList(new Integer[] {
                    Block.anvil.blockID,
                    Block.beacon.blockID,
                    Block.bed.blockID,
                    Block.brewingStand.blockID,
                    Block.cake.blockID,
                    Block.cauldron.blockID,
                    Block.chest.blockID,
                    Block.commandBlock.blockID,
                    Block.dispenser.blockID,
                    Block.doorSteel.blockID,
                    Block.doorWood.blockID,
                    Block.dragonEgg.blockID,
                    Block.enchantmentTable.blockID,
                    Block.enderChest.blockID,
                    Block.fenceGate.blockID,
                    Block.jukebox.blockID, // actually, returns false if there is no record inside. Just let this sound lag.
                    Block.lever.blockID,
                    Block.music.blockID, // a.k.a. note block
                    Block.redstoneRepeaterActive.blockID,
                    Block.redstoneRepeaterIdle.blockID,
                    Block.stoneButton.blockID,
                    Block.stoneOvenActive.blockID,
                    Block.stoneOvenIdle.blockID,
                    Block.trapdoor.blockID,
                    Block.woodenButton.blockID,
                    Block.workbench.blockID // a.k.a. crafting table
            }));
}
