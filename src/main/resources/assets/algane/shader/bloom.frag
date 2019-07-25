#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform float Radius;
uniform vec2 Direction;

void main() {
    vec3 colBins = vec3(0.0);
    float alphaBin = 0.0;
    float totalColWeight = 0.0;
    float totalAlphaWeight = 0.0;
    for (float r = -Radius; r <= Radius; r += 1.0) {
        vec4 nearby = texture2D(DiffuseSampler, texCoord + oneTexel * r * Direction);
        float weight = 1.0 - abs(r / Radius);
//            strength = strength * strength;
        if (nearby.a > 0) {
            colBins = colBins + nearby.rgb * weight;
            totalColWeight = totalColWeight + weight;
        }
        alphaBin = alphaBin + nearby.a * weight;
        totalAlphaWeight = totalAlphaWeight + weight;
    }
    gl_FragColor = vec4(colBins / totalColWeight, alphaBin / totalAlphaWeight);
}
