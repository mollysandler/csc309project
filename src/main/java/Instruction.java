import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Riya Badadare
 */
public abstract class Instruction implements Draggable, Cloneable {
    protected PImage img;
    protected PApplet screen;
    private int xPos;
    private int yPos;
    protected int width;
    private final int height;
    private boolean isDragging;
    private int xOffset;
    private int yOffset;
    private final String skill;

    public Instruction(PApplet screen, int xPos, int yPos, PImage img, String skill){
        this.screen = screen;
        this.xPos = xPos;
        this.yPos = yPos;
        this.img = img;
        this.skill = skill;
        this.isDragging = false;
        width = img.width;
        height = img.height;
    }

    public void display(){
        screen.image(img, xPos, yPos);
    }
    public boolean isMouseOver() {
        return (((xPos < screen.mouseX) && (screen.mouseX < xPos + width))
                && ((yPos < screen.mouseY) && (screen.mouseY < yPos + height)));
    }

    // for cloning the sidebar blocks
    public Instruction clone() throws CloneNotSupportedException {
        return (Instruction) super.clone();
    }

    public boolean toSnap(Instruction b) {
        double xDiff = Math.abs(this.xPos - b.xPos);
        double yDiff = Math.abs(this.yPos - b.yPos);
        return (xDiff < 60) && (yDiff < 60);
    }
    public void drag() {
        if (isDragging) {
            xPos = screen.mouseX - xOffset;
            yPos = screen.mouseY - yOffset;
        }
    }

    public void mousePressed() {
        if(isMouseOver()){ //calculate offset for the dragging
            xOffset = Math.abs(xPos - screen.mouseX);
            yOffset = Math.abs(yPos - screen.mouseY);
            isDragging = true;
        }
    }

    public int getyPos() {
        return this.yPos;
    }
    public int getxPos() {
        return this.xPos;
    }
    public void setyPos(int newyPos) {
        this.yPos = newyPos;
    }
    public void setxPos(int newxPos) {
        this.xPos = newxPos;
    }
    public String toString() {
        return "instruction\n";
    }
    public void setIsDragging(boolean newIsDragging) {
        this.isDragging = newIsDragging;
    }
    public String getSkill() {
        return this.skill;
    }

    public void checkRunAction( int control ) {
//        System.out.println( control + ": " + this );
        if ( control != 0 ) return;
        List<String> commands = new ArrayList<>();
        commands.add( getSkill() );
        commands.add( "perform" );
        WorldData.getWorldData().setCommands(commands);
        try {
            Thread.sleep(WorldData.getWorldData().getSpeed());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        runAction();
    }
    public abstract void runAction();
}


