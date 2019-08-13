package gr.vlas.chaos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;


public class Main implements Runnable{

	private JFrame frame;
	private Graphics g;
	private BufferStrategy bs;
	private Canvas canvas;
	private boolean running = true;
	private Thread thread;
	private int num = 0;
	private int width , height;
	private Random r = new Random();
	private int px = 1, py = 1;
	private static final int xa = 400, ya = 20, xb = 700, yb = 700, xc = 30, yc = 500;
	
	public Main(int width, int height) {
		this.width = width;
		this.height = height;
		createFrame();
	}
	
	private void setPoint(int x, int y) {
		px = x;
		py = y;
	}
	
	private int CorY(int num) {
		int y = 0;
		
		if(num == 1 || num == 2) {
			y = (ya + py) / 2;
			//System.out.println("ya");
		}
		if(num == 3 || num == 4) {
			y = (yb + py) / 2;
			//System.out.println("yb");
		}
		if(num == 5 || num == 6) {
			y = (yc + py) / 2;
			//System.out.println("yc");
		}
		if(y < 0) {
			y*= -1;
		}
		System.out.println("[Y ]" + y);
		
		return y;
	}
	
	private int CorX(int num) {
		int x = 0;
		
		if(num == 1 || num == 2) {
			x = (xa + px) / 2;
			//System.out.println("xa");
		}
		if(num == 3 || num == 4) {
			x = (xb + px) / 2;
			//System.out.println("xb");
		}
		if(num == 5 || num == 6) {
			x = (xc + px) / 2;
			//System.out.println("xc");
		}
		if(x < 0) {
			x*= -1;
		}
		
		return x;
	}
	
	private void draw(Graphics g) {
		for(int i = 0; i < 1000; i++) {
			num = r.nextInt(6) + 1;
			System.out.println("[NUM] : " + num);
			setPoint(CorX(num), CorY(num));
			//System.out.println("[RANDOM] "+ num);
			g.setColor(Color.RED);
			g.fillRect(px, py, 5, 5);
			System.out.println(CorX(num) + CorY(num));
		}
		//g.fillRect(px, py, 5, 5);
	}
	
	
	
	private void render() {
		bs = canvas.getBufferStrategy();
		if(bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);
		//render here
		g.setColor(Color.BLACK);
		g.fillRect(xa, ya, 10, 10);
		g.fillRect(xb, yb, 10, 10);
		g.fillRect(xc, yc, 10, 10);
		
		draw(g);
		
		bs.show();
		g.dispose();
	}
	
	private void update() {
		
	}
	
	public void run() {
		System.out.println("KK");
		long lastTime = System.nanoTime();
		long lastTimer = System.currentTimeMillis();
		double ns = 1000000000.0 / 60.0;
		double delta = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			while (delta >= 1) {
				update();
				delta--;
			} 

			render();

			while (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
			}
		}
	}
	
	private void createFrame() {
		frame = new JFrame("Chaos");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvas = new Canvas();
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setFocusable(false);
		
		frame.add(canvas);
		frame.pack();
	}
	
	public static void main (String[] args) {
		Main main = new Main(800, 800);
		main.start();
		main.run();
	}

	public synchronized void start() {
		if(running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if(!running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
