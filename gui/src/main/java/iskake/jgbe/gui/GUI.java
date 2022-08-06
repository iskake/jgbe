package iskake.jgbe.gui;

import imgui.ImGui;
import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.mem.ROMHeader;
import iskake.jgbe.core.gb.input.Input;
import iskake.jgbe.core.gb.mem.CartridgeROM;
import iskake.jgbe.core.gb.mem.ROMFactory;
//import iskake.jgbe.gui.audio.GameBoyAPU;
import iskake.jgbe.gui.input.GameBoyJoypad;
import iskake.jgbe.gui.video.OpenGLRenderer;

import java.nio.*;

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
    private final GameBoyJoypad joypad;
//    private final GameBoyAPU apu; // TODO
    private final OpenGLRenderer renderer;
    private final ROMFactory romFactory;
    // TODO? Should this be moved to renderer? (get the frame with callback, for example)
    private final ByteBuffer bb = ByteBuffer.allocateDirect(160 * 144 * 3);

    private final DebugGUI debugGUI;

    private ImGuiImplGl3 imGuiGl3;
    private ImGuiImplGlfw imGuiGlfw;

    private long window;
    private int screenTexture;

    private boolean debugMode = false;
    private boolean paused = false;
    private boolean advanceOneFrame = false;
    private boolean showMenuBar = false;

    private int frames = 0;
    private int frameMod = 1;
    private int currSizeMod = 3;

    public GUI() {
        joypad = new GameBoyJoypad();
//        apu = new GameBoyAPU();
        gb = new GameBoy(joypad);
        romFactory = new ROMFactory();
        renderer = new OpenGLRenderer();
        debugGUI = new DebugGUI(gb);
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods) {
        // TODO: use settings file for input and other settings
        if (action == GLFW_PRESS) {
            if (mods == GLFW_MOD_CONTROL) {
                switch (key) {
                    case GLFW_KEY_1 -> setWindowSize(1);
                    case GLFW_KEY_2 -> setWindowSize(2);
                    case GLFW_KEY_3 -> setWindowSize(3);
                    case GLFW_KEY_4 -> setWindowSize(4);

                    case GLFW_KEY_P -> paused = !paused;
                    case GLFW_KEY_LEFT_BRACKET -> advanceOneFrame = true;

                    case GLFW_KEY_O -> reloadNewROM(getROMPathFileDialog());
                    case GLFW_KEY_R -> reload();

                    case GLFW_KEY_D -> toggleDebugMode();
                }
            } else {
                switch (key) {
                    case GLFW_KEY_Z -> joypad.setButtonActive(Input.BUTTON_B);
                    case GLFW_KEY_X -> joypad.setButtonActive(Input.BUTTON_A);
                    case GLFW_KEY_ENTER -> joypad.setButtonActive(Input.BUTTON_START);
                    case GLFW_KEY_BACKSPACE -> joypad.setButtonActive(Input.BUTTON_SELECT);

                    case GLFW_KEY_RIGHT -> joypad.setDirectionActive(Input.DPAD_RIGHT);
                    case GLFW_KEY_LEFT -> joypad.setDirectionActive(Input.DPAD_LEFT);
                    case GLFW_KEY_UP -> joypad.setDirectionActive(Input.DPAD_UP);
                    case GLFW_KEY_DOWN -> joypad.setDirectionActive(Input.DPAD_DOWN);

                    case GLFW_KEY_SPACE -> frameMod = 4;
                }
            }
        }
        if (action == GLFW_RELEASE) {
            switch (key) {
                case GLFW_KEY_Z -> joypad.setButtonInactive(Input.BUTTON_B);
                case GLFW_KEY_X -> joypad.setButtonInactive(Input.BUTTON_A);
                case GLFW_KEY_ENTER -> joypad.setButtonInactive(Input.BUTTON_START);
                case GLFW_KEY_BACKSPACE -> joypad.setButtonInactive(Input.BUTTON_SELECT);

                case GLFW_KEY_RIGHT -> joypad.setDirectionInactive(Input.DPAD_RIGHT);
                case GLFW_KEY_LEFT -> joypad.setDirectionInactive(Input.DPAD_LEFT);
                case GLFW_KEY_UP -> joypad.setDirectionInactive(Input.DPAD_UP);
                case GLFW_KEY_DOWN -> joypad.setDirectionInactive(Input.DPAD_DOWN);

                case GLFW_KEY_SPACE -> frameMod = 1;
            }
        }
    }

    private void setWindowSize(int sizeModifier) {
        currSizeMod = sizeModifier;
        glfwSetWindowSize(window, LCD_SIZE_X * sizeModifier, LCD_SIZE_Y * sizeModifier);
    }

    private void dropCallback(long window, int count, long stringPtrPtr) {
        if (count > 1) {
            log.warn("Multiple files dragged, only the first Game Boy ROM file will be opened.");
        }
        for (int i = 0; i < count; i++) {
            String name = GLFWDropCallback.getName(stringPtrPtr, i);
            if (name.endsWith(".gb") || name.endsWith(".gbc")) {
                log.debug("Dragged Game Boy ROM detected: " + name);
                reloadNewROM(name);
                break;
            } else {
                log.warn("File '" + name + "' is not a Game Boy ROM.");
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

        window = glfwCreateWindow(width, height, "JGBE - (no game loaded)", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create GLFW window.");
        }

        glfwMakeContextCurrent(window);
        glfwSetKeyCallback(window, this::keyCallback);
        glfwSetDropCallback(window, this::dropCallback);
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
        screenTexture = renderer.createTexture(160, 144);
        initImGui(window);

        log.info("Finished initialization");
    }

    private void reload() {
        log.info("Reloading emulator.");
        gb.restart();
    }

    public void reloadNewROM(String romPath) {
        log.info("Loading ROM: " + romPath);

        if (!romPath.equals("")) {
            CartridgeROM rom = romFactory.getROM(romPath);
            gb.restart(rom);
            if (rom != null) {
                String title = ROMHeader.getTitle(rom.getROMBank0());
                glfwSetWindowTitle(window, "JGBE - " + title);
            } else {
                glfwSetWindowTitle(window, "JGBE - (no game loaded)");
            }
        } else {
            log.info("No file selected.");
        }
    }

    public String getROMPathFileDialog() {
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

        return romPath;
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
            }
            renderer.clear();
            startImGuiFrame();
            drawMenuBar();
            if (!debugMode) {
                renderer.renderFrame(screenTexture);
            } else {
                // We don't call `renderer.renderFrame` since debugGUI will render the
                // frame (in an ImGui window) using `ImGui.image`.
                debugGUI.draw(screenTexture);
            }
            endImGuiFrame();

//               glfwSwapInterval(0); // Ignore vsync
            if ((frames % frameMod == 0))
                glfwSwapBuffers(window);
        }
        gb.stopRunning();

        log.info("Exited the run loop");
    }

    private void runFrame() {
        long start = System.nanoTime();

        gb.runUntilVBlank();

        long end = System.nanoTime();
        float deltaTime = (end - start) / 1_000_000f;
//        log.info("delta: " + deltaTime + "ms");

        if (frames % frameMod == 0)
            bb.put(gb.getFrame()).rewind();
        renderer.updateTexture(screenTexture, LCD_SIZE_X, LCD_SIZE_Y, bb);
        frames++;
    }

    private void drawMenuBar() {
        // Because a main menu bar is always active in debug mode, we 'combine' the menu bars
        // so all menus and menu items are added together.
        if (showMenuBar || debugMode) {
            if (ImGui.beginMainMenuBar()) {
                if (ImGui.beginMenu("File")) {
                    if (ImGui.menuItem("Open ROM")) {
                        reloadNewROM(getROMPathFileDialog());
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
            glfwSetWindowSize(window, 1600, 1000);
        } else {
            setWindowSize(currSizeMod);
        }
    }

    public void dispose() {
        log.info("Terminating glfw");

        renderer.disposeTexture(screenTexture);
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
