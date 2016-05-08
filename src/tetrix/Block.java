package tetrix;

import java.awt.Color;
import java.util.Arrays;

public class Block {

	Color color;
	public int[][] position;
	private int width;
	private int height;
	public int x;
	public int y;

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public Block(){
		x = 4;
		y = 0;
		//generate shape
		switch((int)(Math.random()*4)){
			case 0://O shape
				position = new int[][] { {0,0},{0,1},{1,0},{1,1} };
				width = 2;
				height = 2;
				break;
			case 1://Z shape
				position = new int[][] { {0,0},{0,1},{1,1},{1,2} };
				width = 3;
				height = 2;
				break;
			case 2://L shape
				position = new int[][] { {0,0},{1,0},{2,0},{2,1} };
				width = 2;
				height = 3;
				break;
			case 3://I shape
				position = new int[][] { {0,0},{1,0},{2,0},{3,0} };
				width = 1;
				height = 4;
				break;
		}

		//permuate shape via fliping
		if (Math.random()<.5) {
			for (int i=0; i<4; i++) {
				position[i][1] = width-1-position[i][1];
			}
		}

		//determin color
		switch((int)(Math.random()*7)){
			case 0:
				color=Color.red;
				break;
			case 1:
				color=Color.orange;
				break;
			case 2:
				color=Color.green;
				break;
			case 3:
				color=Color.blue;
				break;
			case 4:
				color=Color.cyan;
				break;
			case 5:
				color=Color.magenta.darker();
				break;
			case 6:
				color=Color.yellow;
				break;
		}
	}

	public Block(Block block){
		position = new int[4][2];
		for (int i=0; i<block.position.length; i++) {
			position[i] = Arrays.copyOf(block.position[i],
					block.position[i].length);
		}
		width=block.width;
		height=block.height;
		x=block.x;
		y=block.y;
		color=block.color;
	}

	public int[] cordinate(int i){
		int[] cord = new int[2];
		cord[0]=position[i][0]+y;
		cord[1]=position[i][1]+x;
		return cord;
	}

	//rotate clockwise
	public void rotate(){
		int temp;
		for (int i=0; i<4; i++) {
			temp = position[i][0];
			position[i][0] = position[i][1];
			position[i][1] = height -1 -temp;
		}

		//swap width and height
		temp = width;
		width = height;
		height = temp;
	}

	//rotate counter-clockwise
	public void rotateCounter(){
		int temp;
		for (int i=0; i<4; i++) {
			temp = position[i][0];
			position[i][0] = width -1 -position[i][1];
			position[i][1] = temp;
		}

		//swap width and height
		temp = width;
		width = height;
		height = temp;
	}
}
