package iskake.jgbe.core.gb;

public interface GameBoyDisplayable {
    byte[] getFrame();
    byte[] getFrameMapped();
    boolean canGetFrame();
}
