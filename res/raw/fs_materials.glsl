precision mediump float;

vec3 dirtTexture(sampler2D walk, vec2 tex) {
	return (texture2D(walk, tex * 0.5).r * vec3(0.78, 0.47, 0.31) + 
			texture2D(walk, tex * 4.0).r * vec3(0.68, 0.52, 0.21)) * 0.5;
}

vec3 rockTexture(sampler2D walk, vec2 tex) {
	return (texture2D(walk, tex * 1.0).r * vec3(0.47, 0.55, 0.79) + 
			texture2D(walk, tex * 8.0).r * vec3(0.55, 0.66, 0.44)) * 0.5;
}

vec3 grassTexture(sampler2D cros, vec2 tex) {
	return (texture2D(cros, tex * 1.0).r * vec3(0.21, 0.85, 0.29) + 
			texture2D(cros, tex * 4.0).r * vec3(0.76, 0.81, 0.44)) * 0.5;
}
