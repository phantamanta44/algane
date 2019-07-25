#version 120

uniform vec2 Size;

varying vec2 texCoord;

void main(){
    texCoord = gl_Vertex.xy / Size;
    texCoord.y = 1.0 - texCoord.y;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
