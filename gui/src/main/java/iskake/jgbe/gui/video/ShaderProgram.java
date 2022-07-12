package iskake.jgbe.gui.video;

import static org.lwjgl.opengl.GL33.*;

import java.util.function.IntBinaryOperator;

import org.lwjgl.opengl.GL33;

public class ShaderProgram {
    public int ID;
    private final String vertexShaderSource;
    private final String fragmentShaderSource;

    public ShaderProgram(String vertexShaderSource, String fragmentShaderSource) {
        this.vertexShaderSource = vertexShaderSource;
        this.fragmentShaderSource = fragmentShaderSource;
    }

    public void init() {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        checkShaderOrProgramSuccess(GL33::glGetShaderi, vertexShader, GL_COMPILE_STATUS, "Vertex shader compilation");

        // Fragment shader
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        checkShaderOrProgramSuccess(GL33::glGetShaderi, fragmentShader, GL_COMPILE_STATUS, "Fragment shader compilation");

        // Shader program
        ID = glCreateProgram();
        glAttachShader(ID, vertexShader);
        glAttachShader(ID, fragmentShader);
        glLinkProgram(ID);
        checkShaderOrProgramSuccess(GL33::glGetProgrami, ID, GL_LINK_STATUS, "Shader program linking");

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void use() {
        glUseProgram(ID);
    }

    /**
     * Check the result of the specified function, e.g. {@code glGetShaderi} returns {@code GL_TRUE} (success.)
     * If it does not, throw an error message.
     */
    private void checkShaderOrProgramSuccess(IntBinaryOperator func, int shaderOrProgram, int pname, String operation) {
        int success = func.applyAsInt(shaderOrProgram, pname);
        if (success != GL_TRUE) {
            String s = glGetShaderInfoLog(shaderOrProgram, 512);
            throw new RuntimeException(operation + " failed: '" + s + "' (errorcode " + success + ")");
        }
    }

}
