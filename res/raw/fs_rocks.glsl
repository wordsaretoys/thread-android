precision mediump float;

uniform sampler2D walk0;
uniform sampler2D walk1;

varying vec3 obj;
varying vec3 eye;
varying vec2 tex;

void main(void) {

	vec3 col = rockTexture(walk0, tex) + rockTexture(walk1, tex * 2.0);
	col = fog(col, eye);
	
	gl_FragColor = vec4(col, 1.0);
}
