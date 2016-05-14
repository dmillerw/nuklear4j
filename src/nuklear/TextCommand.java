package nuklear;

class TextCommand extends Command {
	int x;
	int y;
	int w;
	int h;
	int len;
	int bgA;
	int bgR;
	int bgG;
	int bgB;
	int fgA;
	int fgR;
	int fgG;
	int fgB;
	String s;

	public TextCommand(int x, int y, int w, int h, int len, int bgA, int bgR, int bgG, int bgB, int fgA, int fgR,
			int fgG, int fgB, String s) {
		super(NK_COMMAND_TEXT);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.len = len;
		this.bgA = bgA;
		this.bgR = bgR;
		this.bgG = bgG;
		this.bgB = bgB;
		this.fgA = fgA;
		this.fgR = fgR;
		this.fgG = fgG;
		this.fgB = fgB;
		this.s = s;
	}

	public String toString() {
		return "TextCommand [s=" + s + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", len=" + len + ", bgA="
				+ bgA + ", bgR=" + bgR + ", bgG=" + bgG + ", bgB=" + bgB + ", fgA=" + fgA + ", fgR=" + fgR + ", fgG="
				+ fgG + ", fgB=" + fgB + "]";
	}

}