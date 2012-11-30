attribute vec3 position;
attribute vec2 texture;

uniform mat4 projector;
uniform mat4 modelview;

varying vec3 obj;
varying vec3 eye;
varying vec2 tex;

void main(void) {
	vec4 pos = modelview * vec4(position, 1.0);
	gl_Position = projector * pos;
	eye = pos.xyz;
	obj = position;
	tex = texture;
}
