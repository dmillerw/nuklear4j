package nuklear.demo;

import nuklear.AWTBackend;
import nuklear.swig.nk_button_behavior;
import nuklear.swig.nk_context;
import nuklear.swig.nk_edit_types;
import nuklear.swig.nk_panel;
import nuklear.swig.nk_panel_flags;
import nuklear.swig.nk_rect;
import nuklear.swig.nuklear;
import nuklear.swig.nuklearConstants;

public class NuklearDemo {

	public static int NK_TRUE;
	public static int NK_FALSE;

	public static void main(String argv[]) {
		System.loadLibrary("nuklear-java");

		NK_TRUE = nuklearConstants.nk_true;
		NK_FALSE = nuklearConstants.nk_false;

		int screenWidth = 640;
		int screenHeight = 480;

		AWTBackend backend = new AWTBackend();
		backend.initialize(screenWidth, screenHeight);

		// nuklear.initialize(640, 480);

		nk_context ctx = nuklear.nk_headless_init(screenWidth, screenHeight, backend.getMaxCharWidth(),
				backend.getFontHeight());

		int EASY = 0;
		int HARD = 1;
		int op = EASY;
		int[] property = { 20 };
		int[] buffer = new int[64];
		int[] len = { 0 };

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

			backend.handleEvent(ctx);

			nuklear.nk_begin(ctx, layout, "Demo", bounds, flags);

			nuklear.nk_layout_row_static(ctx, 30, 80, 1);
			if (nuklear.nk_button_label(ctx, "button", nk_button_behavior.NK_BUTTON_DEFAULT) == NK_TRUE) {
				System.out.println("button pressed");
			}

			nuklear.nk_layout_row_dynamic(ctx, 30, 2);
			if (nuklear.nk_option_label(ctx, "easy", op == EASY ? NK_TRUE : NK_FALSE) == NK_TRUE)
				op = EASY;
			if (nuklear.nk_option_label(ctx, "hard", op == HARD ? NK_TRUE : NK_FALSE) == NK_TRUE)
				op = HARD;
			nuklear.nk_layout_row_dynamic(ctx, 25, 1);
			nuklear.nk_property_int(ctx, "Compression:", 0, property, 100, 10, 1);
			nuklear.nk_edit_string2(ctx, nk_edit_types.NK_EDIT_SIMPLE.swigValue(), buffer, len, buffer.length);

			nuklear.nk_end(ctx);

			// System.out.println("Command count=" + commandList.size());

			backend.clear();
			backend.render();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
