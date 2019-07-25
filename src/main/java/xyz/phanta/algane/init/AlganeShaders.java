package xyz.phanta.algane.init;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.util.render.shader.*;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.fml.relauncher.Side;
import xyz.phanta.algane.constant.ResConst;

@SuppressWarnings("NullableProblems")
public class AlganeShaders {

    public static IShaderProgram WHITE;
    public static Uniform<Vec2f, ?> WHITE_SIZE;
    public static Uniform<Integer, ?> WHITE_TEX_SAMPLER;

    public static IShaderProgram BLOOM;
    public static Uniform<Vec2f, ?> BLOOM_SIZE;
    public static Uniform<Integer, ?> BLOOM_TEX_SAMPLER;
    public static Uniform<Float, ?> BLOOM_RADIUS;
    public static Uniform<Vec2f, ?> BLOOM_DIRECTION;

    @InitMe(sides = { Side.CLIENT })
    public static void init() {
        IShaderProgram.Source whiteSrc  = ShaderUtils.newShaderProgram()
                .withShader(ShaderUtils.newShader(ShaderType.VERTEX, ResConst.SHADER_GENERIC_VERT))
                .withShader(ShaderUtils.newShader(ShaderType.FRAGMENT, ResConst.SHADER_WHITE_FRAG));
        WHITE_SIZE = whiteSrc.getUniform(UniformType.FLOAT2, "Size");
        WHITE_TEX_SAMPLER = whiteSrc.getUniform(UniformType.INT, "DiffuseSampler");
        WHITE = whiteSrc.compile();

        IShaderProgram.Source bloomSrc = ShaderUtils.newShaderProgram()
                .withShader(ShaderUtils.newShader(ShaderType.VERTEX, ResConst.SHADER_BLOOM_VERT))
                .withShader(ShaderUtils.newShader(ShaderType.FRAGMENT, ResConst.SHADER_BLOOM_FRAG));
        BLOOM_SIZE = bloomSrc.getUniform(UniformType.FLOAT2, "Size");
        BLOOM_TEX_SAMPLER = bloomSrc.getUniform(UniformType.INT, "DiffuseSampler");
        BLOOM_RADIUS = bloomSrc.getUniform(UniformType.FLOAT, "Radius");
        BLOOM_DIRECTION = bloomSrc.getUniform(UniformType.FLOAT2, "Direction");
        BLOOM = bloomSrc.compile();
    }

}
