package Game;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
/*
this file is commented by robin

*/
class Buffer 
{
	public boolean flag=true; 
	synchronized int up(int i)
	{
		if(flag)//��С��û�����Եʱ
		{
			i-=15;
			return i;
		}
		else
		{
			try
			{
				System.out.print("�����̵߳ȴ�������");
				notify();
				this.wait();
			}
			catch(InterruptedException e){}
			
			return i;
		}
	}
	
	synchronized int down(int i)
	{
		if(!flag)//��С��û�����Եʱ
		{
			i+=15;
			return i;
		}
		else
		{
			try
			{
				System.out.print("�½��̵߳ȴ�������");
				notify();
				this.wait();
			}
			catch(InterruptedException e){}

			return i;
		}
	}
}
class Up extends Thread
{
	public Ball ball;
	public Buffer buffer;
	public int y;
	public Up(Ball ball,Buffer buffer,int y)
	{
		this.ball=ball;
		this.buffer=buffer;
		this.y=y;
	}
	@Override
	public void run()
	{
		while(true)
		{
			if(y<80)
			{
				buffer.flag=false;
				y=440;
			}
			y=buffer.up(y);
			ball.j=y;
			ball.repaint();
			try{Thread.sleep(100);}
			catch(InterruptedException e){}
		}
	}
}
class Down extends Thread
{
	private Ball ball;
	private Buffer buffer;
	public int y;
	public Down(Ball ball,Buffer buffer,int y)
	{
		this.buffer=buffer;
		this.y=y;
		this.ball=ball;
	}
	@Override
	public void run()
	{
		while(true)
		{
			if(y>440)
			{
				buffer.flag=true;
				y=65;
			}
			y=buffer.down(y);
			ball.j=y;
			ball.repaint();
			try{Thread.sleep(100);}
			catch(InterruptedException e){}
		}
	}
}
public class Ball extends JFrame
{ 
	static int i = 250; 
	static int j = 440; 
	public Ball() 
	{  
		this.setSize(500, 500); 
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	} 
	@Override
	public void paint(Graphics g) 
	{  
		super.paint(g); 
		g.setColor(Color.black);  
		g.fillOval(i, j, 60, 60);  
	} 

	public static void main(String args[]) 
	{
		Ball ball=new Ball();
		Buffer buffer=new Buffer();
		Up up=new Up(ball,buffer,440);
		up.setPriority(10);
		up.start();
		Down down=new Down(ball,buffer,65);
		down.start();
	}
}
