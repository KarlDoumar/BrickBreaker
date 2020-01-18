/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.block.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.awt.geom.*;
import java.awt.Point;
import ca.mcgill.ecse223.block.model.BouncePoint.BounceDirection;
import math.geom2d.conic.CircleArc2D;
import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import math.geom2d.Point2D;
import java.util.*;

// line 11 "../../../../../Block223PlayMode.ump"
// line 104 "../../../../../Block223Persistence.ump"
// line 1 "../../../../../Block223States.ump"
public class PlayedGame implements Serializable
{

  //------------------------
  // STATIC VARIABLES
  //------------------------


  /**
   * at design time, the initial wait time may be adjusted as seen fit
   */
  public static final int INITIAL_WAIT_TIME = 10;
  private static int nextId = 1;
  public static final int NR_LIVES = 3;

  /**
   * the PlayedBall and PlayedPaddle are not in a separate class to avoid the bug in Umple that occurred for the second constructor of Game
   * no direct link to Ball, because the ball can be found by navigating to Game and then Ball
   */
  public static final int BALL_INITIAL_X = Game.PLAY_AREA_SIDE / 2;
  public static final int BALL_INITIAL_Y = Game.PLAY_AREA_SIDE / 2;

  /**
   * no direct link to Paddle, because the paddle can be found by navigating to Game and then Paddle
   * pixels moved when right arrow key is pressed
   */
  public static final int PADDLE_MOVE_RIGHT = 5;

  /**
   * pixels moved when left arrow key is pressed
   */
  public static final int PADDLE_MOVE_LEFT = -5;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PlayedGame Attributes
  private int score;
  private int lives;
  private int currentLevel;
  private double waitTime;
  private String playername;
  private double ballDirectionX;
  private double ballDirectionY;
  private double currentBallX;
  private double currentBallY;
  private double currentPaddleLength;
  private double currentPaddleX;
  private double currentPaddleY;

  //Autounique Attributes
  private int id;

  //PlayedGame State Machines
  public enum PlayStatus { Ready, Moving, Paused, GameOver }
  private PlayStatus playStatus;

  //PlayedGame Associations
  private Player player;
  private Game game;
  private List<PlayedBlockAssignment> blocks;
  private BouncePoint bounce;
  private Block223 block223;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PlayedGame(String aPlayername, Game aGame, Block223 aBlock223)
  {
    // line 48 "../../../../../Block223PlayMode.ump"
    boolean didAddGameResult = setGame(aGame);
          if (!didAddGameResult)
          {
             throw new RuntimeException("Unable to create playedGame due to game");
          }
    // END OF UMPLE BEFORE INJECTION
    score = 0;
    lives = NR_LIVES;
    currentLevel = 1;
    waitTime = INITIAL_WAIT_TIME;
    playername = aPlayername;
    resetBallDirectionX();
    resetBallDirectionY();
    resetCurrentBallX();
    resetCurrentBallY();
    currentPaddleLength = getGame().getPaddle().getMaxPaddleLength();
    resetCurrentPaddleX();
    currentPaddleY = Game.PLAY_AREA_SIDE - Paddle.VERTICAL_DISTANCE - Paddle.PADDLE_WIDTH;
    id = nextId++;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create playedGame due to game");
    }
    blocks = new ArrayList<PlayedBlockAssignment>();
    boolean didAddBlock223 = setBlock223(aBlock223);
    if (!didAddBlock223)
    {
      throw new RuntimeException("Unable to create playedGame due to block223");
    }
    setPlayStatus(PlayStatus.Ready);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setScore(int aScore)
  {
    boolean wasSet = false;
    score = aScore;
    wasSet = true;
    return wasSet;
  }

  public boolean setLives(int aLives)
  {
    boolean wasSet = false;
    lives = aLives;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentLevel(int aCurrentLevel)
  {
    boolean wasSet = false;
    currentLevel = aCurrentLevel;
    wasSet = true;
    return wasSet;
  }

  public boolean setWaitTime(double aWaitTime)
  {
    boolean wasSet = false;
    waitTime = aWaitTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setPlayername(String aPlayername)
  {
    boolean wasSet = false;
    playername = aPlayername;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setBallDirectionX(double aBallDirectionX)
  {
    boolean wasSet = false;
    ballDirectionX = aBallDirectionX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetBallDirectionX()
  {
    boolean wasReset = false;

   // int minXSpeed = game.getBall().getMinBallSpeedX();
    //game.getBall().setMinBallSpeedX(minXSpeed);
    setBallDirectionX(getGame().getBall().getMinBallSpeedX());

    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setBallDirectionY(double aBallDirectionY)
  {
    boolean wasSet = false;
    ballDirectionY = aBallDirectionY;
    wasSet = true;
    return wasSet;
  }


  public boolean resetBallDirectionY()
  {
    boolean wasReset = false;
    setBallDirectionY(getGame().getBall().getMinBallSpeedY());
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentBallX(double aCurrentBallX)
  {
    boolean wasSet = false;
    currentBallX = aCurrentBallX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentBallX()
  {
    boolean wasReset = false;
    currentBallX = getDefaultCurrentBallX();
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentBallY(double aCurrentBallY)
  {
    boolean wasSet = false;
    currentBallY = aCurrentBallY;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentBallY()
  {
    boolean wasReset = false;
    currentBallY = getDefaultCurrentBallY();
    wasReset = true;
    return wasReset;
  }

  public boolean setCurrentPaddleLength(double aCurrentPaddleLength)
  {
    boolean wasSet = false;
    currentPaddleLength = aCurrentPaddleLength;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentPaddleX(double aCurrentPaddleX)
  {
    boolean wasSet = false;
    currentPaddleX = aCurrentPaddleX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentPaddleX()
  {
    boolean wasReset = false;
    currentPaddleX = getDefaultCurrentPaddleX();
    wasReset = true;
    return wasReset;
  }

  public int getScore()
  {
    return score;
  }

  public int getLives()
  {
    return lives;
  }

  public int getCurrentLevel()
  {
    return currentLevel;
  }

  public double getWaitTime()
  {
    return waitTime;
  }

  /**
   * added here so that it only needs to be determined once
   */
  public String getPlayername()
  {
    return playername;
  }

  /**
   * 0/0 is the top left corner of the play area, i.e., a directionX/Y of 0/1 moves the ball down in a straight line
   */
  public double getBallDirectionX()
  {
    return ballDirectionX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultBallDirectionX()
  {
    return getGame().getBall().getMinBallSpeedX();
  }

  public double getBallDirectionY()
  {
    return ballDirectionY;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultBallDirectionY()
  {
    return getGame().getBall().getMinBallSpeedY();
  }

  /**
   * the position of the ball is at the center of the ball
   */
  public double getCurrentBallX()
  {
    return currentBallX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentBallX()
  {
    return BALL_INITIAL_X;
  }

  public double getCurrentBallY()
  {
    return currentBallY;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentBallY()
  {
    return BALL_INITIAL_Y;
  }

  public double getCurrentPaddleLength()
  {
    return currentPaddleLength;
  }

  /**
   * the position of the paddle is at its top left corner
   */
  public double getCurrentPaddleX()
  {
    return currentPaddleX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentPaddleX()
  {
    return (Game.PLAY_AREA_SIDE - currentPaddleLength) / 2;
  }

  public double getCurrentPaddleY()
  {
    return currentPaddleY;
  }

  public int getId()
  {
    return id;
  }

  public String getPlayStatusFullName()
  {
    String answer = playStatus.toString();
    return answer;
  }

  public PlayStatus getPlayStatus()
  {
    return playStatus;
  }

  public boolean play()
  {
    boolean wasEventProcessed = false;

    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Ready:
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      case Paused:
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean pause()
  {
    boolean wasEventProcessed = false;

    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Moving:
        setPlayStatus(PlayStatus.Paused);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean move()
  {
    boolean wasEventProcessed = false;

    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Moving:
        if (hitPaddle())
        {
        // line 21 "../../../../../Block223States.ump"
          doHitPaddleOrWall();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        if (isOutOfBoundsAndLastLife())
        {
        // line 22 "../../../../../Block223States.ump"
          doOutOfBounds();
          setPlayStatus(PlayStatus.GameOver);
          wasEventProcessed = true;
          break;
        }
        if (isOutOfBounds())
        {
        // line 23 "../../../../../Block223States.ump"
          doOutOfBounds();
          setPlayStatus(PlayStatus.Paused);
          wasEventProcessed = true;
          break;
        }
        if (hitLastBlockAndLastLevel())
        {
        // line 24 "../../../../../Block223States.ump"
          doHitBlock();
          setPlayStatus(PlayStatus.GameOver);
          wasEventProcessed = true;
          break;
        }
        if (hitLastBlock())
        {
        // line 25 "../../../../../Block223States.ump"
          doHitBlockNextLevel();
          setPlayStatus(PlayStatus.Ready);
          wasEventProcessed = true;
          break;
        }
        if (hitBlock())
        {
        // line 26 "../../../../../Block223States.ump"
          doHitBlock();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        if (hitWall())
        {
        // line 27 "../../../../../Block223States.ump"
          doHitPaddleOrWall();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        // line 28 "../../../../../Block223States.ump"
        doHitNothingAndNotOutOfBounds();
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setPlayStatus(PlayStatus aPlayStatus)
  {
    playStatus = aPlayStatus;

    // entry actions and do activities
    switch(playStatus)
    {
      case Ready:
        // line 16 "../../../../../Block223States.ump"
        doSetup();
        break;
      case GameOver:
        // line 34 "../../../../../Block223States.ump"
        doGameOver();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }

  public boolean hasPlayer()
  {
    boolean has = player != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetMany */
  public PlayedBlockAssignment getBlock(int index)
  {
    PlayedBlockAssignment aBlock = blocks.get(index);
    return aBlock;
  }

  public List<PlayedBlockAssignment> getBlocks()
  {
    List<PlayedBlockAssignment> newBlocks = Collections.unmodifiableList(blocks);
    return newBlocks;
  }

  public int numberOfBlocks()
  {
    int number = blocks.size();
    return number;
  }

  public boolean hasBlocks()
  {
    boolean has = blocks.size() > 0;
    return has;
  }

  public int indexOfBlock(PlayedBlockAssignment aBlock)
  {
    int index = blocks.indexOf(aBlock);
    return index;
  }
  /* Code from template association_GetOne */
  public BouncePoint getBounce()
  {
    return bounce;
  }

  public boolean hasBounce()
  {
    boolean has = bounce != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Block223 getBlock223()
  {
    return block223;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setPlayer(Player aPlayer)
  {
    boolean wasSet = false;
    Player existingPlayer = player;
    player = aPlayer;
    if (existingPlayer != null && !existingPlayer.equals(aPlayer))
    {
      existingPlayer.removePlayedGame(this);
    }
    if (aPlayer != null)
    {
      aPlayer.addPlayedGame(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame == null)
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removePlayedGame(this);
    }
    game.addPlayedGame(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBlocks()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public PlayedBlockAssignment addBlock(int aX, int aY, Block aBlock)
  {
    return new PlayedBlockAssignment(aX, aY, aBlock, this);
  }

  public boolean addBlock(PlayedBlockAssignment aBlock)
  {
    boolean wasAdded = false;
    if (blocks.contains(aBlock)) { return false; }
    PlayedGame existingPlayedGame = aBlock.getPlayedGame();
    boolean isNewPlayedGame = existingPlayedGame != null && !this.equals(existingPlayedGame);
    if (isNewPlayedGame)
    {
      aBlock.setPlayedGame(this);
    }
    else
    {
      blocks.add(aBlock);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBlock(PlayedBlockAssignment aBlock)
  {
    boolean wasRemoved = false;
    //Unable to remove aBlock, as it must always have a playedGame
    if (!this.equals(aBlock.getPlayedGame()))
    {
      blocks.remove(aBlock);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBlockAt(PlayedBlockAssignment aBlock, int index)
  {
    boolean wasAdded = false;
    if(addBlock(aBlock))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBlocks()) { index = numberOfBlocks() - 1; }
      blocks.remove(aBlock);
      blocks.add(index, aBlock);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBlockAt(PlayedBlockAssignment aBlock, int index)
  {
    boolean wasAdded = false;
    if(blocks.contains(aBlock))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBlocks()) { index = numberOfBlocks() - 1; }
      blocks.remove(aBlock);
      blocks.add(index, aBlock);
      wasAdded = true;
    }
    else
    {
      wasAdded = addBlockAt(aBlock, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setBounce(BouncePoint aNewBounce)
  {
    boolean wasSet = false;
    bounce = aNewBounce;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBlock223(Block223 aBlock223)
  {
    boolean wasSet = false;
    if (aBlock223 == null)
    {
      return wasSet;
    }

    Block223 existingBlock223 = block223;
    block223 = aBlock223;
    if (existingBlock223 != null && !existingBlock223.equals(aBlock223))
    {
      existingBlock223.removePlayedGame(this);
    }
    block223.addPlayedGame(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (player != null)
    {
      Player placeholderPlayer = player;
      this.player = null;
      placeholderPlayer.removePlayedGame(this);
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePlayedGame(this);
    }
    while (blocks.size() > 0)
    {
      PlayedBlockAssignment aBlock = blocks.get(blocks.size() - 1);
      aBlock.delete();
      blocks.remove(aBlock);
    }

    bounce = null;
    Block223 placeholderBlock223 = block223;
    this.block223 = null;
    if(placeholderBlock223 != null)
    {
      placeholderBlock223.removePlayedGame(this);
    }
  }


  /**
   * Guards
   */
  // line 41 "../../../../../Block223States.ump"
   private boolean hitPaddle(){
    BouncePoint bouncePoint;
    bouncePoint = calculateBouncePointPaddle();
    setBounce(bouncePoint);
    return bouncePoint != null;
  }

  // line 48 "../../../../../Block223States.ump"
    private boolean isOutOfBoundsAndLastLife(){
    boolean outOfBounds = false;
	  if (lives == 1) {
		  outOfBounds = isOutOfBounds();
	  }

    // TODO implement
    return outOfBounds;
  }

  // line 58 "../../../../../Block223States.ump"
   private boolean isOutOfBounds(){
    boolean outOfBounds = false;
	   if( (currentBallY + Ball.BALL_DIAMETER/2) >= Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER)
		 {
		   outOfBounds= true;

		 }
	 //  else {
	//	   isOutOfBounds();
	//   }
    	// TODO implement
    	return outOfBounds;
  }

  // line 73 "../../../../../Block223States.ump"
   private boolean hitLastBlockAndLastLevel(){
    game = getGame();
	   int nrLevels = game.numberOfLevels();
	   setBounce(null);

	   if(nrLevels == currentLevel) {
		   int nrBlocks = numberOfBlocks();

		   if(nrBlocks == 1) {
			   PlayedBlockAssignment block = getBlock(0);
			   BouncePoint bp = calculateBouncePointBlock(block);
         if (bp != null) {
           setBounce(bp);
           return true;
         }
		   }


	   }
	   return false;
  }

  // line 95 "../../../../../Block223States.ump"
   private boolean hitLastBlock(){
    int nrBlocks = numberOfBlocks();
		    setBounce(null);

		    if(nrBlocks == 1) {
				 PlayedBlockAssignment block = getBlock(0);

				  BouncePoint bp = calculateBouncePointBlock(block);
          if (bp != null) {
					  setBounce(bp);
					  return true;
				  }
			   }
		    return false;
  }

  // line 111 "../../../../../Block223States.ump"
   private boolean hitBlock(){
			setBounce(null);

			for(int i = 0; i <= numberOfBlocks()-1; i++) {
				PlayedBlockAssignment block = getBlock(i);
				 BouncePoint bp = calculateBouncePointBlock(block);
				 bounce = getBounce();
         if (bp!=null){
  				 boolean closer = isCloser(bp, bounce);
  				 if(closer) {
  					 setBounce(bp);
  				 }
  				break;
         }
			}
			return getBounce() != null;
  }

  // line 132 "../../../../../Block223States.ump"
   private boolean hitWall(){
    BouncePoint bouncePoint;
  	bouncePoint = calculateBouncePointWall();
  	setBounce(bouncePoint);
  	return bouncePoint != null;
  }


  /**
   * Actions
   */
  // line 142 "../../../../../Block223States.ump"
   private void doSetup(){
    resetAttributes();

		this.resetCurrentBallX();
		this.resetCurrentBallY();
		this.resetBallDirectionX();
		this.resetBallDirectionY();
		this.resetCurrentPaddleX();

		Game game = this.getGame();
		Level level = game.getLevel(currentLevel-1);

		List<BlockAssignment> assignments = level.getBlockAssignments();

		for(BlockAssignment assignment: assignments) {
			PlayedBlockAssignment pblock = new PlayedBlockAssignment((assignment.getGridHorizontalPosition()),(assignment.getGridVerticalPosition()), assignment.getBlock(), this);
		}

    int  x, y, maxH = game.maxNumberOfHorizontalBlocks(), maxV = game.maxNumberOfVerticalBlocks();
    Random random = new Random();

    while(numberOfBlocks() < game.getNrBlocksPerLevel()) {
			x = (random.nextInt(maxH) + 1);
			y = (random.nextInt(maxV) + 1);
			PlayedBlockAssignment pblock = new PlayedBlockAssignment(x,y,game.getRandomBlock(), this);
		}
  }

  // line 171 "../../../../../Block223States.ump"
   private void doHitPaddleOrWall(){
    bounceBall();
  }

  // line 175 "../../../../../Block223States.ump"
   private void bounceBall(){
    double distanceX = getBallDirectionX();
	   double distanceY = getBallDirectionY();
	   double positionX = getCurrentBallX();
	   double positionY = getCurrentBallY();
	   double bouncePointX = getBounce().getX();
	   double bouncePointY = getBounce().getY();
	   double distanceOutgoingX = distanceX - Math.abs(bouncePointX - positionX);
	   double distanceOutgoingY = (distanceY - Math.abs(bouncePointY - positionY));

	   BounceDirection bounceDirection = getBounce().getDirection();

	   if(distanceOutgoingX != 0 || distanceOutgoingY != 0) {
		   if(bounceDirection.equals(BounceDirection.FLIP_BOTH)) {
			   ballDirectionX *= -1;
			   ballDirectionY *= -1;
			   if (getCurrentBallX() > Game.PLAY_AREA_SIDE) {
				   setCurrentBallX(Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER/2);
			   }
			   if (getCurrentBallX() < 0) {
				   setCurrentBallX(Ball.BALL_DIAMETER/2);
			   }
			   if (getCurrentBallY() < 0) {
				   setCurrentBallY(Ball.BALL_DIAMETER/2);
			   }
			   currentBallX = bouncePointX + distanceOutgoingX / distanceX * ballDirectionX;
			   currentBallY = bouncePointY + distanceOutgoingY / distanceY * ballDirectionY;
		   }
		   if(bounceDirection.equals(BounceDirection.FLIP_X)) {
			   ballDirectionX *= -1;
			   if (ballDirectionY == 0) {
				   ballDirectionY += 0.1 * Math.abs(ballDirectionX);
			   }
			   else {
				   ballDirectionY += Math.signum(ballDirectionY) * 0.1 * Math.abs(ballDirectionX);
			   }
			   if (getCurrentBallX() > Game.PLAY_AREA_SIDE) {
				   setCurrentBallX(Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER/2);
			   }
			   if (getCurrentBallX() < 0) {
				   setCurrentBallX(Ball.BALL_DIAMETER/2);
			   }
			   if (getCurrentBallY() < 0) {
				   setCurrentBallY(Ball.BALL_DIAMETER/2);
			   }
			   currentBallX = bouncePointX + distanceOutgoingX / distanceX * ballDirectionX;
			   currentBallY = bouncePointY + distanceOutgoingX / distanceX * ballDirectionY;
		   }
		   if(bounceDirection.equals(BounceDirection.FLIP_Y)) {
			   if (ballDirectionX == 0) {
				   ballDirectionX += 0.1 * Math.abs(ballDirectionY);
			   }
			   else {
				   ballDirectionX += Math.signum(ballDirectionX) * 0.1 * Math.abs(ballDirectionY);
			   }
			   ballDirectionY *= -1;
			   if (getCurrentBallX() > Game.PLAY_AREA_SIDE) {
				   setCurrentBallX(Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER/2);
			   }
			   if (getCurrentBallX() < 0) {
				   setCurrentBallX(Ball.BALL_DIAMETER/2);
			   }
			   if (getCurrentBallY() < 0) {
				   setCurrentBallY(Ball.BALL_DIAMETER/2);
			   }
			   currentBallX = bouncePointX + distanceOutgoingY / distanceY * ballDirectionX;
			   currentBallY = bouncePointY + distanceOutgoingY / distanceY * ballDirectionY;
		   }
		   // did not add this last check to umple
		   if (ballDirectionX >= 10) {
			   ballDirectionX = 10;
		   }
		   if (ballDirectionX <= -10) {
			   ballDirectionX = -10;
		   }
		   if (ballDirectionY >= 10) {
			   ballDirectionY = 10;
		   }
		   if (ballDirectionY <= -10) {
			   ballDirectionY = -10;
		   }
	   }
	   setBounce(null);
  }

  // line 220 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointPaddle(){
    BouncePoint closestPoint = null;
	double newBallX = getCurrentBallX() + ballDirectionX;
	double newBallY = getCurrentBallY() + ballDirectionY;
    Rectangle2D paddleRect = new Rectangle2D.Double();
    paddleRect.setFrame(getCurrentPaddleX() - Ball.BALL_DIAMETER/2, getCurrentPaddleY() - Ball.BALL_DIAMETER/2, getCurrentPaddleLength() + Ball.BALL_DIAMETER, Ball.BALL_DIAMETER/2 + Paddle.PADDLE_WIDTH);
    Line2D lineA = new Line2D.Double();
    lineA.setLine(getCurrentPaddleX(), getCurrentPaddleY() - Ball.BALL_DIAMETER/2, getCurrentPaddleX() + getCurrentPaddleLength(), getCurrentPaddleY() - Ball.BALL_DIAMETER/2);
    Line2D lineB = new Line2D.Double();
    lineB.setLine(getCurrentPaddleX() - Ball.BALL_DIAMETER/2, getCurrentPaddleY(), getCurrentPaddleX() - Ball.BALL_DIAMETER/2, getCurrentPaddleY() + Paddle.PADDLE_WIDTH);
    Line2D lineC = new Line2D.Double();
    lineC.setLine(getCurrentPaddleX() + getCurrentPaddleLength() + Ball.BALL_DIAMETER/2, getCurrentPaddleY(), getCurrentPaddleX() + getCurrentPaddleLength() + Ball.BALL_DIAMETER/2, getCurrentPaddleY() + Paddle.PADDLE_WIDTH);
    Line2D ballSegment = new Line2D.Double();
    ballSegment.setLine(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY);
    ArrayList<BouncePoint> intersectionArray = new ArrayList<>();
    int numBouncePoints = 0;
    CircleArc2D arcE = new CircleArc2D(getCurrentPaddleX(), getCurrentPaddleY(), Ball.BALL_DIAMETER/2, Math.PI/2, Math.PI, false);
    CircleArc2D arcF = new CircleArc2D(getCurrentPaddleX() + getCurrentPaddleLength(), getCurrentPaddleY(), Ball.BALL_DIAMETER/2, 0, Math.PI/2, false);

    if (paddleRect.intersectsLine(ballSegment) && ballSegment.getY2() != lineA.getY1()) {
    	if (lineA.intersectsLine(ballSegment)) {
    		intersectionArray.add(new BouncePoint(calculateIntersectionPoint(ballSegment, lineA).getX(), calculateIntersectionPoint(ballSegment, lineA).getY(), BounceDirection.FLIP_Y));
    		numBouncePoints++;
    	}
    	if (lineB.intersectsLine(ballSegment)) {
    		intersectionArray.add(new BouncePoint(calculateIntersectionPoint(ballSegment, lineB).getX(), calculateIntersectionPoint(ballSegment, lineB).getY(), BounceDirection.FLIP_X));
    		numBouncePoints++;
    	}
    	if (lineC.intersectsLine(ballSegment)) {
    		intersectionArray.add(new BouncePoint(calculateIntersectionPoint(ballSegment, lineC).getX(), calculateIntersectionPoint(ballSegment, lineC).getY(), BounceDirection.FLIP_X));
    		numBouncePoints++;
    	}
    	if (arcE.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY)).size() != 0) {
    		ArrayList<math.geom2d.Point2D> var = arcE.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY));
    		for(int i = 0; i < var.size(); i ++) {
				if (ballDirectionX < 0) {
	    			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_Y));
	        		numBouncePoints++;
				}
				else {
	    			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_X));
	        		numBouncePoints++;
				}
    		}
    	}
    	if (arcF.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY)).size() != 0) {
    		ArrayList<math.geom2d.Point2D> var = arcF.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY));
    		for(int i = 0; i < var.size(); i ++) {
    			if (ballDirectionX < 0) {
        			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_X));
            		numBouncePoints++;
    			}
    			else {
        			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_Y));
            		numBouncePoints++;
    			}
    		}
    	}

    	if (numBouncePoints == 1) {
    		closestPoint = intersectionArray.get(0);
    	}
    	else {
    		for(int i = 0; i < numBouncePoints - 1; i++) {
        		if(isCloser(intersectionArray.get(i), intersectionArray.get(i + 1))) {
        			closestPoint = intersectionArray.get(i);
        		}
        		else {
        			closestPoint = intersectionArray.get(i + 1);
        		}
        	}
    	}
    }
	if (closestPoint != null && newBallX == closestPoint.getX() && newBallY == closestPoint.getY()){
		return null;
	}
    return closestPoint;
  }

  // line 299 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointWall(){
    BouncePoint bouncePoint = null;
		Line2D ballSegment = new Line2D.Double();
		ballSegment.setLine(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY);
		double newBallX = getCurrentBallX() + ballDirectionX;
		double newBallY = getCurrentBallY() + ballDirectionY;
		Line2D A = new Line2D.Double();
		Line2D B = new Line2D.Double();
		Line2D C = new Line2D.Double();
		A.setLine(Ball.BALL_DIAMETER/2, Ball.BALL_DIAMETER/2, Ball.BALL_DIAMETER/2, Game.PLAY_AREA_SIDE);
		B.setLine(Ball.BALL_DIAMETER/2, Ball.BALL_DIAMETER/2, Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER/2, Ball.BALL_DIAMETER/2);
		C.setLine(Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER/2, Ball.BALL_DIAMETER/2, Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER/2, Game.PLAY_AREA_SIDE);

		if (A.intersectsLine(ballSegment) && B.intersectsLine(ballSegment)) {
			if (calculateIntersectionPoint(A, ballSegment).equals(calculateIntersectionPoint(B, ballSegment))) {
				bouncePoint = new BouncePoint(calculateIntersectionPoint(A, ballSegment).getX(), calculateIntersectionPoint(A, ballSegment).getY(), BounceDirection.FLIP_BOTH);
			}
			else {
				bouncePoint = new BouncePoint(Ball.BALL_DIAMETER/2, Ball.BALL_DIAMETER/2, BounceDirection.FLIP_BOTH);
			}

		} else if (B.intersectsLine(ballSegment) && C.intersectsLine(ballSegment)) {
			if (calculateIntersectionPoint(C, ballSegment).equals(calculateIntersectionPoint(B, ballSegment))) {
				bouncePoint = new BouncePoint(calculateIntersectionPoint(C, ballSegment).getX(), calculateIntersectionPoint(C, ballSegment).getY(), BounceDirection.FLIP_BOTH);
			}
			else {
				bouncePoint = new BouncePoint(Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER/2, Ball.BALL_DIAMETER/2, BounceDirection.FLIP_BOTH);
			}

		} else if (A.intersectsLine(ballSegment)) {

			bouncePoint = new BouncePoint(calculateIntersectionPoint(A, ballSegment).getX(), calculateIntersectionPoint(A, ballSegment).getY(), BounceDirection.FLIP_X );

		} else if (C.intersectsLine(ballSegment)) {

			bouncePoint = new BouncePoint(calculateIntersectionPoint(C, ballSegment).getX(), calculateIntersectionPoint(C, ballSegment).getY(), BounceDirection.FLIP_X);

		} else if (B.intersectsLine(ballSegment)) {

			bouncePoint = new BouncePoint(calculateIntersectionPoint(B, ballSegment).getX(), calculateIntersectionPoint(B, ballSegment).getY(), BounceDirection.FLIP_Y);
		}
		if (bouncePoint != null && newBallX == bouncePoint.getX() && newBallY == bouncePoint.getY()){
			return null;
		}
		if (getCurrentBallX() > Game.PLAY_AREA_SIDE) {
			bouncePoint = new BouncePoint(Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER/2, getCurrentBallY(), BounceDirection.FLIP_X);
		}
		if (getCurrentBallX() < 0) {
			bouncePoint = new BouncePoint(Ball.BALL_DIAMETER/2, getCurrentBallY(), BounceDirection.FLIP_X);
		}
		if (getCurrentBallY() < 0) {
			bouncePoint = new BouncePoint(getCurrentBallX(), Ball.BALL_DIAMETER/2, BounceDirection.FLIP_Y);
		   }
		return bouncePoint;
  }

  // line 346 "../../../../../Block223States.ump"
   private Point2D calculateIntersectionPoint(Line2D s, Line2D d){
    double a1 = s.getY2() - s.getY1();
		double b1 = s.getX1() - s.getX2();
		double c1 = a1 * s.getX1() + b1 * s.getY1();
		double a2 = d.getY2() - d.getY1();
		double b2 = d.getX1() - d.getX2();
		double c2 = a2 * d.getX1() + b2 * d.getY1();

		double delta = a1*b2-a2*b1;

		Point2D point = new Point2D((b2*c1-b1*c2)/delta,(a1*c2-a2*c1)/delta);
		System.out.println((b2*c1-b1*c2)/delta+ " " + (a1*c2-a2*c1)/delta);
		return point;
  }

  // line 361 "../../../../../Block223States.ump"
   private void doOutOfBounds(){
	   	 setLives(lives-1);
		 resetAttributes();
  }

  // line 366 "../../../../../Block223States.ump"
   private void doHitBlock(){
    score = getScore();
	    bounce = getBounce();

	    PlayedBlockAssignment pblock = bounce.getHitBlock();
	    Block block = pblock.getBlock();
	    int points = block.getPoints();

	    setScore(score + points);

	    pblock.delete();
	    bounceBall();
  }

  // line 380 "../../../../../Block223States.ump"
   private void doHitBlockNextLevel(){
    doHitBlock();
		   int level = getCurrentLevel();
		   setCurrentLevel(level + 1);
		   setCurrentPaddleLength(getGame().getPaddle().getMaxPaddleLength()-(getGame() //double check this
				   .getPaddle().getMaxPaddleLength()-getGame().getPaddle().getMinPaddleLength())
				   /(getGame().numberOfLevels()-1)*(getCurrentLevel()-1));

		   setWaitTime(INITIAL_WAIT_TIME * Math.pow(getGame().getBall().getBallSpeedIncreaseFactor(),(getCurrentLevel()-1)));
  }

  // line 391 "../../../../../Block223States.ump"
   public void doHitNothingAndNotOutOfBounds(){
    double x = getCurrentBallX();
	    double y = getCurrentBallY();
	    double dx = getBallDirectionX();
	    double dy = getBallDirectionY();
	    setCurrentBallX(x+dx);
	    setCurrentBallY(y+dy);
  }


  // line 400 "../../../../../Block223States.ump"
 private void doGameOver(){

	  Block223 block223 = new Block223();
	  block223 = Block223Application.getBlock223();
	  Player p = getPlayer();
	  if(p != null) {
		  Game game = getGame();
		  HallOfFameEntry hof = new HallOfFameEntry(score, playername,p,game,block223);
		  game.setMostRecentEntry(hof);
		  //this.delete();

		  //hof= create(score, playername,p,game,block223);

	  }
  }

  // line 431 "../../../../../Block223States.ump"
   private void resetAttributes(){
    resetCurrentBallX();
	   resetCurrentBallY();
	   resetBallDirectionX();
	   resetBallDirectionY();
	   resetCurrentPaddleX();
  }

  // line 441 "../../../../../Block223States.ump"
   private boolean isCloser(BouncePoint first, BouncePoint second){
     if(first == null) {
			   return false;
		   } if(second == null) {
			   return true;
		   }


		   double distanceFirst = Math.sqrt(Math.pow((first.getX()-currentBallX),2) + Math.pow((first.getY()-currentBallY),2));
		   double distanceSecond = Math.sqrt(Math.pow((second.getX()-currentBallX),2) + Math.pow((second.getY()-currentBallY),2));

		   if(distanceFirst > distanceSecond) {
			   return true;
		   }
		  else{
		   return false; //
		  }
  }

  // line 459 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointBlock(PlayedBlockAssignment block){
	   Rectangle2D blockRect = new Rectangle2D.Double();
	   blockRect.setFrame(Block223Controller.toPixelsX(block.getX())- Ball.BALL_DIAMETER/2, Block223Controller.toPixelsY(block.getY()) - Ball.BALL_DIAMETER/2, Block.SIZE + Ball.BALL_DIAMETER, Block.SIZE + Ball.BALL_DIAMETER);
	   double newBallX = getCurrentBallX() + ballDirectionX;
	   double newBallY = getCurrentBallY() + ballDirectionY;
	   Line2D lineA = new Line2D.Double();
	   Line2D lineB = new Line2D.Double();
	   Line2D lineC = new Line2D.Double();
	   Line2D lineD = new Line2D.Double();
	   Line2D ballSegment = new Line2D.Double();

	   lineA.setLine(Block223Controller.toPixelsX(block.getX()), Block223Controller.toPixelsY(block.getY())-Ball.BALL_DIAMETER/2, Block223Controller.toPixelsX(block.getX())+Block.SIZE, Block223Controller.toPixelsY(block.getY())-Ball.BALL_DIAMETER/2);
	   lineB.setLine(Block223Controller.toPixelsX(block.getX())-Ball.BALL_DIAMETER/2, Block223Controller.toPixelsY(block.getY()), Block223Controller.toPixelsX(block.getX())-Ball.BALL_DIAMETER/2, Block223Controller.toPixelsY(block.getY())+Block.SIZE);
	   lineC.setLine(Block223Controller.toPixelsX(block.getX())+Ball.BALL_DIAMETER/2+Block.SIZE, Block223Controller.toPixelsY(block.getY()), Block223Controller.toPixelsX(block.getX())+Ball.BALL_DIAMETER/2+Block.SIZE, Block223Controller.toPixelsY(block.getY())+Block.SIZE);
	   lineD.setLine(Block223Controller.toPixelsX(block.getX()), Block223Controller.toPixelsY(block.getY())+Block.SIZE+Ball.BALL_DIAMETER/2, Block223Controller.toPixelsX(block.getX())+Block.SIZE, Block223Controller.toPixelsY(block.getY())+Block.SIZE+Ball.BALL_DIAMETER/2);
	   ballSegment.setLine(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY);

	   CircleArc2D arcE = new CircleArc2D(Block223Controller.toPixelsX(block.getX()), Block223Controller.toPixelsY(block.getY()), Ball.BALL_DIAMETER/2, Math.PI/2, Math.PI, false);
	   CircleArc2D arcF = new CircleArc2D(Block223Controller.toPixelsX(block.getX())+Block.SIZE, Block223Controller.toPixelsY(block.getY()), Ball.BALL_DIAMETER/2, 0, Math.PI/2, false);
	   CircleArc2D arcG = new CircleArc2D(Block223Controller.toPixelsX(block.getX()), Block223Controller.toPixelsY(block.getY())+Block.SIZE, Ball.BALL_DIAMETER/2, Math.PI,Math.PI* 3/2, false);
	   CircleArc2D arcH = new CircleArc2D(Block223Controller.toPixelsX(block.getX())+Block.SIZE, Block223Controller.toPixelsY(block.getY())+Block.SIZE, Ball.BALL_DIAMETER/2, 3*Math.PI/2, 2*Math.PI, false);
	   ArrayList<BouncePoint> intersectionArray = new ArrayList<>();
	   int numBouncePoints = 0;
	   BouncePoint closestPoint = null;

	   if (blockRect.intersectsLine(ballSegment)) {

	    	if (lineA.intersectsLine(ballSegment)) {
	    		intersectionArray.add(new BouncePoint(calculateIntersectionPoint(ballSegment, lineA).getX(), calculateIntersectionPoint(ballSegment, lineA).getY(), BounceDirection.FLIP_Y));
	    		numBouncePoints++;
	    	}
	    	if (lineB.intersectsLine(ballSegment)) {
	    		intersectionArray.add(new BouncePoint(calculateIntersectionPoint(ballSegment, lineB).getX(), calculateIntersectionPoint(ballSegment, lineB).getY(), BounceDirection.FLIP_X));
	    		numBouncePoints++;
	    	}
	    	if (lineC.intersectsLine(ballSegment)) {
	    		intersectionArray.add(new BouncePoint(calculateIntersectionPoint(ballSegment, lineC).getX(), calculateIntersectionPoint(ballSegment, lineC).getY(), BounceDirection.FLIP_X));
	    		numBouncePoints++;
	    	}
	    	if (lineD.intersectsLine(ballSegment)) {
	    		intersectionArray.add(new BouncePoint(calculateIntersectionPoint(ballSegment, lineD).getX(), calculateIntersectionPoint(ballSegment, lineD).getY(), BounceDirection.FLIP_Y));
	    		numBouncePoints++;
	    	}
	    	if (arcE.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY)).size() != 0) {
	    		ArrayList<math.geom2d.Point2D> var = arcE.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY));
	    		for(int i = 0; i < var.size(); i ++) {
					if (ballDirectionX < 0) {
		    			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_Y));
		        		numBouncePoints++;
					}
					else {
		    			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_X));
		        		numBouncePoints++;
					}
	    		}
	    	}
	    	if (arcF.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY)).size() != 0) {
	    		ArrayList<math.geom2d.Point2D> var = arcF.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY));
	    		for(int i = 0; i < var.size(); i ++) {
	    			if (ballDirectionX < 0) {
	        			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_X));
	            		numBouncePoints++;
	    			}
	    			else {
	        			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_Y));
	            		numBouncePoints++;
	    			}
	    		}
	    	}

	     	if (arcG.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY)).size() != 0) {
	    		ArrayList<math.geom2d.Point2D> var = arcG.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY));
	    		for(int i = 0; i < var.size(); i ++) {
	    			if (ballDirectionX < 0) {
	        			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_Y));
	            		numBouncePoints++;
	    			}
	    			else {
	        			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_X));
	            		numBouncePoints++;
	    			}
	    		}
	    	}
	     	if (arcH.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY)).size() != 0) {
	    		ArrayList<math.geom2d.Point2D> var = arcH.intersections(new math.geom2d.line.Line2D(getCurrentBallX(), getCurrentBallY(), getCurrentBallX() + ballDirectionX, getCurrentBallY() + ballDirectionY));
	    		for(int i = 0; i < var.size(); i ++) {
	    			if (ballDirectionX < 0) {
	        			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_X));
	            		numBouncePoints++;
	    			}
	    			else {
	        			intersectionArray.add(new BouncePoint(var.get(i).getX(), var.get(i).getY(), BounceDirection.FLIP_Y));
	            		numBouncePoints++;
	    			}
	    		}
	    	}
	    	if (numBouncePoints == 1) {
	    		closestPoint = intersectionArray.get(0);
	    	}
	    	else {
	    		for(int i = 0; i < numBouncePoints - 1; i++) {
	        		if(isCloser(intersectionArray.get(i), intersectionArray.get(i + 1))) {
	        			closestPoint = intersectionArray.get(i);
	        		}
	        		else {
	        			closestPoint = intersectionArray.get(i + 1);
	        		}
	        	}
	    	}
	    }
	   if (closestPoint != null && round(newBallX,5) == round(closestPoint.getX(),5) && round(newBallY,4) == round(closestPoint.getY(),4)){
			return null;
		}
	   if(closestPoint != null) {
			closestPoint.setHitBlock(block);
		}



	    return closestPoint;
	  }
   public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "score" + ":" + getScore()+ "," +
            "lives" + ":" + getLives()+ "," +
            "currentLevel" + ":" + getCurrentLevel()+ "," +
            "waitTime" + ":" + getWaitTime()+ "," +
            "playername" + ":" + getPlayername()+ "," +
            "ballDirectionX" + ":" + getBallDirectionX()+ "," +
            "ballDirectionY" + ":" + getBallDirectionY()+ "," +
            "currentBallX" + ":" + getCurrentBallX()+ "," +
            "currentBallY" + ":" + getCurrentBallY()+ "," +
            "currentPaddleLength" + ":" + getCurrentPaddleLength()+ "," +
            "currentPaddleX" + ":" + getCurrentPaddleX()+ "," +
            "currentPaddleY" + ":" + getCurrentPaddleY()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "bounce = "+(getBounce()!=null?Integer.toHexString(System.identityHashCode(getBounce())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "block223 = "+(getBlock223()!=null?Integer.toHexString(System.identityHashCode(getBlock223())):"null");
  }
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------

  // line 107 "../../../../../Block223Persistence.ump"
  private static final long serialVersionUID = 8597675110221231714L ;


}
