/*
 * Copyright (c) 2015 Ruibo Chen All rights reserved.
 * Ruibo Chen PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

/**
 * Interactive map of camera locations
 * Built using JxBrowser by TeamDev
 * Map belongs to zeemaps.com/photoenforced.com
 */

public class Map extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	
	public Map() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);	
		
        Browser browser = new Browser();
        BrowserView browserView = new BrowserView(browser); //launch browser

        JFrame frame = new JFrame();
        frame.setTitle("Goodbye Cameras! - by Ruibo Chen");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.add(browserView, BorderLayout.CENTER);
        frame.setSize(1200, 900);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.loadURL("http://www.zeemaps.com/pub?group=216042&legend=1&nopdf=1&x=-73.9744&y=40.7238&z=7"); //load url of map
	}

}
