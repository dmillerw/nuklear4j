package nuklear;

class ScissorCommand extends Command {
	int x;
	int y;
	int w;
	int h;

	public ScissorCommand(int x, int y, int w, int h) {
		super(NK_COMMAND_SCISSOR);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public String toString() {
		return "ScissorCommand [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
	}

}