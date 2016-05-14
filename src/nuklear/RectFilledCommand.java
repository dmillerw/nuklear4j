package nuklear;

class RectFilledCommand extends Command {
	int x;
	int y;
	int w;
	int h;
	int rounded;
	int a;
	int r;
	int g;
	int b;

	public RectFilledCommand(int x, int y, int w, int h, int rounded, int a, int r, int g, int b) {
		super(NK_COMMAND_RECT_FILLED);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rounded = rounded;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public String toString() {
		return "RectFilledCommand [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", rounded=" + rounded + ", a=" + a
				+ ", r=" + r + ", g=" + g + ", b=" + b + "]";
	}

}