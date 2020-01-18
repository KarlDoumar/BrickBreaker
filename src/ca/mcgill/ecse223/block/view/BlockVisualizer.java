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
import ca.mcgill.ecse223.block.controller.TOBlock;



class BlockVisualizer extends JPanel {

  private static final long serialVersionUID = 7553525719309958506L;
  // UI elements
  private List<Rectangle2D> blockShapes = new ArrayList<Rectangle2D>();
  private static final int BLOCKSIZE = 20;
  private static final int SPACING = 10;
  private static final int MAXNUMBEROFBLOCKSSHOWN = 9;
  private static final int MIDX = 20;
  private static final int TOPY = 20;

  // data elements
  private List<TOBlock> blocks;
	private HashMap<Rectangle2D, TOBlock> rectangles;
  private int firstVisibleBlock;


  public BlockVisualizer() {
    super();
    init();
  }

  private void init(){
    rectangles = new HashMap<Rectangle2D, TOBlock>();
    firstVisibleBlock = 0;

  }


  public void moveUp() {
    if (firstVisibleBlock > 0) {
      firstVisibleBlock--;
      repaint();
      
    }
  }

  public void moveDown() {
      if (rectangles!=null && firstVisibleBlock < blocks.size() - MAXNUMBEROFBLOCKSSHOWN) {
        firstVisibleBlock++;
        repaint();
        
      }
  }


  private void doDrawing(Graphics g) throws InvalidInputException{
    Graphics2D g2d = (Graphics2D) g.create();
    BasicStroke aStroke = new BasicStroke(2);
    g2d.setStroke(aStroke);
    g2d.setColor(Color.BLACK);
    Rectangle2D boundingBox = new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight());
    g2d.draw(boundingBox);

    try {
        blocks = Block223Controller.getBlocksOfCurrentDesignableGame();
      if (blocks != null) {
        blockShapes.clear();
        rectangles.clear();
        int index = 0;
        int visibleIndex = 0;
        for (TOBlock block : blocks) {
          if (index >= firstVisibleBlock && visibleIndex < MAXNUMBEROFBLOCKSSHOWN) {
            Rectangle2D rectangle = new Rectangle2D.Float(MIDX - BLOCKSIZE / 2, TOPY - BLOCKSIZE / 2 + visibleIndex * (BLOCKSIZE + SPACING)-SPACING/2, BLOCKSIZE, BLOCKSIZE);
            blockShapes.add(rectangle);
            rectangles.put(rectangle, block);
            g2d.setColor(new Color(block.getRed(), block.getGreen(), block.getBlue()));
  					g2d.fill(rectangle);
  					g2d.setColor(Color.BLACK);
  					g2d.draw(rectangle);
            g2d.drawString(new Integer(block.getPoints()).toString()+" Points.", MIDX + BLOCKSIZE, TOPY + BLOCKSIZE / 4 + visibleIndex * (BLOCKSIZE + SPACING)-SPACING/2);
            if (block.getRed()+block.getGreen()+block.getBlue()<255*3/2) g2d.setColor(Color.WHITE);
            g2d.drawString(new Integer(block.getId()).toString(), MIDX - BLOCKSIZE / 4, TOPY + BLOCKSIZE / 4 + visibleIndex * (BLOCKSIZE + SPACING)-SPACING/2);
            visibleIndex++;
          }
          index++;
        }
      }
    } catch(InvalidInputException e) {
      if (e.getMessage().equals("A game must be selected to access its information.")) return;
      e.printStackTrace();
    }
  }

  public void refresh() {
    rectangles = new HashMap<Rectangle2D, TOBlock>();
    firstVisibleBlock = 0;
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
