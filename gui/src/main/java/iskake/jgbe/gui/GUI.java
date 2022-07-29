package iskake.jgbe.gui;

import imgui.ImGui;
import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.mem.CartridgeROM;
import iskake.jgbe.gui.video.OpenGLRenderer;

import java.io.*;
import java.nio.*;
import java.nio.file.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import imgui.glfw.*;
import imgui.gl3.*;

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

    private final DebugGUI debugGUI;

    private ImGuiImplGl3 imGuiGl3;
    private ImGuiImplGlfw imGuiGlfw;

    private long window;

    private boolean debugMode = false;
    private boolean paused = false;
    private boolean advanceOneFrame = false;
    private boolean showMenuBar = false;

    public GUI() {
        // this.joypad = new GameBoyJoypad();
        gb = new GameBoy(null);
        renderer = new OpenGLRenderer();
        debugGUI = new DebugGUI(gb);

    }

    private void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            switch (key) {
                case GLFW_KEY_ESCAPE -> glfwSetWindowShouldClose(window, true);

                case GLFW_KEY_1 -> glfwSetWindowSize(window, LCD_SIZE_X, LCD_SIZE_Y);
                case GLFW_KEY_2 -> glfwSetWindowSize(window, LCD_SIZE_X * 2, LCD_SIZE_Y * 2);
                case GLFW_KEY_3 -> glfwSetWindowSize(window, LCD_SIZE_X * 3, LCD_SIZE_Y * 3);
                case GLFW_KEY_4 -> glfwSetWindowSize(window, LCD_SIZE_X * 4, LCD_SIZE_Y * 4);

                case GLFW_KEY_O -> reloadNewROM();
                case GLFW_KEY_R -> reload();

                case GLFW_KEY_P -> paused = !paused;
                case GLFW_KEY_LEFT_BRACKET -> advanceOneFrame = true;

                case GLFW_KEY_D -> toggleDebugMode();
            }
        }
    }

    private void cursorEnterCallback(long window, boolean entered) {
        showMenuBar = entered;
    }

    public long initGLFW() {
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
        glfwSetCursorEnterCallback(window, this::cursorEnterCallback);
        return window;
    }

    void initImGui(long window) {
        ImGui.createContext();

//        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        ImGui.getIO().setIniFilename(null);

        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGlfw.init(window, true);

        imGuiGl3 = new ImGuiImplGl3();
        imGuiGl3.init();
    }

    public void init() {
        log.info("Initializing JGBE");

        window = initGLFW();
        renderer.init(window);
        initImGui(window);

        log.info("Finished initialization");
    }

    private void reload() {
        gb.restart();
    }

    public void reloadNewROM() {
        String romPath = "";
        PointerBuffer romPathPtr = memAllocPointer(1);

        try {
            int result = NFD_OpenDialog("gb,gbc", null, romPathPtr);
            if (result == NFD_OKAY) {
                romPath = romPathPtr.getStringUTF8();
                log.info("Selected file: " + romPath);
            }
        } finally {
            memFree(romPathPtr);
        }

        if (!romPath.equals("")) {
            gb.restart(loadROM(romPath));
        } else {
            log.info("No file selected.");
        }
    }

    /** This function should be called before a frame's ImGui functionality has been run. */
    private void startImGuiFrame() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    /** This function should be called after a frame's ImGui functionality has been run. */
    private void endImGuiFrame() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    public void run() {
        log.info("Entering the run loop");

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            if (!paused || advanceOneFrame) {
                advanceOneFrame = false;
                runFrame();
                renderer.clear();
                startImGuiFrame();
                drawMenuBar();
                if (!debugMode) {
                    renderer.renderFrame();
                } else {
                    // We don't call `renderer.renderFrame` since debugGUI will render the
                    // frame (in an ImGui window) using `ImGui.image`.
                    debugGUI.draw(renderer.tex);
                }
                endImGuiFrame();

                glfwSwapInterval(1); // Ignore vsync
                glfwSwapBuffers(window);
            }
        }

        log.info("Exited the run loop");
    }

    private void runFrame() {
        long start = System.nanoTime();

        gb.runUntilVBlank();

        long end = System.nanoTime();
        float deltaTime = (end - start) / 1_000_000f;
        log.debug("delta: " + deltaTime + "ms");

        bb.put(gb.getFrame()).rewind();
        renderer.updateTexture(bb);
    }

    private void drawMenuBar() {
        // Because a main menu bar is always active in debug mode, we 'combine' the menu bars
        // so all menus and menu items are added together.
        if (showMenuBar || debugMode) {
            if (ImGui.beginMainMenuBar()) {
                if (ImGui.beginMenu("File")) {
                    if (ImGui.menuItem("Open ROM")) {
                        reloadNewROM();
                    }
                    if (ImGui.menuItem("Restart")) {
                        reload();
                    }
                    if (ImGui.menuItem("Quit")) {
                        glfwSetWindowShouldClose(window, true);
                    }
                    ImGui.endMenu();
                }

                ImGui.endMainMenuBar();
            }
        }
    }

    private void toggleDebugMode() {
        debugMode = !debugMode;
        if (debugMode) {
            glfwSetWindowSize(window, 1280, 800);
        } else {
            glfwSetWindowSize(window, LCD_SIZE_X * 3, LCD_SIZE_Y * 3);
        }
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
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        log.info("Finished terminating glfw");
    }
}
