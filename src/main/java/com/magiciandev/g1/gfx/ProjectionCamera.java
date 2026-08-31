package com.magiciandev.g1.gfx;

import com.magiciandev.g1.Game;
import com.magiciandev.g1.RaycasterUtils;
import com.magiciandev.g1.entity.Camera;
import com.magiciandev.g1.texture.TextureCache;

import javax.xml.soap.Text;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class ProjectionCamera {

    /**
     * Instance of the projection pane for dimensions.
     */
    private final RaycasterProjectionPanel PROJECTION_PANEL;

    /**
     * Texture (FPS) to render overtop all other objects.
     */
    private BufferedImage TEXTURE;

    private BufferedImage overlayTexture;

    /**
     * Offset from the top of the image to prevent the image from being fully exposed
     * over the y-axis.
     */
    private final int OSCILLATION_Y_OFFSET = 30;

    /**
     * Oscillation intensity level for both x and y axis. The higher this value, the wilder the bob.
     */
    private final int OSCILLATION_INTENSITY = 20;

    /**
     *
     */
    private double SPRITE_X_SCALE = 1.0f;

    /**
     *
     */
    private double SPRITE_Y_SCALE = 2.5f;

    /**
     * Current angle of the lemniscate iteration. Wraps around after 360.
     */
    private double oscillationAngle;

    public ProjectionCamera(final RaycasterProjectionPanel projectionPanel) {
        this.PROJECTION_PANEL = projectionPanel;
        this.TEXTURE = TextureCache.getImage(Game.currentAnimation);
    }

    public void draw(final Graphics2D g2) {
        this.TEXTURE = TextureCache.getImage(Game.currentAnimation);

        switch (Game.currentAnimation){
            case "fist_1.png": {
                SPRITE_X_SCALE = 1.0f;
                SPRITE_Y_SCALE = 2.5f;
                break;
            }
            case "pistolidle.png": {
                SPRITE_X_SCALE = 4.0f;
                SPRITE_Y_SCALE = 2.5f;
                break;
            }

            default: {
                SPRITE_Y_SCALE = 2.5F;
                SPRITE_X_SCALE = 1.0F;
                break;
            }
        }

        this.oscillationAngle += this.getOscillationSpeed();
        // Convert to the lemniscate coordinates.
        double x = this.OSCILLATION_INTENSITY * RaycasterUtils.cos(Math.toRadians(this.oscillationAngle));
        double y = this.OSCILLATION_INTENSITY * RaycasterUtils.sin(Math.toRadians(this.oscillationAngle * 2.f));
        double w = this.PROJECTION_PANEL.getPreferredSize().width / this.SPRITE_X_SCALE;
        double h = this.PROJECTION_PANEL.getPreferredSize().height / this.SPRITE_Y_SCALE;
        int cx = (int) (this.PROJECTION_PANEL.getPreferredSize().width / 2.f - w / 2.f);
        g2.drawImage(this.TEXTURE, (int) (x + cx),
                this.OSCILLATION_Y_OFFSET + (int) (y + this.PROJECTION_PANEL.getPreferredSize().height - h),
                (int) w, (int) h, null);

        switch(Game.precipitationCount){
            case 1:
                overlayTexture = TextureCache.getImage("blackblizzardbig.png");
                break;
            case 2:
                overlayTexture = TextureCache.getImage("blackblizzardbig1.png");
                break;
            case 3:
                overlayTexture = TextureCache.getImage("blackblizzardbig2.png");
                break;
            case 4:
                overlayTexture = TextureCache.getImage("blackblizzardbig3.png");
                break;
            default:
                overlayTexture = null;
                break;
        }

        //draw screen overlay (overlays should be passed in as 320x240 sprites)
        if(overlayTexture != null){
            g2.drawImage(overlayTexture, 0, 0, 640, 480, null);
        }
    }

    private double getOscillationSpeed() {
        Camera ca = this.PROJECTION_PANEL.getCamera();
        if (ca.isRunning()) {
            return 5;
        } else if (ca.isWalking()) {
            return 2;
        } else {
            return 1;
        }
    }
}
