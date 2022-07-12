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

import static org.lwjgl.glfw.GLFW.*;

public class JGBEWindow {
    private GameBoy gb;
    private OpenGLRenderer renderer;
    private long window;
    // private GameBoyJoypad joypad;

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
        GLFWErrorCallback.createPrint(System.err).set();
        glfwInit();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        int width = 160 * 3;
        int hidth = 144 * 3;

        window = glfwCreateWindow(width, hidth, "JGBE", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwMakeContextCurrent(window);
        glfwSetKeyCallback(window, this::keyCallback);
        renderer.init(window);

        gb.restart(loadROM("rom/bin/test_tile.gb")); // temp
    }

    public void run() {
        while (!glfwWindowShouldClose(window)) {
            long start = System.nanoTime();
            gb.runUntilVBlank();
            long end = System.nanoTime();
            long deltaTime = (end - start) / 1_000_000;

            // System.out.println("delta: " + deltaTime + "ms");

            glfwPollEvents();
            ByteBuffer bb = ByteBuffer.allocateDirect(160 * 144 * 3);
            bb.put(gb.getFrameMapped()).rewind();
            renderer.updateTexture(bb);
            renderer.renderFrame();
            glfwSwapBuffers(window);
        }
    }

    /**
     * Load a new ROM file, by creating a new GameBoy thread.
     * 
     * @param pathString the path of the ROM file (as a string).
     */
    private CartridgeROM loadROM(String pathString) {
        System.out.println("Loading rom: " + pathString);
        Path path = Paths.get(pathString);
        byte[] romFile;

        try {
            romFile = Files.readAllBytes(path);
        } catch (IOException e) {
            String reason = e instanceof FileNotFoundException
                    || e instanceof NoSuchFileException
                            ? "file not found."
                            : "an exception occurred: " + e.toString();
            System.err.println("Could not read the file " + path.getFileName() + ": " + reason);
            return null;
        }

        try {
            return new CartridgeROM(romFile);
        } catch (Exception e) {
            System.err.println("Could not load the file " + path.getFileName() +
                    ": an exception occurred: " + e.toString());
            return null;
        }
    }

    public void dispose() {
        renderer.dispose();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
