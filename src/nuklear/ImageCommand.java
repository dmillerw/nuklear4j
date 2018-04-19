package nuklear;

class ImageCommand extends Command {
	int x;
	int y;
	int w;
	int h;
	int id;

	public ImageCommand(int x, int y, int w, int h, int id) {
		super(NK_COMMAND_IMAGE);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.id = id;
	}

	public String toString() {
		return "ImageCommand [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", id=" + id + "]";
	}

}