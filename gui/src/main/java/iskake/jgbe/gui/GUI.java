package iskake.jgbe.gui;

import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.mem.CartridgeROM;
import iskake.jgbe.core.gb.ppu.PPU;
import iskake.jgbe.gui.video.OpenGLRenderer;

import java.io.*;
import java.nio.*;
import java.nio.file.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static iskake.jgbe.core.gb.ppu.PPU.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class GUI {
    private final static Logger log = LoggerFactory.getLogger(GUI.class);

    private final GameBoy gb;
    // private final GameBoyJoypad joypad;
    private final OpenGLRenderer renderer;
    // TODO? Should this be moved to renderer? (get the frame with callback, for example)
    private final ByteBuffer bb = ByteBuffer.allocateDirect(160 * 144 * 3);
    private long window;

    public GUI() {
        // this.joypad = new GameBoyJoypad();
        renderer = new OpenGLRenderer();
        gb = new GameBoy(null);
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            switch (key) {
                case GLFW_KEY_ESCAPE -> glfwSetWindowShouldClose(window, true);

                case GLFW_KEY_1 -> glfwSetWindowSize(window, LCD_SIZE_X, LCD_SIZE_Y);
                case GLFW_KEY_2 -> glfwSetWindowSize(window, LCD_SIZE_X * 2, LCD_SIZE_Y * 2);
                case GLFW_KEY_3 -> glfwSetWindowSize(window, LCD_SIZE_X * 3, LCD_SIZE_Y * 3);
                case GLFW_KEY_4 -> glfwSetWindowSize(window, LCD_SIZE_X * 4, LCD_SIZE_Y * 4);

                case GLFW_KEY_O -> {
                    if (reload())
                        run();
                }
            }
        }
    }

    public void init() {
        log.info("Initializing JGBE");

        GLFWErrorCallback.createPrint(System.err).set();
        glfwInit();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        int width = LCD_SIZE_X * 3;
        int height = LCD_SIZE_Y * 3;

        window = glfwCreateWindow(width, height, "JGBE", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create GLFW window.");
        }

        glfwMakeContextCurrent(window);
        glfwSetKeyCallback(window, this::keyCallback);
        renderer.init(window);

        log.info("Finished initialization");
    }

    public boolean reload() {
        String romPath = null;
        PointerBuffer romPathPtr = memAllocPointer(1);

        try {
            int result = NFD_OpenDialog("gb,gbc", null, romPathPtr);
            if (result == NFD_OKAY) {
                romPath = romPathPtr.getStringUTF8();
                log.info("Out: " + romPath);
            }
        } finally {
            memFree(romPathPtr);
        }

        if (romPath != null) {
            gb.restart(loadROM(romPath));
            return true;
        } else {
            log.info("No file selected.");
            return false;
        }
    }

    public void run() {
        log.info("Entering the run loop");

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            renderGBFrame();

            // glfwSwapInterval(0); // Ignore vsync
            glfwSwapBuffers(window);
        }

        log.info("Exited the run loop");
    }

    public void renderGBFrame() {
        long start = System.nanoTime();

        gb.runUntilVBlank();

        long end = System.nanoTime();
        float deltaTime = (end - start) / 1_000_000f;
        log.debug("delta: " + deltaTime + "ms");

        bb.put(gb.getFrame()).rewind();
        renderer.updateTexture(bb);
        renderer.renderFrame();
    }

    /**
     * Load a new ROM file.
     * 
     * @param pathString the path to the ROM file.
     */
    private CartridgeROM loadROM(String pathString) {
        log.info("Loading rom: " + pathString);
        Path path = Paths.get(pathString);
        byte[] romFile;

        try {
            romFile = Files.readAllBytes(path);
        } catch (IOException e) {
            String reason = e instanceof FileNotFoundException
                    || e instanceof NoSuchFileException
                            ? "file not found."
                            : "an exception occurred: " + e;
            log.error("Could not read the file " + path.getFileName() + ": " + reason);
            return null;
        }

        try {
            return new CartridgeROM(romFile);
        } catch (Exception e) {
            log.error("Could not load the file " + path.getFileName() +
                    ": an exception occurred: " + e);
            return null;
        }
    }

    public void dispose() {
        log.info("Terminating glfw");

        renderer.dispose();
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        log.info("Finished terminating glfw");
    }
}
