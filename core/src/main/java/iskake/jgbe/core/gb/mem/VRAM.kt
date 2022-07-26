package iskake.jgbe.core.gb.mem

import iskake.jgbe.core.gb.ppu.PPUController
import iskake.jgbe.core.gb.ppu.Tile
import iskake.jgbe.core.gb.ppu.TileMap

/**
 * Video memory. Only accessible in modes 0-2 (STAT register bits 0-1).
 */
class VRAM(size: Int, private val ppuControl: PPUController) : RAM(size) {
    val tileBlock0 = arrayOfNulls<Tile>(0x80)
    val tileBlock1 = arrayOfNulls<Tile>(0x80)
    val tileBlock2 = arrayOfNulls<Tile>(0x80)
    private val tileDataBlocks = arrayOf(tileBlock0, tileBlock1, tileBlock2)

    /**
     * Get both of the tilemaps in VRAM.
     * The tilemaps are stored in the ranges `$9800-$9bff` and
     * `$9c00-$9fff` respectively.
     *
     * @return Both tilemaps in VRAM.
     */
    val tileMaps = arrayOfNulls<TileMap>(2)

    init {
        for (i in 0..0x7f) {
            val offset = i * 16
            tileBlock0[i] =
                Tile({ address: Int -> read(address) }, { address: Int, value: Byte -> write(address, value) }, offset)
            tileBlock1[i] = Tile({ address: Int -> read(address) },
                { address: Int, value: Byte -> write(address, value) },
                0x800 + offset
            )
            tileBlock2[i] = Tile({ address: Int -> read(address) },
                { address: Int, value: Byte -> write(address, value) },
                0x1000 + offset
            )
        }
        tileMaps[0] =
            TileMap({ address: Int -> read(address) }, { address: Int, value: Byte -> write(address, value) }, 0x1800)
        tileMaps[1] =
            TileMap({ address: Int -> read(address) }, { address: Int, value: Byte -> write(address, value) }, 0x1c00)
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun read(address: Int): Byte {
        return if (!ppuControl.isVRAMAccessable) {
            0xff.toByte()
        } else super.read(address)
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun write(address: Int, value: Byte) {
        if (!ppuControl.isVRAMAccessable) {
            return
        }
        super.write(address, value)

        // Since the tile has been written to, it is necessary to decode it again.
        if (address < 0x800) {
            tileBlock0[address / 16]!!.isDecoded = false
        } else if (address < 0x1000) {
            tileBlock1[(address - 0x800) / 16]!!.isDecoded = false
        } else if (address < 0x1800) {
            tileBlock2[(address - 0x1000) / 16]!!.isDecoded = false
        }
    }

    fun getTileBlock(tileDataVal: Int): Array<Tile?> {
        return tileDataBlocks[tileDataVal]
    }
    // TODO: update docs
    /**
     * Get the specified tilemap in VRAM.
     * The tilemaps are stored in the ranges `$9800-$9bff` and
     * `$9c00-$9fff` respectively.
     *
     * @param i The index of the tilemap to get.
     * @return The specified tilemap in VRAM.
     */
    fun getTileMap(i: Int): TileMap? {
        return tileMaps[i]
    }
}