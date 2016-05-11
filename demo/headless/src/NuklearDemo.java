import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class NuklearDemo {

	public static int NK_TRUE;
	public static int NK_FALSE;

	public static void main(String argv[]) {
		System.loadLibrary("nuklear-java");

		NK_TRUE = nuklearConstants.nk_true;
		NK_FALSE = nuklearConstants.nk_false;

		int screenWidth = 640;
		int screenHeight = 480;

		AWTBackend backend = new AWTBackend();
		backend.initialize(screenWidth, screenHeight);

		// nuklear.initialize(640, 480);

		nk_context ctx = nuklear.nk_headless_init(screenWidth, screenHeight, backend.getMaxCharWidth(),
				backend.getFontHeight());

		int EASY = 0;
		int HARD = 1;
		int op = EASY;
		int[] property = { 20 };
		int[] buffer = new int[64];
		int[] len = { 0 };

		nk_panel layout = new nk_panel();
		// nk_rect(50, 50, 210, 250)
		nk_rect bounds = new nk_rect();
		bounds.setX(50);
		bounds.setY(50);
		bounds.setW(210);
		bounds.setH(250);
		long flags = nk_panel_flags.NK_WINDOW_BORDER.swigValue() | nk_panel_flags.NK_WINDOW_MOVABLE.swigValue()
				| nk_panel_flags.NK_WINDOW_SCALABLE.swigValue() | nk_panel_flags.NK_WINDOW_MINIMIZABLE.swigValue()
				| nk_panel_flags.NK_WINDOW_TITLE.swigValue();

		while (true) {

			backend.handleEvent(ctx);

			nuklear.nk_begin(ctx, layout, "Demo", bounds, flags);

			nuklear.nk_layout_row_static(ctx, 30, 80, 1);
			if (nuklear.nk_button_label(ctx, "button", nk_button_behavior.NK_BUTTON_DEFAULT) == NK_TRUE) {
				System.out.println("button pressed");
			}

			nuklear.nk_layout_row_dynamic(ctx, 30, 2);
			if (nuklear.nk_option_label(ctx, "easy", op == EASY ? NK_TRUE : NK_FALSE) == NK_TRUE)
				op = EASY;
			if (nuklear.nk_option_label(ctx, "hard", op == HARD ? NK_TRUE : NK_FALSE) == NK_TRUE)
				op = HARD;
			nuklear.nk_layout_row_dynamic(ctx, 25, 1);
			nuklear.nk_property_int(ctx, "Compression:", 0, property, 100, 10, 1);
			nuklear.nk_edit_string2(ctx, nk_edit_types.NK_EDIT_SIMPLE.swigValue(), buffer, len, buffer.length);

			nuklear.nk_end(ctx);

			int[] intBuffer = new int[10000];
			nuklear.nk_headless_render(intBuffer);

			Vector commandList = new Vector();
			Command.build(intBuffer, commandList);

			// System.out.println("Command count=" + commandList.size());

			backend.clear();
			backend.render(commandList);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

abstract class Command {

	public int getType() {
		return type;
	}

	public static final int NK_COMMAND_SCISSOR = nk_command_type.NK_COMMAND_SCISSOR.swigValue();
	public static final int NK_COMMAND_LINE = nk_command_type.NK_COMMAND_LINE.swigValue();
	public static final int NK_COMMAND_RECT = nk_command_type.NK_COMMAND_RECT.swigValue();
	public static final int NK_COMMAND_RECT_FILLED = nk_command_type.NK_COMMAND_RECT_FILLED.swigValue();
	public static final int NK_COMMAND_CIRCLE = nk_command_type.NK_COMMAND_CIRCLE.swigValue();
	public static final int NK_COMMAND_CIRCLE_FILLED = nk_command_type.NK_COMMAND_CIRCLE_FILLED.swigValue();
	public static final int NK_COMMAND_TRIANGLE = nk_command_type.NK_COMMAND_TRIANGLE.swigValue();
	public static final int NK_COMMAND_TRIANGLE_FILLED = nk_command_type.NK_COMMAND_TRIANGLE_FILLED.swigValue();
	public static final int NK_COMMAND_TEXT = nk_command_type.NK_COMMAND_TEXT.swigValue();
	private int type;

	public Command(int type) {
		this.type = type;
	}

	static void build(int[] drawBuffer, Vector commandList) {

		int index = 0;
		int commandCount = drawBuffer[index++];
		Command[] commands = new Command[commandCount];

		for (int i = 0; i < commandCount; i++) {

			int commandType = drawBuffer[index++];
			int commandSize = drawBuffer[index++];

			if (commandType == Command.NK_COMMAND_SCISSOR) {
				checkSize(commandSize, nuklearConstants.NK_COMMAND_SCISSOR_SIZE);
				int x = drawBuffer[index++];
				int y = drawBuffer[index++];
				int w = drawBuffer[index++];
				int h = drawBuffer[index++];
				commandList.add(new ScissorCommand(x, y, w, h));
				// System.out.println("Command: SCISSOR " + x + " " + y + " " +
				// w + " " + h);

			} else if (commandType == Command.NK_COMMAND_LINE) {
				checkSize(commandSize, nuklearConstants.NK_COMMAND_LINE_SIZE);
				int x0 = drawBuffer[index++];
				int y0 = drawBuffer[index++];
				int x1 = drawBuffer[index++];
				int y1 = drawBuffer[index++];
				int line_thickness = drawBuffer[index++];
				int a = drawBuffer[index++];
				int r = drawBuffer[index++];
				int g = drawBuffer[index++];
				int b = drawBuffer[index++];
				commandList.add(new LineCommand(x0, y0, x1, y1, line_thickness, a, r, g, b));
				// System.out.println("Command: LINE " + x0 + " " + y0 + " " +
				// x1 + " " + y1);
			} else if (commandType == Command.NK_COMMAND_RECT) {
				checkSize(commandSize, nuklearConstants.NK_COMMAND_RECT_SIZE);
				int x = drawBuffer[index++];
				int y = drawBuffer[index++];
				int w = drawBuffer[index++];
				int h = drawBuffer[index++];
				int rounded = drawBuffer[index++];
				int line_thickness = drawBuffer[index++];
				int a = drawBuffer[index++];
				int r = drawBuffer[index++];
				int g = drawBuffer[index++];
				int b = drawBuffer[index++];
				commandList.add(new RectCommand(x, y, w, h, rounded, line_thickness, a, r, g, b));
				// System.out.println("Command: RECT " + x + " " + y + " " + w +
				// " " + h);

			} else if (commandType == Command.NK_COMMAND_RECT_FILLED) {
				checkSize(commandSize, nuklearConstants.NK_COMMAND_RECT_FILLED_SIZE);
				int x = drawBuffer[index++];
				int y = drawBuffer[index++];
				int w = drawBuffer[index++];
				int h = drawBuffer[index++];
				int rounded = drawBuffer[index++];
				int a = drawBuffer[index++];
				int r = drawBuffer[index++];
				int g = drawBuffer[index++];
				int b = drawBuffer[index++];
				commandList.add(new RectFilledCommand(x, y, w, h, rounded, a, r, g, b));
				// System.out.println("Command: RECT_FILLED " + x + " " + y + "
				// " + w + " " + h);

			} else if (commandType == Command.NK_COMMAND_CIRCLE) {
				checkSize(commandSize, nuklearConstants.NK_COMMAND_CIRCLE_SIZE);
				int x = drawBuffer[index++];
				int y = drawBuffer[index++];
				int w = drawBuffer[index++];
				int h = drawBuffer[index++];
				int line_thickness = drawBuffer[index++];
				int a = drawBuffer[index++];
				int r = drawBuffer[index++];
				int g = drawBuffer[index++];
				int b = drawBuffer[index++];
				commandList.add(new CircleCommand(x, y, w, h, line_thickness, a, r, g, b));
				// System.out.println("Command: CIRCLE " + x + " " + y + " " + w
				// + " " + h);

			} else if (commandType == Command.NK_COMMAND_CIRCLE_FILLED) {
				checkSize(commandSize, nuklearConstants.NK_COMMAND_CIRCLE_FILLED_SIZE);
				int x = drawBuffer[index++];
				int y = drawBuffer[index++];
				int w = drawBuffer[index++];
				int h = drawBuffer[index++];
				int a = drawBuffer[index++];
				int r = drawBuffer[index++];
				int g = drawBuffer[index++];
				int b = drawBuffer[index++];
				commandList.add(new CircleFilledCommand(x, y, w, h, a, r, g, b));
				// System.out.println("Command: CIRCLE_FILLED " + x + " " + y +
				// " " + w + " " + h);

			} else if (commandType == Command.NK_COMMAND_TRIANGLE) {
				checkSize(commandSize, nuklearConstants.NK_COMMAND_TRIANGLE_SIZE);
				int x0 = drawBuffer[index++];
				int y0 = drawBuffer[index++];
				int x1 = drawBuffer[index++];
				int y1 = drawBuffer[index++];
				int x2 = drawBuffer[index++];
				int y2 = drawBuffer[index++];
				int line_thickness = drawBuffer[index++];
				int a = drawBuffer[index++];
				int r = drawBuffer[index++];
				int g = drawBuffer[index++];
				int b = drawBuffer[index++];
				commandList.add(new TriangleCommand(x0, y0, x1, y1, x2, y2, line_thickness, a, r, g, b));
				// System.out.println("Command: TRIANGLE " + x0 + " " + y0 + " "
				// + x1 + " " + y1 + " " + x2 + " " + y2);

			} else if (commandType == Command.NK_COMMAND_TRIANGLE_FILLED) {
				checkSize(commandSize, nuklearConstants.NK_COMMAND_TRIANGLE_FILLED_SIZE);
				int x0 = drawBuffer[index++];
				int y0 = drawBuffer[index++];
				int x1 = drawBuffer[index++];
				int y1 = drawBuffer[index++];
				int x2 = drawBuffer[index++];
				int y2 = drawBuffer[index++];
				int a = drawBuffer[index++];
				int r = drawBuffer[index++];
				int g = drawBuffer[index++];
				int b = drawBuffer[index++];
				commandList.add(new TriangleFilledCommand(x0, y0, x1, y1, x2, y2, a, r, g, b));
				// System.out.println("Command: TRIANGLE " + x0 + " " + y0 + " "
				// + x1 + " " + y1 + " " + x2 + " " + y2);

			} else if (commandType == Command.NK_COMMAND_TEXT) {

				int x = drawBuffer[index++];
				int y = drawBuffer[index++];
				int w = drawBuffer[index++];
				int h = drawBuffer[index++];
				int len = drawBuffer[index++];
				checkSize(commandSize, nuklearConstants.NK_COMMAND_TEXT_SIZE + len);
				int bgA = drawBuffer[index++];
				int bgR = drawBuffer[index++];
				int bgG = drawBuffer[index++];
				int bgB = drawBuffer[index++];
				int fgA = drawBuffer[index++];
				int fgR = drawBuffer[index++];
				int fgG = drawBuffer[index++];
				int fgB = drawBuffer[index++];

				char[] chars = new char[len];
				for (int j = 0; j < len; j++) {
					chars[j] = (char) drawBuffer[index++];
				}
				String s = new String(chars);

				commandList.add(new TextCommand(x, y, w, h, len, bgA, bgR, bgG, bgB, fgA, fgR, fgG, fgB, s));
				// System.out.println("Command: TEXT " + x + " " + y + " " + w +
				// " " + h + " " + s);

			} else {
				throw new RuntimeException("Unknown command : " + commandType);
			}
		}

	}

	private static void checkSize(int commandSize, int expectedSize) {
		if (commandSize != expectedSize) {
			System.out.println("ERROR");
			throw new RuntimeException();
		}
	}
}

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

class LineCommand extends Command {
	int x0;
	int y0;
	int x1;
	int y1;
	int line_thickness;
	int a;
	int r;
	int g;
	int b;

	public LineCommand(int x0, int y0, int x1, int y1, int line_thickness, int a, int r, int g, int b) {
		super(NK_COMMAND_LINE);
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.line_thickness = line_thickness;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public String toString() {
		return "LineCommand [x0=" + x0 + ", y0=" + y0 + ", x1=" + x1 + ", y1=" + y1 + ", line_thickness="
				+ line_thickness + ", a=" + a + ", r=" + r + ", g=" + g + ", b=" + b + "]";
	}

}

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

class CircleFilledCommand extends Command {
	int x;
	int y;
	int w;
	int h;
	int a;
	int r;
	int g;
	int b;

	public CircleFilledCommand(int x, int y, int w, int h, int a, int r, int g, int b) {
		super(NK_COMMAND_CIRCLE_FILLED);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public CircleFilledCommand(int type, int x, int y, int w, int h, int a, int r, int g, int b) {
		super(type);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public String toString() {
		return "CircleFilledCommand [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", a=" + a + ", r=" + r + ", g="
				+ g + ", b=" + b + "]";
	}

}

class TriangleCommand extends Command {
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

	public TriangleCommand(int x0, int y0, int x1, int y1, int x2, int y2, int line_thickness, int a, int r, int g,
			int b) {
		super(NK_COMMAND_TRIANGLE);
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.line_thickness = line_thickness;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public String toString() {
		return "TriangleCommand [x0=" + x0 + ", y0=" + y0 + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2
				+ ", line_thickness=" + line_thickness + ", a=" + a + ", r=" + r + ", g=" + g + ", b=" + b + "]";
	}

}

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

class AWTBackend implements MouseMotionListener, MouseListener, KeyListener {

	private Canvas canvas = new Canvas();
	Font font; 
	private int screenWidth;
	private int screenHeight;
	private BufferedImage screenImage;
	FontMetrics fontMetrics;
	private JPanel panel;
	private JFrame frame;
	private Vector eventQueue;

	public int getFontHeight() {
		return fontMetrics.getHeight();
	}

	public int getMaxCharWidth() {
		return fontMetrics.stringWidth("W");
	}

	public void initialize(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		screenImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB_PRE);
		font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		fontMetrics = screenImage.getGraphics().getFontMetrics(font);

		final Dimension dimension = new Dimension(screenWidth, screenHeight);
		panel = new JPanel() {

			public Dimension getMinimumSize() {
				return dimension;
			}

			public Dimension getPreferredSize() {
				return dimension;
			}

			public void paintComponent(java.awt.Graphics g) {
				super.paintComponent(g);
				g.drawImage(screenImage, 0, 0, null);
			}
		};
		panel.setFocusable(true);

		// Events
		eventQueue = new Vector();
		panel.addMouseMotionListener(this);
		panel.addMouseListener(this);
		panel.addKeyListener(this);

		// Show
		frame = new JFrame();
		// frame.addWindowListener(this);
		frame.add(panel);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null); // Center the frame (has to be called
											// after pack)
		frame.setVisible(true);
		panel.requestFocusInWindow();

	}

	public void clear() {

		screenImage.getGraphics().setColor(Color.GRAY);
		screenImage.getGraphics().fillRect(0, 0, screenWidth, screenHeight);
	}

	public void handleEvent(nk_context ctx) {

		nuklear.nk_input_begin(ctx);

		synchronized (eventQueue) {
			int size = eventQueue.size();
			if (size > 0) {
				Object o = eventQueue.remove(0);
				if (o instanceof MouseEvent) {
					MouseEvent me = (MouseEvent) o;

					if (me.getID() == MouseEvent.MOUSE_MOVED) {
						// System.out.println("MOUSE_MOVED");
						nuklear.nk_input_motion(ctx, me.getX(), me.getY());
					} else if (me.getID() == MouseEvent.MOUSE_PRESSED) {
						// System.out.println("MOUSE_PRESSED");
						// int NuklearDemo.NK_TRUE = (me.getID() ==
						// MouseEvent.MOUSE_PRESSED) ? NuklearDemo.NK_TRUE :
						// NuklearDemo.NK_FALSE;
						int modifiers = me.getModifiersEx();
						int button = me.getButton();
						if (button == MouseEvent.BUTTON1) {
							// System.out.println("MOUSE_PRESSED: BUTTON1");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_LEFT, me.getX(), me.getY(),
									NuklearDemo.NK_TRUE);
						} else if (button == MouseEvent.BUTTON3) {
							// System.out.println("MOUSE_PRESSED: BUTTON3");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_RIGHT, me.getX(), me.getY(),
									NuklearDemo.NK_TRUE);
						}
					} else if (me.getID() == MouseEvent.MOUSE_RELEASED) {
						// int NuklearDemo.NK_TRUE = (me.getID() ==
						// MouseEvent.MOUSE_PRESSED) ? NuklearDemo.NK_TRUE :
						// NuklearDemo.NK_FALSE;
						int button = me.getButton();
						if (button == MouseEvent.BUTTON1) {
							// System.out.println("MOUSE_RELEASED: BUTTON1");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_LEFT, me.getX(), me.getY(),
									NuklearDemo.NK_FALSE);
						} else if (button == MouseEvent.BUTTON3) {
							// System.out.println("MOUSE_RELEASED: BUTTON3");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_RIGHT, me.getX(), me.getY(),
									NuklearDemo.NK_FALSE);
						}
					} else if (me.getID() == MouseEvent.MOUSE_DRAGGED) {
						// int NuklearDemo.NK_TRUE = (me.getID() ==
						// MouseEvent.MOUSE_PRESSED) ? NuklearDemo.NK_TRUE :
						// NuklearDemo.NK_FALSE;
						int button = me.getButton();
						if (button == MouseEvent.BUTTON1) {
							// System.out.println("MOUSE_RELEASED: BUTTON1");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_LEFT, me.getX(), me.getY(),
									NuklearDemo.NK_TRUE);
						} else if (button == MouseEvent.BUTTON3) {
							// System.out.println("MOUSE_RELEASED: BUTTON3");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_RIGHT, me.getX(), me.getY(),
									NuklearDemo.NK_TRUE);
						}
						nuklear.nk_input_motion(ctx, me.getX(), me.getY());
					}

					// nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_LEFT,
					// me.getX(), me.getY(), down);

				} else if (o instanceof KeyEvent) {
					KeyEvent e = (KeyEvent) o;
					char c = e.getKeyChar();
					int keyCode = e.getKeyCode();
					boolean pressed = (e.getID() == KeyEvent.KEY_PRESSED);
					if (keyCode == KeyEvent.VK_BACK_SPACE) {
						nuklear.nk_input_key(ctx, nk_keys.NK_KEY_BACKSPACE,
								pressed ? NuklearDemo.NK_TRUE : NuklearDemo.NK_FALSE);
					} else {
						if (!pressed) {
							if (c != KeyEvent.CHAR_UNDEFINED) {
								nuklear.nk_input_char(ctx, c);
							}
						}
					}
				}
			}
		}

		nuklear.nk_input_end(ctx);

		// nk_input_motion(ctx, evt->motion.x, evt->motion.y);
	}

	public void render(Vector commandList) {
		int size = commandList.size();

		Graphics g = screenImage.getGraphics();
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));

		for (int i = 0; i < size; i++) {
			Command command = (Command) commandList.get(i);
			if (command.getType() == Command.NK_COMMAND_SCISSOR) {
				ScissorCommand sc = (ScissorCommand) command;
				g.setClip(sc.x, sc.y, sc.w + 1, sc.h);
			} else if (command.getType() == Command.NK_COMMAND_LINE) {
				LineCommand lc = (LineCommand) command;
				g.setColor(new Color(lc.r, lc.g, lc.b, lc.a));
				g.drawLine(lc.x0, lc.y0, lc.x1, lc.y1);
			} else if (command.getType() == Command.NK_COMMAND_RECT) {
				RectCommand rc = (RectCommand) command;
				g.setColor(new Color(rc.r, rc.g, rc.b, rc.a));
				if (rc.rounded == NuklearDemo.NK_FALSE) {
					g.drawRect(rc.x, rc.y, rc.w, rc.h);
				} else {
					g.drawRoundRect(rc.x, rc.y, rc.w, rc.h, rc.rounded, rc.rounded);
				}
			} else if (command.getType() == Command.NK_COMMAND_RECT_FILLED) {
				RectFilledCommand rc = (RectFilledCommand) command;
				g.setColor(new Color(rc.r, rc.g, rc.b, rc.a));
				if (rc.rounded == NuklearDemo.NK_FALSE) {
					g.fillRect(rc.x, rc.y, rc.w, rc.h);
				} else {
					g.fillRoundRect(rc.x, rc.y, rc.w, rc.h, rc.rounded, rc.rounded);
				}
			} else if (command.getType() == Command.NK_COMMAND_CIRCLE) {
				CircleCommand cc = (CircleCommand) command;
				g.setColor(new Color(cc.r, cc.g, cc.b, cc.a));
				g.drawOval(cc.x, cc.y, cc.w, cc.h);
			} else if (command.getType() == Command.NK_COMMAND_CIRCLE_FILLED) {
				CircleFilledCommand cc = (CircleFilledCommand) command;
				g.setColor(new Color(cc.r, cc.g, cc.b, cc.a));
				g.fillOval(cc.x, cc.y, cc.w, cc.h);
			} else if (command.getType() == Command.NK_COMMAND_TRIANGLE) {
				TriangleCommand tc = (TriangleCommand) command;
				int[] xPoints = { tc.x0, tc.x1, tc.x2 };
				int[] yPoints = { tc.y0, tc.y1, tc.y2 };
				g.setColor(new Color(tc.r, tc.g, tc.b, tc.a));
				g.drawPolygon(xPoints, yPoints, 3);
			} else if (command.getType() == Command.NK_COMMAND_TRIANGLE_FILLED) {
				TriangleFilledCommand tc = (TriangleFilledCommand) command;
				int[] xPoints = { tc.x0, tc.x1, tc.x2 };
				int[] yPoints = { tc.y0, tc.y1, tc.y2 };
				g.setColor(new Color(tc.r, tc.g, tc.b, tc.a));
				g.fillPolygon(xPoints, yPoints, 3);
			} else if (command.getType() == Command.NK_COMMAND_TEXT) {
				TextCommand tc = (TextCommand) command;

				int x = tc.x;
				int y = tc.y;
				int w = tc.w;
				int h = tc.h;
				// System.out.println("draw text " + x + " " + y + " " + w + " "
				// + h + " " + tc.s);

				g.setColor(new Color(tc.bgR, tc.bgG, tc.bgB, tc.bgA));
				g.fillRect(x, y, w, h);
				g.setColor(new Color(tc.fgR, tc.fgG, tc.fgB, tc.fgA));
				int stringH = fontMetrics.getAscent();
				g.drawString(tc.s, x, y + stringH);

			}
		}

		panel.paintImmediately(0, 0, screenWidth, screenHeight);

	}

	private void addAndMergeMouseMotionEvents(MouseEvent currentEvent) {
		synchronized (eventQueue) {
			Object o;
			if (!eventQueue.isEmpty() && ((o = eventQueue.lastElement()) instanceof MouseEvent)) {
				MouseEvent lastEvent = (MouseEvent) o;
				if (currentEvent.getID() == lastEvent.getID()) {
					eventQueue.set(eventQueue.size() - 1, currentEvent);
				} else {
					eventQueue.add(currentEvent);
				}
			} else {
				eventQueue.add(currentEvent);
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		addAndMergeMouseMotionEvents(e);
	}

	public void mouseMoved(MouseEvent e) {
		addAndMergeMouseMotionEvents(e);
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		synchronized (eventQueue) {
			eventQueue.add(e);
			// e.consume();
		}

	}

	public void mouseReleased(MouseEvent e) {
		synchronized (eventQueue) {
			eventQueue.add(e);
			// e.consume();
		}

	}

	public void keyPressed(KeyEvent e) {
		synchronized (eventQueue) {
			eventQueue.add(e);
			// e.consume();
		}
	}

	public void keyReleased(KeyEvent e) {
		synchronized (eventQueue) {
			eventQueue.add(e);
			// e.consume();
		}
	}

	public void keyTyped(KeyEvent e) {

	}
}
