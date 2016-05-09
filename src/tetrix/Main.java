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
	static final Color EMPTY = Color.black;
	boolean gameover = false;

	public Main (){
		setBackground(Color.black);

		//create block
		block = new Block();
		nextBlock = new Block();
		
		//create grid
		grid = new Color[GRIDH][GRIDW];
		for (int row=0; row<GRIDH; row++) {
			for (int col=0; col<GRIDW; col++) {
				grid[row][col]=EMPTY;
			}
		}//put block into grid
		for (int i=0; i<4; i++) {
			grid[block.cordinate(i)[0]][block.cordinate(i)[1]]=block.color;
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (gameover) {//stop if game over
			return false;
		}

        //Ignore KEY_RELEASED events (we only care when the key is pressed)
        String params = e.paramString();
        if (params.contains("KEY_RELEASED"))
        {
            return false;
        }

		//handle key presses
		int event = e.getKeyCode();
		switch (event){
			case KeyEvent.VK_A:
				if (canMoveLeft()) {
					clearBlock();
					block.x--;
					addBlock();
				}
				break;
			case KeyEvent.VK_S:
				if (canMoveDown()) {
					clearBlock();
					block.y++;
					addBlock();
				}
				break;
			case KeyEvent.VK_D:
				if (canMoveRight()) {
					clearBlock();
					block.x++;
					addBlock();
				}
				break;
			case KeyEvent.VK_W:
				//store block
				break;
			case KeyEvent.VK_Q:
				clearBlock();
				Block temp = rotatedCounter();
				if (temp!=null) {
					block = temp;
				}
				addBlock();
				break;
			case KeyEvent.VK_E:
				clearBlock();
				temp = rotated();
				if (temp!=null) {
					block = temp;
				}
				addBlock();
				break;
			default:
				return false;
		}

		//update graphics
		repaint();

		return true;
	}

	//clear block from grid
	public void clearBlock(){
		for (int i=0; i<4; i++) {
			grid[block.cordinate(i)[0]][block.cordinate(i)[1]]=EMPTY;
		}
	}

	public void addBlock(){
		for (int i=0; i<4; i++) {
			grid[block.cordinate(i)[0]][block.cordinate(i)[1]]=block.color;
		}
	}
	
	public void paint(Graphics g){
		//draw grid
		int cellWidth = WIDTH/GRIDW;
		int cellHeight = HEIGHT/GRIDH;
		for (int row=0; row<GRIDH; row++) {
			for (int col=0; col<GRIDW; col++) {
				if (!grid[row][col].equals(EMPTY)) {
					g.setColor(grid[row][col]);
					g.fillRect(col*cellWidth, row*cellHeight,
							cellWidth, cellHeight);
					g.setColor(Color.white);
					g.drawRect(col*cellWidth, row*cellHeight,
							cellWidth, cellHeight);
				}
			}
		}

		//draw grid outline
		g.setColor(Color.white);
		g.drawLine(0, 0, 0, HEIGHT);
		g.drawLine(0, HEIGHT, WIDTH, HEIGHT);
		g.drawLine(WIDTH, 0, WIDTH, HEIGHT);

		//next block 10 pixel buffer next to grid
		g.drawRect(WIDTH+10,30,cellWidth*4,cellHeight*4);
		for (int i=0; i<4; i++) {
			g.setColor(nextBlock.color);
			g.fillRect(WIDTH+nextBlock.position[i][1]*cellWidth+10,
					30+nextBlock.position[i][0]*cellHeight, cellWidth, cellHeight);
			g.setColor(Color.white);
			g.drawRect(WIDTH+nextBlock.position[i][1]*cellWidth+10,
					30+nextBlock.position[i][0]*cellHeight, cellWidth, cellHeight);
		}
		
		if (gameover) {
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.setColor(Color.white);
			g.drawString("GAME OVER",WIDTH/2,HEIGHT/2);
		}
	}

	//empty row if full
	public void updateGrid(){
		boolean full;
		for (int row=0; row<GRIDH; row++) {//for each row
			full = true;//check is the row clearable
			for (int col=0; col<GRIDW; col++) {
				if (grid[row][col].equals(EMPTY)) {
					full = false;
					break;
				}
			}

			if(full){ //if row can be cleared
				for (int i=row; i>0; i--) {
					//move all rows above down
					grid[i] = grid [i-1];
				}
			}
		}
	}

	public void run(){
		while (!gameover){
			if (canMoveDown()) {
				clearBlock();
				block.y++;
			} else{
				updateGrid();//check has a row been filled
				block = nextBlock;
				nextBlock = new Block();
			}
			for (int i=0; i<4; i++) {
				if (!grid[block.cordinate(i)[0]][block.cordinate(i)[1]].
					equals(EMPTY)) {
					gameover = true;
				}
			}
			addBlock();
			repaint();
			try {
				Thread.sleep(300);
			} catch(Exception e) {//ignore exception
			}
		}
	}

	public boolean canMoveDown(){
		if (block.y+block.getHeight()>=GRIDH) {
			return false;//if block reach bottom
		}
		for (int i=0; i<4; i++) { //for each cell in block
			if (!grid[block.cordinate(i)[0]+1] //if cell below isn't empty
				[block.cordinate(i)[1]].equals(EMPTY)) {
				boolean flag = true;//and cell below isn't a part of block
				for (int j=0; j<4; j++) {
					if (block.position[i][1]==block.position[j][1]
						&& block.position[i][0]<block.position[j][0]) {
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

	public boolean canMoveLeft(){
		if (block.x<=0) {
			return false;//if block reach edge
		}
		for (int i=0; i<4; i++) { //for each cell in block
			if (!grid[block.cordinate(i)[0]] //if cell to left isn't empty
				[block.cordinate(i)[1]-1].equals(EMPTY)) {
				boolean flag = true;//that cell isn't a part of block
				for (int j=0; j<4; j++) {
					if (block.position[i][1]>block.position[j][1]
						&& block.position[i][0]==block.position[j][0]) {
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
	
	public boolean canMoveRight(){
		if (block.x+block.getWidth()>=GRIDW) {
			return false;//if block reach edge
		}
		for (int i=0; i<4; i++) { //for each cell in block
			if (!grid[block.cordinate(i)[0]] //if cell to right isn't empty
				[block.cordinate(i)[1]+1].equals(EMPTY)) {
				boolean flag = true;//that cell isn't a part of block
				for (int j=0; j<4; j++) {
					if (block.position[i][1]<block.position[j][1]
						&& block.position[i][0]==block.position[j][0]) {
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

	//return a rotated block, return null if illegal
	public Block rotated(){
		Block rotated = new Block(block);
		rotated.rotate();

		//push away from edge
		while(rotated.x+rotated.getWidth()>GRIDW){
			rotated.x--;
		}
		while(rotated.x<0){
			rotated.x++;
		}
		while(rotated.y+rotated.getHeight()>=GRIDH){
			rotated.y--;
		}

		for (int i=0; i<4; i++) {
			int co1=rotated.cordinate(i)[0];
			int co2=rotated.cordinate(i)[1];
			if (!grid[rotated.cordinate(i)[0]][rotated.cordinate(i)[1]].
				equals(EMPTY)) {
				return null;
			}
		}
		return rotated;
	}

	//return a rotated block, return null if illegal
	public Block rotatedCounter(){
		Block rotated = new Block(block);
		rotated.rotateCounter();

		//push away from edge
		while(rotated.x+rotated.getWidth()>GRIDW){
			rotated.x--;
		}
		while(rotated.x<0){
			rotated.x++;
		}
		while(rotated.y+rotated.getHeight()>=GRIDH){
			rotated.y--;
		}

		for (int i=0; i<4; i++) {
			if (!grid[rotated.cordinate(i)[0]][rotated.cordinate(i)[1]].
				equals(EMPTY)) {
				return null;
			}
		}
		return rotated;
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
