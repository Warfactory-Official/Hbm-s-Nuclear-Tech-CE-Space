#version 120

uniform sampler2D bodyTex;
uniform int useTexture;

void main() {
	vec4 color = gl_Color;
	if(useTexture != 0) color *= texture2D(bodyTex, gl_TexCoord[0].xy);
	gl_FragColor = color;
}
