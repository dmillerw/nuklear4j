package nuklear;

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

import nuklear.swig.nk_buttons;
import nuklear.swig.nk_color;
import nuklear.swig.nk_context;
import nuklear.swig.nk_keys;
import nuklear.swig.nuklear;

public class AWTBackend implements Backend, MouseMotionListener, MouseListener, KeyListener {

	private Canvas canvas = new Canvas();
	private Font font = new Font(Font.MONOSPACED, Font.PLAIN, 10);
	private BufferedImage screenImage;
	private FontMetrics fontMetrics;
	private JPanel panel;
	private JFrame frame;
	private Vector eventQueue;
	private Vector commandList = new Vector();
	private int[] intBuffer = new int[10000];

	public int getFontHeight() {
		return fontMetrics.getHeight();
	}

	public int getMaxCharWidth() {
		return fontMetrics.stringWidth("W");
	}
	
	/**
	 * Set the rendering surface.
	 * @param screenImage
	 */
	public void setRenderingSurface(BufferedImage screenImage) {
		if ((screenImage.getWidth() != this.screenImage.getWidth()) || (screenImage.getHeight() != this.screenImage.getHeight())) {
			throw new IllegalStateException("Buffered image must have the same dimensions than the previous one");
		}
		this.screenImage = screenImage;
	}
	
	public void setFont(Font font) {
		if (screenImage != null) {
			throw new IllegalStateException("Font can't be changed after initialization");
		}
		this.font = font;
	}
	
	/**
	 * Rendering will be done in the provided BufferedImage. Remember to add this class as the event listener.  
	 * @param screenImage
	 */
	public void initialize(BufferedImage screenImage) {
		this.screenImage = screenImage;
		fontMetrics = screenImage.getGraphics().getFontMetrics(font);
	}

	/**
	 * Create a frame and render inside.
	 * @param screenWidth
	 * @param screenHeight
	 */
	public void initialize(int screenWidth, int screenHeight) {
		initialize(new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB_PRE));

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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.addWindowListener(this);
		frame.add(panel);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null); // Center the frame (has to be called
											// after pack)
		frame.setVisible(true);
		panel.requestFocusInWindow();

	}

	/* (non-Javadoc)
	 * @see nuklear.Backend#clear(nuklear.swig.nk_color)
	 */
	public void clear(nk_color bgColor) {
		Color c = new Color(bgColor.getR(), bgColor.getG(), bgColor.getB());
		Graphics g = screenImage.getGraphics();
		g.setColor(c);
		g.fillRect(0, 0, screenImage.getWidth(), screenImage.getHeight());
	}

	/* (non-Javadoc)
	 * @see nuklear.Backend#handleEvent(nuklear.swig.nk_context)
	 */
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
						// int Nuklear.NK_TRUE = (me.getID() ==
						// MouseEvent.MOUSE_PRESSED) ? Nuklear.NK_TRUE :
						// Nuklear.NK_FALSE;
						int modifiers = me.getModifiersEx();
						int button = me.getButton();
						if (button == MouseEvent.BUTTON1) {
							// System.out.println("MOUSE_PRESSED: BUTTON1");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_LEFT, me.getX(), me.getY(),
									Nuklear4j.NK_TRUE);
						} else if (button == MouseEvent.BUTTON3) {
							// System.out.println("MOUSE_PRESSED: BUTTON3");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_RIGHT, me.getX(), me.getY(),
									Nuklear4j.NK_TRUE);
						}
					} else if (me.getID() == MouseEvent.MOUSE_RELEASED) {
						// int Nuklear.NK_TRUE = (me.getID() ==
						// MouseEvent.MOUSE_PRESSED) ? Nuklear.NK_TRUE :
						// Nuklear.NK_FALSE;
						int button = me.getButton();
						if (button == MouseEvent.BUTTON1) {
							// System.out.println("MOUSE_RELEASED: BUTTON1");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_LEFT, me.getX(), me.getY(),
									Nuklear4j.NK_FALSE);
						} else if (button == MouseEvent.BUTTON3) {
							// System.out.println("MOUSE_RELEASED: BUTTON3");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_RIGHT, me.getX(), me.getY(),
									Nuklear4j.NK_FALSE);
						}
					} else if (me.getID() == MouseEvent.MOUSE_DRAGGED) {
						// int Nuklear.NK_TRUE = (me.getID() ==
						// MouseEvent.MOUSE_PRESSED) ? Nuklear.NK_TRUE :
						// Nuklear.NK_FALSE;
						int button = me.getButton();
						if (button == MouseEvent.BUTTON1) {
							// System.out.println("MOUSE_RELEASED: BUTTON1");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_LEFT, me.getX(), me.getY(),
									Nuklear4j.NK_TRUE);
						} else if (button == MouseEvent.BUTTON3) {
							// System.out.println("MOUSE_RELEASED: BUTTON3");
							nuklear.nk_input_button(ctx, nk_buttons.NK_BUTTON_RIGHT, me.getX(), me.getY(),
									Nuklear4j.NK_TRUE);
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
								pressed ? Nuklear4j.NK_TRUE : Nuklear4j.NK_FALSE);
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
	
	/* (non-Javadoc)
	 * @see nuklear.Backend#render(nuklear.swig.nk_context)
	 */
	public void render(nk_context ctx) {
		nuklear.nk_headless_render(ctx, intBuffer);
		Command.build(intBuffer, commandList);
		render(commandList);
		commandList.clear();
	}

	private void render(Vector commandList) {
		int size = commandList.size();

		Graphics g = screenImage.getGraphics();
		g.setFont(font);

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
				if (rc.rounded == Nuklear4j.NK_FALSE) {
					g.drawRect(rc.x, rc.y, rc.w, rc.h);
				} else {
					g.drawRoundRect(rc.x, rc.y, rc.w, rc.h, rc.rounded, rc.rounded);
				}
			} else if (command.getType() == Command.NK_COMMAND_RECT_FILLED) {
				RectFilledCommand rc = (RectFilledCommand) command;
				g.setColor(new Color(rc.r, rc.g, rc.b, rc.a));
				if (rc.rounded == Nuklear4j.NK_FALSE) {
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

		panel.paintImmediately(0, 0, screenImage.getWidth(), screenImage.getHeight());

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

	/* (non-Javadoc)
	 * @see nuklear.Backend#waitEvents(long)
	 */
	public boolean waitEvents(long delay) {
		synchronized (eventQueue) {
			if (eventQueue.size() > 0) {
				return true;
			}
		}
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
		
	}

	public Vector getEventQueue() {
		return eventQueue;
	}
	
	
	
}