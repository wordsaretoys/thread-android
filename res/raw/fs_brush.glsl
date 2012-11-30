precision mediump float;

const float PI = 3.141592654;

uniform sampler2D stem0;
uniform sampler2D walk0;

varying vec3 obj;
varying vec3 eye;
varying vec2 tex;

void main(void) {
	float m = texture2D(stem0, vec2(0.1 * tex.y / tex.x, tex.x)).r * texture2D(walk0, tex).r;
	vec3 col = 1.5 * m * abs(sin(obj * 10.0));

	col = fog(col, eye);
	
	gl_FragColor = vec4(col, 1.0);
	
	if (m * (1.0 - length(tex)) < 0.025)
		discard;
}
