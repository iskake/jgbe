package iskake.jgbe.core.gb.mem

import iskake.jgbe.core.gb.ppu.PPUController
import iskake.jgbe.core.gb.ppu.Sprite

/**
 * Object Attribute Memory ('Sprite Attribute Table').
 * Only accessible in modes 0-1 (STAT register bits 0-1).
 */
class OAM(size: Int, private val ppuControl: PPUController) : RAM(size) {
    /**
     * Get all sprites in OAM.
     *
     * @return All sprites in OAM.
     */
    val sprites = arrayOfNulls<Sprite>(40)

    init {
        for (i in sprites.indices) {
            sprites[i] =
                Sprite({ address: Int -> read(address) }, { address: Int, value: Byte -> write(address, value) }, i * 4)
        }
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun read(address: Int): Byte {
        return if (!ppuControl.isOAMAccessable) {
            0xff.toByte()
        } else super.read(address)
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun write(address: Int, value: Byte) {
        if (!ppuControl.isOAMAccessable) {
            return
        }
        super.write(address, value)
    }
}