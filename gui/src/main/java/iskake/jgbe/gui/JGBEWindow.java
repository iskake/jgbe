package iskake.jgbe.gui;

import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.mem.CartridgeROM;
import iskake.jgbe.gui.video.OpenGLRenderer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.lwjgl.glfw.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.*;

public class JGBEWindow {
    private final static Logger log = LoggerFactory.getLogger(JGBEWindow.class);

    private final GameBoy gb;
    // private final GameBoyJoypad joypad;
    private final OpenGLRenderer renderer;
    // TODO? Should this be moved to renderer? (get the frame with callback, for example)
    private final ByteBuffer bb = ByteBuffer.allocateDirect(160 * 144 * 3);
    private long window;

    public JGBEWindow() {
        // this.joypad = new GameBoyJoypad();
        renderer = new OpenGLRenderer();
        gb = new GameBoy(null);
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window, true);
        }
    }

    public void init() {
        log.info("Initializing JGBE");

        GLFWErrorCallback.createPrint(System.err).set();
        glfwInit();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        int width = 160 * 3;
        int height = 144 * 3;

        window = glfwCreateWindow(width, height, "JGBE", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create GLFW window.");
        }

        glfwMakeContextCurrent(window);
        glfwSetKeyCallback(window, this::keyCallback);
        renderer.init(window);

        gb.restart(loadROM("rom/bin/test_tile.gb")); // temp

        log.info("Finished initialization");
    }

    public void run() {
        log.info("Entering the render loop");

        while (!glfwWindowShouldClose(window)) {
            long start = System.nanoTime();

            gb.runUntilVBlank();

            long end = System.nanoTime();
            float deltaTime = (end - start) / 1_000_000f;
            log.debug("delta: " + deltaTime + "ms");

            glfwPollEvents();
            bb.put(gb.getFrame()).rewind();
            renderer.updateTexture(bb);
            renderer.renderFrame();

            // glfwSwapInterval(0); // Ignore vsync
            glfwSwapBuffers(window);
        }
        log.info("Exited the render loop");
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
