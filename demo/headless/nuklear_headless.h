#ifndef NK_SDL_H_
#define NK_SDL_H_

#include "../../nuklear.h"

struct nk_headless_Font {
    int width;
    int height;
    int handle;
};

typedef struct nk_headless_Font nk_headless_Font;

struct nk_headless_Surface {
	long handle;
	int w;
	int h;
};

typedef struct nk_headless_Surface Headless_Surface;

NK_API struct nk_context *nk_headless_init(Headless_Surface *screen_surface);
/* NK_API void nk_sdl_handle_event(SDL_Event *evt);*/
NK_API void nk_headless_render(struct nk_color clear);
NK_API void nk_headless_shutdown(void);

#endif
