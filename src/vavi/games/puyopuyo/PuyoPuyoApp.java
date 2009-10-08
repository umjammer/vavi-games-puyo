/*
 * Copyright (c) 2009 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.puyopuyo;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * PuyoPuyoApp
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 090105 nsano initial version <br>
 */
public class PuyoPuyoApp extends Applet implements PuyoPuyo.View {

    /** */
    private PuyoPuyo.Stage stage;

    /** */
    private Image offscreenImage;
    /** */
    private Graphics ofscreenGraphics;
    /** */
    private Image wallImage;
    /** */
    private Image fieldImage;
    /** */
    private Image nextImage;
    /** */
    private Image[] images;

    /** */
    private int offScreenWidth;
    /** */
    private int offScreenHeight;
    /** */
    private AudioClip[] clips;

    /** */
    private int[] fieldLefts, fieldTops;
    /** �Ղ�̕� */
    private int puyoSize;

    /** �Q�[�����X�^�[�g���������� */
    private int startFlag;
    /** �ꎞ��~�E�X�^�[�g���� */
    private int stopFlag;

    private String state = ""; // "test";

    /** ���y�̒�~ */
    public void stopClips() {
        for (int i = 0; i < clips.length; i++) {
            clips[i].stop();
        }
    }

    /* */
    public void init() {
        // �p�����[�^���擾
        int playersCount = Integer.parseInt(getParameter("n"));
        stage = new PuyoPuyo.Stage(playersCount);
        offScreenWidth = Integer.parseInt(getParameter("w"));
        offScreenHeight = Integer.parseInt(getParameter("h"));
        stage.set = Integer.parseInt(getParameter("s"));
        stage.soundFlag = Integer.parseInt(getParameter("v"));
        stage.puyoFlag = Integer.parseInt(getParameter("p"));
        // ���z��ʂ��`
        offscreenImage = createImage(offScreenWidth, offScreenHeight);
        ofscreenGraphics = offscreenImage.getGraphics();
        // ���y�t�@�C���ǂݍ���
        clips = new AudioClip[6];
//        clips[0] = getAudioClip(getDocumentBase(), "sound/ca_at.au"); // BGM
        clips[0] = getAudioClip(getDocumentBase(), "sound/puyo_08.mid"); // BGM
        clips[1] = getAudioClip(getDocumentBase(), "sound/728.au"); // �I��
        clips[2] = getAudioClip(getDocumentBase(), "sound/pyoro22.au"); // �ړ�
        clips[3] = getAudioClip(getDocumentBase(), "sound/puu58.au"); // ��]
        clips[4] = getAudioClip(getDocumentBase(), "sound/puu47.au"); // �ςݏグ
        clips[5] = getAudioClip(getDocumentBase(), "sound/open23.au"); // �������
        // �摜�ǂݍ���
        wallImage = getImage(getDocumentBase(), "img/wall.gif");
        fieldImage = getImage(getDocumentBase(), "img/dt13.gif");
        nextImage = getImage(getDocumentBase(), "img/next.gif");
        images = new Image[12];
        images[0] = getImage(getDocumentBase(), "img/back.gif");
        images[1] = getImage(getDocumentBase(), "img/puyo/gray.gif");
        images[2] = getImage(getDocumentBase(), "img/puyo/red.gif");
        images[3] = getImage(getDocumentBase(), "img/puyo/yellow.gif");
        images[4] = getImage(getDocumentBase(), "img/puyo/blue.gif");
        images[5] = getImage(getDocumentBase(), "img/puyo/green.gif");
        images[6] = getImage(getDocumentBase(), "img/puyo/purple.gif");
        // �L�[�R�[�h�擾
        addKeyListener(controller);
        // ������
        stage.init();
        puyoSize = 16;
        startFlag = 0;
        stopFlag = 0;
    }

    /** */
    private KeyListener controller = new KeyAdapter() {
        /* */
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_R) {
                // ���Z�b�g
                stopClips();
                stage.init();
                puyoSize = 16;
                startFlag = 0;
                stopFlag = 0;
                for (int i = 0; i < stage.playersCount; i++) {
                    stage.games[i].init();
                }
                repaint();
            } else if (startFlag == 0 && keyCode == KeyEvent.VK_S) {
                // �X�^�[�g
                startFlag = 1;
                if (stage.soundFlag == 1) {
                    clips[0].loop();
                }
                for (int i = 0; i < stage.playersCount; i++) {
                    stage.games[i].start();
                }
            } else if (startFlag == 1 && stage.games[0].waitFlag == 0) {
                // �Q�[�����X�^�[�g���Ă�����

                // �X�g�b�v
                if (keyCode == KeyEvent.VK_S && stage.playersCount == 1) {
                    if (stopFlag == 0) {
                        stage.games[0].waitFlag = 1;
                        stage.games[0].sleep(PuyoPuyo.FallSpeed, "Stop");
                    } else {
                        stage.games[0].autoFall();
                    }
                    // �Q�[���ĊJ���ꎞ��~
                    if (stopFlag == 1) {
                        stopFlag = 0;
                    } else {
                        stopFlag = 1;
                    }
                    // �`��
                    repaint();
                }
                // �ړ��E��]�E�I�[�g���[�h
                if (stopFlag == 0 && stage.games[0].waitFlag == 0) {
                    if (keyCode == KeyEvent.VK_X) { // ��]
                        stage.games[0].rotate(1);
                    } else if (keyCode == KeyEvent.VK_Z) { // ��]
                        stage.games[0].rotate(2);
                    } else if (keyCode == KeyEvent.VK_LEFT) { // ���ړ�
                        stage.games[0].left();
                    } else if (keyCode == KeyEvent.VK_RIGHT) { // �E�ړ�
                        stage.games[0].right();
                    } else if (keyCode == KeyEvent.VK_DOWN) { // ���ړ�
                        stage.games[0].down();
                    } else if (keyCode == KeyEvent.VK_UP) { // ��ړ�
                        stage.games[0].up();
                    } else if (keyCode == KeyEvent.VK_SPACE) { // ��C�ɉ��ړ�
                        stage.games[0].bottom();
                    } else if (keyCode == KeyEvent.VK_A) { // �I�[�g���[�h�؂�ւ�
                        if (stage.games[0].autoFlag == 0) {
                            stage.games[0].autoFlag = 1;
                            stage.games[0].autoMove();
                        } else {
                            stage.games[0].autoFlag = 0;
                        }
                        repaint();
                    } else if (keyCode == KeyEvent.VK_R) { // repaint
                        repaint();
                    }
                }
            }
        }
    };

    /* */
    public void start() {
        // �t�H�[�J�X�����킹��
        this.requestFocus();
        // �I�u�W�F�N�g���`
        fieldLefts = new int[stage.playersCount];
        fieldTops = new int[stage.playersCount];
        for (int i = 0; i < stage.playersCount; i++) {
            stage.games[i] = new PuyoPuyo(stage, i);
            stage.games[i].setView(this);
            // �t�B�[���h�J�n�ʒu
            fieldLefts[i] = (i % 4) * ((stage.columns + 2) * puyoSize + 100);
            fieldTops[i] = (i - i % 4) / 4 * (stage.lows * puyoSize + 44);
        }
    }

    /* */
    public void stop() {
        for (int i = 0; i < stage.playersCount; i++) {
            stage.gameFlags[i] = 0;
        }
        stopClips();
    }

    /* */
    public void update(Graphics g) {
        paint(g);
    }

    /* */
    synchronized public void paint(Graphics g) {
        // �t�B�[���h��\��
        for (int n = 0; n < stage.playersCount; n++) {
            // �w�i�摜
            ofscreenGraphics.drawImage(fieldImage, fieldLefts[n], fieldTops[n], this);
            // ��
            for (int i = 2; i < stage.lows; i++) {
                ofscreenGraphics.drawImage(wallImage, fieldLefts[n], i * puyoSize + fieldTops[n], this);
                ofscreenGraphics.drawImage(wallImage, fieldLefts[n] + (stage.columns + 1) * puyoSize, i * puyoSize + fieldTops[n], this);
            }
            for (int j = 0; j < stage.columns + 2; j++) {
                ofscreenGraphics.drawImage(wallImage, fieldLefts[n] + j * puyoSize, stage.lows * puyoSize + fieldTops[n], this);
            }
            // �Ղ�
            for (int i = 2; i < stage.lows; i++) {
                for (int j = 0; j < stage.columns; j++) {
                    ofscreenGraphics.drawImage(images[0], puyoSize + j * puyoSize + fieldLefts[n], i * puyoSize + fieldTops[n], this);
                    ofscreenGraphics.drawImage(images[stage.games[n].grid[i][j]], puyoSize + j * puyoSize + fieldLefts[n], i * puyoSize + fieldTops[n], this);
                }
            }
            // �\���������
            ofscreenGraphics.drawImage(images[1], puyoSize + fieldLefts[n], puyoSize + fieldTops[n], this);
            // ����
            ofscreenGraphics.setColor(new Color(0, 0, 0));
            ofscreenGraphics.setFont(new Font("�l�r �o�S�V�b�N", Font.PLAIN, 12));
            ofscreenGraphics.drawString("Next", 137 + fieldLefts[n], 41 + fieldTops[n]); // NEXT
            ofscreenGraphics.drawString("" + stage.disturbCounts[n], 38 + fieldLefts[n], 30 + fieldTops[n]); // �������
            ofscreenGraphics.drawString(stage.games[n].overMessage, 135 + fieldLefts[n], 130 + fieldTops[n]); // �Q�[���I�[�o�[
            if (stopFlag == 1) { // Stop
                ofscreenGraphics.drawString("STOP", 135 + fieldLefts[n], 190 + fieldTops[n]);
            }
            if (state != "test") {
                ofscreenGraphics.drawString("Score: " + stage.games[n].score, 135 + fieldLefts[n], 170 + fieldTops[n]); // Score
                if (stage.games[n].autoFlag == 1) { // Auto
                    ofscreenGraphics.drawString("AUTO", 135 + fieldLefts[n], 210 + fieldTops[n]);
                }
            }
            // �A��
            if (stage.games[n].chainCount >= 1) {
                ofscreenGraphics.drawString(stage.games[n].message + "�I", 135 + fieldLefts[n], 130 + fieldTops[n]);
                ofscreenGraphics.drawString("(" + stage.games[n].chainCount + "�A��)", 135 + fieldLefts[n], 145 + fieldTops[n]);
            }
            // ���Ղ�
            if ((stage.games[n].waitFlag == 0 || stopFlag == 1) && stage.gameFlags[n] == 1) {
                if (stage.games[n].pos[0][0] > 1) {
                    ofscreenGraphics.drawImage(images[stage.games[n].puyo1], (stage.games[n].pos[0][1] + 1) * puyoSize + fieldLefts[n], (stage.games[n].pos[0][0]) * puyoSize + fieldTops[n], this);
                }
                if (stage.games[n].pos[1][0] > 1) {
                    ofscreenGraphics.drawImage(images[stage.games[n].puyo2], (stage.games[n].pos[1][1] + 1) * puyoSize + fieldLefts[n], (stage.games[n].pos[1][0]) * puyoSize + fieldTops[n], this);
                }
            }
            // NEXT�Ղ�
            ofscreenGraphics.drawImage(nextImage, 138 + fieldLefts[n], 47 + fieldTops[n], this);
            ofscreenGraphics.drawImage(images[stage.games[n].npuyo1], 143 + fieldLefts[n], 51 + fieldTops[n], this);
            ofscreenGraphics.drawImage(images[stage.games[n].npuyo2], 143 + fieldLefts[n], 67 + fieldTops[n], this);
            ofscreenGraphics.drawImage(images[stage.games[n].nnpuyo1], 159 + fieldLefts[n], 59 + fieldTops[n], this);
            ofscreenGraphics.drawImage(images[stage.games[n].nnpuyo2], 159 + fieldLefts[n], 75 + fieldTops[n], this);
            // �e�X�g�p
            if (state.equals("test")) {
                ofscreenGraphics.setColor(new Color(0, 0, 0));
                // ����
                ofscreenGraphics.drawString(stage.games[n].x, 10 + fieldLefts[n], 260 + fieldTops[n]); // "Chain=" + G[n].max_chain_num + ", " +
                // �z��
                for (int i = 0; i < stage.lows; i++) {
                    for (int j = 0; j < stage.columns; j++) {
                        if (stage.games[n].lastIgnitionLabel2[i][j] > 1) {
                            ofscreenGraphics.setColor(new Color(255, 0, 0));
                        } else if (stage.games[n].lastIgnitionLabel2[i][j] == 1) {
                            ofscreenGraphics.setColor(new Color(0, 0, 255));
                        } else {
                            ofscreenGraphics.setColor(new Color(0, 0, 0));
                        }
                        ofscreenGraphics.drawString("" + stage.games[n].lastChainLabel[i][j], 11 * j + 150 + fieldLefts[n], 11 * i + 100 + fieldTops[n]);
                    }
                }
            }
        }
        // �ꊇ�\��
        g.drawImage(offscreenImage, 0, 0, null);
    }

    /* */
    @Override
    public void play(int folder, int cn) {
        play(getDocumentBase(), "sound/chain/" + folder + "/" + cn + ".wav");
    }

    /* */
    @Override
    public void playClip(int i) {
        clips[i].play();
    }
}

/* */