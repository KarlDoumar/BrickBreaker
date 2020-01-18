package ca.mcgill.ecse223.block.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOGridCell;



class LevelViewer extends JPanel {

  private static final long serialVersionUID = 7553525719309958506L;
  // UI elements
  private List<Rectangle2D> blockShapes = new ArrayList<Rectangle2D>();
  private static final int BLOCKSIZE = 20;
  private static final int SPACING = 10;

  // data elements
  private List<TOGridCell> blockGrid;
	private HashMap<Rectangle2D, TOGridCell> rectangles;
	private int level;


  public LevelViewer() {
    super();
    init();
  }

  private void init(){
    rectangles = new HashMap<Rectangle2D, TOGridCell>();
  }

  private void doDrawing(Graphics g) throws InvalidInputException{
    Graphics2D g2d = (Graphics2D) g.create();
    BasicStroke aStroke = new BasicStroke(2);
    g2d.setStroke(aStroke);
    g2d.setColor(Color.BLACK);
    Rectangle2D boundingBox = new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight());
    g2d.draw(boundingBox);
    int spacing = 5;
    int boxSize = 22; 
    
    try {
        blockGrid = Block223Controller.getBlocksAtLevelOfCurrentDesignableGame(level);
      if (blockGrid != null) {
        blockShapes.clear();
        rectangles.clear();
        for (TOGridCell block : blockGrid) {
          Rectangle2D rectangle = new Rectangle2D.Float(30+(spacing+boxSize)*(block.getGridHorizontalPosition()-1), 8+ (block.getGridVerticalPosition()-1) * (boxSize + spacing), boxSize, boxSize);
          blockShapes.add(rectangle);
          rectangles.put(rectangle, block);
          g2d.setColor(new Color(block.getRed(), block.getGreen(), block.getBlue()));
					g2d.fill(rectangle);
					g2d.setColor(Color.BLACK);
					g2d.draw(rectangle);
          if (block.getRed()+block.getGreen()+block.getBlue()<255*3/2) g2d.setColor(Color.WHITE);
          g2d.drawString(new Integer(block.getId()).toString(), 37+(spacing+boxSize)*(block.getGridHorizontalPosition()-1), 23+ (block.getGridVerticalPosition()-1) * (boxSize + spacing));

        }
      }
    } catch(InvalidInputException e) {
      if (e.getMessage().equals("A game must be selected to access its information.")||e.getMessage().equals("A level must be selected to access its information.")||e.getMessage().equals("Level 0 does not exist for the game.")) return;
      e.printStackTrace();
    }
  }

  public void refresh(int level) {
    rectangles = new HashMap<Rectangle2D, TOGridCell>();
    this.level = level;
    repaint();
  }
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    try {
		doDrawing(g);
	} catch (InvalidInputException e) {
		e.printStackTrace();
	}
  }
}
