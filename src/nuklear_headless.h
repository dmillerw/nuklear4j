#ifndef NK_SDL_H_
#define NK_SDL_H_

#include "nuklear.h"

struct nk_headless_Font {
    int width;
    int height;
    int handle;
};

typedef struct nk_headless_Font nk_headless_Font;

NK_API struct nk_context *nk_headless_init(int w, int h, int max_char_width, int font_height);
/* NK_API void nk_sdl_handle_event(SDL_Event *evt);*/
NK_API void nk_headless_clear(struct nk_color clear);
NK_API int* nk_headless_render();
NK_API void nk_headless_shutdown(void);

NK_API nk_flags nk_edit_string2(struct nk_context*, nk_flags, int *buffer, int *len, int max);

#endif
