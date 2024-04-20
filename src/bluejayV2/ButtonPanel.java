package bluejayV2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ButtonPanel extends JPanel {

	private Color backgroundColor;
	private Color hoverColor;
	private Color pressedColor;
	private boolean isHovered;
	private boolean isPressed;
	private JLabel a, b, c;

	public ButtonPanel(Color backgroundColor, Icon icon, String title, String description) {

		this.backgroundColor = backgroundColor;
		this.hoverColor = backgroundColor.brighter().brighter();
		this.pressedColor = new Color(200, 200, 200).brighter().brighter();
		this.isHovered = false;
		this.isPressed = false;

		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setLayout(new MigLayout("center", "[grow,fill]", "[center][center][center]"));

		a = new JLabel("", JLabel.CENTER);
		a.setIcon(icon);
		add(a, "cell 0 0,growx,aligny center");

		b = new JLabel(title, JLabel.CENTER);
		b.setFont(new Font("SansSerif", Font.BOLD, 20));
		add(b, "cell 0 1,growx,aligny top");

		c = new JLabel(description, JLabel.CENTER);
		c.setFont(new Font("SansSerif", Font.PLAIN, 15));
		add(c, "cell 0 2,growx,aligny top");

		setLabelTextColor(this, Color.white); // You can replace Color.RED with any color

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				isHovered = true;
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				isHovered = false;
				repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				isPressed = true;
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				isPressed = false;
				repaint();
			}
		});
	}

	// Method to set text color for all JLabels inside a container
	private static void setLabelTextColor(Container container, Color color) {
		for (Component component : container.getComponents()) {
			if (component instanceof JLabel) {
				JLabel label = (JLabel) component;
				label.setForeground(color);
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isPressed) {
			g.setColor(pressedColor);
		} else if (isHovered) {
			g.setColor(hoverColor);
		} else {
			g.setColor(backgroundColor);
		}
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
