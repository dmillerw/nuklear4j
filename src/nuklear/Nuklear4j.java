package nuklear;

import nuklear.swig.nk_context;
import nuklear.swig.nuklear;
import nuklear.swig.nuklearConstants;

public class Nuklear4j {

	public static int NK_TRUE;
	public static int NK_FALSE;
	
	public static void initializeNative() {
		System.loadLibrary("nuklear-java");
		
		NK_TRUE = nuklearConstants.nk_true;
		NK_FALSE = nuklearConstants.nk_false;
	}
	
	public static void initializeContext(nk_context ctx, int width, int height, int max_char_width, int font_height) {
		nuklear.nk_headless_init(ctx, width, height, max_char_width, font_height);
	}

}
