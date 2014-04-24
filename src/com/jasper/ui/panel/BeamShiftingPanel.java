/*
 * @(#)BeamShiftingPanel.java
 *
 * This program is version 2.0 of JDC Education Kit for Optical Experiments.
 * Copyright (C) Jasper Display Corporation 2013.
 * This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public
 * License version 2.0 as published by the Free Software Foundation. 

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
 * You may contact Jasper Display Corporation by email at info@jasperdisplay.com
 * or by telephone at +1-408-855-6640.
 * More information is provided at http://www.jasperdisplay.com/.
 */
package com.jasper.ui.panel;

import com.jasper.core.OpticsPane;
import java.text.DecimalFormat;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JOptionPane;
import com.jasper.core.PatternImage;
import com.jasper.ui.widget.DoubleJSlider;
import com.jasper.ui.EduLensOn11;
import com.jasper.ui.EduPatternJPanel;
import com.jasper.ui.EduPatternShowOn;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jdesktop.beansbinding.BindingGroup;

import static com.jasper.ui.EduPatternShowOn.patternFrameDoubleClick;
import static com.jasper.ui.EduPatternShowOn.patternFrame;
import javax.swing.JTextArea;
/**
 *
 * @author sonnv
 */
public class BeamShiftingPanel extends OpticsPane{
     PatternImage image1 = new PatternImage();
    ResourceBundle labels;
    private String actionTag = "BeamShifting";
    
    private JPanel panelPattern;
    private JFrame magFrameLenon;
    private double xoff = 0.0, yoff = 0.0, focal = 0.0;
    
    private javax.swing.JTabbedPane tabbedControl;
    public javax.swing.JLabel lblFocalCalibration;
    private javax.swing.JLabel lblXPosCalibration;
    private javax.swing.JLabel lblYPosCalibration;
    private DoubleJSlider jSliderXPositionCalibration;
    private DoubleJSlider jSliderYPositionCalibration;
    private javax.swing.JTextField txtXPositionCalibration;
    private javax.swing.JTextField txtYPositionCalibration;
    private javax.swing.JButton buttonLensOn;
    private javax.swing.JButton buttonDisplaySecondOn;
    private javax.swing.JButton buttonGeneral;
    
    static String logmessageCalibration = "Beam Shifting: Phy=%s Theta=%s";
    private double xoffCalibration = 0, yoffCalibration = 0;
    private int countSecondDisplayCalibration = 1;
    private int countLenOnCalibration = 1;
    private javax.swing.JPanel panelCalebration;
    private javax.swing.JTextArea txtAreaLog;
    
    public BeamShiftingPanel(ResourceBundle labels, BindingGroup bindingGroup,JPanel panelPattern, JTabbedPane tabbedControl) {
        this.labels = labels;
        this.txtAreaLog = new javax.swing.JTextArea();
        this.panelPattern = panelPattern;
        this.tabbedControl = tabbedControl;
        image1 = ((EduPatternJPanel) panelPattern).pimage;
        initComponents(bindingGroup);
    }
    
    private void initComponents(BindingGroup bindingGroup) {
        panelCalebration = new javax.swing.JPanel();
        
        lblFocalCalibration = new javax.swing.JLabel();
        lblXPosCalibration = new javax.swing.JLabel();
        lblYPosCalibration = new javax.swing.JLabel();
        txtXPositionCalibration = new javax.swing.JTextField();
        txtYPositionCalibration = new javax.swing.JTextField();
        buttonLensOn = new javax.swing.JButton();
        buttonDisplaySecondOn = new javax.swing.JButton();
        buttonGeneral = new javax.swing.JButton();
        
        lblXPosCalibration.setText("Deviation angle (degree)");
        lblYPosCalibration.setText("Rotation (degree)");
        
        buttonGeneral.setText(labels.getString("btnGenerate"));
        buttonGeneral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGenerateActionPerformedCalibration(evt);
            }
        });

        buttonLensOn.setEnabled(false);
        buttonLensOn.setText(labels.getString("btnLensOn"));
        buttonLensOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button11LensOnCalibrationActionPerformed(evt);
                countLenOnCalibration++;
                if (countLenOnCalibration % 2 == 0) {
                    buttonLensOn.setText(labels.getString("btnLensOff"));
                    panelPattern.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            patternFrameDoubleClick.show();
                        }
                        });
                } else {
                    buttonLensOn.setText(labels.getString("btnLensOn"));
                }
            }
        });

        buttonDisplaySecondOn.setEnabled(false);
        buttonDisplaySecondOn.setText(labels.getString("btnSecondDisplayOn"));
        buttonDisplaySecondOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSecondGenerateActionPerformedCalibration(evt);
                countSecondDisplayCalibration++;
                if (countSecondDisplayCalibration % 2 == 0) {
                    buttonDisplaySecondOn.setText(labels.getString("btnSecondDisplayOff"));
                } else {
                    buttonDisplaySecondOn.setText(labels.getString("btnSecondDisplayOn"));
                }
            }
        });
        
        txtXPositionCalibration.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke) {
                if(txtXPositionCalibration.getText() == null || txtXPositionCalibration.getText().equals("")){
                    lblXPosCalibration.setForeground(Color.red);
                } else {
                    lblXPosCalibration.setForeground(Color.black);
                }
                keyEventGenerateActionPerformedCalibration(ke);
            }
        });

        jSliderXPositionCalibration = new DoubleJSlider(-28, 28, 1, 10);
        jSliderXPositionCalibration.setValue(0);
        txtXPositionCalibration.setText(String.valueOf(jSliderXPositionCalibration.getValue()));

        jSliderXPositionCalibration.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderGenerateActionPerformedCalibration(evt);
                DecimalFormat df = new DecimalFormat("0.####");
                txtXPositionCalibration.setText(df.format(jSliderXPositionCalibration.getScaledValue()));
            }
        });
        
        txtYPositionCalibration.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke) {
                if(txtYPositionCalibration.getText() == null || txtYPositionCalibration.getText().equals("")){
                    lblYPosCalibration.setForeground(Color.red);
                } else {
                    lblYPosCalibration.setForeground(Color.black);
                }
                keyEventGenerateActionPerformedCalibration(ke);
            }
        });

        jSliderYPositionCalibration = new DoubleJSlider(-1800, 1800, 100, 10);
        jSliderYPositionCalibration.setValue(0);
        txtYPositionCalibration.setText(String.valueOf(jSliderYPositionCalibration.getValue()));

        jSliderYPositionCalibration.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderGenerateActionPerformedCalibration(evt);
                DecimalFormat df = new DecimalFormat("0.####");
                txtYPositionCalibration.setText(df.format(jSliderYPositionCalibration.getScaledValue()));
            }
        });

        javax.swing.GroupLayout jPanelCalibrationdricalLayout = new javax.swing.GroupLayout(panelCalebration);
        panelCalebration.setLayout(jPanelCalibrationdricalLayout);
        jPanelCalibrationdricalLayout.setHorizontalGroup(
            jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCalibrationdricalLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCalibrationdricalLayout.createSequentialGroup()
                        //.addComponent(buttonCalibrationReset, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        //.addGap(18, 18, 18)
                        .addComponent(buttonGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(buttonLensOn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(buttonDisplaySecondOn, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCalibrationdricalLayout.createSequentialGroup()
                        .addGroup(jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblYPosCalibration, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                            .addComponent(lblXPosCalibration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtYPositionCalibration)
                            .addComponent(txtXPositionCalibration, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSliderXPositionCalibration, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                            .addComponent(jSliderYPositionCalibration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 0, 0))
        );
        jPanelCalibrationdricalLayout.setVerticalGroup(
            jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCalibrationdricalLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSliderXPositionCalibration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblXPosCalibration)
                        .addComponent(txtXPositionCalibration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCalibrationdricalLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblYPosCalibration)
                            .addComponent(txtYPositionCalibration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelCalibrationdricalLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jSliderYPositionCalibration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(232, 232, 232)
                .addGroup(jPanelCalibrationdricalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonLensOn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonDisplaySecondOn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
    }
    
    public JPanel getPanel() {
        return panelCalebration;
    }
    
    public JTextArea getLogArea() {
        return txtAreaLog;
    }
    
    private void buttonGenerateActionPerformedCalibration(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGenerateActionPerformedCalibration
        if (parseArguments()) {
            actionTag = "BeamShifting";
            buttonLensOn.setEnabled(true);
            buttonDisplaySecondOn.setEnabled(true);
            PatternImage image = ((EduPatternJPanel) panelPattern).pimage;
            image.updateBeamShiftingParameter(xoffCalibration, yoffCalibration);
            image.paintBeamShifting();
            EduPatternShowOn.updateLensPatternPattern(image, genLogCalibration());
            imageGenerated = true;
        }
    }//GEN-LAST:event_buttonGenerateActionPerformedCalibration

    private void button11LensOnCalibrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button11LensOnCalibrationActionPerformed
        if (parseArguments()) {
            actionTag = "BeamShifting";
            PatternImage image = ((EduPatternJPanel) panelPattern).pimage;
            image.updateBeamShiftingParameter(xoffCalibration, yoffCalibration);
            image.paintBeamShifting();
            EduPatternShowOn.updateLensPatternPattern(image, genLogCalibration());
            imageGenerated = true;

            if (countLenOnCalibration % 2 == 0) {
                magFrameLenon.dispose();
                panelPattern.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        patternFrameDoubleClick.show();
                    }
                });
            } else {
                magFrameLenon = new JFrame("1:1 Lens On");
                URL url = ClassLoader.getSystemResource("resources/jdclogo_48x48.png");
                Toolkit kit = Toolkit.getDefaultToolkit();
                Image img = kit.createImage(url);
                magFrameLenon.setIconImage(img);

                EduLensOn11 mag = new EduLensOn11(panelPattern, new Dimension(120, 120));
                magFrameLenon.getContentPane().add(mag);
                magFrameLenon.pack();
                magFrameLenon.setLocation(new Point(568, 450));
                magFrameLenon.setResizable(false);
                magFrameLenon.setVisible(true);
                magFrameLenon.setAlwaysOnTop(true);
                magFrameLenon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                magFrameLenon.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                            countLenOnCalibration--;
                            buttonLensOn.setText(labels.getString("btnLensOn"));
                            magFrameLenon.dispose();
                    }
                });
            }
        }

    }//GEN-LAST:event_button11LensOnCalibrationActionPerformed
	
	private void buttonSecondGenerateActionPerformedCalibration(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSecondGenerateActionPerformedCyllin
            if (parseArguments()) {
                actionTag = "BeamShifting";
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] devices = env.getScreenDevices();
                if (devices.length == 1) {
                    countSecondDisplayCalibration--;
                    JOptionPane.showMessageDialog(null, "No second display is found", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    PatternImage image = ((EduPatternJPanel) panelPattern).pimage;
                    image.updateBeamShiftingParameter(xoffCalibration, yoffCalibration);
                    image.paintBeamShifting();
                    EduPatternShowOn.updatePatternSecondDisplay(image, genLogCalibration());
                    //EduPatternTest.updateLensPatternPattern(image, genLog());
                    imageGenerated = true;
                    if (countSecondDisplayCalibration % 2 == 0) {
                        patternFrameDoubleClick.dispose();
                        patternFrame.dispose();
                    }
                }
            }
    }//GEN-LAST:event_buttonSecondGenerateActionPerformedCyllin

    private void sliderGenerateActionPerformedCalibration(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderGenerateActionPerformedCyllin
        actionTag = "BeamShifting";
        if (parseArguments()) {
            buttonLensOn.setEnabled(true);
            buttonDisplaySecondOn.setEnabled(true);

            PatternImage image = ((EduPatternJPanel) panelPattern).pimage;
            image.updateBeamShiftingParameter(xoffCalibration, yoffCalibration);
            image.paintBeamShifting();
            EduPatternShowOn.updateLensPatternPattern(image, genLogCalibration());
            setLog(genLogCalibration());
            imageGenerated = true;
        }
    }//GEN-LAST:event_sliderGenerateActionPerformedCyllin
    
    private void keyEventGenerateActionPerformedCalibration(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keyEventGenerateActionPerformedCalibration
        actionTag = "BeamShifting";
        if (parseArguments()) {
            PatternImage image = ((EduPatternJPanel) panelPattern).pimage;
            image.updateBeamShiftingParameter(xoffCalibration, yoffCalibration);
            image.paintBeamShifting();
            EduPatternShowOn.updateLensPatternPattern(image, genLogCalibration());
            setLog(genLogCalibration());
            imageGenerated = true;
        }
    }//GEN-LAST:event_keyEventGenerateActionPerformedCalibration
  
    private String genLogCalibration() {
        return String.format(logmessageCalibration, Double.toString(xoffCalibration), Double.toString(yoffCalibration));
    }
    
     private boolean parseArguments() {
        boolean ret = false;
        try {          
           double xoffCali = Double.valueOf(txtXPositionCalibration.getText());
            double yoffCali = Double.valueOf(txtYPositionCalibration.getText());
            this.xoffCalibration = xoffCali;
            this.yoffCalibration = yoffCali;
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    
     public void setLog(String msg) {
        txtAreaLog.append(msg + System.getProperty("line.separator"));
    }
    
     @Override
    public void updatePatternScreen() {
        PatternImage image = ((EduPatternJPanel) panelPattern).pimage;
        if (!imageGenerated) {
            image.updateLensParameter(xoff, yoff, focal);
            image.paintLens();
            imageGenerated = true;
        }
        EduPatternShowOn.updatePatternScreen(image, "");
    }

    @Override
    public void updateRegenerate() {
        PatternImage image = ((EduPatternJPanel) panelPattern).pimage;
        if (imageGenerated) {
            image.updateLensParameter(xoff, yoff, focal);
            image.paintLens();
        }
    }
}
