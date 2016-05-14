package nuklear;

class CircleCommand extends Command {
	int x;
	int y;
	int w;
	int h;
	int line_thickness;
	int a;
	int r;
	int g;
	int b;

	public CircleCommand(int x, int y, int w, int h, int line_thickness, int a, int r, int g, int b) {
		super(NK_COMMAND_CIRCLE);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.line_thickness = line_thickness;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public String toString() {
		return "CircleCommand [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", line_thickness=" + line_thickness
				+ ", a=" + a + ", r=" + r + ", g=" + g + ", b=" + b + "]";
	}

}