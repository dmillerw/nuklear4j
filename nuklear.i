 %module nuklear
 
 %{
//#define NK_INCLUDE_FIXED_TYPES
#define NK_INCLUDE_DEFAULT_ALLOCATOR
#include "nuklear_headless.h"
extern int initialize(int w, int h);
 %}

//%include "carrays.i"
//%array_class(int, IntArray);

#define NK_COMMAND_SCISSOR_SIZE 4
#define NK_COMMAND_LINE_SIZE 9
#define NK_COMMAND_CURVE_SIZE 1
#define NK_COMMAND_RECT_SIZE 10
#define NK_COMMAND_RECT_FILLED_SIZE 9
#define NK_COMMAND_RECT_MULTI_COLOR_SIZE 1
#define NK_COMMAND_CIRCLE_SIZE 9
#define NK_COMMAND_CIRCLE_FILLED_SIZE 8
#define NK_COMMAND_ARC_SIZE 1
#define NK_COMMAND_ARC_FILLED_SIZE 1
#define NK_COMMAND_TRIANGLE_SIZE 11
#define NK_COMMAND_TRIANGLE_FILLED_SIZE 10
#define NK_COMMAND_POLYGON_SIZE 1
#define NK_COMMAND_POLYGON_FILLED_SIZE 1
#define NK_COMMAND_POLYLINE_SIZE 1
#define NK_COMMAND_TEXT_SIZE 13
#define NK_COMMAND_IMAGE_SIZE 1
 
#define NK_API extern
#define NK_FLAG(x) (1 << (x))
#define NK_UTF_SIZE 4
#define NK_INPUT_MAX 16

typedef unsigned char nk_byte;
typedef short nk_short;
typedef unsigned short nk_ushort;
typedef int nk_int;
typedef unsigned int nk_uint;
typedef unsigned long nk_size;
typedef unsigned long nk_ptr;
typedef unsigned int nk_hash;
typedef unsigned int nk_flags;
typedef nk_uint nk_rune;
typedef unsigned char nk_byte;

enum {nk_false, nk_true};
struct nk_color {nk_byte r,g,b,a;};
struct nk_vec2 {float x,y;};
struct nk_vec2i {short x, y;};
struct nk_rect {float x,y,w,h;};
struct nk_recti {short x,y,w,h;};
typedef char nk_glyph[NK_UTF_SIZE];
typedef union {void *ptr; int id;} nk_handle;
struct nk_image {nk_handle handle;unsigned short w,h;unsigned short region[4];};
struct nk_scroll {unsigned short x, y;};
enum nk_heading {NK_UP, NK_RIGHT, NK_DOWN, NK_LEFT};

enum nk_button_behavior {NK_BUTTON_DEFAULT,NK_BUTTON_REPEATER};
enum nk_modify          {NK_FIXED=nk_false,NK_MODIFIABLE=nk_true};
enum nk_orientation     {NK_VERTICAL,NK_HORIZONTAL};
enum nk_collapse_states {NK_MINIMIZED=nk_false,NK_MAXIMIZED = nk_true};
enum nk_show_states     {NK_HIDDEN=nk_false,NK_SHOWN=nk_true};
enum nk_chart_type      {NK_CHART_LINES,NK_CHART_COLUMN,NK_CHART_MAX};
enum nk_chart_event     {NK_CHART_HOVERING=0x01, NK_CHART_CLICKED=0x02};
enum nk_color_format    {NK_RGB, NK_RGBA};
enum nk_popup_type      {NK_POPUP_STATIC,NK_POPUP_DYNAMIC};
enum nk_layout_format   {NK_DYNAMIC,NK_STATIC};
enum nk_tree_type       {NK_TREE_NODE,NK_TREE_TAB};
enum nk_anti_aliasing   {NK_ANTI_ALIASING_OFF,NK_ANTI_ALIASING_ON};

enum nk_keys {
    NK_KEY_NONE,
    NK_KEY_SHIFT,
    NK_KEY_CTRL,
    NK_KEY_DEL,
    NK_KEY_ENTER,
    NK_KEY_TAB,
    NK_KEY_BACKSPACE,
    NK_KEY_COPY,
    NK_KEY_CUT,
    NK_KEY_PASTE,
    NK_KEY_UP,
    NK_KEY_DOWN,
    NK_KEY_LEFT,
    NK_KEY_RIGHT,
    NK_KEY_TEXT_INSERT_MODE,
    NK_KEY_TEXT_LINE_START,
    NK_KEY_TEXT_LINE_END,
    NK_KEY_TEXT_START,
    NK_KEY_TEXT_END,
    NK_KEY_TEXT_UNDO,
    NK_KEY_TEXT_REDO,
    NK_KEY_TEXT_WORD_LEFT,
    NK_KEY_TEXT_WORD_RIGHT,
    NK_KEY_MAX
};

enum nk_buttons {
    NK_BUTTON_LEFT,
    NK_BUTTON_MIDDLE,
    NK_BUTTON_RIGHT,
    NK_BUTTON_MAX
};

enum nk_panel_flags {
    NK_WINDOW_BORDER        = NK_FLAG(0), /* Draws a border around the window to visually separate the window * from the background */
    NK_WINDOW_BORDER_HEADER = NK_FLAG(1), /* Draws a border between window header and body */
    NK_WINDOW_MOVABLE       = NK_FLAG(2), /* The movable flag indicates that a window can be moved by user input or * by dragging the window header */
    NK_WINDOW_SCALABLE      = NK_FLAG(3), /* The scalable flag indicates that a window can be scaled by user input * by dragging a scaler icon at the button of the window */
    NK_WINDOW_CLOSABLE      = NK_FLAG(4), /* adds a closable icon into the header */
    NK_WINDOW_MINIMIZABLE   = NK_FLAG(5), /* adds a minimize icon into the header */
    NK_WINDOW_DYNAMIC       = NK_FLAG(6), /* special window type growing up in height while being filled to a * certain maximum height */
    NK_WINDOW_NO_SCROLLBAR  = NK_FLAG(7), /* Removes the scrollbar from the window */
    NK_WINDOW_TITLE         = NK_FLAG(8) /* Forces a header at the top at the window showing the title */
};

enum nk_edit_flags {
    NK_EDIT_DEFAULT                 = 0,
    NK_EDIT_READ_ONLY               = NK_FLAG(0),
    NK_EDIT_AUTO_SELECT             = NK_FLAG(1),
    NK_EDIT_SIG_ENTER               = NK_FLAG(2),
    NK_EDIT_ALLOW_TAB               = NK_FLAG(3),
    NK_EDIT_NO_CURSOR               = NK_FLAG(4),
    NK_EDIT_SELECTABLE              = NK_FLAG(5),
    NK_EDIT_CLIPBOARD               = NK_FLAG(6),
    NK_EDIT_CTRL_ENTER_NEWLINE      = NK_FLAG(7),
    NK_EDIT_NO_HORIZONTAL_SCROLL    = NK_FLAG(8),
    NK_EDIT_ALWAYS_INSERT_MODE      = NK_FLAG(9),
    NK_EDIT_MULTILINE               = NK_FLAG(11)
};

enum nk_edit_types {
    NK_EDIT_SIMPLE  = NK_EDIT_ALWAYS_INSERT_MODE,
    NK_EDIT_FIELD   = NK_EDIT_SIMPLE|NK_EDIT_SELECTABLE,
    NK_EDIT_BOX     = NK_EDIT_ALWAYS_INSERT_MODE| NK_EDIT_SELECTABLE|
                        NK_EDIT_MULTILINE|NK_EDIT_ALLOW_TAB
};

enum nk_edit_events {
    NK_EDIT_ACTIVE      = NK_FLAG(0), /* edit widget is currently being modified */
    NK_EDIT_INACTIVE    = NK_FLAG(1), /* edit widget is not active and is not being modified */
    NK_EDIT_ACTIVATED   = NK_FLAG(2), /* edit widget went from state inactive to state active */
    NK_EDIT_DEACTIVATED = NK_FLAG(3), /* edit widget went from state active to state inactive */
    NK_EDIT_COMMITED    = NK_FLAG(4) /* edit widget has received an enter and lost focus */
};

struct nk_mouse_button {
    int down;
    unsigned int clicked;
    struct nk_vec2 clicked_pos;
};

struct nk_mouse {
    struct nk_mouse_button buttons[NK_BUTTON_MAX];
    struct nk_vec2 pos;
    struct nk_vec2 prev;
    struct nk_vec2 delta;
    float scroll_delta;
};

struct nk_key {
    int down;
    unsigned int clicked;
};

struct nk_keyboard {
    struct nk_key keys[NK_KEY_MAX];
    char text[NK_INPUT_MAX];
    int text_len;
};

struct nk_input {
    struct nk_keyboard keyboard;
    struct nk_mouse mouse;
};

struct nk_context {
    struct nk_input input;
    struct nk_style style;
//    struct nk_buffer memory;
//    struct nk_clipboard clip;
    nk_flags last_widget_state;
};

struct nk_panel {
    nk_flags flags;
};

/* User Input */
NK_API void nk_input_begin(struct nk_context*);
NK_API void nk_input_motion(struct nk_context*, int x, int y);
NK_API void nk_input_key(struct nk_context*, enum nk_keys, int down);
NK_API void nk_input_button(struct nk_context*, enum nk_buttons, int x, int y, int down);
NK_API void nk_input_scroll(struct nk_context*, float y);
NK_API void nk_input_char(struct nk_context*, char c);
NK_API void nk_input_end(struct nk_context*);

/* window */
NK_API int nk_begin(struct nk_context *ctx, struct nk_panel *layout, const char *title, struct nk_rect bounds, nk_flags flags);
NK_API void nk_end(struct nk_context*);
 
 /* Layout */
NK_API void nk_layout_row_dynamic(struct nk_context*, float height, int cols);
NK_API void nk_layout_row_static(struct nk_context*, float height, int item_width, int cols);

/* Widgets: Buttons */
NK_API int nk_button_text(struct nk_context *ctx, const char *title, int len, enum nk_button_behavior);
NK_API int nk_button_label(struct nk_context *ctx, const char *title, enum nk_button_behavior);

/* Widgets: Radio */
NK_API int nk_radio_label(struct nk_context*, const char*, int *active);
NK_API int nk_radio_text(struct nk_context*, const char*, int, int *active);
NK_API int nk_option_label(struct nk_context*, const char*, int active);
NK_API int nk_option_text(struct nk_context*, const char*, int, int active);

enum nk_command_type {
    NK_COMMAND_NOP,
    NK_COMMAND_SCISSOR,
    NK_COMMAND_LINE,
    NK_COMMAND_CURVE,
    NK_COMMAND_RECT,
    NK_COMMAND_RECT_FILLED,
    NK_COMMAND_RECT_MULTI_COLOR,
    NK_COMMAND_CIRCLE,
    NK_COMMAND_CIRCLE_FILLED,
    NK_COMMAND_ARC,
    NK_COMMAND_ARC_FILLED,
    NK_COMMAND_TRIANGLE,
    NK_COMMAND_TRIANGLE_FILLED,
    NK_COMMAND_POLYGON,
    NK_COMMAND_POLYGON_FILLED,
    NK_COMMAND_POLYLINE,
    NK_COMMAND_TEXT,
    NK_COMMAND_IMAGE
};

%include "arrays_java.i"
%apply int[] {int *};
%apply float[] {float *};

/* Widgets: Property */
NK_API void nk_property_float(struct nk_context *layout, const char *name, float min, float *val, float max, float step, float inc_per_pixel);
NK_API void nk_property_int(struct nk_context *layout, const char *name, int min, int *val, int max, int step, int inc_per_pixel);
NK_API float nk_propertyf(struct nk_context *layout, const char *name, float min, float val, float max, float step, float inc_per_pixel);
NK_API int nk_propertyi(struct nk_context *layout, const char *name, int min, int val, int max, int step, int inc_per_pixel);


/* Widgets: TextEdit */
NK_API nk_flags nk_edit_string2(struct nk_context*, nk_flags, int *buffer, int *len, int max);
NK_API nk_flags nk_edit_buffer(struct nk_context*, nk_flags, struct nk_text_edit*, nk_filter);
 
extern int initialize(int w, int h);
NK_API void nk_headless_init(struct nk_context* ctx, int w, int h, int max_char_width, int font_height);
NK_API void nk_headless_render(struct nk_context* ctx, int* draw_buffer);
 