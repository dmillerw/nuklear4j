package nuklear;

class RectCommand extends Command {
	int x;
	int y;
	int w;
	int h;
	int rounded;
	int line_thickness;
	int a;
	int r;
	int g;
	int b;

	public RectCommand(int x, int y, int w, int h, int rounded, int line_thickness, int a, int r, int g, int b) {
		super(NK_COMMAND_RECT);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rounded = rounded;
		this.line_thickness = line_thickness;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public String toString() {
		return "RectCommand [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", rounded=" + rounded
				+ ", line_thickness=" + line_thickness + ", a=" + a + ", r=" + r + ", g=" + g + ", b=" + b + "]";
	}

}