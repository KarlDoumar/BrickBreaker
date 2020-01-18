package ca.mcgill.ecse223.block.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOCurrentlyPlayedGame;
import ca.mcgill.ecse223.block.controller.TOHallOfFame;
import ca.mcgill.ecse223.block.model.HallOfFameEntry;

public class Block223PlayPage extends JFrame implements Block223PlayModeInterface {

	private static final long serialVersionUID = 8000587698425212261L;

	private Block223Page parent;
	// Fields
	private JLabel errorMessage;
	private JLabel block223Title = new JLabel("BLOCK223");
	private JLabel levelLabel = new JLabel("Level: ");
	private JLabel levelNumLabel = new JLabel();
	private JLabel livesLabel = new JLabel("Lives: ");
	private JLabel livesNumLabel = new JLabel();
	private JLabel scoreLabel = new JLabel("Score: ");
	private JLabel scoreNumLabel = new JLabel();
	private Font titleFont = new Font("Serif", Font.BOLD, 32);

	//
	private JPanel hofEntry = new JPanel();

	//Hall of Fame labels and buttons
	int start = 1;
	int end = 10;
	private JLabel hallOfFameTitle = new JLabel("Hall of Fame");
	private JLabel hofPage = new JLabel(start + " - " + end);
	private JButton prevHofPage = new JButton("Back");
	private JButton nextHofPage = new JButton("Next");

	//Labels needed to display values
	private JLabel entry = new JLabel();
	private JLabel noEntry = new JLabel();

//	private Font textFont = new Font("", Font.PLAIN, 12);


	private PlayArea playArea;
	private final int PLAY_AREA_SIDE = 390;
	//data elements
	private String error = "";
	private PlayListener bp;


	private Thread t1;
	/** Creates new form Block223PlayPage */
	public Block223PlayPage(Block223Page parent) {
		this.parent = parent;
		init();
		// update(this.getGraphics());
	}

	public void refresh(){
//		update(this.getGraphics());

		// error
		errorMessage.setText(error);
		if (error == null || error.length() == 0) {
			// populate page with data
			TOCurrentlyPlayedGame game;
			try {
				game = Block223Controller.getCurrentPlayableGame();

				// refresh score, lvl, lives.
				levelNumLabel.setText(String.valueOf(game.getCurrentLevel()));
				livesNumLabel.setText(String.valueOf(game.getLives()));
				scoreNumLabel.setText(String.valueOf(game.getScore()));



				if (game.getPaused()) {
//					Runnable r3 = new Runnable() {
//						@Override
//						public void run() {
//							try {
//								Thread.sleep(500L);
//							}
//							catch(InterruptedException ex)
//							{
//							   Thread.currentThread().interrupt();
//							}
//							if (takeInputs().contains(" ")){
//								try {
//									Block223Controller.startGame(Block223PlayPage.this);
//								} catch (InvalidInputException e) {
//									error = e.getMessage();
//								}
//							}
//						}
//					};
//					Thread t3 = new Thread(r3);
//					t3.start();
					playArea.pause();
					playArea.refresh();
					while(true) {
						try {
							Thread.sleep(500L);
						}
						catch(InterruptedException ex)
						{
						   Thread.currentThread().interrupt();
						}
						if (takeInputs().contains(" ")){
							try {
								playArea.pause();
								Block223Controller.startGame(Block223PlayPage.this);
								break;
							} catch (InvalidInputException e) {
								error = e.getMessage();
							}
						}

					}

				}
			} catch (InvalidInputException e1) {
				error = e1.getMessage();
				errorMessage.setText(error);
			}



			try {
				playArea.refresh();
			}catch(InvalidInputException e) {
				error = e.getMessage();
				errorMessage.setText(error);
			}
			refreshHOF();

		}
		pack();
	}

	private void prevPageButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (start != 1) {
			TOHallOfFame HoF = null;
			try {
				HoF = Block223Controller.getHallOfFame(start-10, end-10);
			} catch (InvalidInputException e) {
				// TODO Auto-generated catch block
				error = e.getMessage();
			}
			if (HoF.numberOfEntries() > 0) {
				start = start -10;
				if (HoF.numberOfEntries() < 10) {
					end = end - (end % 10);

				}
			}
			refreshHOF();
		}
	}

	private void nextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {
		TOHallOfFame HoF = null;
		try {
			HoF = Block223Controller.getHallOfFame(start + 10, end + 10);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			error = e.getMessage();
		}
		if (HoF.numberOfEntries() > 0) {
			start = start + 10;
			if (HoF.numberOfEntries() < 10) {
				end = HoF.numberOfEntries() + 10;
			}else {
				end = end + 10;
			}
			refreshHOF();
		}
	}
	/** This method is called from within the constructor to initialize the form.
	 */
	private void refreshHOF() {
		hofPage.setText(start + "-" + end);
		TOHallOfFame HoF = null;
		try {
			HoF = Block223Controller.getHallOfFame(start, end);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			error = e.getMessage();
		}
		hofEntry.removeAll();
		hofEntry.setLayout(new BoxLayout(hofEntry, BoxLayout.Y_AXIS));
		if (HoF!=null && HoF.numberOfEntries() > 0) {
		for (int i = 0; i <= HoF.numberOfEntries()-1; i++) {
			int num = i + 1;
			entry = new JLabel(num + ". " + HoF.getEntry(i).getPlayername() + "    " + HoF.getEntry(i).getScore());
			hofEntry.add(entry);
		}
		} else {
			noEntry = new JLabel("No player has played this game.");
			hofEntry.add(noEntry);
		}
	}
	private void init() {
		// global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Block 223");
		this.addListener();
		this.pack();

		//listener for HOF buttons
		prevHofPage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				prevPageButtonActionPerformed(evt);
			}
		});

		nextHofPage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				nextPageButtonActionPerformed(evt);
			}
		});


		setVisible(true);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		parent.setState(Frame.ICONIFIED);
		// elements for error message
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		//init elements
		block223Title.setFont(titleFont);
		playArea = new PlayArea();
		/*
		TOHallOfFame HoF = null;
		try {
			HoF = Block223Controller.getHallOfFame(start, end);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			error = e.getMessage();
		}
		HoF.addEntry(0, "Matthew", 100000);
		HoF.addEntry(1, "Karl", 50000);
		HoF.addEntry(2, "Isaac", 40000);
		HoF.addEntry(3, "Edward", 25000);
		HoF.addEntry(4, "Mich", 10000);
		HoF.addEntry(5, "Greg", 10);
		HoF.addEntry(6, "Matthew", 100000);
		HoF.addEntry(7, "Karl", 50000);
		HoF.addEntry(8, "Isaac", 40000);
		HoF.addEntry(9, "Edward", 25000);
		HoF.addEntry(10, "Mich", 10000);
		HoF.addEntry(11, "shawn", 10);
		HoF.addEntry(12, "laurie", 100000);
		HoF.addEntry(13, "jade", 50000);
		HoF.addEntry(14, "orion", 40000);
		HoF.addEntry(15, "jon", 25000);
		HoF.addEntry(16, "little dipper", 10000);
		HoF.addEntry(17, "drogon", 10);
		HoF.addEntry(18, "iceberg", 100000);
		HoF.addEntry(19, "potato", 50000);
		HoF.addEntry(20, "lettuce", 40000);
		HoF.addEntry(21, "cactus", 25000);
		HoF.addEntry(22, "fern", 10000);
		HoF.addEntry(23, "grass", 10);

		hofEntry.removeAll();
		hofEntry.setLayout(new BoxLayout(hofEntry, BoxLayout.Y_AXIS));
		if (HoF.numberOfEntries() > 10) {
			end = 9;
		}
		else {
			end = HoF.numberOfEntries();
		}
		if (HoF.numberOfEntries() > 0) {
		for (int i = 0; i <= end; i++) {
			int num = i + 1;
			entry = new JLabel(num + ". " + HoF.getEntry(i).getPlayername() + "    " + HoF.getEntry(i).getScore());
			hofEntry.add(entry);
		}

		} else {
			noEntry = new JLabel("No player has played this game.");
			hofEntry.add(noEntry);
		}
		try {
			hofEntry.setLayout(new BoxLayout(hofEntry, BoxLayout.Y_AXIS));
			if (HoF.numberOfEntries() > 10) {
				end = 9;
			}
			else {
				end = HoF.numberOfEntries();
			}
			if (HoF.numberOfEntries() > 0) {
			for (int i = 0; i < end; i++) {
				int num = i + 1;
				entry = new JLabel(num + ". " + HoF.getEntry(i).getPlayername() + "    " + HoF.getEntry(i).getScore());
				hofEntry.add(entry);
			}

		 }}
		 catch(Exception e){
			 noEntry = new JLabel("No player has played this game.");
			 hofEntry.add(noEntry);

		 }*/




		//layout
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		playArea.setMinimumSize(new Dimension(PLAY_AREA_SIDE, PLAY_AREA_SIDE));
		for (int i = 0; i < end-1; i++) {

		}

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(block223Title)
						.addComponent(playArea)
				)

				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
						.addComponent(levelLabel)
						.addComponent(levelNumLabel)

						.addComponent(livesLabel)
						.addComponent(livesNumLabel)

						.addComponent(scoreLabel)
						.addComponent(scoreNumLabel)
						)
						.addComponent(hallOfFameTitle)
						.addComponent(hofEntry)
						.addGroup(layout.createSequentialGroup()
							.addComponent(prevHofPage)
							.addComponent(hofPage)
							.addComponent(nextHofPage)
							)
						)
		);

		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {block223Title});

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(block223Title)
						.addComponent(levelLabel)
						.addComponent(levelNumLabel)

						.addComponent(livesLabel)
						.addComponent(livesNumLabel)

						.addComponent(scoreLabel)
						.addComponent(scoreNumLabel)
				)



				.addGroup(layout.createParallelGroup()
						.addComponent(playArea)


						.addGroup(layout.createSequentialGroup()
						.addComponent(hallOfFameTitle)
						.addComponent(hofEntry)
						.addGroup(layout.createParallelGroup()
								.addComponent(prevHofPage)
								.addComponent(hofPage)
								.addComponent(nextHofPage))
						)
				)


		);
		pack();
	}


	public String takeInputs(){
		if (bp == null) {
			return "";
		}
		return bp.takeInputs();
	}


	private void addListener() {
				// initiating a thread to start listening to keyboard inputs
				bp = new PlayListener();
				Runnable r1 = new Runnable() {
					@Override
					public void run() {
						// in the actual game, add keyListener to the game window
						Block223PlayPage.this.addKeyListener(bp);
					}
				};
				t1 = new Thread(r1);
				t1.start();
				// to be on the safe side use join to start executing thread t1 before executing
				// the next thread
				try {
					t1.join();
				} catch (InterruptedException e1) {
		}
	}

	private void reset(){
		this.removeAll();

	}
	public void endGame(){
		reset();
		String endText = "";
		JLabel endTextLabel = new JLabel(endText);
		JLabel endTitle = new JLabel("Game Over");
		JButton acceptEndButton = new JButton("Back to Menu");

		acceptEndButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				acceptEndActionPerformed();
				Block223Controller.gameOverDelete();
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup()
				.addComponent(endTitle)
				.addComponent(endTextLabel)
				.addComponent(acceptEndButton)
			)
		);

		// layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {});

		layout.setVerticalGroup(layout.createSequentialGroup()
			.addComponent(endTitle)
			.addComponent(endTextLabel)
			.addComponent(acceptEndButton)
		);
		pack();
		update(this.getGraphics());
		parent.setState(Frame.NORMAL);
		this.setVisible(false);
		Block223Controller.gameOverDelete();
		t1.stop();
		parent.stopThread();
	}

	private void acceptEndActionPerformed() {
		parent.setState(Frame.NORMAL);
		this.setVisible(false);
	}

}
