package ca.mcgill.ecse223.block.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOGame;
import ca.mcgill.ecse223.block.controller.TOPlayableGame;
import ca.mcgill.ecse223.block.controller.TOUserMode.Mode;

public class Block223Page extends JFrame {
	private static final long serialVersionUID = 8200587698425212261L;

	// UI elements
	private JLabel errorMessage;
	private JLabel successMessage;
	//login/logout
	private JTextField txtUsername;
	private JTextField pwdAdminpassword;
	private JTextField pwdPlayerpassword;
	private JButton loginButton;
	private JButton create_userButton;
	private JButton logoutButton;
	private JButton testGameButton;
	private JButton publishGameButton;



	//save
	private JButton saveButton;

	//game
	private HashMap<Integer, String> gameAvailable;
	private JButton createGame;
	private JButton deleteGame;
	private JButton playGameButton;
	private JButton defineGame;

	private JTextField gameNameTextField;
	// private JButton selectGameButton;
	private JComboBox<String> selectGameComboBox;
	private JLabel selectGameLabel;
	//game settings

	private Thread t2;

	//level
	// private JButton selectLevelButton;
	private JLabel selectLevelLabel;
	private JComboBox<Integer> selectLevelComboBox;

	//blocks
	private JTextField blockIdTextField;
	private JButton deleteBlockButton;

	private  JTextField blockIdField;
	private  JTextField rowPositionField;
	private  JTextField columnPositionField;


  private JLabel blockRangeLabel;
	private JLabel rowPositionLabel;
	private JLabel blockIdLabel;
	private JLabel columnPositionLabel;
	private JButton addBlockLevelButton;
	private JButton updateBlock;


	private JButton downBlockButton;
	private JButton upBlockButton;
	private BlockVisualizer blockVisualizer;
	protected static final int HEIGHT_BLOCK_VISUALIZER = 200;
	protected static final int WIDTH_BLOCK_VISUALIZER = 200;

	private LevelViewer levelViewer;
	protected static final int HEIGHT_BLOCK_VIEWER = 200;
	protected static final int WIDTH_BLOCK_VIEWER = 200;

	//block positioning declaration
	private JTextField currentRowTextField;
	private JTextField currentColumnTextField;
	private JTextField newRowTextField;
	private JTextField newColumnTextField;
	private JButton removeBlockButton;
	private JButton moveBlockButton;
	private JLabel currentRowLabel;
	private JLabel currentColumnLabel;
	private JLabel newRowLabel;
	private JLabel newColumnLabel;

	//game settings declaration


	private JTextField nrLevelsField;
	private JTextField nrBlocksPerLevelField;
	private JTextField minBallSpeedXField;
	private JTextField minBallSpeedYField;
	private JTextField ballSpeedIncreaseFactorField;
	private JTextField maxPaddleLengthField;
	private JTextField minPaddleLengthField;

	private JButton updateGameButton;
	private JLabel gameNameLabel;
	private JLabel nrLevelsLabel;
	private JLabel nrBlocksPerLevelLabel;
	private JLabel minBallSpeedXLabel;
	private JLabel minBallSpeedYLabel;
	private JLabel ballSpeedIncreaseFactorLabel;
	private JLabel maxPaddleLengthLabel;
	private JLabel minPaddleLengthLabel;


	//add block declaration
	private JTextField blockRedField;
	private JTextField blockGreenField;
	private JTextField blockBlueField;
	private JTextField blockPointsField;


	private JButton addBlockButton;
	private JLabel blockRedLabel;
	private JLabel blockGreenLabel;
	private JLabel blockBlueLabel;
	private JLabel blockPointsLabel;

	//JSeparator lines
	private JSeparator aboveLevelLine;
	private JSeparator belowLevelLine;
	private JSeparator blockSeparatorLine;
	private JSeparator visualizationSeperator;

	//data elements
	private String error = null;
	private String success = null;
	private HashMap<Integer, Integer> playableGamesMap = new HashMap<Integer, Integer>();

	/** Creates new form Block223Page */
	public Block223Page() {
		// init();
		refresh();
	}

// 	private void init() {
// //		ui = new Block223PlayPage(this);
// 	}

	private void refresh(){
		Mode userType = Block223Controller.getUserMode().getMode();
		switch (userType) {
		case None:
			initLogin();
			break;
		case Design:
			initComponents();
			break;
		case Play:
			initPlay();
			break;
		}
	}

	private void initLogin(){
		// elements for error message
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);

		txtUsername = new JTextField();
		txtUsername.setText("Username");

		pwdPlayerpassword = new JTextField();
		pwdPlayerpassword.setText("PlayerPassword");
		pwdPlayerpassword.setToolTipText("Player Password\r\n");

		loginButton = new JButton("Login");


		create_userButton = new JButton("Create new user");

		pwdAdminpassword = new JTextField();
		pwdAdminpassword.setText("AdminPassword");
		pwdAdminpassword.setToolTipText("Admin Password\r\n");


		loginButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logActionPerformed(evt);
			}
		});

		create_userButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				registrationActionPerformed(evt);
			}
		});


		//layout
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);



		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(errorMessage)
				.addComponent(txtUsername)
				.addComponent(pwdPlayerpassword)
				.addComponent(pwdAdminpassword)
				.addComponent(loginButton)
				.addComponent(create_userButton)
				);
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {create_userButton, loginButton});

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(errorMessage)
				.addComponent(txtUsername)
				.addComponent(pwdPlayerpassword)
				.addComponent(pwdAdminpassword)
				.addComponent(loginButton)
				.addComponent(create_userButton)
				);
		pack();
	}

	private void logActionPerformed(ActionEvent evt) {
		error = "";
		String username = txtUsername.getText();
		String player_password = pwdPlayerpassword.getText();
		String admin_password = pwdAdminpassword.getText();

		try {
			Block223Controller.login(username, player_password);
			 resetLogin();
			refresh();
		}catch(InvalidInputException e) {
			try {
				Block223Controller.login(username, admin_password);
				resetLogin();
				refresh();
			} catch(InvalidInputException ex) {
				error = ex.getMessage();
			}
		}
		refreshLogin();
	}
	private void registrationActionPerformed(ActionEvent evt) {
		error = "";
		String username = txtUsername.getText();
		String player_password = pwdPlayerpassword.getText();
		String admin_password = pwdAdminpassword.getText();

		try {
			Block223Controller.register(username, player_password, admin_password);
		}catch(InvalidInputException e) {
			error = e.getMessage();
		}
		refreshLogin();
	}


	private void initPlay(){
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);

		selectGameLabel = new JLabel("Game:");
		selectGameComboBox = new JComboBox<String>(new String[0]);
		logoutButton = new JButton("Logout");
		playGameButton = new JButton("Play");
		playableGamesMap.clear();
		try {
			for (TOPlayableGame pGame: Block223Controller.getPlayableGames()) {
				selectGameComboBox.addItem(pGame.getName());
				playableGamesMap.put(selectGameComboBox.getItemCount()-1, pGame.getNumber());
			}
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}




		playGameButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				playGame();
				refreshPlay();
			}
		});

		logoutButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				try {
					error = "";
					logoutActionPerformed(evt);
					resetPlay();
					refresh();
				} catch (InvalidInputException e) {
					error = e.getMessage();
				}
			}

			private void logoutActionPerformed(ActionEvent evt) throws InvalidInputException  {
				Block223Controller.logout();
			}
		});
		//layout
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
    selectGameComboBox.setPreferredSize(logoutButton.getPreferredSize());



		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(selectGameLabel)
				.addComponent(selectGameComboBox)
				.addGroup(layout.createParallelGroup()
						.addComponent(logoutButton)
						.addComponent(playGameButton)
						)
				);

		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {selectGameLabel, selectGameComboBox, playGameButton, logoutButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {playGameButton, logoutButton});

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(logoutButton)
				.addGroup(layout.createParallelGroup()
						.addComponent(selectGameLabel)
						.addComponent(selectGameComboBox)
						.addComponent(playGameButton)
						)
				);
		pack();
	}
	private void refreshLogin(){
		// error
		errorMessage.setText(error);
		pack();
	}
	private void refreshPlay(){
		// error
		errorMessage.setText(error);
		if (error == null || error.length() == 0) {
			// populate page with data
		}
		pack();
	}

	private void refreshLevelViewer(){
		Object levelnum = selectLevelComboBox.getSelectedItem();
		if (levelnum!=null) {
		levelViewer.refresh((int)levelnum);
		}
		else levelViewer.refresh(0);
	}

	private void refreshBlockVisualizer() {
		blockVisualizer.refresh();
	}
	/** This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		// elements for error message
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		successMessage = new JLabel();
		successMessage.setForeground(Color.GREEN);

		//elements for ...

		//login/logout

		logoutButton = new JButton("Logout");

		//layout

		logoutButton.addActionListener(new java.awt.event.ActionListener() {


			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				try {
					error = "";
					logoutActionPerformed(evt);
					resetDesign();
					refresh();
				} catch (InvalidInputException e) {
					error = e.getMessage();
				}
			}

			private void logoutActionPerformed(ActionEvent evt) throws InvalidInputException  {
				Block223Controller.logout();
			}
		});


		//save
		saveButton = new JButton();
		saveButton.setText("Save");
		//game




		selectGameComboBox = new JComboBox<String>(new String[0]);
		// selectGameButton = new JButton("Select Game");
		selectGameLabel = new JLabel("Game:");
    gameNameLabel = new JLabel("Game Name:");
		createGame = new JButton("Create New Game");
		defineGame = new JButton("Define Game");
		deleteGame = new JButton("Delete Game");
		gameNameTextField = new JTextField();
		testGameButton = new JButton("Test");
		publishGameButton = new JButton("Publish");



		//game settings



		//level

		selectLevelComboBox = new JComboBox<Integer>();
		// selectLevelButton = new JButton("Select Level");
    selectLevelLabel = new JLabel("Level:");

		//blocks
    blockIdTextField = new JTextField("");
    blockIdTextField.setPreferredSize(new Dimension(30,10));
    updateBlock = new JButton("Update Block");

		deleteBlockButton = new JButton();
		deleteBlockButton.setText("Delete");


		upBlockButton = new JButton();
		upBlockButton.setText("UP");
		downBlockButton = new JButton();
		downBlockButton.setText("DOWN");

		blockVisualizer = new BlockVisualizer();
		blockVisualizer.setMinimumSize(new Dimension(WIDTH_BLOCK_VISUALIZER, HEIGHT_BLOCK_VISUALIZER));

		levelViewer = new LevelViewer();
		levelViewer.setMinimumSize(new Dimension(WIDTH_BLOCK_VIEWER, HEIGHT_BLOCK_VIEWER));

		//block positioning initialization
		currentRowTextField = new JTextField();
		currentColumnTextField = new JTextField();
		newRowTextField = new JTextField();
		newColumnTextField = new JTextField();
		removeBlockButton = new JButton("Remove Block");
		moveBlockButton = new JButton("Move Block");
		currentRowLabel = new JLabel("Current row position: ");
		currentColumnLabel = new JLabel("Current column position: ");
		newRowLabel = new JLabel("New row position: ");
		newColumnLabel = new JLabel("New column position: ");

		//game settings initialization

		nrLevelsField = new JTextField();
		nrBlocksPerLevelField = new JTextField();
		minBallSpeedXField = new JTextField();
		minBallSpeedYField = new JTextField();
		ballSpeedIncreaseFactorField = new JTextField();
		maxPaddleLengthField = new JTextField();
		minPaddleLengthField = new JTextField();

		updateGameButton = new JButton("Update Game Settings");
    nrLevelsLabel = new JLabel("Number of Levels:");
    nrBlocksPerLevelLabel = new JLabel("Number of Blocks:");
		minBallSpeedXLabel = new JLabel("Ball (Minimum X speed): ");
		minBallSpeedYLabel = new JLabel("Ball (Minimum Y speed): ");
		ballSpeedIncreaseFactorLabel = new JLabel("Ball (Acceleration speed): ");
		maxPaddleLengthLabel = new JLabel("Paddle Length (max): ");
		minPaddleLengthLabel =	new JLabel("Paddle Length (min): ");


		blockIdField = new JTextField();
		rowPositionField  = new JTextField();
		columnPositionField= new JTextField();
    addBlockLevelButton = new JButton("Add Block to Level");
		rowPositionLabel = new JLabel("Row position: ");
		blockIdLabel = new JLabel("Block ID: ");
		columnPositionLabel = new JLabel("Column position: ");



		//game add block Intialization
		blockRedField = new JTextField();
		blockGreenField = new JTextField();
		blockBlueField = new JTextField();
		blockPointsField = new JTextField();

		 blockRangeLabel = new JLabel("RGB values between [0-255]");
		addBlockButton = new JButton("Add Block to Game");
		blockRedLabel = new JLabel("Enter Block Red: ");
		blockGreenLabel = new JLabel("Enter Block Green: ");
		blockBlueLabel = new JLabel("Enter Block Blue: ");
		blockPointsLabel= new JLabel("Enter Block Points: ");
		updateBlock = new JButton("Update Block");

		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Block 223");

		//listeners for ...

		//login/logout


		//save
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				saveButtonActionPerformed(evt);
			}
		});
		//game
		testGameButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				testGame();
			}
		});
		publishGameButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				error = "";
				try {
					Block223Controller.publishGame();
					success = "succesfully published game.";
				} catch (InvalidInputException e) {
					error = e.getMessage();
				}
				refreshData();
			}
		});

		createGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createGameActionPerformed(evt);
			}

		});
		deleteGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteGameActionPerformed(evt);
			}

		});


		//game settings
		updateGameButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateGameButtonActionPerformed(evt);
				TOGame currentGame = null;
				try {
					currentGame = Block223Controller.getCurrentDesignableGame();
				} catch (InvalidInputException e) {
					error = e.getMessage();
				};
				selectLevelComboBox.removeAllItems();
				for (int i = 1; i <= currentGame.getNrLevels(); i++) {
					selectLevelComboBox.addItem(i);
				}
			}
		});

		defineGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				defineGameButtonActionPerformed(evt);
				TOGame currentGame = null;
				try {
					currentGame = Block223Controller.getCurrentDesignableGame();
				} catch (InvalidInputException e) {
					error = e.getMessage();
				};
				selectLevelComboBox.removeAllItems();
				for (int i = 1; i <= currentGame.getNrLevels(); i++) {
					selectLevelComboBox.addItem(i);
				}
			}
		});
		//level
		//blocks
		deleteBlockButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteBlockButtonActionPerformed(evt);
			}
		});

		addBlockButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addBlockActionPerformed(evt);
			}
		});
		//block positioning listeners
		moveBlockButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				moveBlockButtonActionPerformed(evt);
			}
		});
		removeBlockButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeBlockButtonActionPerformed(evt);
			}
		});
		updateBlock.addActionListener(new java.awt.event.ActionListener() {
    		@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
    		updateBlockActionPerformed(evt);
    }
  });
    addBlockLevelButton.addActionListener(new java.awt.event.ActionListener() {
    		@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
    		addBlockToLevelButtonActionPerformed(evt);
    }
  });

		// listeners for block visualization
		upBlockButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				upBlockButtonActionPerformed(evt);
			}
		});
		downBlockButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				downBlockButtonActionPerformed(evt);
			}
		});

		selectGameComboBox.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshData();
			}
		});

		selectLevelComboBox.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshData();
			}
		});
		updateGamelist();

		// horizontal line elements
   	aboveLevelLine = new JSeparator();
    belowLevelLine = new JSeparator();
    blockSeparatorLine = new JSeparator();
    visualizationSeperator = new JSeparator(SwingConstants.VERTICAL);

    minPaddleLengthField.setPreferredSize(new Dimension((int)(createGame.getPreferredSize().getWidth()+updateGameButton.getPreferredSize().getWidth()+defineGame.getPreferredSize().getWidth()-minPaddleLengthLabel.getPreferredSize().getWidth()),(int)(createGame.getPreferredSize().getHeight())));
		//layout
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);



		layout.setHorizontalGroup(layout.createParallelGroup()
			.addGroup(layout.createSequentialGroup()
				.addComponent(errorMessage)
				.addComponent(successMessage)
			)
      .addComponent(aboveLevelLine)
      .addComponent(belowLevelLine)
//      // top menu section
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(selectGameLabel) //selectGameLabel
								.addComponent(selectGameComboBox) //selectGameComboBox
							  	// .addComponent(selectGameButton)
								.addComponent(deleteGame) //deleteGameButton

								.addGroup(layout.createParallelGroup()
										.addComponent(saveButton)
										.addComponent(publishGameButton)
								)
								.addGroup(layout.createParallelGroup()
										.addComponent(logoutButton)//logoutButton
										.addComponent(testGameButton)
								)
								)
						.addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
            .addComponent(gameNameLabel)
								.addComponent(nrBlocksPerLevelLabel)
								.addComponent(nrLevelsLabel)
        )
								.addGroup(layout.createParallelGroup()
	  								.addComponent(gameNameTextField) //gameNameTextField
										.addComponent(nrBlocksPerLevelField)
										.addComponent(nrLevelsField)
										)
								.addGroup(layout.createParallelGroup()
           .addGroup(layout.createSequentialGroup()
					 .addComponent(createGame) //create game button
					 .addComponent(defineGame)
           .addComponent(updateGameButton)
           )
           .addGroup(layout.createSequentialGroup()
             .addGroup(layout.createParallelGroup()
										.addComponent(minPaddleLengthLabel) //minPaddleSizeLabel
										.addComponent(maxPaddleLengthLabel) //MaxPaddleSizeLabel

										)
								.addGroup(layout.createParallelGroup()
										.addComponent(minPaddleLengthField) //minPaddleSizeTextField
										.addComponent(maxPaddleLengthField) //MaxPaddleSizeTextField
										)
           )
         )
								.addGroup(layout.createParallelGroup() //ball labels
										.addComponent(ballSpeedIncreaseFactorLabel) //
										.addComponent(minBallSpeedXLabel) //
										.addComponent(minBallSpeedYLabel) //

										)
								.addGroup(layout.createParallelGroup() //ball TextFields
										.addComponent(ballSpeedIncreaseFactorField) //
										.addComponent(minBallSpeedXField) //
										.addComponent(minBallSpeedYField) //

										)
								)
								)

				// bottom menu section
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup() // levelViewer group
								         .addComponent(levelViewer) // levelViewer
								.addGroup(layout.createSequentialGroup()
										.addComponent(selectLevelLabel) //levelSelectLabel
										.addComponent(selectLevelComboBox) //selectLevel
									  	// .addComponent(selectLevelButton)
										)
								)
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createParallelGroup() //blockPositioning
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup() //current position labels
														.addComponent(currentRowLabel) //
														.addComponent(currentColumnLabel) //
														.addComponent(removeBlockButton) //
														)
												.addGroup(layout.createParallelGroup() //TextFields
														.addComponent(currentRowTextField) //
														.addComponent(currentColumnTextField) //
														)
												.addGroup(layout.createParallelGroup() //new position labels
														.addComponent(newRowLabel) //
														.addComponent(newColumnLabel) //
														.addComponent(moveBlockButton) //moveBlockButton
														)
												.addGroup(layout.createParallelGroup() //TextFields
														.addComponent(newRowTextField) //
														.addComponent(newColumnTextField) //
														)
												)
										)
         				.addComponent(blockSeparatorLine)
								.addGroup(layout.createSequentialGroup() //blocks
										.addGroup(layout.createParallelGroup() //blockVisualizer
												.addGroup(layout.createSequentialGroup()
														.addComponent(upBlockButton)
														.addComponent(downBlockButton)
														)
												.addComponent(blockVisualizer)
												)
           .addComponent(visualizationSeperator)
           .addGroup(layout.createParallelGroup()
             .addGroup(layout.createSequentialGroup()
               .addComponent(updateBlock) //updateBlockButton
               .addComponent(deleteBlockButton)
             )
             .addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup() //block info 1
												.addComponent(blockRedLabel) //RGB labels
												.addComponent(blockGreenLabel) //RGB labels
												.addComponent(blockBlueLabel) //RGB labels
												.addComponent(blockPointsLabel) //Points labels
												.addComponent(addBlockButton) //addBlockGameButton
												.addComponent(blockIdLabel) //blockIdLabel
												.addComponent(rowPositionLabel) //rowpositionLabel
												.addComponent(columnPositionLabel) //columnPositionLabel
												.addComponent(addBlockLevelButton) //addBlockLevelButton
												)
               .addGroup(layout.createParallelGroup() //block info 2
												.addComponent(blockRedField) //RGB Field
												.addComponent(blockGreenField) //RGB Field
												.addComponent(blockBlueField) //RGB Field
												.addComponent(blockPointsField) //blockPointsTextField
												.addComponent(blockIdTextField)
												.addComponent(rowPositionField) //rowpositionTextField
												.addComponent(columnPositionField) //columnPositionTextField
												)
										)
								)
						)
       )
      )
				);

   //
   layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {saveButton, deleteBlockButton, removeBlockButton, moveBlockButton, selectLevelComboBox, createGame,updateGameButton, addBlockButton, addBlockLevelButton, updateBlock,selectGameComboBox, publishGameButton, testGameButton, defineGame}); //
   layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {saveButton, logoutButton, publishGameButton, testGameButton}); //
   layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {currentRowTextField, currentColumnTextField, newRowTextField, newColumnTextField, nrLevelsField,nrBlocksPerLevelField,minBallSpeedXField,minBallSpeedYField,ballSpeedIncreaseFactorField,maxPaddleLengthField,minPaddleLengthField,blockRedField,blockGreenField,blockBlueField,blockPointsField,columnPositionField,rowPositionField,blockIdTextField, gameNameTextField}); //

   layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {minPaddleLengthField,maxPaddleLengthField, minBallSpeedXField, minBallSpeedYField,ballSpeedIncreaseFactorField,nrBlocksPerLevelField, nrLevelsField, gameNameTextField}); //
//   layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {}); //

   layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {deleteBlockButton, moveBlockButton, removeBlockButton, updateGameButton, updateBlock});
   layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addBlockButton, addBlockLevelButton}); //

		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {currentRowLabel, currentColumnLabel, newRowLabel, newColumnLabel});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {upBlockButton, downBlockButton});

		//

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
					.addComponent(errorMessage)
					.addComponent(successMessage)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(selectGameLabel) //selectGameLabel
						.addComponent(selectGameComboBox) //selectGameComboBox
					  	// .addComponent(selectGameButton)
						.addComponent(deleteGame) //deleteGameButton
						.addComponent(saveButton)
						.addComponent(logoutButton) //logoutButton
						) //top menu (5)
				.addGroup(layout.createParallelGroup()
						.addComponent(publishGameButton)
						.addComponent(testGameButton)
				)
				.addGroup(layout.createParallelGroup()
     .addComponent(gameNameLabel)
	 .addComponent(gameNameTextField)
				.addComponent(minPaddleLengthLabel)
				.addComponent(minPaddleLengthField)
				.addComponent(minBallSpeedXLabel)
				.addComponent(minBallSpeedXField)
    )//1st setting line (6)
				.addGroup(layout.createParallelGroup()
				.addComponent(nrBlocksPerLevelLabel)
				.addComponent(nrBlocksPerLevelField)
				.addComponent(maxPaddleLengthLabel)
				.addComponent(maxPaddleLengthField)
				.addComponent(minBallSpeedYLabel)
				.addComponent(minBallSpeedYField)
	 ) //12nd setting line (4)
				.addGroup(layout.createParallelGroup()
				.addComponent(nrLevelsLabel)
				.addComponent(nrLevelsField)
				.addComponent(ballSpeedIncreaseFactorLabel)
				.addComponent(ballSpeedIncreaseFactorField)
     		.addComponent(updateGameButton)
				.addComponent(createGame)
				.addComponent(defineGame)
	 )
////3rd setting line + buttons (4)
.addComponent(aboveLevelLine)
				.addGroup(layout.createParallelGroup()
    .addComponent(selectLevelLabel) //levelSelectLabel
    .addComponent(selectLevelComboBox) //selectLevel
	// .addComponent(selectLevelButton)
  )
  .addComponent(belowLevelLine)
	.addGroup(layout.createParallelGroup()
	   .addComponent(levelViewer) //levelViewer ()
		 .addGroup(layout.createSequentialGroup()
     .addGroup(layout.createParallelGroup()
						.addComponent(currentRowLabel)
						.addComponent(currentRowTextField)
						.addComponent(newRowLabel)
						.addComponent(newRowTextField)
						) //1st block positioning (4)
				.addGroup(layout.createParallelGroup()
						.addComponent(currentColumnLabel)
						.addComponent(currentColumnTextField)
						.addComponent(newColumnLabel)
						.addComponent(newColumnTextField)
						) //2nd block positioning (4)
				.addGroup(layout.createParallelGroup()
						.addComponent(removeBlockButton)
						.addComponent(moveBlockButton)
						) //buttons block positioning (2)
     .addComponent(blockSeparatorLine)
		 .addGroup(layout.createParallelGroup()
				.addGroup(layout.createParallelGroup()
     .addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(upBlockButton)
										.addComponent(downBlockButton)
										)
								.addComponent(blockVisualizer)
								)
						) //blockVisualizer
						.addComponent(visualizationSeperator)
     .addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(blockRedLabel)
						.addComponent(blockRedField)
						) //RED (2)
				.addGroup(layout.createParallelGroup()
						.addComponent(blockBlueLabel)
						.addComponent(blockBlueField)
						) //Blue (2)
				.addGroup(layout.createParallelGroup()
						.addComponent(blockGreenLabel)
						.addComponent(blockGreenField)
						) //green (2)
       //1st points labels
				.addGroup(layout.createParallelGroup()
						.addComponent(blockPointsLabel)
						.addComponent(blockPointsField)
						) //points (2)
				.addComponent(addBlockButton) //addBlockGameButton
				.addGroup(layout.createParallelGroup()
						.addComponent(blockIdLabel)
   		  .addComponent(blockIdTextField)
						) //id (2)
				.addGroup(layout.createParallelGroup()
       .addComponent(updateBlock) //updateBlockButton
						.addComponent(deleteBlockButton)
						)
     .addGroup(layout.createParallelGroup()
.addComponent(rowPositionLabel) //rowpositionLabel
.addComponent(rowPositionField)
          ) //row (2)
     .addGroup(layout.createParallelGroup()
.addComponent(columnPositionLabel) //columnPositionLabel
.addComponent(columnPositionField) //columnPositionLabel
) //column (2)
				.addComponent(addBlockLevelButton) //addBlockLevelButton
      )
			)
			)
      )
			)
				);

		pack();
	}




	private void refreshData() {

		// error
		errorMessage.setText(error);
		successMessage.setText(success);
		success = "";
		if (error == null || error.length() == 0) {
			// populate page with data
			TOGame currentGame = null;
			try {
				currentGame = Block223Controller.getCurrentDesignableGame();
			}
			catch(Exception e) {
				error = e.getMessage();
			  errorMessage.setText(error);
			}

			if (currentGame == null || selectGameComboBox.getSelectedItem()!= null && currentGame.getName() != selectGameComboBox.getSelectedItem().toString()) {
				try {
					selectGameActionPerformed();
					currentGame = Block223Controller.getCurrentDesignableGame();

				} catch(InvalidInputException e) {
					error = e.getMessage();
				}
			}

			if (currentGame == null) {
				gameNameTextField.setText("");
				nrLevelsField.setText("");
				nrBlocksPerLevelField.setText("");
				minBallSpeedXField.setText("");
				minBallSpeedYField.setText("");
				ballSpeedIncreaseFactorField.setText("");
				maxPaddleLengthField.setText("");
				minPaddleLengthField.setText("");
			}
			else{
				gameNameTextField.setText(currentGame.getName());
				nrLevelsField.setText(""+currentGame.getNrLevels());
				nrBlocksPerLevelField.setText(""+currentGame.getNrBlocksPerLevel());
				minBallSpeedXField.setText(""+currentGame.getMinBallSpeedX());
				minBallSpeedYField.setText(""+currentGame.getMinBallSpeedY());
				ballSpeedIncreaseFactorField.setText(""+currentGame.getBallSpeedIncreaseFactor());
				maxPaddleLengthField.setText(""+currentGame.getMaxPaddleLength());
				minPaddleLengthField.setText(""+currentGame.getMinPaddleLength());


				refreshBlockVisualizer();
				refreshLevelViewer();
			}
			currentRowTextField.setText(newRowTextField.getText());
			currentColumnTextField.setText(newColumnTextField.getText());
			newRowTextField.setText("");
			newColumnTextField.setText("");
			rowPositionField.setText("");
			columnPositionField.setText("");
		}
		pack();
		error = "";
}

	private void selectGameActionPerformed() throws InvalidInputException {
		error = "";
		TOGame currentGame = null;
		if (selectGameComboBox != null && selectGameComboBox.getSelectedItem() != null) {
		try {
		String name = selectGameComboBox.getSelectedItem().toString();
		Block223Controller.selectGame(name);
		currentGame = Block223Controller.getCurrentDesignableGame();
		selectLevelComboBox.removeAllItems();
		for (int i = 1; i <= currentGame.getNrLevels(); i++) {
			selectLevelComboBox.addItem(i);
		}
		if (currentGame.getNrLevels()>0) {
			selectLevelComboBox.setSelectedIndex(0);
		}
		else selectLevelComboBox.setSelectedIndex(-1);
		} catch(InvalidInputException e) {
			throw e;
		}
		}
	}



	private void saveButtonActionPerformed(ActionEvent evt) {
		error = "";
		try {
			Block223Controller.saveGame();
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
	}

	private void deleteBlockButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// clear error message and basic input validation
		error = "";

		try {
			Block223Controller.deleteBlock(Integer.parseInt(blockIdTextField.getText()));
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		// update visuals
		refreshData();
	}

	private void moveBlockButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		try {
			Block223Controller.moveBlock(selectLevelComboBox.getSelectedIndex()+1,
					Integer.parseInt(currentColumnTextField.getText()),
					Integer.parseInt(currentRowTextField.getText()),
					Integer.parseInt(newColumnTextField.getText()),
					Integer.parseInt(newRowTextField.getText()));
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		refreshData();
	}

	private void removeBlockButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		try {
			Block223Controller.removeBlock(selectLevelComboBox.getSelectedIndex()+1,
					Integer.parseInt(currentColumnTextField.getText()),
					Integer.parseInt(currentRowTextField.getText()));
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		refreshData();
	}



	private void addBlockActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		try {
	      Block223Controller.addBlock(//GETLEVEL,
					Integer.parseInt(blockRedField.getText()),
					Integer.parseInt(blockGreenField.getText()),
					Integer.parseInt(blockBlueField.getText()),
					Integer.parseInt(blockPointsField.getText()));
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		refreshData();
	}
	private void updateGameButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		String newName = gameNameTextField.getText();
		try {
	      Block223Controller.updateGame(//GETLEVEL,
					(gameNameTextField.getText()),
					Integer.parseInt(nrLevelsField.getText()),
					Integer.parseInt(nrBlocksPerLevelField.getText()),
					Integer.parseInt(minBallSpeedXField.getText()),
					Integer.parseInt(minBallSpeedYField.getText()),
					Double.parseDouble(ballSpeedIncreaseFactorField.getText()),
					Integer.parseInt(maxPaddleLengthField.getText()),
					Integer.parseInt(minPaddleLengthField.getText()));




		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		updateGamelist();
		selectGameComboBox.setSelectedItem(newName);
		try {
		selectGameActionPerformed();
		}
		catch(InvalidInputException e) {
			error = e.getMessage();
		}
//		refreshData();
	}


	private void upBlockButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		blockVisualizer.moveUp();
	}

	private void downBlockButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		blockVisualizer.moveDown();
	}
	private void createGameActionPerformed(ActionEvent evt) {
		// clear error message and basic input validation
		error = "";
		try {
			Block223Controller.createGame(gameNameTextField.getText());
			updateGamelist();
		} catch (InvalidInputException e) {
			error =	e.getMessage();
		}
		refreshData(); //refreshes UI

	}
	private void deleteGameActionPerformed(ActionEvent evt) {
		// clear error message and basic input validation
		error = "";

		try{
			Block223Controller.deleteGame(gameAvailable.get(selectGameComboBox.getSelectedIndex()));
			updateGamelist();
		}
		catch(InvalidInputException e) {
			error = e.getMessage();
		}
		refreshData(); //refreshes UI

	}

	private void addBlockToLevelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		try {
		      Block223Controller.positionBlock(//GETLEVEL,
		        Integer.parseInt(blockIdTextField.getText()),
					selectLevelComboBox.getSelectedIndex()+1,
					Integer.parseInt(columnPositionField.getText()),
					Integer.parseInt(rowPositionField.getText()));
		}
		catch (Exception e) {
			error = e.getMessage();
		}
		refreshData();
	}

	private void updateGamelist(){
		try {
			gameAvailable = new HashMap<Integer, String>();
			Integer index = 0;
			selectGameComboBox.removeAllItems();
			for (TOGame game : Block223Controller.getDesignableGames()) {
				gameAvailable.put(index, game.getName());
				selectGameComboBox.addItem(game.getName());
				index++;
			};
			selectGameComboBox.setSelectedIndex(-1);

		} catch(Exception e) {
			error = e.getMessage();
		}
//		refreshData();
	}

	private void updateBlockActionPerformed(ActionEvent evt) {
		error = "";
		try {
		Block223Controller.updateBlock(//GET LEVEL
			Integer.parseInt(blockIdTextField.getText()),
					Integer.parseInt(blockRedField.getText()),
					Integer.parseInt(blockGreenField.getText()),
					Integer.parseInt(blockBlueField.getText()),
					Integer.parseInt(blockPointsField.getText()));
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		refreshData();
	}

	private void defineGameButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		try {
				Block223Controller.setGameDetails(//GETLEVEL,
					Integer.parseInt(nrLevelsField.getText()),
					Integer.parseInt(nrBlocksPerLevelField.getText()),
					Integer.parseInt(minBallSpeedXField.getText()),
					Integer.parseInt(minBallSpeedYField.getText()),
					Double.parseDouble(ballSpeedIncreaseFactorField.getText()),
					Integer.parseInt(maxPaddleLengthField.getText()),
					Integer.parseInt(minPaddleLengthField.getText()));

		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		refreshData();
	}

	private void testGame(){
		String name =(String) selectGameComboBox.getSelectedItem();
		error = "";
		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				try {
//					Block223Controller.selectPlayableGame(name, 1);
					Block223Controller.testGame(new Block223PlayPage(Block223Page.this));
				} catch (InvalidInputException e) {
				}
			}
		};
		Thread t2 = new Thread(r2);
		t2.start();
	}

	private void playGame(){
		String name =(String) selectGameComboBox.getSelectedItem();
		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				try {
					Block223Controller.selectPlayableGame(name, playableGamesMap.get(selectGameComboBox.getSelectedIndex()));
					Block223Controller.startGame(new Block223PlayPage(Block223Page.this));
					// button.setVisible(true);
				} catch (InvalidInputException e) {
				}
			}
		};
		t2 = new Thread(r2);
		t2.start();


//		} catch (InvalidInputException e) {
//			error = e.getMessage();
//		}
		// this.setState(Frame.NORMAL);
	}

	private void resetDesign(){
		this.remove(errorMessage);
		this.remove(successMessage);
		//login/logout
		this.remove(logoutButton);
		this.remove(publishGameButton);
		this.remove(testGameButton);
		//save
		this.remove(saveButton);

		//game

		// this.remove(selectGameButton);
		this.remove(createGame);
		this.remove(defineGame);
		this.remove(deleteGame);

		this.remove(gameNameTextField);
		this.remove(selectGameComboBox);
		this.remove(selectGameLabel);
		//game settings



		//level
		// this.remove(selectLevelButton);
		this.remove(selectLevelLabel);
		this.remove(selectLevelComboBox);
		//blocks
		this.remove(blockIdTextField);
		this.remove(deleteBlockButton);

		this.remove(blockIdField);
		this.remove(rowPositionField);
		this.remove(columnPositionField);


		this.remove(blockRangeLabel);
		this.remove(rowPositionLabel);
		this.remove(blockIdLabel);
		this.remove(columnPositionLabel);
		this.remove(addBlockLevelButton);
		this.remove(updateBlock);


		this.remove(downBlockButton);
		this.remove(upBlockButton);
		this.remove(blockVisualizer);
		this.remove(levelViewer);

		//block positioning declaration
		this.remove(currentRowTextField);
		this.remove(currentColumnTextField);
		this.remove(newRowTextField);
		this.remove(newColumnTextField);
		this.remove(removeBlockButton);
		this.remove(moveBlockButton);
		this.remove(currentRowLabel);
		this.remove(currentColumnLabel);
		this.remove(newRowLabel);
		this.remove(newColumnLabel);

		//game settings declaration


		this.remove(nrLevelsField);
		this.remove(nrBlocksPerLevelField);
		this.remove(minBallSpeedXField);
		this.remove(minBallSpeedYField);
		this.remove(ballSpeedIncreaseFactorField);
		this.remove(maxPaddleLengthField);
		this.remove(minPaddleLengthField);

		this.remove(updateGameButton);
		this.remove(gameNameLabel);
		this.remove(nrLevelsLabel);
		this.remove(nrBlocksPerLevelLabel);
		this.remove(minBallSpeedXLabel);
		this.remove(minBallSpeedYLabel);
		this.remove(ballSpeedIncreaseFactorLabel);
		this.remove(maxPaddleLengthLabel);
		this.remove(minPaddleLengthLabel);


		//add block declaration
		this.remove(blockRedField);
		this.remove(blockGreenField);
		this.remove(blockBlueField);
		this.remove(blockPointsField);


		this.remove(addBlockButton);
		this.remove(blockRedLabel);
		this.remove(blockGreenLabel);
		this.remove(blockBlueLabel);
		this.remove(blockPointsLabel);
		this.remove(aboveLevelLine);
		this.remove(belowLevelLine);
		this.remove(blockSeparatorLine);
		this.remove(visualizationSeperator);
	}
	private void resetPlay(){
		this.remove(errorMessage);
		//login/logout
		this.remove(logoutButton);

		this.remove(selectGameComboBox);
		this.remove(selectGameLabel);
		this.remove(playGameButton);
	}
	private void resetLogin(){
		this.remove(errorMessage);
		//login/logout
		this.remove(txtUsername);
		this.remove(pwdAdminpassword);
		this.remove(pwdPlayerpassword);
		this.remove(loginButton);
		this.remove(create_userButton);
	}
	public void stopThread() {
		t2.stop();
	}
}
