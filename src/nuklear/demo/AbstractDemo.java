package nuklear.demo;

import java.io.InputStream;

import nuklear.Backend;
import nuklear.Nuklear4j;
import nuklear.swig.nk_button_behavior;
import nuklear.swig.nk_color;
import nuklear.swig.nk_context;
import nuklear.swig.nk_edit_types;
import nuklear.swig.nk_image;
import nuklear.swig.nk_layout_format;
import nuklear.swig.nk_modify;
import nuklear.swig.nk_panel;
import nuklear.swig.nk_panel_flags;
import nuklear.swig.nk_popup_type;
import nuklear.swig.nk_rect;
import nuklear.swig.nk_style_header_align;
import nuklear.swig.nk_text_alignment;
import nuklear.swig.nuklear;


/**
 * Simple backend-agnostic demo
 */
public abstract class AbstractDemo {

	private nk_context ctx;
	private Backend backend;
	
	public void initialize() {
		int screenWidth = 640;
		int screenHeight = 480;

		backend = createBackend(screenWidth, screenHeight);
		
		Nuklear4j.initializeNative();
		ctx = new nk_context();
		Nuklear4j.initializeContext(ctx, screenWidth, screenHeight, backend.getMaxCharWidth(), backend.getFontHeight());
	}
	
	public abstract Backend createBackend(int screenWidth, int screenHeight);

	public void overview() {

		nk_color bgColor = new nk_color();
		bgColor.setA((short) 255);
		bgColor.setR((short) 190);
		bgColor.setG((short) 190);
		bgColor.setB((short) 190);

		boolean show_menu = true;
		boolean titlebar = true;
		boolean border = true;
		boolean resize = true;
		boolean movable = true;
		boolean no_scrollbar = true;
		boolean minimizable = true;
		boolean close = true;

		nk_panel layout = new nk_panel();
		nk_panel menu = new nk_panel();
		/* popups */
		int header_align = nk_style_header_align.NK_HEADER_RIGHT;
		boolean show_app_about = false;

		long window_flags = 0;
		if (border)
			window_flags |= nk_panel_flags.NK_WINDOW_BORDER;
		if (resize)
			window_flags |= nk_panel_flags.NK_WINDOW_SCALABLE;
		if (movable)
			window_flags |= nk_panel_flags.NK_WINDOW_MOVABLE;
		if (no_scrollbar)
			window_flags |= nk_panel_flags.NK_WINDOW_NO_SCROLLBAR;
		if (minimizable)
			window_flags |= nk_panel_flags.NK_WINDOW_MINIMIZABLE;
		if (close)
			window_flags |= nk_panel_flags.NK_WINDOW_CLOSABLE;

		nk_rect bounds = new nk_rect();
		bounds.setX(10);
		bounds.setY(10);
		bounds.setW(400);
		bounds.setH(400);

		int MENU_DEFAULT = 0;
		int MENU_WINDOWS = 1;
		int[] mprog = { 60 };
		int[] mslider = { 10 };
		int[] mcheck = { Nuklear4j.NK_TRUE };

		int[] prog = { 40 };
		int[] slider = { 10 };
		int[] check = { Nuklear4j.NK_TRUE };
		
		
		int editBufferMaxSize = 255;
		String initString = "You can edit me !";
		StringBuffer stringBuffer = new StringBuffer(editBufferMaxSize);
		stringBuffer.append(initString);
		stringBuffer.setLength(editBufferMaxSize);
		int[] editBufferCurrentLength = { initString.length() };
		byte[] byteBuffer = stringBuffer.toString().getBytes();
		
		InputStream is = getClass().getResourceAsStream("/image7.png");
		nk_image image = backend.createImage(is);
		
		/*
		 * We choose to render only if an event occurs
		 */
		boolean firstLoop = true;
		while (true) {

			if (backend.waitEvents(50) || firstLoop) {
				backend.handleEvent(ctx);

				if (nuklear.nk_begin(ctx, layout, "Overview", bounds, window_flags)) {
					if (show_menu) {
						/* menubar */
						nuklear.nk_menubar_begin(ctx);
						nuklear.nk_layout_row_begin(ctx, nk_layout_format.NK_STATIC, 25, 4);
						nuklear.nk_layout_row_push(ctx, 70);
						if (nuklear.nk_menu_begin_label(ctx, menu, "MENU", nk_text_alignment.NK_TEXT_LEFT, 120)) {

							nuklear.nk_layout_row_dynamic(ctx, 25, 1);
							if (nuklear.nk_menu_item_label(ctx, "Hide", nk_text_alignment.NK_TEXT_LEFT))
								show_menu = false;
							if (nuklear.nk_menu_item_label(ctx, "About", nk_text_alignment.NK_TEXT_LEFT))
								show_app_about = true;
							nuklear.nk_progress(ctx, prog, 100, nk_modify.NK_MODIFIABLE);
							nuklear.nk_slider_int(ctx, 0, slider, 16, 1);
							nuklear.nk_checkbox_label(ctx, "check", check);
							nuklear.nk_menu_end(ctx);
						}
						nuklear.nk_layout_row_push(ctx, 70);
						nuklear.nk_progress(ctx, mprog, 100, nk_modify.NK_MODIFIABLE);
						nuklear.nk_slider_int(ctx, 0, mslider, 16, 1);
						nuklear.nk_layout_row_push(ctx, 90);
						nuklear.nk_checkbox_label(ctx, "check", mcheck);
						nuklear.nk_menubar_end(ctx);
						nuklear.nk_layout_row_end(ctx);
						
						nuklear.nk_layout_row_begin(ctx, nk_layout_format.NK_DYNAMIC, 200, 1);
						nuklear.nk_layout_row_push(ctx, 1.0f);
						//nuklear.nk_edit_string(ctx, nk_edit_types.NK_EDIT_BOX, stringBuffer, editBufferCurrentLength, editBufferMaxSize);
						nuklear.nk_edit_string2(ctx, nk_edit_types.NK_EDIT_BOX, byteBuffer, editBufferCurrentLength, editBufferMaxSize); 
						nuklear.nk_layout_row_end(ctx);
						
						nuklear.nk_layout_row_begin(ctx, nk_layout_format.NK_DYNAMIC, 80, 1);
						nuklear.nk_layout_row_push(ctx, 0.5f);
						nuklear.nk_button_image(ctx, image, nk_button_behavior.NK_BUTTON_DEFAULT);
						//nuklear.nk_button_image_label(ctx, image, "home", nk_text_alignment.NK_TEXT_CENTERED, nk_button_behavior.NK_BUTTON_DEFAULT);
						nuklear.nk_layout_row_end(ctx);
						
					}

					if (show_app_about) {
						/* about popup */
						nk_panel popup = new nk_panel();
						nk_rect s = new nk_rect();
						s.setX(20);
						s.setY(100);
						s.setW(300);
						s.setH(190);
						if (nuklear.nk_popup_begin(ctx, popup, nk_popup_type.NK_POPUP_STATIC, "About", nk_panel_flags.NK_WINDOW_CLOSABLE, s)) {
							nuklear.nk_layout_row_dynamic(ctx, 20, 1);
							nuklear.nk_label(ctx, "Nuklear", nk_text_alignment.NK_TEXT_LEFT);
							nuklear.nk_label(ctx, "By Micha Mettke", nk_text_alignment.NK_TEXT_LEFT);
							nuklear.nk_label(ctx, "nuklear is licensed under the public domain License.", nk_text_alignment.NK_TEXT_LEFT);
							nuklear.nk_popup_end(ctx);
						} else
							show_app_about = false;
					}

				} // if (nuklear.nk_begin)
				nuklear.nk_end(ctx);

				backend.clear(bgColor);
				backend.render(ctx);
				firstLoop = false; // Ugly
			}

		} // while

	}

//	public void calculator() {
//	
//	 nk_panel layout = new nk_panel();
//	 long flags = nk_panel_flags.NK_WINDOW_BORDER | nk_panel_flags.NK_WINDOW_MOVABLE | nk_panel_flags.NK_WINDOW_NO_SCROLLBAR;
//	
//	
//	 nk_rect bounds = new nk_rect();
//	 bounds.setX(10);
//	 bounds.setY(10);
//	 bounds.setW(180);
//	 bounds.setH(250);
//	 if (nuklear.nk_begin(ctx, layout, "Calculator", bounds, flags)) {
//	 boolean set = false;
//	 boolean prev = false;
//	 boolean op = false;
//	 char numbers[] = { '7', '8', '9', '4', '5', '6' ,'1', '2', '3' };
//	 char ops[] = { '+', '-', '*', '/' };
//	 double a = 0, b = 0;
//	 double current = a;
//	
//	 int i = 0;
//	 boolean solve = false;
//	 {
//	 int[] len = { 10 };
//	 int[] buffer = new int[256];
//	 nuklear.nk_layout_row_dynamic(ctx, 35, 1);
//	 //len = snprintf(buffer, 256, "%.2f", *current);
//	 nuklear.nk_edit_string2(ctx, nk_edit_types.NK_EDIT_SIMPLE, buffer, len, 255);
//	 //buffer[len] = 0;
//	 //*current = atof(buffer);}
//	
//	
//	 nuklear.nk_layout_row_dynamic(ctx, 35, 4);
//	 for (i = 0; i < 16; ++i) {
//	 if (i >= 12 && i < 15) {
//	 if (i > 12) continue;
//	 if (nuklear.nk_button_label(ctx, "C", nk_button_behavior.NK_BUTTON_DEFAULT)) {
//	 a = b = 0;
//	 op = false; 
//	 current = &a; set = false;
//	 } if (nuklear.nk_button_label(ctx, "0", nk_button_behavior.NK_BUTTON_DEFAULT)) {
//	 *current = *current*10.0f; set = false;
//	 }
//	 if (nuklear.nk_button_label(ctx, "=", nk_button_behavior.NK_BUTTON_DEFAULT)) {
//	 solve = true; prev = op; op = 0;
//	 }
//	 } else if (((i+1) % 4)) {
//	 if (nuklear.nk_button_text(ctx, &numbers[(i/4)*3+i%4], 1, nk_button_behavior.NK_BUTTON_DEFAULT)) {
//	 *current = *current * 10.0f + numbers[(i/4)*3+i%4] - '0';
//	 set = false;
//	 }
//	 } else if (nk_button_text(ctx, &ops[i/4], 1, nk_button_behavior.NK_BUTTON_DEFAULT)) {
//	 if (!set) {
//	 if (current != &b) {
//	 current = &b;
//	 } else {
//	 prev = op;
//	 solve = true;
//	 }
//	 }
//	 op = ops[i/4];
//	 set = false;
//	 }
//	 }
//	 if (solve) {
//	 if (prev == '+') a = a + b;
//	 if (prev == '-') a = a - b;
//	 if (prev == '*') a = a * b;
//	 if (prev == '/') a = a / b;
//	 current = &a;
//	 if (set) current = &b;
//	 b = 0; set = false;
//	 }
//	 }
//	 nk_end(ctx);
//	 }

	public void simple() {

		int EASY = 0;
		int HARD = 1;
		int op = EASY;
		int[] property = { 20 };
		byte[] buffer = new byte[64];
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
		long flags = nk_panel_flags.NK_WINDOW_BORDER | nk_panel_flags.NK_WINDOW_MOVABLE | nk_panel_flags.NK_WINDOW_SCALABLE
				| nk_panel_flags.NK_WINDOW_MINIMIZABLE | nk_panel_flags.NK_WINDOW_TITLE;

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
					nuklear.nk_edit_string2(ctx, nk_edit_types.NK_EDIT_SIMPLE, buffer.toString().getBytes(), len, buffer.length);

				}
				nuklear.nk_end(ctx);

				backend.clear(bgColor);
				backend.render(ctx);
			}

		}
	}


}
