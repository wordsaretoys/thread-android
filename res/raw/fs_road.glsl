precision mediump float;

const float PI = 3.141592654;

uniform sampler2D walk0;
uniform sampler2D walk1;
uniform sampler2D line0;
uniform sampler2D cros0;

varying vec3 obj;
varying vec3 eye;
varying vec2 tex;

void main(void) {

	/* use positions as texture coordinates here */
	/* to avoid distortion caused by road curvature */
	vec2 ot = vec2(obj.x, tex.y);
	vec3 rt = rockTexture(line0, ot);
	vec3 dt = dirtTexture(walk0, ot);
	vec3 gt = grassTexture(cros0, ot);

	/* can use texture coordinates for mixing, though */
	float m1 = pow(sin(tex.x * PI), 2.0);
	float m2 = texture2D(walk1, tex).r;
	vec3 col = mix(mix(gt, dt, m1), mix(dt, rt, m2), m1);
	
	col = fog(col, eye);
	
	gl_FragColor = vec4(col, 1.0);
}