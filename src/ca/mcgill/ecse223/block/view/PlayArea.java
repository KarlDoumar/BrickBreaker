package ca.mcgill.ecse223.block.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOCurrentBlock;
import ca.mcgill.ecse223.block.controller.TOCurrentlyPlayedGame;
import ca.mcgill.ecse223.block.controller.TOGridCell;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.model.Paddle;
import ca.mcgill.ecse223.block.model.PlayedBlockAssignment;



class PlayArea extends JPanel {


  // UI elements
  private List<Rectangle2D> blockShapes = new ArrayList<Rectangle2D>();
  private Rectangle2D paddleShape;
  private Ellipse2D ballShape;

  // data elements
	private int level;
	private int paddleLength;
	private int paddleX;
	private int paddleY;
	private boolean pause = false;

	private int ballX;
	private int ballY;
	private int ballD = 10; //diameter of the ball
	private List<TOCurrentBlock> blocks;



  //constants
  private final int boxSize = 20;
  private final int paddleWidth = Paddle.PADDLE_WIDTH;
  private final int paddleHeight = Paddle.VERTICAL_DISTANCE;
  private final int playAreaSize = Game.PLAY_AREA_SIDE;

  public PlayArea() {
    super();
    // init();
//    try {
//		refresh();
//	} catch (InvalidInputException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
    repaint();
  }

/*
  private void init(){
    //is this even needed?
    refresh();
  }
  */

  private void doDrawing(Graphics g){
	Graphics2D background = (Graphics2D) g.create();
	background.setColor(Color.WHITE);
	Rectangle2D background_1 = new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight());
	background.draw(background_1);
	background.fill(background_1);
	
    Graphics2D g2d = (Graphics2D) g.create();
    BasicStroke aStroke = new BasicStroke(2);
    g2d.setStroke(aStroke);
    g2d.setColor(Color.BLACK);
    Rectangle2D boundingBox = new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight());
    g2d.draw(boundingBox);
    
    if (pause) {
    	g2d.setColor(Color.RED);
    	g2d.drawString("Paused", this.getWidth()/2, this.getHeight()/2);
    }

    //draw every block in the level
 
    if (blocks!=null) {
    for (TOCurrentBlock block: blocks) {
      Rectangle2D rectangle = new Rectangle2D.Float(Block223Controller.toPixelsX(block.getX()), Block223Controller.toPixelsY(block.getY()), boxSize, boxSize); //TODO convert to pixel location
      blockShapes.add(rectangle);
     g2d.setColor(new Color(block.getRed(), block.getGreen(), block.getBlue()));
      g2d.fill(rectangle);
      g2d.setColor(Color.BLACK);
      g2d.draw(rectangle);
    }
    
    }else System.out.println("No blocks");

   // draw paddle
    paddleShape = new Rectangle2D.Float(paddleX,  paddleY, paddleLength, paddleWidth);
    g2d.draw(paddleShape);
    g2d.fill(paddleShape);

    //draw ball
    ballShape = new Ellipse2D.Float(ballX, ballY, ballD, ballD);
    g2d.draw(ballShape);
    g2d.fill(ballShape);

  }

  public void refresh() throws InvalidInputException {

	  TOCurrentlyPlayedGame playedGame = Block223Controller.getCurrentPlayableGame();
	  
	  // get every block in the level
	  blocks = playedGame.getBlocks();
	  
	  // get the paddle size and position
	  paddleLength = playedGame.getCurrentPaddleLength();
	  paddleX = playedGame.getCurrentPaddleX();
	  paddleY = playAreaSize - paddleHeight - paddleWidth;
	  //	  paddleX += 1;
	  //paddleY = this.getHeight() - 2*paddleWidth;
	  
	  // get the ball position
	  ballX = playedGame.getCurrentBallX() - ballD/2;
	  ballY = playedGame.getCurrentBallY() - ballD/2;

    repaint();
  }
  public void setLevel(int level) throws InvalidInputException{
    this.level = level;
    refresh();
  }


  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
		doDrawing(g);
  }
  public void pause() {
	  if (pause) pause = false;
	  else pause = true;
  }
}
