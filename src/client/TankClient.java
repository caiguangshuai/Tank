package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import entity.CommonWall;
import entity.Direction;
import entity.Element;
import entity.Group;
import entity.MetalWall;
import entity.Moveable;
import entity.River;
import entity.Tank;
import entity.Tree;

public class TankClient extends JPanel{

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private boolean redraw = true;
	
	private Tank self = new Tank(400, 500, Group.red, Direction.stop, Direction.up, this);
	private List<Element> elements = new ArrayList<Element>();
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				BeautyEyeLNFHelper.launchBeautyEyeLNF();
				BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
		        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
		        UIManager.put("RootPane.setupButtonVisible", false);
		        TankClient client = new TankClient();
		        client.initialize();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
	}
	
	public void initLayout() {// 面板内元素的布局
		for(int i=1; i<10; i++) {
			Tank t = new Tank(i * 50 + 100,60, Group.blue, Direction.stop, Direction.down, this);
			elements.add(t);
		}
		
		for (int i = 0; i < 32; i++) {
			if (i < 16) {
				elements.add(new CommonWall(220 + 20 * i, 300, this)); // 普通墙布局
				elements.add(new CommonWall(500 + 20 * i, 180, this));
				elements.add(new CommonWall(200, 400 + 20 * i, this));
				elements.add(new CommonWall(500, 400 + 20 * i, this));
			} else if (i < 32) {
				elements.add(new CommonWall(220 + 20 * (i - 16), 320, this));
				elements.add(new CommonWall(500 + 20 * (i - 16), 220, this));
				elements.add(new CommonWall(220, 400 + 20 * (i - 16), this));
				elements.add(new CommonWall(520, 400 + 20 * (i - 16), this));
			}
		}
		
		for (int i = 0; i < 4; i++) { // 树的布局
			if (i < 4) {
				elements.add(new Tree(0 + 30 * i, 360, this));
				elements.add(new Tree(220 + 30 * i, 360, this));
				elements.add(new Tree(440 + 30 * i, 360, this));
				elements.add(new Tree(660 + 30 * i, 360, this));
			}

		}
		
		for (int i = 0; i < 20; i++) { // 金属墙布局
			if (i < 10) {
				elements.add(new MetalWall(140 + 30 * i, 150, this));
				elements.add(new MetalWall(600, 400 + 20 * (i), this));
			} else if (i < 20)
				elements.add(new MetalWall(140 + 30 * (i - 10), 180, this));
			else
				elements.add(new MetalWall(500 + 30 * (i - 10), 160, this));
		}
		
		elements.add(new River(85, 100, this));
	}
	
	public JMenuBar initMenuBar() {// 初始化菜单栏
		JMenuBar menuBar = new JMenuBar();
		JMenu[] menus = new JMenu[] {
				new JMenu("游戏"),
				new JMenu("暂停/继续"),
				new JMenu("帮助"),
				new JMenu("游戏级别")
		};
		
		JMenuItem[][] menuitems = new JMenuItem[][] {
			{
				new JMenuItem("重新开始新游戏"),
				new JMenuItem("退出")
			},
			{
				new JMenuItem("暂停"),
				new JMenuItem("继续")
			},
			{
				new JMenuItem("帮助"),
				new JMenuItem("关于"),
			},
			{
				new JMenuItem("级别1"),
				new JMenuItem("级别2"),
				new JMenuItem("级别3"),
				new JMenuItem("级别4"),
			}
		};
		
		for(int i=0; i<menus.length; i++) {
			for(int j=0; j<menuitems[i].length; j++) {
				menus[i].add(menuitems[i][j]);
				menuitems[i][j].addActionListener(new ActionHandler());
			}
			menuBar.add(menus[i]);
		}
		return menuBar;
	}
	
	public void initialize() {// 设置窗体元素的属性参数和其他初始化操作
		initLayout();
		setBackground(Color.GRAY);
		setDoubleBuffered(true);
		
		JFrame frame = new JFrame("坦克大战 —— 作者：蔡小溯");
		frame.setBounds(500, 50, WIDTH, HEIGHT);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.setJMenuBar(initMenuBar());
		frame.addKeyListener(new KeyHandler());
		new Thread(new Repaint()).start();
		frame.setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {//画出所有游戏内元素
		super.paintComponent(g);
//		g.drawImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/screen.jpg")), 0, 0, null);
		
		self.draw(g);
		self.hitThey(elements);
		
		for(int i=0; i<elements.size(); i++) {
			Element e = elements.get(i);
			e.draw(g);
			if(e instanceof Moveable) {
				Moveable m = (Moveable) e;
				m.hit(self);
				m.hitThey(elements);
			}
		}

	}
	
	private class Repaint implements Runnable {
		//重画的线程体
		@Override
		public void run() {
			while(redraw) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class KeyHandler extends KeyAdapter {
		//键盘监听者
		@Override
		public void keyPressed(KeyEvent e) {
			self.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			self.keyReleased(e);
		}
		
	}
	
	public boolean add(Element e) {
		return elements.add(e);
	}
	
	public boolean remove(Element e) {
		return elements.remove(e);
	}
	
	private class ActionHandler implements ActionListener {//处理菜单的事件监听器

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			String itemText = item.getText();
			if(itemText.equals("暂停")) {
				redraw = false;
			} else if(itemText.equals("继续")) {
				redraw = true;
				new Thread(new Repaint()).start();
			} else if(itemText.equals("关于")) {
				String message = "坦克大战（Java版）\n作者：蔡广帅\n网址：www.sheuni.com";
				JOptionPane.showMessageDialog(TankClient.this,message , "关于游戏", JOptionPane.DEFAULT_OPTION);
			}
		}
		
	}

}
