package iskake.jgbe.gui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.Debugger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static iskake.jgbe.core.gb.ppu.PPU.LCD_SIZE_X;
import static iskake.jgbe.core.gb.ppu.PPU.LCD_SIZE_Y;

public class DebugGUI {
    private static final Logger log = LoggerFactory.getLogger(DebugGUI.class);

    private final ImBoolean showVram = new ImBoolean(false);
    private final GameBoy gb;
//    private final Debugger dbg;

    public DebugGUI(GameBoy gb) {
        this.gb = gb;
//        this.dbg = gb.getDebugger(); // todo
    }

    public void draw(int screenTex) {
        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("Debug")) {
                ImGui.menuItem("Show VRAM view", "", showVram);
                ImGui.endMenu();
            }

            ImGui.endMainMenuBar();
        }

        showGBFrame(screenTex);

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
        ImGui.setNextWindowContentSize(LCD_SIZE_X * 2, LCD_SIZE_Y * 2);
        ImGui.setNextWindowSize(0, 0); // Sets the window size to the content size.
        ImGui.begin("Emulator view", ImGuiWindowFlags.MenuBar);
        {
            // Temp.
            if (ImGui.beginMenuBar()) {
                if (ImGui.beginMenu("View")) {
                    ImGui.menuItem("Show background layer", "", true);
                    ImGui.menuItem("Show window layer", "", true);
                    ImGui.menuItem("Show sprite layer", "", true);
                    ImGui.endMenu();
                }
                ImGui.endMenuBar();
            }
            ImGui.image(screenTex, LCD_SIZE_X * 2, LCD_SIZE_Y * 2);
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
