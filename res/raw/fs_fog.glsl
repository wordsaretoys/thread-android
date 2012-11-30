precision mediump float;

vec3 fog(vec3 col, vec3 eye) {
	float l = clamp(length(eye) / 6.0, 0.0, 1.0);
	return mix(col, vec3(0.75, 0.75, 0.75), l);
}
