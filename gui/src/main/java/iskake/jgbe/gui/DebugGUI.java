package iskake.jgbe.gui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

import iskake.jgbe.core.gb.GameBoy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static iskake.jgbe.core.gb.ppu.PPU.LCD_SIZE_X;
import static iskake.jgbe.core.gb.ppu.PPU.LCD_SIZE_Y;

public class DebugGUI {
    private static final Logger log = LoggerFactory.getLogger(DebugGUI.class);

    private final ImBoolean showMem = new ImBoolean(false);
    private final ImBoolean showVram = new ImBoolean(false);

    private final ImBoolean showBG = new ImBoolean(true);
    private final ImBoolean showWin = new ImBoolean(true);
    private final ImBoolean showSpr = new ImBoolean(true);
    private final GameBoy gb;
//    private final Debugger dbg;

    public DebugGUI(GameBoy gb) {
        this.gb = gb;
//        this.dbg = gb.getDebugger(); // todo
    }

    public void draw(int screenTex) {
        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("Debug")) {
                if (ImGui.menuItem("Toggle debugger")) {
                    gb.toggleDebugger();
                }
                ImGui.menuItem("Show memory view", "", showMem);
                ImGui.menuItem("Show VRAM view", "", showVram);
                ImGui.endMenu();
            }

            ImGui.endMainMenuBar();
        }

        showGBFrame(screenTex);

        if (showMem.get())
            showMemory();

        if (showVram.get())
            showVRAMView();

        ImGui.showDemoWindow(); // Tmp.

        // Uncomment to enable viewports (after adding the config flag `ViewportsEnable`)
//        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
//            final long backupWindow = glfwGetCurrentContext();
//            ImGui.updatePlatformWindows();
//            ImGui.renderPlatformWindowsDefault();
//            glfwMakeContextCurrent(backupWindow);
//        }
    }

    private void showGBFrame(int screenTex) {
        ImGui.setNextWindowContentSize(LCD_SIZE_X * 3, LCD_SIZE_Y * 3);
        ImGui.setNextWindowSize(0, 0); // Sets the window size to the content size.
        ImGui.begin("Emulator view", ImGuiWindowFlags.MenuBar);
        {
            // Temp.
            if (ImGui.beginMenuBar()) {
                if (ImGui.beginMenu("View")) {
                    ImGui.menuItem("Show background layer", "", showBG);
                    ImGui.menuItem("Show window layer", "", showWin);
                    ImGui.menuItem("Show sprite layer", "", showSpr);
                    ImGui.endMenu();
                }
                ImGui.endMenuBar();
            }
            gb.getPPU().drawBG = showBG.get();
            gb.getPPU().drawWin = showWin.get();
            gb.getPPU().drawSpr = showSpr.get();
            ImGui.image(screenTex, LCD_SIZE_X * 3, LCD_SIZE_Y * 3);
        }
        ImGui.end();
    }

    private final int[] addrPtr = new int[1];

    private void showMemory() {
        ImGui.begin("Memory view");
        float y = ImGui.getContentRegionAvailY();
        y = ((y - 30) / 0x10) - ImGui.getFontSize(); // Clamp size
        {
            ImGui.sliderInt("Address", addrPtr, 0, 0xff, "0x%02x00".formatted(addrPtr[0]));
            int address = addrPtr[0] * 0x100;
            ImGui.text("Addr  ");
            for (int i = 0; i < 16; i++) {
                ImGui.sameLine();
                ImGui.text(" %x".formatted(i));
            }
            for (int i = 0; i < 10 + ((int)y > 0 ? y : 0); i++) {
                if (address + i * 16 > 0xffff)
                    break;

                ImGui.text("%04x  ".formatted(address + i * 16));
                ImGui.sameLine();
                for (int j = 0; j < 16; j++) {
                    int value = Byte.toUnsignedInt(gb.readAddressNoCycle((short)(address + (i * 16 + j))));
                    ImGui.text("%02x".formatted(value));
                    ImGui.sameLine();
                }
                ImGui.newLine();
            }
        }
        ImGui.end();
    }

    private void showVRAMView() {
        ImGui.setNextWindowContentSize(512, 512);
        ImGui.begin("VRAM view");
        {
            ImGui.text("VRAM Block 0");
            // ImGui.image();
            ImGui.separator();
            ImGui.text("VRAM Block 1");
            // ImGui.image();
            ImGui.separator();
            ImGui.text("VRAM Block 2");
            // ImGui.image();
        }
        ImGui.end();
    }
}
