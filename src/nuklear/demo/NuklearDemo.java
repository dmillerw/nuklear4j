package nuklear.demo;

import nuklear.AWTBackend;
import nuklear.Nuklear4j;
import nuklear.swig.nk_button_behavior;
import nuklear.swig.nk_color;
import nuklear.swig.nk_context;
import nuklear.swig.nk_edit_types;
import nuklear.swig.nk_panel;
import nuklear.swig.nk_panel_flags;
import nuklear.swig.nk_rect;
import nuklear.swig.nuklear;

public class NuklearDemo {

	nk_context ctx;
	AWTBackend backend;

	public void initialize() {
		int screenWidth = 640;
		int screenHeight = 480;

		backend = new AWTBackend();
		backend.initialize(screenWidth, screenHeight);

		Nuklear4j.initializeNative();
		ctx = new nk_context();
		Nuklear4j.initializeContext(ctx, screenWidth, screenHeight, backend.getMaxCharWidth(), backend.getFontHeight());
	}

	public void overview() {
		boolean show_menu = true;
		boolean titlebar = true;
		boolean border = true;
		boolean resize = true;
		boolean movable = true;
		boolean no_scrollbar = true;
		long window_flags = 0;
		boolean minimizable = true;
		boolean close = true;
	}

	// public void calculator() {
	//
	// nk_panel layout = new nk_panel();
	// long flags = nk_panel_flags.NK_WINDOW_BORDER.swigValue() |
	// nk_panel_flags.NK_WINDOW_MOVABLE.swigValue()
	// | nk_panel_flags.NK_WINDOW_NO_SCROLLBAR.swigValue();
	//
	//
	// nk_rect bounds = new nk_rect();
	// bounds.setX(10);
	// bounds.setY(10);
	// bounds.setW(180);
	// bounds.setH(250);
	// if (nuklear.nk_begin(ctx, layout, "Calculator", bounds, flags) ==
	// Nuklear4j.NK_TRUE)
	// {
	// int set = 0, prev = 0, op = 0;
	// char numbers[] = "789456123";
	// char ops[] = "+-*/";
	// double a = 0, b = 0;
	// //double *current = &a;
	//
	// int i = 0;
	// int solve = 0;
	// {
	// int[] len = { 10 };
	// int[] buffer = new int[256];
	// nuklear.nk_layout_row_dynamic(ctx, 35, 1);
	// //len = snprintf(buffer, 256, "%.2f", *current);
	// nuklear.nk_edit_string2(ctx, nk_edit_types.NK_EDIT_SIMPLE.swigValue(),
	// buffer, len, 255);
	// //buffer[len] = 0;
	// //*current = atof(buffer);}
	//
	//
	// nk_layout_row_dynamic(ctx, 35, 4);
	// for (i = 0; i < 16; ++i) {
	// if (i >= 12 && i < 15) {
	// if (i > 12) continue;
	// if (nk_button_label(ctx, "C", NK_BUTTON_DEFAULT)) {
	// a = b = op = 0; current = &a; set = 0;
	// } if (nk_button_label(ctx, "0", NK_BUTTON_DEFAULT)) {
	// *current = *current*10.0f; set = 0;
	// }
	// if (nk_button_label(ctx, "=", NK_BUTTON_DEFAULT)) {
	// solve = 1; prev = op; op = 0;
	// }
	// } else if (((i+1) % 4)) {
	// if (nk_button_text(ctx, &numbers[(i/4)*3+i%4], 1, NK_BUTTON_DEFAULT)) {
	// *current = *current * 10.0f + numbers[(i/4)*3+i%4] - '0';
	// set = 0;
	// }
	// } else if (nk_button_text(ctx, &ops[i/4], 1, NK_BUTTON_DEFAULT)) {
	// if (!set) {
	// if (current != &b) {
	// current = &b;
	// } else {
	// prev = op;
	// solve = 1;
	// }
	// }
	// op = ops[i/4];
	// set = 1;
	// }
	// }
	// if (solve) {
	// if (prev == '+') a = a + b;
	// if (prev == '-') a = a - b;
	// if (prev == '*') a = a * b;
	// if (prev == '/') a = a / b;
	// current = &a;
	// if (set) current = &b;
	// b = 0; set = 0;
	// }
	// }
	// nk_end(ctx);
	// }

	public void simple() {

		int EASY = 0;
		int HARD = 1;
		int op = EASY;
		int[] property = { 20 };
		int[] buffer = new int[64];
		int[] len = { 0 };
		nk_color bgColor = new nk_color();
		bgColor.setA((short) 255);
		bgColor.setR((short) 190);
		bgColor.setG((short) 190);
		bgColor.setB((short) 190);

		nk_panel layout = new nk_panel();
		// nk_rect(50, 50, 210, 250)
		nk_rect bounds = new nk_rect();
		bounds.setX(50);
		bounds.setY(50);
		bounds.setW(210);
		bounds.setH(250);
		long flags = nk_panel_flags.NK_WINDOW_BORDER.swigValue() | nk_panel_flags.NK_WINDOW_MOVABLE.swigValue()
				| nk_panel_flags.NK_WINDOW_SCALABLE.swigValue() | nk_panel_flags.NK_WINDOW_MINIMIZABLE.swigValue()
				| nk_panel_flags.NK_WINDOW_TITLE.swigValue();

		while (true) {

			if (backend.waitEvents(50)) {
				backend.handleEvent(ctx);

				if (nuklear.nk_begin(ctx, layout, "Demo", bounds, flags)) {
					nuklear.nk_layout_row_static(ctx, 30, 80, 1);
					if (nuklear.nk_button_label(ctx, "button", nk_button_behavior.NK_BUTTON_DEFAULT)) {
						System.out.println("button pressed");
					}

					nuklear.nk_layout_row_dynamic(ctx, 30, 2);
					if (nuklear.nk_option_label(ctx, "easy", op == EASY))
						op = EASY;
					if (nuklear.nk_option_label(ctx, "hard", op == HARD))
						op = HARD;
					nuklear.nk_layout_row_dynamic(ctx, 25, 1);
					nuklear.nk_property_int(ctx, "Compression:", 0, property, 100, 10, 1);
					nuklear.nk_edit_string2(ctx, nk_edit_types.NK_EDIT_SIMPLE.swigValue(), buffer, len, buffer.length);

				}
				nuklear.nk_end(ctx);

				backend.clear(bgColor);
				backend.render(ctx);
			}

		}
	}

	public static void main(String argv[]) {
		NuklearDemo demo = new NuklearDemo();
		demo.initialize();
		demo.simple();
	}

}
