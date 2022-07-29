package iskake.jgbe.gui.video;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL33.*;

import java.nio.ByteBuffer;

public class OpenGLRenderer {

    private static final String vertexShaderSource = """
        #version 330 core

        layout (location = 0) in vec3 aPos;
        layout (location = 1) in vec2 aTexCoord;

        out vec2 TexCoord;

        void main()
        {
           gl_Position = vec4(aPos, 1.0);
           TexCoord = aTexCoord;
        }
        """;

    private static final String fragmentShaderSource = """
        #version 330 core

        out vec4 FragColor;

        in vec2 TexCoord;

        uniform sampler2D ourTexture;

        void main()
        {
           FragColor = texture(ourTexture, TexCoord);
        }
        """;

    private static final float[] verticesRect = {
        // positions         // texture coords
         1.0f,  1.0f, 0.0f,   1.0f, 0.0f,   // top right
         1.0f, -1.0f, 0.0f,   1.0f, 1.0f,   // bottom right
        -1.0f, -1.0f, 0.0f,   0.0f, 1.0f,   // bottom left
        -1.0f,  1.0f, 0.0f,   0.0f, 0.0f    // top left 
    };

    private static final int[] indices = {
        0, 1, 3,    // first triangle
        1, 2, 3     // second triangle
    };

    private int vao;
    private int vbo;
    private int ebo;

    private void framebufferSizeCallback(long window, int width, int height) {
        glViewport(0, 0, width, height);
    }

    public void init(long window) {
        glfwSetFramebufferSizeCallback(window, this::framebufferSizeCallback);

        createCapabilities();

        // Shader program
        ShaderProgram shaderProg = new ShaderProgram(vertexShaderSource, fragmentShaderSource);
        shaderProg.init();

        // Vertex Array Object
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Vertex Buffer Object
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verticesRect, GL_STATIC_DRAW);

        // Element buffer object
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        final int SIZEOF_FLOAT = 4;

        // Link vertex attributes
        //  Position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * SIZEOF_FLOAT, 0);
        glEnableVertexAttribArray(0);
        //  Texture attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * SIZEOF_FLOAT, (3 * SIZEOF_FLOAT));
        glEnableVertexAttribArray(1);

        shaderProg.use();
        glUniform1i(glGetUniformLocation(shaderProg.ID, "ourTexture"), 0);

        glfwShowWindow(window);
    }

    public int createTexture(int width, int height) {
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST/* _MIPMAP_NEAREST */);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);

        glBindTexture(GL_TEXTURE_2D, 0);

        return texID;
    }

    public void updateTexture(int tex, int width, int height, ByteBuffer data) {
        if (data.limit() != width * height * 3) {
            System.err.println("Invalid data size!");
        }

        glBindTexture(GL_TEXTURE_2D, tex);
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_RGB, GL_UNSIGNED_BYTE, data);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void clear() {
        glClearColor(0.133f, 0.125f, 0.2f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void renderFrame(int tex) {
        glBindTexture(GL_TEXTURE_2D, tex);

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void disposeTexture(int tex) {
        glDeleteTextures(tex);
    }

    public void dispose() {
        glDeleteBuffers(vbo);
    }

}
