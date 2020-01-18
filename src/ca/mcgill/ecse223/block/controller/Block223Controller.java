package ca.mcgill.ecse223.block.controller;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOUserMode.Mode;
import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.model.Admin;
import ca.mcgill.ecse223.block.model.Ball;
import ca.mcgill.ecse223.block.model.Block;
import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.BlockAssignment;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.model.HallOfFameEntry;
import ca.mcgill.ecse223.block.model.Level;
import ca.mcgill.ecse223.block.model.Paddle;
import ca.mcgill.ecse223.block.model.PlayedBlockAssignment;
import ca.mcgill.ecse223.block.model.PlayedGame;
import ca.mcgill.ecse223.block.model.PlayedGame.PlayStatus;
import ca.mcgill.ecse223.block.model.Player;
import ca.mcgill.ecse223.block.model.User;
import ca.mcgill.ecse223.block.model.UserRole;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.view.Block223Page;
import ca.mcgill.ecse223.block.view.Block223PlayModeInterface;

public class Block223Controller {

	// ****************************
	// Modifier methods
	// ****************************
	public static void createGame(String name) throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to create a game.");
		}
		Block223 block223 = Block223Application.getBlock223();
		List<Game> games = block223.getGames();


		for(Game game : games) {
			if(game.getName().equals(name)) {
				throw new InvalidInputException("The name of a game must be unique.");
			}

		}

		if (name == null) {
			throw new InvalidInputException("The name of a game must be specified.");
		}

		if (name == "") {
			throw new InvalidInputException("The name of a game must be specified.");
		}

		UserRole admin = Block223Application.getCurrentUserRole();
		try {
			Game newGame = new Game(name, 1, (Admin) admin, 1, 1, 1, 10, 10, block223);
			block223.addGame(newGame);
			((Admin) Block223Application.getCurrentUserRole()).addGame(newGame);

		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void setGameDetails(int nrLevels, int nrBlocksPerLevel, int minBallSpeedX, int minBallSpeedY,
			Double ballSpeedIncreaseFactor, int maxPaddleLength, int minPaddleLength) throws InvalidInputException {

		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to define game settings.");
		}

		Game game = Block223Application.getCurrentGame();

		if (game == null) {
			throw new InvalidInputException("A game must be selected to define game settings.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can define its game settings.");
		}
		if (game.getPublished()){
			throw new InvalidInputException("A published game cannot be edited.");
		}
		if(!(nrLevels >= 1 && nrLevels<=99)) {
			throw new InvalidInputException("The number of levels must be between 1 and 99.");
		}

		if(nrBlocksPerLevel <= 0) {
			throw new InvalidInputException("The number of blocks per level must be greater than zero.");
		}else if(nrBlocksPerLevel < game.numberOfBlockAssignments()) { // 1 on this line is a placeholder for the existing number of levels in a game
			throw new InvalidInputException("The maximum number of blocks per level cannot be less than the number of existing blocks in a level.");
		}else game.setNrBlocksPerLevel(nrBlocksPerLevel);

		Paddle paddle = game.getPaddle();

		if(minPaddleLength <= 0){
			throw new InvalidInputException("The minimum length of the paddle must be greater than zero.");
		}else paddle.setMinPaddleLength(minPaddleLength);

		if(maxPaddleLength > 390 || maxPaddleLength <= 0){
			throw new InvalidInputException("The maximum length of the paddle must be greater than zero and less than or equal to 390.");
		}else paddle.setMaxPaddleLength(maxPaddleLength);

		Ball ball = game.getBall();

		if (minBallSpeedX < 0 || minBallSpeedY < 0 ||(minBallSpeedX == 0 && minBallSpeedY == 0)) {
			throw new InvalidInputException("The minimum speed of the ball must be greater than zero.");
		}else{
			ball.setMinBallSpeedX(minBallSpeedX);
			ball.setMinBallSpeedY(minBallSpeedY);
		}

		if(ballSpeedIncreaseFactor <= 0) {
			throw new InvalidInputException("The speed increase factor of the ball must be greater than zero.");
		}else ball.setBallSpeedIncreaseFactor(ballSpeedIncreaseFactor);

		List<Level> levels= game.getLevels(); // Look into

		int size = levels.size(); // Look into

		while(size < nrLevels) {
			game.addLevel();
			size++;
		}

		while(size > nrLevels) {
			size = levels.size() - 1;
			game.getLevel(size).delete();
		}
	}


	public static void deleteGame(String name) throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to delete a game.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can delete the game.");
		}

		Block223 block = Block223Application.getBlock223();

		Game game = block.findGame(name); //need to edit this part to findGame();

		if (game != null) {

		if (game.isPublished()) {
			throw new InvalidInputException("A published game cannot be deleted.");
		}
		else {

		}
		}

		if(game != null) {
			Block223 block223 = game.getBlock223();
			game.delete();
			Block223Persistence.save(block223);
		}
	}

	public static void selectGame(String name) throws InvalidInputException {


		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to select a game.");
		}
		Block223 block223 = Block223Application.getBlock223();
		Admin admin = (Admin) Block223Application.getCurrentUserRole();
		List<Game> games = block223.getGames();

		for(Game game : games) { //double check if this works
			String aName = game.getName();

			if(aName.equals(name)) {
				if(game.isPublished()) {
					throw new InvalidInputException("A published game cannot be changed.");
				}
				Admin gameAdmin= game.getAdmin();
				if(!gameAdmin.equals(admin)) {
					throw new InvalidInputException("Only the admin who created the game can select the game.");
				}

			}
		}

		//Game game = Block223Application.getCurrentGame();
		Game game = Block223Application.getBlock223().findGame(name); //need to edit this part to findGame();
		if (game==(null)) {
			throw new InvalidInputException("A game with name " + name + " does not exist." );
		}
		if (!Block223Application.getCurrentUserRole().equals(game.getAdmin())) {//change
			throw new InvalidInputException("Only the admin who created the game can define its game settings. ");
		}




		Block223Application.setCurrentGame(game);

	}



	public static void outOfBounds() throws InvalidInputException{

		PlayedGame game = Block223Application.getCurrentPlayableGame();

		if(game == null) {
			throw new InvalidInputException("Session Game Does Not Exist (or DNE for short) owo" );
		}
		if( (game.getCurrentBallY() + Ball.BALL_DIAMETER/2) > Game.PLAY_AREA_SIDE )
		{
			//				game.ballOutOfBounds();


		}

	}

	public static void updateGame(String name, int nrLevels, int nrBlocksPerLevel, int minBallSpeedX, int minBallSpeedY,
			Double ballSpeedIncreaseFactor, int maxPaddleLength, int minPaddleLength) throws InvalidInputException {




		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to define game settings.");
		}
		if (Block223Application.getCurrentGame()==(null)) {
			throw new InvalidInputException("A game must be selected to define game settings.");
		}
		if (!Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin())) {
			throw new InvalidInputException("Only the admin who created the game can define its game settings.");
		}

		Block223 block223 = Block223Application.getBlock223();
		Admin admin = Block223Application.getCurrentGame().getAdmin();
		List<Game> games = block223.getGames();

		for(Game game : games) { //double check if this works
			String aName = game.getName();

			if(aName.equals(name)){
				if(game.isPublished()) {
					throw new InvalidInputException("A published game cannot be changed.");
				}
				Admin gameAdmin= game.getAdmin();
			if(!gameAdmin.equals(admin)) {
				throw new InvalidInputException("Only the admin who created the game can select the game.");
			}
			if(game.getName().equals(name) && games.size() !=1 ) {
				throw new InvalidInputException("The name of a game must be unique.");
			}


			}
		}

		if (name == null) {
			throw new InvalidInputException("The name of a game must be specified.");
		}

		if (name == "") {
			throw new InvalidInputException("The name of a game must be specified.");
		}
		boolean wasSet = Block223Application.getCurrentGame().setName(name);
		if(wasSet == false) {
		throw new InvalidInputException("The name of a game must be unique.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can select the game.");
		}
		setGameDetails(nrLevels, nrBlocksPerLevel, minBallSpeedX, minBallSpeedY,
			 ballSpeedIncreaseFactor, maxPaddleLength, minPaddleLength);
	}
public static void addBlock(int red, int green, int blue, int points) throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to add a block.");
		}

		Game game = Block223Application.getCurrentGame();

		if (game==(null)) {
			throw new InvalidInputException("A game must be selected to add a block.");
		}
		if (!Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin())) {
			throw new InvalidInputException("Only the admin who created the game can add a block.");
		}
		if (Block223Application.getCurrentGame().getPublished()){
			throw new InvalidInputException("A published game cannot be edited.");
		}
		List<Block> blockList = Block223Application.getCurrentGame().getBlocks();
		for(Block block: blockList) {
			if(block.getRed() == red &
					block.getBlue() == blue &
					block.getGreen() == green) {

				throw new InvalidInputException("A block with the same color already exists for the game.");
			}
		}


		if (red < 0 || red > 255) {
			throw new InvalidInputException("Red must be between 0 and 255.");
		}
		if (green < 0 || green > 255) {
			throw new InvalidInputException("Green must be between 0 and 255.");
		}

		if (blue < 0 || blue > 255) {
			throw new InvalidInputException("Blue must be between 0 and 255.");
		}
		if (points < 1 || points > 1000) {
			throw new InvalidInputException("Points must be between 1 and 1000.");
		}

		try {
			game.addBlock(red, green, blue, points);
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}

	public static void deleteBlock(int id) throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to delete a block.");
		}
		if (Block223Application.getCurrentGame()==null) {
			throw new InvalidInputException("A game must be selected to delete a block.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can delete a block.");
		}
		if (Block223Application.getCurrentGame().getPublished()){
			throw new InvalidInputException("A published game cannot be edited.");
		}

		Game game = Block223Application.getCurrentGame();
		Block block = game.findBlock(id);
		if (block!=null) {
			block.delete();
		}
	}

	public static void updateBlock(int id, int red, int green, int blue, int points) throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to update a block.");
		}
		if (Block223Application.getCurrentGame()==(null)) {
			throw new InvalidInputException("A game must be selected to update a block.");
		}
		if (!Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin())) {
			throw new InvalidInputException("Only the admin who created the game can update a block.");
		}
		if (Block223Application.getCurrentGame().getPublished()){
			throw new InvalidInputException("A published game cannot be edited.");
		}

		for (Block item: Block223Application.getCurrentGame().getBlocks()) {
	        if (item.getRed() == red && item.getGreen() == green && item.getBlue() == blue) {
	        	if (item.getId() == id) {
					break;
				}
	        	throw new InvalidInputException("A block with the same color already exists for the game.");
	        }
	    }

		Game game = Block223Application.getCurrentGame();
		Block block = game.findBlock(id);
		if (block == null) {
			throw new InvalidInputException("The block does not exist.");
		}
		if (red < 0 || red > 255) {
			throw new InvalidInputException("Red must be between 0 and 255.");
		} else {
			block.setRed(red);
		}
		if (green < 0 || green > 255) {
			throw new InvalidInputException("Green must be between 0 and 255.");
		} else {
			block.setGreen(green);
		}
		if (blue < 0 || blue > 255) {
			throw new InvalidInputException("Blue must be between 0 and 255.");
		} else {
			block.setBlue(blue);
		}
		if (points < 1 || points > 1000) {
			throw new InvalidInputException("Points must be between 1 and 1000.");
		} else {
			block.setPoints(points);
		}
	}

	private static boolean sameBlock(List<Block> blocks, int red, int green, int blue) {
		for (Block item: blocks) {
			if (item.getRed() == red && item.getGreen() == green && item.getBlue() == blue) {
				return true;
			}
		}
		return false;
	}

	public static void positionBlock(int id, int level, int gridHorizontalPosition, int gridVerticalPosition)
			throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to position a block.");
		}
		if (Block223Application.getCurrentGame()==(null)) {
			throw new InvalidInputException("A game must be selected to position a block.");
		}
		if (!Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin())) {
			throw new InvalidInputException("Only the admin who created the game can position a block.");
		}
		if (Block223Application.getCurrentGame().getPublished()){
			throw new InvalidInputException("A published game cannot be edited.");
		}

		Game game = Block223Application.getCurrentGame();
		Level lvl = null;
		try {
			lvl = game.getLevel(level - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException("Level " + level + " does not exist for the game.");
		}

		if (lvl.numberOfBlockAssignments() >= game.getNrBlocksPerLevel()) {
			throw new InvalidInputException("The number of blocks has reached the maximum number (" +
					game.getNrBlocksPerLevel() + ") allowed for this game.");
		}

		if (sameAssignment(lvl.getBlockAssignments(),	gridHorizontalPosition, gridVerticalPosition)) {
			throw new InvalidInputException("A block already exists at location " + gridHorizontalPosition +
					"/" + gridVerticalPosition + ".");
		}

		Block block = game.findBlock(id);
		if (block == null) {
			throw new InvalidInputException("The block does not exist.");
		}
		try {
			new BlockAssignment(gridHorizontalPosition, gridVerticalPosition, lvl, block, game);
		} catch (Exception e) {
			throw e;
		}

	}

	private static boolean sameAssignment(List<BlockAssignment> assignments, int gridHorizontalPos, int gridVerticalPos) {
		for (BlockAssignment blockAssignment : assignments) {
			if (blockAssignment.getGridHorizontalPosition() == gridHorizontalPos &&
					blockAssignment.getGridVerticalPosition() == gridVerticalPos) {
				return true;
			}
		}
		return false;
	}

	public static void moveBlock(int level, int oldGridHorizontalPosition, int oldGridVerticalPosition,
			int newGridHorizontalPosition, int newGridVerticalPosition) throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to move a block.");
		}
		if (Block223Application.getCurrentGame()==null) {
			throw new InvalidInputException("A game must be selected to move a block.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can move a block.");
		}
		if (Block223Application.getCurrentGame().getPublished()){
			throw new InvalidInputException("A published game cannot be edited.");
		}

		Game game = Block223Application.getCurrentGame();
		Level aLevel;
		try {
			aLevel = game.getLevel(level - 1);
		}
		catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException("Level " + level + " does not exist for the game.");
		}
		List<BlockAssignment> blockAssignments = game.getBlockAssignments();
		for (BlockAssignment blockAssignment: blockAssignments) {
			if (blockAssignment.getGridHorizontalPosition() == newGridHorizontalPosition && blockAssignment.getGridVerticalPosition() == newGridVerticalPosition) {
				throw new InvalidInputException("A block already exists at location " + newGridHorizontalPosition + "/" + newGridVerticalPosition + ".");
			}
		}
		BlockAssignment blockAssignment = aLevel.findBlockAssignment(oldGridHorizontalPosition, oldGridVerticalPosition);
		if (blockAssignment == null) {
			throw new InvalidInputException("A block does not exist at location " + oldGridHorizontalPosition + "/" + oldGridVerticalPosition + ".");
		}
		int maxNumberOfHorizontalBlocks = game.maxNumberOfHorizontalBlocks();
		try {
			blockAssignment.setGridHorizontalPosition(newGridHorizontalPosition);
		}
		catch (RuntimeException e) {
			throw new InvalidInputException("The horizontal position must be between 1 and " + maxNumberOfHorizontalBlocks + ".");
		}
		int maxNumberOfVerticalBlocks = game.maxNumberOfVerticalBlocks();
		try {
			blockAssignment.setGridVerticalPosition(newGridVerticalPosition);
		}
		catch (RuntimeException e) {
			throw new InvalidInputException("The vertical position must be between 1 and " + maxNumberOfVerticalBlocks + ".");
		}
	}

	public static void removeBlock(int level, int gridHorizontalPosition, int gridVerticalPosition)
			throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to remove a block.");
		}
		if (Block223Application.getCurrentGame()==null) {
			throw new InvalidInputException("A game must be selected to remove a block.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can remove a block.");
		}
		if (Block223Application.getCurrentGame().getPublished()){
			throw new InvalidInputException("A published game cannot be edited.");
		}

		Game game = Block223Application.getCurrentGame();
		Level aLevel = game.getLevel(level - 1);
		BlockAssignment blockAssignment = aLevel.findBlockAssignment(gridHorizontalPosition, gridVerticalPosition);
		if (blockAssignment != null) {
			blockAssignment.delete();
		}
	}

	public static void saveGame() throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to save a game.");
		}
		if (Block223Application.getCurrentGame()==null) {
			throw new InvalidInputException("A game must be selected to save it.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can save it.");
		}

		Block223 block223 = Block223Application.getBlock223();
		try {
			save(block223);
		} catch(Exception e) {
			throw e;
		}
	}

	public static void save(Block223 block223) throws RuntimeException {
		try {
			Block223Persistence.save(block223);
		} catch(RuntimeException e) {
			throw e;
		}
	}

	public static void register(String username, String playerPassword, String adminPassword)throws InvalidInputException {

		Block223 block223 = Block223Application.getBlock223();

		if ((Block223Application.getCurrentUserRole() instanceof Admin || Block223Application.getCurrentUserRole() instanceof Player )) {
			throw new InvalidInputException("Cannot register a new user while a user is logged in.");
		}
		if (username == null || username.equals("")){
			throw new InvalidInputException("The username must be specified.");
		}
		if (playerPassword == null || playerPassword.equals("")){
			throw new InvalidInputException("The player password needs to be specified.");
		}
		if (playerPassword.equals(adminPassword)) {
			throw new InvalidInputException("The passwords have to be different.");
		}


		Player player = new Player(playerPassword, block223);
		User user;

		try {
			user = new User(username, block223, player);
			block223.addUser(user);
		}catch (RuntimeException e) {
			player.delete();
			throw new InvalidInputException("The username has already been taken.");
		}
		if(!(adminPassword == null || adminPassword.equals(""))){
			Admin admin = new Admin(adminPassword, block223);
			user.addRole(admin);
		}

		Block223Persistence.save(block223);

	}

	public static void login(String username, String password) throws InvalidInputException {

		Block223 block223 = Block223Application.getBlock223();

		if ((Block223Application.getCurrentUserRole() instanceof Admin || Block223Application.getCurrentUserRole() instanceof Player )) {
			throw new InvalidInputException("Cannot login a user while a user is already logged in.");
		}

		if(username.equals("")){
			throw new InvalidInputException("The username and password do not match.");
		}

		Block223Application.resetBlock223();

		User user = User.getWithUsername(username);

		if(user == null) {
			throw new InvalidInputException("The username and password do not match.");
		}

		List<UserRole> role = user.getRoles();  // LOOK INTO

		//		boolean is_set = false;
		for(UserRole aRole: role){

            String rolePassword = aRole.getPassword();

            String admin_password = password;

			if (rolePassword.equals(admin_password)) {

                Block223Application.setCurrentUserRole(aRole);
                return;
            }
            else{
            	 String player_password = password;
            	if(rolePassword.equals(player_password)) {
                Block223Application.setCurrentUserRole(aRole);
                return;
            }
            }

		}
		//        if(!is_set) {
		throw new InvalidInputException("The username and password do not match.");
		//        }

		//refresh();
	}

	public static void logout() {

		Block223Application.setCurrentUserRole(null);

		//refresh();
	}

	// play mode

	public static void selectPlayableGame(String name, int id) throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() == null)) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		Game game = Block223Application.getBlock223().findGame(name);
		Block223 block223 = Block223Application.getBlock223();
		PlayedGame pgame;

		if (game != null) {
			Player player = (Player) Block223Application.getCurrentUserRole();
			String username = User.findUsername(player);

			PlayedGame result = new PlayedGame(username, game, block223);
			pgame = result;
			pgame.setPlayer(player);
		} else {
			pgame = block223.findPlayableGame(id);
		}
		if ((game == null) && (pgame == null))
			throw new InvalidInputException("The game does not exist.");

		if ((game == null) && (Block223Application.getCurrentUserRole() != pgame.getPlayer()))
			throw new InvalidInputException("Only the player that started a game can continue the game.");

		Block223Application.setCurrentPlayableGame(pgame);
	}
	private static void LeftPress(PlayedGame pgame) {

		//double currentPaddleLength = pgame.getCurrentPaddleLength();
        double currentPaddleX = pgame.getCurrentPaddleX();
		double shift = PlayedGame.PADDLE_MOVE_RIGHT;

		if(currentPaddleX >0)
		pgame.setCurrentPaddleX(pgame.getCurrentPaddleX() - shift);
	}

	private static void RightPress(PlayedGame pgame) {

		double currentPaddleLength = pgame.getCurrentPaddleLength();
        double currentPaddleX = pgame.getCurrentPaddleX();
		double shift = PlayedGame.PADDLE_MOVE_LEFT;

		if(Game.PLAY_AREA_SIDE-currentPaddleLength >currentPaddleX )
		pgame.setCurrentPaddleX(pgame.getCurrentPaddleX() - shift);
	}

	public static void updatePaddlePosition(String userinputs) {

		PlayedGame pgame = Block223Application.getCurrentPlayableGame();



		for (int i = 0;i<userinputs.length();i++) {
			if(userinputs.charAt(i) == 'l') LeftPress(pgame);

			if(userinputs.charAt(i) == 'r') RightPress(pgame);

			if(userinputs.charAt(i) == ' ') {
				break;
			}
		}
	}

	public static void startGame(Block223PlayModeInterface ui) throws InvalidInputException {

		String error = "";
		if(Block223Application.getCurrentUserRole() == null ||
				(Block223Application.getCurrentUserRole() instanceof Admin
						&& Block223Application.getCurrentPlayableGame().getPlayer() != null)) {
			error = "Player privileges are required to play a game.";
			throw new InvalidInputException(error.trim());
		}
		if(Block223Application.getCurrentPlayableGame() == null) {
			error = "A game must be selected to play it.";
			throw new InvalidInputException(error.trim());
		}
        if ((Block223Application.getCurrentUserRole() instanceof Player) && (Block223Application.getCurrentPlayableGame().getPlayer() == null)) {
            error = "Admin privileges are required to test a game.";
            throw new InvalidInputException(error.trim());
        }
        if ((Block223Application.getCurrentUserRole() instanceof Admin) &&
        		(Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin())) {//Check for the admin of the function
            error = "Only the admin of a game can test the game.";
            throw new InvalidInputException(error.trim());
        }

		PlayedGame game = Block223Application.getCurrentPlayableGame();
		game.play(); // game: PlayedGame

		ui.takeInputs();

		while(game.getPlayStatus() == PlayStatus.Moving) {
			String userinputs = ui.takeInputs();
			updatePaddlePosition(userinputs);
			game.move();
			if(userinputs.contains(" ")) {
				game.pause();
			}
			try {
				Thread.sleep((long)game.getWaitTime());
			}
			catch(InterruptedException ex)
			{
			    Thread.currentThread().interrupt();
			}
			ui.refresh();
		}
		if(game.getPlayStatus() == PlayStatus.GameOver) {
			Block223Application.setCurrentPlayableGame(null);
			save(Block223Application.getBlock223());
			ui.endGame();
		}
		else if(game.getPlayer()!= null) {
			Block223 block223 = Block223Application.getBlock223();
			Block223Persistence.save(block223);
		}
		
	}
	public static void testGame(Block223PlayModeInterface ui) throws InvalidInputException {
		Game game = Block223Application.getCurrentGame();
		UserRole admin = Block223Application.getCurrentUserRole();
		Block223 block223 = Block223Application.getBlock223();

		if (!(admin instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to test a game.");
		}
		if (game == null) {
			throw new InvalidInputException("A game must be selected to test it.");
		}
		if (!game.getAdmin().equals(admin)) {
			throw new InvalidInputException("Only the admin who created the game can test it.");
		}
		if (game.getBlocks().size()<1) {
			throw new InvalidInputException("At least one block must be defined for a game to be tested.");
		}

		String username = User.findUsername(admin);
		PlayedGame pGame = new PlayedGame(username, game, block223);
		pGame.setPlayer(null);
		Block223Application.setCurrentPlayableGame(pGame);
		startGame(ui);
	}

	public static void publishGame () throws InvalidInputException {
		Game game = Block223Application.getCurrentGame();
		UserRole admin = Block223Application.getCurrentUserRole();
		Block223 block223 = Block223Application.getBlock223();
		if (!(admin instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to publish a game.");
		}
		if (game == null) {
			throw new InvalidInputException("A game must be selected to publish it.");
		}
		if (!game.getAdmin().equals(admin)) {
			throw new InvalidInputException("Only the admin who created the game can publish it.");
		}
		if (game.getBlocks().size()<1) {
			throw new InvalidInputException("At least one block must be defined for a game to be published.");
		}
		game.setPublished(true);
	}


	// ****************************
	// Query methods
	// ****************************
	public static List<TOGame> getDesignableGames() throws InvalidInputException{
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}

		Block223 block223 = Block223Application.getBlock223();
		UserRole admin = Block223Application.getCurrentUserRole();
		ArrayList<TOGame> result = new ArrayList<>();
		List<Game> games = block223.getGames();

		for(Game game : games) { //double check if this works
			Admin gameAdmin = game.getAdmin();
			if(gameAdmin.equals(admin) && !game.isPublished()) {
				TOGame	to = new TOGame(game.getName(), game.getLevels().size(), game.getNrBlocksPerLevel(), game.getBall().getMinBallSpeedX(),
						game.getBall().getMinBallSpeedY(), game.getBall().getBallSpeedIncreaseFactor(), game.getPaddle().getMaxPaddleLength(), game.getPaddle().getMinPaddleLength());
				result.add(to);
			}
		}
		return result;

	}

	public static TOGame getCurrentDesignableGame() throws InvalidInputException {


		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		if (Block223Application.getCurrentGame()==(null)) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (!Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin())) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}

		Game game = Block223Application.getCurrentGame();
		TOGame to = new TOGame(game.getName(), game.getLevels().size(), game.getNrBlocksPerLevel(), game.getBall().getMinBallSpeedX(),
				game.getBall().getMinBallSpeedY(), game.getBall().getBallSpeedIncreaseFactor(), game.getPaddle().getMaxPaddleLength(),
				game.getPaddle().getMinPaddleLength());

		return to;

	}

	public static List<TOBlock> getBlocksOfCurrentDesignableGame() throws InvalidInputException{
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		if (Block223Application.getCurrentGame()==(null)) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (!Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin())) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}

		Game game = Block223Application.getCurrentGame();
		ArrayList<TOBlock> result = new ArrayList<TOBlock>();
		List<Block> blocks = game.getBlocks();
		for (Block block : blocks) {
			TOBlock to = new TOBlock(block.getId(), block.getRed(), block.getGreen(), block.getBlue(), block.getPoints());
			result.add(to);
		}
		return result;
	}

	public static TOBlock getBlockOfCurrentDesignableGame(int id) throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		if (Block223Application.getCurrentGame()==(null)) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (!Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin())) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}

		Game game = Block223Application.getCurrentGame();
		Block block = game.findBlock(id);

		if (block == null) {
			throw new InvalidInputException("The block does not exist.");
		}

		TOBlock to = new TOBlock(block.getId(), block.getRed(), block.getGreen(), block.getGreen(), block.getPoints());

		return to;
	}

	public static List<TOGridCell> getBlocksAtLevelOfCurrentDesignableGame(int level) throws InvalidInputException {

		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		if (Block223Application.getCurrentGame()==(null)) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (!Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin())) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}

		Game game = Block223Application.getCurrentGame();
		List<TOGridCell> result = new ArrayList<TOGridCell>();
		Level lvl = null;
		try {
			lvl = game.getLevel(level - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException("Level " + level + " does not exist for the game.");
		}
		List<BlockAssignment> assignments = lvl.getBlockAssignments();
		for (BlockAssignment blockAssignment : assignments) {
			TOGridCell to = new TOGridCell(blockAssignment.getGridHorizontalPosition(), blockAssignment.getGridVerticalPosition(),
					blockAssignment.getBlock().getId(), blockAssignment.getBlock().getRed(),
					blockAssignment.getBlock().getGreen(), blockAssignment.getBlock().getBlue(),
					blockAssignment.getBlock().getPoints());
			result.add(to);
		}
		return result;
	}

	public static TOUserMode getUserMode() {

		UserRole role = Block223Application.getCurrentUserRole();

		TOUserMode to = new TOUserMode(null);

		if(role instanceof Admin) {
			to.setMode(Mode.Design);
		}
		else if(role instanceof Player) {
			to.setMode(Mode.Play);
		}
		else {
			to.setMode(Mode.None);
		}
		return to;
	}

	public static TOHallOfFame getHallOfFame(int start, int end) throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() == null )) {
			throw new InvalidInputException("Player privileges are required to access a gameÂ’s hall of fame.");
		}
		if (Block223Application.getCurrentPlayableGame()== null) {
			throw new InvalidInputException("A game must be selected to view its hall of fame.");
		}
		PlayedGame pgame = Block223Application.getCurrentPlayableGame();
		Game game = pgame.getGame();
		TOHallOfFame result = new TOHallOfFame(game.getName());

		if (start < 1) {
			start = 1;
		}
		if (end > game.numberOfHallOfFameEntries()) {
			end = game.numberOfHallOfFameEntries();
		}
		start = game.numberOfHallOfFameEntries() - start;
		end = game.numberOfHallOfFameEntries() - end;

		for (int i = start; i >= end; i--) {
			TOHallOfFameEntry to = new TOHallOfFameEntry(
					i + 1,
					game.getHallOfFameEntry(i).getPlayername(),
					game.getHallOfFameEntry(i).getScore(),
					result);
		}
		return result;
	}

	public static TOHallOfFame getHallOfFameWithMostRecentEntry(int numberOfEntries) throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to access a game’s hall of fame.");
		}
		if (Block223Application.getCurrentPlayableGame() == null) {
			throw new InvalidInputException("A game must be selected to view its hall of fame.");
		}
		PlayedGame pgame = Block223Application.getCurrentPlayableGame();
		Game game = pgame.getGame();
		TOHallOfFame result = new TOHallOfFame(game.getName());
		HallOfFameEntry mostRecent = game.getMostRecentEntry();
		int indexR = game.indexOfHallOfFameEntry(mostRecent);
		int start = indexR + numberOfEntries/2;
		if (start > game.numberOfHallOfFameEntries() - 1) {
			start = game.numberOfHallOfFameEntries() - 1;
		}
		int end = start - numberOfEntries + 1;
		if (end < 0) {
			end = 0;
		}
		for (int i = start; i >= end; i--) {
			TOHallOfFameEntry to = new TOHallOfFameEntry(
					i + 1,
					game.getHallOfFameEntry(i).getPlayername(),
					game.getHallOfFameEntry(i).getScore(),
					result);
		}
		return result;
	}
	// play mode
		public static List<TOPlayableGame> getPlayableGames() throws InvalidInputException{
			String error = "";
			if (!(Block223Application.getCurrentUserRole() instanceof Player)) {
				error = "Player privileges are required to play a game.";
				throw new InvalidInputException(error);
			}
			Block223 block223 = Block223Application.getBlock223();
			UserRole player = Block223Application.getCurrentUserRole();
			List<TOPlayableGame> result = new ArrayList<>();  //  --> creates List <TOPlayableGame>
			List<Game> games = block223.getGames();
			for(Game agame: games) {
				if(agame.isPublished()) {
					TOPlayableGame to = new TOPlayableGame(agame.getName(),-1,0);
					result.add(to);
				}
			}
			List<PlayedGame> playedgames = ((Player) player).getPlayedGames();
			for(PlayedGame apgame: playedgames) {
				TOPlayableGame to = new TOPlayableGame(apgame.getGame().getName(), apgame.getId(), apgame.getCurrentLevel());
				result.add(to);
			}
			return result;
		}
		public static TOCurrentlyPlayedGame getCurrentPlayableGame() throws InvalidInputException{
			String error = "";

			if(Block223Application.getCurrentPlayableGame() == null) {
				error = "A game must be selected to play it.";
				throw new InvalidInputException(error.trim());
			}

			if(Block223Application.getCurrentUserRole() == null) {
				error = "Player privileges are required to play a game.";
				throw new InvalidInputException(error.trim());
			}
			if(Block223Application.getCurrentUserRole() instanceof Player &&
					Block223Application.getCurrentPlayableGame().getPlayer() == null) {
				error = "Admin privileges are required to test a game.";
				throw new InvalidInputException(error.trim());
			}
			if(Block223Application.getCurrentUserRole() instanceof Admin &&
					(Block223Application.getCurrentGame().getAdmin() != Block223Application.getCurrentUserRole())) {
				error = "Only the admin of a game can test the game.";
				throw new InvalidInputException(error.trim());
			}
			if(Block223Application.getCurrentUserRole() instanceof Admin && Block223Application.getCurrentPlayableGame().getPlayer() != null) {
				error = "Player privileges are required to play a game.";
				throw new InvalidInputException(error.trim());
			}


			PlayedGame pgame = Block223Application.getCurrentPlayableGame();
			boolean paused = pgame.getPlayStatus() == PlayStatus.Ready || pgame.getPlayStatus() == PlayStatus.Paused;

			TOCurrentlyPlayedGame result = new TOCurrentlyPlayedGame(pgame.getGame().getName(), paused,
					pgame.getScore(), pgame.getLives(),pgame.getCurrentLevel(), pgame.getPlayername(),
					(int)pgame.getCurrentBallX(), (int)pgame.getCurrentBallY(), (int)pgame.getCurrentPaddleLength(), (int)pgame.getCurrentPaddleX());

			List<PlayedBlockAssignment> blocks = pgame.getBlocks();
			for(PlayedBlockAssignment pblock: blocks) {
				TOCurrentBlock to = new TOCurrentBlock(pblock.getBlock().getRed(),pblock.getBlock().getGreen(),
						pblock.getBlock().getBlue(),pblock.getBlock().getPoints(),pblock.getX(),pblock.getY(), result);
			}
			return result;
		}
		public static void gameOverDelete() {
			Block223Application.getCurrentPlayableGame().delete();
		}
		
		public static int toPixelsY(int gridY){
			int blockSz = Block.SIZE;
			int rowPadding = Game.ROW_PADDING;
			int wallPadding = Game.WALL_PADDING;
			int posY = 0;

			posY += wallPadding;

			while(gridY != 1) {
				posY += (blockSz +rowPadding);
				gridY--;
			}
			
			return posY;
		}

		public static int toPixelsX(int gridX){
			int blockSz = Block.SIZE;
			int clmnPadding = Game.COLUMNS_PADDING;
			int wallPadding = Game.WALL_PADDING;
			int posX = 0;

			posX += wallPadding;

			while(gridX != 1) {
				posX += (blockSz + clmnPadding);
				gridX--;
			}
			
			return posX;
		}
}
