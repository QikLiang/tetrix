package tetrix;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Panel;
import java.awt.event.KeyEvent;

public class Main extends Panel implements KeyEventDispatcher{

	final static int WIDTH = 300;
	final static int HEIGHT = 500;
	final static int GRIDW = 10;
	final static int GRIDH = 18;
	Color[][] grid;
	Block block;
	Block nextBlock;
	Color EMPTY = Color.black;
	boolean gameover = false;

	public Main (){
		setBackground(Color.black);

		//create block
		block = new Block();
		nextBlock = new Block();
		
		//create grid
		grid = new Color[GRIDW][GRIDH];
		for (int row=0; row<GRIDW; row++) {
			for (int col=0; col<GRIDH; col++) {
				grid[row][col]=EMPTY;
			}
		}//put block into grid
		for (int i=0; i<4; i++) {
			grid[block.cordinate(i)[0]][block.cordinate(i)[1]]=block.color;
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void paint(Graphics g){
		//draw grid
		int cellWidth = WIDTH/GRIDW;
		int cellHeight = HEIGHT/GRIDH;
		for (int row=0; row<GRIDW; row++) {
			for (int col=0; col<GRIDH; col++) {
				if (!grid[row][col].equals(EMPTY)) {
					g.setColor(grid[row][col]);
					g.fillRect(row*cellWidth, col*cellHeight,
							cellWidth, cellHeight);
					g.setColor(Color.white);
					g.drawRect(row*cellWidth, col*cellHeight,
							cellWidth, cellHeight);
				}
			}
		}

		//draw grid outline
		g.setColor(Color.white);
		g.drawLine(0, 0, 0, HEIGHT);
		g.drawLine(0, HEIGHT, WIDTH, HEIGHT);
		g.drawLine(WIDTH, 0, WIDTH, HEIGHT);

		//next block
		g.drawRect(WIDTH,30,cellWidth*4,cellHeight*4);
		for (int i=0; i<4; i++) {
			g.setColor(nextBlock.color);
			g.fillRect(WIDTH+nextBlock.position[i][0]*cellWidth,
					30+nextBlock.position[i][1]*cellHeight, cellWidth, cellHeight);
			g.setColor(Color.white);
			g.drawRect(WIDTH+nextBlock.position[i][0]*cellWidth,
					30+nextBlock.position[i][1]*cellHeight, cellWidth, cellHeight);
		}
		
		if (gameover) {
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.setColor(Color.white);
			g.drawString("GAME OVER",WIDTH/2,HEIGHT/2);
		}
	}

	public void run(){
		while (!gameover){
			if (canMove()) {
				for (int i=0; i<4; i++) {
					grid[block.cordinate(i)[0]][block.cordinate(i)[1]]=EMPTY;
				}
				block.y++;
			} else{
				block = nextBlock;
				nextBlock = new Block();
			}
			for (int i=0; i<4; i++) {
				if (!grid[block.cordinate(i)[0]][block.cordinate(i)[1]].
					equals(EMPTY)) {
					gameover = true;
				}
				grid[block.cordinate(i)[0]][block.cordinate(i)[1]]=block.color;
			}
			repaint();
			try {
				Thread.sleep(300);
			} catch(Exception e) {//ignore exception
			}
		}
	}

	public boolean canMove(){
		for (int i=0; i<4; i++) { //for each cell in block
			if (block.cordinate(i)[1]>=GRIDH-1) { //if cell at bottom
				return false;
			}
			if (!grid[block.cordinate(i)[0]] //if cell below isn't empty
				[block.cordinate(i)[1]+1].equals(EMPTY)) {
				boolean flag = true;//and cell below isn't apart of block
				for (int j=0; j<4; j++) {
					if (block.position[i][0]==block.position[j][0]
						&& block.position[i][1]<block.position[j][1]) {
						flag = false;
					}
				}
				if (flag) {
					return false;//return false
				}
			}
		}
		return true;
	}
	
	//copy pasted from sample code from a school project
	public static void main (String[] args) {
        //Init the game board
        Main game = new Main();

        //Create a window for this program
        final Frame myFrame = new Frame();
        myFrame.setSize(WIDTH+200, HEIGHT+50);

        //Tell this Window to close when someone presses the close button
        myFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                };
            });

        //Ask Java to tell me about what keys the user presses on the keyboard.  
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(game);

        //Put the game in the window
        myFrame.add(game);
        myFrame.setVisible(true);
		game.run();
	}
}
