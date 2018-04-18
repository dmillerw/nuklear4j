package nuklear;

import nuklear.swig.nk_color;
import nuklear.swig.nk_context;

public interface Backend {

	void clear(nk_color bgColor);

	void handleEvent(nk_context ctx);

	void render(nk_context ctx);

	boolean waitEvents(long delay);

	int getFontHeight();

	int getMaxCharWidth();

}