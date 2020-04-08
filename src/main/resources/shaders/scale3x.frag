#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform ivec2 u_inputSize;

void main() {
    float w = u_inputSize.x;
    float h = u_inputSize.y;
    float x = floor(v_texCoords.x * w);
    float y = floor(v_texCoords.y * h);
    float sx = 1 / w;
    float sy = 1 / h;

    // [ A B C ]
    // [ D E F ]
    // [ G H I ]
    vec4 A = texture2D(u_texture, vec2((x - 1) * sx, (y - 1) * sy));
    vec4 B = texture2D(u_texture, vec2(x * sx, (y - 1) * sy));
    vec4 C = texture2D(u_texture, vec2((x + 1) * sx, (y - 1) * sy));
    vec4 D = texture2D(u_texture, vec2((x + 1) * sx, y * sy));
    vec4 E = texture2D(u_texture, vec2(x * sx, y * sy));
    vec4 F = texture2D(u_texture, vec2((x + 1) * sx, y * sy));
    vec4 G = texture2D(u_texture, vec2((x - 1) * sx, (y + 1) * sy));
    vec4 H = texture2D(u_texture, vec2(x * sx, (y + 1) * sy));
    vec4 I = texture2D(u_texture, vec2((x + 1) * sx, (y + 1) * sy));

    // [ E0 E1 E2 ]
    // [ E3 E4 E5 ]
    // [ E6 E7 E8 ]
    vec4 E0 = D == B && B != F && D != H ? D : E;
    vec4 E1 = (D == B && B != F && D != H && E != C) || (B == F && B != D && F != H && E != A) ? B : E;
    vec4 E2 = B == F && B != D && F != H ? F : E;
    vec4 E3 = (D == B && B != F && D != H && E != G) || (D == H && D != B && H != F && E != A) ? D : E;
    vec4 E4 = E;
    vec4 E5 = (B == F && B != D && F != H && E != I) || (H == F && D != H && B != F && E != C) ? F : E;
    vec4 E6 = D == H && D != B && H != F ? D : E;
    vec4 E7 = (D == H && D != B && H != F && E != I) || (H == F && D != H && B != F && E != G) ? H : E;
    vec4 E8 = H == F && D != H && B != F ? F : E;

    float dx = floor(mod(v_texCoords.x * u_inputSize.x * 3, 3));
    float dy = floor(mod(v_texCoords.y * u_inputSize.y * 3, 3));

    if (dx < 1) {
        if (dy < 1) {
            gl_FragColor = E0;
        } else if (dy < 2) {
            gl_FragColor = E1;
        } else {
            gl_FragColor = E2;
        }
    } else if (dx < 2) {
        if (dy < 1) {
            gl_FragColor = E3;
        } else if (dy < 2) {
            gl_FragColor = E4;
        } else {
            gl_FragColor = E5;
        }
    } else {
        if (dy < 1) {
            gl_FragColor = E6;
        } else if (dy < 2) {
            gl_FragColor = E7;
        } else {
            gl_FragColor = E8;
        }
    }

}
