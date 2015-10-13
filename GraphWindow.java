import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JFrame;

class Pixel{
	public Pixel(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public int x;
	public int y;
	
	@Override
	public boolean equals(Object other){
		Pixel otherPixel = (Pixel)other;
		if(x == otherPixel.x && y == otherPixel.y)
			return true;
		return false;
	}
};

class Point{
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	public double x;
	public double y;
	
	@Override
	public boolean equals(Object other){
		Point otherPoint = (Point)other;
		if(x == otherPoint.x && y == otherPoint.y)
			return true;
		return false;
	}
}

class GraphComponent extends JComponent {

    private static final long serialVersionUID = 1L;

    private ArrayList<Pixel> pixels;
    
    public GraphComponent( )
    {
    	pixels = new ArrayList<Pixel>( );
    }
    
    public void setPixel(int x, int y){
    	pixels.add(new Pixel(x, y));
    	repaint();
    }
    
    public boolean testPixel(int x, int y){
    	Pixel testPixel = new Pixel(x, y);
    	for(int a=0;a<pixels.size();a++){
    		if(pixels.get(a).equals(testPixel))
    			return true;
    	}
    	return false;
    }
    
    public void resetPixel(Pixel pix){
    	if(pixels.indexOf(pix) != -1)
    		pixels.remove(pixels.indexOf(pix));
    	repaint();
    }
    
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(232, 160);
    }

    @Override
    public void paintComponent(Graphics g) {
    	try {
    		super.paintComponent(g);
    		Graphics2D g2d = (Graphics2D)g;
    		g2d.setColor(Color.black);
    		Iterator<Pixel> pixelIterator = pixels.iterator( );
    		while(pixelIterator.hasNext())
    		{
    			Pixel onePixelInIterator = pixelIterator.next( );
    			g2d.fillRect(onePixelInIterator.x, onePixelInIterator.y, 2, 2);
    		}
    	}
    	catch (Exception e) {
    		
    	}
    }
}


public class GraphWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int m_iWidth;
	private int m_iHeight;
	private GraphComponent m_Graph;
	
	public GraphWindow(int w, int h) {
		super("Graph Window");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		m_iWidth = w;
		m_iHeight = h;
		m_Graph = new GraphComponent( );
	}

	public void displayWindow(){
		add(m_Graph);
		setSize(m_iWidth, m_iHeight);
		getContentPane().setPreferredSize(new Dimension(m_iWidth, m_iHeight));
		pack();
		setVisible(true);
	}
	
	public void setPixel(double x, double y){
		int iY = (int)Math.round(y);
		int iX = (int)Math.round(x);
		iY = (m_iHeight - 2 - (2 * iY));
		m_Graph.setPixel(iX * 2, iY);
	}
	
	public void setPixelSafe(double x, double y) {
		if (!this.testPixel(x, y)) {
			this.setPixel(x, y);
		}
	}
	
	public void resetPixel(double x, double y){
		int iY = (int)Math.round(y);
		int iX = (int)Math.round(x);
		iY = (m_iHeight - 2 - (2 * iY));
		m_Graph.resetPixel(new Pixel(iX * 2, iY));
	}
	
	public boolean testPixel( double x, double y ){
		int iY = (int)Math.round(y);
		int iX = (int)Math.round(x);
		iY = (m_iHeight - 2 - (2 * iY));
		return m_Graph.testPixel(iX * 2, iY);
	}
	
	public static void main(String[] args)
	{
		GraphWindow a = new GraphWindow( 262, 160 );
		a.displayWindow( );
	}
	
	public void drawLine(double x1, double y1, double x2, double y2 )
	{
		double dX = x2 - x1;
		double dY = y2 - y1;
		if(dX == 0){
			for(double a=y1;Math.abs( a - y1 ) <= Math.abs( dY );a+=(Math.signum(dY)))
				setPixelSafe(x1, a);
			return;
		}
		double err = 0;
		double dErr = Math.abs( dY / dX );
		double y = y1;
		for(double a=x1;Math.abs( a - x1 ) <= Math.abs( dX );a+=Math.signum(dX))
		{
			setPixelSafe( a, y );
			err += dErr;
			while(err >= 0.5)
			{
				setPixelSafe(a,  y);
				y += Math.signum(dY);
				err -= 1.0;
			}
		}
	}
}
