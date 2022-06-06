package iskake.jgbe.core.gb;

public interface GameBoyDisplayable {
    byte[] getFrame();
    int[] getFrameMapped();
    boolean canGetFrame();
}
