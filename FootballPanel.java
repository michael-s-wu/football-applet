import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class FootballPanel extends JPanel{

	FootballDisplayPanel displayPanel = new FootballDisplayPanel();
	private int footballX = 233;
	private double footballY = 224;
	private int WOY = 217;                                 //Y coord of the wide receivers
	private int DBY = 150;
	private int RBX = 265;     
	private int RBY = 305;
	private int LBX = 305;
	private double gain;
	JPanel buttonPanel = new JPanel();
	PassTimerListener PTList = new PassTimerListener();
	lineTimeListener LTList = new lineTimeListener();
	RunTimeListener RTList = new RunTimeListener();
	Timer passTimer = new Timer(20, PTList);
	Timer lineTimer = new Timer(20, LTList);
	Timer runTimer = new Timer (20,RTList);
	private int timer = 0;
	boolean plusX = false;
	private double distance = 35;
	private int plays = 0;
	private int lineX = 249;
	private int difference = 0;
	private boolean passBall;
	private boolean spacePressed;
	private boolean runBall;
	private boolean win;
	private boolean lose;
	private int space;
	boolean hike = true;
	boolean pass = false;
	boolean run = true;
	private boolean isEasy = true;
	private boolean isBeginning = true;
	JButton passButton = new JButton("Pass Ball");
	JButton runButton = new JButton("Run Ball");
	JButton playAgain = new JButton("New Game");
	JButton difficultyButton = new JButton("Easy Mode");
	double n = Math.random();


	public static void main(String[] args) {
		JFrame window = new JFrame("How Fast Can You Score?");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FootballPanel content = new FootballPanel();
		window.setContentPane(content);
		window.setLocation(170,70);
		window.setSize(500,600);
		window.setResizable(false);
		window.setVisible(true);
	}

	public FootballPanel() {
		displayPanel = new FootballDisplayPanel();
		this.setLayout(new BorderLayout());
		this.add(buttonPanel, BorderLayout.SOUTH);
		ButtonListener listener = new ButtonListener();
		buttonPanel.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.BLACK));
		buttonPanel.add(passButton);
		buttonPanel.add(runButton);
		buttonPanel.add(playAgain);
		buttonPanel.add(difficultyButton);
		playAgain.setEnabled(false);
		passButton.addActionListener(listener);
		runButton.addActionListener(listener);
		playAgain.addActionListener(listener);
		difficultyButton.addActionListener(listener);
		displayPanel.setBackground(Color.GREEN);
		this.add(displayPanel);
		keyListener keyListener = new keyListener();
		addKeyListener(keyListener); 
		repaint();
	}

	void drawPlayers (Graphics g){
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 500, 550);
		g.setColor(Color.BLUE);
		for (int x = 0; x < 4; x++){
			g.fillRect(145 + 40 * x,175,35,35); //D-line
		}
		g.fillRect(LBX, 175,35,35);
		g.fillRect(25,DBY,35,35);               //Left DB
		g.fillRect(440,DBY,35,35);              //Right DB
		g.fillRect(185,50,35,35);               //Left SS   
		g.fillRect(265,60,35,35);               //Right SS
		g.setColor(Color.RED);
		g.fillRect(225,217,35,35);              //Center
		g.fillRect(265,225,35,35);              //Right inside LB
		g.fillRect(185,225,35,35);              //Left inside LB   
		g.fillRect(145,245,35,35);              //Left outside LB
		g.fillRect(305,245,35,35);              //Right outside LB
		g.fillRect(225,320,35,35);              //QB
		g.fillRect(RBX,RBY,35,35);              //RB
		g.fillRect(20,WOY,35,35);               //Left wide-out
		g.fillRect(445,WOY,35,35);              //Right wide-out
		g.setColor(Color.BLACK);
		g.fillOval(footballX,(int)footballY,19,33);              //Football
		g.setColor(Color.BLACK);
		g.drawLine(0, 214, 500, 214);           //LoS
		g.drawString("Plays Used: " + plays, 10, 470);
		if (distance < 1){
			g.drawString("Yard Line: TOUCHDOWN!", 10, 490);
		}
		else if (distance <= 50 && distance > 1){
			g.drawString("Yard Line: Opponent's " + distance, 10, 490);
		}
		else if (distance > 50) {
			g.drawString("Yard Line: Own " + (100-distance), 10, 490);
		}
		g.drawString("Select a play at the bottom. " , 170, 15);
		g.drawString("Most recent gain: " + gain, 10, 510);
		lineTimer.stop();
	}

	void passBall(Graphics g){
		timer = 0;
		spacePressed = false;
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 500, 520);
		g.setColor(Color.BLACK);
		g.drawRect(50, 50, 400, 400);
		g.setColor(Color.WHITE);
		g.fillRect(75, 75, 350, 200);
		g.setColor(Color.RED);
		g.fillRect(150, 125, 200, 100);
		g.setColor(Color.YELLOW);
		g.fillRect(175, 125, 150, 100);
		g.setColor(Color.GREEN);
		g.fillRect(245, 125, 10, 100);
		g.setColor(Color.BLACK);
		g.drawLine(lineX, 125, lineX, 225);
		g.drawString("Hit space when the line is in the "
				+ "green zone for your play to have the "
				+ "best", 10, 20);
		g.drawString("result. Your gain will be displayed at the bottom.", 10, 35);
		lineTimer.start();
		requestFocus();
	}

	void win (Graphics g){
		playAgain.setText("New Game");
		playAgain.setEnabled(true);
		passButton.setEnabled(false);
		runButton.setEnabled(false);
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 500, 520);
		g.setColor(Color.WHITE);
		g.fillRect(100, 100, 300, 350);
		g.setColor(Color.BLACK);
		g.drawString("It took you " + plays + " plays to score. ", 110, 170);
		for (int i = 0; i < 10; i++){
			g.drawString("You Win!", 110, 200 + 15 * i);
		}
		g.drawString("Want to play again?   :) ", 110, 370);
		g.drawString("Maybe try playing on hard mode.", 110, 390);
		difficultyButton.setEnabled(true);
	}

	void lose (Graphics g){
		playAgain.setText("New Game");
		playAgain.setEnabled(true);
		passButton.setEnabled(false);
		runButton.setEnabled(false);
		g.setColor(Color.RED);
		g.fillRect(0, 0, 500, 540);
		g.setColor(Color.WHITE);
		g.drawString("Wow. You managed to lose. " , 10 , 100);
		g.drawString("How did you even do that? You would have to lose 65 yards to lose the game." , 10 , 130);
		g.drawString("My only hope is that you wanted to see what would happen if you lose. " , 10 , 160);
		g.drawString("Please tell me that's the case... " , 10 , 190);
		g.drawString("Well anyways feel free to re-start the game and play for realz." , 10 , 220);
		difficultyButton.setEnabled(true);
	}

	void playAgain (Graphics g){
		distance = 35;
		plays = 0;
		passButton.setEnabled(true);
		runButton.setEnabled(true);
		playAgain.setEnabled(false);
		lose = false;
		win = false;
		gain = 0;
		repaint();
	}

	void newGame (Graphics g){

		passButton.setEnabled(false);
		runButton.setEnabled(false);
		playAgain.setEnabled(true);
		g.setColor(Color.WHITE);
		g.fillRect(0,0,500,537);
		g.setColor(Color.BLACK);
		g.drawString("Welcome to 'How Fast Can You Score?'  :) ", 10,20);
		g.drawString("In this football based game, you start with the ball", 10,50);
		g.drawString("at your opponent's 35 yard-line. The goal of this game", 10,80);
		g.drawString("Is to see how quickly you can score a touchdown. ", 10,110);
		g.drawString("You are assessed based on how many plays it takes to score. ", 10,140);
		g.drawString("For each play, you can choose to run the ball or to pass the ball.", 10,170);
		g.drawString("Passing the ball can result in more yardage, ", 10,200);
		g.drawString("but the potential to lose yards is greater. ", 10,230);
		g.drawString("Once you have selected a play, a meter will be displayed", 10,260);
		g.drawString("to determine the outcome of the play.", 10,290);
		g.drawString("Please select your difficulty level before the first play.", 10,320);
		g.drawString("When you're ready, click on the new game button. Good luck!", 10,350);


	}

	public class FootballDisplayPanel extends JPanel{

		public FootballDisplayPanel(){

			Color backgroundColor = Color.green;
			this.setBackground(backgroundColor);		
		}

		public void paintComponent (Graphics g){
			super.paintComponent(g);
			if (isBeginning){
				newGame(g);
			}
			if (win){
				win(g);
			}
			if (lose){
				lose(g);
			}
			if (win == false && lose == false && isBeginning == false){
				drawPlayers(g);
			}
			if (passBall == true || runBall == true){
				difficultyButton.setEnabled(false);
				passBall(g);
				space = 0;
				passButton.setEnabled(false);
				runButton.setEnabled(false);
			}
		}
	}

	public class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			String command = evt.getActionCommand();
			if (command.equals("Pass Ball")){
				passBall = true;
				n = Math.random();
			}
			if (command.equals("Run Ball")){
				runBall = true;
				n = Math.random();
			}
			if (command.equals("New Game")){
				isBeginning = false;
				playAgain(getGraphics());
				playAgain.setText("Skip Animations");
			}
			if (command.equals("Easy Mode")){
				difficultyButton.setText("Hard Mode");
				isEasy = false;
			}
			if (command.equals("Hard Mode")){
				difficultyButton.setText("Easy Mode");
				isEasy = true;
			}
			if (command.equals("Skip Animations")){
				if (passTimer.isRunning() == true){
					passTimer.stop();
				}
				if (runTimer.isRunning() == true){
					runTimer.stop();
				}
				runBall = false;
				passBall = false;
				passButton.setEnabled(true);
				runButton.setEnabled(true);
				footballX = 233;
				footballY = 224;
				DBY = 150;
				WOY = 217; 
				RBX = 265; 
				RBY = 305;
				LBX = 305;
				timer = 0;
				playAgain.setEnabled(false);
				if (distance< 1){
					win = true;
				}
				if (distance >= 100){
					lose = true;
				}
				repaint();
			}
			repaint();

		}
	}

	public class PassTimerListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			playAgain.setEnabled(true);
			if (gain > 1){
				if (timer == 0){
					if ( footballY < 310 ){
						footballY+=5;
					}
					if ( footballY >= 310 && WOY > 100){
						WOY-=3;
						DBY-=3;
					}
					if (WOY < 110){
						footballY = footballY - 7;
						if (n >= 0.50){
							footballX-=2;
						}
						else footballX+=2;
					}
					if (footballY < 100){
						timer = 1;
					}
					repaint();
				}
			}
			else {
				if (timer == 0){
					if ( hike == true && timer == 0){
						footballY+=5;
						if (footballY >= 320){
							hike = false;
							pass = true;
						}
					}
					if ( pass == true && DBY > 50){
						WOY-=2;
						DBY-=2;
					}
					if (pass == true && DBY <= 50 && WOY < 250 && footballX > 35){
						WOY += 2;
					}
					if (pass == true && WOY >= 250 && footballX > 25 && footballX < 455){
						if (n > 0.5){
							footballX -= 3;
						}
						else footballX += 3;
						footballY -= 1;
					}
					if (footballX < 25 || footballX >= 455){
						pass = false;
						run = true;
					}
					if (run == true && footballX <= 35 || footballX >= 455){
						pass = false;
						DBY += 7;
						if (DBY > 210){
							run = false;
							timer = 1;
							hike = true;
						}
					} 
					repaint();
				}
			}
			if (timer != 0){
				timer ++;
			}
			if (timer >= 80){
				passTimer.stop();
				footballX = 233;
				footballY = 224;
				DBY = 150;
				WOY = 217; 
				passButton.setEnabled(true);
				runButton.setEnabled(true);
				timer = 0;
				playAgain.setEnabled(false);
				if (distance< 1){
					win = true;
				}
				if (distance >= 100){
					lose = true;
				}
				repaint();
			}
		}
	}

	public class RunTimeListener implements ActionListener{
		public void actionPerformed (ActionEvent evt){
			playAgain.setEnabled(true);
			if (gain > 0){
				if (timer == 0){	
					if ( footballY < 310 && WOY > 100){
						footballY+=5;
					}
					if ( footballY >= 310 && WOY > 100){
						WOY-=3;
						DBY-=3;
					}
					if (WOY == 100 && footballX < 275){

						footballX+=2;
						footballY-=1;
					}
					if (footballX >= 275 && footballX != 385){
						footballX +=2;
						footballY -=2;
						RBX+=2;
						RBY-=2;
					}
					if (footballX == 385){
						footballY -=2;
						RBY -= 2;
					}
					if (RBY < 150){
						timer = 1;
					}
					repaint();
				}
				if (timer != 0){
					timer ++;
				}
			}			

			if (gain < 0){
				if (timer == 0){
					if ( footballY < 310 && WOY > 100){
						footballY+=5;
					}
					if ( footballY >= 310 && WOY > 100){
						WOY-=3;
						DBY-=3;
					}
					if (WOY == 100 && footballX < 275){						
						footballX+=2;
						footballY-=1;
					}
					if (footballX >= 275 && footballX < 355){
						footballX +=2;
						footballY -=2;
						RBX+=2;
						RBY-=2;
						LBX+= 1;
					}
					if (footballX >= 355){
						timer = 1;
					}
					repaint();
				}
				if (timer != 0){
					timer  = timer + 1;
				}
			}
			if (timer == 80){
				runTimer.stop();
				runBall = false;
				passButton.setEnabled(true);
				runButton.setEnabled(true);
				playAgain.setEnabled(false);
				footballX = 233;
				footballY = 224;
				DBY = 150;
				WOY = 217; 
				RBX = 265; 
				RBY = 305;
				LBX = 305;
				timer = 0;
				if (distance != 0){
					repaint();
				}
				else if (distance < 1){
					win = true;
				}
				else if (distance >= 100){
					lose = true;
				}
				repaint();
			}
		}	
	}

	public class lineTimeListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (spacePressed == false){
				if (lineX < 150){
					plusX = true;
				}
				if (lineX > 350){
					plusX = false;
				}
				if (plusX){
					if (isEasy == false){
						lineX+=2;
					}
					lineX+=3;
				}
				if (plusX == false){
					if (isEasy == false){
						lineX-=2;	
					}
					lineX-=3;
				}
				repaint();
			}
			timer ++;
			Graphics g = getGraphics();
			if (timer > 175){
				n = Math.random();
				lineTimer.stop();
				drawPlayers(g);
				spacePressed = false;
				plays++;
				if (passBall){
					passTimer.start();
				}
				if (runBall){
					runTimer.start();
				}
				passBall = false;
				runBall = false;
				timer = 0;
			}
		}
	}

	public class keyListener implements KeyListener{
		public void keyPressed(KeyEvent evt) {}
		public void keyReleased(KeyEvent evt) {}	
		public void keyTyped(KeyEvent evt) {
			char ch = evt.getKeyChar();
			if (ch == ' ' && passTimer.isRunning() == false && runTimer.isRunning() == false){
				spacePressed = true;
				space++;
				gain = 0;
				Graphics g = getGraphics();
				if (passBall){
					if (249 > lineX){
						difference = 249 - lineX;
					}
					else difference = lineX - 249;
					if (isEasy == true){
						gain = 1.5 * (10 - difference) + (int) (Math.random() * 5);
						if (gain < -10){
							gain = -10;
						}
						if (gain == 0){
							gain = 1;
						}
					}
					if (isEasy == false){
						gain = (10 - difference) + (int) (Math.random() * 3);
						if (gain < -15){
							gain = -15;
						}
						if (gain == 0){
							gain = -1;
						}
					}


				}	
				if (runBall){
					if (249 > lineX){
						difference = 249 - lineX;
					}
					else difference = lineX - 249;

					gain = (10 - difference);
					if (isEasy == true){
						gain = (10 - difference) + (int) (Math.random() * 5);
						if (gain < -6){
							gain = -6; 
						}
						if (gain == 0){
							gain = 1;
						}
					}
					if (isEasy == false){
						gain = (int) (10 - difference) + (int) (Math.random() * 3);
						if (gain < -8){
							gain = -8;							
						}
						if (gain == 0){
							gain = -1;
						}
					}

				}
				g.setColor(Color.BLACK);
				if (space == 1){
					timer = 0;
					distance = distance - gain;
					if (distance < 0){
						distance = 0;
					}
					if ( distance > 100){
						distance = 100;
					}
					g.drawRect(90, 475, 150, 20);
					if (gain > 0){
						g.drawString("You gain " + gain + " yards.", 100, 490);
					}
					if (gain < 0){
						g.drawString("You lose " + (-1 * gain ) + " yards.", 100, 490);
					}
				}
			}
		}
	}
}

