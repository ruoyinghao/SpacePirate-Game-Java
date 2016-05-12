//Computer Science 1, project 2: space pirate.
//Written by Ruoying Hao and Danielle King.
//Honor Pledge: I have acted with honesty and integrity when producing the work and am unaware of anyone who has not.
//When completing this project, we received assistance from the tutors who were on duty on March 17 and March 23.

//Import packages for graphic design and keyboard detector.
import acm.graphics.*; 
import acm.program.*;
import java.awt.*;
import java.awt.event.*;


public class SpacePirate extends GraphicsProgram {
	// Width of the main window in pixels
	private static final int WIDTH = 750;
	// Height of the main window
	private static final int HEIGHT = 500;
	// number of landing pads
	private static final int NPADS = 5;
	// dimensions of each landing pad
	private static final int PAD_WIDTH = WIDTH/(3*NPADS+3);
	private static final int PAD_HEIGHT = 20;
	// lander dimensions
	private static final int LANDER_HEIGHT = 16; 
	private static final int LANDER_WIDTH = 24; 
	// some constants for the physics
	private static final double LANDER_ACCELERATION_X = .0001;
	private static final double LANDER_ACCELERATION_Y =.0001;
	private static final double GRAVITY = .000025; 
	// asteroid parameters
	private static final int N_ASTEROIDS_X = 6; 
	private static final int N_ASTEROIDS_Y =9;
	private static final int ASTEROID_SIZE = 8;
	// The space ship, ocean, and current landing pad
	private GCompound lander;                                                         
	// number of pads which have been visited
	private int padsVisited = 0;
	// current lander velocity
	// Delta is used in physics to denote the change,
	// i.e. how much the lander moves in the x or y direction
	private double landerDeltaX;
	private double landerDeltaY;

	// status of lander thrusters
	private boolean leftThrust = false;
	private boolean rightThrust = false;
	private boolean upThrust = false;
	// game status
	private boolean gameRunning = false;
	private boolean hasLanded = false;
	private boolean setup=false;
	// game speed 
	private int speed = 1;
	// serialVersionUID prevents a warning in Eclipse due to a technical detail
	// about the serializable Interface that is beyond the scope of this project
	private static final long serialVersionUID = 1L;

	// init method runs when the applet starts	
	public void init(){
		resize(WIDTH, HEIGHT); // set the window size
		addKeyListeners();	   // setup program to accept keyboard info
		setBackground(Color.BLACK);

		// put the instructions on the screen 
		GLabel title1 = new GLabel ("Hi, and welcome to Space Pirate!" , 99, 65);
		title1.setColor(Color.WHITE);
		GLabel title2 = new GLabel ("Press 'n' to begin.", 109,80);
		title2.setColor(Color.WHITE);
		GLabel instru0 = new GLabel ("INSTRUCTIONS:", 99, 125);
		instru0.setColor(Color.WHITE);
		GLabel landpad = new GLabel ("Try to win the game by landing the pirate ship on all the landing pads.", 109.5, 140);
		landpad.setColor(Color.WHITE);
		GLabel youdie1 = new GLabel ("You loses if the ship sinks in the ocean, collides with an asteroid, ", 109.5, 155);
		youdie1.setColor(Color.WHITE);
		GLabel youdie2 = new GLabel ("lands only one foot in the landing pad, or flies off the sides of the screen.", 109.5, 170);
		youdie2.setColor(Color.WHITE);
		GLabel causion = new GLabel ("Note that after sucessfully landing on a pad, the speed of the ship will increase, which make the game harder. ", 109.5, 185);
		causion.setColor(Color.WHITE);
		GLabel instru1 = new GLabel ("Press 'n' to start a new game,", 109.5, 210);
		instru1.setColor(Color.WHITE);
		GLabel instru2 = new GLabel ("Press 's' to start the movement of the Pirate Ship,", 109.5, 225);
		instru2.setColor(Color.WHITE);
		GLabel instru3 = new GLabel ("Use the up, left, and right arrow keys for thrusters,", 109.5, 240);
		instru3.setColor(Color.WHITE);
		GLabel instru4 = new GLabel ("Press 'p' to pauses the game,", 109.5, 255);
		instru4.setColor(Color.WHITE); 
		GLabel instru5 = new GLabel ("Press 'q' to quit the game.", 109.5, 270); //Added feature quit button.
		instru5.setColor(Color.WHITE); 

		add(title1);
		add(title2);
		add(instru0);
		add(instru1);
		add(instru2);
		add(instru3);
		add(instru4);
		add(landpad);
		add(youdie1);
		add(youdie2);
		add(causion);
		add(instru5);

	}

	// Note: You do NOT need to call the keyPressed or keyReleased methods. 
	// addKeyListeners() is called in init() already
	// Each time a key is pressed (or released), this method (or the following method)
	// will automatically be called.

	// Called whenever a key is pressed	   
	public void keyPressed(KeyEvent evt) {

		int key = evt.getKeyCode();  // Keyboard code for the pressed key.

		// arrow keys for the thrusters
		if (key == KeyEvent.VK_LEFT) {  // left-arrow key
			if(gameRunning == true){
				leftThrust = true;		
			}
		}
		else if (key == KeyEvent.VK_RIGHT) {  // right-arrow key
			if(gameRunning == true){
				rightThrust = true;
			}
		}
		else if (key == KeyEvent.VK_UP) {  // up-arrow key
			if(gameRunning == true){
				upThrust = true;
			}
			if(gameRunning == false && hasLanded == true){
				hasLanded = false;
				upThrust = true;
				gameRunning = true;
				setZeroVelocity();
				remove(getElementAt(lander.getX(),(lander.getY()+LANDER_HEIGHT-1))); //after the ship landed on a pad, remove the pad by pressing up-arrow key.
			}
		}
		else if(key == KeyEvent.VK_N) { // N for starting a new game
			gameSetup();
		}
		else if(key == KeyEvent.VK_S){  //S for starting the movement of the ship or making the game continue after it is paused.
			if(setup==true) //failed to add this if statement will result in error feedback in the console if a player mistakenly pressed 's' in the instruction page.
			gameRunning = true;
		}
		else if(key == KeyEvent.VK_P){ //P for pause.
			gameRunning = false;
		}
		else if(key==KeyEvent.VK_Q){//quit button feature. when the player quit, end the game and return to the instruction page.
			gameRunning=false;
			setup=false;
			clearScreen();
			gameOver("You quit the game.");
			init();
			
		}
	}

	// detect if a key is released.
	public void keyReleased(KeyEvent evt) {
		int key = evt.getKeyCode();  // Keyboard code for the pressed key
		if (key == KeyEvent.VK_LEFT){
			leftThrust = false;
		}
		if (key == KeyEvent.VK_UP){
			upThrust = false;
		}
		if (key == KeyEvent.VK_RIGHT){
			rightThrust = false;
		}
	}

	// set up a new game
	private void gameSetup() {
		// set the game status
		padsVisited = 0; // discard the old record.	
		hasLanded = false;
		gameRunning = false;                                                                
		setup = true;
		speed=1;
		setZeroVelocity();
		clearScreen(); // clear all objects

		// draw everything
		placeLander();
		initAsteroids();
		initOcean();	
		initLandingPads(); //Asteroids, pads and the ocean need to be drawn after the drawing of the ship in order to enable the ship to detect their color.
		//NOTE: received assistance from the tutor on Mar 17:understanding the relationship between the order of the "drawing" methods and "checking for collision" outcomes.
	}

	// clear all objects from the screen
	private void clearScreen() {
		for(int x = 0; x<= WIDTH; x++){
			for(int y = 0; y <= HEIGHT; y++){
				if(getElementAt(x,y)!= null){ //Test all the points in the screen, if there is an object on that point, remove the object.
					remove(getElementAt(x,y));
				}
			}
		}
		/* 
		 * Strategy: use nested for loops to look at every valid (x,y) combination of pixels in the display
		 * Check if there is an object there using the getElementAt method. 
		 * If there is an object there (note what getElementAt returns if there is no such object)
		 * remove it using the remove method which can take a GObject as a parameter, ex: remove(lander);
		 */ 
	}


	// draw the ocean
	private void initOcean() {
		GRect ocean = new GRect(0,HEIGHT-4*PAD_HEIGHT, WIDTH, HEIGHT);
		ocean.setFilled(true);
		ocean.setColor(Color.BLUE);
		add(ocean);
	}

	// draw the asteroids.
	//the distance between two lines of the asteroids is designed to be smaller when the asteroids are closer to the pads.
	//This design can force the player to move the ship up higher to avoid asteroid collision when moving horizontally.
	//Thus prevent the waste of the space.
	private void initAsteroids() {
		for(int x=0;x<N_ASTEROIDS_X;x++){                                                                                     
			for(int y=0;y<N_ASTEROIDS_Y;y++){
				GOval ball = new GOval(70*(y%2)+(WIDTH-40)/(N_ASTEROIDS_X-1)*x,70+(HEIGHT-130-10*(y+1))/(N_ASTEROIDS_Y-1)*y, ASTEROID_SIZE,ASTEROID_SIZE);
				ball.setFilled(true);
				ball.setColor(Color.WHITE);
				add(ball);
			}
		}
		for(int x=1;x<=4;x++){
			GOval ball2 = new GOval(80+122*x,30, ASTEROID_SIZE,ASTEROID_SIZE);//put asteroids on the top of the screen.
			ball2.setFilled(true);
			ball2.setColor(Color.WHITE);
			GOval ball1 = new GOval(80+122*x,380, ASTEROID_SIZE,ASTEROID_SIZE);//put asteroids between the pads to keep the user from moving the ship horizontally(shortcut!).
			ball1.setFilled(true);
			ball1.setColor(Color.WHITE);
			add(ball1);
			add(ball2);
		}
		for(int y=1;y<4;y++){ //put asteroids under the home location of the ship to avoiding shortcut.
			GOval ball3 = new GOval(WIDTH/2+12,60+90*y, ASTEROID_SIZE,ASTEROID_SIZE);
			ball3.setFilled(true);
			ball3.setColor(Color.WHITE);
			add(ball3);
		}
	}

	// draw the landing pads using for loop.
	private void initLandingPads(){
		for(int i = 0; i < NPADS; i++){
			placeLandingPad(Color.YELLOW, i);
		}		
	}	

	private void placeLandingPad(Color c, int i) {
		GRect square = new GRect((3*i+3)*(PAD_WIDTH), HEIGHT-5*PAD_HEIGHT, PAD_WIDTH, PAD_HEIGHT);
		square.setFilled(true);
		square.setColor(c);
		add(square);
	}

	// draw the lander/pirate ship
	private void placeLander(){		
		lander = new GCompound();		
		GOval ball = new GOval(2, (LANDER_HEIGHT-10)/2+1, LANDER_WIDTH-4,LANDER_HEIGHT-10); 
		ball.setFilled(true);
		lander.add(ball);
		GRect leftwing = new GRect(0, 0, 2, LANDER_HEIGHT);
		leftwing.setFilled(true);
		lander.add(leftwing);
		GRect rightwing = new GRect(LANDER_WIDTH-1,	0, 2, LANDER_HEIGHT);
		rightwing.setFilled(true);
		lander.add(rightwing);
		lander.setColor(Color.GREEN);
		add(lander);

		// Note the use of the move method here. What happens if you change the parameters? If we change the parameter, another object may move.
		// This method may be useful in moving the ship throughout the game, perhaps in the run method.
		lander.move(WIDTH/2,25);		
		pause(100); // pauses the execution of the code for 100 milliseconds.
	}

	// sets lander speed to zero in each direction
	private void setZeroVelocity() {	
		landerDeltaX = 0;
		landerDeltaY = 0;
	}

	// update the velocity based on thrusters and gravity
	private void updateVelocity(){	
		if (leftThrust == true){
			landerDeltaX -= LANDER_ACCELERATION_X; //lander move to left with increasing speed.
		}
		else if (rightThrust == true) {
			landerDeltaX += LANDER_ACCELERATION_X;//lander move to right with increasing speed.
		}
		//NOTE: received assistance from the tutor on Mar 23: learning how to slower down the horizontal speed after releasing the arrow keys.
		else{// when the left/right arrow keys is released, slow down the horizontal speed of the ship.
			if(landerDeltaX < 0){
				landerDeltaX += LANDER_ACCELERATION_X;
			}
			if(landerDeltaX > 0){
				landerDeltaX -= LANDER_ACCELERATION_X;
			}
		}
		if (upThrust == true) {
			landerDeltaY -= LANDER_ACCELERATION_Y-GRAVITY;
		}
		else{
			landerDeltaY +=GRAVITY;
		}	
	}

	// run the game
	// note that run is called automatically after init
	// there is no main method in an applet
	public void run(){
		while(!gameRunning){                                                                 
			pause(1000);
			while(gameRunning == true){                                                                 
				pause(.00001);  //pause the game or the ship will move too fast.
				updateVelocity();
				lander.move(landerDeltaX+landerDeltaX/2*(speed-1), landerDeltaY+landerDeltaY/2*(speed-1));//when "speed" increases, the ship will move faster. 
				checkocean();  //check if the ship hit the ocean.
				checkForOutOfBounds();//check if the ship touches the two sides of the screen.
				checkForAsteroidCollision();//check if the ship hit an asteroid.
				checkForLanding();//check if the ship has left 2 feet on a landing pad.
				if(padsVisited == 5){
					win();
				}
				
			}
		}
	}

	// check if the lander is off the screen to the left or right
	private void checkForOutOfBounds() {
		// Hint: use the getX method available for GObjects
		double x = lander.getX();
		if(x < 0  || x > WIDTH-LANDER_WIDTH){
			gameOver("You Moved Your Ship Off the Screen! GAME OVER!! Press N to start a new game.");
		}
	}

	// check if the lander hit an asteroid
	private void checkForAsteroidCollision() {
		// Hint: use the getX and getY methods
		// You may also want to write a separate method that checks points on a bounding box
		// around the lander and determines if any of those points are touching another object
		// See the handout.

		double x=lander.getX();
		double y=lander.getY();
		//check 8 points on the ship.
		//NOTE: received assistance from the tutor on Mar 17: figuring out how to check multiple points on the ship for asteroid collision.
		if(getElementAt(x,y).getColor()==Color.WHITE ||getElementAt(x+LANDER_WIDTH,y).getColor()==Color.WHITE ||
				getElementAt(x+8,y+5).getColor()==Color.WHITE || getElementAt(x+16,y+5).getColor()==Color.WHITE  ||
				getElementAt(x+8,y+9).getColor()==Color.white ||getElementAt(x+16,y+9).getColor()==Color.WHITE ||
				getElementAt(x,y+LANDER_HEIGHT-1).getColor()==Color.WHITE || getElementAt(x+LANDER_WIDTH,y+LANDER_HEIGHT-1).getColor()==Color.WHITE ||
				getElementAt(x,y+LANDER_HEIGHT/2).getColor()==Color.WHITE ||getElementAt(x+LANDER_WIDTH,y+LANDER_HEIGHT/2).getColor()==Color.WHITE){
			gameOver("Your Ship Collided With An Asteroid! GAME OVER!! Press 'n' to start a new game.");
		}
	}


	//check if the ship touched the ocean
	private void checkocean(){
		double y=lander.getY()+LANDER_HEIGHT-1;
		double x=lander.getX();
		if(getElementAt(x,y).getColor()==Color.BLUE){
			gameOver("Your ship sunk! GAME OVER!! Press 'n' to start a new game");
		}
	}

	//check if the ship is successfully landed on a pad.
	private void checkForLanding() {
		// Hint: use the getX and getY methods available for GObjects
		//NOTE: for the bottom of the ship, y coordinate cannot be getY()+LANDER_HEIGHT, it can be getY()+LANDER_HEIGHT-1.
		//for the side of the ship, x coordinate cannot be getX()+WIDTH-1.
		//type in the illegal integers can result in error warning.
		double x = lander.getX();
		double y = lander.getY();
		if(getElementAt(x,y+LANDER_HEIGHT-1).getColor() == Color.YELLOW ){
			if(getElementAt(x+LANDER_WIDTH, y+LANDER_HEIGHT-1).getColor()!=Color.YELLOW){
				gameOver("Incorrect Landing GAME OVER!(one foot on the pad), press 'n' to start a new game."); //if the right foot is on the pad but the left foot isn't, game over.
			}
			else{//if both feet on the pad, stop the movement of the ship, increase the speed.
				hasLanded = true;
				gameRunning = false;
				padsVisited++;
				speed++;
				while(hasLanded){   //if we don't pause here, when the padsVisited reaches 5, the game will be running too fast to call win();                                                              
					pause(1000);
				}  
			}
		}
		if(getElementAt(x+LANDER_WIDTH,y+LANDER_HEIGHT-1).getColor()==Color.YELLOW ){
			if(getElementAt(x, y+LANDER_HEIGHT-1).getColor()!=Color.YELLOW){
				gameOver("Incorrect Landing GAME OVER!(one foot on the pad), press 'n' to start a new game.");
			}
			else{
				hasLanded = true;
				gameRunning = false;
				padsVisited++;
				speed++;
				while(hasLanded){                                                                 
					pause(1000);
				}  
			}
		}
	}

	// print a message (on the game screen) when you lose the game
	// the message depends on why the player lost
	private void gameOver(String message) {                                                                         
		GLabel title = new GLabel(message,40,25);
		title.setColor(Color.WHITE);
		add(title);
		gameRunning = false;
	
	}

	// print a message when you win the game
	private void win() {
		gameRunning=false;
		GLabel title1 = new GLabel ("Congratulations, WINNER!!" , 40, 25);
		title1.setColor(Color.WHITE);
		add(title1);
	}
}

