package nuklear;

class TriangleFilledCommand extends Command {
	int x0;
	int y0;
	int x1;
	int y1;
	int x2;
	int y2;
	int line_thickness;
	int a;
	int r;
	int g;
	int b;

	public TriangleFilledCommand(int x0, int y0, int x1, int y1, int x2, int y2, int a, int r, int g, int b) {
		super(NK_COMMAND_TRIANGLE_FILLED);
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public String toString() {
		return "TriangleFilledCommand [x0=" + x0 + ", y0=" + y0 + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2="
				+ y2 + ", line_thickness=" + line_thickness + ", a=" + a + ", r=" + r + ", g=" + g + ", b=" + b + "]";
	}

}