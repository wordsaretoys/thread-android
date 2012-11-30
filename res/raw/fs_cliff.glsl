precision mediump float;

uniform sampler2D walk0;
uniform sampler2D walk1;
uniform sampler2D line0;
uniform sampler2D cros0;

varying vec3 obj;
varying vec3 eye;
varying vec2 tex;

void main(void) {
	vec3 rt = rockTexture(line0, tex);
	vec3 dt = dirtTexture(walk0, tex);
	vec3 gt = grassTexture(cros0, tex);
	float m = texture2D(walk1, tex * 0.1).r;
	vec3 col = mix( mix(rt, dt, m), mix(dt, gt, m), m);

	col = fog(col, eye);
	
	gl_FragColor = vec4(col, 1.0);
}
