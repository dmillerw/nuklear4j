package nuklear.demo;

import nuklear.AWTBackend;
import nuklear.Backend;

public class AWTDemo extends AbstractDemo {

	public static void main(String argv[]) {
		AWTDemo demo = new AWTDemo();
		demo.initialize();
		// demo.simple();
		demo.overview();
	}


	//@Override
	public Backend createBackend(int screenWidth, int screenHeight) {
		AWTBackend backend = new AWTBackend();
		
		/*
		 * Uncomment if you want to play with fonts (default: Font.MONOSPACED, Font.PLAIN, 10)
		 */
		//backend.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
		
		backend.initialize(screenWidth, screenHeight);
		
		/* If you want to render in your own buffer: 
		 * backend.initialize(BufferedImage image);
		 * myAWTOrSwingComponent.addMouseMotionListener(backend);
		 * myAWTOrSwingComponent.addMouseListener(backend);
		 * myAWTOrSwingComponent.addKeyListener(backend);  
		 */ 
		
		return backend;
	}

}
